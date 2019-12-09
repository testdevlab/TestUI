package testUI.Utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import testUI.Configuration;

import java.io.IOException;

import static testUI.Configuration.browser;
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
    private static boolean mobile = false;

    public final String IOS_PLATFORM = "ios";
    public final String ANDROID_PLATFORM = "android";

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

    public void setAppiumAndroidConfiguration() {
        usingGrid = true;
        HttpPost post = new HttpPost(cliServerURL + "/session/start");
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
                Configuration.automationName = "UiAutomator1";
            }
            Configuration.systemPort = json.getInt("port") + 100;
            Configuration.chromeDriverPort = json.getInt("port") + 101;
            Configuration.appiumUrl = appiumURL;
            Configuration.UDID = UDID;
            Configuration.automationType = Configuration.ANDROID_PLATFORM;
        } catch (IOException e) {
            throw new TestUIException("Could not retrieve remote configuration with testUI " +
                    "server: \n" + e.toString());
        } finally {
            close();
        }
    }

    public void setConfiguration() {
        usingGrid = true;
        if (platformName.toLowerCase().equals(IOS_PLATFORM) ||
                platformName.toLowerCase().equals(ANDROID_PLATFORM)) {
            mobile = true;
        }
        if (mobile) {
            if (appiumURL.isEmpty()) {
                JSONObject json = makeRequest();
                if (platformName.toLowerCase().equals(IOS_PLATFORM)) {
                    setIOSConfiguration(json);
                } else {
                    setAndroidConfiguration(json);
                }
                attachShutDownHookSReleaseDevice(cliServerURL, UDID);
            }
        } else {
            JSONObject json = makeRequest();
            setDesktopConfiguration(json);
        }
    }

    public void setAppiumRelease(String CliServerURL, String UDID) {
        HttpPost post = new HttpPost(CliServerURL + "/session/release");
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

    public GridTestUI setPlatform(String OS) {
        platformName = OS;

        return this;
    }

    public GridTestUI setDeviceName(String deviceName) {
        GridTestUI.deviceName = deviceName;

        return this;
    }

    public GridTestUI setUDID(String UDID) {
        GridTestUI.UDID = UDID;

        return this;
    }

    public GridTestUI setBrowserName(String browser) {
        browserName = browser;
        return this;
    }

    public GridTestUI setServerURL(String url) {
        cliServerURL = url;

        return this;
    }

    private String setJsonBody() {
        String jsonBody;
        if (!mobile) {
            jsonBody = "{\"selenium\":{\"browser\": \"" + browserName + "\"";
            if (!platformName.isEmpty()) {
                jsonBody = jsonBody.concat(", \"os\": \"" +
                        platformName + "\"");
            }
        } else {
            if (platformName.isEmpty() || platformName.toLowerCase().equals("android")) {
                platformName = "Android";
            } else {
                platformName = "IOS";
            }
            jsonBody = "{\"appium\":{\"os\": \"" + platformName + "\"";
            if (!deviceName.isEmpty()) {
                jsonBody = jsonBody.concat(", \"deviceName\": \"" + deviceName + "\"");
            }
            if (!UDID.isEmpty()) {
                jsonBody = jsonBody.concat(", \"deviceName\": \"" + UDID + "\"");
            }
        }

        jsonBody = jsonBody.concat("}}");

        return jsonBody;
    }

    private JSONObject makeRequest() {
        HttpPost post = new HttpPost(cliServerURL + "/session/start");
        String postBody = setJsonBody();
        JSONObject json;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            StringEntity requestEntity = new StringEntity(postBody, ContentType.APPLICATION_JSON);
            post.setEntity(requestEntity);
            CloseableHttpResponse response = httpClient.execute(post);
            json = new JSONObject(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            throw new TestUIException("Could not retrieve remote configuration with testUI " +
                    "server: \n" + e.toString());
        } finally {
            close();
        }

        return json;
    }

    private void setIOSConfiguration(JSONObject json) {
        try {
            appiumURL = json.getString("proxyURL");
            UDID = json.getString("udid");
            Configuration.iOSDeviceName = json.getString("deviceName");
            deviceName = Configuration.iOSDeviceName;
            Configuration.iOSVersion = json.getString("version");
            Configuration.wdaPort = json.getInt("port") + 100;
            Configuration.appiumUrl = appiumURL;
            Configuration.UDID = UDID;
            Configuration.automationType = Configuration.IOS_PLATFORM;
        } catch (Exception e) {
            throw new TestUIException("there is no device available with this specifications: \n" +
                    setJsonBody());
        }
    }

    private void setAndroidConfiguration(JSONObject json) {
        try {
            appiumURL = json.getString("proxyURL");
            UDID = json.getString("udid");
            Configuration.androidDeviceName = json.getString("deviceName");
            deviceName = Configuration.androidDeviceName;
            putAllureParameter("Device name", json.getString("deviceName"));
            Configuration.androidVersion = json.getString("version");
            if (Configuration.androidVersion.startsWith("4") ||
                    Configuration.androidVersion.startsWith("3") ||
                    Configuration.androidVersion.startsWith("2")) {
                Configuration.automationName = "UiAutomator1";
            }
            Configuration.systemPort = json.getInt("port") + 100;
            Configuration.chromeDriverPort = json.getInt("port") + 101;
            Configuration.appiumUrl = appiumURL;
            Configuration.UDID = UDID;
            Configuration.automationType = Configuration.ANDROID_PLATFORM;
        } catch (JSONException e) {
            throw new TestUIException("There is no node available for the specified parameters " +
                    "\n"  + setJsonBody());
        }
    }

    private void setDesktopConfiguration(JSONObject json) {
        try {
            Configuration.remote = json.getString("proxyURL");
            if (getPlatform(json) != null) {
                System.out.println( getPlatform(json));
                Configuration.selenideBrowserCapabilities.setCapability(
                        CapabilityType.PLATFORM_NAME, getPlatform(json));
            }
            Configuration.browser = browserName.toLowerCase();
            Configuration.automationType = Configuration.DESKTOP_PLATFORM;
        } catch (JSONException e) {
            throw new TestUIException("There is no node available for the specified parameters " +
                    "\n"  + setJsonBody());
        }
    }
}

