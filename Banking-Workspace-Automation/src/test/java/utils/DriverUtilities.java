package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
        switch(browser){
            case "chrome":
                this.driver=new ChromeDriver();
                break;

            default:
                System.out.println("The browser is not Chrome , Please use Chrome for running these tests");
                break;
        }

        driver.manage().window().maximize();

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