package testUI;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.remote.DesiredCapabilities;

import static testUI.ADBUtils.checkAndInstallChromedriver;
import static testUI.Configuration.*;
import static testUI.TestUIDriver.*;
import static testUI.TestUIServer.*;
import static testUI.UIUtils.*;

public class AndroidTestUIDriver {

    // ANDROID APP AND BROWSER SUPPORT

    public static void openApp(TestUIConfiguration configuration) {
        if (((getServices().size() == 0 || !getServices().get(0).isRunning()) && desiredCapabilities == null) || getDevices().size() == 0) {
            if (getServices().size() != 0) {
                stop(1);
            }
            startServerAndDevice(configuration);
            if (getDevices().size() != 0 && configuration.isInstallMobileChromeDriver()) {
                checkAndInstallChromedriver();
            }
            DesiredCapabilities cap = setAppAndroidCapabilities();
            startFirstAndroidDriver(cap);
            attachShutDownHook(getServices(), getDrivers());
            if (!configuration.getEmulatorName().isEmpty()) {
                setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
            }
            putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        } else {
            driver = 1;
            DesiredCapabilities cap = setAppAndroidCapabilities();
            if (configuration.getAppiumUrl().isEmpty()) {
                putAllureParameter("Using Appium port", getUsePort().get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            startFirstAndroidDriver(cap);
            putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        }
        emulatorName = "";
    }

    public static void openNewApp(TestUIConfiguration configuration) {
        deviceTests = true;
        iOSTesting = false;
        startServerAndDevice(configuration);
        if (getDevices().size() != 0 && Configuration.installMobileChromeDriver) {
            checkAndInstallChromedriver();
        }
        DesiredCapabilities cap = setAppAndroidCapabilities();
        startAndroidDriver(cap);
        if (!emulatorName.isEmpty()) {
            setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
            attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
        }
        putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        emulatorName = "";
    }

    public static void openBrowser(String urlOrRelativeUrl, TestUIConfiguration configuration) {
        iOSTesting = false;
        if (deviceTests) {
            urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
            if (((getServices().size() == 0 || !getServices().get(0).isRunning()) && desiredCapabilities == null) || getDevices().size() == 0) {
                if (getServices().size() != 0) {
                    tryStop(1);
                }
                startServerAndDevice(configuration);
                if (getDevices().size() != 0 && Configuration.installMobileChromeDriver) {
                    checkAndInstallChromedriver();
                }
                startFirstAndroidBrowserDriver(urlOrRelativeUrl, configuration);
                attachShutDownHook(getServices(), getDrivers());
                if (!emulatorName.isEmpty()) {
                    setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                    attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                }
                putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
            } else {
                driver = 1;
                if (appiumUrl.isEmpty()) {
                    putAllureParameter("Using Appium port", getUsePort().get(0));
                } else {
                    putAllureParameter("Using Appium url", appiumUrl);
                }
                startFirstAndroidBrowserDriver(urlOrRelativeUrl, configuration);
                putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
            }
        } else {
            startSelenideDriver(urlOrRelativeUrl);
        }
        emulatorName = "";
        putAllureParameter("Browser", browser);
    }

    protected static void navigateURL(String urlOrRelativeUrl) {
        iOSTesting = false;
        urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
        if (deviceTests) {
            getDriver().get(urlOrRelativeUrl);
        } else {
            WebDriverRunner.getWebDriver().navigate().to(urlOrRelativeUrl);
        }
    }

    public static void openNewBrowser(String urlOrRelativeUrl, TestUIConfiguration configuration) {
        iOSTesting = false;
        if (deviceTests) {
            urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
            startServerAndDevice(configuration);
            if (getDevices().size() >= driver) {
                checkAndInstallChromedriver();
            }
            DesiredCapabilities cap = setAndroidBrowserCapabilities(configuration);
            startBrowserAndroidDriver(cap, urlOrRelativeUrl);
            attachShutDownHook(getServices(), getDrivers());
            if (!emulatorName.isEmpty()) {
                setDevice(getDriver().getCapabilities().asMap().get("deviceUDID").toString(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
                attachShutDownHookStopEmulator(getServices(), getDriver().getCapabilities().asMap().get("deviceUDID").toString());
            }
            putAllureParameter("Version", getDriver().getCapabilities().asMap().get("platformVersion").toString());
        } else {
            startSelenideDriver(urlOrRelativeUrl);
        }
        putAllureParameter("Browser", browser);
    }
}
