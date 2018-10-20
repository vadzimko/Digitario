package info.vadzimko.web.filter;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import info.vadzimko.engine.FBGraph;
import info.vadzimko.engine.FacebookEngine;
import info.vadzimko.engine.Game;
import info.vadzimko.engine.VKEngine;
import info.vadzimko.web.page.GamePage;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class LoginFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();

        if (session.getAttribute("Logged") == null) {
            String uri = request.getRequestURI();
            if (uri.endsWith("/loginVK")) {
                response.sendRedirect(VKEngine.OAUTH_URI);
            } else if (uri.endsWith("/loginFB")) {
                response.sendRedirect(FacebookEngine.AUTH_URI);
            } else if (request.getRequestURI().startsWith("/VKlogin/callback")) {
                logInFromVK(request, response);
            } else if (request.getRequestURI().startsWith("/FBlogin/callback")) {
                logInFromFB(request, response);
            } else chain.doFilter(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    private void logInFromVK(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String code = request.getParameter("code");
        UserActor actor;
        try {
            actor = VKEngine.getActor(code);
        } catch (ServletException e) {
            response.sendRedirect("/index.html");
            return;
        }
        session.setAttribute("Logged", true);
        try {
            UserXtrCounters user = VKEngine.vk.users().get(actor).execute().get(0);
            String name = user.getFirstName() + " " + user.getLastName();
            session.setAttribute("Name", name);

            String userID = "0" + actor.getId();
            session.setAttribute("UserID", userID);
            if (GamePage.game.getNames().containsKey(userID)) {
                session.setAttribute("Number", GamePage.game.getAnswers().get(userID));
            }
        } catch (ApiException | ClientException e) {
            session.setAttribute("Name", "Unknown");
        }
        response.sendRedirect("/game");
    }

    private void logInFromFB(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        if (code == null || code.equals("")) {
            response.sendRedirect("/");
            return;
        }
        try {
            FacebookEngine fbConnection = new FacebookEngine();
            String accessToken = fbConnection.getAccessToken(code);

            FBGraph fbGraph = new FBGraph(accessToken);
            String graph = fbGraph.getFBGraph();
            Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
            HttpSession session = request.getSession();

            session.setAttribute("Logged", true);
            session.setAttribute("Name", fbProfileData.get("name"));

            String userID = "1" + fbProfileData.get("id");
            session.setAttribute("UserID", userID);
            if (GamePage.game.getNames().containsKey(userID)) {
                session.setAttribute("Number", GamePage.game.getAnswers().get(userID));
            }
            response.sendRedirect("/game");
        } catch (ServletException e) {
            response.sendRedirect("/");
        }
    }
}
