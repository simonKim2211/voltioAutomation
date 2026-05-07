Feature: F6 Transaction History API Testing

  As a QA tester
  I want to validate transaction history APIs
  So that account activity can be verified after money movement operations

  Scenario: Retrieve transaction history after deposit
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a deposit transaction
    When I send the transaction history request
    Then the response status code should be 200
    And the transaction history response should contain at least one transaction
    And the transaction history response should contain transaction direction "CREDIT"

  Scenario: Retrieve transaction history after withdrawal
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a withdrawal transaction
    When I send the transaction history request
    Then the response status code should be 200
    And the transaction history response should contain at least one transaction
    And the transaction history response should contain transaction direction "DEBIT"

  Scenario: Retrieve transaction history with default date range
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a deposit transaction
    When I send the transaction history request without date filters
    Then the response status code should be 200
    And the transaction history response should contain at least one transaction

  Scenario: Reject transaction history access for another customer's account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has a CHECKING account
    When Customer A sends a transaction history request for Customer B account
    Then the response status code should be 403

  Scenario: Export transaction history as PDF
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a deposit transaction
    When I send the transaction history PDF export request
    Then the response status code should be 200
    And the response content type should contain "application/pdf"