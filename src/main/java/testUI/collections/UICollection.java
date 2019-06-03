package testUI.collections;

import com.codeborne.selenide.ElementsCollection;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import testUI.Configuration;
import testUI.elements.UIElement;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static org.hamcrest.MatcherAssert.assertThat;
import static testUI.TestUIDriver.getDriver;
import static testUI.Utils.AppiumHelps.*;
import static testUI.elements.TestUI.takeScreenshotInFaiure;

public class UICollection implements Collection {
    private int index = 0;
    private By element;
    private By SelenideElement;
    private By iOSElement;
    private String accesibilityId;
    private String accesibilityIdiOS;

    public static UICollection EE(By element) {
        return new UICollection(element, element, element, 0, "","");
    }

    public static UICollection EE(String accesibilityId) {
        return new UICollection(null, null, null, 0,accesibilityId ,accesibilityId);
    }

    public static UICollection EEx(String xpath) {
        return new UICollection(By.xpath(xpath), By.xpath(xpath), By.xpath(xpath), 0, "","");
    }

    protected UICollection(By element, By SelenideElement, By iOSElement, int index, String accesibilityId, String accesibilityIdiOS) {
        this.element = element;
        this.index = index;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
    }

    public UICollection setSelenideCollection(By SelenideElement) {
        return new UICollection(element, SelenideElement, element, index, accesibilityId,accesibilityIdiOS);
    }

    public UICollection setIOSCollection(By iOSElement) {
        return new UICollection(element, SelenideElement, iOSElement, index, accesibilityId, accesibilityIdiOS);
    }

    public UICollection setIOSCollection(String accesibilityIdiOS) {
        return new UICollection(element, SelenideElement, iOSElement, index, accesibilityId, accesibilityIdiOS);
    }

    public UICollection setAndroidCollection(By element) {
        return new UICollection(element, SelenideElement, iOSElement, index, accesibilityId, accesibilityIdiOS);
    }

    public UICollection setAndroidCollection(String accesibilityId) {
        return new UICollection(element, SelenideElement, iOSElement, index, accesibilityId, accesibilityIdiOS);
    }

    public ElementsCollection getSelenideCollection() {
        return $$(SelenideElement);
    }

    private By getAppiumElement() {
        if (Configuration.iOSTesting)
            return iOSElement;
        return element;
    }

    private String getAccesibilityId() {
        if (Configuration.iOSTesting)
            return accesibilityIdiOS;
        return accesibilityId;
    }

    private List getElementList() {
        if (!getAccesibilityId().isEmpty())
            return getDriver().findElementsByAccessibilityId(getAccesibilityId());
        return getDriver().findElements(getAppiumElement());
    }

    public String asString() {
        try {
            if (Configuration.deviceTests) {
                return getElementList().get(index).toString();
            } else {
                return $$(SelenideElement).get(index).toString();
            }
        } catch (Throwable e) {
            takeScreenshotInFaiure();
            throw new Error(e);
        }
    }

    public UIElement get(int i) {
        return new UIElement(element, SelenideElement, element, i, true, accesibilityId, accesibilityIdiOS);
    }

    public UIElement first() {
        return new UIElement(element, SelenideElement, element, 0, true, accesibilityId,accesibilityIdiOS);
    }

    public int size() {
        if (Configuration.deviceTests) {
            try {
                return getElementList().size();
            } catch (Exception e) {
                return 0;
            }
        } else {
            try {
                return $$(SelenideElement).size();
            } catch (Throwable e) {
                takeScreenshotInFaiure();
                throw new Error(e);
            }
        }
    }

    public Dimension getSize() {
        if (Configuration.deviceTests) {
            try {
                return ((MobileElement) getElementList().get(index)).getSize();
            } catch (Exception e) {
                return new Dimension(0,0);
            }
        } else {
            try {
                return $$(SelenideElement).get(index).getSize();
            } catch (Throwable e) {
                takeScreenshotInFaiure();
                throw new Error(e);
            }
        }
    }

    public UIElement findByVisible() {
        if (Configuration.deviceTests) {
            for (int i = 0; i < size(); i++) {
                if (visible(getAppiumElement(), getAccesibilityId(), i)) {
                    return new UIElement(element, SelenideElement, element, i, true, accesibilityId,accesibilityIdiOS);
                }
            }
        } else {
            for (int i = 0; i < size(); i++) {
                if ($$(SelenideElement).get(i).is(com.codeborne.selenide.Condition.visible)) {
                    return new UIElement(element, SelenideElement, element, i, true, accesibilityId,accesibilityIdiOS);
                }
            }
        }
        takeScreenshotInFaiure();
        assertThat("No visible element with this selector: " + element.toString(), false);
        return new UIElement(element, SelenideElement, element, index, true, accesibilityId,accesibilityIdiOS);
    }

    public UIElement findByText(String text) {
        if (Configuration.deviceTests) {
            for (int i = 0; i < size(); i++) {
                if (containsText(getAppiumElement(), getAccesibilityId(), i, text)) {
                    return new UIElement(element, SelenideElement, element, i, true, accesibilityId,accesibilityIdiOS);
                }
            }
        } else {
            for (int i = 0; i < size(); i++) {
                if ($$(SelenideElement).get(i).is(com.codeborne.selenide.Condition.text(text))) {
                    return new UIElement(element, SelenideElement, element, i, true, accesibilityId,accesibilityIdiOS);
                }
            }
        }
        takeScreenshotInFaiure();
        assertThat("No visible element with that text '" + text + "' and this selector: " + element.toString(), false);
        return new UIElement(element, SelenideElement, element, index, true, accesibilityId,accesibilityIdiOS);
    }

    public UIElement findByEnabled() {
        if (Configuration.deviceTests) {
            for (int i = 0; i < size(); i++) {
                if (enable(getAppiumElement(), getAccesibilityId(), i)) {
                    return new UIElement(element, SelenideElement, element, i, true, accesibilityId,accesibilityIdiOS);
                }
            }
        } else {
            for (int i = 0; i < size(); i++) {
                if ($$(SelenideElement).get(i).is(com.codeborne.selenide.Condition.enabled)) {
                    return new UIElement(element, SelenideElement, element, i, true, accesibilityId,accesibilityIdiOS);
                }
            }
        }
        takeScreenshotInFaiure();
        assertThat("No enabled element with this selector: " + element.toString(), false);
        return new UIElement(element, SelenideElement, element, index, true, accesibilityId,accesibilityIdiOS);
    }
}
