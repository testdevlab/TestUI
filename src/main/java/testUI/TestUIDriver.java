package testUI;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.elements.TestUI;
import testUI.elements.UIElement;

import java.util.*;

import static testUI.UIUtils.*;
import static testUI.Utils.Logger.putLogInfo;

public class TestUIDriver {
    private static ThreadLocal<List<AppiumDriver>> driver = new ThreadLocal<>();
    private static ThreadLocal<List<AndroidDriver>> AndroidTestUIDriver = new ThreadLocal<>();
    private static ThreadLocal<List<IOSDriver>> IOSTestUIDriver = new ThreadLocal<>();
    private static TestUIDriver testUIDriver = new TestUIDriver();
    private static DesiredCapabilities desiredCapabilities;

    public synchronized static UIElement setDriver(AndroidDriver driver) {
        List<AppiumDriver> appiumDrivers = new ArrayList<>(getDrivers());
        appiumDrivers.add(driver);
        TestUIDriver.driver.set(appiumDrivers);
        List<AndroidDriver> androidDrivers = new ArrayList<>(getAndroidDrivers());
        androidDrivers.add(driver);
        TestUIDriver.AndroidTestUIDriver.set(androidDrivers);
        return TestUI.E("");
    }

    public synchronized static UIElement setDriver(IOSDriver driver) {
        List<AppiumDriver> appiumDrivers = new ArrayList<>(getDrivers());
        appiumDrivers.add(driver);
        TestUIDriver.driver.set(appiumDrivers);
        List<IOSDriver> iOSDrivers = new ArrayList<>(getIOSDrivers());
        iOSDrivers.add(driver);
        TestUIDriver.IOSTestUIDriver.set(iOSDrivers);
        return TestUI.E("");
    }

    public static UIElement setDriver(WebDriver driver) {
        WebDriverRunner.setWebDriver(driver);
        return TestUI.E("");
    }

    public synchronized static void setDriver(IOSDriver driver, int driverNumber) {
        List<IOSDriver> iOSDrivers = new ArrayList<>(getIOSDrivers());
        iOSDrivers.set(driverNumber, driver);
        TestUIDriver.IOSTestUIDriver.set(iOSDrivers);
        List<AppiumDriver> appiumDrivers = new ArrayList<>(getDrivers());
        appiumDrivers.set(driverNumber, driver);
        TestUIDriver.driver.set(appiumDrivers);
    }

    public synchronized static void setDriver(AndroidDriver driver, int driverNumber) {
        List<AndroidDriver> androidDrivers = new ArrayList<>(getAndroidDrivers());
        androidDrivers.set(driverNumber, driver);
        TestUIDriver.AndroidTestUIDriver.set(androidDrivers);
        List<AppiumDriver> appiumDrivers = new ArrayList<>(getDrivers());
        appiumDrivers.set(driverNumber, driver);
        TestUIDriver.driver.set(appiumDrivers);
    }

    public static AndroidDriver getAndroidTestUIDriver() {
        if (getAndroidDrivers().isEmpty() || getAndroidDrivers().size() < Configuration.driver) {
            throw new NullPointerException(
                    "There is no driver bound to the automation, "
                            + "start driver before running test cases! \n"
                            + "Configuration.driver is set to "
                            + Configuration.driver
                            + " and the number of drivers is only "
                            + getAndroidDrivers().size());
        }
        return getAndroidDrivers().get(Configuration.driver - 1);
    }

    public static IOSDriver getIOSTestUIDriver() {
        if (getIOSDrivers().isEmpty() || getIOSDrivers().size() < Configuration.driver) {
            throw new NullPointerException(
                    "There is no driver bound to the automation, "
                            + "start driver before running test cases! \n"
                            + "Configuration.driver is set to "
                            + Configuration.driver
                            + " and the number of drivers is only "
                            + getIOSDrivers().size());
        }
        return getIOSDrivers().get(Configuration.driver - 1);
    }

    public static AppiumDriver getDriver() {
        if (getDrivers().isEmpty() || getDrivers().size() < Configuration.driver) {
            throw new NullPointerException(
                    "There is no driver bound to the automation, "
                            + "start driver before running test cases! \n"
                            + "Configuration.driver is set to "
                            + Configuration.driver
                            + " and the number of drivers is only "
                            + getDrivers().size());
        }
        return getDrivers().get(Configuration.driver - 1);
    }

    public static List<AppiumDriver> getDrivers() {
        if (driver.get() == null)
            return new ArrayList<>();
        return driver.get();
    }

    public static List<AndroidDriver> getAndroidDrivers() {
        if (AndroidTestUIDriver.get() == null)
            return new ArrayList<>();
        return AndroidTestUIDriver.get();
    }

