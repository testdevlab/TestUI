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
import static testUI.UIUtils.executeJs;
import static testUI.Utils.AppiumHelps.sleep;
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
        Configuration.UDID = "emulator-5554";
        Configuration.appiumUrl = "http://10.1.21.153:4723/";
        open("https://www.google.com");
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getMobileElement());
        sleep(2000);
        stop();
        Configuration.testUILogLevel = LogLevel.DEBUG;
        open("https://www.google.com");
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getMobileElement());
    }

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser2() {
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.appiumUrl = "http://10.1.21.153:4723/";
        Configuration.UDID = "emulator-5554";
        open("https://www.google.com");
    }
}
