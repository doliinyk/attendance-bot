package ods.attendancebot.constants;

public class UrlConstants {
	public static final String WHITELIST_FILE_URL = "https://drive.google.com/uc?id=1AGQOSJCESdtIr-Nc90YkuD9i588g2e8e";

	public static final String MAIN_WINDOW_URL = "http://eguru.tk.te.ua/my/";
	public static final String LOGIN_WINDOW_URL = "http://eguru.tk.te.ua/login/index.php";

	public static String LOGOUT_WINDOW_URL(String sessionKey) {
		return "http://eguru.tk.te.ua/login/logout.php?sesskey=" + sessionKey;
	}
}
