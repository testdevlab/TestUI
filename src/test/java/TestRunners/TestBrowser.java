package TestRunners;

import io.netty.handler.logging.LogLevel;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.GoogleLandingPage;
import testUI.Configuration;
import testUI.Utils.GridTestUI;

import java.util.HashMap;
import java.util.Map;

import static testUI.TestUIDriver.getSelenideDriver;
import static testUI.TestUIDriver.setDriver;
import static testUI.TestUIServer.stop;
import static testUI.UIOpen.open;
import static testUI.UIUtils.*;
import static testUI.Utils.AppiumHelps.sleep;
import static testUI.Utils.By.*;
import static testUI.Utils.Performance.getListOfCommandsTime;
import static testUI.Utils.Performance.logAverageTime;
import static testUI.elements.TestUI.E;

public class TestBrowser {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopBrowser() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.testUILogLevel = LogLevel.DEBUG;
        Configuration.browser = "chrome";
        open("https://www.google.com");
        UIAssert("the url is not correct",
                getSelenideDriver().getCurrentUrl().equals("https://www.google.com/"));
        googleLandingPage.getGoogleSearchInput().given()
                .waitFor(5).untilIsVisible();
        executeJs("arguments[0].value='TestUI';", googleLandingPage.getGoogleSearchInput()
                .getSelenideElement().getWrappedElement());
        googleLandingPage.getGoogleSearchInput().given().shouldBe().visible().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().shouldHave().not().emptyText();
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible()
                .then().saveScreenshot("/Users/alvarolasernalopez/Documents" +
                "/screen" +
                ".png");
        logAverageTime();
        System.out.println(getListOfCommandsTime());
    }

    @Test
    public void setDriverTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=" + "Agent", "--ignore-certificate-errors");
        selenideBrowserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        selenideBrowserCapabilities.setBrowserName("firefox");
        open("https://www.whatsmyua.info/");
        sleep(10000);
    }

    @Test
    @DisplayName("Laptop browser test case")
    public void testDesktopBrowserSafari() {
        Configuration.automationType = DESKTOP_PLATFORM;
        Configuration.browser = "safari";
        Configuration.serverLogLevel = "all";
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible();
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
        Configuration.browser = "chrome";
        GridTestUI gridTestUI = new GridTestUI();
        gridTestUI.setServerURL("http://alvaro:password@10.2.5.202:8000")
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
    }

    @Test
    public void testUIWithLoadero() {

        String linkId = "AbbzyH2Gl_dPBPk0";
        int participant = 1;
        sleep(participant * 15 * 1000);
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 1);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("no-sandbox");
        options.addArguments("use-fake-ui-for-media-stream");
        options.addArguments("use-fake-device-for-media-stream");
        ChromeDriver driver = new ChromeDriver(options);
        setDriver(driver);
        Configuration.testUILogLevel = LogLevel.DEBUG;
        open("https://m.me/v/" + linkId);
        E(byId("username")).sendKeys("Test");
        E(byCssSelector("._6z07")).waitFor(10).untilIsVisible().click();
        for (int i = 0; i < 10; i++) {
            try {
                E(byCssSelector("[aria-label=\"Mute microphone\"]")).waitFor(10).untilIsVisible().click();
                E(byCssSelector("[aria-label=\"Unmute microphone\"]")).waitFor(4).untilIsVisible();
                break;
            } catch(Error e) {
                System.out.println(e.getMessage());
            }
        }
        sleep(600000);
    }
}
