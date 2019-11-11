package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;
import testUI.Utils.CliTestUI;

import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;
import static testUI.Utils.By.byMobileCss;
import static testUI.Utils.Performance.getListOfCommandsTime;
import static testUI.Utils.Performance.logAverageTime;
import static testUI.elements.TestUI.E;

public class TestAndroidRemote {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser() {
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.appiumUrl = "http://127.0.0.1:4723/wd/hub";
        Configuration.androidDeviceName = "emulator-5554";
        open("https://www.google.com");
        E(byMobileCss("#SIvCob")).click();
        googleLandingPage.getGoogleSearchInput().scrollIntoView(true)
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
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.appiumUrl = "http://127.0.0.1:4723/wd/hub";
        Configuration.androidDeviceName = "emulator-5554";
        open("https://www.google.com");
        E(byMobileCss("#SIvCob")).click();
        googleLandingPage.getGoogleSearchInput().scrollIntoView(true)
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().setValueJs("TestUI")
                .shouldHave().not().emptyAttribute("value");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(10).untilIsVisible()
                .and("Click on search button").click();
        logAverageTime();
        System.out.println(getListOfCommandsTime());
    }

    @Test
    @DisplayName("Test cli testUI")
    public void testCliTestUI() {
        CliTestUI cliTestUI = new CliTestUI();
        cliTestUI.setAppiumRemoteConfiguration("http://localhost:8080");
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput()
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().then("Check that search button visible")
                .waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }
}
