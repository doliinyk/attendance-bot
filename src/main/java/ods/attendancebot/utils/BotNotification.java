package ods.attendancebot.utils;

import ods.attendancebot.Program;
import ods.attendancebot.constants.ResourceConstants;
import ods.attendancebot.handlers.AttendanceHandler;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class BotNotification {
	private static final SystemTray systemTray = SystemTray.getSystemTray();
	private static TrayIcon trayIcon;
	private static MenuItem exitItem;

	static {
		if (SystemTray.isSupported()) {
			try {
				URL url = Program.class.getClassLoader()
						.getResource(ResourceConstants.IMAGE_FILE_NAME);
				Image image = Toolkit.getDefaultToolkit()
						.getImage(url);

				trayIcon = new TrayIcon(image, "Attendance bot");
				trayIcon.setImageAutoSize(true);

				createMenu();
				systemTray.add(trayIcon);
			} catch (AWTException ignore) {
			}
		}
	}

	private static void createMenu() {
		PopupMenu popupMenu = new PopupMenu();
		MenuItem logItem = new MenuItem("Show log");
		exitItem = new MenuItem("Shutdown");

		logItem.addActionListener(e -> {
			try {
				Desktop.getDesktop()
						.open(Paths.get("attendance-bot.log")
								.toFile());
			} catch (IOException ex) {
				BotLogger.error(ex.getMessage());
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
		if (!exitItem.isEnabled()) {
			exitItem.setEnabled(true);
		}
	}

	public static void sendNotification(String text, MessageType messageType) {
		if (SystemTray.isSupported()) {
			trayIcon.displayMessage("Attendance bot", text, messageType);
		}
	}

	public static void destroyTrayIcon() {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			BotLogger.error(e.getMessage());
		}

		systemTray.remove(trayIcon);
	}
}