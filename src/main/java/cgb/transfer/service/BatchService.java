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
import cgb.transfer.entity.Customer;
import cgb.transfer.entity.Status;
import cgb.transfer.exception.CreateTransferException;
import cgb.transfer.exception.BatchTransferException;
import cgb.transfer.exception.BatchTransferException.BatchTransferFailure;
import cgb.transfer.exception.BatchException;
import cgb.transfer.exception.BatchException.BatchFailure;
import cgb.transfer.exception.CreateTransferException.TransferFailure;
import cgb.transfer.exception.CustomerException;
import cgb.transfer.exception.CustomerException.CustomerFailure;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.BatchRepository;
import cgb.transfer.repository.BatchTransferRepository;
import cgb.transfer.repository.CustomerRepository;
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
	
	@Autowired
	private CustomerRepository customerRepo;

	/**
	 * Le lien vers le repository des virements soumis par lot.
	 */
	@Autowired
	private BatchTransferRepository batchTransferRepo;

	/**
	 * Lien vers le service de mailing.
	 */
	@Autowired
	private MailService mailing;

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
		Batch batch = batchRepo.findByRefBatch(batchRef).get();

		for (BatchTransferRequest transferRequest : transferRequestList) {
			BatchTransfer newBatchTransfer = createBatchTransfer(batch, transferRequest);
			batch.addTransfer(newBatchTransfer);
		}

		batch.setStatus("closed");

		int failed = batchTransferRepo.countByBatchAndStatusNot(batch, Status.SUCCESS.getName());
		int successful = batchTransferRepo.countByBatchAndStatus(batch, Status.SUCCESS.getName());

		log.write("End of execution for batch n°" + batchRef + "  Successful transfers : " + successful
				+ ", Failed transfers : " + failed);
		mailing.sendBatchReport("fake@mail.com"/* UserCGB.getEmail() */, batchRef, batch.getStartDate(), successful,
				failed);
		batchRepo.save(batch);
	}

	/**
	 * Méthode asynchrone de gestion du rejeu des virement par lots annulés pour
	 * fonds insuffisants.
	 * 
	 * @param oldBatch   Le lot dont certains virement ont été annulés.
	 * @param newBatchId La référence du lot nouvellement créé.
	 */
	@Async
	@Transactional
	public void replayBatch(Batch oldBatch, String newBatchId) {
		log.write("Replaying batch n°" + oldBatch.getRefBatch() + ", new batchId is " + newBatchId);
		Batch batch = batchRepo.findByRefBatch(newBatchId).get();

		List<BatchTransfer> transferList = batchTransferRepo.findByBatchAndStatus(oldBatch, Status.DELAYED.getName());

		for (BatchTransfer bt : transferList) {
			replayBatchTransfer(batch, bt.getId());
			batch.addTransfer(bt);
		}

		batch.setStatus("closed");

		int failed = batchTransferRepo.countByBatchAndStatusNot(batch, Status.SUCCESS.getName());
		int successful = batchTransferRepo.countByBatchAndStatus(batch, Status.SUCCESS.getName());

		log.write("End of replay for batch n°" + batch.getRefBatch() + "  Successful transfers : " + successful
				+ ", Failed transfers : " + failed);
		mailing.sendBatchReport("fake@mail.com"/* UserCGB.getEmail() */, batch.getRefBatch(), batch.getStartDate(),
				successful, failed);
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

		if (destAccount.isEmpty()) {
			// Le compte destinataire n'existe pas.
			BatchTransfer invalidBatchTransfer = fallbackBatchTransfer(batch, transferRequest,
					Status.FAILURE.getName());
			log.write("Error during transfer n°" + invalidBatchTransfer.getId() + " : DESTINATION_ACCOUNT_NOT_FOUND");
			return batchTransferRepo.save(invalidBatchTransfer);
		} else if (amount < 0) {
			// Le montant du virement est négatif.
			BatchTransfer invalidBatchTransfer = fallbackBatchTransfer(batch, transferRequest,
					Status.CANCELED.getName());
			log.write("Error during transfer n°" + invalidBatchTransfer.getId() + " : NEGATIVE_AMOUNT");
			return batchTransferRepo.save(invalidBatchTransfer);
		} else if (sourceAccount.getSolde().compareTo(amount) < 0) {
			// Les fonds du compte source sont insuffisants.
			BatchTransfer invalidBatchTransfer = fallbackBatchTransfer(batch, transferRequest,
					Status.DELAYED.getName());
			log.write("Error during transfer n°" + invalidBatchTransfer.getId() + " : INSUFFICIENT_FUNDS");
			return batchTransferRepo.save(invalidBatchTransfer);
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
		batchTransfer.setStatus(Status.SUCCESS.getName());

		return batchTransferRepo.save(batchTransfer);

	}

	/**
	 * Méthode permettant d'exécuter le rejeu d'un virement annulé pour fonds
	 * insuffisants.
	 * 
	 * @param batch           Le lot auquel sera ajouté le virement.
	 * @param batchTransferId L'identifiant du virement annulé pour fonds
	 *                        insuffisants.
	 * @return Le virement s'il est maintenant possible, sinon null.
	 */
	@Transactional
	public BatchTransfer replayBatchTransfer(Batch batch, Long batchTransferId) {
		BatchTransfer bt = batchTransferRepo.findById(batchTransferId).get();
		Double amount = bt.getAmount();

		Account sourceAccount = accountRepo.findById(batch.getSourceAccount()).get();
		Account destAccount = accountRepo.findById(bt.getDestinationAccount()).get();

		if (sourceAccount.getSolde().compareTo(amount) < 0) {
			// Si le compte source n'a toujours pas les fonds nécessaires on log et on sort.
			log.write("Error during replay of transfer n°" + bt.getId() + " : INSUFFICIENT_FUNDS");
			bt.setStatus(Status.CANCELED.getName());
			batchTransferRepo.save(bt);
			return null;
		}

		sourceAccount.setSolde(sourceAccount.getSolde() - (amount));
		destAccount.setSolde(destAccount.getSolde() + (amount));

		accountRepo.save(sourceAccount);
		accountRepo.save(destAccount);

		bt.setBatch(batch);
		bt.setCompletionDate(LocalDate.now());
		bt.setStatus(Status.SUCCESS.getName());

		return batchTransferRepo.save(bt);
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
		batchTransfer.setCompletionDate(LocalDate.now());
		batchTransfer.setStatus(status);

		return batchTransferRepo.save(batchTransfer);
	}

	/**
	 * Méthode de récupération d'un lot.
	 * 
	 * @param refBatch La référence du lot.
	 * @return Le lot et tous les virements qui lui sont associés.
	 * @throws BatchException Eurreur renvoyée si le lot n'existe pas dans la BDD.
	 */
	public Batch findBatch(String refBatch) throws BatchException {
		return batchRepo.findByRefBatch(refBatch).orElseThrow(() -> new BatchException(BatchFailure.BATCH_NOT_FOUND));
	}

	/**
	 * Méthode de récupération des virements par lots qui ont rencontré des erreurs
	 * par la référence du lot.
	 * 
	 * @param batchRef La référence du lot.
	 * @return La liste des virements en échec du lot, peut être vide.
	 * @throws BatchException Une erreur est levée si le lot fournit n'existe pas.
	 */
	public List<BatchTransfer> findFailedTransfersWithBatch(String batchRef) throws BatchException {
		Batch batch = batchRepo.findByRefBatch(batchRef)
				.orElseThrow(() -> new BatchException(BatchFailure.BATCH_NOT_FOUND));
		return batchTransferRepo.findByBatchAndStatusNot(batch, Status.SUCCESS.getName());
	}

	/**
	 * Méthode de récupération des virements par lots qui ont rencontré des erreurs
	 * sur un intervalle de temps donné.
	 * 
	 * @param startDateInterval La date de début de l'intervalle.
	 * @param endDateInterval   La date de fin de l'intervalle
	 * @return La liste des virements en échec sur l'intervalle donné, peut être
	 *         vide.
	 */
	public List<BatchTransfer> findFailedTransfersWithDate(LocalDate startDateInterval, LocalDate endDateInterval) {
		return batchTransferRepo.findByCompletionDateBetweenAndStatusNot(startDateInterval, endDateInterval,
				Status.SUCCESS.getName());
	}

	/**
	 * Méthode de récupération des virements par lots vers le compte destinataire
	 * donné qui ont rencontré des erreurs.
	 * 
	 * @param accountNumber L'IBAN du compte destinataire.
	 * @return La liste des virements vers le compte courant donné qui sont en
	 *         échec, peut être vide.
	 */
	public List<BatchTransfer> findFailedTransfersWithDestAccount(String accountNumber) {
		return batchTransferRepo.findByDestinationAccountAndStatusNot(accountNumber, Status.SUCCESS.getName());
	}

	/**
	 * Méthode de récupération des virements par lots qui ont été reportés par la
	 * référence du lot.
	 * 
	 * @param batchRef La référence du lot.
	 * @return La liste des virements reportés pour fonds insuffisant du lot, peut
	 *         être vide.
	 * @throws BatchException Une erreur est levée si le lot fournit n'existe pas.
	 */
	public List<BatchTransfer> findDelayedTransfersWithBatch(String batchRef) throws BatchException {
		Batch batch = batchRepo.findByRefBatch(batchRef)
				.orElseThrow(() -> new BatchException(BatchFailure.BATCH_NOT_FOUND));
		return batchTransferRepo.findByBatchAndStatus(batch, Status.DELAYED.getName());
	}
	
	public List<BatchTransfer> findFailedTransfersWithBatchAndCustomer(String refBatch, Long idCustomer) throws BatchException, CustomerException {
		Customer customer = customerRepo.findById(idCustomer).orElseThrow(() -> new CustomerException(CustomerFailure.CUSTOMER_NOT_FOUND));		
		Batch batch = batchRepo.findByRefBatch(refBatch).orElseThrow(() -> new BatchException(BatchFailure.BATCH_NOT_FOUND));
		if (customer.getMyAccounts().contains(batch.getSourceAccount())) {			
			return batchTransferRepo.findByBatchAndStatus(batch, Status.FAILURE.getName());
		} else {
			throw new CustomerException(CustomerFailure.INVALID_ACCOUNT);
		}
	}
	
	@Transactional
	public BatchTransfer closeTransfer(Long id) throws BatchTransferException {
		BatchTransfer transfer = batchTransferRepo.findById(id).orElseThrow(() -> new BatchTransferException(BatchTransferFailure.BATCH_TRANSFER_NOT_FOUND));
		transfer.setStatus(Status.CLOSED.getName());
		return batchTransferRepo.save(transfer);
	}
}
