package bot.managers;

import bot.constants.TimeConstants;
import bot.constants.UrlConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class OperationsManager {
	private static WebDriver driver;

	public static void initialize(WebDriver driver) {
		OperationsManager.driver = driver;
	}

	public static boolean filterAttendancesFromEvents(WebElement event) {
		try {
			WebElement eventIcon = findElementByCssSelector(event, "img.icon");
			String imageSource = eventIcon.getAttribute("src");

			return imageSource.equals(UrlConstants.ATTENDANCE_ICON_URL);
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
		LocalTime currentTime = LocalTime.now();

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
		return (currentTime.compareTo(TimeConstants.FIRST_LESSON_START) > 0 && currentTime.compareTo(TimeConstants.FIRST_LESSON_END) < 0) || (currentTime.compareTo(TimeConstants.SECOND_LESSON_START) > 0 && currentTime.compareTo(TimeConstants.SECOND_LESSON_END) < 0) || (currentTime.compareTo(TimeConstants.THIRD_LESSON_START) > 0 && currentTime.compareTo(TimeConstants.THIRD_LESSON_END) < 0) || (currentTime.compareTo(TimeConstants.FOURTH_LESSON_START) > 0 && currentTime.compareTo(TimeConstants.FOURTH_LESSON_END) < 0);
	}

	public static long getTimeToLessonStart(LocalTime currentTime) {
		long[] durations = {
				Duration.between(currentTime, TimeConstants.FIRST_LESSON_START).toMillis(),
				Duration.between(currentTime, TimeConstants.SECOND_LESSON_START).toMillis(),
				Duration.between(currentTime, TimeConstants.THIRD_LESSON_START).toMillis(),
				Duration.between(currentTime, TimeConstants.FOURTH_LESSON_START).toMillis()
		};

		long minimalDuration;
		minimalDuration = Arrays.stream(durations)
				.filter(d -> d > 0)
				.min()
				.orElse(-1);

		return minimalDuration;
	}
}
