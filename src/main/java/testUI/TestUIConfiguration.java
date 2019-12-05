package testUI;

public class TestUIConfiguration {

    // TODO: reduce static fields from conf
    private final String browser = Configuration.browser;
    private final String baseUrl = Configuration.baseUrl;
    private final int timeout = Configuration.timeout;
//    private final String appPackage = Configuration.appPackage;
//    private final String appActivity = Configuration.appActivity;
    private final boolean deviceTests = Configuration.deviceTests;
    private final boolean useEmulators = Configuration.useEmulators;
    private final int driver = Configuration.driver;
    private final int baseAppiumPort = Configuration.baseAppiumPort;
    private final int baseAppiumBootstrapPort = Configuration.baseAppiumBootstrapPort;
//    private final boolean useAllure = Configuration.useAllure;
//    private final String iOSVersion = Configuration.iOSVersion;
//    private final int launchAppTimeout = Configuration.launchAppTimeout;
//    private final String iOSAppPath = Configuration.iOSAppPath;
//    private final String iOSDeviceName = Configuration.iOSDeviceName;
    private final String androidDeviceName = Configuration.androidDeviceName;
    private final boolean iOSTesting = Configuration.iOSTesting;
//    private final String androidAppPath = Configuration.androidAppPath;
//    private final String androidVersion = Configuration.androidVersion;
    private final static ThreadLocal<String> chromeDriverPath = new ThreadLocal<>();
    private final boolean installMobileChromeDriver = Configuration.installMobileChromeDriver;
    private final String appiumUrl = Configuration.appiumUrl;
    private static ThreadLocal<String> emulatorName = new ThreadLocal<>();
//    private final String xcodeOrgId = Configuration.xcodeOrgId;
//    private final String xcodeSigningId = Configuration.xcodeSigningId;
    private final String UDID = Configuration.UDID;
//    private final String bundleId = Configuration.bundleId;
//    private final String updatedWDABundleId = Configuration.updatedWDABundleId;
//    private final DesiredCapabilities addMobileDesiredCapabilities =
//            Configuration.addMobileDesiredCapabilities;
//    private final String AutomationName = Configuration.AutomationName;
//    private final boolean useNewWDA = Configuration.useNewWDA;
    private final String serverLogLevel = Configuration.serverLogLevel;
//    private final String screenshotPath = Configuration.screenshotPath;
    private final int timeStartAppiumServer = Configuration.timeStartAppiumServer;

    public TestUIConfiguration() {
        if (!Configuration.chromeDriverPath.isEmpty()) {
            setChromeDriverPath(Configuration.chromeDriverPath);
        }
        if (!Configuration.emulatorName.isEmpty()) {
            setChromeDriverPath("");
        }
    }

    public void setEmulatorName(String path) {
        emulatorName.set(path);
    }

    public String getEmulatorName() {
        if (emulatorName.get() == null)
            return "";
        return emulatorName.get();
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

    public boolean isUseEmulators() {
        return useEmulators;
    }

    public int getBaseAppiumPort() {
        return baseAppiumPort;
    }

    public int getDriver() {
        return driver;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getAndroidDeviceName() {
        return androidDeviceName;
    }

    public String getUDID() {
        return UDID;
    }

    public String getAppiumUrl() {
        return appiumUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBrowser() {
        return browser;
    }

    public int getTimeStartAppiumServer() {
        return timeStartAppiumServer;
    }

    public String getServerLogLevel() {
        return serverLogLevel;
    }

}
