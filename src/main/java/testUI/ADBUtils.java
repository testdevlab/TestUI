package testUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
}