package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.Configuration.ANDROID_PLATFORM;
import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;
import static testUI.Utils.By.byMobileCss;
import static testUI.elements.TestUI.E;

public class TestAndroidLocal {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Android browser test case stop")
    public void testAndroidBrowser() {
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.automationType = ANDROID_PLATFORM;
        Configuration.installMobileChromeDriver = true;
        open("https://www.google.com");
        E(byMobileCss("#SIvCob")).click();
        googleLandingPage.getGoogleSearchInput().scrollTo().view(true)
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().setValueJs("TestUI")
                .shouldHave().not().emptyAttribute("value");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(1).untilIsVisible()
                .and("Click on search button").click();
        stop();
        Configuration.testUILogLevel = LogLevel.DEBUG;
        open("https://www.google.com");
        E(byMobileCss("#SIvCob")).click();
        googleLandingPage.getGoogleSearchInput().scrollTo().view(true)
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
        Configuration.testUILogLevel = LogLevel.DEBUG;
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

    @Test
    @DisplayName("Test cli testUI")
    public void testCliTestUI() {
        Configuration.testUILogLevel = LogLevel.INFO;
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput()
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }
}
