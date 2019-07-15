package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.ADBUtils.checkAndInstallChromedriver;
import static testUI.UIOpen.navigate;
import static testUI.UIOpen.open;
import static testUI.UIUtils.setDevice;
import static testUI.Utils.AppiumHelps.sleep;
import static testUI.Utils.By.*;

public class TestBrowser {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();



    @Test
    @DisplayName("Laptop browser test case")
    public void testAndroidBrowser() {
        Configuration.deviceTests = false;
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible().then().click();
    }

    @Test
    @DisplayName("Laptop browser test case one line code")
    public void testAndroidBrowserOneLine() {
        Configuration.deviceTests = false;
        open("https://loadero.com/login")
                .given("I set element").setElement(byCssSelector("#username"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").setValueJs("\\uD83D\\uDE00")
                .given("I set element").setElement(byCssSelector("#password"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").setValueJs("password")
                .then("I find the submit").setElement(byCssSelector("[type=\"submit\"]"))
                .and("I click on it").click();
        sleep(5000);
    }

    @Test
    public void test() {
        setDevice("emulator-5554", "emulator-5554");
        checkAndInstallChromedriver();
    }
}
