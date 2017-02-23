ID:1
Feature: Create an account
  In order to create an account
  A guest customer
  Should register on the website

  Scenario: Required information for registration
  Given the guest customer wants to create an account
  When the guest customer registers on the website by providing personal information
  And address
  Then the the guest customer should be able to apply for verification

ID:2
Feature: Buy an item
  In order to order an item
  A registered customer
  Should be logged in to the website

  Scenario: Registered customer buys an item
  Given the registered customer wants to buy an item
  When the customer adds an item to the cart
  And proceeds to checkout
  And chooses a delivery address
  And chooses a shipping option
  And chooses a payment method
  And confirms the order
  Then the order should be complete 
