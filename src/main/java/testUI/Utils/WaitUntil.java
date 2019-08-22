package testUI.Utils;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import testUI.Configuration;

import java.util.List;

import static testUI.TestUIDriver.getDrivers;
import static testUI.TestUIDriver.takeScreenshot;
import static testUI.UIUtils.getDevicesNames;
import static testUI.UIUtils.getIOSDevices;
import static testUI.Utils.AppiumHelps.*;

public class WaitUntil {

    private static void assertFunction(String element, String accesibility, String reason, boolean found) {
        if (!found) {
            if (Configuration.useAllure) {
                boolean test = Configuration.deviceTests;
                Configuration.deviceTests = true;
                for (int index = 0; index < getDrivers().size(); index++) {
                    byte[] screenshot = takeScreenshot(index);
                    List<String> deviceName = Configuration.iOSTesting && index == 0 ? getIOSDevices() : getDevicesNames();
                    Allure.getLifecycle().addAttachment("Screenshot Mobile " + deviceName.get(index), "image/png", "png", screenshot);
                }
                Configuration.deviceTests = false;
                if (WebDriverRunner.driver().hasWebDriverStarted()) {
                    try {
                        byte[] screenshot = takeScreenshot();
                        Allure.getLifecycle().addAttachment("Screenshot Laptop Browser", "image/png", "png", screenshot);
                    } catch (Exception e) {
                        System.err.println("Could not take a screenshot in the laptop browser...");
                    }
                }
                Configuration.deviceTests = test;
            }
            if (accesibility == null || accesibility.isEmpty()) {
                throw new Error("The element '" + element + "' " + reason);
            } else {
                throw new Error("The element 'By." + accesibility.split(": ")[0] + ": " + accesibility.split(": ")[1] + "' " + reason);
            }
        }
    }

    private static String getElementString(org.openqa.selenium.By element) {
        if (element != null)
            return element.toString();
        return "";
    }

    public static void waitUntilVisible(org.openqa.selenium.By element, String accessibility, int Seconds, boolean isVisible) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (visible(element, accessibility) == isVisible) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility, "is not visible after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilVisible(org.openqa.selenium.By element, String accesibility, int index, int Seconds,
                                        boolean isVisible) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (visible(element,accesibility, index) ==isVisible) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility, "is not visible after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilClickable(org.openqa.selenium.By element, String accessibility) {
        long t= System.currentTimeMillis();
        long end = t+(Configuration.timeout * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if ((enable(element, accessibility) || Configuration.iOSTesting) && visible(element, accessibility)) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"' is not clickable after "
                + Configuration.timeout + " seconds!", found);
    }

    public static void waitUntilClickable(org.openqa.selenium.By element, String accessibility, int index) {
        long t= System.currentTimeMillis();
        long end = t+(Configuration.timeout * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element, accessibility, index) && visible(element, accessibility, index)) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility, "' is not clickable after "
                + Configuration.timeout + " seconds!", found);
    }

    public static void waitUntilEnable(org.openqa.selenium.By element, String accessibility, int Seconds, boolean isEnabled) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element,accessibility) == isEnabled) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"' is not enabled after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilEnable(org.openqa.selenium.By element, String accessibility, int index, int Seconds, boolean isEnabled) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element, accessibility, index) == isEnabled) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility, "is not enabled after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilExist(org.openqa.selenium.By element, String accessibility, int Seconds, boolean Exist) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (exists(element, accessibility) == Exist) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"' not exists after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilExist(org.openqa.selenium.By element, String accessibility, int index, int Seconds, boolean exists) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (exists(element, accessibility, index) == exists) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,"not exists after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilContainsText(org.openqa.selenium.By element, String accessibility, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsText(element, accessibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"has not containText '" + text + "' after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilContainsText(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsText(element, accessibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,
                "with containText '" + text + "' is not visible after "
                        + Seconds + " seconds!", found);
    }

    public static void waitUntilContainsTextNoCaseSensitive(org.openqa.selenium.By element, String accessibility, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsTextNoCaseSensitive(element, accessibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"has not containText '" + text + "' after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilContainsTextNoCaseSensitive(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String text,
                                                     boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsTextNoCaseSensitive(element, accessibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,
                "with containText '" + text + "' is not visible after " + Seconds + " seconds!", found);
    }

    public static void waitUntilExactText(org.openqa.selenium.By element, String accessibility, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (equalsText(element, accessibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"has not containText '" + text + "' after " + Seconds + " seconds!", found);
    }

    public static void waitUntilEmptyText(org.openqa.selenium.By element, String accessibility, int Seconds, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (emptyText(element, accessibility) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"should be empty or is not visible after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilExactText(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (equalsText(element, accessibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,
                "with containText '" + text + "' is not visible after " + Seconds + " seconds!", found);
    }

    public static void waitUntilEmptyText(org.openqa.selenium.By element, String accessibility, int index, int Seconds, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (emptyText(element, accessibility, index) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,
                "with should be empty text or is not visible after " + Seconds + " seconds!", found);
    }

    public static void waitUntilHasValue(org.openqa.selenium.By element, String accessibility, int Seconds, String text, boolean hasVallue) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (value(element, accessibility, text) == hasVallue) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"has not value '" + text + "' after "
                + Seconds + " seconds!", found);
    }

    public static void waitUntilHasValue(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String text, boolean hasValue) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (value(element, accessibility, index, text) == hasValue) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,"with value '"
                + text + "is not visible after" + Seconds + " seconds!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String Attribute, String value,
                                             boolean hasValue) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, index, Attribute, value) == hasValue) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,"has no attribute '" + Attribute
                + "' with value '" + value + "' after " + Seconds + " seconds!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String Attribute,
                                             boolean hasAttribute) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, index, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,"has no attribute '" + Attribute
                + "' after " + Seconds + " seconds!", found);
    }

    public static void waitUntilEmptyAttribute(org.openqa.selenium.By element, String accessibility, int index, int Seconds, String Attribute,
                                           boolean hasAttribute) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (emptyAttribute(element, accessibility, index, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,"should have attribute '" + Attribute
                + "' empty or null!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accessibility, int Seconds, String Attribute, String value, boolean is) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, Attribute, value) == is) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"has no attribute '" + Attribute
                + "' with value '" + value + "'!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accessibility, int Seconds, String Attribute, boolean hasAttribute) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"has no attribute '" + Attribute + "'!", found);
    }

    public static void waitUntilEmptyAttribute(org.openqa.selenium.By element, String accessibility, int Seconds, String Attribute, boolean hasAttribute) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (emptyAttribute(element, accessibility, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accessibility,"should have attribute '" + Attribute
                + "' empty or null!", found);
    }
}
