package testUI.elements;

public interface Asserts {

    Asserts not();

    UIElement visible();

    UIElement enabled();

    UIElement exists();

    UIElement value(String text);

    Attribute attribute(String Attribute);

    UIElement theAttribute(String Attribute);

    UIElement exactText(String text);

    UIElement containNoCaseSensitiveText(String text);

    UIElement containText(String text);

    UIElement emptyText();

    UIElement emptyAttribute(String Attribute);

    UIElement currentUrlEqualTo(String expectedUrl);

    UIElement currentUrlContains(String expectedUrl);
}
