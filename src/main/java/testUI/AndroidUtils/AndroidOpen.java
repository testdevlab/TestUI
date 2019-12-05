package testUI.AndroidUtils;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIConfiguration;
import testUI.TestUIServer;

import static testUI.AndroidUtils.AndroidCapabilities.setAndroidBrowserCapabilities;
import static testUI.AndroidUtils.AndroidCapabilities.setAppAndroidCapabilities;
import static testUI.AndroidUtils.AndroidTestUIDriver.*;
import static testUI.TestUIDriver.*;

public class AndroidOpen extends TestUIServer {
    private ADBUtils adbUtils = new ADBUtils();

    // ANDROID APP AND BROWSER SUPPORT

    public void openApp(TestUIConfiguration configuration) {
        if (((getAppiumServices().size() == 0 || !getAppiumServices().get(0).isRunning())
                && desiredCapabilities == null) || getDevices().size() == 0
                && configuration.getAppiumUrl().isEmpty()) {
            if (getAppiumServices().size() != 0) {
                stop(1);
            }
            startServerAndDevice(configuration);
            if (getDevices().size() != 0 && configuration.isInstallMobileChromeDriver()) {
                adbUtils.checkAndInstallChromedriver();
            }
            DesiredCapabilities cap = setAppAndroidCapabilities(configuration);
            startFirstAndroidDriver(cap);
            attachShutDownHook(getAppiumServices(), getDrivers());
            setEmulatorIfNeeded(configuration);
            putAllureParameter(
                    "Version",
                    getDriver().getCapabilities().asMap().get("platformVersion").toString()
            );
        } else {
            driver = 1;
            DesiredCapabilities cap = setAppAndroidCapabilities(configuration);
            if (configuration.getAppiumUrl().isEmpty()) {
                putAllureParameter("Using Appium port", getUsePort().get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            startFirstAndroidDriver(cap);
            putAllureParameter(
                    "Version",
                    getDriver().getCapabilities().asMap().get("platformVersion").toString()
            );
        }
        Configuration.emulatorName = "";
    }

    public void openNewApp(TestUIConfiguration configuration) {
        startServerAndDevice(configuration);
        if (getDevices().size() != 0 && Configuration.installMobileChromeDriver) {
            adbUtils.checkAndInstallChromedriver();
        }
        DesiredCapabilities cap = setAppAndroidCapabilities(configuration);
        startAndroidDriver(cap);
        setEmulatorIfNeeded(configuration);
        putAllureParameter(
                "Version",
                getDriver().getCapabilities().asMap().get("platformVersion").toString()
        );
        Configuration.emulatorName = "";
    }

    public void openBrowser(String urlOrRelativeUrl, TestUIConfiguration configuration) {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM) ) {
            urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
            if ((((getAppiumServices().size() == 0 ||
                    !getAppiumServices().get(0).isRunning()) && desiredCapabilities == null) ||
                    getDevices().size() == 0) && configuration.getAppiumUrl().isEmpty()) {
                if (getAppiumServices().size() != 0) {
                    tryStop(1);
                }
                startServerAndDevice(configuration);
                if (getDevices().size() != 0 && Configuration.installMobileChromeDriver) {
                    adbUtils.checkAndInstallChromedriver();
                }
                startFirstAndroidBrowserDriver(urlOrRelativeUrl, configuration);
                attachShutDownHook(getAppiumServices(), getDrivers());
                setEmulatorIfNeeded(configuration);
                putAllureParameter(
                        "Version",
                        getDriver().getCapabilities().asMap().get("platformVersion").toString()
                );
            } else {
                Configuration.driver = 1;
                if (Configuration.appiumUrl.isEmpty()) {
                    putAllureParameter("Using Appium port", getUsePort().get(0));
                } else {
                    putAllureParameter("Using Appium url", Configuration.appiumUrl);
                }
                startFirstAndroidBrowserDriver(urlOrRelativeUrl, configuration);
                putAllureParameter(
                        "Version",
                        getDriver().getCapabilities().asMap().get("platformVersion").toString()
                );
            }
            putAllureParameter("Browser", "Chrome");
        } else {
            startSelenideDriver(urlOrRelativeUrl);
            putAllureParameter("Browser", Configuration.browser);
        }
        Configuration.emulatorName = "";
    }

    public void navigateURL(String urlOrRelativeUrl) {
        urlOrRelativeUrl = Configuration.baseUrl + urlOrRelativeUrl;
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            getDriver().get(urlOrRelativeUrl);
        } else {
            WebDriverRunner.getWebDriver().navigate().to(urlOrRelativeUrl);
        }
    }

    public void openNewBrowser(String urlOrRelativeUrl, TestUIConfiguration configuration) {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
            startServerAndDevice(configuration);
            if (getDevices().size() >= Configuration.driver) {
                adbUtils.checkAndInstallChromedriver();
            }
            DesiredCapabilities cap = setAndroidBrowserCapabilities(configuration);
            startBrowserAndroidDriver(cap, urlOrRelativeUrl);
            attachShutDownHook(getAppiumServices(), getDrivers());
            setEmulatorIfNeeded(configuration);
            putAllureParameter(
                    "Version",
                    getDriver().getCapabilities().asMap().get("platformVersion").toString()
            );
        } else {
            startSelenideDriver(urlOrRelativeUrl);
        }
        putAllureParameter("Browser", Configuration.browser);
    }

    private void setEmulatorIfNeeded(TestUIConfiguration configuration) {
        if (!configuration.getEmulatorName().isEmpty()) {
            setDevice(
                    getDriver().getCapabilities().asMap().get("deviceUDID").toString(),
                    getDriver().getCapabilities().asMap().get("deviceUDID").toString()
            );
            attachShutDownHookStopEmulator(
                    getAppiumServices(),
                    getDriver().getCapabilities().asMap().get("deviceUDID").toString()
            );
        }
    }
}
