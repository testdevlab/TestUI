package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;

import static testUI.TestUIDriver.getAndroidTestUIDriver;
import static testUI.UIOpen.open;

public class TestAndroid {
    private GoogleLandingPage googleLandingPage = new GoogleLandingPage();

    @Test
    @DisplayName("Android browser test case")
    public void testAndroidBrowser() {
        open("https://www.google.com");
//        getAndroidTestUIDriver().toggleWifi();
//        getAndroidTestUIDriver().toggleWifi();
        googleLandingPage.getGoogleSearchInput().scrollIntoView(true)
                .given("Check search input visible and set value").waitFor(5).untilIsVisible().then().setValueJs("TestUI")
                .shouldHave().not().emptyAttribute("value");
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
