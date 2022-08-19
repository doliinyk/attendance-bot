package ods.attendancebot.handlers;

import ods.attendancebot.constants.UrlConstants;
import ods.attendancebot.utils.BotLogger;
import ods.attendancebot.utils.BotNotification;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static ods.attendancebot.handlers.CollectHandler.*;
import static ods.attendancebot.handlers.TimeEventHandler.*;

public class AttendanceHandler {
	private static WebDriver driver;
	private static final Thread thread = Thread.currentThread();
	private static int attendancesProcessed = 0;

	public static void initialize(WebDriver driver) {
		AttendanceHandler.driver = driver;
	}

	public static void handleAttendances() {
		LocalTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Kiev"))
				.toLocalTime();
		BotLogger.info("Started handling attendances");

		do {
			try {
				checkAttendances();
				currentTime = ZonedDateTime.now(ZoneId.of("Europe/Kiev"))
						.toLocalTime();

				boolean isLessonsNow = checkLessonsContinues(currentTime);
				long timeToSleep = isLessonsNow
						? 180_000
						: getTimeToLessonsStart(currentTime);

				BotLogger.info("Sleeping for " + timeToSleep / 1000 + " seconds " + (isLessonsNow
						? "during lessons"
						: "to lessons start"));
				BotNotification.enableExitItem();

				thread.sleep(timeToSleep);
			} catch (InterruptedException | IllegalArgumentException e) {
				break;
			} catch (StaleElementReferenceException ignore) {
			}
		} while (currentTime.compareTo(getTimeWhenLessonsEnd()) < 0);

		BotLogger.info("Handled " + attendancesProcessed + " attendances");
		logoutFromAccount();
	}

	public static void interrupt() {
		thread.interrupt();
	}

	public static void loginIntoAccount() throws LoginException {
		driver.get(UrlConstants.LOGIN_WINDOW_URL);

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
				.to(UrlConstants.LOGOUT_WINDOW_URL(sessionKey));

		BotLogger.info("Logged out from account " + ConfigHandler.getValueFromConfig("username")
				.split("@")[0]);
	}

	private static void checkAttendances() {
		for (int i = 0; i < 2; i++) {
			List<WebElement> events = collectEventsFromMainWindow();
			List<String> eventUrls = collectUrlsFromEvents(events);

			eventUrls.forEach(AttendanceHandler::checkEvent);
			returnToMainWindow();
		}
	}

	private static void checkEvent(String eventUrl) {
		try {
			driver.navigate()
					.to(eventUrl);

			List<WebElement> attendances = collectAttendancesFromEventWindow();

			attendances.forEach(AttendanceHandler::checkAttendance);
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

		attendanceStatus = "Checked " + ++attendancesProcessed + " attendance of " + attendanceName;
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
