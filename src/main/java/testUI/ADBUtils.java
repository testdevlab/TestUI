package testUI;

import org.apache.commons.io.FileUtils;

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
            Process p = Runtime.getRuntime().exec(androidHome + platformTools + "adb start-server");
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

    public static List<String> getDeviceNames() {
        setPathAndCheckAdbServer();
        String s;
        List<String> f = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(androidHome + platformTools + "adb devices");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f.add(s.split("\t")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        f.remove(0);
        return f;
    }

    public static String getDeviceStatus(String device) {
        setPathAndCheckAdbServer();
        String s;
        try {
            Process p = Runtime.getRuntime().exec(androidHome + platformTools + "adb devices");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                if (s.split("\t")[0].equals(device))
                    return s.split("\t")[1];
            }
        } catch (IOException e) {
            System.err.println("Something went wrong when trying to retrieve the status of the device... will try to establish connection anyway");
        }
        return "device";
    }

    public static List<String> getEmulatorName() {
        setPathAndCheckAdbServer();
        String s;
        List<String> f = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(androidHome + emulatorFolder + "emulator -list-avds");
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

    public static String getDeviceVersion(String device) {
        setPathAndCheckAdbServer();
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec(androidHome + platformTools + "adb -s " + device + " shell getprop ro.build.version.release ");
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

    public static String getDeviceModel(String device) {
        setPathAndCheckAdbServer();
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec(androidHome + platformTools + "adb -s " + device + " shell getprop ro.product.model");
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

    public static void stopEmulator(String emulator) {
        setPathAndCheckAdbServer();
        try {
            putLog("Stopping emulator for device: " + emulator
                    + "\n adb -s " + emulator + " emu kill");
            Runtime.getRuntime().exec(androidHome + platformTools + "adb -s " + emulator + " emu kill");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkAndInstallChromedriver() {
        setPathAndCheckAdbServer();
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec(androidHome + platformTools + "adb -s " + getDevice() + " shell dumpsys package com.android.chrome | grep versionName");
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
            String chromedriverVersion = getChromedriverVersion(chromeVersion);
            String ActualVersion = returnChromeDriverVersion();
            String chromedriverPath = getChromeDriverPath();
            if (!Configuration.chromeDriverPath.isEmpty()) {
                putLog("Detected Chrome driver already specified for this device");
            } else if (ActualVersion.contains(chromedriverVersion) || chromedriverVersion.equals("NOT")) {
                putLog("Detected Chrome version = " + chromeVersion + " matches with the actual chromedriver: " + ActualVersion);
            } else if (!doesFileExists(chromedriverPath)) {
                putLog("Detected Chrome version = " + chromeVersion + " but the Appium ChromeDriver is unknown, maybe you should check the appium " +
                        "installation or run npm install appium -g");
            } else if (getTargetDirectory()) {
                putLog("Detected Chrome driver already installed for this device, placed in target directory");
            } else {
                putLog("Detected Chrome version = " + chromeVersion + ". Installing the ChromeDriver: " + chromedriverVersion);
                String chromedriverCustomPath = "";
                Process p2 = Runtime.getRuntime().exec("npm install appium-chromedriver -g --chromedriver_version=\"" + chromedriverVersion + "\"");
                BufferedReader stdInput2 = new BufferedReader(new
                        InputStreamReader(p2.getInputStream()));
                while ((s = stdInput2.readLine()) != null) {
                    if (s.contains("successfully put in place")) {
                        chromedriverCustomPath = "/" + s.split(" /")[1].split(" successfully put in place")[0];
                    }
                }
                Configuration.chromeDriverPath = copyFileToCustomFolder(chromedriverCustomPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String returnChromeDriverVersion() {
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
        }catch (IOException e) {
            e.printStackTrace();
        }
        return ActualVersion;
    }

    private static String getChromeDriverPath() {
        String Platform = System.getProperty("os.name").toLowerCase();
        if (Platform.contains("mac")) {
            return "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/mac/chromedriver";
        } else if (Platform.contains("linux")) {
            return "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/linux/chromedriver";
        } else {
            return "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/win/chromedriver";
        }
    }

    private static String copyFileToCustomFolder(String originalPath) {
        File targetClassesDir = new File(ADBUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File targetDir = targetClassesDir.getParentFile();
        String destinationPath =  targetDir + "/chromedriver" + getDevice();
        try {
            FileUtils.moveFile(
                    FileUtils.getFile(originalPath),
                    FileUtils.getFile(destinationPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destinationPath;
    }

    private static boolean getTargetDirectory() {
        File targetClassesDir = new File(ADBUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File targetDir = targetClassesDir.getParentFile();
        String destinationPath =  targetDir + "/chromedriver" + getDevice();
        File tmpDir = new File(destinationPath);
        if (tmpDir.exists()) {
            Configuration.chromeDriverPath = destinationPath;
        }
        return tmpDir.exists();
    }

    private static boolean doesFileExists(String filePath) {
        File tmpDir = new File(filePath);
        return tmpDir.exists();
    }

    private static String getChromedriverVersion(String chromeVersion) {
        switch (chromeVersion.split("\\.")[0]) {
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
                return "2.44";
            case "71":
                return "2.44";
            case "70":
                return "2.44";
            case "69":
                return "2.44";
            case "68":
                return "2.40";
            case "67":
                return "2.40";
            case "66":
                return "2.40";
            case "65":
                return "2.35";
            case "64":
                return "2.35";
            case "63":
                return "2.35";
            case "62":
                return "2.35";
            case "61":
                return "2.32";
            case "60":
                return "2.32";
            case "59":
                return "2.32";
            default:
                return "NOT";
        }
    }
}