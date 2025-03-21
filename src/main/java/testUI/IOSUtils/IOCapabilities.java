package testUI.IOSUtils;

import io.appium.java_client.remote.MobileBrowserType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;

import java.util.Map;

import static testUI.TestUIDriver.getDesiredCapabilities;
import static testUI.UIUtils.*;
import static testUI.UIUtils.putAllureParameter;

public class IOCapabilities extends Configuration {
    private static IOSCommands iosCommands = new IOSCommands();

    public static DesiredCapabilities setIOSCapabilities(boolean browser) {
        DesiredCapabilities capabilities;

        if (getDesiredCapabilities() == null) {
            // CHECK IF DEVICE SPECIFIED
            capabilities = getIOSDevice();
            if (!getIOSDevices().toString().contains(iOSDeviceName)) {
                setiOSDevice(iOSDeviceName);
            }
            // BROWSER OR APP
            if (browser) {
                capabilities.setCapability("appium:autoWebview", true);
                capabilities.setCapability("appium:browserName",
                        MobileBrowserType.SAFARI);
            } else if (!Configuration.iOSAppPath.isEmpty()) {
                String appPath = Configuration.iOSAppPath.charAt(0) == '/' ?
                        Configuration.iOSAppPath :
                        System.getProperty("user.dir") + "/" + Configuration.iOSAppPath;
                capabilities.setCapability("appium:app", appPath);
            }
            // IN CASE OF REAL DEVICE
            if (!Configuration.xcodeOrgId.isEmpty()) {
                capabilities.setCapability("xcodeOrgId",
                        Configuration.xcodeOrgId);
                capabilities.setCapability("xcodeSigningId",
                        Configuration.xcodeSigningId);
            }
            if (!Configuration.updatedWDABundleId.isEmpty()) {
                capabilities.setCapability("appium:updatedWDABundleId",
                        Configuration.updatedWDABundleId);
            }
            if (!Configuration.bundleId.isEmpty()) {
                capabilities.setCapability("appium:bundleId", Configuration.bundleId);
            }
            // DEFAULT THINGS
            int wdaLocalPort;
            if (Configuration.appiumUrl.isEmpty()) {
                wdaLocalPort =
                        8100 + 20 * (Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) -
                                Configuration.baseAppiumPort) / 100;
                capabilities.setCapability("appium:wdaLocalPort", wdaLocalPort);
            }
            capabilities.setCapability("appium:noReset", false);
            capabilities.setCapability("appium:useNewWDA",
                    Configuration.useNewWDA);
            capabilities.setCapability("appium:platformName", Platform.IOS);
            capabilities.setCapability("appium:automationName", "XCUITest");
            capabilities.setCapability("appium:wdaLaunchTimeout",
                    Configuration.launchAppTimeout);
            capabilities.setCapability("appium:commandTimeouts", 30000);
            // ADD CUSTOM CAPABILITIES
            if (!Configuration.addMobileDesiredCapabilities.asMap().isEmpty()) {
                for (String key : addMobileDesiredCapabilities.asMap().keySet()) {
                    capabilities.setCapability(key, addMobileDesiredCapabilities.asMap().get(key));
                }
                addMobileDesiredCapabilities = new DesiredCapabilities();
            }
        } else {
            capabilities = getDesiredCapabilities();
        }
        Configuration.desiredCapabilities = capabilities;
        putAllureParameter("Device Model", Configuration.iOSDeviceName);
        putAllureParameter("Version", Configuration.iOSVersion);
        return capabilities;
    }


    private static DesiredCapabilities getIOSDevice() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (Configuration.appiumUrl.isEmpty()) {
            if (Configuration.iOSDeviceName.isEmpty()) {
                if (Configuration.UDID.isEmpty()) {
                    Map<String, String> sampleIOSDevice = iosCommands.getSampleDevice(0);
                    Configuration.iOSDeviceName = sampleIOSDevice.get("name");
                    Configuration.iOSVersion = sampleIOSDevice.get("version");
                    Configuration.UDID = sampleIOSDevice.get("udid");
                } else {
                    if (Configuration.iOSVersion.isEmpty()) {
                        Configuration.iOSVersion = iosCommands.getIOSVersion(Configuration.UDID);
                    }
                    if (Configuration.iOSDeviceName.isEmpty()) {
                        Configuration.iOSDeviceName = iosCommands.getIOSName(Configuration.UDID);
                    }
                }
                Configuration.iOSDeviceName = Configuration.iOSDeviceName.isEmpty() ? "iPhone" :
                        Configuration.iOSDeviceName;
                Configuration.iOSVersion = Configuration.iOSVersion.isEmpty() ? "13.2" :
                        Configuration.iOSVersion;
                capabilities.setCapability("appium:deviceName",
                        Configuration.iOSDeviceName);
                capabilities.setCapability("appium:platformVersion",
                        Configuration.iOSVersion);
                capabilities.setCapability("appium:udid", Configuration.UDID);
            } else {
                if (Configuration.UDID.isEmpty()) {
                    capabilities.setCapability("appium:udid", "auto");
                } else {
                    capabilities.setCapability("appium:udid", Configuration.UDID);
                }
                capabilities.setCapability("appium:deviceName",
                        Configuration.iOSDeviceName);
                capabilities.setCapability("appium:platformVersion",
                        Configuration.iOSVersion);
            }
        } else {
            capabilities.setCapability("appium:deviceName" ,
                    Configuration.iOSDeviceName);
            capabilities.setCapability("appium:platformVersion",
                    Configuration.iOSVersion);
            capabilities.setCapability("appium:udid", Configuration.UDID);
        }

        return capabilities;
    }
}
