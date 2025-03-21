package pages;

import testUI.elements.UIElement;
import lombok.Data;

import static testUI.Utils.By.*;
import static testUI.elements.TestUI.E;

@Data
public class GoogleLandingPage {
    private UIElement googleSearch = E(byXpath("//button[@class='Tg7LZd']"))
            .setSelenideElement(byCssSelector("[aria-label=\"Google Search\"]"))
            .setiOSElement(byId("id"));
    private UIElement googleSearchInput = E(byCssSelector("form[role=\"search\"] textarea"))
            .setSelenideElement(byCssSelector("form[role=\"search\"] textarea"));
    private UIElement googleCookies = E(byText("I agree"))
            .setSelenideElement(byText("I agree"));
}
