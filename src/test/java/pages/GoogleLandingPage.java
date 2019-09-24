package pages;

import testUI.elements.UIElement;
import lombok.Data;

import static testUI.Utils.By.*;
import static testUI.elements.TestUI.E;

@Data
public class GoogleLandingPage {
    private UIElement googleSearch = E(byXpath("//button[@class='Tg7LZd']"))
                                                                                    .setSelenideElement(byName("btnK"))
                                                                                    .setiOSElement(byId("id"));
    private UIElement googleSearchInput = E(byMobileName("q")).setSelenideElement(byName("q"));
}
