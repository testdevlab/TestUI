package pages;

import testUI.elements.UIElement;
import lombok.Data;

import static testUI.Utils.By.byId;
import static testUI.Utils.By.byXpath;
import static testUI.elements.TestUI.E;

@Data
public class FacebookLandingPage {
    private UIElement safariFacebookEmailDiv = E(byXpath("//*[@id=\"email_input_container\"]"));
    private UIElement safariFacebookEmailInput = E(byId("m_login_email"));
    private UIElement safariFacebookPasswordInput = E(byId("m_login_password"));
}
