package testUI;

import io.qameta.allure.Allure;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.json.JSONObject;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static testUI.UIUtils.putLog;

public class NetworkCalls {

    private List<List<JSONObject>> calls;
    private List<JSONObject> filteredCalls;
    private List<Map<String, String>> callHar;
    private boolean severalFilters;

    private NetworkCalls(List<List<JSONObject>> calls, List<JSONObject> filteredCalls, List<Map<String, String>> callHar, boolean severalFilters) {
        this.calls = calls;
        this.filteredCalls = filteredCalls;
        this.callHar = callHar;
        this.severalFilters = severalFilters;
    }

    private static BrowserMobProxy proxy;

    public static BrowserMobProxy getProxy() {
        return proxy;
    }

    public static void setNetworkCalls() {
        if (Configuration.logNetworkCalls) {
            if (Configuration.remote == null || Configuration.remote.isEmpty()) {
                if (proxy == null || !proxy.isStarted()) {
                    proxy = new BrowserMobProxyServer();
                    // start the proxy
                    proxy.start(0);
                    // get the Selenium proxy object
                    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
                    seleniumProxy.setHttpProxy("localhost:" + getProxy().getPort());
                    seleniumProxy.setSslProxy("localhost:" + getProxy().getPort());
                    Configuration.selenideBrowserCapabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
                    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
                    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
                    // create a new HAR with the label "Proxy"
                    proxy.newHar("Proxy");
                }
            }
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
            Configuration.selenideBrowserCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        }
    }

