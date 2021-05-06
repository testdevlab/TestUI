package testUI;

import org.openqa.selenium.Cookie;
import testUI.AndroidUtils.AndroidOpen;
import testUI.IOSUtils.IOSOpen;
import testUI.Utils.TestUIException;
import testUI.elements.TestUI;
import testUI.elements.UIElement;

import java.util.Set;

import static testUI.Configuration.*;
import static testUI.TestUIDriver.*;
import static testUI.UIUtils.putLog;
import static testUI.Utils.Logger.putLogDebug;
import static testUI.Utils.Logger.putLogInfo;
import static testUI.Utils.Performance.setTime;
import static testUI.elements.TestUI.setScreenshotTaken;
import static testUI.elements.Element.setStep;

public class UIOpen {

    private static AndroidOpen androidTestUIDriver = new AndroidOpen();
    private static IOSOpen iOSTestUIOpen = new IOSOpen();
    private static BrowserLogs networkCalls = new BrowserLogs();

    public static UIElement open() {
        networkCalls.setLogs();
        setScreenshotTaken(false);
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM)) {
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
                iOSTestUIOpen.openIOSApp(new TestUIConfiguration());
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
                    iOSTestUIOpen.openIOSApp(new TestUIConfiguration());
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
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM)) {
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
                iOSTestUIOpen.openNewIOSApp(new TestUIConfiguration());
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
                    iOSTestUIOpen.openNewIOSApp(new TestUIConfiguration());
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
        networkCalls.setLogs();
        setScreenshotTaken(false);
        long t = System.currentTimeMillis();
        if (automationType.equals(IOS_PLATFORM)) {
            iOSTestUIOpen.openIOSBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        } else {
            androidTestUIDriver.openBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        }
        long finalTime = System.currentTimeMillis() - t;
        setTime(finalTime);
        putLogDebug("open url -> %s after %d ms", urlOrRelativeUrl, finalTime );
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement navigate(String urlOrRelativeUrl) {
        androidTestUIDriver.navigateURL(urlOrRelativeUrl);
        putLogInfo("navigate to url -> %s", urlOrRelativeUrl);
        return TestUI.E("");
    }

    public static UIElement openNew(String urlOrRelativeUrl) {
        setScreenshotTaken(false);
        if (automationType.equals(IOS_PLATFORM)) {
            iOSTestUIOpen.openNewIOSBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        } else {
            androidTestUIDriver.openNewBrowser(urlOrRelativeUrl, new TestUIConfiguration());
        }
        putLogInfo("open url -> %s", urlOrRelativeUrl);
        setStep(false);
        return TestUI.E("");
    }

    public static UIElement addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        if (!automationType.equals(DESKTOP_PLATFORM)) {
            getDriver().manage().addCookie(cookie);
        } else {
            getSelenideDriver().manage().addCookie(cookie);
        }
        putLogDebug("adding cookie -> %s:%s", key, value);
        return TestUI.E("");
    }

    public static Set<Cookie> getCookies() {
        if (!automationType.equals(DESKTOP_PLATFORM)) {
            return getDriver().manage().getCookies();
        } else {
            return getSelenideDriver().manage().getCookies();
        }
    }
}
