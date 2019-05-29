
![TestUI](ReadmeResources/testui6.png)

<br/>
<br/>
<br/>

### 1. [Introduction](README.md/#introduction)
#### &nbsp; &nbsp; 1.1. [Quick Start](README.md/#quick-start)
#### &nbsp; &nbsp; 1.2. [Android](README.md/#android)
#### &nbsp; &nbsp; 1.3. [iOS](README.md/#ios)
### 2. [Elements](README.md/#elements)
#### &nbsp; &nbsp; 2.1. [Element Action Methods](README.md/#element-action-methods)
#### &nbsp; &nbsp; 2.2. [Element Assertion Methods](README.md/#element-assertions-methods)
### 3. [Collections](README.md/#collections)
#### &nbsp; &nbsp; 3.1. [Collection Action Methods](README.md/#collection-action-methods)
#### &nbsp; &nbsp; 3.2. [Collection Assertion Methods](README.md/#collection-assertion-methods)
### 4. [Browser Testing](README.md/#browser-testing)
### 5. [Driver and Server](README.md/#driver-and-server)
### 6. [Configuration Settings](README.md/#configuration-settings)
### 7. [Project Contributors](README.md/#project-contributors)
#### &nbsp; &nbsp; 7.1. [Reporting Issues](README.md/#reporting-issues)
### 8. [Code Examples](README.md/#code-examples)

<a name="Intro"></a>
## Introduction

The idea of “TestUI” was born from a desire of having a simpler way to automate tests for mobile.
A framework that would allow you to concentrate merely on writing test cases and not coding for hours until
finally discovering why your Appium server or your device did not connect to your scenarios or why the
functions you wrote does not work as they should.

At first you will probably be happy to have a faster way of creating the Appium server and driver, but the framework
does much more than that! It provides a fluent API which makes your code readable, simple and efficient. This framework was inspired by a similar one for desktop browser automation (Selenide), so for those who have worked with it will find the methods quite similar. Now let's check how it looks...

<a name="Android"></a>

### Quick Start

* #### Install Java 8 or above

The framework is based on Java, so you will need Java JDK to be able to work with it. 

* #### Install Maven

The framework uses maven to retrieve the necessary dependencies/libraries from maven central. In the future you will be able to use other
means like gradle, but for now this project is NOT uploaded into Maven Central. We will be working to make this possible as soon as possible.

