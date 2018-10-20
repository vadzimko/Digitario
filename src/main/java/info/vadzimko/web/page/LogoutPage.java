package info.vadzimko.web.page;

import info.vadzimko.web.Exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LogoutPage extends BasePage {

    public void action(HttpServletRequest request, Map<String, Object> view) {
        request.getSession().removeAttribute("UserID");
        request.getSession().removeAttribute("Logged");
        request.getSession().removeAttribute("Number");

        throw new RedirectException("/");
    }
}
