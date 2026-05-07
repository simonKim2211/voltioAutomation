Feature: F2 Customer Management API Testing

  As a QA tester
  I want to validate customer management APIs
  So that customer profile workflows can be tested through the backend

  Scenario: Create customer with valid PERSON details
    Given I am an authenticated customer user
    And I have valid PERSON customer details
    When I send the create customer request
    Then the response status code should be 201
    And the customer response should contain created customer details

  Scenario: Create customer with valid COMPANY details
    Given I am an authenticated customer user
    And I have valid COMPANY customer details
    When I send the create customer request
    Then the response status code should be 201
    And the customer response should contain created customer details

  Scenario: Reject customer creation without token
    Given I have valid PERSON customer details
    When I send the create customer request without token
    Then the response status code should be 401

  Scenario: Reject customer creation with missing required fields
    Given I am an authenticated customer user
    And I have customer details with missing required fields
    When I send the create customer request
    Then the response status code should be 422

  Scenario: Reject customer creation with invalid customer type
    Given I am an authenticated customer user
    And I have customer details with invalid customer type
    When I send the create customer request
    Then the response status code should be 422

  Scenario: Get customer by customer ID
    Given I am an authenticated customer user
    And I have already created a valid customer
    When I send the get customer request
    Then the response status code should be 200
    And the customer response should contain created customer details

  Scenario: Update customer name and address
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have valid customer update details
    When I send the update customer request
    Then the response status code should be 200
    And the customer response should contain updated customer details

  Scenario: Reject customer update with immutable email field
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have customer update details with immutable email field
    When I send the update customer request
    Then the response status code should be 400

  Scenario: Reject customer update with immutable accountNumber field
    Given I am an authenticated customer user
    And I have already created a valid customer
    And I have customer update details with immutable accountNumber field
    When I send the update customer request
    Then the response status code should be 400


  Scenario: Reject get customer with invalid customer ID
    Given I am an authenticated customer user
    When I send the get customer request with invalid customer ID
    Then the response status code should be 400