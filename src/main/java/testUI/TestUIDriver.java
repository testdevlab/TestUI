package testUI;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.elements.TestUI;
import testUI.elements.UIElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static testUI.ADBUtils.*;
import static testUI.Configuration.addMobileDesiredCapabilities;
import static testUI.Configuration.iOSDeviceName;
import static testUI.UIUtils.*;
import static testUI.iOSCommands.*;

public class TestUIDriver {
    private static List<AppiumDriver> driver = new ArrayList<>();
    private static List<AndroidDriver> AndroidTestUIDriver = new ArrayList<>();
    private static List<IOSDriver> IOSTestUIDriver = new ArrayList<>();
    private static Map<String, AppiumDriver> driverNames = new HashMap<>();

    public synchronized static UIElement setDriver(AndroidDriver driver) {
        TestUIDriver.driver.add(driver);
        TestUIDriver.AndroidTestUIDriver.add(driver);
        return TestUI.E("");
    }

    public synchronized static UIElement setDriver(IOSDriver driver) {
        TestUIDriver.driver.add(driver);
        TestUIDriver.IOSTestUIDriver.add(driver);
        return TestUI.E("");
    }

    public static UIElement setDriver(WebDriver driver) {
        WebDriverRunner.setWebDriver(driver);
        return TestUI.E("");
    }

    public  static void setDriver(AppiumDriver driver, String deviceName) {
        driverNames.put(deviceName, driver);
    }

    public static Map<String, AppiumDriver> getDriverNames() {
        return driverNames;
    }

    public synchronized static void setDriver(AppiumDriver driver, int driverNumber) {
        TestUIDriver.driver.set(driverNumber, driver);
    }

    public synchronized static void setDriver(AndroidDriver driver, int driverNumber) {
        TestUIDriver.AndroidTestUIDriver.set(driverNumber, driver);
        TestUIDriver.driver.set(driverNumber, driver);
    }

    public static AndroidDriver getAndroidTestUIDriver() {
        if (AndroidTestUIDriver.isEmpty() || AndroidTestUIDriver.size() < Configuration.driver) {
            throw new NullPointerException("There is no driver bound to the automation, start driver before running test cases! \n" +
                    "Configuration.driver is set to " + Configuration.driver + " and the number of drivers is only " + AndroidTestUIDriver.size());
        }
        return AndroidTestUIDriver.get(Configuration.driver - 1);
    }

    public static IOSDriver getIOSTestUIDriver() {
        if (IOSTestUIDriver.isEmpty() || IOSTestUIDriver.size() < Configuration.driver) {
            throw new NullPointerException("There is no driver bound to the automation, start driver before running test cases! \n" +
                    "Configuration.driver is set to " + Configuration.driver + " and the number of drivers is only " + IOSTestUIDriver.size());
        }
        return IOSTestUIDriver.get(Configuration.driver - 1);
    }

    public static AppiumDriver getDriver() {
        if (driver.isEmpty() || driver.size() < Configuration.driver) {
            throw new NullPointerException("There is no driver bound to the automation, start driver before running test cases! \n" +
                    "Configuration.driver is set to " + Configuration.driver + " and the number of drivers is only " + driver.size());
        }
        return driver.get(Configuration.driver - 1);
    }

    public static List<AppiumDriver> getDrivers() {
        return driver;
    }

    public static void removeDriver(int driver) {
        TestUIDriver.driver.remove(driver);
    }

    private static DesiredCapabilities desiredCapabilities;

    public static WebDriver getSelenideDriver() {
        return WebDriverRunner.getWebDriver();
    }

    public static byte[] takeScreenshot() {
        if (Configuration.deviceTests) {
            if (getDrivers().size() != 0) {
                Configuration.driver = Configuration.driver > getDrivers().size() ? getDrivers().size() : Configuration.driver;
                return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            } else {
                return new byte[1];
            }
        }
        return ((TakesScreenshot) getSelenideDriver()).getScreenshotAs(OutputType.BYTES);
    }

    public static List<byte[]> takeScreenshotAllDevicesList() {
        List<byte[]> screenshots = new ArrayList<>();
        boolean test = Configuration.deviceTests;
        Configuration.deviceTests = true;
        for (int index = 0; index < getDrivers().size(); index++) {
            screenshots.add(takeScreenshot(index));
        }
        Configuration.deviceTests = false;
        if (WebDriverRunner.driver().hasWebDriverStarted()) {
            try {
                screenshots.add(takeScreenshot());
            } catch (Exception e) {
                System.err.println("Could not take a screenshot in the laptop browser...");
            }
        }
        Configuration.deviceTests = test;
        return screenshots;
    }

