package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.UIOpen.open;
import static testUI.Utils.By.byMobileCss;
import static testUI.elements.TestUI.E;

public class TestAndroid {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser() {
        open("https://www.google.com");
        E(byMobileCss("#SIvCob")).click();
        googleLandingPage.getGoogleSearchInput().scrollIntoView(true)
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().setValueJs("TestUI")
                .shouldHave().not().emptyAttribute("value");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }

    @Test
    @DisplayName("Android browser test case remote")
    public void testAndroidBrowserRemote() {
        Configuration.appiumUrl = "http://127.0.0.1:4723/wd/hub";
        Configuration.emulatorName = "Nexus_5X_API_27";
        open("https://www.google.com");
        E(byMobileCss("#SIvCob")).click();
        googleLandingPage.getGoogleSearchInput().scrollIntoView(true)
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().setValueJs("TestUI")
                .shouldHave().not().emptyAttribute("value");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser2() {
        Configuration.appiumUrl = "";
        Configuration.androidDeviceName = "";
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput()
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }
}
