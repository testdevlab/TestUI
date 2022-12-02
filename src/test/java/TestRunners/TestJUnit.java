package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.UIOpen.open;

public class TestJUnit {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Android app test case")
    public void testAndroidApp() {
        Configuration.appPackage = "com.android.vending";
        Configuration.appActivity = ".AssetBrowserActivity";
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        open();
    }

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser() {
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.UDID = "";
        Configuration.installMobileChromeDriver = true;
        open("https://www.google.com")
        .given().setElement(googleLandingPage.getGoogleSearchInput());
    }

    @Test
    @DisplayName("IOS app test case")
    public void testIOSApp() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.useNewWDA = false;
        Configuration.bundleId = "com.apple.Preferences";
        open();
    }

    @Test
    @DisplayName("IOS browser test case")
    public void testIOSBrowser() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.useNewWDA = false;
        open("https://www.facebook.com");
    }
}
