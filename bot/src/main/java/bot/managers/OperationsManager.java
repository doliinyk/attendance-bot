package bot.managers;

import bot.constants.LessonsConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.*;
import java.util.Arrays;
import java.util.List;

public class OperationsManager {
	private static WebDriver driver;

	public static void initialize(WebDriver driver) {
		OperationsManager.driver = driver;
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

	public static boolean checkLessonContinues(LocalTime currentTime) {
		return (currentTime.compareTo(LessonsConstants.FIRST_LESSON_START) > 0 && currentTime.compareTo(LessonsConstants.FIRST_LESSON_END) < 0) || (currentTime.compareTo(LessonsConstants.SECOND_LESSON_START) > 0 && currentTime.compareTo(LessonsConstants.SECOND_LESSON_END) < 0) || (currentTime.compareTo(LessonsConstants.THIRD_LESSON_START) > 0 && currentTime.compareTo(LessonsConstants.THIRD_LESSON_END) < 0) || (currentTime.compareTo(LessonsConstants.FOURTH_LESSON_START) > 0 && currentTime.compareTo(LessonsConstants.FOURTH_LESSON_END) < 0);
	}

	public static long getTimeToLessonStart(LocalTime currentTime) {
		long[] durations = {
				Duration.between(currentTime, LessonsConstants.FIRST_LESSON_START).toMillis(),
				Duration.between(currentTime, LessonsConstants.SECOND_LESSON_START).toMillis(),
				Duration.between(currentTime, LessonsConstants.THIRD_LESSON_START).toMillis(),
				Duration.between(currentTime, LessonsConstants.FOURTH_LESSON_START).toMillis()
		};

		long minimalDuration;
		minimalDuration = Arrays.stream(durations)
				.limit(getLessonsAmountToday())
				.filter(d -> d >= 0)
				.min()
				.orElse(-1);

		return minimalDuration;
	}

	public static LocalTime getLastLessonToday() {
		int lessonsAmountToday = getLessonsAmountToday();

		switch (lessonsAmountToday) {
			case 1:
				return LessonsConstants.FIRST_LESSON_END;
			case 2:
				return LessonsConstants.SECOND_LESSON_END;
			case 3:
				return LessonsConstants.THIRD_LESSON_END;
			case 4:
				return LessonsConstants.FOURTH_LESSON_END;
			default:
				return LocalTime.of(0, 0);
		}
	}

	private static int getLessonsAmountToday() {
		String currentDay = LocalDate.now()
				.getDayOfWeek()
				.toString();

		switch (currentDay) {
			case "MONDAY":
				return LessonsConstants.MONDAY_LESSONS_AMOUNT;
			case "TUESDAY":
				return LessonsConstants.TUESDAY_LESSONS_AMOUNT;
			case "WEDNESDAY":
				return LessonsConstants.WEDNESDAY_LESSONS_AMOUNT;
			case "THURSDAY":
				return LessonsConstants.THURSDAY_LESSONS_AMOUNT;
			case "FRIDAY":
				return LessonsConstants.FRIDAY_LESSONS_AMOUNT;
			default:
				return 0;
		}
	}
}
