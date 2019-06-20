package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.FacebookLandingPage;
import pages.GoogleLandingPage;
import pages.LandingPage;
import testUI.Configuration;

import static testUI.UIOpen.open;

public class TestAndroid {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser() {
        open("http://www.crealiza.es");
        googleLandingPage.getGoogleSearchInput().scrollIntoView(true)
                .given("Check search input visible and set value").waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().then("Check that search button visible").waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser2() {
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput()
                .given("Check search input visible and set value").waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().then("Check that search button visible").waitFor(10).untilIsVisible()
                .and("Click on search button").click();
    }
}
