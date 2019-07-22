package testUI.elements;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;
import testUI.Configuration;

import static testUI.Utils.WaitUntil.waitUntilHasAttribute;

public class AttributeImp extends TestUI implements testUI.elements.Attribute {
    private By AppiumElement;
    private By SelenideElement;
    private By iOSElement;
    private int time;
    private String Attribute;
    private boolean is;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;

    protected AttributeImp(By AppiumElement,
                           By SelenideElement,
                           By iOSElement,
                           int index,
                           boolean collection,
                           String accesibilityId,
                           String accesibilityIdiOS,
                           String Attribute,
                           int time,
                           boolean is) {
        this.AppiumElement = AppiumElement;
        this.SelenideElement = SelenideElement;
        this.iOSElement = iOSElement;
        this.time = time;
        this.Attribute = Attribute;
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

    public AttributeImp not() {
        return new AttributeImp(AppiumElement, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS, Attribute,
                time,false);
    }

    public UIElement equalTo(String value) {
        if (Configuration.deviceTests) {
            if (collection) {
                waitUntilHasAttribute(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId),
                        index, time,
                        Attribute, value
                        , is);
            } else {
                waitUntilHasAttribute(getAppiumElement(iOSElement, AppiumElement), getAccesibilityId(accesibilityIdiOS, accesibilityId), time,
                        Attribute, value
                        , is);
            }
        } else {
            selenideAssert(condition(is, Condition.attribute(Attribute, value)), time, SelenideElement, index, collection);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement,index, collection, accesibilityId, accesibilityIdiOS);
    }
}
