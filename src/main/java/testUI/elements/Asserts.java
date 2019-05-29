package testUI.elements;

public interface Asserts {

    ShouldBe not();

    UIElement visible();

    UIElement enabled();

    UIElement exists();

    UIElement value(String text);

    AttributeImp attribute(String Attribute);

    UIElement theAttribute(String Attribute);

    UIElement untilIsVisible();

    UIElement untilIsEnabled();

    UIElement untilExists();

    UIElement untilHasText(String text);

    UIElement untilHasValue(String value);

    UIElement untilNotVisible();

    UIElement untilNotExists();

    UIElement untilNotEnabled();

    UIElement untilHasNotText(String text);

    UIElement untilHasNotValue(String value);

    AttributeImp untilHasAttribute(String Attribute);

    AttributeImp untilNotHasAttribute(String Attribute);

    UIElement exactText(String text);

    UIElement containNoCaseSensitiveText(String text);

    UIElement containText(String text);
}
