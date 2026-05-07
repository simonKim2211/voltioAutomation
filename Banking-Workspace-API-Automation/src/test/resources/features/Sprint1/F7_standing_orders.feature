
Feature: F7 Standing Order API Testing

  As a QA tester
  I want to validate standing order APIs
  So that recurring payment instructions can be tested through the backend

  Scenario: Create standing order with valid details
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have valid standing order details
    When I send the create standing order request
    Then the response status code should be 201
    And the standing order response should contain created standing order details

  Scenario: List standing orders for account
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have already created a valid standing order
    When I send the list standing orders request
    Then the response status code should be 200
    And the standing orders list should contain the created standing order

  Scenario: Cancel standing order
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have already created a valid standing order
    When I send the cancel standing order request
    Then the response status code should be 200

  Scenario: Reject standing order with invalid payee account
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have standing order details with invalid payee account
    When I send the create standing order request
    Then the response status code should be 400

  Scenario: Reject standing order with amount above daily transfer limit
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have standing order details with amount above daily transfer limit
    When I send the create standing order request
    Then the response status code should be 400

  Scenario: Reject standing order with start date less than 24 hours
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have standing order details with start date less than 24 hours
    When I send the create standing order request
    Then the response status code should be 400

  Scenario: Reject duplicate active standing order
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have already created a valid standing order
    When I send the same standing order request again
    Then the response status code should be 409