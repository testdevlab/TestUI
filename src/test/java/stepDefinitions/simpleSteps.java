package stepDefinitions;

import testUI.Configuration;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.FacebookLandingPage;
import pages.GoogleLandingPage;
import pages.LandingPage;

import static testUI.TestUIDriver.takeScreenshotAllDevicesMap;
import static testUI.UIUtils.UIAssert;
import static testUI.Utils.AppiumHelps.sleep;
import static testUI.collections.TheCollection.Given.given;
import static testUI.elements.TheElement.theElement;

public class simpleSteps {
    private LandingPage landingPage = new LandingPage();
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();
    private FacebookLandingPage facebookLandingPage = new FacebookLandingPage();

    @When("I click on catering")
    public void iClickOnCatering(){
        landingPage.getCatering().given().waitFor(10).untilIsVisible().then().click();
    }

    @When("I click on near me")
    public void iClickOnNearMe(){
        landingPage.getNearMeCollection().get(1)
                .then().waitFor(5).untilIsVisible().and().click();
    }

    @Then("I click on suggested")
    public void iClickOnSuggested(){
        System.out.println(landingPage.getSuggestedCollection().size());
        landingPage.getSuggestedCollection().get(0)
                .given().shouldHave().containText("Suggest").then().click();
        landingPage.getSuggestedCollection().get(0)
                .given().shouldHave().containNoCaseSensitiveText("suggest").then().click();
        System.out.println(landingPage.getSuggestedCollection().findByVisible().and().getText());
    }

    @Then("I click on map")
    public void iClickOnMap(){
        landingPage.getMap().and().click();
        landingPage.getFakeElement().shouldBe().not().visible();
        // THIS is a example of elements or collections
        // that you rather not having in a Java Page Object
        theElement().withText("Suggested").shouldBe().visible();
        given().aCollection().withText("Suggested").get(0).shouldBe().visible();
    }


    @When("I search for {word} in Google")
    public void iClickOnGoogleSearching(String search){
    }

    @Then("I take screenshot in all devices")
    public void iTakeScreenshotInAllDevices() {
        takeScreenshotAllDevicesMap(true);
    }

    @When("I search for {word} in Google and fail assertion")
    public void iClickOnGoogleSearchingAndFail(String search){
    }

    @Given("I am using {word} browser")
    public void iAmUsingLaptopBrowser(String type) {
        Configuration.automationType = type;
    }

    @Given("I in IOS landing app")
    public void iInIOSLandingApp() {
        landingPage.getIosSecondElement()
                .given().waitFor(5).untilIsVisible();
    }

    @Then("I click on the second iOS app button")
    public void iClickOnSecondButton() {
        landingPage.getIosSecondElement()
                .given().waitFor(5).untilIsVisible()
                .then().click();
    }

    @Then("I click on the first iOS app button")
    public void iClickOnFirstButton() {
        landingPage.getIosFirstElement()
                .given().shouldBe().visible()
                .then().click();
    }

    @When("I enter email {word} and password {word} in facebook landing page")
    public void iTapSearchOnSafariGoogle(String email, String password) {
        facebookLandingPage.getSafariFacebookEmailDiv().click();
        facebookLandingPage.getSafariFacebookEmailInput()
                .given().waitFor(5).untilIsVisible()
                .when().sendKeys(email)
                .then().shouldHave().theAttribute("value")
                .and().shouldHave().value(email)
                .and().shouldHave().attribute("value").not().equalTo("whatever");
        facebookLandingPage.getSafariFacebookEmailInput()
                .then().waitFor(5).untilHasAttribute("value").equalTo(email);
        facebookLandingPage.getSafariFacebookPasswordInput()
                .given().sendKeys(password)
                .then().shouldHave().value(password)
                .then().clear();
        sleep(2000);
    }
}
