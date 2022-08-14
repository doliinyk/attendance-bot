package ods.attendancebot.handlers;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static ods.attendancebot.handlers.OperationHandler.findElementByCssSelector;
import static ods.attendancebot.handlers.OperationHandler.findElementsByCssSelector;

public class CollectHandler {
	public static List<WebElement> collectEventsFromMainWindow() {
		Stream<WebElement> events = collectActivitiesByCssSelector("div.card-text.content.calendarwrapper>div.event");

		return events.filter(OperationHandler::filterAttendancesDayAndTime)
				.collect(toList());
	}

	public static List<WebElement> collectAttendancesFromEventWindow() {
		Stream<WebElement> events = collectActivitiesByCssSelector("div.eventlist.my-1>div.event");

		return events.map(a -> findElementByCssSelector(a, "a.card-link"))
				.collect(toList());
	}

	private static Stream<WebElement> collectActivitiesByCssSelector(String cssSelector) {
		return findElementsByCssSelector(cssSelector).stream()
				.filter(OperationHandler::filterAttendancesFromEvents);
	}

	public static List<String> collectUrlsFromEvents(List<WebElement> attendances) {
		return attendances.stream()
				.map(a -> findElementByCssSelector(a, "a[data-action='view-event']").getAttribute("href"))
				.collect(toList());
	}
}
