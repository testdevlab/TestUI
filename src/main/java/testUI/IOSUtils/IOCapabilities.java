package testUI.IOSUtils;

import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
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
                capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME,
                        MobileBrowserType.SAFARI);
            } else if (!Configuration.iOSAppPath.isEmpty()) {
                String appPath = Configuration.iOSAppPath.charAt(0) == '/' ?
                        Configuration.iOSAppPath :
                        System.getProperty("user.dir") + "/" + Configuration.iOSAppPath;
                capabilities.setCapability(MobileCapabilityType.APP, appPath);
            }
            // IN CASE OF REAL DEVICE
            if (!Configuration.xcodeOrgId.isEmpty()) {
                capabilities.setCapability(IOSMobileCapabilityType.XCODE_ORG_ID,
                        Configuration.xcodeOrgId);
                capabilities.setCapability(IOSMobileCapabilityType.XCODE_SIGNING_ID,
                        Configuration.xcodeSigningId);
            }
            if (!Configuration.updatedWDABundleId.isEmpty()) {
                capabilities.setCapability("updatedWDABundleId",
                        Configuration.updatedWDABundleId);
            }
            if (!Configuration.bundleId.isEmpty()) {
                capabilities.setCapability("bundleId", Configuration.bundleId);
            }
            // DEFAULT THINGS
            int wdaLocalPort;
            if (Configuration.appiumUrl.isEmpty()) {
                wdaLocalPort =
                        8100 + 20 * (Integer.parseInt(getUsePort().get(getUsePort().size() - 1)) -
                                Configuration.baseAppiumPort) / 100;
            } else {
                wdaLocalPort = Configuration.wdaPort;
            }
            capabilities.setCapability(IOSMobileCapabilityType.WDA_LOCAL_PORT, wdaLocalPort);
            capabilities.setCapability(MobileCapabilityType.NO_RESET, false);
            capabilities.setCapability(IOSMobileCapabilityType.USE_NEW_WDA,
                    Configuration.useNewWDA);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
            capabilities.setCapability(IOSMobileCapabilityType.START_IWDP, true);
            capabilities.setCapability(IOSMobileCapabilityType.LAUNCH_TIMEOUT,
                    Configuration.launchAppTimeout);
            capabilities.setCapability(IOSMobileCapabilityType.COMMAND_TIMEOUTS, 30000);
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
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
                        Configuration.iOSDeviceName);
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
                        Configuration.iOSVersion);
                capabilities.setCapability("udid", Configuration.UDID);
            } else {
                if (Configuration.UDID.isEmpty()) {
                    capabilities.setCapability("udid", "auto");
                } else {
                    capabilities.setCapability("udid", Configuration.UDID);
                }
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
                        Configuration.iOSDeviceName);
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
                        Configuration.iOSVersion);
            }
        } else {
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,
                    Configuration.iOSDeviceName);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
                    Configuration.iOSVersion);
            capabilities.setCapability("udid", Configuration.UDID);
        }

        return capabilities;
    }
}
