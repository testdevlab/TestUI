Feature: Simple Feature

  @testApp
  Scenario: Android app scenario
    When I click on catering
    Then I click on near me
    Then I click on suggested
    Then I click on map

  @testApp
  Scenario: Android app scenario
    When I click on catering
    Then I click on near me
    Then I click on suggested
    Then I click on map

  @testBrowser
  Scenario: Android chrome browser scenario
    When I search for Appium in Google

  @testLaptopBrowser
  Scenario: Laptop browser scenario
    When I search for Appium in Google

  @testLaptopAndMobile
  Scenario: Laptop and Mobile Browser scenario
    Given I am using laptop browser
    Then I search for Appium in Google
    Given I am using mobile browser
    Then I search for TestUI in Google
      And I take screenshot in all devices

  @testLaptopAndMobileFail
  Scenario: Laptop and Mobile Browser scenario
    Given I am using laptop browser
    Then I search for Appium in Google and fail assertion
    Given I am using mobile browser
    Then I search for TestUI in Google

  @IOS
  Scenario: iOS app scenario
    Given I in IOS landing app
    Then I click on the second iOS app button
    And I click on the first iOS app button

  @IOSBrowser
  Scenario: iOS Safari Browser scenario
    When I enter email email@test.com and password password in facebook landing page

  @IOSBrowserFail
  Scenario: iOS Safari Browser scenario
    When I enter email email@test.com and password password in facebook landing page