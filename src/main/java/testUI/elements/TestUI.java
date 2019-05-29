package testUI.elements;

import testUI.Configuration;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import static testUI.TestUIDriver.*;
import static testUI.TestUIDriver.takeScreenshot;
import static testUI.UIUtils.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TestUI {

    public static UIElement E(By element) {
        return new UIElement(element, element, element, 0, false, "","");
    }

    public static UIElement E(String accesibilityId) {
        return new UIElement(null,null,null,0,false, accesibilityId, accesibilityId);
    }

    public static UIElement Ex(String xpath) {
        return new UIElement(By.xpath(xpath), By.xpath(xpath), By.xpath(xpath),0,false,"","");
    }

    protected By getAppiumElement(By iOSElement, By AndroidElement) {
        if (Configuration.iOSTesting)
            return iOSElement;
        return AndroidElement;
    }

    protected String getAccesibilityId(String accesibilityIdiOS, String accesibilityId) {
        if (Configuration.iOSTesting)
           return accesibilityIdiOS;
        return accesibilityId;
    }

    protected MobileElement getElement(String accesibilityIdiOS, String accesibilityId,By iOSElement, By element, int index,
                                       boolean collection) {
        if (collection) {
            if (!getAccesibilityId(accesibilityIdiOS,accesibilityId).isEmpty()) {
                return (MobileElement) getDriver().findElementsByAccessibilityId(getAccesibilityId(accesibilityIdiOS,accesibilityId)).get(index);
            }
            return (MobileElement) getDriver().findElements(getAppiumElement(iOSElement, element)).get(index);
        }
        if (!getAccesibilityId(accesibilityIdiOS,accesibilityId).isEmpty()) {
            return (MobileElement) getDriver().findElementByAccessibilityId(getAccesibilityId(accesibilityIdiOS,accesibilityId));
        }
        return (MobileElement) getDriver().findElement(getAppiumElement(iOSElement, element));
    }

    protected SelenideElement getSelenide(By element, int index, boolean collection) {
        if (collection)
            return $$(element).get(index);
        return $(element);
    }

    protected void selenideAssert(Condition condition, int time, By SelenideElement, int index, boolean collection) {
        try {
            getSelenide(SelenideElement,index, collection).waitUntil(condition, time * 1000);
        } catch (Throwable e) {
            boolean test = Configuration.deviceTests;
            Configuration.deviceTests = true;
            for (int in = 0; in < getDrivers().size(); in++) {
                byte[] screenshot = takeScreenshot(in);
                Allure.getLifecycle().addAttachment("Screenshot Mobile " + getDevicesNames().get(in), "image/png", "png", screenshot);
            }
            Configuration.deviceTests = false;
            if (WebDriverRunner.driver().hasWebDriverStarted()) {
                try {
                    byte[] screenshot = takeScreenshot();
                    Allure.getLifecycle().addAttachment("Screenshot Laptop Browser", "image/png", "png", screenshot);
                } catch (Exception ex) {
                    System.err.println("Could not take a screenshot in the laptop browser...");
                }
            }
            Configuration.deviceTests = test;
            getSelenide(SelenideElement,index, collection).waitUntil(condition, 0);
        }
    }
}
