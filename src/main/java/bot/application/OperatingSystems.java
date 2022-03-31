package bot.application;

public enum OperatingSystems {
	WINDOWS,
	LINUX,
	MACOS,
	OTHER;

	public static OperatingSystems getCurrentOperatingSystem() {
		final String operatingSystemProperty = System.getProperty("os.name")
				.toLowerCase();

		if (operatingSystemProperty.contains("win")) {
			return WINDOWS;
		} else if (operatingSystemProperty.contains("nix") || operatingSystemProperty.contains("nux") || operatingSystemProperty.contains("aix")) {
			return LINUX;
		} else if (operatingSystemProperty.contains("mac")) {
			return MACOS;
		} else {
			return OTHER;
		}
	}
}
