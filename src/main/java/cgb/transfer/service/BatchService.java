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
	private static AccountRepository accountRepo;

	@Autowired
	private static BatchRepository batchRepo;

	@Autowired
	private static BatchTransferRepository batchTransferRepo;

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
	public void executeBatch(Batch batch, List<TransferRequest> requestList) {
		List<TransferRequest> transferList = requestList;

		for (TransferRequest transferRequest : transferList) {
			try {
				createBatchTransfer(batch, transferRequest);
			} catch (CreateTransferException e) {
				// TODO log + abort
				fallbackBatchTransfer(batch, transferRequest);
			}
		}

		batch.setStatus("closed");
		batchRepo.save(batch);
	}

	@Transactional
	public void createBatchTransfer(Batch batch, TransferRequest transferRequest) throws CreateTransferException {
		String destinationAccountNumber = transferRequest.getDestinationAccountNumber();
		String description = transferRequest.getDescription();
		Double amount = transferRequest.getAmount();

		Account sourceAccount = accountRepo.findById(batch.getSourceAccount()).get();
		Account destinationAccount = accountRepo.findById(destinationAccountNumber)
				.orElseThrow(() -> new CreateTransferException(TransferFailure.DESTINATION_ACCOUNT_NOT_FOUND));

		BatchTransfer batchTransfer = new BatchTransfer();
		batchTransfer.setBatch(batch);
		batchTransfer.setDestinationAccount(destinationAccountNumber);
		batchTransfer.setCompletionDate(LocalDate.now());
		batchTransfer.setDescription(description);
		batchTransfer.setStatus("waiting");

		/* Pas de découvert autorisé */
		if (sourceAccount.getSolde().compareTo(amount) < 0) {
			batchTransfer.setStatus("delayed");
			throw new CreateTransferException(TransferFailure.INSUFFICIENT_FUNDS);
		} else if (amount < 0) {
			throw new CreateTransferException(TransferFailure.NEGATIVE_AMOUNT);
		}

		sourceAccount.setSolde(sourceAccount.getSolde() - (amount));
		destinationAccount.setSolde(destinationAccount.getSolde() + (amount));

		accountRepo.save(sourceAccount);
		accountRepo.save(destinationAccount);

		batchTransfer.setAmount(amount);

		batchTransferRepo.save(batchTransfer);

		batch.addTransfer(batchTransfer);

		batchRepo.save(batch);

	}

	@Transactional
	public void fallbackBatchTransfer(Batch batch, TransferRequest transferRequest) {
		String destinationAccountNumber = transferRequest.getDestinationAccountNumber();
		String description = transferRequest.getDescription();
		Double amount = transferRequest.getAmount();
		String batchId = batch.getRefBatch();

		boolean transferIsSaved = batchTransferRepo
				.findWhereBatchIdAndDestinationAccountAndDescription(batchId, destinationAccountNumber, description)
				.isPresent();

		if (!transferIsSaved && amount > 0) {
			BatchTransfer batchTransfer = new BatchTransfer();

			batchTransfer.setBatch(batch);
			batchTransfer.setDestinationAccount(destinationAccountNumber);
			batchTransfer.setAmount(amount);
			batchTransfer.setCompletionDate(null);
			batchTransfer.setDescription(description);
			batchTransfer.setStatus("postponed");

			batchTransferRepo.save(batchTransfer);

			batch.addTransfer(batchTransfer);

			batchRepo.save(batch);
		}
	}
}
