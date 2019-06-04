package testUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static testUI.UIUtils.getDevice;
import static testUI.UIUtils.putLog;

public class ADBUtils {

    public static List<String> getDeviceNames() {
        String s;
        List<String> f = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("adb devices");

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
        String s;
        try {
            Process p = Runtime.getRuntime().exec("adb devices");

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
        String s;
        List<String> f = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("emulator -list-avds");
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
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec("adb -s " + device + " shell getprop ro.build.version.release ");
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
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec("adb -s " + device + " shell getprop ro.product.model");
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
        try {
            putLog("Stopping emulator for device: " + emulator
                    + "\n adb -s " + emulator + " emu kill");
            Runtime.getRuntime().exec("adb -s " + emulator + " emu kill");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkAndInstallChromedriver() {
        String s;
        String f = null;
        try {
            Process p = Runtime.getRuntime().exec("adb -s " + getDevice() + " shell dumpsys package com.android.chrome | grep versionName");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                f = s;
                if (f.contains("versionName=")) {
                    break;
                }
            }
            String chromeVersion = f.split("=")[1];
            String chromedriverVersion = getChromedriverVersion(chromeVersion);
            String Platform = System.getProperty("os.name").toLowerCase();
            String ActualVersion = "";
            String chromedriverPath;
            if (Platform.contains("mac")) {
                chromedriverPath = "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/mac/chromedriver -v";
            } else if (Platform.contains("linux")) {
                chromedriverPath = "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/linux/chromedriver -v";
            } else {
                chromedriverPath = "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/win/chromedriver -v";
            }
            Process p3 = Runtime.getRuntime().exec(chromedriverPath);
            BufferedReader stdInput3 = new BufferedReader(new
                    InputStreamReader(p3.getInputStream()));
            while ((s = stdInput3.readLine()) != null) {
                ActualVersion = s;
            }
            if (ActualVersion.contains(chromedriverVersion) || chromedriverVersion.equals("NOT")) {
                putLog("Detected Chrome version = " + chromeVersion + " matches with the actual chromedriver: " + ActualVersion);
            } else if (ActualVersion.isEmpty()) {
                putLog("Detected Chrome version = " + chromeVersion + " but the Appium ChromeDriver is unknown, maybe you should check the appium " +
                        "installation or run npm install appium -g");
            } else {
                putLog("Detected Chrome version = " + chromeVersion + ". Installing the chromedriver: " + chromedriverVersion);
                Process p5 = Runtime.getRuntime().exec("npm uninstall appium -g");
                BufferedReader stdInput5 = new BufferedReader(new
                        InputStreamReader(p5.getInputStream()));
                while ((s = stdInput5.readLine()) != null) {
                    System.out.println(s);
                }
                Process p2 = Runtime.getRuntime().exec("npm install appium -g --chromedriver_version=\"" + chromedriverVersion + "\"");
                BufferedReader stdInput2 = new BufferedReader(new
                        InputStreamReader(p2.getInputStream()));
                while ((s = stdInput2.readLine()) != null) {
                    System.out.println(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getChromedriverVersion(String chromeVersion) {
        switch (chromeVersion.split("\\.")[0]) {
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
                return "2.44";
        }
    }
}