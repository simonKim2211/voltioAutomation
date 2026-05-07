package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RegistrationPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final AuthenticatedHomePage authenticatedHomePage;

    @FindBy(xpath = "//select[@id='register-type']")
    private WebElement accountTypeDropdown;

    @FindBy(xpath = "//button[text()='Continue']")
    private WebElement nextButton;

    @FindBy(xpath = "//label[normalize-space()='Email']/following-sibling::input")
    private WebElement emailInput;

    @FindBy(xpath = "//label[normalize-space()='Password']/following-sibling::input")
    private WebElement passwordInput;

    @FindBy(xpath = "//label[normalize-space()='Full Name' or normalize-space()='Company Name']/following-sibling::input")
    private List<WebElement> nameOrCompanyInputs;

    @FindBy(xpath = "//label[normalize-space()='Address']/following-sibling::input")
    private WebElement addressInput;

    @FindBy(xpath = "//button[normalize-space()='Create Account']")
    private WebElement createAccountButton;

    @FindBy(xpath = "//*[normalize-space()='Validation failed']")
    private List<WebElement> registrationErrorMessages;

    @FindBy(xpath = "//*[contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'already registered')]")
    private List<WebElement> duplicateEmailMessages;

    @FindBy(xpath = "//*[@role='alert' or contains(@class,'alert')]")
    private List<WebElement> alertMessages;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.authenticatedHomePage = new AuthenticatedHomePage(driver);
        PageFactory.initElements(driver, this);
    }

    public void openHomePage(String baseUrl) {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
    }

    public void openRegistrationPage(String registrationUrl) {
        driver.get(registrationUrl);
        waitForRegistrationStepOne();
    }

    public void waitForRegistrationStepOne() {
        wait.until(ExpectedConditions.visibilityOf(accountTypeDropdown));
    }

    public void selectAccountTypeAndContinue(String accountType) {
        wait.until(ExpectedConditions.visibilityOf(accountTypeDropdown));
        selectAccountType(accountType);

        clickWhenClickable(nextButton);
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        waitForNameOrCompanyInputVisible();
        wait.until(ExpectedConditions.visibilityOf(addressInput));
    }

    public void enterName(String name) {
        typeIntoField(resolveVisibleInput(nameOrCompanyInputs), name);
    }

    public void enterEmail(String email) {
        typeIntoField(emailInput, email);
    }

    public void enterPassword(String password) {
        typeIntoField(passwordInput, password);
    }

    public void enterAddress(String address) {
        typeIntoField(addressInput, address);
    }

    public void submitRegistration() {
        clickWhenClickable(createAccountButton);
    }

    public boolean isRegistrationSuccessful() {
        try {
            wait.until(webDriver -> hasSuccessfulRegistrationState());
            return true;
        } catch (TimeoutException e) {
            return hasSuccessfulRegistrationState();
        }
    }

    public boolean isRegistrationErrorDisplayed() {
        try {
            wait.until(webDriver -> hasRegistrationErrorState());
            return true;
        } catch (TimeoutException e) {
            return hasRegistrationErrorState();
        }
    }

    private boolean hasSuccessfulRegistrationState() {
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        return !currentUrl.contains("/register")
                && authenticatedHomePage.hasAuthenticatedHomeIndicators()
                && !hasRegistrationErrorState();
    }

    private boolean hasRegistrationErrorState() {
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        if (!currentUrl.contains("/register") && !isRegistrationFormVisible()) {
            return false;
        }

        if (hasAnyVisibleElement(registrationErrorMessages)
                || hasAnyVisibleElement(duplicateEmailMessages)
                || hasAnyVisibleElement(alertMessages)
                || hasBrowserValidationError()) {
            return true;
        }

        String pageText = driver.getPageSource().toLowerCase();
        return pageText.contains("already registered")
                || pageText.contains("validation failed");
    }

    private boolean isRegistrationFormVisible() {
        return isDisplayed(createAccountButton)
                || isDisplayed(emailInput)
                || isDisplayed(passwordInput)
                || isDisplayed(addressInput)
                || resolveVisibleInput(nameOrCompanyInputs) != null;
    }

    private boolean hasBrowserValidationError() {
        return hasValidationMessage(emailInput)
                || hasValidationMessage(passwordInput)
                || hasAnyValidationMessage(nameOrCompanyInputs)
                || hasValidationMessage(addressInput);
    }

    private void selectAccountType(String accountType) {
        Select select = new Select(accountTypeDropdown);

        try {
            select.selectByVisibleText(accountType);
            return;
        } catch (NoSuchElementException ignored) {
            // Fallback below for case/spacing differences in option labels.
        }

        String expected = accountType == null ? "" : accountType.trim().toLowerCase();
        List<WebElement> options = select.getOptions();

        for (WebElement option : options) {
            String text = option.getText() == null ? "" : option.getText().trim().toLowerCase();
            if (text.equals(expected)) {
                select.selectByVisibleText(option.getText().trim());
                return;
            }
        }

        for (WebElement option : options) {
            String text = option.getText() == null ? "" : option.getText().trim().toLowerCase();
            if (!expected.isEmpty() && text.contains(expected)) {
                select.selectByVisibleText(option.getText().trim());
                return;
            }
        }

        throw new NoSuchElementException("Could not find account type option: " + accountType);
    }

    private void waitForNameOrCompanyInputVisible() {
        wait.until(webDriver -> resolveVisibleInput(nameOrCompanyInputs) != null);
    }

    private void typeIntoField(WebElement field, String value) {
        wait.until(ExpectedConditions.visibilityOf(field));
        field.clear();
        field.sendKeys(value == null ? "" : value);
    }

    private void clickWhenClickable(WebElement element) {
        WebElement target = wait.until(ExpectedConditions.elementToBeClickable(element));
        try {
            target.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
        }
    }


    private boolean hasAnyVisibleElement(List<WebElement> elements) {
        if (elements == null) {
            return false;
        }

        for (WebElement element : elements) {
            if (isDisplayed(element)) {
                return true;
            }
        }
        return false;
    }

    private WebElement resolveVisibleInput(List<WebElement> elements) {
        if (elements == null) {
            return null;
        }

        for (WebElement element : elements) {
            if (isDisplayed(element)) {
                return element;
            }
        }

        return null;
    }

    private boolean hasAnyValidationMessage(List<WebElement> elements) {
        return hasValidationMessage(resolveVisibleInput(elements));
    }

    private boolean isDisplayed(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasValidationMessage(WebElement element) {
        if (element == null) {
            return false;
        }

        try {
            String validationMessage = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].validationMessage || '';", element);
            return validationMessage != null && !validationMessage.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}

