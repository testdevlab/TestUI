package stepDefinitions;

import testUI.Configuration;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.qameta.allure.Allure;

import static testUI.UIOpen.open;
import static testUI.UIOpen.openNew;
import static testUI.TestUIServer.*;
import static testUI.TestUIDriver.*;
import static testUI.UIUtils.clearBrowserData;

public class Hooks {

    @Before("@testApp") // ANDROID APP
    public void beforeApp() {
//        Configuration.appActivity = "lv.lattelecombpo.yellowpages.MainActivity";
//        Configuration.appPackage = "lv.lattelecombpo.yellowpages";
        Configuration.androidAppPath = "1188.apk";
        //Configuration.androidDeviceName = "ce0617165c13ab2e027e";
        open();
    }

    @Before("@testBrowser") // ANDROID CHROME BROWSER
    public void beforeBrowser() {
        //Configuration.chromeDriverPath = "chromedriver";
        //Configuration.appiumUrl = "http://127.0.0.1:4724/wd/hub"; // Custom appium url
        //Configuration.emulatorName = "Nexus_5X_API_26";
        open("https://www.google.com");
    }

    @Before("@testLaptopBrowser") // BROWSER, YOU CAN CHOOSE BROWSER WITH Configuration.browser VARIABLE
    public void beforeLaptopBrowser() {
        Configuration.deviceTests = false;
        Configuration.startMaximized = true;
        open("https://www.google.com");
    }

    @Before("@testLaptopAndMobile") // BROWSER LAPTOP AND ANDROID BROWSER
    public void beforeLaptopBrowserAndMobile() {
        open("https://www.google.com");
        Configuration.deviceTests = false;
        openNew("https://www.google.com");
    }

    @Before("@testLaptopAndMobileFail") // BROWSER LAPTOP AND ANDROID BROWSER
    public void beforeLaptopBrowserAndMobileFail() {
        open("https://www.google.com");
        Configuration.deviceTests = false;
        openNew("https://www.google.com");
    }

    @Before("@IOS")
    public void beforeIOSApp() {
        Configuration.iOSTesting = true;
        Configuration.iOSVersion = "12.2";
        Configuration.iOSAppPath = "/Users/alvarolasernalopez/Documents/Automation/testapp/build/Release-iphonesimulator/testapp.app";
        Configuration.iOSDeviceName = "iPhone 6";
        Configuration.updatedWDABundleId = "";
        Configuration.UDID = "";
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("Something", "something");
//        Configuration.addMobileDesiredCapabilities = capabilities;
        open();
    }

    @Before("@IOSBrowser")
    public void beforeIOSBrowser() {
        Configuration.iOSTesting = true;
        open("https://www.facebook.com");
    }

    @Before("@IOSBrowserFail")
    public void beforeIOSBrowserFail() {
        Configuration.iOSTesting = true;
        open("https://www.google.com");
    }

    @After(order = 0) // saveScreenshot IS A TESTUI METHOD THAT RETURNS SCREENSHOT IN BYTE TYPE
    public void takeScreenshotAfterFailure(Scenario scenario){
        if(scenario.isFailed()){
            byte[] screenshot = takeScreenshot();
            Allure.getLifecycle().addAttachment("Screenshot", "image/png", "png", screenshot);
            scenario.embed(screenshot, "image/png");
        }
        clearBrowserData();
    }

    @After("@testLaptopAndMobile or @testLaptopBrowser or @testLaptopAndMobileFail")
    public void afterLaptopAndMobile(){
        Configuration.deviceTests = false;
        stop();
    }
}