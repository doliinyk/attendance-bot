package bot.application;

import bot.managers.AttendanceManager;
import bot.managers.ConfigManager;
import bot.managers.OperationsManager;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.LoginException;

import static bot.managers.AttendanceManager.loginIntoAccount;
import static bot.managers.AttendanceManager.processAttendances;

public class Program {
	private static final WebDriver driver = BotWebDriver.getDriver();

	public static void main(String[] args) {
		initializeAndRunManagers();

		destroyBotWebDriver();
	}

	private static void initializeAndRunManagers() {
		try {
			initializeManagers();

			loginIntoAccount();
			processAttendances();
		} catch (RuntimeException | LoginException e) {
			e.printStackTrace();
		}
	}

	private static void initializeManagers() throws RuntimeException {
		ConfigManager.initialize(driver);
		OperationsManager.initialize(driver);
		AttendanceManager.initialize(driver);
	}

	private static void destroyBotWebDriver() {
		BotWebDriver.destroyWebDriver();
	}
}