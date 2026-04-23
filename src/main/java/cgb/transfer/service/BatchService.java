package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.Batch;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.exception.CreateTransferException;
import cgb.transfer.exception.CreateTransferException.TransferFailure;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchRepository;
import cgb.transfer.repository.BatchTransferRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class BatchService {

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private BatchRepository batchRepo;

	@Autowired
	private BatchTransferRepository batchTransferRepo;

	@Transactional
	public Batch createBatch(BatchRequest batchRequest) throws CreateTransferException {
		Account sourceAccount = accountRepo.findById(batchRequest.getSourceAccount())
				.orElseThrow(() -> new CreateTransferException(TransferFailure.SOURCE_ACCOUNT_NOT_FOUND));

		Batch batch = new Batch();
		batch.setRefBatch(batchRequest.getRefBatch());
		batch.setSourceAccount(sourceAccount.getAccountNumber());
		batch.setDescription(batchRequest.getDescription());
		batch.setStartDate(LocalDate.now());
		batch.setStatus("received");

		return batchRepo.save(batch);
	}

	@Async
	@Transactional
	public void executeBatch(String batchRef, List<TransferRequest> transferRequestList) {
		List<TransferRequest> transferList = transferRequestList;
		Batch batch = batchRepo.findByRefBatch(batchRef).orElseThrow();

		try {
			for (TransferRequest transferRequest : transferList) {
				BatchTransfer newBatchTransfer = createBatchTransfer(batch, transferRequest);
				batch.addTransfer(newBatchTransfer);
			}
		} catch (/*Exception*/ CreateTransferException e) {
			// TODO log
			for (TransferRequest transferRequest : transferList) {
				fallbackBatchTransfer(batch, transferRequest);
			}
		}

		batch.setStatus("closed");
		batchRepo.save(batch);
	}

	@Transactional
	public BatchTransfer createBatchTransfer(Batch batch, TransferRequest transferRequest) throws CreateTransferException {		
		BatchTransfer batchTransfer = transferRequest.DTOtoBatchTransfer();
		batchTransfer.setBatch(batch);
		batchTransfer.setStatus("waiting");
		
		Double amount = batchTransfer.getAmount();
		
		Account sourceAccount = accountRepo.findById(batch.getSourceAccount()).get();
		Account destinationAccount = accountRepo.findById(batchTransfer.getDestinationAccount())
				.orElseThrow(() -> new CreateTransferException(TransferFailure.DESTINATION_ACCOUNT_NOT_FOUND));

		/* Pas de découvert autorisé */
		if (sourceAccount.getSolde().compareTo(amount) < 0) {
			throw new CreateTransferException(TransferFailure.INSUFFICIENT_FUNDS);
		} else if (amount < 0) {
			throw new CreateTransferException(TransferFailure.NEGATIVE_AMOUNT);
		}

		sourceAccount.setSolde(sourceAccount.getSolde() - (amount));
		destinationAccount.setSolde(destinationAccount.getSolde() + (amount));

		accountRepo.save(sourceAccount);
		accountRepo.save(destinationAccount);

		batchTransfer.setAmount(amount);
		batchTransfer.setCompletionDate(LocalDate.now());
		batchTransfer.setStatus("success");
			
		return batchTransferRepo.save(batchTransfer);

	}

	@Transactional
	public BatchTransfer fallbackBatchTransfer(Batch batch, TransferRequest transferRequest) {
		String destinationAccountNumber = transferRequest.getDestinationAccountNumber();
		String description = transferRequest.getDescription();
		Double amount = transferRequest.getAmount();

		boolean transferIsSaved = batchTransferRepo
				.findByBatchAndDestinationAccountAndDescription(batch, destinationAccountNumber, description)
				.isPresent();

		if (!transferIsSaved && amount > 0) {
			BatchTransfer batchTransfer = transferRequest.DTOtoBatchTransfer();

			batchTransfer.setBatch(batch);
			batchTransfer.setDestinationAccount(destinationAccountNumber);
			batchTransfer.setAmount(amount);
			batchTransfer.setCompletionDate(null);
			batchTransfer.setDescription(description);
			batchTransfer.setStatus("postponed");

			return batchTransferRepo.save(batchTransfer);
		}
		return null;
	}
}
