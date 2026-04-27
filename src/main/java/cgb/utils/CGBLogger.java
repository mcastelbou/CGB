package cgb.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Classe de log de l'API CGB.
 */
public class CGBLogger {

	/**
	 * L'instance de singleton CGBLogger.
	 */
	private static CGBLogger uniqueinstance;

	/**
	 * L'instance de FileWriter.
	 */
	private FileWriter fileWriter;

	/**
	 * L'instance de PrintWriter.
	 */
	private PrintWriter printWriter;

	/**
	 * L'emplacement du fichier de logs (à partir de la racine du projet).
	 */
	private static final String LOGS = "logs/logs.txt";

	/**
	 * Constructeur de la classe CGBLogger.
	 */
	private CGBLogger() {
	}

	/**
	 * Méthode d'instanciation de la classe de logging.
	 * 
	 * @return L'instance du singleton.
	 */
	public static CGBLogger getInstance() {
		if (uniqueinstance == null) {
			uniqueinstance = new CGBLogger();
		}
		return uniqueinstance;
	}

	/**
	 * Méthode d'écriture dans le fichier de log.
	 * 
	 * @param content Le contenu du message à loguer.
	 */
	public void write(String content) {
		try {
			fileWriter = new FileWriter(LOGS, true);
			printWriter = new PrintWriter(fileWriter);

			printWriter.println(format(content));
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode de formatage des logs.
	 * 
	 * @param content Le contenu du message à loguer.
	 * @return La ligne de log à inscrire dans le fichier de log.
	 */
	private String format(String content) {
		String format = "%s | %s.";
		String response = String.format(format, LocalDateTime.now(), content);
		return response;
	}
}
