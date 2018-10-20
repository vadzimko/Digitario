package info.vadzimko.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static info.vadzimko.engine.Game.Attempt;

public class RulesPage extends BasePage {

    private static List<Attempt> example1 = new ArrayList<>(Arrays.asList(
            new Attempt("Donald Trump", 1L),
            new Attempt("Vladimir Putin", 1L),
            new Attempt("Albert Einstein", 3L)));

    private static List<Attempt> example2 = new ArrayList<>(Arrays.asList(
            new Attempt("Tom", 1L),
            new Attempt("Daniel", 2L)));

    public void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("example1", example1);
        view.put("example2", example2);
    }

}
