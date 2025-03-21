package testUI.AndroidUtils;

import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIConfiguration;
import testUI.Utils.TestUIException;

import static testUI.TestUIDriver.getDesiredCapabilities;
import static testUI.UIUtils.*;
import static testUI.UIUtils.putAllureParameter;
import static testUI.Utils.Logger.putLogDebug;

public class AndroidCapabilities extends Configuration {
    private static ADBUtils adbUtils = new ADBUtils();

    public static DesiredCapabilities setAppAndroidCapabilities(TestUIConfiguration configuration) {
        retrieveDevice(configuration);
        // Created object of DesiredCapabilities class.
        DesiredCapabilities cap = setChromeDriverCap(configuration);
        if (getDesiredCapabilities() == null) {
            cap.merge(setDeviceCapability(configuration));
            cap.setCapability("appium:appWaitDuration",
                    Configuration.launchAppTimeout);
            if (Configuration.automationName.isEmpty()) {
                cap.setCapability("appium:automationName", "UiAutomator2");
            } else {
                cap.setCapability("appium:automationName",
                        Configuration.automationName);
            }
            cap.setCapability("appium:platformName", Platform.ANDROID);
            if (!Configuration.appActivity.isEmpty() && !Configuration.appPackage.isEmpty()) {
                cap.setCapability("appium:appActivity",
                        Configuration.appActivity);
                cap.setCapability("appium:appPackage",
                        Configuration.appPackage);
            }
            if (!Configuration.androidAppPath.isEmpty()) {
                String appPath = Configuration.androidAppPath.charAt(0) == '/'
                        ? Configuration.androidAppPath
                        : System.getProperty("user.dir") + "/" + Configuration.androidAppPath;
                cap.setCapability("appium:androidInstallPath", appPath);
                cap.setCapability("appium:app", appPath);
            }
            cap.merge(setPortCapabilities(configuration));
        } else {
            cap = getDesiredCapabilities();
        }
        // ADD CUSTOM CAPABILITIES
        if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
            for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                cap.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
            }
            addMobileDesiredCapabilities = new DesiredCapabilities();
        }
        Configuration.desiredCapabilities = cap;
        putLogDebug("Caps -> %s", cap.toString());
        return cap;
    }

    public static DesiredCapabilities setAndroidBrowserCapabilities(
            TestUIConfiguration configuration) {
        retrieveDevice(configuration);
        // Created object of DesiredCapabilities class.
        DesiredCapabilities cap = setChromeDriverCap(configuration);
        if (getDesiredCapabilities() == null) {
            cap.merge(setDeviceCapability(configuration));
            if (Configuration.automationName.isEmpty()) {
                cap.setCapability("appium:automationName", "UiAutomator2");
            } else {
                cap.setCapability("appium:automationName",
                        Configuration.automationName);
            }
            cap.merge(setPortCapabilities(configuration));
            cap.setCapability("appium:noReset", true);
            cap.setCapability("appium:platformName", Platform.ANDROID);
            cap.setCapability("appium:browserName", "chrome");
            cap.setCapability("appium:nativeWebScreenshot", true);
            if (!Configuration.useW3C) {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("w3c", false);
                cap.merge(chromeOptions);
            }
        } else {
            cap = getDesiredCapabilities();
        }
        // ADD CUSTOM CAPABILITIES
        if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
            for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                cap.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
            }
            addMobileDesiredCapabilities = new DesiredCapabilities();
        }
        Configuration.desiredCapabilities = cap;
        putLogDebug("Caps -> %s", cap.toString());
        return cap;
    }


    private static void retrieveDevice(TestUIConfiguration configuration) {
        if (Configuration.appiumUrl.isEmpty()) {
            if (configuration.getEmulatorName().isEmpty() && getDevices().size() == 0) {
                throw new TestUIException("There is no device available to run the automation!");
            }
            if (configuration.getEmulatorName().isEmpty()
                    && !adbUtils.getDeviceStatus(getDevice()).equals("device")) {
                System.err.println("The device status is " + adbUtils.getDeviceStatus(getDevice()) +
                        " to use usb, you must allow usb debugging for this device: " + getDevice());
                throw new TestUIException("The device status is " + adbUtils.getDeviceStatus(getDevice()) +
                        " to use usb, you must allow usb debugging for this device: " + getDevice());
            }
            getDevModel(configuration);
        } else {
            if (configuration.getEmulatorName().isEmpty() && getDevices().size() == 0) {
                if (!Configuration.emulatorName.isEmpty()) {
                    configuration.setEmulatorName(Configuration.emulatorName);
                } else if (!Configuration.UDID.isEmpty() && !Configuration.androidDeviceName.isEmpty()) {
                    setDevice(Configuration.UDID, Configuration.androidDeviceName);
                } else if (!Configuration.UDID.isEmpty()) {
                    setDevice(Configuration.UDID, Configuration.UDID);
                } else {
                    throw new TestUIException("There is no device available to run the automation!");
                }
            }
        }
    }

    private static DesiredCapabilities setDeviceCapability(TestUIConfiguration configuration) {
        DesiredCapabilities cap = new DesiredCapabilities();
        if (configuration.getEmulatorName().isEmpty()) {
            String udid = configuration.getUDID().isEmpty() ? getDevice()
                    : configuration.getUDID();
            cap.setCapability("appium:udid", udid);
            cap.setCapability("appium:deviceName", udid);
        } else {
            cap.setCapability("appium:avd", configuration.getEmulatorName());
            cap.setCapability("appium:deviceName", configuration.getEmulatorName());
        }

        return cap;
    }

    private static DesiredCapabilities setPortCapabilities(TestUIConfiguration configuration) {
        DesiredCapabilities cap = new DesiredCapabilities();
        if (configuration.getAppiumUrl().isEmpty()) {
            int systemPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 10;
            int chromeDriverPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 15;
            cap.setCapability("appium:chromedriverPort", chromeDriverPort);
            cap.setCapability("appium:systemPort", systemPort);
        }
        if (Configuration.chromeDriverPort != 0) {
            cap.setCapability("appium:chromedriverPort", chromeDriverPort);
        }
        if (Configuration.systemPort != 0) {
            cap.setCapability("appium:systemPort", systemPort);
        }

        return cap;
    }

    private static DesiredCapabilities setChromeDriverCap(
            TestUIConfiguration configuration) {
        DesiredCapabilities cap = new DesiredCapabilities();
        if (!configuration.getChromeDriverPath().isEmpty()) {
            String slash = System.getProperty("os.name").toLowerCase().contains("w") ? "\\" : "/";
            String chromePath = configuration.getChromeDriverPath().charAt(0) == '/' ||
                    configuration.getChromeDriverPath().startsWith("C:\\")
                    ? configuration.getChromeDriverPath()
                    : System.getProperty("user.dir") + slash + configuration.getChromeDriverPath();
            cap.setCapability("appium:chromedriverExecutable", chromePath);
            return cap;
        }

        return cap;
    }
    private static void getDevModel(TestUIConfiguration configuration) {
        String devModel;
        if (configuration.getEmulatorName().isEmpty()) {
            devModel = (getDeviceName().equals(getDevice()) ?
                    adbUtils.getDeviceModel(getDevice()) : getDeviceName());
        } else {
            if (Configuration.driver == 1) {
                Configuration.firstEmulatorName.set(configuration.getEmulatorName());
            }
            devModel = configuration.getEmulatorName();
        }
        if (Configuration.driver == 1 && Configuration.firstEmulatorName.get() != null) {
            putAllureParameter("Device Model", Configuration.firstEmulatorName.get());
        } else {
            putAllureParameter("Device Model", devModel);
        }
    }
}