    public static List<IOSDriver> getIOSDrivers() {
        if (IOSTestUIDriver.get() == null)
            return new ArrayList<>();
        return IOSTestUIDriver.get();
    }

    public static void removeDriver(int driver) {
        List<AppiumDriver> appiumDrivers = new ArrayList<>(getDrivers());
        appiumDrivers.remove(driver);
        TestUIDriver.driver.set(appiumDrivers);
    }

    public static void removeiOSDriver(int driver) {
        List<IOSDriver> appiumiOSDrivers = new ArrayList<>(getIOSDrivers());
        appiumiOSDrivers.remove(driver);
        TestUIDriver.IOSTestUIDriver.set(appiumiOSDrivers);
    }

    public static void removeAndroidDriver(int driver) {
        List<AndroidDriver> appiumAndroidDrivers = new ArrayList<>(getAndroidDrivers());
        appiumAndroidDrivers.remove(driver);
        TestUIDriver.AndroidTestUIDriver.set(appiumAndroidDrivers);
    }

    public static WebDriver getSelenideDriver() {
        return WebDriverRunner.getWebDriver();
    }

    public static byte[] takeScreenshot() {
        if (!automationType.equals(DESKTOP_PLATFORM)) {
            if (getDrivers().size() != 0) {
                Configuration.driver = Math.min(Configuration.driver, getDrivers().size());
                return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            } else {
                return new byte[1];
            }
        }
        return ((TakesScreenshot) getSelenideDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static List<byte[]> takeScreenshotAllDevicesList() {
        List<byte[]> screenshots = new ArrayList<>();
        String aType = Configuration.automationType;
        automationType = ANDROID_PLATFORM;
        for (int index = 0; index < getDrivers().size(); index++) {
            screenshots.add(takeScreenshot(index));
        }
        automationType = DESKTOP_PLATFORM;
        if (WebDriverRunner.driver().hasWebDriverStarted()) {
            try {
                screenshots.add(takeScreenshot());
            } catch (Exception e) {
                System.err.println("Could not take a screenshot in the laptop browser...");
            }
        }
        automationType = aType;
        return screenshots;
    }

    public static Map<String, byte[]> takeScreenshotAllDevicesMap(boolean includeAllure) {
        Map<String, byte[]> screenshots = new HashMap<>();
        String aType = Configuration.automationType;
        automationType = ANDROID_PLATFORM;
        for (int index = 0; index < getDrivers().size(); index++) {
            screenshots.put(getDevicesNames().get(index), takeScreenshot(index));
        }
        automationType = DESKTOP_PLATFORM;
        if (WebDriverRunner.driver().hasWebDriverStarted()) {
            try {
                screenshots.put("browser", takeScreenshot());
            } catch (Exception e) {
                System.err.println("Could not take a screenshot in the laptop browser...");
            }
        }
        if (includeAllure) {
            screenshots.forEach((k, v) -> Allure.getLifecycle().addAttachment(
                    k, "image/png", "png", v));
        }
        automationType = aType;
        return screenshots;
    }

    public static byte[] takeScreenshot(int index) {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            return ((TakesScreenshot) getDrivers().get(index)).getScreenshotAs(OutputType.BYTES);
        }
        try {
            return ((TakesScreenshot) getSelenideDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Could not take a screenshot in the laptop browser...");
        }
        return new byte[1];
    }

    public static byte[] takeScreenshot(AppiumDriver driver) {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        try {
            return ((TakesScreenshot) getSelenideDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Could not take a screenshot in the laptop browser...");
        }
        return new byte[1];
    }

    public static void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        TestUIDriver.desiredCapabilities = desiredCapabilities;
    }

    public static DesiredCapabilities getDesiredCapabilities() {
        return TestUIDriver.desiredCapabilities;
    }

    public static UIElement getTestUIDriver() {
        return TestUI.E("");
    }

    public static void switchApplicationContext(String context) {
        AppiumDriver driver = getDriver();

        if (driver instanceof AndroidDriver) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            Set<String> contextNames = androidDriver.getContextHandles();

            boolean contextFound = false;
            for (String contextName : contextNames) {
                if (contextName.contains(context)) {
                    androidDriver.context(contextName);
                    putLogInfo("Switched to context: " + contextName);
                    contextFound = true;
                    break;
                }
            }
            if (!contextFound) {
                putLogInfo("Provided context is not available");
            }
        } else if (driver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) driver;
            Set<String> contextNames = iosDriver.getContextHandles();
            boolean contextFound = false;
            for (String contextName : contextNames) {
                if (contextName.contains(context)) {
                    iosDriver.context(contextName);
                    putLogInfo("Switched to context: " + contextName);
                    contextFound = true;
                    break;
                }
            }
            if (!contextFound) {
                putLogInfo("Provided context is not available");
            }
        } else {
            putLogInfo("Unsupported driver type.");
        }
    }
}
