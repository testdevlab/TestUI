package pages;

import testUI.collections.UICollection;
import testUI.elements.UIElement;
import lombok.Data;

import static testUI.Utils.By.*;
import static testUI.collections.TestUI.EE;
import static testUI.collections.TestUI.EEx;
import static testUI.elements.TestUI.E;
import static testUI.elements.TestUI.Ex;

@Data
public class LandingPage {
    private UIElement catering = Ex("//android.widget.Button[@text=\"Catering\"]");
    private UICollection nearMeCollection
            = EE(byId("lv.lattelecombpo.yellowpages:id/label"));
    private UICollection suggestedCollection
            = EEx("//*[@resource-id='android:id/tabs']//*[@text='Suggested']");
    private UIElement map = E(byXpath("//*[@resource-id='android:id/tabs']//*[@text='Map']"));
    private UIElement fakeElement
            = E(byXpath("//*[@resource-id='android:id/tabs']//*[@text='XXXXX']"));
    private UIElement iosFirstElement = E(byName("First"));
    private UIElement iosSecondElement = E(byName("Second"));
}
