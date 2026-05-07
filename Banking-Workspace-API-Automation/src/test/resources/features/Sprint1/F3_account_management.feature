Feature: F3 Account Management API Testing

  As a QA tester
  I want to validate account management APIs
  So that account workflows can be tested through the backend

  Scenario: Create CHECKING account with valid details
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have valid CHECKING account details
    When I send the create account request
    Then the response status code should be 201
    And the account response should contain created account details

  Scenario: Create SAVINGS account with valid details
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have valid SAVINGS account details
    When I send the create account request
    Then the response status code should be 201
    And the account response should contain created account details

    @knownDefect
  Scenario: Reject CHECKING account with interest rate
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have CHECKING account details with interest rate
    When I send the create account request
    Then the response status code should be 422

    @knownDefect
  Scenario: Reject SAVINGS account without interest rate
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have SAVINGS account details without interest rate
    When I send the create account request
    Then the response status code should be 422

  Scenario: Reject account creation with negative balance
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have account details with negative balance
    When I send the create account request
    Then the response status code should be 422

  Scenario: Get account by account ID
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    When I send the get account request
    Then the response status code should be 200
    And the account response should contain created account details

  Scenario: List customer accounts
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    When I send the list customer accounts request
    Then the response status code should be 200
    And the customer accounts response should contain the created account

  Scenario: Update SAVINGS account interest rate
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid SAVINGS account
    And I have valid savings account update details
    When I send the update account request
    Then the response status code should be 200
    And the account response should contain updated interest rate


  Scenario: Reject get account with invalid account ID
    Given I am an authenticated customer user
    When I send the get account request with invalid account ID
    Then the response status code should be 400