package bot.application;

import bot.constants.ResourceConstants;
import bot.managers.AttendanceManager;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.URL;

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
		exitItem = new MenuItem("Shutdown");

		exitItem.addActionListener(e -> {
			BotLogger.info("Bot closed via tray icon");
			AttendanceManager.interrupt();
		});
		exitItem.setEnabled(false);

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