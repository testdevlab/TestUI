package testUI.Utils;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.By;

import static testUI.TestUIDriver.getDriver;

public class AppiumHelps {
    public static boolean exists(By element, String accesibilityId) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).isDisplayed();
            return getDriver().findElementByAccessibilityId(accesibilityId).isDisplayed();
        } catch (StaleElementReferenceException var4) {
            return false;
        }
    }

    public static boolean exists(By element, String accesibilityId, int index) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement)getDriver().findElements(element).get(index)).isDisplayed();
            return ((MobileElement)getDriver().findElementsByAccessibilityId(accesibilityId)).isDisplayed();
        } catch (StaleElementReferenceException var4) {
            return false;
        }
    }

    public static boolean visible(By element, String accesibilityId) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).isDisplayed();
            return getDriver().findElementByAccessibilityId(accesibilityId).isDisplayed();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean visible(By element, String accesibilityId, int index) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).isDisplayed();
            return ((MobileElement) getDriver().findElementsByAccessibilityId(accesibilityId)).isDisplayed();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean enable(By element, String accesibilityId) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).isEnabled();
            return getDriver().findElementByAccessibilityId(accesibilityId).isEnabled();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean enable(By element, String accesibilityId, int index) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement)getDriver().findElements(element).get(index)).isEnabled();
            return ((MobileElement)getDriver().findElementsByAccessibilityId(accesibilityId)).isEnabled();
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean value(By element, String accesibilityId, String text) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute("value").equals(text);
            return getDriver().findElementByAccessibilityId(accesibilityId).getAttribute("value").equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean value(By element, String accesibilityId, int index, String text) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement)getDriver().findElements(element).get(index)).getAttribute("value").equals(text);
            return ((MobileElement)getDriver().findElementsByAccessibilityId(accesibilityId)).getAttribute("value").equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accesibilityId, int index, String Attribute, String text) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement)getDriver().findElements(element).get(index)).getAttribute(Attribute).equals(text);
            return ((MobileElement)getDriver().findElementsByAccessibilityId(accesibilityId)).getAttribute(Attribute).equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accesibilityId, int index, String Attribute) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement)getDriver().findElements(element).get(index)).getAttribute(Attribute) != null;
            return ((MobileElement)getDriver().findElementsByAccessibilityId(accesibilityId)).getAttribute(Attribute) != null;
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accesibilityId, String Attribute, String text) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute(Attribute).equals(text);
            return getDriver().findElementByAccessibilityId(accesibilityId).getAttribute(Attribute).equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean attribute(By element, String accesibilityId, String Attribute) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).getAttribute(Attribute) != null;
            return getDriver().findElementByAccessibilityId(accesibilityId).getAttribute(Attribute) != null;
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean equalsText(By element, String accesibilityId, int index, String text) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement)getDriver().findElements(element).get(index)).getText().contains(text);
            return ((MobileElement)getDriver().findElementsByAccessibilityId(accesibilityId)).getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean equalsText(By element, String accesibilityId, String text) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).getText().equals(text);
            return getDriver().findElementByAccessibilityId(accesibilityId).getText().equals(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsText(By element, String accesibilityId, String text) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).getText().contains(text);
            return getDriver().findElementByAccessibilityId(accesibilityId).getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsText(By element, String accesibilityId, int index, String text) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).getText().contains(text);
            return ((MobileElement) getDriver().findElementsByAccessibilityId(accesibilityId)).getText().contains(text);
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsTextNoCaseSensitive(By element, String accesibilityId, String text) {
        try {
            if (accesibilityId.isEmpty())
                return getDriver().findElement(element).getText().toLowerCase().contains(text.toLowerCase());
            return getDriver().findElementByAccessibilityId(accesibilityId).getText().toLowerCase().contains(text.toLowerCase());
        } catch (Exception var4) {
            return false;
        }
    }

    public static boolean containsTextNoCaseSensitive(By element, String accesibilityId, int index, String text) {
        try {
            if (accesibilityId.isEmpty())
                return ((MobileElement) getDriver().findElements(element).get(index)).getText().toLowerCase().contains(text.toLowerCase());
            return ((MobileElement) getDriver().findElementsByAccessibilityId(accesibilityId)).getText().toLowerCase().contains(text.toLowerCase());
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
}
