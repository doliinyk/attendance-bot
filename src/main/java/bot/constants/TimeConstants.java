package bot.constants;

import java.time.LocalTime;

import static bot.managers.ConfigManager.parseStringFromConfigToLocalTime;

public class TimeConstants {
	public static final LocalTime FIRST_LESSON_START = parseStringFromConfigToLocalTime("first-lesson-start");
	public static final LocalTime FIRST_LESSON_END = parseStringFromConfigToLocalTime("first-lesson-end");
	public static final LocalTime SECOND_LESSON_START = parseStringFromConfigToLocalTime("second-lesson-start");
	public static final LocalTime SECOND_LESSON_END = parseStringFromConfigToLocalTime("second-lesson-end");
	public static final LocalTime THIRD_LESSON_START = parseStringFromConfigToLocalTime("third-lesson-start");
	public static final LocalTime THIRD_LESSON_END = parseStringFromConfigToLocalTime("third-lesson-end");
	public static final LocalTime FOURTH_LESSON_START = parseStringFromConfigToLocalTime("fourth-lesson-start");
	public static final LocalTime FOURTH_LESSON_END = parseStringFromConfigToLocalTime("fourth-lesson-end");
}
