package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;
import testUI.TestUIDriver;

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
        googleLandingPage.getGoogleSearchInput().scrollTo().view(true)
                .given("Check search input visible and set value").waitFor(5)
                .untilIsVisible();
        stop();
        Configuration.testUILogLevel = LogLevel.DEBUG;
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().scrollTo().view(true)
                .given("Check search input visible and set value");
    }

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser2() {
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.appiumUrl = "";
        Configuration.androidDeviceName = "";
        open("https://www.google.com");
    }
}
