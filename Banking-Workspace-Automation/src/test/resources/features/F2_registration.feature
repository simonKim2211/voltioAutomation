Feature: F2 Registration

  @REG_TC_001
  Scenario Outline: Validate registration with different inputs
    Given user opens the registration page link
    When registration account type step is displayed
    And user selects account type "<accountType>" and continues registration
    And user enters registration name "<name>" email "<email>" password "<password>" and address "<address>"
    And user submits the registration form
    Then user should see registration result "<expectedResult>"

    Examples:
      | accountType | name     | email                   | password       | address     | expectedResult |
      | personal    | autoName | sharedRegistrationEmail | strongPassword | autoAddress | success        |
      | personal    | autoName | sharedRegistrationEmail | strongPassword | autoAddress | error          |
      | business    | autoName | invalidEmail            | strongPassword | autoAddress | error          |
      | business    | autoName | uniqueEmail             | weakPassword   | autoAddress | error          |

