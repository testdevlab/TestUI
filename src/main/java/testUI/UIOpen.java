package testUI;

import org.openqa.selenium.Cookie;
import testUI.Utils.TestUIException;
import testUI.elements.TestUI;
import testUI.elements.UIElement;

import java.util.Set;

import static testUI.Configuration.*;
import static testUI.TestUIDriver.*;
import static testUI.UIUtils.putLog;
import static testUI.elements.TestUI.setScreenshotTaken;
import static testUI.elements.Element.setStep;

public class UIOpen {

    private static AndroidTestUIDriver androidTestUIDriver = new AndroidTestUIDriver();
    private static IOSTestUIDriver iOSTestUIDriver = new IOSTestUIDriver();
    private static NetworkCalls networkCalls = new NetworkCalls();

    public static UIElement open() {
        networkCalls.setNetworkCalls();
        setScreenshotTaken(false);
        if (iOSTesting) {
            if (iOSAppPath.isEmpty() && bundleId.isEmpty() && getDesiredCapabilities() == null) {
                if (!androidAppPath.isEmpty()
                        || (!appActivity.isEmpty() && !appPackage.isEmpty())) {
                    putLog(
                            "iOSTesting variable is set to true, "
                                    + "but while all the Android variables "
                                    + "are correctly set, the iOS ones aren't:"
                                    + "\niOSAppPath = " + iOSAppPath
                                    + "\n trying to start the Android app");
                    androidTestUIDriver.openApp(new TestUIConfiguration());
                } else {
                    throw new TestUIException(
                            "iOSAppPath or bundleId is mandatory field to run iOS apps, "
                                    + "here are your iOS values:"
                                    + "\niOSAppPath = " + iOSAppPath
                                    + "\nbundelId = " + bundleId
                                    + "\niOSDeviceName = " + iOSDeviceName
                                    + "\niOSVersion = " + iOSVersion);
                }

            } else {
                iOSTestUIDriver.openIOSApp(new TestUIConfiguration());
            }
        } else {
            if (androidAppPath.isEmpty() &&
                    (appActivity.isEmpty() && appPackage.isEmpty())
                    && getDesiredCapabilities() == null) {
                if (!iOSAppPath.isEmpty()) {
                    putLog("iOSTesting variable is set to false, " +
                            "but while all the iOS variables are correctly "
                            + "set, the android ones aren't:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage
                            + "\n trying to start the iOS app");
                    iOSTestUIDriver.openIOSApp(new TestUIConfiguration());
                } else {
                    throw new TestUIException(
                            "androidAppPath or appActivity and appPackage are "
                                    + "mandatory fields to run Android apps, but their values are:"
                                    + "\nandroidAppPath = " + androidAppPath
                                    + "\nappActivity = " + appActivity
                                    + "\nappPackage = " + appPackage);
                }
            } else {
                androidTestUIDriver.openApp(new TestUIConfiguration());
            }
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement openNew() {
        setScreenshotTaken(false);
        if (iOSTesting) {
            if (iOSAppPath.isEmpty() && bundleId.isEmpty() && getDesiredCapabilities() == null) {
                if (!androidAppPath.isEmpty()
                        && (!appActivity.isEmpty() && !appPackage.isEmpty())) {
                    putLog("iOSTesting variable is set to true, "
                            + "but while all the Android variables are "
                            + "correctly set, the iOS ones aren't:"
                            + "\niOSAppPath = " + iOSAppPath
                            + "\niOSDeviceName = " + iOSDeviceName
                            + "\niOSVersion = " + iOSVersion
                            + "\n trying to start the Android app");
                    androidTestUIDriver.openNewApp(new TestUIConfiguration());
                } else {
                    throw new TestUIException(
                            "iOSAppPath is mandatory fields to run iOS apps, here are your "
                                    + "iOS values:"
                                    + "\niOSAppPath = " + iOSAppPath
                                    + "\niOSDeviceName = " + iOSDeviceName
                                    + "\niOSVersion = " + iOSVersion);
                }
            } else {
                iOSTestUIDriver.openNewIOSApp(new TestUIConfiguration());
            }
        } else {
            if (androidAppPath.isEmpty() &&
                    (appActivity.isEmpty() && appPackage.isEmpty())
                    && getDesiredCapabilities() == null) {
                if (!iOSAppPath.isEmpty()) {
                    putLog("iOSTesting variable is set to false, "
                            + "but while all the iOS variables are "
                            + "correctly set, the android ones aren't:"
                            + "\nandroidAppPath = " + androidAppPath
                            + "\nappActivity = " + appActivity
                            + "\nappPackage = " + appPackage
                            + "\n trying to start the iOS app");
                    iOSTestUIDriver.openNewIOSApp(new TestUIConfiguration());
                } else {
                    throw new TestUIException(
                            "androidAppPath or appActivity and appPackage are "
                                    + "mandatory fields to run Android apps, but their values are:"
                                    + "\nandroidAppPath = " + androidAppPath
                                    + "\nappActivity = " + appActivity
                                    + "\nappPackage = " + appPackage);
                }
            } else {
                androidTestUIDriver.openNewApp(new TestUIConfiguration());
            }
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement open(String urlOrRelativeUrl) {
        networkCalls.setNetworkCalls();
        setScreenshotTaken(false);
        if (deviceTests && iOSTesting) {
            iOSTestUIDriver.openIOSBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        } else {
            androidTestUIDriver.openBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement navigate(String urlOrRelativeUrl) {
        androidTestUIDriver.navigateURL(urlOrRelativeUrl);
        return TestUI.E("");
    }

    public static UIElement openNew(String urlOrRelativeUrl) {
        setScreenshotTaken(false);
        if (deviceTests && iOSTesting) {
            iOSTestUIDriver.openNewIOSBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        } else {
            androidTestUIDriver.openNewBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        }
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        if (deviceTests) {
            getDriver().manage().addCookie(cookie);
        } else {
            getSelenideDriver().manage().addCookie(cookie);
        }
        return TestUI.E("");
    }

    public static Set<Cookie> getCookies() {
        if (deviceTests) {
            return getDriver().manage().getCookies();
        } else {
            return getSelenideDriver().manage().getCookies();
        }
    }
}
