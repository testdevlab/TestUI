package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.FacebookLandingPage;
import pages.GoogleLandingPage;
import pages.LandingPage;
import testUI.Configuration;

import static testUI.UIOpen.open;

public class TestJUnit {
    private LandingPage landingPage = new LandingPage();
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();
    private FacebookLandingPage facebookLandingPage = new FacebookLandingPage();

    @Test
    @DisplayName("Android app test case")
    public void testAndroidApp() {
        Configuration.androidAppPath = "1188.apk";
        open();
        landingPage.getCatering().given().waitFor(10).untilIsVisible().then().click();
        landingPage.getNearMeCollection().get(1).then().waitFor(5).untilIsVisible().and().click();
        System.out.println(landingPage.getSuggestedCollection().findByVisible().and().getText());
    }

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser() {
        open("https://www.google.com")
        .given().setElement(googleLandingPage.getGoogleSearchInput())
        .and().waitFor(5).untilIsVisible().then().sendKeys("TestUI")
        .when().setElement(googleLandingPage.getGoogleSearch())
        .and().waitFor(10).untilIsVisible().then().click();
    }

    @Test
    @DisplayName("IOS app test case")
    public void testIOSApp() {
        Configuration.iOSTesting = true;
        Configuration.iOSVersion = "12.2";
        Configuration.iOSAppPath = "/Users/alvarolasernalopez/Documents/Automation/testapp/build/Release-iphonesimulator/testapp.app";
        Configuration.iOSDeviceName = "iPhone 6";
        open();
    }

    @Test
    @DisplayName("IOS browser test case")
    public void testIOSBrowser() {
        Configuration.iOSTesting = true;
        open("https://www.facebook.com");
        facebookLandingPage.getSafariFacebookEmailDiv().click();
        facebookLandingPage.getSafariFacebookEmailInput()
                .given().waitFor(5).untilIsVisible()
                .when().sendKeys("email@email.com")
                .then().shouldHave().theAttribute("value")
                .and().shouldHave().value("email@email.com")
                .and().shouldHave().attribute("value").not().equalTo("whatever");
        facebookLandingPage.getSafariFacebookEmailInput()
                .then().waitFor(5).untilHasAttribute("value").equalTo("email@email.com");
        facebookLandingPage.getSafariFacebookPasswordInput()
                .given().sendKeys("password")
                .then().shouldHave().value("password")
                .then().clear();
    }
}
