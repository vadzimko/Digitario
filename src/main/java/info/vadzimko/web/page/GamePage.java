package info.vadzimko.web.page;

import info.vadzimko.engine.Game;
import info.vadzimko.web.Exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class GamePage extends BasePage {
    public static Game game = new Game();

    public void action(HttpServletRequest request, Map<String, Object> view) {
        HttpSession session = request.getSession();
        view.put("Name", session.getAttribute("Name"));
        view.put("participating", game.getAnswers().containsKey((String) session.getAttribute("UserID")));
        view.put("number", session.getAttribute("Number"));
        view.put("lastWinner", game.getLastWinner());
        view.put("participants", game.getAnswers().size());

        if (game.isStarted()) {
            view.put("started", true);
            view.put("timer", game.timeToEndInSeconds());
        } else {
            view.put("started", false);
        }
    }

    public void addAnswer(HttpServletRequest request, Map<String, Object> view) {
        try {
            game.addAnswer(request);
        } catch (NumberFormatException e) {
            // no operation
        }
        throw new RedirectException("/game");
    }
}
