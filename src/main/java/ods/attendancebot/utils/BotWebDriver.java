package ods.attendancebot.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.util.Arrays;

public class BotWebDriver {
	private static WebDriver driver;
	private static WebDriverManager webDriverManager;
	private static boolean isHidden = true;

	public static void setupDriverAndOptions(String[] args) {
		OperatingSystems operatingSystem = OperatingSystems.getCurrentOperatingSystem();
		if (Arrays.asList(args)
				.contains("visible")) {
			isHidden = false;
		}
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
		BotLogger.info("WebDriver initialized");
	}

	private static void setupEdgeDriver() {
		webDriverManager = WebDriverManager.edgedriver();
		webDriverManager.setup();

		EdgeOptions options = new EdgeOptions();
		if (isHidden) {
			options.addArguments("--headless");
		}

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
		if (isHidden) {
			options.addArguments("--headless");
		}

		driver = new FirefoxDriver();
	}

	public static void destroyWebDriver() {
		driver.quit();

		BotLogger.info("WebDriver destroyed");
	}

	public static WebDriver getDriver() {
		return driver;
	}
}
