package info.vadzimko.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class LastGamePage extends BasePage {

    public void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", GamePage.game.getLastAnswers());
    }

}
