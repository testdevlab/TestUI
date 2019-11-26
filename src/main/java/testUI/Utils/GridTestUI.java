package testUI.Utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import testUI.Configuration;

import java.io.IOException;

import static testUI.UIUtils.putAllureParameter;

public class GridTestUI {
    // one instance, reuse
    private static String appiumURL = "";
    private static String deviceName = "";
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAppiumAndroidConfiguration(String CliServerURL) {
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
                deviceName = json.getString("udid");
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
                Configuration.androidDeviceName = deviceName;
                Configuration.deviceTests = true;
                Configuration.iOSTesting = false;
            } catch (IOException e) {
                throw new TestUIException("Could not retrieve remote configuration with testUI " +
                        "server: \n" + e.toString());
            } finally {
                close();
            }
            attachShutDownHookSReleaseDevice(CliServerURL, deviceName);
        }
    }

    public void setAppiumiOSConfiguration(String CliServerURL) {
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
                deviceName = json.getString("udid");
                Configuration.iOSDeviceName = json.getString("deviceName");
                Configuration.iOSVersion = json.getString("version");
                Configuration.wdaPort = json.getInt("port") + 100;
                Configuration.appiumUrl = appiumURL;
                Configuration.UDID = deviceName;
                Configuration.deviceTests = true;
                Configuration.iOSTesting = true;
            } catch (IOException e) {
                throw new TestUIException("Could not retrieve remote configuration with testUI " +
                        "server: \n" + e.toString());
            } finally {
                close();
            }
            attachShutDownHookSReleaseDevice(CliServerURL, deviceName);
        }
    }

    private void setAppiumRelease(String CliServerURL, String deviceName) {
        HttpPost post = new HttpPost(CliServerURL + "/release");
        String postBody = "{\"appium\":{\"udid\": \"" + deviceName + "\"}}";
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

    private void attachShutDownHookSReleaseDevice(
            String url, String deviceName) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> setAppiumRelease(url, deviceName))
        );
    }
}

