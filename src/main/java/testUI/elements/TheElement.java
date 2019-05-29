package testUI.elements;

import static testUI.Utils.By.*;

public class TheElement {

    public static TheElement theElement() {
        return new TheElement();
    }

    public UIElement withText(String text) {
        return TestUI.E(byText(text));
    }

    public UIElement withId(String id) {
        return TestUI.E(byId(id));
    }

    public UIElement withValue(String value) {
        return TestUI.E(byValue(value));
    }

    public UIElement withXpath(String xpath) {
        return TestUI.E(byXpath(xpath));
    }
}
