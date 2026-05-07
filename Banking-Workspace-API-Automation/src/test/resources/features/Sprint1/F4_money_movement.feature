Feature: F4 Money Movement API Testing

  As a QA tester
  I want to validate deposit, withdrawal, and transfer APIs
  So that money movement workflows can be tested through the backend

  Scenario: Deposit increases account balance
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have valid deposit details
    When I send the deposit request
    Then the response status code should be 200
    And the money movement response should show balance "125.00"

  Scenario: Reject deposit with zero amount
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have deposit details with zero amount
    When I send the deposit request
    Then the response status code should be 422

  Scenario: Reject deposit with negative amount
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have deposit details with negative amount
    When I send the deposit request
    Then the response status code should be 422

  Scenario: Duplicate deposit idempotency key does not apply deposit twice
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have valid deposit details
    When I send the deposit request with the same idempotency key twice
    Then the final account balance should be "125.00"

  Scenario: Withdrawal decreases account balance
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have valid withdrawal details
    When I send the withdrawal request
    Then the response status code should be 200
    And the money movement response should show balance "80.00"

  Scenario: Reject withdrawal with insufficient funds
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have withdrawal details greater than current balance
    When I send the withdrawal request
    Then the response status code should be 409

  Scenario: Duplicate withdrawal idempotency key does not apply withdrawal twice
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have valid withdrawal details
    When I send the withdrawal request with the same idempotency key twice
    Then the final account balance should be "80.00"

  Scenario: Transfer debits source account and credits destination account
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created source and destination CHECKING accounts
    And I have valid transfer details
    When I send the transfer request
    Then the response status code should be 200
    And the source account balance should be "125.00"
    And the destination account balance should be "125.00"

  Scenario: Reject transfer with insufficient funds
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created source and destination CHECKING accounts
    And I have transfer details greater than source balance
    When I send the transfer request
    Then the response status code should be 409

  Scenario: Reject transfer to same account
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have transfer details using same source and destination account
    When I send the transfer request
    Then the response status code should be 422