package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.GoogleLandingPage;
import testUI.Configuration;

import java.util.ArrayList;

import static testUI.TestUIDriver.*;
import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;
import static testUI.UIUtils.*;
import static testUI.Utils.By.*;
import static testUI.Utils.Performance.logAverageTime;
import static testUI.elements.TestUI.E;
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
        Configuration.headless = true;
        open("https://www.google.com");
        UIAssert("the url is not correct",
                getSelenideDriver().getCurrentUrl().equals("https://www.google.com/"));
        googleLandingPage.getGoogleSearch()
                .then().saveScreenshot("~/screen.png");
        logAverageTime();

        raiseSoftAsserts();
        stop();
    }

    @Test
    public void setDriverTest() {
        ChromeOptions options = new ChromeOptions();
        Configuration.softAsserts = true;
        options.addArguments(
                "--user-agent=Agent", "--ignore-certificate-errors", "--headless", "--remote-allow-origins=*");
        Configuration.chromeOptions = options;
        selenideBrowserCapabilities.setBrowserName("chrome");
        open("https://www.whatismybrowser.com/detect/what-is-my-user-agent/");
        E(byCssSelector("#detected_value a")).waitFor(10).untilHasText("Agent");
        stop();
    }


    @Test
    @DisplayName("Laptop browser test case, assert status code")
    public void testDesktopBrowserStatusCode() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.logNetworkCalls = true;
        Configuration.browser = "chrome";
        Configuration.headless = true;

        open("https://www.google.com")
                .getNetworkCalls().filterByExactUrl("https://www.google.com/")
                .and()
                .filterByUrl("https://www.google.com/").assertFilteredCallExists()
                .assertStatusCode(200)
                .assertResponseHeader("Content-Type", "text/html; charset=UTF-8");

        stop();
        open("https://www.google.com")
                .getLastNetworkCalls(100)
                .filterByUrl("https://www.google.com/")
                .assertFilteredCallExists();
        stop();
    }

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopCustomDriverBrowser() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.softAsserts = true;

        open("https://www.google.com");
        stop();
        String userAgent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--user-agent=" + userAgent);
        options.addArguments("--headless");
        ChromeDriver chromeDriver = new ChromeDriver(options);
        setDriver(chromeDriver);
        open("https://www.whatismybrowser.com/detect/what-is-my-user-agent/");
        E(byCssSelector("#detected_value a")).waitFor(10).untilHasText(userAgent);
        stop();
        open("https://www.google.com");
        stop();
    }

    @Test
    @DisplayName("Laptop browser test case one line code")
    public void testAndroidBrowserOneLine() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.useAllure = false;
        Configuration.softAsserts = true;
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.testUIErrors = new ArrayList<>();
        Configuration.testUILogLevel = LogLevel.DEBUG;
        open("https://loadero.com/login");
        getSelenideDriver().switchTo().frame(1); // It uses iFrame now
        E(byCssSelector("#username"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").setValueJs("\\uD83D\\uDE00")
                .given("I set element").setElement(byCssSelector("#password"))
                .and("I check if visible").waitFor(5).untilIsVisible()
                .and("I send keys").setValueJs("password")
                .then("I find the submit").setElement(byCssSelector("[type=\"submit\"]"))
                .and("I click on it").click();
        raiseSoftAsserts();
        stop();
    }
}
