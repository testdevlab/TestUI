package testUI;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.qameta.allure.Allure;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testUI.Utils.TestUIException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static testUI.ADBUtils.checkAndInstallChromedriver;
import static testUI.TestUIDriver.*;
import static testUI.TestUIServer.stop;
import static testUI.Utils.AppiumHelps.sleep;
import static testUI.elements.TestUI.takeScreenshotsAllure;

public class UIUtils {
    private static Logger logger = LoggerFactory.getLogger(UIUtils.class);
    private volatile static List<AppiumDriverLocalService> service = new ArrayList<>();
    private static List<String> Device = new ArrayList<>();
    private static List<String> DeviceName = new ArrayList<>();
    private static List<String> IOSDevices = new ArrayList<>();
    private static List<String> Emulators = new ArrayList<>();

    protected static void setEmulator(String  emulators) {
        Emulators.add(emulators);
    }

    protected static List<String> getEmulators() {
        return Emulators;
    }

    public static void setDevice(String dev, String deviceName) {
        Device.add(dev);
        DeviceName.add(deviceName);
    }

    public static void setiOSDevice(String dev) {
        IOSDevices.add(dev);
    }

    public static List<String> getIOSDevices() {
        return IOSDevices;
    }

    protected static List<AppiumDriverLocalService> getServices() {
        return service;
    }

    protected static AppiumDriverLocalService getService(int index) {
        return service.get(index);
    }

    public static void setService(AppiumDriverLocalService service) {
        UIUtils.service.add(service);
    }

    public static String getDevice() {
        if (Device.size() >= Configuration.driver) {
            return Device.get(Configuration.driver - 1);
        }
        putErrorLog("No device has been set!");
        return "";
    }

    public static String getDeviceName() {
        return DeviceName.get(Configuration.driver - 1);
    }

    public static List<String> getDevicesNames() {
        return DeviceName;
    }

    public static List<String> getDevices() {
        return Device;
    }

    public static void putLog(String log) {
        logger.info(log);
    }

    public static void putErrorLog(String log) {
        logger.error(log);
    }

    public static void putAllureParameter(String name, String log) {
        logger.info(name + " " + log);
        if (Configuration.useAllure) {
            Allure.parameter(name, log);
        }
    }

    private static void setUpSelenideVariables() {
        SelenideConfig defaults = new SelenideConfig();
        com.codeborne.selenide.Configuration.headless = Configuration.headless;
        com.codeborne.selenide.Configuration.baseUrl = Configuration.baseUrl;
        com.codeborne.selenide.Configuration.startMaximized = Configuration.startMaximized;
        com.codeborne.selenide.Configuration.browser = Configuration.browser;
        com.codeborne.selenide.Configuration.browserBinary = Configuration.browserBinary.isEmpty() ? defaults.browserBinary() : Configuration.browserBinary;
        com.codeborne.selenide.Configuration.browserCapabilities = Configuration.selenideBrowserCapabilities == null ? defaults.browserCapabilities() :
                Configuration.selenideBrowserCapabilities;
        com.codeborne.selenide.Configuration.assertionMode = Configuration.assertionMode == null ? defaults.assertionMode() : Configuration.assertionMode;
        com.codeborne.selenide.Configuration.browserVersion = Configuration.browserVersion.isEmpty() ? defaults.browserVersion() : Configuration.browserVersion;
        com.codeborne.selenide.Configuration.browserSize = Configuration.browserSize.isEmpty() ? defaults.browserSize() : Configuration.browserSize;
        com.codeborne.selenide.Configuration.fastSetValue = Configuration.fastSetValue;
        com.codeborne.selenide.Configuration.remote = Configuration.remote.isEmpty() ? defaults.remote() : Configuration.remote;
        com.codeborne.selenide.Configuration.browserPosition = Configuration.browserPosition.isEmpty() ? defaults.browserPosition() : Configuration.browserPosition;
    }

