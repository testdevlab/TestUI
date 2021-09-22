package testUI.elements;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.Quotes;
import testUI.Configuration;
import testUI.BrowserLogs;
import testUI.Utils.TestUIException;
import testUI.collections.UICollection;

import java.io.File;
import java.io.IOException;

import static testUI.TestUIDriver.*;
import static testUI.UIOpen.navigate;
import static testUI.Utils.AppiumHelps.*;
import static testUI.Utils.Logger.*;
import static testUI.Utils.Performance.setTime;
import static testUI.Utils.WaitUntil.waitUntilClickable;
import static testUI.Utils.WaitUntil.waitUntilVisible;
import static testUI.collections.TestUI.EE;

public class Element extends TestUI implements UIElement {
    protected By element;
    private By SelenideElement;
    private By iOSElement;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;
    private long lastCommandTime;

    public long getLastCommandTime() {
        return lastCommandTime;
    }

    public Element(By element,
                   By SelenideElement,
                   By iOSElement,
                   int index,
                   boolean collection,
                   String accesibilityId,
                   String accesibilityIdiOS) {
        this.element = element;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.index = index;
        this.collection = collection;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
    }

    public Element(By element,
                   By SelenideElement,
                   By iOSElement,
                   int index,
                   boolean collection,
                   String accesibilityId,
                   String accesibilityIdiOS,
                   long lastCommandTime) {
        this.element = element;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.index = index;
        this.collection = collection;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
        this.lastCommandTime = lastCommandTime;
    }

    private Element(By element) {
        this.element = element;
        this.SelenideElement = element;
        this.iOSElement = element;
        this.index = 0;
        this.collection = false;
        this.accesibilityId = "";
        this.accesibilityIdiOS = "";
    }

    public Element(String accesibilityId) {
        this.index = 0;
        this.collection = false;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityId;
    }

