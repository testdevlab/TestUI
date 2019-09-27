package testUI.elements;

public interface WaitAsserts {

    UIElement untilIsVisible();

    UIElement untilIsEnabled();

    UIElement untilExists();

    UIElement untilHasText(String text);

    Element untilHasCaseNotSensitiveText(String text);

    UIElement untilHasValue(String value);

    UIElement untilNotVisible();

    UIElement untilNotExists();

    UIElement untilNotEnabled();

    UIElement untilHasNotText(String text);

    UIElement untilHasNotValue(String value);

    Attribute untilHasAttribute(String Attribute);

    Attribute untilNotHasAttribute(String Attribute);
}
