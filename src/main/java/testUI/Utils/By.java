package testUI.Utils;

import com.codeborne.selenide.Selectors;
import testUI.Configuration;

public class By {

    public static org.openqa.selenium.By byText(String text) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.xpath("//*[contains(@text,'" + text + "')]");
        return Selectors.byText(text);
    }

    public static org.openqa.selenium.By byId(String id) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.id(id);
        return Selectors.byId(id);
    }

    public static org.openqa.selenium.By byXpath(String xpath) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.xpath(xpath);
        return Selectors.byXpath(xpath);
    }

    public static org.openqa.selenium.By byCssSelector(String cssSelector) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.cssSelector(cssSelector);
        return Selectors.byCssSelector(cssSelector);
    }

    public static org.openqa.selenium.By byName(String name) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.name(name);
        return Selectors.byName(name);
    }

    public static org.openqa.selenium.By byAttribute(String attribute, String value) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.xpath("//*[contains(@" + attribute + ",'" + value + "']");
        return Selectors.byAttribute(attribute, value);
    }

    public static org.openqa.selenium.By byValue(String value) {
        if (Configuration.deviceTests)
            return org.openqa.selenium.By.xpath("//*[contains(@value,'" + value + "']");
        return Selectors.byValue(value);
    }

    public String byAccesibilityId(String value) {
        return value;
    }
}
