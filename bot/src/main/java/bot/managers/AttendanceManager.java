package bot.managers;

import bot.application.BotLogger;
import bot.application.BotNotification;
import bot.constants.UrlConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static bot.managers.CollectManager.*;
import static bot.managers.OperationsManager.*;

public class AttendanceManager {
	private static WebDriver driver;
	private static final Thread thread = Thread.currentThread();

	public static void initialize(WebDriver driver) {
		AttendanceManager.driver = driver;
	}

	public static void processAttendances() {
		LocalTime currentTime;
		BotLogger.info("Started processing attendances");

		do {
			checkAttendances();
			checkAttendances();
			currentTime = ZonedDateTime.now(ZoneId.of("Europe/Kiev"))
					.toLocalTime();

			boolean isLessonNow = checkLessonContinues(currentTime);
			long timeToSleep = isLessonNow
					? 300_000
					: getTimeToLessonStart(currentTime);

			BotLogger.info("Sleeping for " + timeToSleep / 1000 + " seconds " + (isLessonNow
					? "during lesson"
					: "to lesson start"));
			BotNotification.enableExitItem();

			try {
				thread.sleep(timeToSleep);
			} catch (InterruptedException | IllegalArgumentException e) {
				break;
			}
		} while (currentTime.compareTo(getLastLessonToday()) < 0);

		if (currentTime.compareTo(getLastLessonToday()) >= 0) {
			BotLogger.info("Processed all attendances");
		}
	}

	public static void interrupt() {
		thread.interrupt();
	}

	public static void loginIntoAccount() throws LoginException {
		driver.get(UrlConstants.LOGIN_WINDOW_URL);

		ConfigManager.sendLoginKeysToWebElements();

		driver.findElement(By.id("loginbtn"))
				.click();

		BotLogger.info("Logged into account " + ConfigManager.getValueFromConfig("username")
				.split("@")[0]);
	}

	private static void checkAttendances() {
		List<WebElement> events = collectEventsFromMainWindow();
		List<String> eventUrls = collectUrlsFromEvents(events);

		eventUrls.forEach(AttendanceManager::checkEvent);
		returnToMainWindow();
	}

	private static void checkEvent(String eventUrl) {
		try {
			driver.navigate()
					.to(eventUrl);

			List<WebElement> attendances = collectAttendancesFromEventWindow();

			attendances.forEach(AttendanceManager::checkAttendance);
		} catch (org.openqa.selenium.NoSuchElementException ignored) {
			returnToMainWindow();
		}
	}

	private static void checkAttendance(WebElement attendance) {
		attendance.click();

		String attendanceName = "\"" + findElementByCssSelector("div.page-header-headings>h1").getText() + "\"";
		String attendanceStatus = "Opening attendance " + attendanceName;
		BotLogger.info(attendanceStatus);

		WebElement sendAttendanceButton = findElementByCssSelector("td.statuscol.cell.c2.lastcol>a");
		sendAttendanceButton.click();

		WebElement sendPresentButton = findElementByCssSelector("label.form-check-inline.form-check-label");
		sendPresentButton.click();

		WebElement sendSaveButton = findElementByCssSelector("input.btn.btn-primary[value='Зберегти зміни']");
		sendSaveButton.click();

		attendanceStatus = "Checked attendance " + attendanceName;
		BotLogger.info(attendanceStatus);
		BotNotification.sendNotification(attendanceStatus, TrayIcon.MessageType.INFO);
	}

	private static void returnToMainWindow() {
		driver.navigate()
				.to(UrlConstants.MAIN_WINDOW_URL);

		try {
			if (driver.getCurrentUrl()
					.equals(UrlConstants.LOGIN_WINDOW_URL)) {
				loginIntoAccount();
			}
		} catch (LoginException ignore) {
		}
	}
}
