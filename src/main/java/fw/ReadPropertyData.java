package fw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Properties;

public final class ReadPropertyData {
	private static File file;
	private static FileInputStream fileInput;
	private static Properties properties;

	static {
		final String propertyFile = System.getProperty("user.dir")
				+ "\\src\\main\\resources\\properties\\data.properties";
		file = new File(propertyFile);
		fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		properties = new Properties();
		try {
			properties.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final RuntimeMXBean runtimeMxBean = ManagementFactory
				.getRuntimeMXBean();
		final List<String> arguments = runtimeMxBean.getInputArguments();
		setVMarguments(arguments, "username");
		setVMarguments(arguments, "password");
		setVMarguments(arguments, "video");		
	}

	private ReadPropertyData() {
	}

	public static String getBaseUrl() {
		return properties.getProperty("baseUrl");
	}

	public static String getUsername() {
		return properties.getProperty("username");
	}

	public static String getUserPassword() {
		return properties.getProperty("password");
	}

	public static String getDriverPath() {
		return System.getProperty("user.dir")
				+ properties.getProperty("driver");
	}

	public static String getVideoFilePath() {
		return System.getProperty("user.dir")
				+ properties.getProperty("video");
	}

	private static void setVMarguments(List<String> arguments, String property) {
		arguments
				.stream()
				.filter(argument -> argument.startsWith("-D".concat(property)))
				.map(argument -> {
					final String[] array = argument.split("=");
					return array[1];
				})
				.forEach(argument -> properties.setProperty(property, argument));
	}
}
