package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.ADBUtils.checkAndInstallChromedriver;
import static testUI.UIOpen.navigate;
import static testUI.UIOpen.open;
import static testUI.UIUtils.setDevice;
import static testUI.Utils.By.byName;
import static testUI.Utils.By.byXpath;

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
        open("https://www.google.com")
                .given("I set element").setElement(byXpath("//input[@name='q']"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").sendKeys("TestUI")
                .then("I find the search button").setElement(byName("btnK"))
                .and("I click on it").click()
                .when("I navigate to the google main page").navigateTo("https://www.google.com")
                .then("I check that the search field is visible").setElement(byXpath("//input[@name='q']")).shouldBe().visible();
    }

    @Test
    public void test() {
        setDevice("emulator-5554", "emulator-5554");
        checkAndInstallChromedriver();
    }
}
