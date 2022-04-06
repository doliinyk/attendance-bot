package bot.application;

import org.apache.log4j.Logger;

public class BotLogger {
	private static final Logger logger = Logger.getLogger(Program.class);

	public static void info(String message) {
		logger.info(message);
	}

	public static void error(String message) {
		logger.error(message);
	}
}
