# QA Automation Implementation Context

## Current Goal
Implement UI automation for:
1. Registration
2. Customer creation
3. Account creation

Use Selenium WebDriver + Cucumber + Maven + Page Object Model + PageFactory.

## IDEs
Team uses Eclipse and IntelliJ.

## Application URL
Home:
https://frontend-524103119199.northamerica-northeast2.run.app/

## Framework Rules
- Java Maven project
- Cucumber feature files using Gherkin
- Selenium WebDriver
- Page Object Model
- PageFactory with @FindBy
- Hooks class must manage browser setup/teardown
- Use existing DriverUtilities class
- Do not create DriverFactory
- Do not create unit tests
- Do not create API tests
- Do not create backend tests
- Do not use driver.findElement inside Step Definitions
- Step Definitions must call Page Object methods only
- Assertions should be in Step Definitions, not Page classes
- Page classes should only perform actions and return page state

## Existing Driver Utility
Use:
DriverUtilities.getInstance().getDriver()

Package:
Utilities

Do not generate a separate driver manager.

## Feature Naming
Feature files must be named:
F1_login.feature
F2_registration.feature
F3_customer_creation.feature
F4_account_creation.feature

## Feature File Rules
Use Scenario Outline.
Use Examples tables.
Do not hardcode values inside step sentences.
Use placeholders like:
"<email>"
"<password>"
"<name>"
"<address>"
"<customerType>"
"<accountType>"
"<balance>"
"<interestRate>"
"<expectedResult>"

## Page Object Naming
RegistrationPage.java
CustomerPage.java
AccountPage.java

## Step Definition Naming
RegistrationSteps.java
CustomerSteps.java
AccountSteps.java

## Authentication Flow
Start from home page:
https://frontend-524103119199.northamerica-northeast2.run.app/

Click Login navbar link:
<a href="/login">Login</a>

Then on login page enter email/password and click:
<button type="submit">Sign In</button>

## Required Existing Login Locators
Login nav:
@FindBy(xpath = "//a[@href='/login' and normalize-space()='Login']")

Email:
@FindBy(xpath = "//label[normalize-space()='Email']/following-sibling::input")

Password:
@FindBy(xpath = "//label[normalize-space()='Password']/following-sibling::input")

Sign In:
@FindBy(xpath = "//button[@type='submit' and normalize-space()='Sign In']")

## Implementation Rule
Registration, customer creation, and account creation may require the user to be logged in first.
If a page requires login, call reusable login helper methods instead of duplicating login code.