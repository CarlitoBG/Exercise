Feature: User should be able to log in

@id1
Scenario: User is able to log in successfully
Given I am an already registered User
And I open website at {https://www.abv.bg/}
When I enter a valid username in the username field
And I enter a valid password in the password field
And I click on the login button
Then I should be logged in successfully

@id2
Scenario: User is NOT able to log in successfully
Given I am an already registered User
And I open website at {https://www.abv.bg/}
When I enter an invalid username in the username field
And I enter an invalid password in the password field
And I click on the login button
Then an error message should be displayed
