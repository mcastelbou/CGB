package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cgb.transfer.dto.BatchRequest;
import cgb.transfer.dto.BatchTransferRequest;
import cgb.transfer.entity.Account;
import cgb.transfer.entity.Batch;
import cgb.transfer.entity.BatchTransfer;
import cgb.transfer.exception.CreateTransferException;
import cgb.transfer.exception.CreateTransferException.TransferFailure;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.utils.CGBLogger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Classe du service de gestion des lots de virements.
 */
@Service
public class BatchService {

	/**
	 * Le lien vers le repository des comptes bancaires.
	 */
	@Autowired
	private AccountRepository accountRepo;

	/**
	 * Le lien vers le repository des lots.
	 */
	@Autowired
	private BatchRepository batchRepo;

	/**
	 * Le lien vers le repository des virements soumis par lot.
	 */
	@Autowired
	private BatchTransferRepository batchTransferRepo;

	/**
	 * L'instance de logueur.
	 */
	private CGBLogger log = CGBLogger.getInstance();

	/**
	 * Méthode de création et d'enregistrement d'un lot.
	 * 
	 * @param batchRequest Un DTO contenant les infos à enregistrer.
	 * @return L'objet BatchTransfer tel qu'enregistré en BDD.
	 * @throws CreateTransferException Erreur renvoyée lorsque le compte source
	 *                                 n'existe pas.
	 */
	@Transactional
	public Batch createBatch(BatchRequest batchRequest) throws CreateTransferException {
		Account sourceAccount = accountRepo.findById(batchRequest.getSourceAccount())
				.orElseThrow(() -> new CreateTransferException(TransferFailure.SOURCE_ACCOUNT_NOT_FOUND));

		if (batchRequest.getRefBatch() == null) {
			// Si elle n'est pas renseignée, calcul de la référence du lot tel que ('Date du
			// jour-numéro d'ordre').
			batchRequest.setRefBatch(LocalDate.now() + "-" + (batchRepo.countByStartDate(LocalDate.now()) + 1));
		}

		Batch batch = new Batch();
		batch.setRefBatch(batchRequest.getRefBatch());
		batch.setSourceAccount(sourceAccount.getAccountNumber());
		batch.setDescription(batchRequest.getDescription());
		batch.setStartDate(LocalDate.now());
		batch.setStatus("received");

		log.write("Batch created : refBatch " + batch.getRefBatch());

		return batchRepo.save(batch);
	}

	/**
	 * Méthode asynchrone de gestion de l'ajout de chaque virement au lot
	 * correspondant.
	 * 
	 * @param batchRef            La référence du lot.
	 * @param transferRequestList La liste des virements à enregistrer.
	 */
	@Async
	@Transactional
	public void executeBatch(String batchRef, List<BatchTransferRequest> transferRequestList) {
		log.write("Beginning execution for batch n°" + batchRef);
		Batch batch = batchRepo.findByRefBatch(batchRef).orElseThrow();

		for (BatchTransferRequest transferRequest : transferRequestList) {
			BatchTransfer newBatchTransfer = createBatchTransfer(batch, transferRequest);
			batch.addTransfer(newBatchTransfer);
		}

		batch.setStatus("closed");

		String[] statusFailed = { "failure", "delayed", "canceled" };
		String[] statusSuccessful = { "success" };
		int failed = batchTransferRepo.countByBatchAndStatusIn(batch, statusFailed);
		int successful = batchTransferRepo.countByBatchAndStatusIn(batch, statusSuccessful);
		log.write("End of execution for batch n°" + batchRef + "  Successful transfers : " + successful
				+ ", Failed transfers : " + failed);
		batchRepo.save(batch);
	}

	/**
	 * Méthode de création d'un virement par lot.
	 * 
	 * @param batch           Le lot auquel est lié le virement.
	 * @param transferRequest Un DTO représentant le virement à enregistrer.
	 * @return L'objet BatchTransfer tel qu'enregistré en BDD.
	 */
	@Transactional
	public BatchTransfer createBatchTransfer(Batch batch, BatchTransferRequest transferRequest) {
		Double amount = transferRequest.getAmount();

		Account sourceAccount = accountRepo.findById(batch.getSourceAccount()).get();
		Optional<Account> destAccount = accountRepo.findById(transferRequest.getDestinationAccount());

		if (!destAccount.isPresent()) {
			// Le compte destinataire n'existe pas.
			BatchTransfer invalidBatch = fallbackBatchTransfer(batch, transferRequest, "failure");
			log.write("Error during transfer n°" + invalidBatch.getId() + " : DESTINATION_ACCOUNT_NOT_FOUND");
			return invalidBatch;
		} else if (amount < 0) {
			// Le montant du virement est négatif.
			BatchTransfer invalidBatch = fallbackBatchTransfer(batch, transferRequest, "canceled");
			log.write("Error during transfer n°" + invalidBatch.getId() + " : NEGATIVE_AMOUNT");
			return invalidBatch;
		} else if (sourceAccount.getSolde().compareTo(amount) < 0) {
			// Les fonds du compte source sont insuffisants.
			BatchTransfer invalidBatch = fallbackBatchTransfer(batch, transferRequest, "delayed");
			log.write("Error during transfer n°" + invalidBatch.getId() + " : INSUFFICIENT_FUNDS");
			return invalidBatch;
		}

		Account destinationAccount = destAccount.get();
		sourceAccount.setSolde(sourceAccount.getSolde() - (amount));
		destinationAccount.setSolde(destinationAccount.getSolde() + (amount));

		accountRepo.save(sourceAccount);
		accountRepo.save(destinationAccount);

		BatchTransfer batchTransfer = transferRequest.DTOtoBatchTransfer();
		batchTransfer.setBatch(batch);
		batchTransfer.setAmount(amount);
		batchTransfer.setCompletionDate(LocalDate.now());
		batchTransfer.setStatus("success");

		return batchTransferRepo.save(batchTransfer);

	}

	/**
	 * Méthode de secours pour l'enregistrement d'un virement par lot qui contient
	 * des erreurs.
	 * 
	 * @param batch           Le lot auquel est lié le virement.
	 * @param transferRequest Un DTO représentant le virement à enregistrer.
	 * @param status          Le status sous lequel doit être enregistré le virement
	 *                        afin de déterminer la cause de l'échec.
	 * @return L'objet BatchTransfer tel qu'enregistré en BDD.
	 */
	@Transactional
	public BatchTransfer fallbackBatchTransfer(Batch batch, BatchTransferRequest transferRequest, String status) {
		BatchTransfer batchTransfer = transferRequest.DTOtoBatchTransfer();

		batchTransfer.setBatch(batch);
		batchTransfer.setCompletionDate(null);
		batchTransfer.setStatus(status);

		return batchTransferRepo.save(batchTransfer);
	}
}
