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
import static testUI.elements.TestUI.setScreenshotTaken;

public class WaitUntil {

    private static void assertFunction(
            String element,
            String accesibility,
            String reason,
            boolean found) {
        if (!found) {
            if (Configuration.useAllure) {
                String aType = Configuration.automationType;
                if (aType.equals(Configuration.DESKTOP_PLATFORM) && getDevicesNames() != null)
                    Configuration.automationType = Configuration.ANDROID_PLATFORM;
                if (aType.equals(Configuration.DESKTOP_PLATFORM) && getIOSDevices() != null)
                    Configuration.automationType = Configuration.IOS_PLATFORM;
                for (int index = 0; index < getDrivers().size(); index++) {
                    byte[] screenshot = takeScreenshot(index);
                    List<String> deviceName =
                            Configuration.automationType.equals(Configuration.IOS_PLATFORM)
                                    ? getIOSDevices() : getDevicesNames();
                    String name = deviceName != null && deviceName.size() > index ?
                            deviceName.get(index) : "Device";
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
                        byte[] screenshot = takeScreenshot();
                        Allure.getLifecycle().addAttachment(
                                "Screenshot Laptop Browser",
                                "image/png",
                                "png",
                                screenshot
                        );
                    } catch (Exception e) {
                        System.err.println("Could not take a screenshot in the laptop browser...");
                    }
                }
                Configuration.automationType = aType;
            }
            setScreenshotTaken(true);
            String errorMessage;
            if (accesibility == null || accesibility.isEmpty()) {
                errorMessage = "The element '" + element + "' " + reason;
            } else {
                errorMessage = "The element 'By." +
                                accesibility.split(": ", 2)[0] + ": " +
                                accesibility.split(": ", 2)[1] + "' " +
                                reason;
            }
            TestUIException.handleError(errorMessage);
        }
    }

    private static String getElementString(org.openqa.selenium.By element) {
        if (element != null)
            return element.toString();
        return "";
    }

    public static void waitUntilVisible(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            boolean isVisible) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (visible(element, accessibility) == isVisible) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "is not visible after " + time + " ms!",
                found
        );
    }

    public static void waitUntilVisible(
            org.openqa.selenium.By element,
            String accesibility,
            int index,
            int seconds,
            boolean isVisible) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (visible(element, accesibility, index) == isVisible) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accesibility,
                "is not visible after " + time + " ms!",
                found
        );
    }

    public static void waitUntilClickable(org.openqa.selenium.By element, String accessibility) {
        long t = System.currentTimeMillis();
        long end = t + (Configuration.timeout * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if ((Configuration.automationType.equals(Configuration.IOS_PLATFORM) || enable(element,
                    accessibility)) && visible(element,
                    accessibility)) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "' is not clickable after " + time + " ms!",
                found
        );
    }

    public static void waitUntilClickable(
            org.openqa.selenium.By element,
            String accessibility,
            int index) {
        long t = System.currentTimeMillis();
        long end = t + (Configuration.timeout * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (enable(element, accessibility, index) && visible(element, accessibility, index)) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "' is not clickable after " + time + " ms!",
                found
        );
    }

    public static void waitUntilEnable(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            boolean isEnabled) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (enable(element, accessibility) == isEnabled) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "' is not enabled after " + time + " ms!",
                found
        );
    }

    public static void waitUntilEnable(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            boolean isEnabled) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (enable(element, accessibility, index) == isEnabled) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accessibility,
                "is not enabled after " + time + " ms!",
                found
        );
    }

    public static void waitUntilExist(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            boolean Exist) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (exists(element, accessibility) == Exist) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "' not exists after " + time + " ms!",
                found
        );
    }

    public static void waitUntilExist(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            boolean exists) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (exists(element, accessibility, index) == exists) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accessibility,
                "not exists after " + time + " ms!",
                found
        );
    }

    public static void waitUntilContainsText(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String text,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (containsText(element, accessibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "has not containText '" +
                        text +
                        "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilContainsText(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String text,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (containsText(element, accessibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(getElementString(element) + "[" + index + "]", accessibility,
                "with containText '" + text + "' is not visible after "
                        + time + " ms!", found);
    }

    public static void waitUntilContainsTextNoCaseSensitive(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String text,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (containsTextNoCaseSensitive(element, accessibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "has not containText '" + text + "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilContainsTextNoCaseSensitive(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String text,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (containsTextNoCaseSensitive(element, accessibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accessibility,
                "with containText '" + text + "' is not visible after " + time + " ms!",
                found
        );
    }

    public static void waitUntilExactText(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String text,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (equalsText(element, accessibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "has not containText '" + text + "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilEmptyText(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (emptyText(element, accessibility) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "should be empty or is not visible after " + time + " ms!",
                found
        );
    }

    public static void waitUntilExactText(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String text,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (equalsText(element, accessibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) + "[" + index + "]",
                accessibility,
                "with containText '" + text + "' is not visible after " + time + " ms!",
                found
        );
    }

    public static void waitUntilEmptyText(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            boolean hasText) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (emptyText(element, accessibility, index) == hasText) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) + "[" + index + "]",
                accessibility,
                "with should be empty text or is not visible after " + time + " ms!",
                found
        );
    }

    public static void waitUntilHasValue(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String text,
            boolean hasValue) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (value(element, accessibility, text) == hasValue) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "has not value '" + text + "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilHasValue(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String text,
            boolean hasValue) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (value(element, accessibility, index, text) == hasValue) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) + "[" + index + "]",
                accessibility,
                "with value '" + text + "is not visible after" + time + " ms!",
                found
        );
    }

    public static void waitUntilHasAttribute(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String Attribute,
            String value,
            boolean hasValue) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, index, Attribute, value) == hasValue) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accessibility,
                "has no attribute '" +
                        Attribute + "' with value '" + value + "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilHasAttribute(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String Attribute,
            boolean hasAttribute) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, index, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accessibility,
                "has no attribute '" + Attribute + "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilEmptyAttribute(
            org.openqa.selenium.By element,
            String accessibility,
            int index,
            int seconds,
            String Attribute,
            boolean hasAttribute) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (emptyAttribute(element, accessibility, index, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element) +
                        "[" + index + "]",
                accessibility,
                "should have attribute '" +
                        Attribute + "' empty or null after " + time + " ms!",
                found
        );
    }

    public static void waitUntilHasAttribute(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String Attribute,
            String value,
            boolean is) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, Attribute, value) == is) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "has no attribute '" +
                        Attribute +
                        "' with value '" +
                        value +
                        "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilHasAttribute(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String Attribute,
            boolean hasAttribute) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (attribute(element, accessibility, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "has no attribute '" +
                        Attribute + "' after " + time + " ms!",
                found
        );
    }

    public static void waitUntilEmptyAttribute(
            org.openqa.selenium.By element,
            String accessibility,
            int seconds,
            String Attribute,
            boolean hasAttribute) {
        long t = System.currentTimeMillis();
        long end = t + (seconds * 1000L);
        boolean found = false;
        while (System.currentTimeMillis() < end) {
            if (emptyAttribute(element, accessibility, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(Configuration.poolingInterval);
        }
        long time = System.currentTimeMillis() - t;
        assertFunction(
                getElementString(element),
                accessibility,
                "should have attribute '" +
                        Attribute +
                        "' empty or null after " + time + " ms!",
                found
        );
    }
}
