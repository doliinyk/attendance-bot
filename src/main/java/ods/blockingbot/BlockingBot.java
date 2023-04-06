package ods.blockingbot;

import ods.blockingbot.handlers.BlockingHandler;
import ods.blockingbot.handlers.ConfigHandler;
import ods.blockingbot.utils.BotDriver;
import ods.blockingbot.utils.BotLogger;
import ods.blockingbot.utils.BotTray;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.FileNotFoundException;

public class BlockingBot {
	public static void main(String[] args) {
		BotLogger.info("Starting bot");
		BotTray.sendNotification("Starting bot", TrayIcon.MessageType.INFO);

		initializeAndRunManagers();
		destroyDriver();

		BotTray.destroyTrayIcon();
	}

	private static void initializeAndRunManagers() {
		try {
			initializeManagers();

			BlockingHandler.loginIntoAccount();
			BlockingHandler.handle();

			BotLogger.info("Shutdown bot");
			BotTray.sendNotification("Shutdown bot", TrayIcon.MessageType.INFO);
		} catch (FileNotFoundException e) {
			BotLogger.error(e.getMessage());
			BotTray.sendNotification("Bot terminated. Error message contains in log", TrayIcon.MessageType.ERROR);
		}
	}

	private static void initializeManagers() throws RuntimeException, FileNotFoundException {
		WebDriver driver = BotDriver.getDriver();

		ConfigHandler.initialize(driver);
		BlockingHandler.initialize(driver);

		BotLogger.info("Managers initialized");
	}

	private static void destroyDriver() {
		BotDriver.destroyWebDriver();
	}
}