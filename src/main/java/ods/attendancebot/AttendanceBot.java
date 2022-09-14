package ods.attendancebot;

import ods.attendancebot.handlers.AttendanceHandler;
import ods.attendancebot.handlers.ConfigHandler;
import ods.attendancebot.handlers.TimeEventHandler;
import ods.attendancebot.utils.BotLogger;
import ods.attendancebot.utils.BotTray;
import ods.attendancebot.utils.BotDriver;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.FileNotFoundException;

public class AttendanceBot {
	public static void main(String[] args) {
		BotLogger.info("Starting bot");
		BotTray.sendNotification("Starting bot", TrayIcon.MessageType.INFO);

		initializeAndRunManagers();
		destroyDriver();

		BotTray.destroyTrayIcon();
	}

	private static void initializeAndRunManagers() {
		try {
			initializeManagers();

			AttendanceHandler.loginIntoAccount();
			AttendanceHandler.handleAttendances();

			BotLogger.info("Shutdown bot");
			BotTray.sendNotification("Shutdown bot", TrayIcon.MessageType.INFO);
		} catch (LoginException | FileNotFoundException e) {
			BotLogger.error(e.getMessage());
			BotTray.sendNotification("Bot terminated. Error message contains in log", TrayIcon.MessageType.ERROR);
		}
	}

	private static void initializeManagers() throws RuntimeException, FileNotFoundException {
		WebDriver driver = BotDriver.getDriver();

		ConfigHandler.initialize(driver);
		TimeEventHandler.initialize(driver);
		AttendanceHandler.initialize(driver);
		BotLogger.info("Managers initialized");
	}

	private static void destroyDriver() {
		BotDriver.destroyWebDriver();
	}
}