package ods.attendancebot.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

public class BotDriver {
	private static WebDriver driver;
	private static WebDriverManager webDriverManager;

	static {
		OperatingSystem operatingSystem = OperatingSystem.getCurrentOperatingSystem();

		setupDriverAndOptions(operatingSystem);
	}

	public static void setupDriverAndOptions(OperatingSystem operatingSystem) {
		switch (operatingSystem) {
			case WINDOWS:
				setupEdgeDriver();
				break;
			case MACOS:
				setupSafariDriver();
				break;
			case LINUX:
				setupFirefoxDriver();
				break;
			case OTHER:
			default:
				throw new RuntimeException("Operating system not supported");
		}
		BotLogger.info("Driver initialized");
	}

	private static void setupEdgeDriver() {
		webDriverManager = WebDriverManager.edgedriver();
		webDriverManager.setup();

		EdgeOptions options = new EdgeOptions();
		options.addArguments("--headless");

		driver = new EdgeDriver(options);
	}

	private static void setupSafariDriver() {
		webDriverManager = WebDriverManager.safaridriver();
		webDriverManager.setup();

		driver = new SafariDriver();
	}

	private static void setupFirefoxDriver() {
		webDriverManager = WebDriverManager.firefoxdriver();
		webDriverManager.setup();

		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--headless");

		driver = new FirefoxDriver();
	}

	public static void destroyWebDriver() {
		driver.quit();

		BotLogger.info("Driver destroyed");
	}

	public static WebDriver getDriver() {
		return driver;
	}
}
