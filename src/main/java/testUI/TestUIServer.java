package testUI;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import testUI.AndroidUtils.ADBUtils;
import testUI.IOSUtils.IOSCommands;
import testUI.Utils.AppiumHelps;
import testUI.Utils.AppiumTimeoutException;
import testUI.Utils.GridTestUI;
import testUI.Utils.TestUIException;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static testUI.BrowserLogs.getProxy;
import static testUI.BrowserLogs.stopProxy;
import static testUI.TestUIDriver.*;
import static testUI.Utils.AppiumHelps.sleep;

public class TestUIServer extends UIUtils {
    private static ThreadLocal<Boolean> serviceRunning = new ThreadLocal<>();
    private static ADBUtils adbUtils = new ADBUtils();

    protected static void startServer(
            String port,
            String Bootstrap,
            TestUIConfiguration configuration) {
        AppiumServiceBuilder builder;
        DesiredCapabilities cap;
        //Set Capabilities
        cap = new DesiredCapabilities();
        cap.setCapability("noReset", "false");
        //Build the Appium service
        builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(Integer.parseInt(port));
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "info");
        builder.withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, Bootstrap);
        //Start the server with the builder
        TestUIServer.serviceRunning.set(false);
        boolean slowResponse = false;
        setService(AppiumDriverLocalService.buildService(builder));
        getAppiumServices().get(getAppiumServices().size() - 1).start();
        long t = System.currentTimeMillis();
        long end = t + (configuration.getTimeStartAppiumServer() * 1000);
        while (System.currentTimeMillis() < end) {
            String serviceOut = getAppiumServices().get(getAppiumServices().size() - 1).getStdOut();
            if (serviceOut != null) {
                if (serviceOut.contains("Could not start REST http")) {
                    putLog("Could not start server in port: " +
                            port +
                            "\n Let's try a different one"
                    );
                    TestUIServer.serviceRunning.set(false);
                    slowResponse = false;
                    break;
                } else if (serviceOut.contains(
                        "Appium REST http interface listener started")) {
                    TestUIServer.serviceRunning.set(true);
                    slowResponse = false;
                    break;
                } else {
                    slowResponse = true;
                    TestUIServer.serviceRunning.set(true);
                }
            } else {
                slowResponse = true;
                TestUIServer.serviceRunning.set(false);
            }
            sleep(100);
        }
        if (slowResponse) {
            getAppiumServices().get(getAppiumServices().size() - 1).stop();
            throw new AppiumTimeoutException("Appium server took too long to start");
        }
        if (configuration.getServerLogLevel().equals("error")) {
            getAppiumServices().get(getAppiumServices().size() - 1).clearOutPutStreams();
        }
        if (!TestUIServer.serviceRunning.get()) {
            getAppiumServices().remove(getAppiumServices().size() - 1);
        }
    }

    protected static void attachShutDownHookStopEmulator(
            List<AppiumDriverLocalService> serv,
            List<String> emulators) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopEmulators(serv, emulators)));
    }

    protected static void attachShutDownHookStopEmulator(
            List<AppiumDriverLocalService> serv,
            String device) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> stopEmulators(serv, device))
        );
    }

    private static Thread closeDriverAndServerThread;

    protected static void attachShutDownHook(
            List<AppiumDriverLocalService> serv,
            List<AppiumDriver> drivers) {
        if (closeDriverAndServerThread != null) {
            Runtime.getRuntime().removeShutdownHook(closeDriverAndServerThread);
        }
        closeDriverAndServerThread = new Thread(() -> closeDriverAndServer(serv, drivers));
        Runtime.getRuntime().addShutdownHook(closeDriverAndServerThread);
    }

    private static void stopEmulators(
            List<AppiumDriverLocalService> serv,
            List<String> emulators) {
        for (int i = 0; i < 40; i++) {
            if (serv.size() != 0 && !serv.get(0).isRunning()) {
                AppiumHelps.sleep(1000);
                for (String device : emulators) {
                    adbUtils.stopEmulator(device);
                }
                break;
            }
            AppiumHelps.sleep(1000);
        }
    }

    private static void stopEmulators(
            List<AppiumDriverLocalService> serv,
            String device) {
        for (int i = 0; i < 40; i++) {
            if (serv.size() == 0 || !serv.get(0).isRunning()) {
                AppiumHelps.sleep(1000);
                adbUtils.stopEmulator(device);
                break;
            }
            AppiumHelps.sleep(1000);
        }
    }

    private static void closeDriverAndServer(
            List<AppiumDriverLocalService> serv,
            List<AppiumDriver> drivers) {
        putLog("Stopping drivers");
        for (AppiumDriver driver : drivers) {
            try {
                driver.close();
            } catch (Exception e) {
                putLog("Couldn't close the driver, probably already closed");
            }
            try {
                driver.quit();
            } catch (Exception e) {
                putLog("Couldn't quit the driver, probably already stopped");
            }
        }
        putLog("Running Shutdown Server");
        for (AppiumDriverLocalService service : serv) {
            if (service.isRunning()) {
                service.stop();
            }
        }
    }

    private static void checkIfAppPathExists() {
        if (!Configuration.androidAppPath.isEmpty()) {
            String appPath = Configuration.androidAppPath.charAt(0) == '/' ?
                    Configuration.androidAppPath :
                    System.getProperty("user.dir") + "/" + Configuration.androidAppPath;
            File tmpDir = new File(appPath);
            if (!tmpDir.exists()) {
                Configuration.androidAppPath = "";
                throw new TestUIException("The file for the Android app :" + appPath + " does not exists!");
            }
        }
    }

    public static synchronized void startServerAndDevice(TestUIConfiguration configuration) {
        checkIfAppPathExists();
        int connectedDevices = adbUtils.getDeviceNames().size();
        int startedEmulators = 0;
        for (String devicesNames : adbUtils.getDeviceNames()) {
            if (devicesNames.contains("emulator")) {
                startedEmulators++;
            }
        }
        int emulators = configuration.isUseEmulators() ? adbUtils.getEmulatorName().size() : 0;
        int totalDevices;
        int ports;
        int bootstrap;
        int realDevices;
        if (configuration.isiOSTesting()) {
            ports = configuration.getBaseAppiumPort() + getUsePort().size() * 100 + 5;
            bootstrap = configuration.getBaseAppiumBootstrapPort() +
                    getUseBootstrapPort().size() * 100;
            totalDevices = 10;
            realDevices = 0;

        } else {
            ports = configuration.getBaseAppiumPort() + getUsePort().size() * 100;
            bootstrap = configuration.getBaseAppiumBootstrapPort() +
                    getUseBootstrapPort().size() * 100;
            totalDevices = emulators + connectedDevices - startedEmulators;
            realDevices = totalDevices - emulators;
        }
        String port = String.valueOf(ports);
        String Bootstrap = String.valueOf(bootstrap);
        for (int device = getUsePort().size(); device < totalDevices; device++) {
            if (configuration.getAppiumUrl().isEmpty()) {
                startServer(port, Bootstrap, configuration);
                attachShutDownHook(getAppiumServices(), getDrivers());
            }
            if (serviceRunning.get() || (!configuration.getAppiumUrl().isEmpty() &&
                    getDevices().size() >= device)) {
                setRunDevice(realDevices, connectedDevices, device, configuration);
                break;
            }
            port = String.valueOf(Integer.parseInt(port) + 100);
            Bootstrap = String.valueOf(Integer.parseInt(Bootstrap) + 100);
        }
        if (configuration.getAppiumUrl().isEmpty()) {
            setUsePort(port);
            setUseBootstrapPort(Bootstrap);
            putAllureParameter("Using Appium port", getUsePort()
                    .get(getUsePort().size() - 1));
        } else {
            putAllureParameter("Using Appium url", appiumUrl);
        }
    }

    protected static void setRunDevice(int realDevices,
                                       int connectedDevices,
                                       int device,
                                       TestUIConfiguration configuration) {
        if (!configuration.isiOSTesting()) {
            if (configuration.getAndroidDeviceName().isEmpty() &&
                    configuration.getEmulatorName().isEmpty()) {
                if (connectedDevices <= device) {
                    if (!configuration.isUseEmulators()) {
                        throw new TestUIException("There are not enough devices connected");
                    } else if (adbUtils.getEmulatorName().get(device - realDevices) == null ||

                            adbUtils.getEmulatorName().get(device - realDevices).isEmpty()) {
                        throw new TestUIException("There are no emulators to start the automation");
                    }
                    configuration.setEmulatorName(adbUtils.getEmulatorName()
                            .get(device - realDevices));
                    setEmulator(configuration.getEmulatorName());
                    attachShutDownHookStopEmulator(getAppiumServices(), getEmulators());
                } else {
                    if (!getDevices().toString().contains(adbUtils.getDeviceNames().get(device))) {
                        setDevice(
                                adbUtils.getDeviceNames().get(device),
                                adbUtils.getDeviceNames().get(device)
                        );
                    }
                }
            } else {
                if (configuration.getEmulatorName().isEmpty()) {
                    setDevice(
                            configuration.getAndroidDeviceName(),
                            configuration.getAndroidDeviceName()
                    );
                }
            }
        } else {
            if (UDID.isEmpty()) {
                IOSCommands iosCommands = new IOSCommands();
                Map<String, String> sampleIOSDevice = iosCommands.getSampleDevice(device);
                UDID = sampleIOSDevice.get("udid");
                iOSDeviceName = sampleIOSDevice.get("name");
                iOSVersion = sampleIOSDevice.get("version");
            }
            setiOSDevice(iOSDeviceName);
        }
        driver = Configuration.automationType.equals(Configuration.IOS_PLATFORM) ?
                getDevices().size() + getIOSDevices().size() : getDevices().size();
        driver = configuration.getEmulatorName().isEmpty() ? driver : driver + 1;
    }

    public static void stop(int driver) {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            removeUsePort(driver - 1);
            removeUseBootstrapPort(driver - 1);
            if (Configuration.automationType.equals(IOS_PLATFORM)) {
                getDrivers().get(driver - 1).close();
                sleep(500);
            }
            getDrivers().get(driver - 1).quit();
            removeDriver(driver - 1);
            getAppiumServices().get(driver - 1).stop();
            getAppiumServices().remove(driver - 1);
            if (getDevices().size() != 0) {
                iOSDevices = driver - getDevices().size();
                adbUtils.stopEmulator(getDevices().get(driver - iOSDevices - 1));
                removeDevice(driver - iOSDevices - 1);
            }
            Configuration.driver = getDrivers().size();
        } else {
            try {
                getSelenideDriver().close();
                getSelenideDriver().quit();
            } catch (Exception e) {
                putLog("Browser closed already");
            }
            closeWebDriver();
        }
        try {
            if (getProxy() != null && getProxy().isStarted()) {
                stopProxy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void tryStop(int driver) {
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            try {
                removeUsePort(driver - 1);
                removeUseBootstrapPort(driver - 1);
            } catch (Exception e) {
                putLog("could not remove ports");
            }
            try {
                getDrivers().get(driver - 1).quit();
            } catch (Exception e) {
                System.err.println("Could not quit driver, probably already stopped");
            }
            try {
                removeDriver(driver - 1);
            } catch (Exception e) {
                putLog("could not remove driver");
            }
            try {
                if (getAppiumServices().size() == driver) {
                    getAppiumServices().get(driver - 1).stop();
                    getAppiumServices().remove(driver - 1);
                }
            } catch (Exception e) {
                putLog("Could not remove services");
            }
            if (getDevices().size() != 0) {
                adbUtils.stopEmulator(getDevices().get(driver - 1));
                removeDevice(driver - 1);
            }
            Configuration.driver = getDrivers().size();
        } else {
            try {
                getSelenideDriver().close();
                getSelenideDriver().quit();
            } catch (Exception e) {
                putLog("Browser closed already");
            }
            closeWebDriver();
        }
        try {
            if (getProxy() != null && getProxy().isStarted()) {
                stopProxy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        GridTestUI grid = new GridTestUI();
        grid.releaseAppiumSession();
        if (!Configuration.automationType.equals(DESKTOP_PLATFORM)) {
            removeUsePort(driver - 1);
            removeUseBootstrapPort(driver - 1);
            if (Configuration.automationType.equals(IOS_PLATFORM)) {
                getDrivers().get(driver - 1).close();
                sleep(500);
            }
            getDrivers().get(driver - 1).quit();
            removeDriver(driver - 1);
            if (getAppiumServices() != null && getAppiumServices().size() >= driver) {
                getAppiumServices().get(driver - 1).stop();
                getAppiumServices().remove(driver - 1);
            }
            if (getDevices().size() != 0) {
                iOSDevices = driver - getDevices().size();
                adbUtils.stopEmulator(getDevices().get(driver - iOSDevices - 1));
                removeDevice(driver - iOSDevices - 1);
            }
            if (getEmulators().size() != 0) {
                if ((driver - iOSDevices - 1) < getEmulators().size()) {
                    adbUtils.stopEmulator(getEmulators().get(driver - iOSDevices - 1));
                    removeEmulator(driver - iOSDevices - 1);
                }
                if ((driver - iOSDevices - 1) < getDevices().size()) {
                    removeDevice(driver - iOSDevices - 1);
                }
            }
            desiredCapabilities = null;
            driver = getDrivers().size();
        } else {
            try {
                getSelenideDriver().close();
                getSelenideDriver().quit();
            } catch (Exception e) {
                putLog("Browser closed already");
            }
            closeWebDriver();
        }
        try {
            if (getProxy() != null && getProxy().isStarted()) {
                stopProxy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