    public static NetworkCalls getNetworkCalls() {
        List<List<JSONObject>> calls = new ArrayList<>();
        List<Map<String, String>> callHar = new ArrayList<>();
        if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
            for (LogEntry list : getWebDriver().manage().logs().get(LogType.PERFORMANCE).getAll()) {
                JSONObject obj = new JSONObject(list.getMessage());
                JSONObject requests = obj.getJSONObject("message").getJSONObject("params");
                if (requests.has("requestId")) {
                    String requestID = requests.getString("requestId");
                    List<JSONObject> list1 = new ArrayList<>();
                    list1.add(requests);
                    if (calls.toString().contains(requestID)) {
                        int i = 0;
                        for (List<JSONObject> call : calls) {
                            if (call.toString().contains(requestID)) {
                                list1.addAll(call);
                                calls.add(i, list1);
                                break;
                            }
                            i++;
                        }
                    } else {
                        calls.add(list1);
                    }
                }
            }
        } else {
            Har har = getProxy().getHar();
            for (HarEntry entry : har.getLog().getEntries()) {
                Map<String, String> call = new HashMap<>();
                call.put("URL", entry.getRequest().getUrl());
                call.put("Status", String.valueOf(entry.getResponse().getStatus()));
                if (entry.getResponse().getStatus() >= 300) {
                    call.put("Response: ", String.valueOf(entry.getResponse().getContent().getText()));
                }
                callHar.add(call);
            }
        }
        return new NetworkCalls(calls, new ArrayList<>(), callHar, false);
    }

    public static NetworkCalls getLastNetworkCalls(int lastX) {
        List<List<JSONObject>> calls = new ArrayList<>();
        List<Map<String, String>> callHar = new ArrayList<>();
        if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
            List<LogEntry> list = getWebDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
            if (list.size() < lastX) {
                lastX = list.size();
            }
            for (int j = list.size() - lastX; j < list.size(); j++) {
                LogEntry logEntry = list.get(j);
                JSONObject obj = new JSONObject(logEntry.getMessage());
                JSONObject requests = obj.getJSONObject("message").getJSONObject("params");
                if (requests.has("requestId")) {
                    String requestID = requests.getString("requestId");
                    List<JSONObject> list1 = new ArrayList<>();
                    list1.add(requests);
                    if (calls.toString().contains(requestID)) {
                        int i = 0;
                        for (List<JSONObject> call : calls) {
                            if (call.toString().contains(requestID)) {
                                list1.addAll(call);
                                calls.add(i, list1);
                                break;
                            }
                            i++;
                        }
                    } else {
                        calls.add(list1);
                    }
                }
            }
        } else {
            Har har = getProxy().getHar();
            for (HarEntry entry : har.getLog().getEntries()) {
                Map<String, String> call = new HashMap<>();
                call.put("URL", entry.getRequest().getUrl());
                call.put("Status", String.valueOf(entry.getResponse().getStatus()));
                if (entry.getResponse().getStatus() >= 300) {
                    call.put("Response: ", String.valueOf(entry.getResponse().getContent().getText()));
                }
                callHar.add(call);
            }
        }
        return new NetworkCalls(calls, new ArrayList<>(), callHar, false);
    }

    public NetworkCalls filterByUrl(String url) {
        List<String> requestIDs = new ArrayList<>();
        List<JSONObject> calls = new ArrayList<>();
        if (!this.severalFilters) {
            if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
                for (List<JSONObject> call : this.calls) {
                    for (JSONObject singleCall : call) {
                        boolean repeatedCall = !calls.toString().contains(singleCall.toString());
                        if (singleCall.has("request") &&
                                singleCall.getJSONObject("request").has("url") &&
                                singleCall.getJSONObject("request").getString("url").contains(url)) {
                            if (repeatedCall) {
                                calls.add(singleCall);
                            }
                            if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                                requestIDs.add(singleCall.getString("requestId"));
                            }
                        } else if (singleCall.has("response") &&
                                singleCall.getJSONObject("response").has("url") &&
                                singleCall.getJSONObject("response").getString("url").contains(url)) {
                            if (repeatedCall) {
                                calls.add(singleCall);
                            }
                            if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                                requestIDs.add(singleCall.getString("requestId"));
                            }
                        }

                    }
                }
            } else {
                for (Map<String ,String> call : this.callHar) {
                    JSONObject jsonObject = new JSONObject();
                    if (call.get("URL").contains(url)) {
                        jsonObject.put("URL",call.get("URL"));
                        jsonObject.put("statusCode", call.get("Status"));
                        calls.add(jsonObject);
                    }
                }
            }
        } else {
            if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
                for (JSONObject singleCall : this.filteredCalls) {
                    if (singleCall.has("request") &&
                            singleCall.getJSONObject("request").has("url") &&
                            singleCall.getJSONObject("request").getString("url").contains(url)) {
                        if (!calls.toString().contains(singleCall.toString())) {
                            calls.add(singleCall);
                        }
                        if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                            requestIDs.add(singleCall.getString("requestId"));
                        }
                    } else if (singleCall.has("response") &&
                            singleCall.getJSONObject("response").has("url") &&
                            singleCall.getJSONObject("response").getString("url").contains(url)) {
                        if (!calls.toString().contains(singleCall.toString())) {
                            calls.add(singleCall);
                        }
                        if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                            requestIDs.add(singleCall.getString("requestId"));
                        }
                    }
                }
            } else {
                for (JSONObject jsonObject : this.filteredCalls) {
                    JSONObject jsonObject2 = new JSONObject();
                    if (jsonObject.get("URL").toString().contains(url)) {
                        jsonObject2.put("URL", jsonObject.get("URL"));
                        jsonObject2.put("statusCode", jsonObject.get("statusCode"));
                        calls.add(jsonObject2);
                    }
                }
            }
        }
        return new NetworkCalls(this.calls, calls, this.callHar,this.severalFilters);
    }

    public NetworkCalls filterByExactUrl(String url) {
        List<String> requestIDs = new ArrayList<>();
        List<JSONObject> calls = new ArrayList<>();
        if (!this.severalFilters) {
            if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
                for (List<JSONObject> call : this.calls) {
                    for (JSONObject singleCall : call) {
                        boolean repeatedCall = !calls.toString().contains(singleCall.toString());
                        if (singleCall.has("request") &&
                                singleCall.getJSONObject("request").has("url") &&
                                singleCall.getJSONObject("request").getString("url").equals(url)) {
                            if (repeatedCall) {
                                calls.add(singleCall);
                            }
                            if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                                requestIDs.add(singleCall.getString("requestId"));
                            }
                        } else if (singleCall.has("response") &&
                                singleCall.getJSONObject("response").has("url") &&
                                singleCall.getJSONObject("response").getString("url").equals(url)) {
                            if (repeatedCall) {
                                calls.add(singleCall);
                            }
                            if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                                requestIDs.add(singleCall.getString("requestId"));
                            }
                        }
                    }
                }
            } else {
                for (Map<String, String> call : this.callHar) {
                    JSONObject jsonObject = new JSONObject();
                    if (call.get("URL").equals(url)) {
                        jsonObject.put("URL", call.get("URL"));
                        jsonObject.put("statusCode", call.get("Status"));
                        calls.add(jsonObject);
                    }
                }
            }
        } else {
            if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
                for (JSONObject singleCall : this.filteredCalls) {
                    boolean repeatedCall = !calls.toString().contains(singleCall.toString());
                    if (singleCall.has("request") &&
                            singleCall.getJSONObject("request").has("url") &&
                            singleCall.getJSONObject("request").getString("url").equals(url)) {
                        if (repeatedCall) {
                            calls.add(singleCall);
                        }
                        if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                            requestIDs.add(singleCall.getString("requestId"));
                        }
                    } else if (singleCall.has("response") &&
                            singleCall.getJSONObject("response").has("url") &&
                            singleCall.getJSONObject("response").getString("url").equals(url)) {
                        if (repeatedCall) {
                            calls.add(singleCall);
                        }
                        if (!requestIDs.toString().contains(singleCall.getString("requestId"))) {
                            requestIDs.add(singleCall.getString("requestId"));
                        }
                    }
                }
            } else {
                for (JSONObject jsonObject : this.filteredCalls) {
                    JSONObject jsonObject2 = new JSONObject();
                    if (jsonObject.get("URL").toString().equals(url)) {
                        jsonObject2.put("URL", jsonObject.get("URL"));
                        jsonObject2.put("statusCode", jsonObject.get("statusCode"));
                        calls.add(jsonObject2);
                    }
                }
            }
        }
        return new NetworkCalls(this.calls, calls, this.callHar,this.severalFilters);
    }


    public NetworkCalls and() {
        return new NetworkCalls(this.calls, this.filteredCalls, this.callHar, true);
    }

    public NetworkCalls or() {
        return new NetworkCalls(this.calls, this.filteredCalls, this.callHar, false);
    }

    public NetworkCalls assertStatusCode(int statusCode) {
        if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
            for (JSONObject responses : this.filteredCalls) {
                if (responses.has("response")) {
                    if (responses.getJSONObject("response").getInt("status") != statusCode
                            && responses.getJSONObject("response").getInt("status") != 0) {
                        throw new Error("Status code should be " + statusCode + " but was "
                                + responses.getJSONObject("response").getInt("status") + "\n Response: \n" +
                                responses);
                    }
                }
            }
        } else {
            for (JSONObject responses : this.filteredCalls) {
                if (responses.getInt("statusCode") != statusCode
                        && responses.getInt("statusCode") != 0) {
                    throw new Error("Status code should be " + statusCode + " but was "
                            + responses.getInt("statusCode") + "\n Response: \n" +
                            responses);
                }
            }
        }
        return new NetworkCalls(this.calls, this.filteredCalls, this.callHar, this.severalFilters);
    }

    public NetworkCalls assertStatusCode(int statusCode, int statusCode2) {
        if (Configuration.remote != null && !Configuration.remote.isEmpty()) {
            for (JSONObject responses : this.filteredCalls) {
                if (responses.has("response")) {
                    if ((responses.getJSONObject("response").getInt("status") < statusCode
                            || responses.getJSONObject("response").getInt("status") > statusCode2)
                            && responses.getJSONObject("response").getInt("status") != 0) {
                        throw new Error("Status code should be between " + statusCode + " and " + statusCode2 + " but was "
                                + responses.getJSONObject("response").getInt("status") + "\n Response: \n" +
                                responses);
                    }
                }
            }
        } else {
            for (JSONObject responses : this.filteredCalls) {
                if ((responses.getInt("statusCode") < statusCode ||
                        responses.getInt("statusCode") > statusCode2)
                                && responses.getInt("statusCode") != 0) {
                    throw new Error("Status code should be between " + statusCode + " and " + statusCode2 + " but was "
                            + responses.getInt("statusCode") + "\n Response: \n" +
                            responses);
                }
            }
        }
        return new NetworkCalls(this.calls, this.filteredCalls, this.callHar, this.severalFilters);
    }

    public List<JSONObject> extractFiltered() {
        return this.filteredCalls;
    }

    public NetworkCalls logFilteredCalls() {
        if (Configuration.useAllure) {
            Allure.addAttachment("Filtered Calls", this.filteredCalls.toString());
        }
        putLog(this.filteredCalls.toString());
        return new NetworkCalls(this.calls, this.filteredCalls, this.callHar, this.severalFilters);
    }

    public NetworkCalls logAllCalls() {
        if (!this.calls.isEmpty()) {
            if (Configuration.useAllure) {
                Allure.addAttachment("Calls", this.calls.toString());
            }
            putLog(this.calls.toString());
        } else {
            if (Configuration.useAllure) {
                Allure.addAttachment("Calls", this.callHar.toString());
            }
            putLog(this.callHar.toString());
        }
        return new NetworkCalls(this.calls, this.filteredCalls, this.callHar, this.severalFilters);
    }
}
