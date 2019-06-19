package testUI.elements;

import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import testUI.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static testUI.TestUIDriver.getDriver;
import static testUI.Utils.AppiumHelps.visible;

public class Scrolling extends TestUI {
    private By AppiumElement;
    private By iOSElement;
    private By SelenideElement;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;

    protected Scrolling(By AppiumElement,
                       By SelenideElement,
                       By iOSElement,
                       int index,
                       boolean collection,
                       String accesibilityId,
                       String accesibilityIdiOS) {
        this.AppiumElement = AppiumElement;
        this.iOSElement = iOSElement;
        this.SelenideElement = SelenideElement;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
        this.index = index;
        this.collection = collection;
    }

    public UIElement view() {
        if (Configuration.deviceTests) {
            for (int i = 0; i < 20; i++) {
                TouchAction action = new TouchAction(getDriver());
                action.press(PointOption.point(40, 500)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
                        .moveTo(PointOption.point(40, 100)).release().perform();
                if (visible(getAppiumElement(iOSElement,AppiumElement),getAccesibilityId(accesibilityIdiOS,accesibilityId))) {
                    break;
                }
            }
        } else {
            getSelenide(SelenideElement,index, collection).scrollIntoView(true);
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }

    public UIElement click() {
        if (Configuration.deviceTests) {
            for (int i = 0; i < 20; i++) {
                try {
                    getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                    getElementWithoutException(accesibilityIdiOS, accesibilityId, iOSElement, AppiumElement, index, collection).click();
                    break;
                } catch (Throwable e) {
                    System.out.println("hey");
                    TouchAction action = new TouchAction(getDriver());
                    action.press(PointOption.point(40, 500)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
                            .moveTo(PointOption.point(40, 200)).release().perform();
                    // Nothing got clicked
                }
            }
        } else {
            getSelenide(SelenideElement,index, collection).scrollIntoView(true).click();
        }
        return new UIElement(AppiumElement, SelenideElement, iOSElement, index, collection, accesibilityId, accesibilityIdiOS);
    }
}
