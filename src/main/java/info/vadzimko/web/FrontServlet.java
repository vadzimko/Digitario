package info.vadzimko.web;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import info.vadzimko.web.Exception.RedirectException;
import info.vadzimko.web.page.RulesPage;
import info.vadzimko.web.page.LoginPage;
import info.vadzimko.web.page.GamePage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FrontServlet extends HttpServlet {
    private static final String BASE_PAGE_NAME = FrontServlet.class.getName().substring(0,
            FrontServlet.class.getName().length() - FrontServlet.class.getSimpleName().length()) + "page";
    private Configuration sourceFreemarkerConfiguration;
    private Configuration targetFreemarkerConfiguration;

    private Method beforeMethod;
    private Method afterMethod;

    private Configuration newConfiguration(File templateDirectory) throws ServletException {
        Configuration freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_28);
        try {
            freemarkerConfiguration.setDirectoryForTemplateLoading(templateDirectory);
        } catch (IOException e) {
            throw new ServletException(e);
        }
        freemarkerConfiguration.setDefaultEncoding("UTF-8");
        freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfiguration.setLogTemplateExceptions(false);
        freemarkerConfiguration.setWrapUncheckedExceptions(true);
        return freemarkerConfiguration;
    }

    @Override
    public void init() throws ServletException {
        sourceFreemarkerConfiguration = newConfiguration(
                new File(FrontServlet.class.getProtectionDomain().getCodeSource().getLocation().getPath(),
                "../templates"));
        targetFreemarkerConfiguration = newConfiguration(new File(getServletContext().getRealPath("WEB-INF/templates")));
        try {
            beforeMethod = info.vadzimko.web.page.BasePage.class.getDeclaredMethod("before", HttpServletRequest.class, Map.class);
            afterMethod = info.vadzimko.web.page.BasePage.class.getDeclaredMethod("after", HttpServletRequest.class, Map.class);
        } catch (NoSuchMethodException e) {
            throw new ServletException("Cannot find methods of class BasePage");
        }
    }

    private Template newTemplate(String templateName) throws ServletException {
        Template template = null;
        try {
            template = sourceFreemarkerConfiguration.getTemplate(templateName);
        } catch (IOException ignored) {
            // No operations.
        }
        if (template == null) {
            try {
                template = targetFreemarkerConfiguration.getTemplate(templateName);
            } catch (IOException e) {
                throw new ServletException(e);
            }
        }
        return template;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Route route = findRoute(request);

        try {
            processRoute(route, request, response);
        } catch (NotFoundException e) {
            response.sendRedirect("/");
        }
    }

    private void processRoute(Route route, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, ServletException, IOException {
        Class<?> clazz = route.newClass();
        Method method = null;
        for (Class<?> c = clazz; method == null && c != null; c = c.getSuperclass()) {
            Method[] declaredMethods = c.getDeclaredMethods();
            int i = 0;
            for (; i < declaredMethods.length && !declaredMethods[i].getName().equals(route.getAction()); i++) {
            }
            if (i < declaredMethods.length) {
                method = declaredMethods[i];
            }
        }
        if (method == null) {
            throw new NotFoundException();
        }

        Object page;
        try {
            page = clazz.newInstance();
        } catch (Exception e) {
            throw new ServletException("Can't create page instance [clazz=" + clazz + "].", e);
        }

        Map<String, Object> view = new HashMap<>();
        try {
            beforeMethod.invoke(page, request, view);
            method.setAccessible(true);
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 2) {
                if (parameterTypes[0] == HttpServletRequest.class && parameterTypes[1] == Map.class) {
                    method.invoke(page, request, view);
                } else if (parameterTypes[0] == HttpServletRequest.class && parameterTypes[1] == HttpServletResponse.class) {
                    method.invoke(page, request, response);
                }
            } else if (parameterTypes.length == 1) {
                if (parameterTypes[0] == HttpServletRequest.class) {
                    method.invoke(page, request);
                } else if (parameterTypes[0] == Map.class) {
                    method.invoke(page, view);
                }
            } else {
                method.invoke(page);
            }
            afterMethod.invoke(page, request, view);
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            if (throwable instanceof RedirectException) {
                RedirectException redirectException = (RedirectException) throwable;
                String action = redirectException.getAction();
                if (action == null) {
                    response.sendRedirect(redirectException.getUrl());
                } else {
                    response.sendRedirect(redirectException.getUrl() + "?action=" + action);
                }
                return;
            }
            throw new ServletException("Can't run page method [clazz=" + clazz + ", method=" + method + "].", e);
        } catch (Exception e) {
            throw new ServletException("Can't run page method [clazz=" + clazz + ", method=" + method + "].", e);
        }

        try {
            Template template = newTemplate(clazz.getSimpleName() + ".ftlh");
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            template.process(view, writer);
            writer.flush();
        } catch (TemplateException e) {
            throw new ServletException(e);
        }
    }

    private Route findRoute(HttpServletRequest request) {
        if (request.getSession().getAttribute("Logged") == null) {
            return Route.getLoginPageRoute();
        }
        String requestURI = request.getRequestURI();

        if (requestURI.length() <= 1) {
            return Route.getGamePageRoute();
        }

        StringBuilder className = new StringBuilder(BASE_PAGE_NAME);
        Arrays.stream(requestURI.split("/")).filter(s -> !s.isEmpty())
                .forEach(s -> {
                    className.append('.');
                    className.append(s);
                });
        int pos = className.lastIndexOf(".") + 1;
        className.setCharAt(pos, Character.toUpperCase(className.charAt(pos)));

        String action = request.getParameter("action");
        if (action != null && action.isEmpty()) {
            action = null;
        }

        return new Route(className + "Page", action);
    }

    private static final class Route {
        private final String className;
        private final String action;

        private Route(String className, String action) {
            this.className = className;
            this.action = action;
        }

        private static Route getGamePageRoute() {
            return new Route(GamePage.class.getName(), null);
        }

        private static Route getRulesPageRoute() {
            return new Route(RulesPage.class.getName(), null);
        }

        private static Route getLoginPageRoute() {
            return new Route(LoginPage.class.getName(), null);
        }

        private Class<?> newClass() throws NotFoundException {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new NotFoundException();
            }
        }

        private String getAction() {
            return action != null ? action : "action";
        }
    }

    private static final class NotFoundException extends Exception {
    }
}
