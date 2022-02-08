package bot.managers;

import bot.application.Program;
import bot.constants.ConfigConstants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Objects;

public class ConfigManager {
	private static WebDriver driver;
	private static JSONObject configJson;

	public static void initialize(WebDriver driver) throws RuntimeException {
		ConfigManager.driver = driver;

		configJson = getJsonObjectFromConfig();
	}

	private static JSONObject getJsonObjectFromConfig() throws RuntimeException {
		JSONObject configJson = null;

		try (FileReader reader = new FileReader(String.valueOf(getConfigFilePath()))) {
			JSONParser parser = new JSONParser();

			configJson = (JSONObject) parser.parse(reader);
		} catch (IOException | ParseException | URISyntaxException e) {
			e.printStackTrace();
		}

		return configJson;
	}

	private static Path getConfigFilePath() throws URISyntaxException {
		URL url = Program.class.getClassLoader()
				.getResource(ConfigConstants.CONFIG_FILE_NAME);

		return Paths.get(ConfigConstants.CONFIG_FILE_NAME);
//		return Paths.get(Objects.requireNonNull(url)
//				.toURI());
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

	public static LocalTime parseStringFromConfigToLocalTime(String key) {
		return LocalTime.parse(getValueFromConfig(key));
	}

	public static String getValueFromConfig(String key) {
		return (String) configJson.get(key);
	}
}
