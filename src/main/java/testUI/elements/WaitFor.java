package testUI.elements;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import testUI.Configuration;

import static testUI.Utils.Logger.putLogDebug;
import static testUI.Utils.WaitUntil.*;

public class WaitFor extends ShouldBe implements Asserts, WaitAsserts {
    private By AppiumElement;
    private By SelenideElement;
    private By iOSElement;
    private int time;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;

    protected WaitFor(By AppiumElement,
                      By SelenideElement,
                      By iOSElement,
                      int index,
                      boolean collection,
                      String accesibilityId,
                      String accesibilityIdiOS,
                      int time) {
        super(AppiumElement,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS,
                time,
                true);
        this.AppiumElement = AppiumElement;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.time = time;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
        this.index = index;
        this.collection = collection;
    }

    private Element getElementObject() {
        return new Element(
                AppiumElement,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public UIElement untilIsVisible() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilVisible(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        true
                );
            } else {
                waitUntilVisible(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        true
                );
            }
        } else {
            selenideAssert(Condition.visible, time, SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' was visible after " + finalTime +
                " ms");
        return getElementObject();
    }

    public Element untilIsEnabled() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilEnable(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        true
                );
            } else {
                waitUntilEnable(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        true
                );
            }
        } else {
            selenideAssert(Condition.enabled, time, SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' was enabled after " + finalTime +
                " ms");
        return getElementObject();
    }

    public Element untilExists() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilExist(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        true
                );
            } else {
                waitUntilExist(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        true
                );
            }
        } else {
            selenideAssert(Condition.exist, time, SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' exists after " + finalTime +
                " ms");
        return getElementObject();
    }

    public Element untilHasText(String text) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilContainsText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        true
                );
            } else {
                waitUntilContainsText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        true
                );
            }
        } else {
            selenideAssert(Condition.text(text), time, SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' has text '" + text + "' after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilHasCaseNotSensitiveText(String text) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilContainsTextNoCaseSensitive(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        true
                );
            } else {
                waitUntilContainsTextNoCaseSensitive(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        true
                );
            }
        } else {
            selenideAssert(Condition.text(text), time, SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' has text '" + text + "' after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilHasValue(String value) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilHasValue(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        value,
                        true
                );
            } else {
                waitUntilHasValue(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        value,
                        true
                );
            }
        } else {
            selenideAssert(Condition.value(value), time, SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' has value '" + value + "' after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilNotVisible() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilVisible(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        false);
            } else {
                waitUntilVisible(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        false);
            }
        } else {
            selenideAssert(Condition.not(Condition.visible),
                    time, SelenideElement, index, collection);

        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' is not visible after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilNotExists() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilExist(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        false);
            } else {
                waitUntilExist(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        false
                );
            }
        } else {
            selenideAssert(Condition.not(Condition.exist), time,
                    SelenideElement, index, collection);
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' not exists after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilNotEnabled() {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilEnable(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        false
                );
            } else {
                waitUntilEnable(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        false
                );
            }
            waitUntilEnable(
                    getAppiumElement(iOSElement, AppiumElement),
                    getAccesibilityId(accesibilityIdiOS, accesibilityId),
                    time,
                    false
            );
        } else {
            selenideAssert(
                    Condition.not(Condition.enabled),
                    time,
                    SelenideElement,
                    index,
                    collection
            );
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' is not enabled after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilHasNotText(String text) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilContainsText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        text,
                        false
                );

            } else {
                waitUntilContainsText(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        text,
                        false
                );
            }
        } else {
            selenideAssert(
                    Condition.not(Condition.text(text)),
                    time,
                    SelenideElement,
                    index,
                    collection
            );
        }
        long finalTime = System.currentTimeMillis() - t;
        putLogDebug("Element '" + stringElement + "' has no text '" + text + "' after "
                + finalTime + " ms");
        return getElementObject();
    }

    public Element untilHasNotValue(String value) {
        long t = System.currentTimeMillis();
        String stringElement = getStringElement(accesibilityIdiOS, accesibilityId, iOSElement,
                AppiumElement, SelenideElement);
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilHasValue(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index,
                        time,
                        value,
                        false
                );
            } else {
                waitUntilHasValue(
                        getAppiumElement(iOSElement, AppiumElement),
                        getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        time,
                        value,
                        false
                );
            }
        } else {
            selenideAssert(Condition.not(Condition.text(value)),
                    time, SelenideElement, index, collection);
        }
        long finishTime = (System.currentTimeMillis() - t);
        putLogDebug("Element '" + stringElement + "' has no value '" + value + "' after "
                + finishTime + " ms");
        return getElementObject();
    }

    public Attribute untilHasAttribute(String Attribute) {
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
                true
        );
    }

    public Attribute untilNotHasAttribute(String Attribute) {
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
                false
        );
    }
}
