package ods.attendancebot.handlers;

import ods.attendancebot.AttendanceBot;
import ods.attendancebot.constants.ResourceConstants;
import ods.attendancebot.constants.UrlConstants;
import ods.attendancebot.utils.BotLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Objects;

public class ConfigHandler {
	private static WebDriver driver;
	private static JSONObject configJson;

	public static void initialize(WebDriver driver) throws RuntimeException, FileNotFoundException {
		ConfigHandler.driver = driver;

		configJson = getJsonObjectFromConfig();
	}

	private static JSONObject getJsonObjectFromConfig() throws FileNotFoundException {
		JSONObject configJson;

		try (FileReader reader = getConfigFileReader()) {
			JSONParser parser = new JSONParser();

			configJson = (JSONObject) parser.parse(reader);
		} catch (NullPointerException | FileNotFoundException e) {
			throw new FileNotFoundException("Config file not found");
		} catch (IOException | ParseException | URISyntaxException e) {
			throw new RuntimeException("Failed reading config file");
		}

		return configJson;
	}

	private static FileReader getConfigFileReader() throws URISyntaxException, FileNotFoundException {
		FileReader fileReader;

		try {
			fileReader = new FileReader(getConfigFilePath());
		} catch (NullPointerException | FileNotFoundException ignore) {
			fileReader = new FileReader(getLocalFilePath());
		}

		return fileReader;
	}

	private static String getConfigFilePath() throws URISyntaxException, NullPointerException {
		URL url = AttendanceBot.class.getClassLoader()
				.getResource(ResourceConstants.CONFIG_FILE_NAME);

		Path path = Paths.get(Objects.requireNonNull(url)
				.toURI());

		return String.valueOf(path);
	}

	private static String getLocalFilePath() {
		Path path = Paths.get(ResourceConstants.CONFIG_FILE_NAME);

		return String.valueOf(path);
	}

	public static void sendLoginKeysToWebElements() throws LoginException {
		String username = getValueFromConfig("username");
		String password = getValueFromConfig("password");

		assertUsernameInWhitelist(username);

		sendLoginKeysToWebElements(username, password);
	}

	private static void sendLoginKeysToWebElements(String username, String password) {
		WebElement loginInput = driver.findElement(By.id("username"));
		WebElement passwordInput = driver.findElement(By.id("password"));

		loginInput.sendKeys(username);
		passwordInput.sendKeys(password);
	}

	private static void assertUsernameInWhitelist(String username) throws LoginException {
		try {
			URL url = new URL(UrlConstants.WHITELIST_FILE_URL);

			URLConnection urlConnection = url.openConnection();
			InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;
			StringBuilder jsonString = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				jsonString.append(line);
			}

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(jsonString.toString());
			JSONArray usernames = (JSONArray) jsonObject.get("usernames");

			if (!usernames.contains(username)) {
				throw new FailedLoginException("Username " + username + " isn't in whitelist");
			}

			bufferedReader.close();
		} catch (IOException | ParseException e) {
			BotLogger.error("Failed asserting username in whitelist");
			throw new FailedLoginException(e.getMessage());
		}
	}

	public static LocalTime parseStringFromConfigToLocalTime(String key) {
		return LocalTime.parse(getValueFromConfig(key))
				.plusSeconds(30);
	}

	public static String getValueFromConfig(String key) {
		return (String) configJson.get(key);
	}

	public static boolean containsKeyInConfig(String key) {
		return configJson.containsKey(key);
	}
}