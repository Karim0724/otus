package ru.sharipov.server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.sharipov.services.DbUserService;
import ru.sharipov.services.TemplateProcessor;
import ru.sharipov.services.UserAuthService;
import ru.sharipov.servlet.AuthorizationFilter;
import ru.sharipov.servlet.LoginServlet;

import java.util.Arrays;

public class MyUserWebService extends UsersWebServerSimple {
    private final UserAuthService authService;

    public MyUserWebService(int port, DbUserService userDao, Gson gson, TemplateProcessor templateProcessor, UserAuthService authService) {
        super(port, userDao, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;

    }
}
