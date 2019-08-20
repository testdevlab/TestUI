package testUI;

import cucumber.api.Scenario;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;

public class Configuration extends SelenideConfiguration {
    public static String browser = "chrome";
    public static String baseUrl = "";
    public static int timeout = 5;
    public static String appPackage = "";
    public static String appActivity = "";
    public static boolean deviceTests = true;
    public static boolean useEmulators = true;
    public static int driver = 1;
    public static int baseAppiumPort = 9586;
    public static int baseAppiumBootstrapPort = 5333;
    public static List<String> usePort = new ArrayList<>();
    public static List<String> useBootstrapPort = new ArrayList<>();
    public static boolean useAllure = true;
    public static String iOSVersion = "";
    public static int launchAppTimeout = 20000;
    public static String iOSAppPath = "";
    public static String iOSDeviceName = "";
    public static String androidDeviceName = "";
    public static boolean iOSTesting = false;
    public static String androidAppPath = "";
    public static String androidVersion = "";
    public static String chromeDriverPath = "";
    public static String appiumUrl = "";
    public static String emulatorName = "";
    public static String xcodeOrgId = "";
    public static String xcodeSigningId = "";
    public static String UDID = "";
    public static String bundleId = "";
    public static String updatedWDABundleId = "";
    public static DesiredCapabilities addMobileDesiredCapabilities = new DesiredCapabilities();
    public static String AutomationName = "";
    public static boolean useNewWDA = true;
    public static String serverLogLevel = "error";
    public static String screenshotPath = "";
    public static int timeStartAppiumServer = 5;

    protected static DesiredCapabilities desiredCapabilities;
    protected static int iOSDevices = 0;
    protected static String firstEmulatorName = "";

    public static void putDataCucumber(Scenario scenario) {
        if (Configuration.deviceTests) {
            if (Configuration.iOSTesting) {
                scenario.write("iOS Device: " + Configuration.iOSDeviceName);
                scenario.write("iOS Version: " + Configuration.iOSVersion);
            } else {
                scenario.write("Android Device: " + Configuration.androidDeviceName);
                scenario.write("Android Version: " + Configuration.androidVersion);
            }
        } else {
            scenario.write("Laptop Browser Testing");
            scenario.write("Browser: " + Configuration.browser);
        }
    }
}
