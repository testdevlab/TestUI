package testUI.collections;

import static testUI.Utils.By.*;
import static testUI.collections.UICollection.EE;

public class TheCollection {

    public static class Given {
        public static Given given() {
            return new Given();
        }

        public TheCollection aCollection() {
            return new TheCollection();
        }
    }

    public UICollection withText(String text) {
        return EE(byText(text));
    }

    public UICollection withId(String id) {
        return EE(byId(id));
    }

    public UICollection withValue(String value) {
        return EE(byValue(value));
    }

    public UICollection withXpath(String xpath) {
        return EE(byXpath(xpath));
    }
}
