package testUI.collections;

import com.codeborne.selenide.ElementsCollection;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import testUI.Configuration;
import testUI.Utils.TestUIException;
import testUI.elements.Element;
import testUI.elements.UIElement;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$$;
import static testUI.TestUIDriver.*;
import static testUI.UIUtils.UIAssert;
import static testUI.Utils.AppiumHelps.*;
import static testUI.elements.TestUI.takeScreenshotsAllure;

public class TestUI implements UICollection {
    private int index;
    private By element;
    private By SelenideElement;
    private By iOSElement;
    private String accesibilityId;
    private String accesibilityIdiOS;

    public static UICollection EE(By element) {
        return new TestUI(
                element,
                element,
                element,
                0,
                "",
                ""
        );
    }

    public static UICollection EE(String accessibilityId) {
        if (accessibilityId.contains(": ")) {
            return new TestUI(
                    null,
                    null,
                    null,
                    0,
                    accessibilityId ,
                    accessibilityId
            );
        }
        return new TestUI(
                null,
                null,
                null,
                0,
                "accessibilityId: " + accessibilityId ,
                "accessibilityId: " + accessibilityId
        );
    }

    public static UICollection EEx(String xpath) {
        return new TestUI(
                By.xpath(xpath),
                By.xpath(xpath),
                By.xpath(xpath),
                0,
                "",
                ""
        );
    }

    protected TestUI(
            By element,
            By SelenideElement,
            By iOSElement,
            int index,
            String accessibilityId,
            String accessibilityIdaOS) {
        this.element = element;
        this.index = index;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.accesibilityId = accessibilityId;
        this.accesibilityIdiOS = accessibilityIdaOS;
    }

    public UICollection setSelenideCollection(By SelenideElement) {
        return new TestUI(
                element,
                SelenideElement,
                element,
                index,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public UICollection setIOSCollection(By iOSElement) {
        return new TestUI(
                element,
                SelenideElement,
                iOSElement,
                index,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public UICollection setIOSCollection(String accessibilityIdiOS) {
        if (accessibilityIdiOS.contains(": ")) {
            return new TestUI(
                    element,
                    SelenideElement,
                    null,
                    index,
                    accesibilityId,
                    accessibilityIdiOS);
        }
        return new TestUI(
                element,
                SelenideElement,
                null,
                index,
                accesibilityId,
                "accessibilityId: " + accessibilityIdiOS
        );
    }

    public UICollection setAndroidCollection(By element) {
        return new TestUI(
                element,
                SelenideElement,
                iOSElement,
                index,
                "",
                accesibilityIdiOS
        );
    }

    public UICollection setAndroidCollection(String accessibilityId) {
        if (accessibilityId.contains(": ")) {
            return new TestUI(
                    null,
                    SelenideElement,
                    iOSElement,
                    index,
                    accessibilityId,
                    accesibilityIdiOS
            );
        }
        return new TestUI(
                null,
                SelenideElement,
                iOSElement,
                index,
                "accessibilityId: " + accessibilityId,
                accesibilityIdiOS
        );
    }

    public ElementsCollection getSelenideCollection() {
        return $$(SelenideElement);
    }

    private By getAppiumElement() {
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM))
            return iOSElement;
        return element;
    }

    private String getAccessibilityId() {
        if (Configuration.automationType.equals(Configuration.IOS_PLATFORM))
            return accesibilityIdiOS;
        return accesibilityId;
    }

    private List getElementList() {
        if (!getAccessibilityId().isEmpty()
                && !getAccessibilityId().split(": ")[0].isEmpty()) {
            switch (getAccessibilityId().split(": ")[0]) {
                case "accessibilityId":
                    return getDriver().
                            findElementsByAccessibilityId(
                                    getAccessibilityId().split(": ")[1]);
                case "className":
                    return getDriver().
                            findElementsByClassName(getAccessibilityId().split(": ")[1]);
                case "androidUIAutomator":
                    return getAndroidTestUIDriver().
                            findElementsByAndroidUIAutomator(
                                    getAccessibilityId().split(": ")[1]);
                case "predicate":
                    return getIOSTestUIDriver().
                            findElementsByIosNsPredicate(getAccessibilityId().split(": ")[1]);
                case "classChain":
                    return getIOSTestUIDriver().
                            findElementsByIosClassChain(getAccessibilityId().split(": ")[1]);
                case "name":
                    return getDriver().
                            findElementsByName(getAccessibilityId().split(": ")[1]);
                case "xpath":
                    return getDriver()
                            .findElementsByXPath(getAccessibilityId().split(": ")[1]);
                case "id":
                    return getDriver().findElementsById(getAccessibilityId().split(": ")[1]);
                case "css":
                    return getDriver()
                            .findElementsByCssSelector(getAccessibilityId().split(": ")[1]);
                default:
                    UIAssert("The type of locator is not valid! " +
                            getAccessibilityId().split(": ")[0],
                            false
                    );
                    return new ArrayList();
            }
        }
        return getDriver().findElements(getAppiumElement());
    }

    public String asString() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                return getElementList().get(index).toString();
            } else {
                return $$(SelenideElement).get(index).toString();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new Error(e);
        }
    }

