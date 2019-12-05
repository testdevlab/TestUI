package testUI.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import testUI.Configuration;

import java.io.IOException;

import static testUI.Configuration.browser;
import static testUI.SelenideConfiguration.remote;
import static testUI.UIUtils.putAllureParameter;

public class GridTestUI {
    // one instance, reuse
    private static String appiumURL = "";
    private static String UDID = "";
    private static String deviceName = "";
    private static String cliServerURL = "";
    private static boolean usingGrid = false;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static String browserName = "";
    private static String platformName = "";

    public boolean isUsingGrid() {
        return usingGrid;
    }

    public String getCliServerURL() {
        return cliServerURL;
    }

    public String getUDID() {
        return UDID;
    }

    private void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSeleniumConfiguration(String CliServerURL) {
        cliServerURL = CliServerURL;
        usingGrid = true;
        HttpPost post = new HttpPost(CliServerURL + "/session");
        String postBody = "{\"selenium\":{\"browser\": \"" + Configuration.browser + "\"}}";
        if (remote.isEmpty()) {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                StringEntity requestEntity = new StringEntity(postBody, ContentType.APPLICATION_JSON);
                post.setEntity(requestEntity);
                CloseableHttpResponse response = httpClient.execute(post);
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));

                Configuration.remote = json.getString("proxyURL");
                if (getPlatform(json) != null) {
                    Configuration.selenideBrowserCapabilities.setCapability(
                            CapabilityType.PLATFORM_NAME, getPlatform(json));
                }
                Configuration.deviceTests = false;
            } catch (IOException e) {
                throw new TestUIException("Could not retrieve remote configuration with testUI " +
                        "server: \n" + e.toString());
            } finally {
                close();
            }
            attachShutDownHookSReleaseDevice(CliServerURL, UDID);
        }
    }

    public void setAppiumAndroidConfiguration(String CliServerURL) {
        cliServerURL = CliServerURL;
        usingGrid = true;
        HttpPost post = new HttpPost(CliServerURL + "/session");
        String postBody = "{\"appium\":{\"os\": \"Android\"}}";
        if (appiumURL.isEmpty()) {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                StringEntity requestEntity = new StringEntity(postBody, ContentType.APPLICATION_JSON);
                post.setEntity(requestEntity);
                CloseableHttpResponse response = httpClient.execute(post);
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                appiumURL = json.getString("proxyURL");
                UDID = json.getString("udid");
                Configuration.androidDeviceName = json.getString("deviceName");
                deviceName = Configuration.androidDeviceName;
                putAllureParameter("Device name", deviceName);
                Configuration.androidVersion = json.getString("version");
                if (Configuration.androidVersion.startsWith("4") ||
                        Configuration.androidVersion.startsWith("3") ||
                        Configuration.androidVersion.startsWith("2")) {
                    Configuration.AutomationName = "UiAutomator1";
                }
                Configuration.systemPort = json.getInt("port") + 100;
                Configuration.chromeDriverPort = json.getInt("port") + 101;
                Configuration.appiumUrl = appiumURL;
                Configuration.UDID = UDID;
                Configuration.deviceTests = true;
                Configuration.iOSTesting = false;
            } catch (IOException e) {
                throw new TestUIException("Could not retrieve remote configuration with testUI " +
                        "server: \n" + e.toString());
            } finally {
                close();
            }
            attachShutDownHookSReleaseDevice(CliServerURL, UDID);
        }
    }

    public void setAppiumAndroidConfiguration() {
        usingGrid = true;
        HttpPost post = new HttpPost(cliServerURL + "/session");
        String postBody = "{\"appium\":{\"os\": \"Android\", \"deviceName\": \"" + deviceName + "\"}}";
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(postBody, ContentType.APPLICATION_JSON);
            post.setEntity(requestEntity);
            CloseableHttpResponse response = httpClient.execute(post);
            JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
            appiumURL = json.getString("proxyURL");
            UDID = json.getString("udid");
            Configuration.androidDeviceName = json.getString("deviceName");
            deviceName = Configuration.androidDeviceName;
            putAllureParameter("Device name", json.getString("deviceName"));
            Configuration.androidVersion = json.getString("version");
            if (Configuration.androidVersion.startsWith("4") ||
                    Configuration.androidVersion.startsWith("3") ||
                    Configuration.androidVersion.startsWith("2")) {
                Configuration.AutomationName = "UiAutomator1";
            }
            Configuration.systemPort = json.getInt("port") + 100;
            Configuration.chromeDriverPort = json.getInt("port") + 101;
            Configuration.appiumUrl = appiumURL;
            Configuration.UDID = UDID;
            Configuration.deviceTests = true;
            Configuration.iOSTesting = false;
        } catch (IOException e) {
            throw new TestUIException("Could not retrieve remote configuration with testUI " +
                    "server: \n" + e.toString());
        } finally {
            close();
        }
    }

    public void setAppiumiOSConfiguration(String CliServerURL) {
        usingGrid = true;
        HttpPost post = new HttpPost(CliServerURL + "/session");
        String postBody = "{\"appium\":{\"os\": \"IOS\"}}";
        if (appiumURL.isEmpty()) {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                StringEntity requestEntity = new StringEntity(postBody, ContentType.APPLICATION_JSON);
                post.setEntity(requestEntity);
                CloseableHttpResponse response = httpClient.execute(post);
                JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
                appiumURL = json.getString("proxyURL");
                UDID = json.getString("udid");
                Configuration.iOSDeviceName = json.getString("deviceName");
                deviceName = Configuration.iOSDeviceName;
                Configuration.iOSVersion = json.getString("version");
                Configuration.wdaPort = json.getInt("port") + 100;
                Configuration.appiumUrl = appiumURL;
                Configuration.UDID = UDID;
                Configuration.deviceTests = true;
                Configuration.iOSTesting = true;
            } catch (IOException e) {
                throw new TestUIException("Could not retrieve remote configuration with testUI " +
                        "server: \n" + e.toString());
            } finally {
                close();
            }
            attachShutDownHookSReleaseDevice(CliServerURL, UDID);
        }
    }

    public void setAppiumRelease(String CliServerURL, String UDID) {
        HttpPost post = new HttpPost(CliServerURL + "/release");
        String postBody = "{\"appium\":{\"udid\": \"" + UDID + "\"}}";
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(postBody, ContentType.APPLICATION_JSON);

            post.setEntity(requestEntity);
            httpClient.execute(post);
        } catch (IOException e) {
            throw new TestUIException("Could not retrieve remote configuration with testUI " +
                    "server: \n" + e.toString());
        } finally {
            close();

        }
    }

    public void releaseAppiumSession() {
        if (isUsingGrid()) {
            setAppiumRelease(getCliServerURL(), getUDID());
            Configuration.appiumUrl = "";
        }
    }

    private void attachShutDownHookSReleaseDevice(
            String url, String UDID) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> setAppiumRelease(url, UDID))
        );
    }

    private Platform getPlatform(JSONObject json) {
        switch (json.getString("os").toLowerCase().charAt(0)) {
            case 'w':
                return Platform.WINDOWS;
            case 'm':
                return Platform.MAC;
            case 'l':
                return Platform.LINUX;
            default:
                return null;
        }
    }

    private String setSeleniumJsonBody() {
        String jsonBody = "{\"selenium\":{\"browser\": \"" + browser + "\"";
        if (!platformName.isEmpty()) {
            jsonBody = jsonBody.concat(", \"os\": \"" + platformName + "\"");
        }

        jsonBody = jsonBody.concat("}}");

        return jsonBody;

    }
}

