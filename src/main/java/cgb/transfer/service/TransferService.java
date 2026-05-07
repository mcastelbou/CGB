package cgb.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgb.transfer.entity.Account;
import cgb.transfer.entity.Transfer;
import cgb.transfer.exception.*;
import cgb.transfer.exception.DeleteTransferException.FailureTransfert;
import cgb.transfer.exception.TransferException;
import cgb.transfer.exception.CreateTransferException.TransferFailure;
import cgb.transfer.repository.AccountRepository;
import cgb.transfer.repository.TransferRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

/**
 * La classe de Service permettant le lien entre Repository et Controller.
 */
@Service
public class TransferService {

	/**
	 * L'instance de Repository de comptes actuellement en cours.
	 */
	@Autowired
	private AccountRepository accountRepository;

	/**
	 * L'instance de Repository de tranferts actuellement en cours.
	 */
	@Autowired
	private TransferRepository transferRepository;

	/*
	 * Rappel du cours sur les transactions... Tout ou rien
	 */
	/**
	 * Fonction transactionnelle de création d'un transfert en DB. Gère
	 * l'interdiction de découvert.
	 * 
	 * @param sourceAccountNumber      L'identifiant unique du compte à l'origine du
	 *                                 transfert.
	 * @param destinationAccountNumber L'identifiant unique du compte destinataire
	 *                                 du transfert.
	 * @param amount                   Le montant du tranfert.
	 * @param transferDate             La date du tranfert.
	 * @param description              La description associé au transfert.
	 * @return L'objet transfert qui a été sauvegarder
	 * @throws RuntimeException On annule la transaction si le compte source n'a pas
	 *                          le solde suffisant.
	 */
	@Transactional
	public Transfer createTransfer(String sourceAccountNumber, String destinationAccountNumber, Double amount,
			LocalDate transferDate, String description) throws CreateTransferException {
		Account sourceAccount = accountRepository.findById(sourceAccountNumber)
				.orElseThrow(() -> new CreateTransferException(TransferFailure.SOURCE_ACCOUNT_NOT_FOUND));
		Account destinationAccount = accountRepository.findById(destinationAccountNumber)
				.orElseThrow(() -> new CreateTransferException(TransferFailure.DESTINATION_ACCOUNT_NOT_FOUND));

		/* Pas de découvert autorisé */
		if (sourceAccount.getSolde().compareTo(amount) < 0) {
			throw new CreateTransferException(TransferFailure.INSUFFICIENT_FUNDS);
		} else if (amount < 0) {
			throw new CreateTransferException(TransferFailure.NEGATIVE_AMOUNT);
		} else if (transferDate.isBefore(LocalDate.now())) {
			throw new CreateTransferException(TransferFailure.ILLEGAL_DATE);
		} else {

			sourceAccount.setSolde(sourceAccount.getSolde() - (amount));
			destinationAccount.setSolde(destinationAccount.getSolde() + (amount));

			accountRepository.save(sourceAccount);
			accountRepository.save(destinationAccount);

			Transfer transfer = new Transfer();
			transfer.setSourceAccountNumber(sourceAccountNumber);
			transfer.setDestinationAccountNumber(destinationAccountNumber);
			transfer.setAmount(amount);
			transfer.setTransferDate(transferDate);
			transfer.setDescription(description);

			return transferRepository.save(transfer);
		}

	}

	/**
	 * Fonction transactionnelle de suppression d'un transfert.
	 * 
	 * @param id L'identifiant unique du transfert que l'on veut supprimer.
	 * @return Le transfert supprimé si l'opération a réussi, sinon null.
	 * @throws DeleteTransferException
	 */
	@Transactional
	public Transfer deleteTransfer(Long id) throws DeleteTransferException {
		Optional<Transfer> otranfer = transferRepository.findById(id);
		transferRepository.deleteById(id);
		if (otranfer.isEmpty())
			throw new DeleteTransferException(FailureTransfert.OBJECT_NOT_FOUND);
		return otranfer.orElse(null);
	}
}
