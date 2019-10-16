package pages;

import testUI.elements.UIElement;
import lombok.Data;

import static testUI.Utils.By.*;
import static testUI.elements.TestUI.E;

@Data
public class FacebookLandingPage {
    private UIElement safariFacebookEmailDiv = E(byMobileXpath("//*[@id=\"email_input_container" +
            "\"]"));
    private UIElement safariFacebookEmailInput = E(byMobileId("m_login_email"));
    private UIElement safariFacebookPasswordInput = E(byMobileId("m_login_password"));
}
