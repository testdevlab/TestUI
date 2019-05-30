package testUI.Utils;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import testUI.Configuration;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static testUI.TestUIDriver.getDrivers;
import static testUI.TestUIDriver.takeScreenshot;
import static testUI.UIUtils.getDevicesNames;
import static testUI.UIUtils.getIOSDevices;
import static testUI.Utils.AppiumHelps.*;

public class WaitUntil {

    private static void assertFunction(String element, String accesibility, String reason, boolean found) {
        if (!found && Configuration.useAllure) {
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
        if (accesibility.isEmpty()) {
            assertThat("The element '" + element + "' " + reason, found);
        } else {
            assertThat("The element 'By.accessibilityId: " + accesibility + "' " + reason, found);
        }
    }

    private static String getElementString(org.openqa.selenium.By element) {
        if (element != null)
            return element.toString();
        return "";
    }

    public static void waitUntilVisible(org.openqa.selenium.By element, String accesibility, int Seconds, boolean isVisible) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (visible(element, accesibility) == isVisible) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility, "is not visible!", found);
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
        assertFunction(getElementString(element) + "[" + index + "]", accesibility, "is not visible!", found);
    }

    public static void waitUntilClickable(org.openqa.selenium.By element, String accesibility) {
        long t= System.currentTimeMillis();
        long end = t+(Configuration.timeout * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element, accesibility) && visible(element, accesibility)) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"' is not clickable!", found);
    }

    public static void waitUntilClickable(org.openqa.selenium.By element, String accesibility, int index) {
        long t= System.currentTimeMillis();
        long end = t+(Configuration.timeout * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element, accesibility, index) && visible(element, accesibility, index)) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility, "' is not clickable!", found);
    }

    public static void waitUntilEnable(org.openqa.selenium.By element, String accesibility, int Seconds, boolean isEnabled) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element,accesibility) == isEnabled) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"' is not enabled!", found);
    }

    public static void waitUntilEnable(org.openqa.selenium.By element, String accesibility, int index, int Seconds, boolean isEnabled) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (enable(element, accesibility, index) == isEnabled) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility, "is not enabled!", found);
    }

    public static void waitUntilExist(org.openqa.selenium.By element, String accesibility, int Seconds, boolean Exist) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (exists(element, accesibility) == Exist) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"' not exists!", found);
    }

    public static void waitUntilExist(org.openqa.selenium.By element, String accesibility, int index, int Seconds, boolean exists) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (exists(element, accesibility, index) == exists) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,"not exists!", found);
    }

    public static void waitUntilContainsText(org.openqa.selenium.By element, String accesibility, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsText(element, accesibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"has not containText '" + text + "'!", found);
    }

    public static void waitUntilContainsText(org.openqa.selenium.By element, String accesibility, int index, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsText(element, accesibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,
                "with containText '" + text + "' is not visible!", found);
    }

    public static void waitUntilContainsTextNoCaseSensitive(org.openqa.selenium.By element, String accesibility, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsTextNoCaseSensitive(element, accesibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"has not containText '" + text + "'!", found);
    }

    public static void waitUntilContainsTextNoCaseSensitive(org.openqa.selenium.By element, String accesibility, int index, int Seconds, String text,
                                                     boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (containsTextNoCaseSensitive(element, accesibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,
                "with containText '" + text + "' is not visible!", found);
    }

    public static void waitUntilExactText(org.openqa.selenium.By element, String accesibility, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (equalsText(element, accesibility, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"has not containText '" + text + "'!", found);
    }

    public static void waitUntilExactText(org.openqa.selenium.By element, String accesibility, int index, int Seconds, String text, boolean hasText) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (equalsText(element, accesibility, index, text) == hasText) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,
                "with containText '" + text + "' is not visible!", found);
    }

    public static void waitUntilHasValue(org.openqa.selenium.By element, String accesibility, int Seconds, String text, boolean hasVallue) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (value(element, accesibility, text) == hasVallue) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"has not value '" + text + "'!", found);
    }

    public static void waitUntilHasValue(org.openqa.selenium.By element, String accesibility, int index, int Seconds, String text, boolean hasValue) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (value(element, accesibility, index, text) == hasValue) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,"with value '"
                + text + "is not visible!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accesibility, int index, int Seconds, String Attribute, String value, boolean hasValue) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accesibility, index, Attribute, value) == hasValue) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,"has no '" + Attribute
                + "' with value '" + value + "'!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accesibility, int index, int Seconds, String Attribute, boolean hasAttribute) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accesibility, index, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element) + "[" + index + "]", accesibility,"has no '" + Attribute
                + "'!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accesibility, int Seconds, String Attribute, String value, boolean is) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accesibility, Attribute, value) == is) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"has no '" + Attribute
                + "' with value '" + value + "'!", found);
    }

    public static void waitUntilHasAttribute(org.openqa.selenium.By element, String accesibility, int Seconds, String Attribute, boolean hasAttribute) {
        long t= System.currentTimeMillis();
        long end = t+(Seconds * 1000);
        boolean found = false;
        while(System.currentTimeMillis() < end) {
            if (attribute(element, accesibility, Attribute) == hasAttribute) {
                found = true;
                break;
            }
            sleep(200);
        }
        assertFunction(getElementString(element), accesibility,"has no '" + Attribute + "'!", found);
    }
}
