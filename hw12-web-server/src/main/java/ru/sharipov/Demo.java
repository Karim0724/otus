package ru.sharipov;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.cfg.Configuration;
import ru.sharipov.dao.repository.DataTemplateHibernate;
import ru.sharipov.dao.repository.HibernateUtils;
import ru.sharipov.dao.sessionmanager.TransactionManagerHibernate;
import ru.sharipov.dbmigrations.MigrationsExecutorFlyway;
import ru.sharipov.model.User;
import ru.sharipov.server.MyUserWebService;
import ru.sharipov.server.UsersWebServer;
import ru.sharipov.services.*;

public class Demo {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var userTemplate = new DataTemplateHibernate<User>(User.class);
        var dbUserService = new DbUserServiceImpl(userTemplate, transactionManager);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UserAuthService userAuthService = new UserAuthServiceImpl(dbUserService);
        UsersWebServer usersWebServer = new MyUserWebService(
                WEB_SERVER_PORT,
                dbUserService,
                gson,
                templateProcessor,
                userAuthService
        );

        usersWebServer.start();
        usersWebServer.join();
    }
}
