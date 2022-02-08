package bot.managers;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

import static bot.managers.OperationsManager.findElementByCssSelector;
import static bot.managers.OperationsManager.findElementsByCssSelector;
import static java.util.stream.Collectors.toList;

public class CollectManager {
	public static List<WebElement> collectEventsFromMainWindow() {
		Stream<WebElement> events = collectActivitiesByCssSelector("div.card-text.content.calendarwrapper>div.event");

		return events.filter(OperationsManager::filterAttendancesDayAndTime)
				.collect(toList());
	}

	public static List<WebElement> collectAttendancesFromEventWindow() {
		Stream<WebElement> events = collectActivitiesByCssSelector("div.eventlist.my-1>div.event");

		return events.map(a -> findElementByCssSelector(a, "a.card-link"))
				.collect(toList());
	}

	private static Stream<WebElement> collectActivitiesByCssSelector(String cssSelector) {
		return findElementsByCssSelector(cssSelector).stream()
				.filter(OperationsManager::filterAttendancesFromEvents);
	}

	public static List<String> collectUrlsFromEvents(List<WebElement> attendances) {
		return attendances.stream()
				.map(a -> findElementByCssSelector(a, "a[data-action='view-event']").getAttribute("href"))
				.collect(toList());
	}
}
