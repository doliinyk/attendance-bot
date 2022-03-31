package bot.application;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class BotWebDriver {
	private static WebDriver driver;
	private static WebDriverManager webDriverManager;
	private static MutableCapabilities options;

	static {
		initializeWebDriver();
	}

	private static void initializeWebDriver() throws RuntimeException {
		OperatingSystems operatingSystem = OperatingSystems.getCurrentOperatingSystem();

		setupDriverAndOptions(operatingSystem);
	}

	private static void setupDriverAndOptions(OperatingSystems operatingSystem) {
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
	}

	private static void setupEdgeDriver() {
		webDriverManager = WebDriverManager.edgedriver();
		webDriverManager.setup();

		options = new EdgeOptions();
		driver = new EdgeDriver((EdgeOptions) options);
	}

	private static void setupSafariDriver() {
		webDriverManager = WebDriverManager.safaridriver();
		webDriverManager.setup();

		options = new SafariOptions();
		driver = new SafariDriver((SafariOptions) options);
	}

	private static void setupFirefoxDriver() {
		webDriverManager = WebDriverManager.firefoxdriver();
		webDriverManager.setup();

		options = new FirefoxOptions();
		driver = new FirefoxDriver((FirefoxOptions) options);
	}

	public static void destroyWebDriver() {
		driver.quit();
	}

	public static WebDriver getDriver() {
		return driver;
	}
}
