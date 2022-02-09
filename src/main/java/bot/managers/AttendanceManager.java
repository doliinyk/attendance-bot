package bot.managers;

import bot.constants.TimeConstants;
import bot.constants.UrlConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalTime;
import java.util.List;

import static bot.managers.CollectManager.*;
import static bot.managers.OperationsManager.*;

public class AttendanceManager {
	private static WebDriver driver;

	public static void initialize(WebDriver driver) {
		AttendanceManager.driver = driver;
	}

	public static void processAttendances() {
		LocalTime currentTime;

		do {
			checkAttendances();
			checkAttendances();
			currentTime = LocalTime.now();

			boolean isLessonNow = checkLessonContinues(currentTime);
			long timeToSleep = isLessonNow
					? 300_000
					: getTimeToLessonStart(currentTime);

			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				break;
			}
		} while (currentTime.compareTo(TimeConstants.FOURTH_LESSON_END) < 0);
	}

	public static void loginIntoAccount() {
		driver.get(UrlConstants.LOGIN_WINDOW_URL);

		ConfigManager.sendLoginKeysToWebElements();

		driver.findElement(By.id("loginbtn"))
				.click();
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

		WebElement sendAttendanceButton = findElementByCssSelector("td.statuscol.cell.c2.lastcol>a");
		System.out.println(sendAttendanceButton.getText());
		sendAttendanceButton.click();

		WebElement sendPresentButton = findElementByCssSelector("label.form-check-inline.form-check-label");
		sendPresentButton.click();

		WebElement sendSaveButton = findElementByCssSelector("input.btn.btn-primary[value='Зберегти зміни']");
		sendSaveButton.click();
	}

	private static void returnToMainWindow() {
		driver.navigate()
				.to(UrlConstants.MAIN_WINDOW_URL);

		if (driver.getCurrentUrl()
				.equals(UrlConstants.LOGIN_WINDOW_URL)) {
			loginIntoAccount();
		}
	}
}
