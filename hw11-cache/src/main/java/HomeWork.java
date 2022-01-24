import cache.HwListener;
import cache.MyCache;
import core.repository.executor.DbExecutor;
import core.repository.executor.DbExecutorImpl;
import core.sessionmanager.TransactionRunner;
import core.sessionmanager.TransactionRunnerJdbc;
import crm.datasource.DriverManagerDataSource;
import crm.model.Client;
import crm.service.DBServiceClient;
import crm.service.DbServiceClientImpl;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orm.*;

import javax.sql.DataSource;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        DataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        TransactionRunner transactionRunner = new TransactionRunnerJdbc(dataSource);
        DbExecutor dbExecutor = new DbExecutorImpl();
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        DataTemplateJdbc<Client> dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);
        MyCache<Long, Client> myCache = new MyCache<>();
        DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, myCache);
        flywayMigrations(dataSource);

        var listener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        myCache.addListener(listener);

        int clientCount = 1_000;
        for (int i = 0; i < clientCount; i++) {
            var client = new Client("name " + i);
            dbServiceClient.saveClient(client);
        }
        dbServiceClient.getClient(1);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
