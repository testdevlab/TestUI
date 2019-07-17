package testUI;

import org.openqa.selenium.remote.DesiredCapabilities;

import static testUI.Configuration.*;
import static testUI.TestUIDriver.setIOSCapabilities;
import static testUI.TestUIServer.startServerAndDevice;
import static testUI.TestUIServer.stop;
import static testUI.UIUtils.*;

public class IOSTestUIDriver {

    // NOW IOS APP AND BROWSER

    public static void openIOSApp() {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        if (((getServices().size() == 0 || !getServices().get(0).isRunning()) && desiredCapabilities == null) || getIOSDevices().size() == 0) {
            if (getServices().size() != 0) {
                stop(1);
            }
            startServerAndDevice();
            DesiredCapabilities cap = setIOSCapabilities(false);
            startFirstIOSDriver(cap);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(false);
            if (appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", usePort.get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            startFirstIOSDriver(cap);
        }
    }

    public static void openNewIOSApp() {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        if (getServices().size() == 0 || !getServices().get(0).isRunning()) {
            startServerAndDevice();
            DesiredCapabilities cap = setIOSCapabilities(false);
            startFirstIOSDriver(cap);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(false);
            putAllureParameter("Using Appium port", usePort.get(0));
            startFirstIOSDriver(cap);
        }
    }

    public static void openIOSBrowser(String urlOrRelativeUrl) {
        deviceTests = true;
        iOSTesting = true;
        urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
        if (((getServices().size() == 0 || !getServices().get(0).isRunning()) && desiredCapabilities == null) || getIOSDevices().size() == 0) {
            if (getServices().size() != 0) {
                stop(1);
            }
            iOSDevices++;
            startServerAndDevice();
            startFirstIOSBrowserDriver(urlOrRelativeUrl);
        } else {
            if (appiumUrl.isEmpty()) {
                putAllureParameter("Using Appium port", usePort.get(0));
            } else {
                putAllureParameter("Using Appium url", appiumUrl);
            }
            startFirstIOSBrowserDriver(urlOrRelativeUrl);
        }
        putAllureParameter("Browser", "Safari");
    }


    public static void openNewIOSBrowser(String urlOrRelativeUrl) {
        deviceTests = true;
        iOSTesting = true;
        iOSDevices++;
        urlOrRelativeUrl = baseUrl + urlOrRelativeUrl;
        if (getServices().size() == 0 || !getServices().get(0).isRunning()) {
            startServerAndDevice();
            DesiredCapabilities cap = setIOSCapabilities(true);
            startBrowserIOSDriver(cap, urlOrRelativeUrl);
        } else {
            DesiredCapabilities cap = setIOSCapabilities(true);
            startBrowserIOSDriver(cap, urlOrRelativeUrl);
        }
        putAllureParameter("Browser", "Safari");
    }
}