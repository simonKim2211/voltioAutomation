Feature: F8 Monthly Statements API Testing

  As a QA tester
  I want to validate monthly statement APIs
  So that statement PDF retrieval and access rules can be tested through the backend

  Scenario: Retrieve monthly statement PDF for current period
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    When I send the monthly statement request for current period
    Then the response status code should be 200
    And the response content type should contain "application/pdf"

  Scenario: Reject monthly statement request with invalid period format
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    When I send the monthly statement request with period "2026/05"
    Then the response status code should be 401

  Scenario: Reject monthly statement access for another customer's account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has a CHECKING account
    When Customer A sends a monthly statement request for Customer B account
    Then the response status code should be 401

  Scenario: Reject monthly statement request for non-existing account
    Given I am an authenticated customer user
    When I send the monthly statement request for non-existing account
    Then the response status code should be 401