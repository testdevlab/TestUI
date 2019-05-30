package TestRunners;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import pages.GoogleLandingPage;
import testUI.Configuration;

import static testUI.UIOpen.navigate;
import static testUI.UIOpen.open;
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
    @DisplayName("Laptop browser test case")
    public void testAndroidBrowserOneLine() {
        Configuration.deviceTests = false;
        open("https://www.google.com")
        .setElement(byXpath("//button[@class='Tg7LZd']")).given().waitFor(5).untilIsVisible().then().sendKeys("TestUI")
        .setElement(byXpath("//input[@name='q']")).click().waitFor(10).untilHasText("test");

        navigate("https://www.google.com")
        .setElement(byXpath("//button[@class='Tg7LZd']")).given().waitFor(5).untilIsVisible().then().sendKeys("TestUI")
        .setElement(byXpath("//input[@name='q']")).click().waitFor(10).untilHasText("test");
    }
}
