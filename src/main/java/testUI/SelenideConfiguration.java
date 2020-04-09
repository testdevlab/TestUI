package testUI;

import com.codeborne.selenide.AssertionMode;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SelenideConfiguration {

    // TODO: Make all the conf available
//    public static long pollingInterval;
//    public static boolean holdBrowserOpen;
//    public static boolean reopenBrowserOnFail;
    public static String remote = "";
    public static boolean logNetworkCalls = false;
    public static boolean browserLogs = false;
    public static String browserVersion = "";
    public static String browserSize = "";
    public static String browserPosition = "";
    public static boolean startMaximized = false;
    public static DesiredCapabilities selenideBrowserCapabilities = new DesiredCapabilities();
//    public static String pageLoadStrategy;
//    public static boolean clickViaJs;
//    public static boolean screenshots;
//    public static boolean savePageSource;
//    public static String reportsFolder;
//    public static String reportsUrl;
    public static boolean fastSetValue;
//    public static boolean versatileSetValue;
//    public static SelectorMode selectorMode;
    public static AssertionMode assertionMode;
//    public static FileDownloadMode fileDownload;
//    public static boolean proxyEnabled;
//    public static String proxyHost;
//    public static int proxyPort;
//    public static boolean driverManagerEnabled;
    public static boolean headless = false;
    public static String browserBinary = "";
    public static ChromeOptions chromeOptions;
    public static FirefoxOptions firefoxOptions;

}
