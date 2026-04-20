package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.TransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.Batch;
import cgb.transfer.entity.Transfer;
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
	public Batch createBatch(BatchRequest batchRequest) {
		Batch batch = new Batch();
		
		batch.setRefBatch(batchRequest.getRefBatch());
		batch.setSourceAccount(batchRequest.getSourceAccount());
		batch.setDescription(batchRequest.getDescription());
		batch.setStartDate(LocalDate.now());
		batch.setStatus("received");
		batch.setTransferList(null /*batchRequest.getTransferList()*/);
		
		return batchRepo.save(batch);
	}

	@Async
	public void executeBatch(Batch batch) {
		List<TransferRequest> transferList = null /*batch.getTransferList()*/;		
		
		try {
			for (TransferRequest transferRequest : transferList) {
				String destAccount = transferRequest.getDestinationAccountNumber();
				Double amount = transferRequest.getAmount();
				String description = transferRequest.getDescription();
				createBatchTransfer(batch, destAccount, amount, description);
			}
		} catch (CreateTransferException e) {
			// TODO log + abort + sauvegarder les virements pas fait en état cancelled
		}
		
		batch.setStatus("closed");
		batchRepo.save(batch);
	}

	@Transactional
	public void createBatchTransfer(Batch batch, String destinationAccountNumber, Double amount,
			String description) throws CreateTransferException {
		// TODO (créer une entity BatchTransfer puis l'enregistrer dans le bon repo)
		Account sourceAccount = accountRepo.findById(batch.getSourceAccount())
				.orElseThrow(() -> new CreateTransferException(TransferFailure.SOURCE_ACCOUNT_NOT_FOUND));
		Account destinationAccount = accountRepo.findById(destinationAccountNumber)
				.orElseThrow(() -> new CreateTransferException(TransferFailure.DESTINATION_ACCOUNT_NOT_FOUND));

		/* Pas de découvert autorisé */
		if (sourceAccount.getSolde().compareTo(amount) < 0) {
			throw new CreateTransferException(TransferFailure.INSUFFICIENT_FUNDS);
		} else if (amount < 0) {
			throw new CreateTransferException(TransferFailure.NEGATIVE_AMOUNT);
		} else {

			sourceAccount.setSolde(sourceAccount.getSolde() - (amount));
			destinationAccount.setSolde(destinationAccount.getSolde() + (amount));

			accountRepo.save(sourceAccount);
			accountRepo.save(destinationAccount);

			BatchTransfer batchTransfer = new BatchTransfer();
			batchTransfer.setBatch(batch); 
			batchTransfer.setDestinationAccount(destinationAccountNumber);
			batchTransfer.setAmount(amount);
			batchTransfer.setCompletionDate(LocalDate.now());
			batchTransfer.setDescription(description); 

			batchTransferRepo.save(batchTransfer);
		}
	}
}