    protected static void startSelenideDriver(String urlOrRelativeUrl) {
        setUpSelenideVariables();
        open(urlOrRelativeUrl);
    }

    protected static void startFirstIOSBrowserDriver(String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            DesiredCapabilities cap = setIOSCapabilities(true);
            try {
                putLog("Starting appium driver...");
                if (getDrivers().size() == 0) {
                    setDriver(new IOSDriver(new URL(url), cap) {
                    });
                } else {
                    setDriver(new IOSDriver(new URL(url), cap) {
                    }, 0);
                }
                Configuration.driver = 1;
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startFirstAndroidBrowserDriver(String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            DesiredCapabilities cap = setAndroidBrowserCapabilities();
            try {
                putLog("Starting appium driver...");
                if (getDrivers().size() == 0) {
                    setDriver(new AndroidDriver(new URL(url), cap) {
                    });
                } else {
                    setDriver(new AndroidDriver(new URL(url), cap) {
                    }, 0);
                }
                Configuration.driver = 1;
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                if (getDevices().size() != 0) {
                    checkAndInstallChromedriver();
                }
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startBrowserAndroidDriver(DesiredCapabilities desiredCapabilities, String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(Configuration.usePort.size()-1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                setDriver(new AndroidDriver(new URL(url),
                        desiredCapabilities) {
                });
                Configuration.driver = getDrivers().size();
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                if (getDevices().size() != 0) {
                    checkAndInstallChromedriver();
                }
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startBrowserIOSDriver(DesiredCapabilities desiredCapabilities, String urlOrRelativeUrl) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(Configuration.usePort.size()-1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                setDriver(new IOSDriver(new URL(url),
                        desiredCapabilities) {
                });
                Configuration.driver = getDrivers().size();
                getDriver().get(urlOrRelativeUrl);
                break;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                if (getDevices().size() != 0) {
                    checkAndInstallChromedriver();
                }
                sleep(500);
                if (i == 1) {
                    throw new Error(e);
                }
            }
        }
    }

    protected static void startFirstAndroidDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                if (getDrivers().size() == 0) {
                    setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {
                    });
                } else {
                    setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {
                    }, 0);
                }
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new TestUIException("Could not create driver! check that the devices are correctly connected and in debug mode",e);
                }
            }
        }
    }

    protected static void startFirstIOSDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(0) + "/wd/hub" : Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                if (getDrivers().size() == 0) {
                    setDriver(new IOSDriver(new URL(url), desiredCapabilities) {
                    });
                } else {
                    setDriver(new IOSDriver(new URL(url), desiredCapabilities) {
                    }, 0);
                }
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new TestUIException("Could not create driver! check that the devices are correctly connected and in debug mode",e);
                }
            }
        }
    }

    protected static void startAndroidDriver(DesiredCapabilities desiredCapabilities) {
        String url = Configuration.appiumUrl.isEmpty() ? "http://127.0.0.1:" + Configuration.usePort.get(Configuration.usePort.size()-1) + "/wd/hub" :
                Configuration.appiumUrl;
        for (int i = 0; i < 2 ; i++) {
            try {
                setDriver(new AndroidDriver(new URL(url), desiredCapabilities) {});
                break;
            } catch (Exception e) {
                System.err.println("Could not create driver! retrying...");
                sleep(500);
                if (i == 1) {
                    throw new TestUIException("Could not create driver! check that the devices are correctly connected and in debug mode",e);
                }
            }
        }
        Configuration.driver = getDrivers().size();
    }


    public static void executeJs(String var1, Object... var2) {
        try {
            if (Configuration.deviceTests) {
                ((JavascriptExecutor) getDriver()).executeScript(var1, var2);
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(var1, var2);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public static void UIAssert(String reason, boolean assertion){
        if (!assertion && Configuration.useAllure) {
            takeScreenshotsAllure();
        }
        if (!assertion) {
            throw new Error(reason);
        }
    }
}