package ods.attendancebot.constants;

import ods.attendancebot.handlers.ConfigHandler;

import java.time.LocalTime;

public class LessonConstants {
	public static final LocalTime MONDAY_LESSONS_START = ConfigHandler.parseStringFromConfigToLocalTime("monday-lessons-start");
	public static final LocalTime MONDAY_LESSONS_END = ConfigHandler.parseStringFromConfigToLocalTime("monday-lessons-end");
	public static final LocalTime TUESDAY_LESSONS_START = ConfigHandler.parseStringFromConfigToLocalTime("tuesday-lessons-start");
	public static final LocalTime TUESDAY_LESSONS_END = ConfigHandler.parseStringFromConfigToLocalTime("tuesday-lessons-end");
	public static final LocalTime WEDNESDAY_LESSONS_START = ConfigHandler.parseStringFromConfigToLocalTime("wednesday-lessons-start");
	public static final LocalTime WEDNESDAY_LESSONS_END = ConfigHandler.parseStringFromConfigToLocalTime("wednesday-lessons-end");
	public static final LocalTime THURSDAY_LESSONS_START = ConfigHandler.parseStringFromConfigToLocalTime("thursday-lessons-start");
	public static final LocalTime THURSDAY_LESSONS_END = ConfigHandler.parseStringFromConfigToLocalTime("thursday-lessons-end");
	public static final LocalTime FRIDAY_LESSONS_START = ConfigHandler.parseStringFromConfigToLocalTime("friday-lessons-start");
	public static final LocalTime FRIDAY_LESSONS_END = ConfigHandler.parseStringFromConfigToLocalTime("friday-lessons-end");
	public static final LocalTime SATURDAY_LESSONS_START;
	public static final LocalTime SATURDAY_LESSONS_END;

	static {
		LocalTime SATURDAY_LESSONS_START1 = LocalTime.of(0, 0);
		LocalTime SATURDAY_LESSONS_END1 = LocalTime.of(0, 0);

		if (ConfigHandler.containsKeyInConfig("saturday-lessons-day")) {
			switch (ConfigHandler.getValueFromConfig("saturday-lessons-day")) {
				case "monday":
					SATURDAY_LESSONS_START1 = LessonConstants.MONDAY_LESSONS_START;
					SATURDAY_LESSONS_END1 = LessonConstants.MONDAY_LESSONS_END;
				case "tuesday":
					SATURDAY_LESSONS_START1 = LessonConstants.TUESDAY_LESSONS_START;
					SATURDAY_LESSONS_END1 = LessonConstants.TUESDAY_LESSONS_END;
				case "wednesday":
					SATURDAY_LESSONS_START1 = LessonConstants.WEDNESDAY_LESSONS_START;
					SATURDAY_LESSONS_END1 = LessonConstants.WEDNESDAY_LESSONS_END;
				case "thursday":
					SATURDAY_LESSONS_START1 = LessonConstants.THURSDAY_LESSONS_START;
					SATURDAY_LESSONS_END1 = LessonConstants.THURSDAY_LESSONS_END;
				case "friday":
					SATURDAY_LESSONS_START1 = LessonConstants.FRIDAY_LESSONS_START;
					SATURDAY_LESSONS_END1 = LessonConstants.FRIDAY_LESSONS_END;
				default:
			}
		}

		SATURDAY_LESSONS_START = SATURDAY_LESSONS_START1;
		SATURDAY_LESSONS_END = SATURDAY_LESSONS_END1;
	}
}
