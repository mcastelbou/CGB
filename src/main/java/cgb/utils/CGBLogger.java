package cgb.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class CGBLogger {

	private static CGBLogger uniqueinstance;

	private FileWriter fileWriter;
	private PrintWriter printWriter;

	private static final String LOGS = "logs/logs.txt";

	private CGBLogger() {
	}

	public static CGBLogger getInstance() {
		if (uniqueinstance == null) {
			uniqueinstance = new CGBLogger();
		}
		return uniqueinstance;
	}

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

	private String format(String content) {
		String format = "%s | %s.";
		String response = String.format(format, LocalDateTime.now(), content);
		return response;
	}
}
