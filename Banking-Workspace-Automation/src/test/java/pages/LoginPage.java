package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final AuthenticatedHomePage authenticatedHomePage;

    @FindBy(xpath = "//a[@href='/login' and normalize-space()='Login']")
    private WebElement loginNavButton;

    @FindBy(xpath = "//label[normalize-space()='Email']/following-sibling::input")
    private WebElement emailInput;

    @FindBy(xpath = "//label[normalize-space()='Password']/following-sibling::input")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit' and normalize-space()='Sign In']")
    private WebElement signInButton;

    @FindBy(xpath = "//*[normalize-space()='Invalid credentials.']")
    private List<WebElement> invalidCredentialMessages;

    @FindBy(xpath = "//*[contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'invalid') or contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'error') or contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'unauthorized') or contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'required') or contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'incorrect') or contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'failed')]")
    private List<WebElement> feedbackMessages;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.authenticatedHomePage = new AuthenticatedHomePage(driver);
        PageFactory.initElements(driver, this);
    }

    public void openHomePage(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void clickLoginNavButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginNavButton)).click();
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickSignInButton() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton));

        try {
            signInButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signInButton);
        }
    }

    public boolean isOnLoginPage() {
        try {
            wait.until(ExpectedConditions.urlContains("/login"));
            return true;
        } catch (TimeoutException e) {
            return driver.getCurrentUrl().contains("/login");
        }
    }

    public boolean isLoginSuccessful() {
        try {
            wait.until(webDriver -> hasSuccessfulLoginState());
            return true;
        } catch (TimeoutException e) {
            return hasSuccessfulLoginState();
        }
    }

    public boolean isLoginErrorDisplayed() {
        try {
            wait.until(webDriver -> hasApplicationErrorState() || hasBrowserValidationError());
            return true;
        } catch (TimeoutException e) {
            return hasApplicationErrorState() || hasBrowserValidationError();
        }
    }

    private boolean hasSuccessfulLoginState() {
        String currentUrl = driver.getCurrentUrl().toLowerCase();

        return !currentUrl.contains("/login")
                && !hasApplicationErrorState()
                && authenticatedHomePage.hasAuthenticatedHomeIndicators();
    }

    private boolean hasApplicationErrorState() {
        if (isAnyElementDisplayed(invalidCredentialMessages)) {
            return true;
        }

        if (feedbackMessages != null) {
            for (WebElement feedbackMessage : feedbackMessages) {
                if (feedbackMessage.isDisplayed()) {
                    return true;
                }
            }
        }

        String pageText = driver.getPageSource().toLowerCase();
        return pageText.contains("invalid")
                || pageText.contains("error")
                || pageText.contains("unauthorized")
                || pageText.contains("required")
                || pageText.contains("incorrect")
                || pageText.contains("failed");
    }


    private boolean hasBrowserValidationError() {
        return hasValidationMessage(emailInput) || hasValidationMessage(passwordInput);
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
                // Ignore stale or missing elements and continue checking remaining candidates.
            }
        }

        return false;
    }

    private boolean hasValidationMessage(WebElement element) {
        String validationMessage = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage || '';", element);
        return validationMessage != null && !validationMessage.trim().isEmpty();
    }
}