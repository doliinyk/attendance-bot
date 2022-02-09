package bot.application;

import bot.constants.TimeConstants;
import bot.managers.AttendanceManager;
import bot.managers.ConfigManager;
import bot.managers.OperationsManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static bot.managers.AttendanceManager.loginIntoAccount;

public class Program {
	private static WebDriver driver;

	public static void main(String[] args) {
		initializeWebDriver();

		try {
			initializeManagers();

			loginIntoAccount();
			processAttendances();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		destroyWebDriver();
	}

	private static void initializeWebDriver() {
		WebDriverManager.chromedriver()
				.setup();

		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);
	}

	private static void initializeManagers() throws RuntimeException {
		ConfigManager.initialize(driver);
		OperationsManager.initialize(driver);
		AttendanceManager.initialize(driver);
	}

	private static void processAttendances() {
		AttendanceManager.processAttendances();
	}

	private static void destroyWebDriver() {
		driver.quit();
	}
}