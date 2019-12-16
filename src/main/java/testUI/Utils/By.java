package testUI.Utils;

import com.codeborne.selenide.Selectors;
import testUI.Configuration;

import static testUI.TestUIDriver.getDriver;

public class By {

    public static org.openqa.selenium.By byText(String text) {
        if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM))
            if (!getDriver().isBrowser())
                return org.openqa.selenium.By.xpath("//*[contains(@text,'" + text + "')]");
        return Selectors.byText(text);
    }

    public static org.openqa.selenium.By byId(String id) {
        return Selectors.byId(id);
    }

    public static org.openqa.selenium.By byXpath(String xpath) {
        return Selectors.byXpath(xpath);
    }

    public static org.openqa.selenium.By byCssSelector(String cssSelector) {
        return Selectors.byCssSelector(cssSelector);
    }

    public static org.openqa.selenium.By byName(String name) {
        return Selectors.byName(name);
    }

    public static org.openqa.selenium.By byAttribute(String attribute, String value) {
        return Selectors.byAttribute(attribute, value);
    }

    public static org.openqa.selenium.By byValue(String value) {
        return Selectors.byValue(value);
    }

    public static String byAccesibilityId(String value) {
        return "accessibilityId: " + value;
    }

    public static String byiOSPredicate(String value) {
        return "predicate: " + value;
    }

    public static String byAndroidUIAutomator(String value) {
        return "androidUIAutomator: " + value;
    }

    public static String byClassChain(String value) {
        return "classChain: " + value;
    }

    public static String byMobileClassName(String value) {
        return "className: " + value;
    }

    public static String byMobileXpath(String value) {
        return "xpath: " + value;
    }

    public static String byMobileId(String value) {
        return "id: " + value;
    }

    public static String byMobileCss(String value) {
        return "css: " + value;
    }

    public static String byMobileName(String value) {
        return "name: " + value;
    }
}