For now, to use this framework you will have to download the code and and use the packages and classes located under main/java/testUI folder
(just copy this folder under your project and don't forget to add the basic dependencies as in the pom.xml).

* #### Installing Appium

*Installation via NPM*  
If you want to run Appium via an npm install first you will have to install Node.js and NPM (use nvm, n, or `brew install node` to install Node.js. 
Make sure you have not installed Node or Appium with sudo, otherwise you'll run into problems). 

The actual installation is as simple as:

<pre>
npm install -g appium
</pre>

* #### Installing Android Studio

You can go to the [official page](https://developer.android.com/studio) and download the latest version, then you will have to add
 <pre>
ANDROID_HOME=/home/user_directory/Android/Sdk
PATH=${PATH}:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
 </pre>

* #### Installing XCode (only mac)

This is only for iOS testing and Safari testing. You must have installed xcode to be able to run test in simulators and devices. 
To install it just go to App Store and search for it, download and follow instructions of the installation.

### Android

Here you see an example of creating an Appium server and Appium driver connecting your device or starting an emulator
present in your computer:

<pre>
Configuration.appPackage = "appPackage";
Configuration.appActivity = "appActivity";
open();

or 

Configuration.androidAppPath = "relative or absolute path";
open();
</pre>

That’s all! After your tests end, the driver will quit and the server will stop without writing any
line of code. But there is a question that will rise in the minds of the most exigent testers: can it
handle forks or executing different tests at the same time? Yes, it will automatically open as many
Appium servers as forks you have chosen, and it will use a different device per server. In case you
have devices connected to your machine, it will take those first, but if there are no enough connected
devices to choose, then it will search for emulators and will start them and finally create linked drivers.  

After this useful features you could just choose to work with Appium as it is documented in its official site;
just call the getDriver() method and you will be able to manage all the features included by Appium java. But of
course this is not yet the same as the browser counterpart Selenide. Now we want Appium elements and Appium collections
that work as the Selenide ones. Here we have an example of an Appium element:

<pre>
UIElement element = E(byId(“your.Id”));
</pre>

With this element you will have a selection of the most useful functions:

<pre>
element.click();  
element.sendKeys(“some text”);   
element.getValue();    
...
</pre>

or

<pre>
element.click().and().sendKeys(“some text”).and().getValue().(...);
</pre>

We provide assertions as well:

<pre>
element.shouldBe().visible();    
element.shouldHave().exactText(“some text”)
</pre>
or

<pre>
element.shouldBe().visible().and().shouldHave().exactText(“some text”);
</pre>

Moreover, the framework will always wait for 5 seconds before considering that one element is not clickable, enabled or visible (This timeout can be changed by setting `Configuration.timeout = timeInSeconds` that you wish as default). In case it has gone through those 5 seconds and still not fitting the imposed conditions it will rise an error that informs you about which element made your test fail. Nonetheless sometimes the app under test is not as fast as we would want it to be, and in these cases 5 seconds is asking too much... then you can choose to wait more without changing the global configuration, just like this:

<pre>
element.waitFor(10).untilIsVisible();
element.waitFor(10).untilIsEnabled();
element.waitFor(10).untilHasText("Some text");
</pre>

Even though now the framework seems to be quite appealing, it has two more features that will satisfy
testers with browser versions of their apps or mobile apps that also work in computer browsers, for example
responsive websites. In this case you can open a chrome browser in your device substituting the openApp method
aforementioned by:

<pre>
open("url");
</pre>

The rest will work the same as with the app method. But wait... because there is one last thing to mention:
it integrates with Selenide! This means that you can choose to use a laptop browser by changing one single boolean variable (`Configuration.useDevice`) to false, and you will be able to use the same Appium elements and collections in your laptop browser tests, or just write new ones with the same syntax as the ones we have used for Appium. This is quite useful since some elements are common in both screen sizes, and therefore you can increase the reusable code.

### IOS

The procedure for opening an iOS app is quite similar to the Android counterpart, the only difference
comes with the testUI driver specifications, in this case you have to choose a device name, its version
and also the path of the iOS' .app for testing:

<pre>
 Configuration.iOSVersion = "12.2"
 Configuration.AppPath = "/path/to/testapp.app";    
 Configuration.deviceName = "iPhone 6";
 openIOSApp();
</pre>

 In case you want to test the Safari browser within the device, you can avoid writing the AppPath and instead
 of open app method use the following method for browser:

<pre>
 Configuration.iOSVersion = "12.2";   
 Configuration.deviceName = "iPhone 6";  
 openIOSBrowser();
</pre>

## Elements

Same element can be defined for all the platforms, i.e. same element definition
for iOS, android and computer Browser:
<pre>
UIElement element = E(byId(“your.Id”));
</pre>
But in most cases the elements are different for each platform. For those cases you can specify elements for specific platforms this way:

<pre>
UIElement element = E(byId(“your.Id”))  
                             .selenideElement(byXpath("//some""))  
                             .iOSElement(byCssSelector(".something""));  
</pre>

<a id="assertion2"></a>

### Element Action Methods

<pre>
element.given().when().then().and()
</pre>

<pre>
element.click()
</pre>

<pre>
element.sendKeys()
</pre>

<pre>
element.getText()
</pre>

<pre>
element.getValue()
</pre>

<pre>
element.getAttribute("attribute")
</pre>

<pre>
element.scrollIntoView()
</pre>

<pre>
element.swipe(element)
</pre>

<pre>
element.swipeLeft()
</pre>

<pre>
element.swipeRight()
</pre>

### Element Assertion methods

<pre>
element.(shouldBe()/shouldHave())    
                                (.not())
                                    .visible()
                                    .enabled()
                                    .Exists()
                                    .exactText("someText")/containText("")/containNoCaseSensitiveText("")
                                    .value("someValue")
                                    .attribute("attributeName").equalTo("value")
                                    .theAttribute("attribute")
</pre>

<pre>
element.waitFor(timeInteger)
                            .untilIs(Not)Visible()
                            .untilIs(Not)Enabled()
                            .until(Not)Exist()
                            .untilHas(Not)Text("someText")
                            .untilHas(Not)Value("someValue")
                            .untilHas(Not)Attribute("someAttribute")
</pre>

## Collections

<!-- copy pasted from elements? -->
Elements can be defined as a unique for all the different platforms, i.e. same element definition
for iOS, android and computer Browser:
<pre>
UICollection collection = EE(byId(“your.Id”));
</pre>

<!-- copy pasted from elements? -->
But in most cases the collections are different in each case which means you can use a definition per platform
like this:

<pre>
UICollection collection = EE(byId(“your.Id”))  
                                .selenideCollection(byXpath("//some""))  
                                .iOSCollection(byCssSelector(".something""));  
</pre>


### Collection Action Methods

<pre>
collection.get(index)
</pre>

<pre>
collection.first()
</pre>

<pre>
collection.findByVisible()
</pre>

<pre>
collection.findByVisible()
</pre>

<pre>
collection.findByEnabled()
</pre>

<pre>
collection.findByExist()
</pre>

<pre>
collection.findByText("some text")
</pre>

### Collection Assertion methods

<!-- Why text in code format? -->
<!-- Also difficult to understand this sentence -->
<pre>
Once you specify which element form the collection you have the same methods than in the
element section above
</pre>


## Browser Testing

With testUI you can test browsers in mobile devices and computers just by changing the boolean value of a single variable:

<pre>
Configuration.deviceTests = false;
</pre>

And then using:

<pre>
open("url");
</pre>

With this you will be able to open a web page in a computer device and navigate to the indicated url. By default it uses chrome but you can change that by setting:

<pre>
Configuration.browser = "firefox", "safari", "edge", "ie"...
</pre>

You just have to make sure your computer has these browsers installed.

In case you want to use a remote browser such as selenium grid, you just have to specify the url to this grid as follows:

<pre>
Configuration.remote = "http://grid:4444/hub/wd"
</pre>

testUI uses Selenide for computer browser automation, so you can check their methods in the official page 
[Here](https://selenide.org/). In case you don't need mobile automation, you should use Selenide framwork instead, since
it is a less heavy framework and the methods are the native ones.

For mobile browsers, android only has chrome for automation and iOS devices only has safari. So in these cases there is no point on specifying which browser to use, they will always take one of them.

Then for Android you will call:

<pre>
open("url");
</pre>

And for iOS:

<pre>
Configuration.iOSTesting = true;
Configuration.iOSversion = "12.2";
Configuration.iOSDevice = "iPhone 6";
open("url");
</pre>


## Driver and Server

If you want to create the Appium driver the usual way, you can do so and set it in the testUI
static method:

<pre>
setDriver(YourAppiumDriver);
</pre>

This way you will still be able to use all the element and collection methods mentioned before.

You can also start your own Appium server and then use it like this:

<pre>
Configuration.appiumUrl = "AppiumURL"; // for example Configuration.appiumUrl = "https://localhost:4723/wd/hub"
open()/open("url")
</pre>

There is a Configuration.remote parameter parameter that works to open browser in a remote grid (such as selenium
grid or selenoid)

You can specify your own desired capabilities and start driver as:

<pre>
setDesiredCapabilities(desiredCapabilities);
openApp();
</pre>

<!-- Why is this not in Android section? -->
It is possible to add the android device connected of your choice by setting this variables:

<pre>
Configuration.androidDeviceName = "deviceName";
Configuration.androidVersion = "version";
</pre>

or the emulator:

<pre>
Configuration.emulatorName = "emulatorName";
</pre>

## Configuration Settings

As mentioned in previous sections, there are some static variables that you can modify to choose
between different options for your automation. Here we will give a detailed description for all of them.

* *`Configuration.appPackage` is a string variable that defines the appPackage of the Android application
you want to test, and must be defined before calling `openApp()` method.
* *`Configuration.appActivity` same rules as the appPackage, in this case it's the main activity of the
android application.
* `Configuration.browser (= "chrome")` (String) you can choose the browser you want to test. However, in mobile version, android just admits chrome and iOS only safari, so it would be just optional for desktop browser testing if you want to test other than chrome (this the default browser).
* `Configuration.baseUrl` (String) it is possible to set the base url for browser automation.
* `Configuration.timeout (= 5)` (integer) you can set the time out (in seconds) in which an element is considered to not be present in the current screen. By default is 5 seconds.
* `Configuration.deviceTests (= true)` (boolean) when set to false the driver used or the driver that will be created will be a
selenide/selenium one, i.e. it will open or manage a computer browser instead of a mobile device.
* `Configuration.startMaximized (= false)` (boolean) when set to true the computer browser will start maximized.
* `Configuration.headless (= false)` (boolean) in computer browser testing you can set this variable to true so it will start a headless session.
* `Configuration.remote` (String) it is possible to set this variable to a specific url where you have running an Selenium grid which will control your bowsers.
* `Configuration.appiumUrl` (String) it is possible to set this variable to a specific url where you have running an Appium server which will control your devices.
In case of computer browser testing, it will point to a selenium grid or where the browsers are running remotely.
* `Configuration.useEmulators (= true)` (boolean) when set to false, it uses only real devices.
* `Configuration.driver (= 1)` (integer) with this variable you can switch between the different mobile drivers created under the same
test scenario, being the first one the 1 and next ones 2, 3...
* `Configuration.useAllure (= true)` (boolean) in case you dont want to use allure reporter it is possible to disable it by setting this variable to false.
* *`Configuration.iOSVersion` (String) iOS version of the device in use. must be set before calling openiOSApp/Browser().
* *`Configuration.iOSAppPath` (String) path to the iOS compiled app for testing.
* *`Configuration.iOSDeviceName` (String) iOS device name in use.
* `Configuration.androidDeviceName` (String) it is possible to specify android device before opening app or browser with this variable.
* `Configuration.emulatorName` (String) it is possible to specify android emulator before opening app or browser with this variable.
* `Configuration.androidAppPath` (String) android relative app path to the apk (if it's in root of project directory => "app.apk"). It will reinstall the app in case of being specified..
* `Configuration.androidVersion` (String) you can set manually the android version of your device. However this is not necessary since the version is
taken automatically by adb commands testUI executes previous to generate the driver
* `Configuration.chromeDriverPath` (String) relative path of your chromedriver (if it's in the root of your directory => "chromedriver"). Only specify if current is not working.


`* this parameters are mandatory before calling open() or open("url")`


## Project Contributors

First you will need to have installed Java 8, and Maven. Then install Android Studio and SDK (set up all the environment variables correctly).
Finally install appium. To be able to run test cases in android devices you must set the android device in debug mode.

For browser testing in mobile device be aware of the chrome version the device has installed, chrome versions < 61 could not work correctly.

To run test cases you must have connected at least one device or at least have one emulator installed.

In the test cases for iOS is using simulators created by XCode, exactly the 12.2 iOS version, which means you will have to install this version using
a mac computer (For iOS testing is mandatory run in a macOS machine)

### Reporting Issues

In case there is some bug, you can report that as an Issue or send an E-mail to alvaro.lasernalopez@testdevlab.com. 
The format for these Issues should be as follows:

##### TestUI Version: 
*(Version)*

##### Platform
*App Android/Browser chrome Android/App iOS/ Browser Safario iOS/ Computer Browser...*
(Mentioning which app you are testing could help)

##### Steps
*Given I set the configuration variables: this, this and this*  
*And I start an testUI driver with open() method*  
*Then I use this method*

##### Results
*The method doesnt do what is expected*

##### Conlose log

*Full stacktrace*

## Code Examples

Android app as a JUnit test case:

<pre>
    @Test
    public void testAndroidApp() {
        Configuration.androidAppPath = "1188.apk";
        open();
        Ex("//android.widget.Button[@text=\"Catering\"]").given().waitFor(10).untilIsVisible().then().click();
        EE(byId("lv.lattelecombpo.yellowpages:id/label")).get(1).then().waitFor(5).untilIsVisible().and().click();
        System.out.println(EE(byId("lv.lattelecombpo.yellowpages:id/label")).then().findByVisible().and().getText());
    }
</pre>

Android browser as JUnit test case:

<pre>
    @Test
    public void testAndroidBrowser() {
        open("https://www.google.com");
        E(byXpath("//input[@name='q']")).given().waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        E(byXpath("//button[@class='Tg7LZd']")).given().waitFor(10).untilIsVisible().then().click();
    }
</pre>

IOS App as JUnit test case:

<pre>
    @Test
    public void testIOSApp() {
        Configuration.iOSTesting = true;
        Configuration.iOSVersion = "12.2";
        Configuration.iOSAppPath = "testapp.app";
        Configuration.iOSDeviceName = "iPhone 6";
        open();
    }
</pre>

IOS browser as JUnit test case:

<pre>
    @Test
    public void testIOSBrowser() {
        Configuration.iOSTesting = true;
        open("https://www.facebook.com");
        E(byXpath("//input[@name='email']")).getSafariFacebookEmailDiv().click();
    }
</pre>

