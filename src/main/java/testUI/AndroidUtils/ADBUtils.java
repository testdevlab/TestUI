package testUI.AndroidUtils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import testUI.TestUIConfiguration;
import testUI.Utils.TestUIException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static testUI.UIUtils.getDevice;
import static testUI.UIUtils.putLog;
import static testUI.Utils.Logger.putLogWarn;

public class ADBUtils {

    private static String androidHome;
    private static String platformTools = "/platform-tools/";
    private static String emulatorFolder = "/emulator/";
    public static String MAC_CHROME_DRIVER =
            "/usr/local/bin/chromedriver";
    public static String LNX_CHROME_DRIVER =
            "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver" +
                    "/linux/chromedriver*";
    public static String WIN_CHROME_DRIVER =
            "\\node_modules\\appium\\node_modules\\appium-chromedriver\\chromedriver\\win" +
                    "\\chromedriver.exe";

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
        if (device == null || device.isEmpty()) {
            throw new TestUIException("device name cannot be empty");
        }
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
            if (getDeviceNames().contains(emulator))
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
                configuration.setChromeDriverPath(chromeDriverPath);
            } else if (getTargetDirectory(configuration, chromeVersion.split("\\.")[0])) {
                putLog("Detected Chrome driver already installed "
                        + "for this device, placed in target directory");
            } else {
                if (!doesFileExists(chromeDriverPath)) {
                    putLog("Detected Chrome version = " + chromeVersion
                            + " but the ChromeDriver is unknown, "
                            + "maybe you should check the appium/chromedriver "
                            + "installation");
                }
                putLog("Detected Chrome version = " + chromeVersion
                                + ". Installing the ChromeDriver: "
                                + chromeDriverVersion);
                WebDriverManager.chromedriver().driverVersion(chromeDriverVersion).setup();
                configuration.setChromeDriverPath(
                        copyFileToCustomFolder(System.getProperty("webdriver.chrome.driver"),
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
            return "npm";
        return "npm.cmd";
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
            Files.move(
                    Paths.get(originalPath),
                    Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
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
        String version = chromeVersion.split("\\.")[0] + ".";
        String URL = "https://chromedriver.storage.googleapis.com/";
        HttpGet get = new HttpGet(URL);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String body = "";
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            body = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            putLogWarn("Could not connect to \"https://chromedriver.storage.googleapis.com/\"");
            return "80";
        }
        String chromeDriverVersion = "";
        String chromeName = "";
        String Platform = System.getProperty("os.name").toLowerCase();
        if (Platform.contains("mac")) {
            System.out.println(System.getProperty("os.arch"));
            if (System.getProperty("os.arch").toLowerCase().contains("aarch64")) {
                chromeName = "mac-aarch64";
            } else {
                chromeName = "/chromedriver_mac64.zip";
            }
        } else if (Platform.contains("linux")) {
            chromeName = "/chromedriver_linux64.zip";
        } else {
            chromeName = "/chromedriver_win32.zip";
        }
        Document doc = loadXMLFromString(body);
        // Getting each of the chrome driver versions from the chromium api
        NodeList list = doc.getDocumentElement().getElementsByTagName("Contents");

        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String text = element.getTextContent();
                // Check that the version and platform matches

                if (text.startsWith(version) && chromeName.equals("mac-aarch64")) {
                    if (text.contains("mac_arm64") || text.contains("m1")) {
                        chromeDriverVersion = text.split("/")[0];
                    }
                }
                if (text.startsWith(version) && text.contains(chromeName)) {
                    chromeDriverVersion = text.split("/")[0];
                }
            }
        }

        return chromeDriverVersion;
    }

    private static Document loadXMLFromString(String xmlString)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }
}
