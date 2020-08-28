package testUI.AndroidUtils;

import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIConfiguration;

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
            cap.setCapability(AndroidMobileCapabilityType.APP_WAIT_DURATION,
                    Configuration.launchAppTimeout);
            if (Configuration.automationName.isEmpty()) {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            } else {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                        Configuration.automationName);
            }
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            if (!Configuration.appActivity.isEmpty() && !Configuration.appPackage.isEmpty()) {
                cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                        Configuration.appActivity);
                cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                        Configuration.appPackage);
            }
            if (!Configuration.androidAppPath.isEmpty()) {
                String appPath = Configuration.androidAppPath.charAt(0) == '/'
                        ? Configuration.androidAppPath
                        : System.getProperty("user.dir") + "/" + Configuration.androidAppPath;
                cap.setCapability("androidInstallPath", appPath);
                cap.setCapability("app", appPath);
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
        putLogDebug("Caps -> " + cap);
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
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
            } else {
                cap.setCapability(MobileCapabilityType.AUTOMATION_NAME,
                        Configuration.automationName);
            }
            cap.merge(setPortCapabilities(configuration));
            cap.setCapability(MobileCapabilityType.NO_RESET, true);
            cap.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
            cap.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");
            cap.setCapability(AndroidMobileCapabilityType.NATIVE_WEB_SCREENSHOT, true);
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
        putLogDebug("Caps -> " + cap);
        return cap;
    }


    private static void retrieveDevice(TestUIConfiguration configuration) {
        if (Configuration.appiumUrl.isEmpty()) {
            if (configuration.getEmulatorName().isEmpty() && getDevices().size() == 0) {
                throw new Error("There is no device available to run the automation!");
            }
            if (configuration.getEmulatorName().isEmpty()
                    && !adbUtils.getDeviceStatus(getDevice()).equals("device")) {
                System.err.println("The device status is " + adbUtils.getDeviceStatus(getDevice()) +
                        " to use usb, you must allow usb debugging for this device: " + getDevice());
                throw new Error();
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
                    throw new Error("There is no device available to run the automation!");
                }
            }
        }
    }

    private static DesiredCapabilities setDeviceCapability(TestUIConfiguration configuration) {
        DesiredCapabilities cap = new DesiredCapabilities();
        if (configuration.getEmulatorName().isEmpty()) {
            String udid = configuration.getUDID().isEmpty() ? getDevice()
                    : configuration.getUDID();
            cap.setCapability(MobileCapabilityType.UDID, udid);
            cap.setCapability(MobileCapabilityType.DEVICE_NAME, udid);
        } else {
            cap.setCapability(AndroidMobileCapabilityType.AVD, configuration.getEmulatorName());
            cap.setCapability(MobileCapabilityType.DEVICE_NAME, configuration.getEmulatorName());
        }

        return cap;
    }

    private static DesiredCapabilities setPortCapabilities(TestUIConfiguration configuration) {
        DesiredCapabilities cap = new DesiredCapabilities();
        if (configuration.getAppiumUrl().isEmpty()) {
            int systemPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 10;
            int chromeDriverPort = Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) + 15;
            cap.setCapability("chromeDriverPort", chromeDriverPort);
            cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
        }
        if (Configuration.chromeDriverPort != 0) {
            cap.setCapability("chromeDriverPort", chromeDriverPort);
        }
        if (Configuration.systemPort != 0) {
            cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, systemPort);
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
            cap.setCapability(AndroidMobileCapabilityType.CHROMEDRIVER_EXECUTABLE, chromePath);
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
