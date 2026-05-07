Feature: F1 Authentication API Testing

  As a QA tester
  I want to validate registration and login APIs
  So that authenticated banking workflows can be tested later

  @F1_TC_001
  Scenario: Register user with valid details
    Given I have valid registration details
    When I send the register request
    Then the response status code should be 201
    And the register response should contain user details
    And the register response should not contain passwordHash

  Scenario: Reject duplicate username registration
    Given I have already registered a valid user
    When I send the register request again with the same username
    Then the response status code should be 409

  Scenario: Reject invalid email during registration
    Given I have registration details with invalid email
    When I send the register request
    Then the response status code should be 422

  Scenario: Reject weak password during registration
    Given I have registration details with weak password
    When I send the register request
    Then the response status code should be 422

  Scenario: Login with valid credentials
    Given I have already registered a valid user
    When I send the login request with valid credentials
    Then the response status code should be 200
    And the login response should contain token details

  Scenario: Reject login with wrong password
    Given I have already registered a valid user
    When I send the login request with wrong password
    Then the response status code should be 401

  Scenario: Reject protected endpoint access without token
    When I access a protected endpoint without token
    Then the response status code should be 401