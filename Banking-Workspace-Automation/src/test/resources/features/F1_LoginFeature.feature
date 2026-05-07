Feature: F1 Login Authentication

  @AUTH_TC_001
  Scenario Outline: Validate login with different credentials
    Given user is on the home page
    When user clicks the Login navigation button
    And user enters email "<email>" and password "<password>"
    And user clicks the Sign In button
    Then user should see login result "<expectedResult>"

    Examples:
      | email             | password        | expectedResult |
      | validUsername     | validPassword   | success        |
      | validUsername     | invalidPassword | error          |
      | unregisteredEmail | validPassword   | error          |
