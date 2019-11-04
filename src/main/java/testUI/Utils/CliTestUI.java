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

public class CliTestUI {
    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAppiumRemoteConfiguration(String CliServerURL) {
        HttpPost post = new HttpPost(CliServerURL + "/appium");
        Appium appium = new Appium();

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);
            JSONObject json = new JSONObject(EntityUtils.toString(response.getEntity()));
            appium.appiumURL = json.getString("url");
            appium.deviceName = json.getString("deviceName");
            Configuration.appiumUrl = appium.appiumURL;
            Configuration.androidDeviceName = appium.deviceName;
            Configuration.deviceTests = true;
        } catch (IOException e) {
            throw new TestUIException("Could not retrieve remote configuration with testUI " +
                    "server: \n" + e.toString());
        } finally {
            close();
        }
        attachShutDownHookSReleaseDevice(CliServerURL, appium.deviceName);
    }

    private void setAppiumRelease(String CliServerURL, String deviceName) {
        HttpPost post = new HttpPost(CliServerURL + "/release");
        String postBody = "{\"appium\":{\"deviceName\": \"" + deviceName + "\"}}";
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

class Appium {
    String appiumURL = "";
    String deviceName = "";
}

