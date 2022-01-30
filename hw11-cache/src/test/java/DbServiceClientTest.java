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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orm.*;

import javax.sql.DataSource;

public class DbServiceClientTest {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(DbServiceClientTest.class);

    private final DataSource dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
    private final TransactionRunner transactionRunner = new TransactionRunnerJdbc(dataSource);
    private final DbExecutor dbExecutor = new DbExecutorImpl();
    private final EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
    private final EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
    private final DataTemplateJdbc<Client> dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);
    private final MyCache<Long, Client> myCache = new MyCache<>();

    private final DBServiceClient dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, myCache);

    @BeforeEach
    public void setUp() {
        flywayMigrations(dataSource);
    }

    @Test
    public void shouldClearCacheIfLackOfMemory() {
        int clientCount = 10_000;
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
