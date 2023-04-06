package ods.blockingbot.utils;

import ods.blockingbot.BlockingBot;
import ods.blockingbot.handlers.BlockingHandler;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class BotTray {
	private static final SystemTray systemTray = SystemTray.getSystemTray();
	private static TrayIcon trayIcon;
	private static MenuItem exitItem;

	static {
		if (SystemTray.isSupported()) {
			try {
				URL url = BlockingBot.class.getClassLoader()
						.getResource("images/icon.png");
				Image image = Toolkit.getDefaultToolkit()
						.getImage(url);

				trayIcon = new TrayIcon(image, "Blocking bot");
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

		logItem.addActionListener(event -> {
			try {
				Desktop.getDesktop()
						.open(Paths.get("blocking-bot.log")
								.toFile());
			} catch (IOException e) {
				BotLogger.error(e.getMessage());
			}
		});
		exitItem.addActionListener(e -> {
			BotLogger.info("Bot closed via tray icon");
			BlockingHandler.setIsWorking(false);
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
		if (SystemTray.isSupported()) {
			trayIcon.displayMessage("Blocking bot", text, messageType);
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