Feature: F5 Authorization and Ownership API Testing

  As a QA tester
  I want to validate customer ownership restrictions
  So that users cannot access or modify resources owned by another customer

  Scenario: Customer cannot get another customer's profile
    Given Customer A and Customer B both exist with customer profiles
    When Customer A sends a get customer request for Customer B
    Then the response status code should be 403

  Scenario: Customer cannot update another customer's profile
    Given Customer A and Customer B both exist with customer profiles
    And I have valid customer update details
    When Customer A sends an update customer request for Customer B
    Then the response status code should be 403

  Scenario: Customer cannot get another customer's account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has a CHECKING account
    When Customer A sends a get account request for Customer B account
    Then the response status code should be 401

  Scenario: Customer cannot list another customer's accounts
    Given Customer A and Customer B both exist with customer profiles
    When Customer A sends a list accounts request for Customer B
    Then the response status code should be 401

  Scenario: Customer cannot deposit into another customer's account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has a CHECKING account
    And I have valid deposit details
    When Customer A sends a deposit request for Customer B account
    Then the response status code should be 401

  Scenario: Customer cannot withdraw from another customer's account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has a CHECKING account
    And I have valid withdrawal details
    When Customer A sends a withdrawal request for Customer B account
    Then the response status code should be 401

  Scenario: Customer cannot transfer from another customer's source account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has source and destination CHECKING accounts
    And I have transfer details from Customer B source account
    When Customer A sends the transfer request
    Then the response status code should be 401

  Scenario: Customer cannot list all customers
    Given I am an authenticated customer user
    When I send the list all customers request
    Then the response status code should be 403