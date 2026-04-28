package cgb.transfer.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
	
	public void sendBatchReport(String email, String batchRef, LocalDate date, int successfulTransfers, int failedTransfers) {
        SimpleMailMessage simpleMail = new SimpleMailMessage();

        simpleMail.setTo(email);
        simpleMail.setSubject("Rapport de traitement du lot n°" + batchRef);

        String format = "Lot n°%s du %s : %s virement(s) réussi(s), %s virement(s) en échec.";
        String content = String.format(format, batchRef, date, successfulTransfers, failedTransfers);

        simpleMail.setText(content);
        mailSender.send(simpleMail);
    }
}
