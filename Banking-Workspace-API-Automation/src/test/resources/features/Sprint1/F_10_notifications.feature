Feature: F10 Notification Evaluation API Testing

  As a QA tester
  I want to validate notification evaluation API security
  So that only authorised internal service callers can evaluate notification events

  Scenario: Reject notification evaluation without API key
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have a valid mandatory notification event payload
    When I send the notification evaluate request without API key
    Then the response status code should be 401

  Scenario: Reject notification evaluation with invalid API key
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have a valid mandatory notification event payload
    When I send the notification evaluate request with invalid API key
    Then the response status code should be 401

  Scenario: Reject notification evaluation with customer JWT
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have a valid mandatory notification event payload
    When I send the notification evaluate request with customer JWT
    Then the response status code should be 401