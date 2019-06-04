package testUI;

import testUI.elements.TestUI;
import testUI.elements.UIElement;

import static testUI.AndroidDriver.*;
import static testUI.Configuration.*;
import static testUI.IOSDriver.*;
import static testUI.TestUIDriver.getDesiredCapabilities;
import static testUI.UIUtils.putLog;
import static testUI.elements.TestUI.setScreenshotTaken;
import static testUI.elements.UIElement.setStep;

public class UIOpen {
    public static UIElement open() {
        setScreenshotTaken(false);
        if (iOSTesting) {
            if (iOSAppPath.isEmpty() && getDesiredCapabilities() == null) {
                if (!androidAppPath.isEmpty() || (!appActivity.isEmpty() && !appPackage.isEmpty())) {
                    putLog("iOSTesting variable is set to true, but while all the Android variables are correctly set, the iOS ones aren't:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\n trying to start the Android app");
                    openApp();
                } else {
                    throw new Error("iOSAppPath is mandatory field to run iOS apps, here are your iOS values:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion);
                }

            } else {
                openIOSApp();
            }
        } else {
            if (androidAppPath.isEmpty() && (appActivity.isEmpty() && appPackage.isEmpty()) && getDesiredCapabilities() == null) {
                if (!iOSAppPath.isEmpty()) {
                    putLog("iOSTesting variable is set to false, but while all the iOS variables are correctly set, the android ones aren't:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage
                            + "\n trying to start the iOS app");
                    openIOSApp();
                } else {
                    throw new Error("androidAppPath or appActivity and appPackage are mandatory fields to run Android apps, but their values are:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage);
                }
            } else {
                openApp();
            }
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement openNew() {
        setScreenshotTaken(false);
        if (iOSTesting) {
            if (iOSAppPath.isEmpty() && getDesiredCapabilities() == null) {
                if (!androidAppPath.isEmpty() && (!appActivity.isEmpty() && !appPackage.isEmpty())) {
                    putLog("iOSTesting variable is set to true, but while all the Android variables are correctly set, the iOS ones aren't:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion
                            + "\n trying to start the Android app");
                    openNewApp();
                } else {
                    throw new Error("iOSAppPath is mandatory fields to run iOS apps, here are your iOS values:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion);
                }
            } else {
                openNewIOSApp();
            }
        } else {
            if (androidAppPath.isEmpty() && (appActivity.isEmpty() && appPackage.isEmpty()) && getDesiredCapabilities() == null) {
                if (!iOSAppPath.isEmpty()) {
                    putLog("iOSTesting variable is set to false, but while all the iOS variables are correctly set, the android ones aren't:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage
                            + "\n trying to start the iOS app");
                    openNewIOSApp();
                } else {
                    throw new Error("androidAppPath or appActivity and appPackage are mandatory fields to run Android apps, but their values are:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage);
                }
            } else {
                openNewApp();
            }
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement open(String urlOrRelativeUrl) {
        setScreenshotTaken(false);
        if (deviceTests && iOSTesting) {
            openIOSBrowser(urlOrRelativeUrl);
        } else {
            openBrowser(urlOrRelativeUrl);
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement navigate(String urlOrRelativeUrl) {
        navigateURL(urlOrRelativeUrl);
        return TestUI.E("");
    }

    public static UIElement openNew(String urlOrRelativeUrl) {
        setScreenshotTaken(false);
        if (deviceTests && iOSTesting) {
            openNewIOSBrowser(urlOrRelativeUrl);
        } else {
            openNewBrowser(urlOrRelativeUrl);
        }
        setStep(false);
        return TestUI.E("");
    }
}
