@knownDefect
Feature: F11 RRSP Account API Testing

  As a QA tester
  I want to validate RRSP account APIs
  So that customers can open and manage RRSP accounts through the existing account system

  Scenario: Create RRSP account with valid details
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have valid RRSP account details
    When I send the create account request
    Then the response status code should be 201
    And the RRSP account response should contain created RRSP account details

  Scenario: Reject second active RRSP account for same customer
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid RRSP account
    And I have valid RRSP account details
    When I send the create account request
    Then the response status code should be 409

  Scenario: Reject RRSP account with interest rate
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have RRSP account details with interest rate
    When I send the create account request
    Then the response status code should be 422

  Scenario: Get RRSP account by account ID
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid RRSP account
    When I send the get account request
    Then the response status code should be 200
    And the RRSP account response should contain created RRSP account details

  Scenario: List customer accounts includes RRSP account
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid RRSP account
    When I send the list customer accounts request
    Then the response status code should be 200
    And the customer accounts response should contain the created account

  Scenario: Customer cannot create RRSP account for another customer
    Given Customer A and Customer B both exist with customer profiles
    And I have valid RRSP account details
    When Customer A sends a create RRSP account request for Customer B
    Then the response status code should be 403