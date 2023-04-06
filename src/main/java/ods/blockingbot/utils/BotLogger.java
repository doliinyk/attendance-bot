package ods.blockingbot.utils;

import org.apache.log4j.Logger;

public class BotLogger {
	private static final Logger logger = Logger.getLogger(BotLogger.class);

	public static void info(String message) {
		logger.info(message);
	}

	public static void error(String message) {
		logger.error(message);
	}
}
