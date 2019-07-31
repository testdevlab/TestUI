package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.ADBUtils.checkAndInstallChromedriver;
import static testUI.NetworkCalls.getNetworkCalls;
import static testUI.TestUIDriver.setDriver;
import static testUI.TestUIServer.stop;
import static testUI.UIOpen.navigate;
import static testUI.UIOpen.open;
import static testUI.UIUtils.*;
import static testUI.Utils.By.*;

public class TestBrowser {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();



    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopBrowser() {
        Configuration.logNetworkCalls = true;
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput().getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible().then().click().saveScreenshot("/Users/alvarolasernalopez/Documents/screen" +
                ".png");
    }


    @Test
    @DisplayName("Laptop browser test case, assert status code")
    public void testDesktopBrowserStatusCode() {
        Configuration.deviceTests = false;
        Configuration.logNetworkCalls = true;
        Configuration.remote = "http://localhost:4444/wd/hub";
        open("https://www.google.com")
                .getNetworkCalls().logAllCalls().filterByExactUrl("https://www.google.com/").logFilteredCalls()
                .and()
                .filterByUrl("https://www.google.com/").assertFilteredCallExists().logFilteredCalls().assertStatusCode(200).assertResponseHeader("Content-Type", "text/html; charset=UTF-8");

        stop();
        open("https://www.google.com")
                .getLastNetworkCalls(100).logAllCalls().filterByUrl("https://www.google.com/").logFilteredCalls().assertFilteredCallExists();
    }

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopCustomDriverBrowser() {
        Configuration.deviceTests = false;
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput().getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText().shouldHave().currentUrlEqualTo("https://www.google.com/")
                .shouldHave().currentUrlContains("https://www.google");
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible().then().click().saveScreenshot("/Users/alvarolasernalopez/Documents/screen" +
                ".png");

        stop();
        ChromeOptions options = new ChromeOptions();
        String userAgent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        options.addArguments("--user-agent=" + userAgent);
        ChromeDriver chromeDriver = new ChromeDriver(options);
        setDriver(chromeDriver);
        open("https://www.google.com");
        stop();
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput().getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible().then().click().saveScreenshot("/Users/alvarolasernalopez/Documents/screen" +
                ".png");
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
    }

    @Test
    public void test() {
        setDevice("emulator-5554", "emulator-5554");
        checkAndInstallChromedriver();
    }
}
