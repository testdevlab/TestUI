package testUI.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebElementCondition;
import org.openqa.selenium.By;
import testUI.Configuration;

import static testUI.TestUIDriver.getDriver;
import static testUI.TestUIDriver.getSelenideDriver;
import static testUI.UIUtils.UIAssert;
import static testUI.Utils.WaitUntil.*;

public class ShouldBe extends TestUI implements Asserts {
    private By AppiumElement;
    private By SelenideElement;
    private By iOSElement;
    private int time;
    private boolean is;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;

    protected ShouldBe(By AppiumElement,
                       By SelenideElement,
                       By iOSElement,
                       int index,
                       boolean collection,
                       String accesibilityId,
                       String accesibilityIdiOS,
                       int time, boolean is) {
        this.AppiumElement = AppiumElement;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.time = time;
        this.is = is;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
        this.index = index;
        this.collection = collection;
    }

    private WebElementCondition condition(boolean hasCondition, WebElementCondition condition) {
        if (hasCondition)
            return condition;
        return Condition.not(condition);
    }

    private Element getElementObject() {
        return new Element(
                AppiumElement,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS);
    }

    public Asserts not() {
        return new ShouldBe(
                AppiumElement,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS,
                Configuration.timeout,
                false);
    }

    public UIElement visible() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilVisible(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        is);
            } else {
                waitUntilVisible(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        is);
            }
        } else {
            selenideAssert(
                    condition(is, Condition.visible),
                    time,
                    SelenideElement,
                    index,
                    collection);
        }
        return getElementObject();
    }

    public UIElement enabled() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilEnable(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        is);
            } else {
                waitUntilEnable(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.enabled), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public UIElement exists() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilExist(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        is);
            } else {
                waitUntilExist(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.exist), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public UIElement containText(String text) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilContainsText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        is);
            } else {
                waitUntilContainsText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.textCaseSensitive(text)), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public UIElement exactText(String text) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilExactText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        is);
            } else {
                waitUntilExactText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.exactText(text)), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public UIElement containNoCaseSensitiveText(String text) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilContainsTextNoCaseSensitive(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        is);
            } else {
                waitUntilContainsTextNoCaseSensitive(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.text(text)), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public UIElement value(String text) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilHasValue(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        is);
            } else {
                waitUntilHasValue(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.value(text)), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public Attribute attribute(String Attribute) {
        return new AttributeImp(
                AppiumElement,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS,
                Attribute,
                time,
                is);
    }

    public UIElement theAttribute(String Attribute) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilHasAttribute(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        Attribute,
                        is);
            } else {
                waitUntilHasAttribute(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        Attribute,
                        is);
            }
        } else {
            selenideAssert(
                    condition(is, Condition.attribute(Attribute)),
                    time,
                    SelenideElement,
                    index,
                    collection);
        }
        return getElementObject();
    }

    public UIElement emptyText() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilEmptyText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        is);
            } else {
                waitUntilEmptyText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        is);
            }
        } else {
            selenideAssert(condition(is, Condition.empty), time, SelenideElement, index, collection);
        }
        return getElementObject();
    }

    public UIElement emptyAttribute(String Attribute) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            if (collection) {
                waitUntilEmptyAttribute(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        Attribute,
                        is);
            } else {
                waitUntilEmptyAttribute(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        Attribute,
                        is);
            }
        } else {
            selenideAssert(
                    condition(is, Condition.attribute(Attribute, "")),
                    time,
                    SelenideElement,
                    index,
                    collection);
        }
        return getElementObject();
    }


    public UIElement currentUrlEqualTo(String expectedUrl) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            UIAssert(
                    "The url is not as expected\n" +
                            "Expected: " +
                            expectedUrl +
                            "\n But was: " +
                            getDriver().getCurrentUrl(),
                    getDriver().getCurrentUrl().equals(expectedUrl));
        } else {
            UIAssert(
                    "The url is not as expected\n" +
                            "Expected: " +
                            expectedUrl +
                            "\n But was: " +
                            getSelenideDriver().getCurrentUrl(),
                    getSelenideDriver().getCurrentUrl().equals(expectedUrl));
        }
        return getElementObject();
    }

    public UIElement currentUrlContains(String expectedUrl) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            UIAssert("The url is not as expected\n" +
                            "Expected: " +
                            expectedUrl +
                            "\n But was: " +
                            getDriver().getCurrentUrl(),
                    getDriver().getCurrentUrl().contains(expectedUrl));
        } else {
            UIAssert("The url is not as expected\n" +
                            "Expected: " +
                            expectedUrl +
                            "\n But was: " +
                            getSelenideDriver().getCurrentUrl(),
                    getSelenideDriver().getCurrentUrl().contains(expectedUrl));
        }
        return getElementObject();
    }
}
