Feature: Get , put ,post and delete

  Scenario: User get the data
    Given User fetch the data from api
    And User verify the response

  Scenario: User Create the data
    Given User pass the required parameters
      | name | job    |
      | Test | QATest |
    And User request to create the data
    And User verify that the data is created