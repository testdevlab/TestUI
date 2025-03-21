package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import testUI.Configuration;

import static testUI.TestUIDriver.getIOSDrivers;
import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;

public class TestIOS {

    @Test
    @DisplayName("IOS browser test case")
    public void testIOSBrowser() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.serverLogLevel = "debug";
        Configuration.useNewWDA = false;
//        Configuration.appiumUrl = "http://IP:4723/wd/hub";
        open("https://www.facebook.com");
        System.out.println(getIOSDrivers().size());
        stop();
        System.out.println(getIOSDrivers().size());
        open("https://www.facebook.com");
    }
}
