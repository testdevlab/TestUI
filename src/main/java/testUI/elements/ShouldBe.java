package testUI.elements;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import testUI.Configuration;

import static testUI.Utils.WaitUntil.*;

public class ShouldBe extends TestUI {
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

    private Condition condition(boolean hasCondition, Condition condition) {
        if (hasCondition)
            return condition;
        return Condition.not(condition);
    }

    public ShouldBe not(){
        return new ShouldBe(AppiumElement,SelenideElement, iOSElement,index, collection, accesibilityId,accesibilityIdiOS,5, false);
    }

    public UIElement visible() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilVisible(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index, time,
                        is);
            } else {
                waitUntilVisible(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time, is);

            }
        } else {
            selenideAssert(condition(is, Condition.visible), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement enabled() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilEnable(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), index, time,
                        is);
            } else {
                waitUntilEnable(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time, is);
            }
        } else {
            selenideAssert(condition(is, Condition.enabled), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement exists() {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilExist(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), index,
                        time, is);
            } else {
                waitUntilExist(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time, is);
            }
        } else {
            selenideAssert(condition(is, Condition.exist), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement containText(String text) {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilContainsText(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), index,
                        time,
                        text, is);
            } else {
                waitUntilContainsText(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time, text, is);
            }
        } else {
            selenideAssert(condition(is, Condition.textCaseSensitive(text)), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement exactText(String text) {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilExactText(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), index,
                        time,
                        text, is);
            } else {
                waitUntilExactText(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time, text, is);
            }
        } else {
            selenideAssert(condition(is, Condition.exactText(text)), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement containNoCaseSensitiveText(String text) {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilContainsTextNoCaseSensitive(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), index,
                        time,
                        text, is);
            } else {
                waitUntilContainsTextNoCaseSensitive(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time, text, is);
            }
        } else {
            selenideAssert(condition(is, Condition.text(text)), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement value(String text) {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilHasValue(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId),index,
                        time,
                        text, is);
            } else {
                waitUntilHasValue(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS,accesibilityId), time, text, is);
            }
        } else {
            selenideAssert(condition(is, Condition.value(text)), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }

    public AttributeImp attribute(String Attribute) {
        return new AttributeImp(AppiumElement, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS, Attribute,
                time,
                is);
    }

    public UIElement theAttribute(String Attribute) {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilHasAttribute(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index, time, Attribute, is);
            } else {
                waitUntilHasAttribute(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS,accesibilityId), time, Attribute, is);
            }
        } else {
            selenideAssert(condition(is, Condition.attribute(Attribute)), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index,collection, accesibilityId, accesibilityIdiOS);
    }
}