    public static Map<String, byte[]> takeScreenshotAllDevicesMap(boolean includeAllure) {
        Map<String, byte[]> screenshots = new HashMap<>();
        boolean test = Configuration.deviceTests;
        Configuration.deviceTests = true;
        for (int index = 0; index < getDrivers().size(); index++) {
            screenshots.put(getDevicesNames().get(index), takeScreenshot(index));
        }
        Configuration.deviceTests = false;
        if (WebDriverRunner.driver().hasWebDriverStarted()) {
            try {
                screenshots.put("browser", takeScreenshot());
            } catch (Exception e) {
                System.err.println("Could not take a screenshot in the laptop browser...");
            }
        }
        if (includeAllure) {
            screenshots.forEach((k, v) -> Allure.getLifecycle().addAttachment(k, "image/png", "png", v));
        }
        Configuration.deviceTests = test;
        return screenshots;
    }

    public static byte[] takeScreenshot(int index) {
        if (Configuration.deviceTests) {
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
        if (Configuration.deviceTests) {
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

    public static DesiredCapabilities setAppAndroidCapabilities() {
        if (Configuration.emulatorName.isEmpty() && !getDeviceStatus(getDevice()).equals("device")) {
            System.err.println("The device status is " + getDeviceStatus(getDevice()) +
                    " to use usb, you must allow usb debugging for this device: " + getDevice());
            throw new Error();
        }
        getDevModel();
        String deviceVersion = Configuration.androidVersion.isEmpty() && Configuration.emulatorName.isEmpty() ? getDeviceVersion(getDevice()) :
                Configuration.androidVersion;
        // Created object of DesiredCapabilities class.
        DesiredCapabilities cap = new DesiredCapabilities();
        if (getDesiredCapabilities() == null) {
            if (Configuration.emulatorName.isEmpty()) {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, getDevice());
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceVersion);
            } else {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, Configuration.emulatorName);
                cap.setCapability(AndroidMobileCapabilityType.AVD, Configuration.emulatorName);
            }
            cap.setCapability(AndroidMobileCapabilityType.APP_WAIT_DURATION, Configuration.launchAppTimeout);
            cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            if (!Configuration.appActivity.isEmpty() && !Configuration.appPackage.isEmpty()) {
                cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Configuration.appActivity);
                cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, Configuration.appPackage);
            } else if (!Configuration.androidAppPath.isEmpty()){
                String appPath = Configuration.androidAppPath.charAt(0) == '/' ? Configuration.androidAppPath :
                        System.getProperty("user.dir") + "/" + Configuration.androidAppPath;
                cap.setCapability("androidInstallPath", appPath);
                cap.setCapability("app", appPath);
            }
            int systemPort = Integer.parseInt(Configuration.usePort.get(Configuration.usePort.size() - 1)) + 10;
            cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
        } else {
            cap = getDesiredCapabilities();
        }
        // ADD CUSTOM CAPABILITIES
        if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
            for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                cap.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
            }
            addMobileDesiredCapabilities = null;
        }
        Configuration.desiredCapabilities = cap;
        return cap;
    }

    public static DesiredCapabilities setAndroidBrowserCapabilities() {
        if (Configuration.emulatorName.isEmpty() && getDevices().size() == 0) {
            throw new Error("There is no device available to run the automation!");
        }
        if (Configuration.emulatorName.isEmpty() && !getDeviceStatus(getDevice()).equals("device")) {
            System.err.println("The device status is " + getDeviceStatus(getDevice()) +
                    " to use usb, you must allow usb debugging for this device: " + getDevice());
            throw new Error();
        }
        getDevModel();
        String deviceVersion = Configuration.androidVersion.isEmpty() && Configuration.emulatorName.isEmpty() ? getDeviceVersion(getDevice()) :
                Configuration.androidVersion;
        String browserFirstLetter = Configuration.browser.subSequence(0, 1).toString().toUpperCase();
        String browser = browserFirstLetter + Configuration.browser.substring(1);
        // Created object of DesiredCapabilities class.
        DesiredCapabilities cap = new DesiredCapabilities();
        if (!Configuration.chromeDriverPath.isEmpty()) {
            String chromePath = Configuration.chromeDriverPath.charAt(0) == '/' ? Configuration.chromeDriverPath :
                    System.getProperty("user.dir") + "/" + Configuration.chromeDriverPath;
            cap.setCapability(AndroidMobileCapabilityType.CHROMEDRIVER_EXECUTABLE, chromePath);
        }
        if (getDesiredCapabilities() == null) {
            if (Configuration.emulatorName.isEmpty()) {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, getDevice());
                cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceVersion);
            } else {
                cap.setCapability(MobileCapabilityType.DEVICE_NAME, Configuration.emulatorName);
                cap.setCapability(AndroidMobileCapabilityType.AVD, Configuration.emulatorName);
            }
            if (Configuration.AutomationName.isEmpty()) {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            } else {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, Configuration.AutomationName);
            }
            int systemPort = Integer.parseInt(Configuration.usePort.get(Configuration.usePort.size() - 1)) + 10;
            int chromeDriverPort = Integer.parseInt(Configuration.usePort.get(Configuration.usePort.size() - 1)) + 15;
            cap.setCapability("chromeDriverPort", chromeDriverPort);
            cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
            cap.setCapability(MobileCapabilityType.NO_RESET, true);
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            cap.setCapability(MobileCapabilityType.BROWSER_NAME, browser);
            cap.setCapability(AndroidMobileCapabilityType.NATIVE_WEB_SCREENSHOT, true);
        } else {
            cap = getDesiredCapabilities();
        }
        // ADD CUSTOM CAPABILITIES
        if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
            for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                cap.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
            }
            addMobileDesiredCapabilities = null;
        }
        Configuration.desiredCapabilities = cap;
        return cap;
    }

    public static DesiredCapabilities setIOSCapabilities(boolean browser) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (getDesiredCapabilities() == null) {
            // CHECK IF DEVICE SPECIFIED
            if (Configuration.iOSDeviceName.isEmpty()) {
                if (Configuration.UDID.isEmpty()) {
                    Map<String, String> sampleIOSDevice = getSampleDevice();
                    Configuration.iOSDeviceName = sampleIOSDevice.get("name");
                    Configuration.iOSVersion = sampleIOSDevice.get("version");
                    Configuration.UDID = sampleIOSDevice.get("udid");
                } else {
                    Configuration.iOSDeviceName = getIOSName(Configuration.UDID);
                    Configuration.iOSVersion = getIOSVersion(Configuration.UDID);
                }
                capabilities.setCapability("udid", Configuration.UDID);
            } else {
                if (Configuration.UDID.isEmpty()) {
                    capabilities.setCapability("udid", "auto");
                } else {
                    capabilities.setCapability("udid", Configuration.UDID);
                }
            }
            if (!getIOSDevices().toString().contains(iOSDeviceName)) {
                setiOSDevice(iOSDeviceName);
            }
            // BROWSER OR APP
            if (browser) {
                capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, MobileBrowserType.SAFARI);
            } else {
                String appPath = Configuration.iOSAppPath.charAt(0) == '/' ? Configuration.iOSAppPath :
                        System.getProperty("user.dir") + "/" + Configuration.iOSAppPath;
                capabilities.setCapability(MobileCapabilityType.APP, appPath);
            }
            // IN CASE OF REAL DEVICE
            if (!Configuration.xcodeOrgId.isEmpty()) {
                capabilities.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID, Configuration.xcodeOrgId);
                capabilities.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID, Configuration.xcodeSigningId);
            }
            if (!Configuration.updatedWDABundleId.isEmpty()) {
                capabilities.setCapability("updatedWDABundleId", Configuration.updatedWDABundleId);
            }
            // DEFAULT THINGS
            capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, Configuration.useNewWDA);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Configuration.iOSDeviceName);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Configuration.iOSVersion);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
            capabilities.setCapability(IOSMobileCapabilityType.START_IWDP, true);
            capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT, Configuration.launchAppTimeout);
            capabilities.setCapability(IOSMobileCapabilityType.COMMAND_TIMEOUTS, 15000);
            // ADD CUSTOM CAPABILITIES
            if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
                for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                    capabilities.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
                }
                addMobileDesiredCapabilities = null;
            }
        } else {
            capabilities = getDesiredCapabilities();
        }
        Configuration.desiredCapabilities = capabilities;
        putAllureParameter("Device Model", Configuration.iOSDeviceName);
        putAllureParameter("Version", Configuration.iOSVersion);
        return capabilities;
    }


    private static void getDevModel() {
        String devModel;
        if (Configuration.emulatorName.isEmpty()) {
            devModel = (getDeviceName().equals(getDevice()) ? getDeviceModel(getDevice()) : getDeviceName());
        } else {
            if (Configuration.driver == 1) {
                Configuration.firstEmulatorName = Configuration.emulatorName;
            }
            devModel = Configuration.emulatorName;
        }
        if (Configuration.driver == 1 && !Configuration.firstEmulatorName.isEmpty()) {
            putAllureParameter("Device Model", Configuration.firstEmulatorName);
        } else {
            putAllureParameter("Device Model", devModel);
        }
    }
}