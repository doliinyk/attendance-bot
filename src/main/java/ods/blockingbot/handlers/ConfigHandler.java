package ods.blockingbot.handlers;

import ods.blockingbot.BlockingBot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ConfigHandler {
	private static WebDriver driver;
	private static JSONObject configJson;
	public static final String CONFIG_FILE_NAME = "config.json";

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
		} catch (NullPointerException | FileNotFoundException | FileSystemNotFoundException ignore) {
			fileReader = new FileReader(getLocalFilePath());
		}

		return fileReader;
	}

	private static String getConfigFilePath() throws URISyntaxException, NullPointerException {
		URL url = BlockingBot.class.getClassLoader()
				.getResource(CONFIG_FILE_NAME);

		Path path = Paths.get(Objects.requireNonNull(url)
				                      .toURI());

		return String.valueOf(path);
	}

	private static String getLocalFilePath() {
		Path path = Paths.get(CONFIG_FILE_NAME);

		return String.valueOf(path);
	}

	public static void sendLoginKeysToWebElements() {
		String username = getValueFromConfig("username");
		String password = getValueFromConfig("password");

		sendLoginKeysToWebElements(username, password);
	}

	private static void sendLoginKeysToWebElements(String username, String password) {
		WebElement loginInput = driver.findElement(By.id("username"));
		WebElement passwordInput = driver.findElement(By.id("password"));

		loginInput.sendKeys(username);
		passwordInput.sendKeys(password);
	}

	public static String getValueFromConfig(String key) {
		return (String) configJson.get(key);
	}

	public static List<String> getArrayValueFromConfig(String key) {
		return (List<String>) configJson.get(key);
	}
}