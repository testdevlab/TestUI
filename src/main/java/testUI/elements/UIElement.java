package testUI.elements;

import testUI.Configuration;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.touch.TouchActions;

import static testUI.TestUIDriver.getDriver;
import static testUI.Utils.AppiumHelps.*;
import static testUI.Utils.WaitUntil.waitUntilClickable;
import static testUI.Utils.WaitUntil.waitUntilVisible;

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

    public UIElement setElement(By element) {
        return new UIElement(element,element, element,0,false,accesibilityId,accesibilityIdiOS);
    }

    public UIElement setElement(String accesibilityId) {
        return new UIElement(element,SelenideElement, iOSElement,0,false,accesibilityId,accesibilityId);
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
        return new UIElement(element, SelenideElement, iOSElement,0,false,accesibilityId,accesibilityIdiOS);
    }

    public UIElement setiOSElement(String iOSElementAccId) {
        return new UIElement(null,null,null,0, false,accesibilityId, iOSElementAccId);
    }

    public UIElement click() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilClickable(getAppiumElement(iOSElement, element), getAccesibilityId(accesibilityIdiOS, accesibilityId));
            } else {
                waitUntilClickable(getAppiumElement(iOSElement, element), getAccesibilityId(accesibilityIdiOS, accesibilityId), index);
            }
            getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index, collection).click();
        } else {
            getSelenide(SelenideElement,index, collection).click();
        }
        return new UIElement(element, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }

    public Dimension getSize() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilVisible(getAppiumElement(iOSElement, element),getAccesibilityId(accesibilityIdiOS,accesibilityId),Configuration.timeout, true);
            } else {
                waitUntilVisible(getAppiumElement(iOSElement, element),getAccesibilityId(accesibilityIdiOS,accesibilityId),Configuration.timeout, true);
            }
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getSize();
        } else {
            return getSelenide(SelenideElement,index, collection).getSize();
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
            return getSelenide(SelenideElement,index, collection).getLocation();
        }
    }

    public WaitFor waitFor(int Seconds) {
        return new WaitFor(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS, Seconds);
    }

    public String getText() {
        if (Configuration.deviceTests) {
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getText();
        }
        return getSelenide(SelenideElement,index, collection).getText();
    }

    public UIElement sendKeys(CharSequence charSequence) {
        if (Configuration.deviceTests) {
            getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).sendKeys(charSequence);
        } else {
            getSelenide(SelenideElement,index, collection).sendKeys(charSequence);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement scrollIntoView() {
        if (Configuration.deviceTests) {
            TouchActions action = new TouchActions(getDriver());
            action.moveToElement(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection)).build().perform();
        } else {
            getSelenide(SelenideElement,index, collection).scrollIntoView(true);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement swipe(int XCoordinate, int YCoordinate) {
        if (Configuration.deviceTests) {
            TouchActions action = new TouchActions(getDriver());
            action.moveToElement(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection), XCoordinate, YCoordinate);
        } else {
            getSelenide(SelenideElement,index, collection).scrollIntoView(true);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement swipeRight() {
        if (Configuration.deviceTests) {
            Dimension size = getDriver().manage().window().getSize();
            int endX = (int) (size.width * 0.8);
            TouchActions action = new TouchActions(getDriver());
            action.longPress(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection)).move(endX,
                    getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getLocation().getY()).release().perform();
        } else {
            getSelenide(SelenideElement,index, collection).scrollIntoView(true);
        }
        return new UIElement(element, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement swipeLeft() {
        if (Configuration.deviceTests) {
            Dimension size = getDriver().manage().window().getSize();
            int endX = (int) (size.width * 0.10);
            TouchActions action = new TouchActions(getDriver());
            action.longPress(getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection)).move(endX,
                    getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getLocation().getY()).release().perform();
        } else {
            getSelenide(SelenideElement,index, collection).scrollIntoView(true);
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public MobileElement getMobileElement() {
        return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection);
    }

    public UIElement clear() {
        if (Configuration.deviceTests) {
            getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).clear();
        } else {
            getSelenide(SelenideElement,index, collection).clear();
        }
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public String getCssValue(String cssValue) {
        if (Configuration.deviceTests) {
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getCssValue(cssValue);
        } else {
            return getSelenide(SelenideElement,index, collection).getCssValue(cssValue);
        }
    }

    public String getValue() {
        if (Configuration.deviceTests) {
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getAttribute("value");
        } else {
            return getSelenide(SelenideElement,index, collection).getValue();
        }
    }

    public String getName() {
        if (Configuration.deviceTests) {
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getAttribute("name");
        } else {
            return getSelenide(SelenideElement,index, collection).name();
        }
    }

    public String getAttribute(String Attribute) {
        if (Configuration.deviceTests) {
            return getElement(accesibilityIdiOS,accesibilityId,iOSElement,element,index,collection).getAttribute(Attribute);
        } else {
            return getSelenide(SelenideElement,index, collection).getAttribute(Attribute);
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
        return new ShouldBe(element,SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS,Configuration.timeout, true);
    }

    public ShouldBe shouldBe() {
        return new ShouldBe(element, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS,Configuration.timeout, true);
    }

    public ShouldBe should() {
        return new ShouldBe(element, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS,Configuration.timeout, true);
    }

    public UIElement and() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement given() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement then() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement when() {
        return new UIElement(element, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public com.codeborne.selenide.SelenideElement getSelenideElement() {
        return getSelenide(element,index, collection);
    }
}