    private Element getElementObject() {
        return new Element(
                element,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    private void handleError(String message) {
        if (Configuration.softAsserts) putSoftAssert(message);
        else throw new TestUIException(message);
    }

    public UIElement setElement(By element) {
        return new Element(element);
    }

    public UIElement setElement(UIElement element) {
        return element;
    }

    public UIElement navigateTo(String url) {
        return navigate(url);
    }

    public UIElement setElement(String selector) {
        if (selector.contains(": ")) {
            return new Element(selector);
        }
        return new Element("accessibilityId: " + selector);
    }

    public UICollection setCollection(By element) {
        return EE(element);
    }

    public UICollection setCollection(String accesibilityId) {
        return EE(accesibilityId);
    }

    public UIElement setSelenideElement(By selenideElement) {
        return new Element(
                element,
                selenideElement,
                iOSElement,
                0,
                false,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public UIElement setiOSElement(By iOSElement) {
        return new Element(
                element,
                SelenideElement,
                iOSElement,
                0,
                false,
                accesibilityId,
                ""
        );
    }

    public UIElement setAndroidElement(By element) {
        return new Element(
                element,
                SelenideElement,
                iOSElement,
                0,
                false,
                "",
                accesibilityIdiOS
        );
    }

    public UIElement setAndroidElement(String accessibilityId) {
        if (accesibilityId.contains(": ")) {
            return new Element(
                    null,
                    SelenideElement,
                    iOSElement,
                    0,
                    false,
                    accessibilityId,
                    accesibilityIdiOS
            );
        }
        return new Element(
                null,
                SelenideElement,
                iOSElement,
                0,
                false,
                "accessibilityId: " + accesibilityId,
                accesibilityIdiOS
        );
    }

    public UIElement setiOSElement(String iOSElementAccId) {
        if (iOSElementAccId.contains(": ")) {
            return new Element(
                    element,
                    SelenideElement,
                    null,
                    0,
                    false,
                    accesibilityId,
                    iOSElementAccId
            );
        }
        return new Element(
                element,
                SelenideElement,
                null,
                0,
                false,
                accesibilityId,
                "accessibilityId: " + iOSElementAccId);
    }

    public UIElement click() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                element, SelenideElement);
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                if (!collection) {
                    waitUntilClickable(
                            getAppiumElement(iOSElement, element),
                            getAccesibilityId(accesibilityIdiOS, accesibilityId)
                    );
                } else {
                    waitUntilClickable(
                            getAppiumElement(iOSElement, element),
                            getAccesibilityId(accesibilityIdiOS, accesibilityId),
                            index
                    );
                }
                getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).click();
            } else {
                getSelenide(SelenideElement, index, collection).click();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        long finalTime = System.currentTimeMillis() - t;
        setTime(finalTime);
        putLogDebug("Element '%s' was clicked after %d ms", stringElement, finalTime);
        return getElementObject();
    }

    public UIElement doubleClick() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                element, SelenideElement);
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                if (!collection) {
                    waitUntilClickable(
                            getAppiumElement(iOSElement, element),
                            getAccesibilityId(accesibilityIdiOS, accesibilityId)
                    );
                } else {
                    waitUntilClickable(
                            getAppiumElement(iOSElement, element),
                            getAccesibilityId(accesibilityIdiOS, accesibilityId),
                            index
                    );
                }
                getElement(accesibilityIdiOS, accesibilityId,
                        iOSElement, element, index, collection).click();
                getElement(accesibilityIdiOS, accesibilityId,
                        iOSElement, element, index, collection).click();
            } else {
                getSelenide(SelenideElement, index, collection).doubleClick();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        long finalTime = System.currentTimeMillis() - t;
        setTime(finalTime);
        putLogDebug("Element '%s' was double clicked after %d ms", stringElement, finalTime);
        return getElementObject();
    }

    public Dimension getSize() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (!collection) {
                waitUntilVisible(
                        getAppiumElement(iOSElement, element),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        Configuration.timeout,
                        true
                );
            } else {
                waitUntilVisible(
                        getAppiumElement(iOSElement, element),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        Configuration.timeout,
                        true
                );
            }
            return getElement(accesibilityIdiOS, accesibilityId,
                    iOSElement, element, index, collection).getSize();
        } else {
            try {
                return getSelenide(SelenideElement, index, collection).getSize();
            } catch (Throwable e) {
                takeScreenshotsAllure();
                handleError(e.getMessage());
                return new Dimension(0,0);
            }
        }
    }

    public Point getLocation() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (!collection) {
                waitUntilVisible(
                        getAppiumElement(iOSElement, element),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        Configuration.timeout,
                        true
                );
            } else {
                waitUntilVisible(
                        getAppiumElement(iOSElement, element),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        Configuration.timeout,
                        true
                );
            }
            return getElement(accesibilityIdiOS, accesibilityId,
                    iOSElement, element, index, collection).getLocation();
        } else {
            try {
                return getSelenide(SelenideElement, index, collection).getLocation();
            } catch (Throwable e) {
                takeScreenshotsAllure();
                handleError(e.getMessage());
                return new Point(0,0);
            }
        }
    }

    public WaitAsserts waitFor(int Seconds) {
        return new WaitFor(
                element,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS,
                Seconds
        );
    }

    public String getText() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                return getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).getText();
            }
            return getSelenide(SelenideElement, index, collection).getText();
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
            return "";
        }
    }

    public UIElement sendKeys(CharSequence charSequence) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                element, SelenideElement);
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).sendKeys(charSequence);
            } else {
                getSelenide(
                        SelenideElement,
                        index,
                        collection).sendKeys(charSequence);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Send keys '%s' to element '%s' after %d ms", charSequence, stringElement, finalTime);
        return getElementObject();
    }

    public UIElement selectElementByValue(String... values) {
        for (String value : values) {
            UIElement e = E(By.xpath("//option[@value = " + Quotes.escape(value) + "]"));
            e.waitFor(5).untilIsVisible();
            if (Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                if (!e.getSelenideElement().isSelected()) {
                    e.getSelenideElement().click();
                }
            } else {
                if (!e.getMobileElement().isSelected()) {
                    e.getMobileElement().click();
                }
            }
        }

        return this;
    }

    public UIElement setValueJs(String value) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                element, SelenideElement);
        click();
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].value='" + value + "';",
                        getElementWithoutException(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection
                        )
                );
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(
                        "arguments[0].value='" + value + "';",
                        getSelenide(SelenideElement, index, collection)
                );
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Set value '%s' to element '%s' after %d ms", value, stringElement, finalTime);
        return getElementObject();
    }

    public UIElement setValueJs(String value, boolean clickBeforeSetValue) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                element, SelenideElement);
        if (clickBeforeSetValue) {
            click();
        }
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].value='" + value + "';",
                        getElementWithoutException(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection
                        )
                );
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(
                        "arguments[0].value='" + value + "';",
                        getSelenide(SelenideElement, index, collection)
                );
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Set value '%s' to element '%s' after %d ms", value, stringElement, finalTime);
        return getElementObject();
    }

    public UIElement executeJsOverElement(String JsScript) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                element, SelenideElement);
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        JsScript,
                        getElementWithoutException(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection
                        )
                );
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(
                        JsScript,
                        getSelenide(SelenideElement, index, collection));
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Executed JS '%s' over element '%s' after %d ms", JsScript, stringElement, finalTime);
        return getElementObject();
    }

    public UIElement executeJs(String var1, Object... var2) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(var1, var2);
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(var1, var2);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        return getElementObject();
    }

    public SlideActions scrollTo() {
        return new Scrolling(
                element,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public UIElement swipe(int XCoordinate, int YCoordinate) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                TouchActions action = new TouchActions(getDriver());
                action.moveToElement(
                        getElement(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection),
                        XCoordinate,
                        YCoordinate
                );
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement swipeRight() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                Dimension size = getDriver().manage().window().getSize();
                int endX = (int) (size.width * 0.8);
                TouchActions action = new TouchActions(getDriver());
                action.longPress(
                        getElement(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection
                        )
                ).move(
                        endX,
                        getElement(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection).getLocation().getY()
                ).release().perform();
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement swipeLeft() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                Dimension size = getDriver().manage().window().getSize();
                int endX = (int) (size.width * 0.10);
                TouchActions action = new TouchActions(getDriver());
                action.longPress(
                        getElement(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection
                        )
                ).move(
                        endX,
                        getElement(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                element,
                                index,
                                collection).getLocation().getY()
                ).release().perform();
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        return getElementObject();
    }

    public MobileElement getMobileElement() {
        try {
            return getElement(
                    accesibilityIdiOS,
                    accesibilityId,
                    iOSElement,
                    element,
                    index,
                    collection
            );
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
            return null;
        }
    }

    public UIElement clear() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).clear();
            } else {
                getSelenide(SelenideElement, index, collection).clear();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
        }
        return getElementObject();
    }

    public String getCssValue(String cssValue) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                return getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).getCssValue(cssValue);
            } else {
                return getSelenide(SelenideElement, index, collection).getCssValue(cssValue);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
            return "";
        }
    }

    public String getValue() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                return getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).getAttribute("value");
            } else {
                return getSelenide(SelenideElement, index, collection).getValue();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
            return "";
        }
    }

    public String getName() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                return getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).getAttribute("name");
            } else {
                return getSelenide(SelenideElement, index, collection).name();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
            return "";
        }
    }

    public String getAttribute(String Attribute) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                return getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        element,
                        index,
                        collection).getAttribute(Attribute);
            } else {
                return getSelenide(SelenideElement, index, collection).getAttribute(Attribute);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            handleError(e.getMessage());
            return "";
        }
    }

    public boolean isVisible() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
            return visible(
                    getAppiumElement(iOSElement, element),
                    getAccesibilityId(accesibilityIdiOS, accesibilityId)
            );
        return getSelenide(SelenideElement, index, collection).isDisplayed();
    }

    public boolean isEnabled() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
            return enable(
                    getAppiumElement(iOSElement, element),
                    getAccesibilityId(accesibilityIdiOS, accesibilityId)
            );
        return getSelenide(SelenideElement, index, collection).isEnabled();
    }

    public boolean Exists() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
            return exists(
                    getAppiumElement(iOSElement, element),
                    getAccesibilityId(accesibilityIdiOS, accesibilityId)
            );
        return getSelenide(SelenideElement, index, collection).exists();
    }

    public Asserts shouldHave() {
        return new ShouldBe(
                element,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS,
                Configuration.timeout,
                true
        );
    }

    public Asserts shouldBe() {
        return shouldHave();
    }

    public Asserts should() {
        return shouldHave();
    }


    public UIElement saveScreenshot(String path) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (getDrivers().size() != 0) {
                Configuration.driver = Math.min(Configuration.driver, getDrivers().size());
                File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
                try {
                    FileUtils.copyFile(scrFile, new File(
                            Configuration.screenshotPath + path));
                } catch (IOException e) {
                    putLogError("Could not save the screenshot in '" + Configuration.screenshotPath + path + "': " +
                            e.getMessage());
                }
            }
        } else {
            File scrFile = ((TakesScreenshot) getSelenideDriver()).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(scrFile, new File(
                        Configuration.screenshotPath + path));
            } catch (IOException e) {
                putLogError("Could not save the screenshot in '" + Configuration.screenshotPath + path + "': " +
                        e.getMessage());
            }
        }
        return getElementObject();
    }

    public String getCurrentUrl() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
            return getDriver().getCurrentUrl();
        return getSelenideDriver().getCurrentUrl();
    }

    public BrowserLogs getNetworkCalls() {
        return new BrowserLogs().getNetworkCalls();
    }

    public void getBrowserLogs() {
        new BrowserLogs().getBrowserLogs();
    }

    public BrowserLogs getLastNetworkCalls(int LastX) {
        return new BrowserLogs().getLastNetworkCalls(LastX);
    }

    public UIElement and() {
        return getElementObject();
    }

    public UIElement and(String description) {
        System.out.println("\u001B[32m Working step ->   And " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("And " + description);
        }
        return getElementObject();
    }

    public UIElement given() {
        return getElementObject();
    }

    public UIElement given(String description) {
        System.out.println("\u001B[32m Working step -> Given " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("Given " + description);
        }
        return getElementObject();
    }

    public UIElement then() {
        return getElementObject();
    }

    public UIElement then(String description) {
        System.out.println("\u001B[32m Working step -> Then " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("Then " + description);
        }
        return getElementObject();
    }

    public UIElement when(String description) {
        System.out.println("\u001B[32m Working step -> When " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("When " + description);
        }
        return getElementObject();
    }

    public UIElement when() {
        return getElementObject();
    }


    public com.codeborne.selenide.SelenideElement getSelenideElement() {
        return getSelenide(SelenideElement, index, collection);
    }

    private static boolean step = false;

    public static boolean getStep() {
        return step;
    }

    public static void setStep(boolean step) {
        Element.step = step;
    }
}
