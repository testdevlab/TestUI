package testUI;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.qameta.allure.Allure;
import org.openqa.selenium.JavascriptExecutor;
import testUI.Utils.TestUIException;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static testUI.TestUIDriver.*;
import static testUI.Utils.Logger.putLogInfo;
import static testUI.Utils.Logger.putLogWarn;
import static testUI.elements.TestUI.takeScreenshotsAllure;

public class UIUtils extends Configuration {
    private volatile static ThreadLocal<List<AppiumDriverLocalService>> service
            = new ThreadLocal<>();
    private static ThreadLocal<List<String>> Device = new ThreadLocal<>();
    private static ThreadLocal<List<String>> DeviceName = new ThreadLocal<>();
    private static ThreadLocal<List<String>> IOSDevices = new ThreadLocal<>();
    private static ThreadLocal<List<String>> Emulators = new ThreadLocal<>();

    protected static void setEmulator(String emulators) {
        List<String> threadDevs;
        if (UIUtils.Emulators.get() == null) {
            threadDevs = new ArrayList<>();
        } else {
            threadDevs = new ArrayList<>(UIUtils.Emulators.get());
        }
        threadDevs.add(emulators);
        Emulators.set(threadDevs);
    }

    public static void removeEmulator(int emulator) {
        List<String> devices = new ArrayList<>(Emulators.get());
        devices.remove(emulator);
        Emulators.set(devices);
    }

    protected static List<String> getEmulators() {
        if (Emulators.get() == null)
            return new ArrayList<>();
        return Emulators.get();
    }

    public static void setDevice(String dev, String deviceName) {
        List<String> threadDevs;
        if (UIUtils.Device.get() == null) {
            threadDevs = new ArrayList<>();
        } else {
            threadDevs = new ArrayList<>(UIUtils.Device.get());
        }
        threadDevs.add(dev);
        List<String> threadDeviceName;
        if (UIUtils.DeviceName.get() == null) {
            threadDeviceName = new ArrayList<>();
        } else {
            threadDeviceName = new ArrayList<>(UIUtils.DeviceName.get());
        }
        threadDeviceName.add(deviceName);
        Device.set(threadDevs);
        DeviceName.set(threadDeviceName);
    }

    public static void setiOSDevice(String dev) {
        List<String> threadDevs;
        if (UIUtils.IOSDevices.get() == null) {
            threadDevs = new ArrayList<>();
        } else {
            threadDevs = new ArrayList<>(UIUtils.IOSDevices.get());
        }
        threadDevs.add(dev);
        IOSDevices.set(threadDevs);
    }

    public static List<String> getIOSDevices() {
        if (IOSDevices.get() == null)
            return new ArrayList<>();
        return IOSDevices.get();
    }

    public static List<AppiumDriverLocalService> getAppiumServices() {
        if (service.get() == null)
            return new ArrayList<>();
        return service.get();
    }

    protected static AppiumDriverLocalService getService(int index) {
        return service.get().get(index);
    }

    public static void setService(AppiumDriverLocalService service) {
        List<AppiumDriverLocalService> appiumServices;
        if (UIUtils.service.get() == null) {
            appiumServices = new ArrayList<>();
        } else {
            appiumServices = new ArrayList<>(UIUtils.service.get());
        }
        appiumServices.add(service);
        UIUtils.service.set(appiumServices);
    }

    public static String getDevice() {
        if (Device.get() == null) {
            putLogWarn("No device has been set!");
            return "";
        }
        if (Device.get().size() >= Configuration.driver) {
            return Device.get().get(Configuration.driver - 1);
        }
        putLogWarn("No device has been set!");
        return "";
    }

    public static String getDeviceName() {
        if (DeviceName.get() == null && DeviceName.get().size() < Configuration.driver) {
            throw new TestUIException(
                    "There is no device set for driver number " +
                            Configuration.driver
            );
        }
        return DeviceName.get().get(Configuration.driver - 1);
    }

    public static List<String> getDevicesNames() {
        if (DeviceName.get() == null) {
            return new ArrayList<>();
        }
        return DeviceName.get();
    }

    public static List<String> getDevices() {
        if (Device.get() == null) {
            return new ArrayList<>();
        }
        return Device.get();
    }

    public static void removeDevice(int device) {
        List<String> devices = new ArrayList<>(Device.get());
        devices.remove(device);
        Device.set(devices);
    }

    public static void putLog(String log) {
        putLogInfo(log);
    }

    public static void putAllureParameter(String name, String log) {
        putLog(name + " " + log);
        if (Configuration.useAllure) {
            Allure.parameter(name, log);
        }
    }

    private static void setUpSelenideVariables() {
        SelenideConfig defaults = new SelenideConfig();
        com.codeborne.selenide.Configuration.screenshots = false;
        com.codeborne.selenide.Configuration.headless = Configuration.headless;
        com.codeborne.selenide.Configuration.baseUrl = Configuration.baseUrl;
        com.codeborne.selenide.Configuration.startMaximized = Configuration.startMaximized;
        com.codeborne.selenide.Configuration.browser = Configuration.browser;
        com.codeborne.selenide.Configuration.browserBinary
                = Configuration.browserBinary.isEmpty()
                ? defaults.browserBinary() : Configuration.browserBinary;
        com.codeborne.selenide.Configuration.browserCapabilities
                = Configuration.selenideBrowserCapabilities == null
                ? defaults.browserCapabilities()
                : Configuration.selenideBrowserCapabilities;
        com.codeborne.selenide.Configuration.assertionMode
                = Configuration.assertionMode == null
                ? defaults.assertionMode() : Configuration.assertionMode;
        com.codeborne.selenide.Configuration.browserVersion
                = Configuration.browserVersion.isEmpty()
                ? defaults.browserVersion() : Configuration.browserVersion;
        com.codeborne.selenide.Configuration.browserSize
                = Configuration.browserSize.isEmpty()
                ? defaults.browserSize() : Configuration.browserSize;
        com.codeborne.selenide.Configuration.fastSetValue = Configuration.fastSetValue;
        com.codeborne.selenide.Configuration.remote
                = Configuration.remote.isEmpty() ? defaults.remote() : Configuration.remote;
        com.codeborne.selenide.Configuration.browserPosition
                = Configuration.browserPosition.isEmpty()
                ? defaults.browserPosition() : Configuration.browserPosition;
    }

    protected static void startSelenideDriver(String urlOrRelativeUrl) {
        setUpSelenideVariables();
        open(urlOrRelativeUrl);
    }


    public static void executeJs(String var1, Object... var2) {
        try {
            if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(var1, var2);
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(var1, var2);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public static void UIAssert(String reason, boolean assertion) {
        if (!assertion && Configuration.useAllure) {
            takeScreenshotsAllure();
        }
        if (!assertion) {
            throw new Error(reason);
        }
    }

    public static void clearBrowserData() {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            getDriver().manage().deleteAllCookies();
            executeJs("localStorage.clear();");
        } else {
            clearBrowserCookies();
            clearBrowserLocalStorage();
        }
    }
}
