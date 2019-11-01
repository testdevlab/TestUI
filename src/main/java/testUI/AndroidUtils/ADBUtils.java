package testUI.AndroidUtils;

import org.apache.commons.io.FileUtils;
import testUI.TestUIConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static testUI.UIUtils.getDevice;
import static testUI.UIUtils.putLog;

public class ADBUtils {

    private static String androidHome;
    private static String platformTools = "/platform-tools/";
    private static String emulatorFolder = "/emulator/";
    private static final String MAC_CHROME_DRIVER =
            "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/mac" +
                    "/chromedriver";
    private static final String LNX_CHROME_DRIVER =
            "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver" +
                    "/linux/chromedriver";
    private static final String WIN_CHROME_DRIVER =
            "\\node_modules\\appium\\node_modules\\appium-chromedriver\\chromedriver\\win" +
                    "\\chromedriver.exe";
    private final String NPM_WIN = "npm.cmd";
    private final String NPM_LIN_MAC = "npm";

    private static void setPathAndCheckAdbServer() {
        if (System.getenv("ANDROID_HOME") != null) {
            androidHome = System.getenv("ANDROID_HOME");
        } else {
            androidHome = "";
            emulatorFolder = "";
            platformTools = "";
        }
        // Check adb is running
        try {
            String s;
            Process p = Runtime.getRuntime().exec(
                    androidHome
                            + platformTools
                            + "adb start-server");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                if (s.contains("daemon started successfully")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getDeviceNames() {
        setPathAndCheckAdbServer();
        String s;
        List<String> f = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(
                    androidHome +
                            platformTools +
                            "adb devices");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f.add(s.split("\t")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> devices = new ArrayList<>();
        for (String device : f) {
            if (!device.isEmpty()) {
                devices.add(device);
            }
        }
        devices.remove(0);
        return devices;
    }

    public String getDeviceStatus(String device) {
        setPathAndCheckAdbServer();
        String s;
        try {
            Process p = Runtime.getRuntime().exec(
                    androidHome +
                            platformTools +
                            "adb devices");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                if (s.split("\t")[0].equals(device))
                    return s.split("\t")[1];
            }

        } catch (IOException e) {
            System.err.println(
                    "Something went wrong when trying to "
                            + "retrieve the status of the device... "
                            + "will try to establish connection anyway");
        }
        return "device";
    }

    public List<String> getEmulatorName() {
        setPathAndCheckAdbServer();
        String s;
        List<String> f = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(
                    androidHome + emulatorFolder +
                            "emulator -list-avds"
            );
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f.add(s.split("\t")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public String getDeviceVersion(String device) {
        setPathAndCheckAdbServer();
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec(
                    androidHome +
                            platformTools +
                            "adb -s " +
                            device +
                            " shell getprop ro.build.version.release "
            );
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f = s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public String getDeviceModel(String device) {
        setPathAndCheckAdbServer();
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec(
                    androidHome
                            + platformTools
                            + "adb -s "
                            + device
                            + " shell getprop ro.product.model");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f = s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public void stopEmulator(String emulator) {
        setPathAndCheckAdbServer();
        try {
            putLog("Stopping emulator for device: " + emulator
                    + "\n adb -s " + emulator + " emu kill");
            Runtime.getRuntime().exec(
                    androidHome
                            + platformTools
                            + "adb -s "
                            + emulator
                            + " emu kill");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAndInstallChromedriver() {
        setPathAndCheckAdbServer();
        String s;
        String f = null;
        try {
            TestUIConfiguration configuration = new TestUIConfiguration();
            Process p = Runtime.getRuntime().exec(androidHome + platformTools
                            + "adb -s " + getDevice()
                            + " shell dumpsys package com.android.chrome | grep versionName");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f = s;
                if (f.contains("versionName=")) {
                    break;
                }
            }
            String chromeVersion;
            if (f != null) {
                chromeVersion = f.split("=")[1];
            } else {
                chromeVersion = "";
            }
            String chromeDriverVersion = getChromedriverVersion(chromeVersion);
            String ActualVersion = returnChromeDriverVersion();
            String chromeDriverPath = getChromeDriverPath();
            if (!configuration.getChromeDriverPath().isEmpty()) {
                putLog("Detected Chrome driver already specified for this device");
            } else if (ActualVersion.contains(chromeDriverVersion)
                    || chromeDriverVersion.equals("NOT")) {
                putLog("Detected Chrome version = " + chromeVersion
                                + " matches with the actual chromedriver: "
                                + ActualVersion);
            } else if (!doesFileExists(chromeDriverPath)) {
                putLog("Detected Chrome version = " + chromeVersion
                                + " but the Appium ChromeDriver is unknown, "
                                + "maybe you should check the appium "
                                + "installation or run npm install appium -g");
            } else if (getTargetDirectory(configuration, chromeVersion.split("\\.")[0])) {
                putLog("Detected Chrome driver already installed "
                        + "for this device, placed in target directory");
            } else {
                putLog("Detected Chrome version = " + chromeVersion
                                + ". Installing the ChromeDriver: "
                                + chromeDriverVersion);
                String chromeDriverCustomPath = "";
                Process p2 = Runtime.getRuntime().exec(getNPMCmd()
                                + " install appium-chromedriver -g --chromedriver_version=\""
                                + chromeDriverVersion + "\"");
                BufferedReader stdInput2 = new BufferedReader(new
                        InputStreamReader(p2.getInputStream()));
                while ((s = stdInput2.readLine()) != null) {
                    if (s.contains("successfully put in place")) {
                        String Platform = System.getProperty("os.name").toLowerCase();
                        if (Platform.contains("win")) {
                            chromeDriverCustomPath = "C:" +
                                    s.split("C:")[1].split(" successfully put in place")[0];
                        } else {
                            chromeDriverCustomPath = "/" +
                                    s.split(" /")[1].split(" successfully put in place")[0];
                        }
                    }
                }
                configuration.setChromeDriverPath(copyFileToCustomFolder(chromeDriverCustomPath,
                        chromeVersion.split("\\.")[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String returnChromeDriverVersion() {
        String s;
        String ActualVersion = "";
        try {
            String chromedriverPath = getChromeDriverPath();
            Process p3 = Runtime.getRuntime().exec(chromedriverPath + " -v");
            BufferedReader stdInput3 = new BufferedReader(new
                    InputStreamReader(p3.getInputStream()));
            while ((s = stdInput3.readLine()) != null) {
                ActualVersion = s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ActualVersion;
    }

    private String getChromeDriverPath() {
        String Platform = System.getProperty("os.name").toLowerCase();
        if (Platform.contains("mac")) {
            return MAC_CHROME_DRIVER;
        } else if (Platform.contains("linux")) {
            return LNX_CHROME_DRIVER;
        } else {
            return getWinNPMPath() + WIN_CHROME_DRIVER;
        }
    }

    private String getNPMCmd() {
        String Platform = System.getProperty("os.name").toLowerCase();
        if (Platform.contains("mac") || Platform.contains("linux"))
            return NPM_LIN_MAC;
        return NPM_WIN;
    }

    private String getWinNPMPath() {
        String s;
        String path = "";
        try {
            Process p3 = Runtime.getRuntime().exec(getNPMCmd() + " bin -g");
            BufferedReader stdInput3 = new BufferedReader(new
                    InputStreamReader(p3.getInputStream()));
            while ((s = stdInput3.readLine()) != null) {
                path = s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    private static String copyFileToCustomFolder(String originalPath, String chromeVersion) {
        File targetClassesDir = new File(
                ADBUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()
        );
        File targetDir = targetClassesDir.getParentFile();
        String destinationPath;
        String Platform = System.getProperty("os.name").toLowerCase();
        if (Platform.contains("mac") || Platform.contains("linux")) {
            destinationPath = targetDir + "/chromedriver" + chromeVersion;
        } else {
            destinationPath = targetDir + "\\chromedriver" + chromeVersion + ".exe";
        }
        try {
            FileUtils.moveFile(
                    FileUtils.getFile(originalPath),
                    FileUtils.getFile(destinationPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationPath;
    }

    private static boolean getTargetDirectory(TestUIConfiguration configuration,
                                              String chromeVersion) {
        File targetClassesDir = new File(
                ADBUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()
        );
        File targetDir = targetClassesDir.getParentFile();
        String destinationPath;
        String Platform = System.getProperty("os.name").toLowerCase();
        if (Platform.contains("mac") || Platform.contains("linux")) {
            destinationPath = targetDir + "/chromedriver" + chromeVersion;
        } else {
            destinationPath = targetDir + "\\chromedriver" + chromeVersion + ".exe";
        }
        File tmpDir = new File(destinationPath);
        if (tmpDir.exists()) {
            configuration.setChromeDriverPath(destinationPath);
        }
        return tmpDir.exists();
    }

    private static boolean doesFileExists(String filePath) {
        File tmpDir = new File(filePath);
        return tmpDir.exists();
    }

    private static String getChromedriverVersion(String chromeVersion) {
        switch (chromeVersion.split("\\.")[0]) {
            case "78":
                return "78.0.3904.11";
            case "77":
                return "77.0.3865.10";
            case "76":
                return "76.0.3809.68";
            case "75":
                return "75.0.3770.90";
            case "74":
                return "74.0.3729.6";
            case "73":
                return "2.46";
            case "72":
            case "71":
            case "70":
            case "69":
                return "2.44";
            case "68":
            case "67":
            case "66":
                return "2.40";
            case "65":
            case "64":
            case "63":
            case "62":
                return "2.35";
            case "61":
            case "60":
            case "59":
                return "2.32";
            default:
                return "NOT";
        }
    }
}