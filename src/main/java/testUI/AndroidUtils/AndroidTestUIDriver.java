package testUI.AndroidUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.Configuration;
import testUI.TestUIConfiguration;
import testUI.Utils.TestUIException;

import java.net.MalformedURLException;
import java.net.URL;

import static testUI.AndroidUtils.AndroidCapabilities.setAndroidBrowserCapabilities;
import static testUI.TestUIDriver.*;
import static testUI.TestUIDriver.getDriver;
import static testUI.Utils.AppiumHelps.sleep;

public class AndroidTestUIDriver extends AndroidOpen {
    private static ADBUtils adbUtils = new ADBUtils();

    protected static void startFirstAndroidDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            try {
                if (getDrivers().size() == 0) {
                    setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {
                    });
                } else {
                    setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {
                              },
                            0);
                }
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new TestUIException(
                            "Could not create driver! check that the devices are correctly " +
                                    "connected and in debug mode",
                            e
                    );
                }
            }
        }
    }


    protected static void startAndroidDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(getUsePort().size() - 1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            try {
                setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {
                });
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new TestUIException(
                            "Could not create driver! check that the devices are "
                                    + "correctly connected and in debug mode",
                            e);
                }
            }
        }
        Configuration.driver = getDrivers().size();
    }

    protected static void startFirstAndroidBrowserDriver(
            String urlOrRelativeUrl,
            TestUIConfiguration configuration) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            DesiredCapabilities cap = setAndroidBrowserCapabilities(configuration);
            try {
                putLog("Starting appium driver...");
                Configuration.driver = 1;
                if (getDrivers().size() == 0) {
                    setDriver(new AndroidDriver(new URL(url), cap) {
                    });
                    getDriver().get(urlOrRelativeUrl);
                } else {
                    if (!Configuration.cleanStart && getDrivers().get(0).isBrowser()) {
                        getDrivers().get(0).get(urlOrRelativeUrl);
                    } else {
                        getDrivers().get(0).quit();
                        setDriver(new AndroidDriver(new URL(url), cap) {
                        }, 0);
                        getDrivers().get(0).get(urlOrRelativeUrl);
                    }
                }
                attachShutDownHookStopDriver(getDriver());
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                if (getDevices().size() != 0 && appiumUrl.isEmpty()) {
                    adbUtils.checkAndInstallChromedriver();
                } else if (getDevices().size() == 0 && getEmulators().size() != 0 &&
                        adbUtils.getDeviceNames().size() != 0 && appiumUrl.isEmpty()) {
                    setDevice(
                            adbUtils.getDeviceNames().get(adbUtils.getDeviceNames().size() - 1),
                            getEmulators().get(0)
                    );
                    adbUtils.checkAndInstallChromedriver();
                }
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startBrowserAndroidDriver(
            DesiredCapabilities desiredCapabilities, String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ?
                "http://127.0.0.1:" + getUsePort().get(getUsePort().size() - 1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2; i++) {
            try {
                setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {
                });
                Configuration.driver = getDrivers().size();
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                if (getDevices().size() != 0) {
                    adbUtils.checkAndInstallChromedriver();
                }
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    private static void attachShutDownHookStopDriver(AppiumDriver driver) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(driver::quit)
        );
    }
}
