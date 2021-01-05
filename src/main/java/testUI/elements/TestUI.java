package testUI.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import testUI.Configuration;
import testUI.TestUIDriver;
import testUI.Utils.TestUIException;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static testUI.Configuration.useAllure;
import static testUI.TestUIDriver.*;
import static testUI.UIUtils.*;
import static testUI.elements.Element.getStep;

public class TestUI {

    public static UIElement E(By element) {
        return new Element(
                element,
                element,
                element,
                0,
                false,
                "",
                ""
        );
    }

    public static UIElement E(String type, String element) {
        return new Element(
                type + ": " + element);
    }


    public static UIElement E(String accesibilityId) {
        if (accesibilityId.contains(": ")) {
            return new Element(
                    null,
                    null,
                    null,
                    0,false,
                     accesibilityId,
                    accesibilityId
            );
        }
        return new Element(
                null,
                null,
                null,
                0,
                false,
                "accessibilityId: " + accesibilityId,
                "accessibilityId: " + accesibilityId);
    }

    public static UIElement Ex(String xpath) {
        return new Element(
                By.xpath(xpath),
                By.xpath(xpath),
                By.xpath(xpath),
                0,
                false,
                "",
                "");
    }

    protected By getAppiumElement(By iOSElement, By AndroidElement) {
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM))
            return iOSElement;
        return AndroidElement;
    }

    protected String  getStringElement(String accesibilityIdiOS, String accesibilityId,
                                       By iOSElement, By AndroidElement, By SelenideElement) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (getLocator(accesibilityIdiOS,accesibilityId).isEmpty())
                return getAppiumElement(iOSElement, AndroidElement).toString();
            return getLocator(accesibilityIdiOS,accesibilityId);
        } else {
            return SelenideElement.toString();
        }
    }

    protected String getLocator(String accesibilityIdiOS, String accesibilityId) {
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM)) {
            if (accesibilityIdiOS != null && !accesibilityIdiOS.isEmpty())
                return accesibilityIdiOS.split(": ")[1];
            return "";
        } else {
            if (accesibilityId != null && !accesibilityId.isEmpty())
                return accesibilityId.split(": ")[1];
            return "";
        }
    }

    protected String getAccesibilityId(String accesibilityIdiOS, String accesibilityId) {
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM))
            return accesibilityIdiOS;
        return accesibilityId;
    }

    protected List getMobileElementList(String locator) {
        switch (locator.split(": ")[0]) {
            case "accessibilityId":
                return getDriver().findElementsByAccessibilityId(locator.split(": ")[1]);
            case "className":
                return getDriver().findElementsByClassName(locator.split(": ")[1]);
            case "androidUIAutomator":
                return getAndroidTestUIDriver()
                        .findElementsByAndroidUIAutomator(locator.split(": ")[1]);
            case "predicate":
                return getIOSTestUIDriver()
                        .findElementsByIosNsPredicate(locator.split(": ")[1]);
            case "classChain":
                return getIOSTestUIDriver()
                        .findElementsByIosClassChain(locator.split(": ")[1]);
            case "name":
                return getDriver().findElementsByName(locator.split(": ")[1]);
            case "xpath":
                return getDriver().findElementsByXPath(locator.split(": ")[1]);
            case "id":
                return getDriver().findElementsById(locator.split(": ")[1]);
            case "css":
                return getDriver().findElementsByCssSelector(locator.split(": ")[1]);
            default:
                UIAssert(
                        "The type of locator is not valid! "
                                + locator.split(": ")[0],
                        false);
                return new ArrayList();
        }
    }

    protected WebElement getMobileElement(String locator) {
        switch (locator.split(": ")[0]) {
            case "accessibilityId":
                return getDriver().findElementByAccessibilityId(locator.split(": ")[1]);
            case "className":
                return getDriver().findElementByClassName(locator.split(": ")[1]);
            case "androidUIAutomator":
                return getAndroidTestUIDriver()
                        .findElementByAndroidUIAutomator(locator.split(": ")[1]);
            case "predicate":
                return getIOSTestUIDriver()
                        .findElementByIosNsPredicate(locator.split(": ")[1]);
            case "classChain":
                return getIOSTestUIDriver()
                        .findElementByIosClassChain(locator.split(": ")[1]);
            case "name":
                return getDriver().findElementByName(locator.split(": ")[1]);
            case "xpath":
                return getDriver().findElementByXPath(locator.split(": ")[1]);
            case "id":
                return getDriver().findElementById(locator.split(": ")[1]);
            case "css":
                return getDriver().findElementByCssSelector(locator.split(": ")[1]);
            default:
                UIAssert(
                        "The type of locator is not valid! "
                                + locator.split(": ")[0],
                        false);
                return getDriver().findElementByName("");
        }
    }

    protected MobileElement getElement(
            String accesibilityIdiOS,
            String accesibilityId,
            By iOSElement,
            By element,
            int index,
            boolean collection) {
        try {
            if (collection) {
                if (!getLocator(accesibilityIdiOS,accesibilityId).isEmpty()) {
                    return (MobileElement) getMobileElementList(
                            getAccesibilityId(accesibilityIdiOS,accesibilityId)).get(index);
                }
                return (MobileElement) getDriver().findElements(
                        getAppiumElement(iOSElement, element)).get(index);
            }
            if (!getLocator(accesibilityIdiOS,accesibilityId).isEmpty()) {
                return (MobileElement) getMobileElement(
                        getAccesibilityId(accesibilityIdiOS,accesibilityId));
            }
            return (MobileElement) getDriver().findElement(getAppiumElement(iOSElement, element));
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
    }

    protected MobileElement getElementWithoutException(
            String accesibilityIdiOS,
            String accesibilityId,
            By iOSElement,
            By element,
            int index,
            boolean collection) {
        if (collection) {
            if (!getLocator(accesibilityIdiOS,accesibilityId).isEmpty()) {
                return (MobileElement) getMobileElementList(
                        getAccesibilityId(accesibilityIdiOS,accesibilityId)).get(index);
            }
            return (MobileElement) getDriver().findElements(
                    getAppiumElement(iOSElement, element)).get(index);
        }
        if (!getLocator(accesibilityIdiOS,accesibilityId).isEmpty()) {
            return (MobileElement) getMobileElement(
                    getAccesibilityId(accesibilityIdiOS,accesibilityId));
        }
        return (MobileElement) getDriver().findElement(getAppiumElement(iOSElement, element));
    }

    protected SelenideElement getSelenide(By element, int index, boolean collection) {
        if (collection)
            return $$(element).get(index);
        return $(element);
    }

    protected void selenideAssert(Condition condition,
                                  int time,
                                  By SelenideElement,
                                  int index,
                                  boolean collection) {
        try {
            getSelenide(SelenideElement,index, collection).waitUntil(condition, time * 1000);
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
    }

    private static boolean screenshotTaken = false;

    public static void setScreenshotTaken(boolean screenshotTaken) {
        TestUI.screenshotTaken = screenshotTaken;
    }

    public static void takeScreenshotsAllure() {
        if (!screenshotTaken && useAllure) {
            if (getStep()) {
                Allure.step("Previous Step Failed!", Status.FAILED);
            }
            String aType = Configuration.automationType;
            if (aType.equals(Configuration.DESKTOP_PLATFORM) && getDevicesNames() != null)
                Configuration.automationType = Configuration.ANDROID_PLATFORM;
            if (aType.equals(Configuration.DESKTOP_PLATFORM) && getIOSDevices() != null)
                Configuration.automationType = Configuration.IOS_PLATFORM;
            for (int in = 0; in < getDrivers().size(); in++) {
                byte[] screenshot = TestUIDriver.takeScreenshot(in);
                List<String> deviceName =
                        Configuration.automationType.equals(Configuration.IOS_PLATFORM)
                                ? getIOSDevices() : getDevicesNames();
                String name = deviceName != null && deviceName.size() > in ?
                        deviceName.get(in) : "Device";
                Allure.getLifecycle().addAttachment(
                        "Screenshot Mobile " +
                                name,
                        "image/png",
                        "png",
                        screenshot
                );
            }
            Configuration.automationType = Configuration.DESKTOP_PLATFORM;
            if (WebDriverRunner.driver().hasWebDriverStarted()) {
                try {
                    byte[] screenshot = TestUIDriver.takeScreenshot();
                    Allure.getLifecycle().addAttachment(
                            "Screenshot Laptop Browser",
                            "image/png",
                            "png",
                            screenshot);
                } catch (Exception ex) {
                    System.err.println("Could not take a screenshot in the laptop browser...");
                }
            }
            Configuration.automationType = aType;
            screenshotTaken = true;
        }
    }
}