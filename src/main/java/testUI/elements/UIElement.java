package testUI.elements;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.touch.TouchActions;
import testUI.Configuration;
import testUI.collections.UICollection;

import static testUI.TestUIDriver.getDriver;
import static testUI.UIOpen.navigate;
import static testUI.Utils.AppiumHelps.*;
import static testUI.Utils.WaitUntil.waitUntilClickable;
import static testUI.Utils.WaitUntil.waitUntilVisible;
import static testUI.collections.UICollection.EE;

public class UIElement extends TestUI implements ElementActions {
    protected By element;
    private By SelenideElement;
    private By iOSElement;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;

    public UIElement(By element,
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

    private UIElement(By element) {
        this.element = element;
        this.SelenideElement = element;
        this.iOSElement = element;
        this.index = 0;
        this.collection = false;
        this.accesibilityId = "";
        this.accesibilityIdiOS = "";
    }

    private UIElement(String accesibilityId) {
        this.index = 0;
        this.collection = false;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityId;
    }

    public UIElement setElement(By element) {
        return new UIElement(element);
    }

    public UIElement setElement(UIElement element) {
        return element;
    }

    public UIElement navigateTo(String url) {
        return navigate(url);
    }

    public UIElement setElement(String accesibilityId) {
        return new UIElement(accesibilityId);
    }

    public UICollection setCollection(By element) {
        return EE(element);
    }

    public UICollection setCollection(String accesibilityId) {
        return EE(accesibilityId);
    }

    public UIElement setSelenideElement(By selenideElement) {
        return new UIElement(element,selenideElement, iOSElement,0,false,accesibilityId,accesibilityIdiOS);
    }

    public UIElement setiOSElement(By iOSElement) {
        return new UIElement(element, SelenideElement, iOSElement,0,false,accesibilityId,accesibilityIdiOS);
    }

    public UIElement setAndroidElement(By element) {
        return new UIElement(element, SelenideElement, iOSElement,0,false,accesibilityId,accesibilityIdiOS);
    }

    public UIElement setAndroidElement(String accesibilityId) {
        return new UIElement(null, SelenideElement, iOSElement,0,false,accesibilityId,accesibilityIdiOS);
    }

    public UIElement setiOSElement(String iOSElementAccId) {
        return new UIElement(element,SelenideElement,null,0, false,accesibilityId, iOSElementAccId);
    }

    public UIElement click() {
        try {
            if (Configuration.deviceTests) {
                if (collection) {
                    waitUntilClickable(getAppiumElement(iOSElement, element), getAccesibilityId(accesibilityIdiOS, accesibilityId));
                } else {
                    waitUntilClickable(getAppiumElement(iOSElement, element), getAccesibilityId(accesibilityIdiOS, accesibilityId), index);
                }
                getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index, collection).click();
            } else {
                    getSelenide(SelenideElement, index, collection).click();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }

    public Dimension getSize() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilVisible(getAppiumElement(iOSElement, element),getAccesibilityId(accesibilityIdiOS,accesibilityId), Configuration.timeout, true);
            } else {
                waitUntilVisible(getAppiumElement(iOSElement, element),getAccesibilityId(accesibilityIdiOS,accesibilityId), Configuration.timeout, true);
            }
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getSize();
        } else {
            try {
                return getSelenide(SelenideElement, index, collection).getSize();
            } catch (Throwable e) {
                takeScreenshotsAllure();
                throw new Error(e);
            }
        }
    }

    public Point getLocation() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilVisible(getAppiumElement(iOSElement, element),getAccesibilityId(accesibilityIdiOS,accesibilityId), Configuration.timeout, true);
            } else {
                waitUntilVisible(getAppiumElement(iOSElement, element),getAccesibilityId(accesibilityIdiOS,accesibilityId), Configuration.timeout, true);
            }
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getLocation();
        } else {
            try{
                return getSelenide(SelenideElement,index, collection).getLocation();
            } catch (Throwable e) {
                takeScreenshotsAllure();
                throw new Error(e);
            }
        }
    }

    public WaitFor waitFor(int Seconds) {
        return new WaitFor(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS, Seconds);
    }

    public String getText() {
        try {
            if (Configuration.deviceTests) {
                return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getText();
            }
            return getSelenide(SelenideElement,index, collection).getText();
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public UIElement sendKeys(CharSequence charSequence) {
        try {
            if (Configuration.deviceTests) {
                getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).sendKeys(charSequence);
            } else {
                getSelenide(SelenideElement, index, collection).sendKeys(charSequence);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement setValueJs(String value) {
        click();
        try {
            if (Configuration.deviceTests) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value='" + value + "';",
                        getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection));
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("arguments[0].value='" + value + "';",
                        getSelenide(SelenideElement, index, collection));
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement setValueJs(String value, boolean clickBeforeSetValue) {
        if (clickBeforeSetValue) {
            click();
        }
        try {
            if (Configuration.deviceTests) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value='" + value + "';",
                        getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection));
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("arguments[0].value='" + value + "';",
                        getSelenide(SelenideElement, index, collection));
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement executeJsOverElement(String JsScript) {
        try {
            if (Configuration.deviceTests) {
                ((JavascriptExecutor) getDriver()).executeScript(JsScript,
                        getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection));
            } else {
                ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript(JsScript,
                        getSelenide(SelenideElement, index, collection));
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }

    public Scrolling scrollTo() {
        return new Scrolling(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    @Deprecated
    public UIElement scrollIntoView(boolean upCenter) {
        try {
            if (Configuration.deviceTests) {
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(" + upCenter + ");",
                        getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection));
            } else {
                getSelenide(SelenideElement,index, collection).scrollIntoView(upCenter);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement swipe(int XCoordinate, int YCoordinate) {
        try {
            if (Configuration.deviceTests) {
                TouchActions action = new TouchActions(getDriver());
                action.moveToElement(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection), XCoordinate, YCoordinate);
            } else {
                getSelenide(SelenideElement,index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement swipeRight() {
        try {
            if (Configuration.deviceTests) {
                Dimension size = getDriver().manage().window().getSize();
                int endX = (int) (size.width * 0.8);
                TouchActions action = new TouchActions(getDriver());
                action.longPress(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection)).move(endX,
                        getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getLocation().getY()).release().perform();
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement swipeLeft() {
        try {
            if (Configuration.deviceTests) {
                Dimension size = getDriver().manage().window().getSize();
                int endX = (int) (size.width * 0.10);
                TouchActions action = new TouchActions(getDriver());
                action.longPress(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection)).move(endX,
                        getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getLocation().getY()).release().perform();
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public MobileElement getMobileElement() {
        try {
            return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection);
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public UIElement clear() {
        try {
            if (Configuration.deviceTests) {
                getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).clear();
            } else {
                    getSelenide(SelenideElement, index, collection).clear();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public String getCssValue(String cssValue) {
        try {
            if (Configuration.deviceTests) {
                return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).getCssValue(cssValue);
            } else {
                return getSelenide(SelenideElement, index, collection).getCssValue(cssValue);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public String getValue() {
        try {
            if (Configuration.deviceTests) {
                return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).getAttribute("value");
            } else {
                return getSelenide(SelenideElement, index, collection).getValue();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public String getName() {
        try {
            if (Configuration.deviceTests) {
                return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).getAttribute("name");
            } else {
                return getSelenide(SelenideElement, index, collection).name();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public String getAttribute(String Attribute) {
        try {
            if (Configuration.deviceTests) {
                return getElement(accesibilityIdiOS, accesibilityId, iOSElement, element, index, collection).getAttribute(Attribute);
            } else {
                return getSelenide(SelenideElement, index, collection).getAttribute(Attribute);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public boolean isVisible() {
        if (Configuration.deviceTests)
            return visible(getAppiumElement(iOSElement,element),getAccesibilityId(accesibilityIdiOS,accesibilityId));
        return getSelenide(SelenideElement,index, collection).isDisplayed();
    }

    public boolean isEnabled() {
        if (Configuration.deviceTests)
            return enable(getAppiumElement(iOSElement,element),getAccesibilityId(accesibilityIdiOS,accesibilityId));
        return getSelenide(SelenideElement,index, collection).isEnabled();
    }

    public boolean Exists() {
        if (Configuration.deviceTests)
            return exists(getAppiumElement(iOSElement,element),getAccesibilityId(accesibilityIdiOS,accesibilityId));
        return getSelenide(SelenideElement,index, collection).exists();
    }

    public ShouldBe shouldHave() {
        return new ShouldBe(element,SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS, Configuration.timeout, true);
    }

    public ShouldBe shouldBe() {
        return new ShouldBe(element, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS, Configuration.timeout, true);
    }

    public ShouldBe should() {
        return new ShouldBe(element, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS, Configuration.timeout, true);
    }

    public UIElement and() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement and(String description) {
        System.out.println("\u001B[32m Working step ->   And " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("And " + description);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement given() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement given(String description) {
        System.out.println("\u001B[32m Working step -> Given " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("Given " + description);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement then() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement then(String description) {
        System.out.println("\u001B[32m Working step -> Then " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("Then " + description);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement when(String description) {
        System.out.println("\u001B[32m Working step -> When " + description + "\u001B[0m");
        step = true;
        if (Configuration.useAllure) {
            Allure.step("When " + description);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement when() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }


    public com.codeborne.selenide.SelenideElement getSelenideElement() {
        return getSelenide(element,index, collection);
    }

    private static boolean step = false;

    public static boolean getStep() {
        return step;
    }

    public static void setStep(boolean step) {
        UIElement.step = step;
    }
}
