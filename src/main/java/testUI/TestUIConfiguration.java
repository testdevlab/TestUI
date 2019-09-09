package testUI;

import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.List;

public class TestUIConfiguration {
    private final String browser = Configuration.browser;
    private final String baseUrl = Configuration.baseUrl;
    private final int timeout = Configuration.timeout;
    private final String appPackage = Configuration.appPackage;
    private final String appActivity = Configuration.appActivity;
    private final boolean deviceTests = Configuration.deviceTests;
    private final boolean useEmulators = Configuration.useEmulators;
    private final int driver = Configuration.driver;
    private final int baseAppiumPort = Configuration.baseAppiumPort;
    private final int baseAppiumBootstrapPort = Configuration.baseAppiumBootstrapPort;
    private final boolean useAllure = Configuration.useAllure;
    private final String iOSVersion = Configuration.iOSVersion;
    private final int launchAppTimeout = Configuration.launchAppTimeout;
    private final String iOSAppPath = Configuration.iOSAppPath;
    private final String iOSDeviceName = Configuration.iOSDeviceName;
    private final String androidDeviceName = Configuration.androidDeviceName;
    private final boolean iOSTesting = Configuration.iOSTesting;
    private final String androidAppPath = Configuration.androidAppPath;
    private final String androidVersion = Configuration.androidVersion;
    private final static ThreadLocal<String> chromeDriverPath = new ThreadLocal<>();
    private final boolean installMobileChromeDriver = Configuration.installMobileChromeDriver;
    private final String appiumUrl = Configuration.appiumUrl;
    private final String emulatorName = Configuration.androidDeviceName;
    private final String xcodeOrgId = Configuration.xcodeOrgId;
    private final String xcodeSigningId = Configuration.xcodeSigningId;
    private final String UDID = Configuration.UDID;
    private final String bundleId = Configuration.bundleId;
    private final String updatedWDABundleId = Configuration.updatedWDABundleId;
    private final DesiredCapabilities addMobileDesiredCapabilities = Configuration.addMobileDesiredCapabilities;
    private final String AutomationName = Configuration.AutomationName;
    private final boolean useNewWDA = Configuration.useNewWDA;
    private final String serverLogLevel = Configuration.serverLogLevel;
    private final String screenshotPath = Configuration.screenshotPath;
    private final int timeStartAppiumServer = Configuration.timeStartAppiumServer;

    public TestUIConfiguration() {
        if (!Configuration.chromeDriverPath.isEmpty()) {
            setChromeDriverPath(Configuration.chromeDriverPath);
        }
    }

    public void setChromeDriverPath(String path) {
        chromeDriverPath.set(path);
    }

    public String getChromeDriverPath() {
        if (chromeDriverPath.get() == null)
            return "";
        return chromeDriverPath.get();
    }

    public int getBaseAppiumBootstrapPort() {
        return baseAppiumBootstrapPort;
    }

    public boolean isDeviceTests() {
        return deviceTests;
    }

    public boolean isInstallMobileChromeDriver() {
        return installMobileChromeDriver;
    }

    public boolean isiOSTesting() {
        return iOSTesting;
    }

    public boolean isUseAllure() {
        return useAllure;
    }

    public boolean isUseEmulators() {
        return useEmulators;
    }

    public boolean isUseNewWDA() {
        return useNewWDA;
    }

    public int getBaseAppiumPort() {
        return baseAppiumPort;
    }

    public int getDriver() {
        return driver;
    }

    public int getLaunchAppTimeout() {
        return launchAppTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public String getAndroidAppPath() {
        return androidAppPath;
    }

    public String getAndroidDeviceName() {
        return androidDeviceName;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getAppiumUrl() {
        return appiumUrl;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public DesiredCapabilities getAddMobileDesiredCapabilities() {
        return addMobileDesiredCapabilities;
    }

    public String getBrowser() {
        return browser;
    }

    public String getBundleId() {
        return bundleId;
    }

    public String getiOSAppPath() {
        return iOSAppPath;
    }

    public String getEmulatorName() {
        return emulatorName;
    }

    public String getAutomationName() {
        return AutomationName;
    }

    public String getiOSDeviceName() {
        return iOSDeviceName;
    }

    public int getTimeStartAppiumServer() {
        return timeStartAppiumServer;
    }

    public String getiOSVersion() {
        return iOSVersion;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public String getUDID() {
        return UDID;
    }

    public String getServerLogLevel() {
        return serverLogLevel;
    }

    public String getUpdatedWDABundleId() {
        return updatedWDABundleId;
    }

    public String getXcodeOrgId() {
        return xcodeOrgId;
    }

    public String getXcodeSigningId() {
        return xcodeSigningId;
    }
}
