package info.vadzimko.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class BasePage {
    public void before(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    public void after(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }
}
