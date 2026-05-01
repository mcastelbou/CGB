package cgb.transfer.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service de gestion de la notification des utilisateurs lorsqu'un virement
 * rencontre une erreur.
 */
@Service
public class MailService {

	/**
	 * Lien vers le système d'envoie par protocle SMTP.
	 */
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Méthode d'envoi de mail standard de la dépendance JavaMailSender.
	 * 
	 * @param email               L'adresse mail du destinataire.
	 * @param batchRef            La référence du lot de virement dans lequel une
	 *                            erreur s'est produite.
	 * @param date                La date de création du lot en question.
	 * @param successfulTransfers Le nombre de virements qui ont été réussis.
	 * @param failedTransfers     Le nombre de virements en échec.
	 */
	public void sendBatchReport(String email, String batchRef, LocalDate date, int successfulTransfers,
			int failedTransfers) {
		SimpleMailMessage simpleMail = new SimpleMailMessage();

		simpleMail.setTo(email);
		simpleMail.setSubject("Rapport de traitement du lot n°" + batchRef);

		String format = "Lot n°%s du %s : %s virement(s) réussi(s), %s virement(s) en échec.";
		String content = String.format(format, batchRef, date, successfulTransfers, failedTransfers);

		simpleMail.setText(content);

		try {
			mailSender.send(simpleMail);
		} catch (MailException e) {
			System.out.println("Le service de mailling est actuellement hors ligne.");
		}
	}
}
