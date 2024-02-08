package testUI.IOSUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static testUI.UIUtils.putLog;
import static testUI.Utils.Logger.putLogWarn;

public class IOSCommands {
    private Map<String, Map<String, String>> getSimulatorNames() {
        String s;
        List<String> output = new ArrayList<>();
        List<String> versions = new ArrayList<>();
        Map<String, String> devices = new HashMap<>();
        Map<String, Map<String, String>> iOS = new HashMap<>();
        try {
            Process p = Runtime.getRuntime().exec("xcrun simctl list");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean Devices = false;
        for (String line : output) {
            if (Devices && line.contains("iPhone") && !line.toLowerCase().contains("unavailable")) {
                devices.put(
                        line.split(" \\(")[0].split("    ")[1],
                        line.split("\\(")[1].split("\\)")[0]);
            } else if (Devices && line.contains("iOS")) {
                if (!versions.isEmpty() && !devices.isEmpty()) {
                    iOS.put(versions.get(versions.size() - 1), devices);
                    devices = new HashMap<>();
                }
                if (line.split("iOS ").length >= 2) {
                    versions.add(line.split("iOS ")[1].split(" ")[0]);
                }
            }
            if (line.contains("== Devices ==")) {
                Devices = true;
            } else if (line.contains("== Device Pairs ==")) {
                if (!iOS.isEmpty() && !devices.isEmpty())
                    iOS.put(versions.get(versions.size() - 1), devices);
                Devices = false;
            }
        }
        putLog("Found simulator devices: " + iOS);
        return iOS;
    }

    private List<String> getDeviceUDIDs() {
        String s;
        List<String> output = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("idevice_id -l");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private Map<String, String> getSampleSimulator() {
        Map<String, Map<String, String>> simulators = getSimulatorNames();
        Map<String, String> sample = new HashMap<>();
        String version = "";
        String name = "";
        String udid = "";
        for (String keys : simulators.keySet()) {
            version = keys;
            for (String phone : simulators.get(keys).keySet()) {
                name = phone;
                udid = simulators.get(keys).get(phone);
                if (!name.isEmpty() && !udid.isEmpty())
                    break;
            }
            if (!name.isEmpty() && !udid.isEmpty())
                break;
        }
        sample.put("version", version);
        sample.put("name", name);
        sample.put("udid", udid);
        return sample;
    }

    public String getIOSVersion(String udid) {
        String s;
        List<String> output = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(
                    "ideviceinfo -u " +
                            udid +
                            " -k ProductVersion"
            );
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (output.get(0).toLowerCase().contains("no device found"))
            return "13.0";
        return output.get(0);
    }

    public String getIOSName(String udid) {
        String s;
        List<String> output = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("ideviceinfo -u "
                    + udid + " -k DeviceName");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                output.add(s);
                if (s.contains("No device found")) {
                    putLogWarn("No device found with this UDID: " + udid);
                    return "iPhone";
                }
            }
            Process p2 = Runtime.getRuntime().exec(
                    "idevicepair pair " + udid);
            BufferedReader stdInput2 = new BufferedReader(new
                    InputStreamReader(p2.getInputStream()));
            while ((s = stdInput2.readLine()) != null) {
                putLog(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.get(0);
    }


    public Map<String, String> getSampleDevice(int number) {
        if (getDeviceUDIDs().isEmpty() || getDeviceUDIDs().size() <= number) {
            return getSampleSimulator();
        } else {
            Map<String, String> sample = new HashMap<>();
            sample.put("version", getIOSVersion(getDeviceUDIDs().get(number)));
            sample.put("name", getIOSName(getDeviceUDIDs().get(number)));
            sample.put("udid", getDeviceUDIDs().get(number));
            return sample;
        }
    }
}
