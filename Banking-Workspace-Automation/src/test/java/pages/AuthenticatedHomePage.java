package pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthenticatedHomePage {

    private final WebDriver driver;

    @FindBy(xpath = "//*[normalize-space()='Overview']")
    private List<WebElement> overviewTabs;

    @FindBy(xpath = "//*[normalize-space()='Customer Accounts']")
    private List<WebElement> customerAccountsTabs;

    @FindBy(xpath = "//*[normalize-space()='My Profile']")
    private List<WebElement> myProfileButtons;

    public AuthenticatedHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean hasAuthenticatedHomeIndicators() {
        String pageText = driver.getPageSource().toLowerCase();
        int matchedIndicators = 0;

        if (pageText.contains("overview")) {
            matchedIndicators++;
        }

        if (pageText.contains("customer accounts")) {
            matchedIndicators++;
        }

        if (pageText.contains("my profile")) {
            matchedIndicators++;
        }

        return matchedIndicators >= 2
                || isAnyElementDisplayed(overviewTabs)
                || isAnyElementDisplayed(customerAccountsTabs)
                || isAnyElementDisplayed(myProfileButtons);
    }

    private boolean isAnyElementDisplayed(List<WebElement> elements) {
        if (elements == null) {
            return false;
        }

        for (WebElement element : elements) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (Exception e) {
                // Ignore stale or detached elements while checking indicator visibility.
            }
        }

        return false;
    }
}

