package ods.attendancebot;

import ods.attendancebot.handlers.AttendanceHandler;
import ods.attendancebot.handlers.ConfigHandler;
import ods.attendancebot.handlers.TimeEventHandler;
import ods.attendancebot.utils.BotLogger;
import ods.attendancebot.utils.BotNotification;
import ods.attendancebot.utils.BotWebDriver;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.FileNotFoundException;

public class AttendanceBot {
	private static final WebDriver driver = BotWebDriver.getDriver();

	public static void main(String[] args) {
		BotLogger.info("Starting bot");
		BotNotification.sendNotification("Starting bot", TrayIcon.MessageType.INFO);

		initializeAndRunManagers();
		destroyBotWebDriver();

		BotNotification.destroyTrayIcon();
	}

	private static void initializeAndRunManagers() {
		try {
			initializeManagers();

			AttendanceHandler.loginIntoAccount();
			AttendanceHandler.handleAttendances();

			BotLogger.info("Shutdown bot");
			BotNotification.sendNotification("Shutdown bot", TrayIcon.MessageType.INFO);
		} catch (LoginException | FileNotFoundException e) {
			BotLogger.error(e.getMessage());
			BotNotification.sendNotification("Bot terminated. Error message contains in log", TrayIcon.MessageType.ERROR);
		}
	}

	private static void initializeManagers() throws RuntimeException, FileNotFoundException {
		ConfigHandler.initialize(driver);
		TimeEventHandler.initialize(driver);
		AttendanceHandler.initialize(driver);
		BotLogger.info("Managers initialized");
	}

	private static void destroyBotWebDriver() {
		BotWebDriver.destroyWebDriver();
	}
}