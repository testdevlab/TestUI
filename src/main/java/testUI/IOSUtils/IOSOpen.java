package testUI.IOSUtils;

import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIConfiguration;

import static testUI.IOSUtils.IOCapabilities.setIOSCapabilities;
import static testUI.IOSUtils.IOSTestUIDriver.*;
import static testUI.TestUIServer.startServerAndDevice;
import static testUI.TestUIServer.stop;
import static testUI.UIUtils.*;

public class IOSOpen extends Configuration {

    // NOW IOS APP AND BROWSER

    public void openIOSApp(TestUIConfiguration configuration) {
        Configuration.deviceTests = true;
        Configuration.iOSTesting = true;
        Configuration.iOSDevices++;
        if (((getAppiumServices().size() == 0 || getAppiumServices().get(0).isRunning()) &&
                Configuration.desiredCapabilities == null) || getIOSDevices().size() == 0) {
            if (getAppiumServices().size() != 0) {
                stop(1);
            }
            startServerAndDevice(configuration);
            DesiredCapabilities cap = setIOSCapabilities(false);
            startFirstIOSDriver(cap);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(false);
            if (Configuration.appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", Configuration.getUsePort().get(0));
            } else {
                putAllureParameter("Using Appium url", Configuration.appiumUrl);
            }
            startFirstIOSDriver(cap);
        }
    }

    public void openNewIOSApp(TestUIConfiguration configuration) {
        Configuration.deviceTests = true;
        Configuration.iOSTesting = true;
        Configuration.iOSDevices++;
        if (getAppiumServices().size() == 0 || !getAppiumServices().get(0).isRunning()) {
            startServerAndDevice(configuration);
            DesiredCapabilities cap = setIOSCapabilities(false);
            startFirstIOSDriver(cap);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(false);
            putAllureParameter("Using Appium port", Configuration.getUsePort().get(0));
            startFirstIOSDriver(cap);
        }
    }

    public void openIOSBrowser(String urlOrRelativeUrl, TestUIConfiguration configuration) {
        Configuration.deviceTests = true;
        Configuration.iOSTesting = true;
        urlOrRelativeUrl = Configuration.baseUrl + urlOrRelativeUrl;
        if (((getAppiumServices().size() == 0 || !getAppiumServices().get(0).isRunning()) &&
                Configuration.desiredCapabilities == null) || getIOSDevices().size() == 0) {
            if (getAppiumServices().size() != 0) {
                stop(1);
            }
            Configuration.iOSDevices++;
            startServerAndDevice(configuration);
            startFirstIOSBrowserDriver(urlOrRelativeUrl);
        } else {
            if (Configuration.appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", Configuration.getUsePort().get(0));
            } else {
                putAllureParameter("Using Appium url", Configuration.appiumUrl);
            }
            startFirstIOSBrowserDriver(urlOrRelativeUrl);
        }
        putAllureParameter("Browser", "Safari");
    }


    public void openNewIOSBrowser(String urlOrRelativeUrl, TestUIConfiguration configuration) {
        Configuration.deviceTests = true;
        Configuration.iOSTesting = true;
        Configuration.iOSDevices++;
        urlOrRelativeUrl = Configuration.baseUrl + urlOrRelativeUrl;
        if (getAppiumServices().size() == 0 || !getAppiumServices().get(0).isRunning()) {
            startServerAndDevice(configuration);
            DesiredCapabilities cap = setIOSCapabilities(true);
            startBrowserIOSDriver(cap, urlOrRelativeUrl);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(true);
            startBrowserIOSDriver(cap, urlOrRelativeUrl);
        }
        putAllureParameter("Browser", "Safari");
    }
}