package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = loadProperties();

    private ConfigReader() {
    }

    public static String getBaseUrl() {
        return getProperty("baseUrl", "");
    }

    public static String getBrowser() {
        return System.getProperty("browser", getProperty("browser", "chrome"));
    }


    public static long getImplicitWait() {
        return getLongProperty("implicitWaitSeconds", 10);
    }

    public static long getPageLoadTimeout() {
        return getLongProperty("pageLoadTimeoutSeconds", 30);
    }

    public static long getScriptTimeout() {
        return getLongProperty("scriptTimeoutSeconds", 30);
    }

    public static String getValidUsername() {
        return getProperty("validUsername", "");
    }

    public static String getValidPassword() {
        return getProperty("validPassword", "");
    }

    public static String getInvalidPassword() {
        return getProperty("invalidPassword", "");
    }

    public static String getProperty(String key, String defaultValue) {
        return System.getProperty(key, PROPERTIES.getProperty(key, defaultValue));
    }

    private static long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value for property '" + key + "': " + value, e);
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("config.properties was not found in the test resources folder.");
            }

            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config.properties.", e);
        }
    }
}

