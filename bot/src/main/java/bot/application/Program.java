package bot.application;

import bot.managers.AttendanceManager;
import bot.managers.ConfigManager;
import bot.managers.OperationsManager;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.LoginException;
import java.awt.*;

import static bot.managers.AttendanceManager.*;

public class Program {
	private static final WebDriver driver = BotWebDriver.getDriver();

	public static void main(String[] args) {
		BotLogger.info("Starting bot");
		BotNotification.sendNotification("Starting bot", TrayIcon.MessageType.INFO);

		initializeAndRunManagers();
		destroyBotWebDriver();

		BotNotification.destroyTrayIcon();
	}

	private static void initializeAndRunManagers() {
		try {
			initializeManagers();

			loginIntoAccount();
			processAttendances();

			BotLogger.info("Shutdown bot");
			BotNotification.sendNotification("Shutdown bot", TrayIcon.MessageType.INFO);
		} catch (RuntimeException | LoginException e) {
			BotLogger.error(e.getMessage());
			BotNotification.sendNotification("Bot terminated. Error message contains in log", TrayIcon.MessageType.ERROR);
		}
	}

	private static void initializeManagers() throws RuntimeException {
		ConfigManager.initialize(driver);
		OperationsManager.initialize(driver);
		AttendanceManager.initialize(driver);
		BotLogger.info("Managers initialized");
	}

	private static void destroyBotWebDriver() {
		BotWebDriver.destroyWebDriver();
	}
}