    public UIElement get(int i) {
        return new Element(
                element,
                SelenideElement,
                iOSElement,
                i,
                true,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public UIElement first() {
        return new Element(
                element,
                SelenideElement,
                iOSElement,
                0,
                true,
                accesibilityId,
                accesibilityIdiOS
        );
    }

    public int size() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            try {
                return getElementList().size();
            } catch (Exception e) {
                return 0;
            }
        } else {
            try {
                return $$(SelenideElement).size();
            } catch (Throwable e) {
                takeScreenshotsAllure();
                throw new Error(e);
            }
        }
    }

    public Dimension getSize() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            try {
                return ((MobileElement) getElementList().get(index)).getSize();
            } catch (Exception e) {
                return new Dimension(0,0);
            }
        } else {
            try {
                return $$(SelenideElement).get(index).getSize();
            } catch (Throwable e) {
                takeScreenshotsAllure();
                throw new Error(e);
            }
        }
    }

    public UIElement findByVisible() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if (visible(getAppiumElement(), getAccessibilityId(), i)) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        } else {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if ($$(SelenideElement).get(i)
                            .is(com.codeborne.selenide.Condition.visible)) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        }
        takeScreenshotsAllure();
        throw new TestUIException(
                "No visible element with this selector: " +
                        element.toString()
        );
    }

    public UIElement findByText(String text) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if (containsText(getAppiumElement(), getAccessibilityId(), i, text)) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        } else {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if ($$(SelenideElement).get(i)
                            .is(com.codeborne.selenide.Condition.text(text))) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        }
        takeScreenshotsAllure();
        throw new TestUIException(
                "No visible element with that text '" +
                        text +
                        "' and this selector: " +
                        element.toString()
        );
    }

    public UIElement findByValue(String value) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if (containsAttribute(getAppiumElement(), getAccessibilityId(), i,
                            "value", value)) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        } else {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if ($$(SelenideElement).get(i)
                            .is(com.codeborne.selenide.Condition.value(value))) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        }
        takeScreenshotsAllure();
        throw new TestUIException(
                "No visible element with that value '" +
                        value +
                        "' and this selector: " +
                        element.toString()
        );
    }

    public UIElement findByEnabled() {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if (enable(getAppiumElement(), getAccessibilityId(), i)) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        } else {
            long t= System.currentTimeMillis();
            long end = t+(Configuration.timeout * 1000);
            while(System.currentTimeMillis() < end) {
                for (int i = 0; i < size(); i++) {
                    if ($$(SelenideElement).get(i).
                            is(com.codeborne.selenide.Condition.enabled)) {
                        return new Element(
                                element,
                                SelenideElement,
                                iOSElement,
                                i,
                                true,
                                accesibilityId,
                                accesibilityIdiOS
                        );
                    }
                }
            }
        }
        takeScreenshotsAllure();
        throw new TestUIException(
                "No enabled element with this selector: " +
                        element.toString()
        );
    }
}
