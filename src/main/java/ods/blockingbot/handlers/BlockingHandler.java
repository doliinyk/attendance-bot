package ods.blockingbot.handlers;

import ods.blockingbot.utils.BotLogger;
import ods.blockingbot.utils.BotTray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BlockingHandler {
	private static WebDriver driver;
	private static int sessionsBlocked = 0;
	private static boolean isWorking = true;

	public static void initialize(WebDriver driver) {
		BlockingHandler.driver = driver;
	}

	public static void handle() {
		BotLogger.info("Starting blocking sessions");
		BotTray.enableExitItem();

		do {
			block();
		} while (isWorking);

		BotLogger.info("Blocked " + sessionsBlocked + " sessions");
		logoutFromAccount();
	}

	public static void loginIntoAccount() {
		driver.get("https://eguru.tk.te.ua/login/index.php");

		ConfigHandler.sendLoginKeysToWebElements();
		driver.findElement(By.id("loginbtn"))
				.click();

		BotLogger.info("Logged into account " + ConfigHandler.getValueFromConfig("username")
				.split("@")[0]);
	}

	public static void logoutFromAccount() {
		String sessionKey = driver.findElement(By.cssSelector("input[type='hidden'][name='sesskey']"))
				.getAttribute("value");
		driver.navigate()
				.to("https://eguru.tk.te.ua/login/logout.php?sesskey=" + sessionKey);

		BotLogger.info("Logged out from account " + ConfigHandler.getValueFromConfig("username")
				.split("@")[0]);
	}

	private static void block() {
		driver.navigate()
				.to("https://eguru.tk.te.ua/report/usersessions/user.php");

		List<WebElement> sessions = driver.findElements(By.xpath("//a[contains(text(), 'Вийти')]"));

		sessions.forEach(session -> {
			try {
				String ip = session.findElement(By.xpath("./../..//a[contains(@href, 'iplookup')]")).getText();

				session.click();

				BotLogger.info("Blocked " + ++sessionsBlocked + " session from IP " + ip);
			} catch (Exception ignored) {
				// ignored
			}
		});
	}

	public static void setIsWorking(boolean isWorking) {
		BlockingHandler.isWorking = isWorking;
	}
}
