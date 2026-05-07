Feature: F9 Spending Insights API Testing

  As a QA tester
  I want to validate spending insight APIs
  So that customer spending breakdowns can be tested through the backend

  Scenario: Retrieve spending insights for owned account
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a withdrawal transaction
    When I send the spending insights request for current month
    Then the response status code should be 200
    And the spending insights response should contain required fields

  Scenario: Spending insights response contains category breakdown
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a withdrawal transaction
    When I send the spending insights request for current month
    Then the response status code should be 200
    And the spending insights response should contain category breakdown

  Scenario: Spending insights response contains six month trend
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    And I have completed a withdrawal transaction
    When I send the spending insights request for current month
    Then the response status code should be 200
    And the spending insights response should contain six month trend

  Scenario: Reject spending insights request for future month
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have already created a valid CHECKING account
    When I send the spending insights request for future month
    Then the response status code should be 409

  Scenario: Reject spending insights access for another customer's account
    Given Customer A and Customer B both exist with customer profiles
    And Customer B has a CHECKING account
    When Customer A sends a spending insights request for Customer B account
    Then the response status code should be 403