package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.FacebookLandingPage;
import testUI.Configuration;

import static testUI.TestUIDriver.getIOSTestUIDriver;
import static testUI.UIOpen.open;

public class TestIOS {
    private FacebookLandingPage facebookLandingPage = new FacebookLandingPage();

    @Test
    @DisplayName("IOS browser test case")
    public void testIOSBrowser() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.serverLogLevel = "all";
        open("https://www.facebook.com");
        System.out.println(getIOSTestUIDriver().getBatteryInfo().getState());
    }
}
