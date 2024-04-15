![TestUI](docs/logo.jpg)

[![](https://jitpack.io/v/testdevlab/TestUI.svg)](https://jitpack.io/#testdevlab/TestUI)
# TestUI

The idea of “TestUI” was born from a desire of having a simpler way to automate tests for mobile.
A framework that would allow you to concentrate merely on writing test cases and not coding for
hours until finally discovering why your Appium server or your device did not connect to your
scenarios or why the functions you wrote do not work as they should.

At first, you will probably be happy to have a faster way of creating the Appium server and driver,
but the framework does much more than that! It provides a fluent API which makes your code readable,
simple and efficient. This framework was inspired by a similar one for desktop browser automation (
Selenide), so for those who have worked with it will find the methods quite similar. Now let's check
how it looks...

## Table of contents:

* [Installation](#installation)
    * [Maven](#maven)
    * [Gradle (Kotlin)](#gradle-kotlin)
    * [Gradle (Groovy)](#gradle-groovy)
* [Start using TestUI](#start-using-testui)
    * [Android](#android)
    * [iOS](#ios)
* [Elements](#elements)
    * [Element Action Methods](#element-action-methods)
    * [Element Assertion Methods](#element-assertion-methods)
* [Collections](#collections)
    * [Collection Action Methods](#collection-action-methods)
    * [Collection Assertion Methods](#collection-assertion-methods)
* [Browser Testing](#browser-testing)
* [Driver and Server](#driver-and-server)
* [Configuration Settings](#configuration-settings)
* [Quick Start](#quick-start)
* [Code Examples](#code-examples)
* [Project Contributors](#project-contributors)
    * [Reporting Issues](#reporting-issues)
    * [Contributing Code](#contributing-code)

## Installation

### Maven

Add Jitpack as a repository in your `pom.xml` file:

```xml

<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Then add TestUI dependency

```xml

<dependencies>
  <dependency>
    <groupId>com.github.testdevlab</groupId>
    <artifactId>TestUI</artifactId>
    <version>2.0.0</version>
  </dependency>
</dependencies>
```

### Gradle (Kotlin)

Add the following lines to `build.gradle.kts`

```kts
repositories {
    maven { url = "https://jitpack.io" }
}

dependencies {
    implementation("com.github.testdevlab:TestUI:2.0.0")
}
```

Add the following lines to `build.gradle`

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.testdevlab:TestUI:2.0.0'
}
```

## Start using TestUI

### Android

Here you see an example of creating an Appium server and Appium driver connecting your device or
starting an emulator present in your computer:

```java
public class AndroidTest {
    public void option1() {
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        Configuration.appPackage = "appPackage";
        Configuration.appActivity = "appActivity";

        open();
    }

    // or

    public void option2() {
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        Configuration.androidAppPath = "relative or absolute path";

        open();
    }
}
```

That’s all! After your tests end, the driver will quit and the server will stop without writing any
line of code. But there is a question that will rise in the minds of the most exigent testers: can
it handle forks or executing different tests at the same time? Yes, it will automatically open as
many Appium servers as forks you have chosen, and it will use a different device per server. In case
you have devices connected to your machine, it will take those first, but if there are no enough
connected devices to choose, then it will search for emulators and will start them and finally
create linked drivers.

After this useful features you could just choose to work with Appium as it is documented in its
official site;
just call the getDriver() method and you will be able to manage all the features included by Appium
java. But of
course this is not yet the same as the browser counterpart Selenide. Now we want Appium elements and
Appium collections
that work as the Selenide ones. Here we have an example of an Appium element. With this element you
will have a selection of the most useful functions to be used in your tests.

```java
public class Test {
    UIElement element = E(byId("your.Id"));

    public void test() {
        element.click();
        element.sendKeys("some text");
        element.getValue();

        // you can also chain methods
        element.click().and().sendKeys("some text").and().getValue();
    }
}
```

We provide assertions as well:

```java
public class Test {
    public void test() {
        element.shouldBe().visible();
        element.shouldHave().exactText("some text");

        // or you can chain methods like so
        element.shouldBe().visible().and().shouldHave().exactText("some text");
    }
}
```

Moreover, the framework will always wait for 5 seconds before considering that one element is not
clickable, enabled or visible (This timeout can be changed by
setting `Configuration.timeout = timeInSeconds` that you wish as default). In case it has gone
through those 5 seconds and still not fitting the imposed conditions it will rise an error that
informs you about which element made your test fail. Nonetheless sometimes the app under test is not
as fast as we would want it to be, and in these cases 5 seconds is asking too much... then you can
choose to wait more without changing the global configuration, just like this:

```java
public class Test {
    public void test() {
        element.waitFor(10).untilIsVisible();
        element.waitFor(10).untilIsEnabled();
        element.waitFor(10).untilHasText("Some text");
    }
}
```

Even though now the framework seems to be quite appealing, it has two more features that will
satisfy
testers with browser versions of their apps or mobile apps that also work in computer browsers, for
example
responsive websites. In this case you can open a chrome browser in your device substituting the
openApp method
aforementioned by:

```java
public class Test {
    public void test() {
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        open("url");
    }
}
```

The rest will work the same as with the app method. But wait... because there is one last thing to
mention:
it integrates with Selenide! This means that you can choose to use a laptop browser by changing one
single boolean variable (`Configuration.useDevice`) to false, and you will be able to use the same
Appium elements and collections in your laptop browser tests, or just write new ones with the same
syntax as the ones we have used for Appium. This is quite useful since some elements are common in
both screen sizes, and therefore you can increase the reusable code.

### IOS

The procedure for opening an iOS app is quite similar to the Android counterpart, the only
difference
comes with the testUI driver specifications, in this case you have to choose a device name, its
version
and also the path of the iOS' .app for testing:

```java
public class Test {
    public void test() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.iOSVersion = "12.2";
        Configuration.AppPath = "/path/to/testapp.app";
        Configuration.deviceName = "iPhone 6";

        open();
    }
}
```

In case you want to test the Safari browser within the device, you can avoid writing the AppPath and
instead
of open app method use the following method for browser:

```java
public class Test {
    public void test() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.iOSVersion = "12.2";
        Configuration.deviceName = "iPhone 6";

        open("http://example.com");
    }
}
```

## Changing application context
In certain scenarios, applications may switch contexts, which can be necessary for automation purposes. 
Utilize the following function to switch the application context:

```java
changeAppContext(""); // Pass the application context to switch to
```

## Elements

Same element can be defined for all the platforms, i.e. same element definition
for iOS, android and computer Browser:

```java
public class Test {
    UIElement element = E(byId("your.Id"));
}
```

But in most cases the elements are different for each platform. For those cases you can specify
elements for specific platforms this way:

```java
public class Test {
    UIElement element = E(byId("your.Id"))
        .setSelenideElement(byXpath("//some"))
        .setiOSElement(byCssSelector(".something"));
}
```

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
Collection definition:
<pre>
UICollection collection = EE(byId(“your.Id”));
</pre>

or:
<pre>
UICollection collection = EE(byId(“your.Id”), byId(“your.Id2”), ...);
</pre>

You can also use a definition per platform
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
collection.findByEnabled()
</pre>

<pre>
collection.findByExist()
</pre>

<pre>
collection.waitUntilAllVisible(timeSeconds) // Only makes sense for collection definitions like EE(E(...), E(...), ...)
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

With testUI you can test browsers in mobile devices and computers just by changing the boolean value
of a single variable:

```java
public class Test {
    public void test() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        Configuration.automationType = Configuration.DESKTOP_PLATFORM;
    }
}
```

And then using:

<pre>
open("url");
</pre>

With this you will be able to open a web page in a computer device and navigate to the indicated
url. By default it uses chrome but you can change that by setting:

<pre>
Configuration.browser = "firefox", "safari", "edge", "ie"...
</pre>

You just have to make sure your computer has these browsers installed.

In case you want to use a remote browser such as selenium grid, you just have to specify the url to
this grid as follows:

<pre>
Configuration.remote = "http://grid:4444/hub/wd"
</pre>

testUI uses Selenide for computer browser automation, so you can check their methods in the official
page
[Here](https://selenide.org/). In case you don't need mobile automation, you should use Selenide
framwork instead, since
it is a less heavy framework and the methods are the native ones.

For mobile browsers, android only has chrome for automation and iOS devices only has safari. So in
these cases there is no point on specifying which browser to use, they will always take one of them.

Then for Android you will call:

<pre>
Configuration.automationType = Configuration.ANDROID_PLATFORM;
open("url");
</pre>

And for iOS:

<pre>
Configuration.automationType = Configuration.IOS_PLATFORM;
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

There is a Configuration.remote parameter parameter that works to open browser in a remote grid (
such as selenium
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
between different options for your automation. Here we will give a detailed description for all of
them.

* *`Configuration.appPackage` is a string variable that defines the appPackage of the Android
  application
  you want to test, and must be defined before calling `openApp()` method.
* *`Configuration.appActivity` same rules as the appPackage, in this case it's the main activity of
  the
  android application.
* `Configuration.browser (= "chrome")` (String) you can choose the browser you want to test.
  However, in mobile version, android just admits chrome and iOS only safari, so it would be just
  optional for desktop browser testing if you want to test other than chrome (this the default
  browser).
* `Configuration.baseUrl` (String) it is possible to set the base url for browser automation.
* `Configuration.timeout (= 5)` (integer) you can set the time out (in seconds) in which an element
  is considered to not be present in the current screen. By default is 5 seconds.
* `Configuration.automationType (= DESKTOP_PLATFORM)` (String) There are three different types,
  ANDROID_PLATFORM, IOS_PLATFORM, DESKTOP_PLATFORM(default)
* `Configuration.startMaximized (= false)` (boolean) when set to true the computer browser will
  start maximized.
* `Configuration.headless (= false)` (boolean) in computer browser testing you can set this variable
  to true so it will start a headless session.
* `Configuration.remote` (String) it is possible to set this variable to a specific url where you
  have running an Selenium grid which will control your bowsers.
* `Configuration.appiumUrl` (String) it is possible to set this variable to a specific url where you
  have running an Appium server which will control your devices.
  In case of computer browser testing, it will point to a selenium grid or where the browsers are
  running remotely.
* `Configuration.useEmulators (= true)` (boolean) when set to false, it uses only real devices.
* `Configuration.driver (= 1)` (integer) with this variable you can switch between the different
  mobile drivers created under the same
  test scenario, being the first one the 1 and next ones 2, 3...
* `Configuration.useAllure (= true)` (boolean) in case you dont want to use allure reporter it is
  possible to disable it by setting this variable to false.
* *`Configuration.iOSVersion` (String) iOS version of the device in use. must be set before calling
  openiOSApp/Browser().
* *`Configuration.iOSAppPath` (String) path to the iOS compiled app for testing.
* *`Configuration.iOSDeviceName` (String) iOS device name in use.
* `Configuration.androidDeviceName` (String) it is possible to specify android device before opening
  app or browser with this variable.
* `Configuration.emulatorName` (String) it is possible to specify android emulator before opening
  app or browser with this variable.
* `Configuration.androidAppPath` (String) android relative app path to the apk (if it's in root of
  project directory => "app.apk"). It will reinstall the app in case of being specified..
* `Configuration.androidVersion` (String) you can set manually the android version of your device.
  However this is not necessary since the version is
  taken automatically by adb commands testUI executes previous to generate the driver
* `Configuration.chromeDriverPath` (String) relative path of your chromedriver (if it's in the root
  of your directory => "chromedriver"). Only specify if current is not working.
* `Configuration.softAsserts` (Boolean) if set to True, then any TestUI error will not stop the
  execution, and in the end of the case (or any given part of the test) it is possible to
  call `raiseSoftAsserts();` to raise all errors that have occurred through the test.

`* this parameters are mandatory before calling open() or open("url")`

## Quick Start

Basic requirements and steps to start using TestUI

### Setup

The framework is based on Java, therefore you will need to **install Java 8 or above**.

Then the setup depends on what platform or platforms you want to test on.

* Android - Appium setup for Android (Node.js, Android SDK, JDK, Appium)
* iOS - Appium setup for iOS (Node.js, Xcode, Carthage, Appium)
* Desktop browser - Selenide does everything for you :) No additional steps

For Appium setup check out their [page](http://appium.io/) and you can see if the setup was
successful by using appium-doctor.

### Try out with existing tests

* Download this repository
* Go to repo in terminal: `cd path/to/repo/`
* Execute the test:
*
    * Android: connect device or start emulator and run `mvn clean -Dtest=TestAndroid test`
*
    * iOS: connect device or start simulator and run `mvn clean -Dtest=TestIOS test`
*
    * Desktop Browser: have Chrome installed and run `mvn clean -Dtest=TestBrowser test`

## Code Examples

Android app as a JUnit test case:

<pre>
    // You need to have the app 1188.apk in project root folder!
    @Test
    public void testAndroidApp() {
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
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
        Configuration.automationType = Configuration.ANDROID_PLATFORM;
        open("https://www.google.com");
        E(byXpath("//input[@name='q']")).given().waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        E(byXpath("//button[@class='Tg7LZd']")).given().waitFor(10).untilIsVisible().then().click();
    }
</pre>

Desktop browser as JUnit test case:

<pre>
    @Test
    public void testAndroidBrowser() {
        Configuration.automationType = Configuration.DESKTOP_PLATFORM;
        open("https://www.google.com");
        E(byXpath("//input[@name='q']")).given().waitFor(5).untilIsVisible().then().sendKeys("TestUI");
        E(byXpath("//button[@class='Tg7LZd']")).given().waitFor(10).untilIsVisible().then().click();
    }
</pre>

IOS App as JUnit test case:

<pre>
    @Test
    public void testIOSApp() {
        Configuration.automationType = Configuration.IOS_PLATFORM;
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
        Configuration.automationType = Configuration.IOS_PLATFORM;
        open("https://www.facebook.com");
        E(byXpath("//input[@name='email']")).getSafariFacebookEmailDiv().click();
    }
</pre>

## Project Contributors

We are glad to hear your feedback or review your suggestions for TestUI framework!

### Reporting Issues

In case there is some bug, you can report that as an Issue or send an E-mail to
testui@testdevlab.com .
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

### Contributing Code

COMING SOON
