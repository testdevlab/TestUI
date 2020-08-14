package testUI.elements;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import testUI.BrowserLogs;
import testUI.collections.UICollection;

public interface UIElement {

    UIElement navigateTo(String url);

    UIElement setElement(By element);

    UIElement setElement(UIElement element);

    UIElement setElement(String accesibilityId);

    UICollection setCollection(By element);

    UICollection setCollection(String accesibilityId);

    UIElement setSelenideElement(By selenideElement);

    UIElement setiOSElement(By iOSElement);

    UIElement setAndroidElement(By element);

    UIElement setAndroidElement(String element);

    UIElement setiOSElement(String iOSElementAccId);

    UIElement click();

    UIElement doubleClick();

    Dimension getSize();

    Point getLocation();

    WaitAsserts waitFor(int Seconds);

    String getText();

    UIElement sendKeys(CharSequence charSequence);

    UIElement setValueJs(String value);

    UIElement selectElementByValue(String... values);

    UIElement setValueJs(String value, boolean clickBeforeSetValue);

    UIElement executeJsOverElement(String JsScript);

    UIElement executeJs(String var1, Object... var2);

    SlideActions scrollTo();

    UIElement swipe(int XCoordinate, int YCoordinate);

    UIElement swipeRight();

    UIElement swipeLeft();

    MobileElement getMobileElement();

    UIElement clear();

    String getCssValue(String cssValue);

    String getValue();

    String getName();

    String getAttribute(String Attribute);

    boolean isVisible();

    boolean isEnabled();

    boolean Exists();

    Asserts shouldHave();

    Asserts shouldBe();

    Asserts should();

    UIElement saveScreenshot(String path);

    BrowserLogs getNetworkCalls();

    String getCurrentUrl();

    void getBrowserLogs();

    BrowserLogs getLastNetworkCalls(int LastX);

    UIElement and();

    UIElement and(String Description);

    UIElement given();

    UIElement given(String Description);

    UIElement then();

    UIElement then(String Description);

    UIElement when();

    UIElement when(String Description);

    SelenideElement getSelenideElement();

    long getLastCommandTime();
}
