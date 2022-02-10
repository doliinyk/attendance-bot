package bot.constants;

import java.time.LocalTime;

import static bot.managers.ConfigManager.parseStringFromConfigToInt;
import static bot.managers.ConfigManager.parseStringFromConfigToLocalTime;

public class LessonsConstants {
	public static final int MONDAY_LESSONS_AMOUNT = parseStringFromConfigToInt("monday-lessons-amount");
	public static final int TUESDAY_LESSONS_AMOUNT = parseStringFromConfigToInt("tuesday-lessons-amount");
	public static final int WEDNESDAY_LESSONS_AMOUNT = parseStringFromConfigToInt("wednesday-lessons-amount");
	public static final int THURSDAY_LESSONS_AMOUNT = parseStringFromConfigToInt("thursday-lessons-amount");
	public static final int FRIDAY_LESSONS_AMOUNT = parseStringFromConfigToInt("friday-lessons-amount");

	public static final LocalTime FIRST_LESSON_START = parseStringFromConfigToLocalTime("first-lesson-start");
	public static final LocalTime FIRST_LESSON_END = parseStringFromConfigToLocalTime("first-lesson-end");
	public static final LocalTime SECOND_LESSON_START = parseStringFromConfigToLocalTime("second-lesson-start");
	public static final LocalTime SECOND_LESSON_END = parseStringFromConfigToLocalTime("second-lesson-end");
	public static final LocalTime THIRD_LESSON_START = parseStringFromConfigToLocalTime("third-lesson-start");
	public static final LocalTime THIRD_LESSON_END = parseStringFromConfigToLocalTime("third-lesson-end");
	public static final LocalTime FOURTH_LESSON_START = parseStringFromConfigToLocalTime("fourth-lesson-start");
	public static final LocalTime FOURTH_LESSON_END = parseStringFromConfigToLocalTime("fourth-lesson-end");
}
