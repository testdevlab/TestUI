package testUI.Utils;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static testUI.TestUIDriver.*;
import static testUI.UIUtils.UIAssert;

public class AppiumHelps {
    public static boolean exists(By element, String accessibilityId) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).isDisplayed();
            return getMobileElement(accessibilityId).isDisplayed();
        } catch (WebDriverException e) {
            if (e.getMessage().contains("Unable to locate element")) {
                return false;
            } else {
                throw new WebDriverException(e);
            }
        }
    }

    public static boolean exists(By element, String accessibilityId, int index) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).isDisplayed();
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).isDisplayed();
        } catch (WebDriverException e) {
            if (e.getMessage().contains("Unable to locate element")) {
                return false;
            } else {
                throw new WebDriverException(e);
            }
        }
    }

    public static boolean visible(By element, String accessibilityId) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).isDisplayed();
            return getMobileElement(accessibilityId).isDisplayed();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean visible(By element, String accessibilityId, int index) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).isDisplayed();
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).isDisplayed();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean enable(By element, String accessibilityId) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).isEnabled();
            return getMobileElement(accessibilityId).isEnabled();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean enable(By element, String accessibilityId, int index) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).isEnabled();
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).isEnabled();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean value(By element, String accessibilityId, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute("value").equals(text);
            return getMobileElement(accessibilityId).getAttribute("value").equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean value(By element, String accessibilityId, int index, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getAttribute("value").equals(text);
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getAttribute("value").equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accessibilityId, int index, String Attribute, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getAttribute(Attribute).equals(text);
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getAttribute(Attribute).equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accessibilityId, int index, String Attribute) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getAttribute(Attribute) != null;
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getAttribute(Attribute) != null;
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean emptyAttribute(By element, String accessibilityId, int index, String Attribute) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getAttribute(Attribute) == null ||
                        ((MobileElement) getDriver().findElements(element).get(index)).
                                getAttribute(Attribute).isEmpty();
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getAttribute(Attribute) == null ||
                    ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                            getAttribute(Attribute).isEmpty();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accessibilityId, String Attribute, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute(Attribute).equals(text);
            return getMobileElement(accessibilityId).getAttribute(Attribute).equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accessibilityId, String Attribute) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute(Attribute) != null;
            return getMobileElement(accessibilityId).getAttribute(Attribute) != null;
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean emptyAttribute(By element, String accessibilityId, String Attribute) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute(Attribute) == null ||
                        getDriver().findElement(element).getAttribute(Attribute).isEmpty();
            return getMobileElement(accessibilityId).getAttribute(Attribute) == null ||
                    getMobileElement(accessibilityId).getAttribute(Attribute).isEmpty();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean equalsText(By element, String accessibilityId, int index, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getText().contains(text);
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean emptyText(By element, String accessibilityId, int index) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getText().isEmpty();
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getText().isEmpty();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean equalsText(By element, String accessibilityId, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getText().equals(text);
            return getMobileElement(accessibilityId).getText().equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean emptyText(By element, String accessibilityId) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getText().isEmpty();
            return getMobileElement(accessibilityId).getText().isEmpty();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsText(By element, String accessibilityId, String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).getText().contains(text);
            return getMobileElement(accessibilityId).getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsText(By element,
                                       String accessibilityId,
                                       int index,
                                       String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getText().contains(text);
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsAttribute(By element,
                                       String accessibilityId,
                                       int index,
                                       String attr,
                                       String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getAttribute(attr).contains(text);
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsTextNoCaseSensitive(By element,
                                                      String accessibilityId,
                                                      String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return getDriver().findElement(element).
                        getText().toLowerCase().contains(text.toLowerCase());
            return getMobileElement(accessibilityId).
                    getText().toLowerCase().contains(text.toLowerCase());
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsTextNoCaseSensitive(
            By element,
            String accessibilityId,
            int index,
            String text) {
        try {
            if (accessibilityId == null || accessibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).
                        getText().toLowerCase().contains(text.toLowerCase());
            return ((MobileElement) getMobileElementList(accessibilityId).get(index)).
                    getText().toLowerCase().contains(text.toLowerCase());
        } catch (Exception var4) {
            return false;
        }
    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(var3);
        }
    }

    private static List getMobileElementList(String locator) {
        switch (locator.split(": ")[0]) {
            case "accessibilityId":
                return getDriver().findElementsByAccessibilityId(locator.split(": ")[1]);
            case "className":
                return getDriver().findElementsByClassName(locator.split(": ")[1]);
            case "androidUIAutomator":
                return getAndroidTestUIDriver().findElementsByAndroidUIAutomator(locator.split(": ")[1]);
            case "predicate":
                return getIOSTestUIDriver().findElementsByIosNsPredicate(locator.split(": ")[1]);
            case "classChain":
                return getIOSTestUIDriver().findElementsByIosClassChain(locator.split(": ")[1]);
            case "name":
                return getDriver().findElementsByName(locator.split(": ")[1]);
            case "xpath":
                return getDriver().findElementsByXPath(locator.split(": ")[1]);
            case "id":
                return getDriver().findElementsById(locator.split(": ")[1]);
            case "css":
                return getDriver().findElementsByCssSelector(locator.split(": ")[1]);
            default:
                UIAssert("The type of locator is not valid! " +
                                locator.split(": ")[0],
                        false
                );
                return new ArrayList();
        }
    }

    private static MobileElement getMobileElement(String locator) {
        switch (locator.split(": ")[0]) {
            case "accessibilityId":
                return (MobileElement) getDriver().findElementByAccessibilityId(locator.split(": ")[1]);
            case "className":
                return (MobileElement) getDriver().findElementByClassName(locator.split(": ")[1]);
            case "androidUIAutomator":
                return (MobileElement) getAndroidTestUIDriver().findElementByAndroidUIAutomator(locator.split(": ")[1]);
            case "predicate":
                return (MobileElement) getIOSTestUIDriver().findElementByIosNsPredicate(locator.split(": ")[1]);
            case "classChain":
                return (MobileElement) getIOSTestUIDriver().findElementByIosClassChain(locator.split(": ")[1]);
            case "name":
                return (MobileElement) getDriver().findElementByName(locator.split(": ")[1]);
            case "xpath":
                return (MobileElement) getIOSTestUIDriver().findElementByXPath(locator.split(": ")[1]);
            case "id":
                return (MobileElement) getDriver().findElementById(locator.split(": ")[1]);
            case "css":
                return (MobileElement) getDriver().findElementByCssSelector(locator.split(": ")[1]);
            default:
                UIAssert(
                        "The type of locator is not valid! " +
                                locator.split(": ")[0],
                        false
                );
                return (MobileElement) getAndroidTestUIDriver().findElement("", "");
        }
    }
}
