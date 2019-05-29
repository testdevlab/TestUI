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
        open("https://www.google.com");
        googleLandingPage.getGoogleSearchInput().given().waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        googleLandingPage.getGoogleSearch().given().waitFor(10).untilIsVisible().then().click();
    }
}
