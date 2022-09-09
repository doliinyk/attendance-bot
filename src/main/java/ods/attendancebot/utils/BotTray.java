package ods.attendancebot.utils;

import ods.attendancebot.AttendanceBot;
import ods.attendancebot.constants.ResourceConstants;
import ods.attendancebot.handlers.AttendanceHandler;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

public class BotTray {
	private static final SystemTray systemTray = SystemTray.getSystemTray();
	private static TrayIcon trayIcon;
	private static MenuItem exitItem;
	private static boolean isNotified;

	public static void setupTray(String[] args) {
		if (SystemTray.isSupported()) {
			try {
				URL url = AttendanceBot.class.getClassLoader()
						.getResource(ResourceConstants.IMAGE_FILE_NAME);
				Image image = Toolkit.getDefaultToolkit()
						.getImage(url);

				trayIcon = new TrayIcon(image, "Attendance bot");
				trayIcon.setImageAutoSize(true);

				createMenu();
				systemTray.add(trayIcon);

				isNotified = !Arrays.asList(args)
						.contains("silent");
			} catch (AWTException ignore) {
			}
		}
	}

	private static void createMenu() {
		PopupMenu popupMenu = new PopupMenu();
		MenuItem logItem = new MenuItem("Show log");
		exitItem = new MenuItem("Shutdown");

		logItem.addActionListener(event -> {
			try {
				Desktop.getDesktop()
						.open(Paths.get("attendance-bot.log")
								.toFile());
			} catch (IOException e) {
				BotLogger.error(e.getMessage());
			}
		});
		exitItem.addActionListener(e -> {
			BotLogger.info("Bot closed via tray icon");
			AttendanceHandler.interrupt();
		});

		exitItem.setEnabled(false);

		popupMenu.add(logItem);
		popupMenu.add(exitItem);
		trayIcon.setPopupMenu(popupMenu);
	}

	public static void enableExitItem() {
		if (SystemTray.isSupported() && !exitItem.isEnabled()) {
			exitItem.setEnabled(true);
		}
	}

	public static void sendNotification(String text, MessageType messageType) {
		if (SystemTray.isSupported() && isNotified) {
			trayIcon.displayMessage("Attendance bot", text, messageType);
		}
	}

	public static void destroyTrayIcon() {
		if (SystemTray.isSupported()) {
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				BotLogger.error(e.getMessage());
			}

			systemTray.remove(trayIcon);
		}
	}
}