package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import pages.GoogleLandingPage;
import testUI.Configuration;
import testUI.Utils.GridTestUI;

import static testUI.TestUIDriver.*;
import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;
import static testUI.UIUtils.*;
import static testUI.Utils.AppiumHelps.sleep;
import static testUI.Utils.By.*;
import static testUI.Utils.Performance.getListOfCommandsTime;
import static testUI.Utils.Performance.logAverageTime;
import static testUI.elements.TestUI.raiseSoftAsserts;

public class TestBrowser {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopBrowser() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.softAsserts = true;
        Configuration.browser = "chrome";
        open("https://www.google.com");
        UIAssert("the url is not correct",
                getSelenideDriver().getCurrentUrl().equals("https://www.google.com/"));
        googleLandingPage.getGoogleCookies().click();
        googleLandingPage.getGoogleSearchInput().given()
                .waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearchInput().waitFor(5).untilHasValue("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible()
                .then().saveScreenshot("~/Documents" +
                "/screen" +
                ".png");
        logAverageTime();
        System.out.println(getListOfCommandsTime());

        raiseSoftAsserts();
    }

    @Test
    public void setDriverTest() {
        ChromeOptions options = new ChromeOptions();
        Configuration.softAsserts = false;
        options.addArguments("--user-agent=" + "Agent", "--ignore-certificate-errors");
        selenideBrowserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        selenideBrowserCapabilities.setBrowserName("chrome");
        open("https://www.whatsmyua.info/");
        sleep(10000);
    }

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopBrowserSafari() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.browser = "safari";
        Configuration.serverLogLevel = "all";
        Configuration.softAsserts = true;
        open("https://www.google.com");
        System.out.println(getTestUIDriver().getCurrentUrl());
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
        googleLandingPage.getGoogleCookies().click();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible()
                .then().click().saveScreenshot("/Users/alvarolasernalopez/Documents/screen" +
                ".png");
        stop();
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible()
                .then().click().saveScreenshot("/Users/alvarolasernalopez/Documents/screen" +
                ".png");

        raiseSoftAsserts();
    }


    @Test
    @DisplayName("Laptop browser test case, assert status code")
    public void testDesktopBrowserStatusCode() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.logNetworkCalls = true;
        Configuration.browser = "chrome";
        Configuration.remote = "http://localhost:4444/wd/hub";
        open("https://www.google.com")
                .getNetworkCalls().logAllCalls().filterByExactUrl("https://www.google.com/")
                .logFilteredCalls()
                .and()
                .filterByUrl("https://www.google.com/").assertFilteredCallExists()
                .logFilteredCalls().assertStatusCode(200)
                .assertResponseHeader("Content-Type", "text/html; charset=UTF-8");

        stop();
        open("https://www.google.com")
                .getLastNetworkCalls(100).logAllCalls()
                .filterByUrl("https://www.google.com/").logFilteredCalls()
                .assertFilteredCallExists();
        stop();
    }

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopCustomDriverBrowser() {
        Configuration.automationType = DESKTOP_PLATFORM;
        open("https://www.google.com");
        Configuration.browser = "chrome";
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText().shouldHave()
                .currentUrlEqualTo("https://www.google.com/")
                .shouldHave().currentUrlContains("https://www.google");
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible()
                .then().click().saveScreenshot("/Users/alvarolasernalopez/Documents/screen" +
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
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        stop();
    }

    @Test
    @DisplayName("Laptop browser test case one line code")
    public void testAndroidBrowserOneLine() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.useAllure = false;
        Configuration.softAsserts = true;
        Configuration.browser = "chrome";
        GridTestUI gridTestUI = new GridTestUI();
        gridTestUI.setServerURL("http://admin:admin@localhost:8000")
                .setPlatform("linux")
                .setConfiguration();
        Configuration.testUILogLevel = LogLevel.DEBUG;
//        Configuration.browserLogs = true;
        open("https://loadero.com/login")
                .given("I set element").setElement(byCssSelector("#username"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").setValueJs("\\uD83D\\uDE00")
                .given("I set element").setElement(byCssSelector("#password"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").setValueJs("password")
                .then("I find the submit").setElement(byCssSelector("[type=\"submit\"]"))
                .and("I click on it").click();
        raiseSoftAsserts();
    }
}
