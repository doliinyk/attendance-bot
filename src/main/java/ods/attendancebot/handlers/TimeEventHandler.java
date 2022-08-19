package ods.attendancebot.handlers;

import ods.attendancebot.constants.LessonConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.*;
import java.util.List;

public class TimeEventHandler {
	private static WebDriver driver;

	public static void initialize(WebDriver driver) {
		TimeEventHandler.driver = driver;
	}

	public static boolean filterAttendancesFromEvents(WebElement event) {
		try {
			WebElement eventDateText = findElementByCssSelector(event, "strong");

			return eventDateText.getText()
					.equals("»");
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	public static boolean filterAttendancesDayAndTime(WebElement attendance) {
		String[] dayAndTime = findElementByCssSelector(attendance, "div.date").getText()
				.split(", ");

		String day = dayAndTime[0];
		String[] time = dayAndTime[dayAndTime.length - 1].split(" » ");

		return compareAttendanceDay(day) && compareAttendanceTimes(time[0], time[1]);
	}

	private static boolean compareAttendanceDay(String day) {
		return day.equals("Today") || day.equals("Сьогодні");
	}

	private static boolean compareAttendanceTimes(String startTimeString, String endTimeString) {
		LocalTime startTime = LocalTime.parse(startTimeString);
		LocalTime endTime = LocalTime.parse(endTimeString);
		LocalTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Kiev"))
				.toLocalTime();

		return currentTime.compareTo(startTime) > 0 && currentTime.compareTo(endTime) < 0;
	}

	public static WebElement findElementByCssSelector(String cssSelector) {
		return findElementByCssSelector(driver, cssSelector);
	}

	public static WebElement findElementByCssSelector(SearchContext element, String cssSelector) {
		WebElement foundElement;
		foundElement = element.findElement(By.cssSelector(cssSelector));

		return foundElement;
	}

	public static List<WebElement> findElementsByCssSelector(String cssSelector) {
		return findElementsByCssSelector(driver, cssSelector);
	}

	public static List<WebElement> findElementsByCssSelector(SearchContext element, String cssSelector) {
		List<WebElement> foundElement;
		foundElement = element.findElements(By.cssSelector(cssSelector));

		return foundElement;
	}

	public static boolean checkLessonsContinues(LocalTime currentTime) {
		return currentTime.compareTo(getTimeWhenLessonsStart()) >= 0 && currentTime.compareTo(getTimeWhenLessonsEnd()) <= 0;
	}

	public static long getTimeToLessonsStart(LocalTime currentTime) {
		long duration = Duration.between(currentTime, getTimeWhenLessonsStart())
				.toMillis();

		return duration >= 0
				? duration
				: 0;
	}

	public static LocalTime getTimeWhenLessonsStart() {
		String currentDay = LocalDate.now()
				.getDayOfWeek()
				.toString();

		switch (currentDay) {
			case "MONDAY":
				return LessonConstants.MONDAY_LESSONS_START;
			case "TUESDAY":
				return LessonConstants.TUESDAY_LESSONS_START;
			case "WEDNESDAY":
				return LessonConstants.WEDNESDAY_LESSONS_START;
			case "THURSDAY":
				return LessonConstants.THURSDAY_LESSONS_START;
			case "FRIDAY":
				return LessonConstants.FRIDAY_LESSONS_START;
			case "SATURDAY":
				return LessonConstants.SATURDAY_LESSONS_START;
			default:
				return LocalTime.of(0, 0);
		}
	}

	public static LocalTime getTimeWhenLessonsEnd() {
		String currentDay = LocalDate.now()
				.getDayOfWeek()
				.toString();

		switch (currentDay) {
			case "MONDAY":
				return LessonConstants.MONDAY_LESSONS_END;
			case "TUESDAY":
				return LessonConstants.TUESDAY_LESSONS_END;
			case "WEDNESDAY":
				return LessonConstants.WEDNESDAY_LESSONS_END;
			case "THURSDAY":
				return LessonConstants.THURSDAY_LESSONS_END;
			case "FRIDAY":
				return LessonConstants.FRIDAY_LESSONS_END;
			case "SATURDAY":
				return LessonConstants.SATURDAY_LESSONS_END;
			default:
				return LocalTime.of(0, 0);
		}
	}
}
