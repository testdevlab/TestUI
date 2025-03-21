package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import testUI.Configuration;

import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;

public class TestIOS {

    @Test
    @DisplayName("IOS browser test case")
    public void testIOSBrowser() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.serverLogLevel = "debug";
        Configuration.useNewWDA = false;
        open("https://www.facebook.com");
        stop();
        open("https://www.facebook.com");
    }
}
