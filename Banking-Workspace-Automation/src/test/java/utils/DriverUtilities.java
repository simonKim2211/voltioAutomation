package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverUtilities {

    private static DriverUtilities instance;
    private WebDriver driver;

    private DriverUtilities() {
        initializeDriver();
    }

    public static DriverUtilities getInstance() {
        if (instance == null) {
            instance = new DriverUtilities();
        }
        return instance;
    }

    private void initializeDriver() {
        String browser = ConfigReader.getBrowser();

        switch (browser) {
            case "chrome":
                ChromeOptions options = new ChromeOptions();

                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");

                this.driver = new ChromeDriver(options);
                break;

            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            instance = null;
        }
    }
}
