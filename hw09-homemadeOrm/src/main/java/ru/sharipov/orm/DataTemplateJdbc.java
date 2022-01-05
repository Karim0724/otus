package ru.sharipov.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sharipov.core.repository.DataTemplate;
import ru.sharipov.core.repository.executor.DbExecutor;
import ru.sharipov.core.sessionmanager.DataBaseOperationException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                Collections.singletonList(id),
                rs -> {
                    try {
                        if (rs.next()) {
                            return createInstance(rs);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectAllSql(),
                Collections.emptyList(),
                rs -> {
                    List<T> entities = new ArrayList<>();
                    try {
                        while (rs.next()) {
                            T instance = createInstance(rs);
                            entities.add(instance);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    return entities;
                }).orElseThrow();
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> params = new ArrayList<>();
        entityClassMetaData.getFieldsWithoutId()
                .stream()
                .sorted(Comparator.comparing(Field::getName))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        var value = field.get(client);
                        params.add(value);
                    } catch (IllegalAccessException e) {
                        throw new DataBaseOperationException("An exception has occurred while getting field value", e);
                    }

                });
        return dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getInsertSql(),
                params
        );
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> params = new ArrayList<>();
        entityClassMetaData.getFieldsWithoutId()
                .stream()
                .sorted(Comparator.comparing(Field::getName))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        var value = field.get(client);
                        params.add(value);
                    } catch (IllegalAccessException e) {
                        throw new DataBaseOperationException("An exception has occurred while getting field value. FieldName: " + field.getName(), e);
                    }

                });
        Field idField = entityClassMetaData.getIdField();
        idField.setAccessible(true);
        try {
            params.add(idField.get(client));
        } catch (IllegalAccessException e) {
            throw new DataBaseOperationException("An exception has occurred while getting id field value", e);
        }
        dbExecutor.executeStatement(
                connection,
                entitySQLMetaData.getUpdateSql(),
                params
        );
    }

    private T createInstance(ResultSet rs) {
        try {
            T instance = entityClassMetaData.getConstructor().newInstance();
            setDataToEntity(instance, rs);
            return instance;
        } catch (Exception e) {
            throw new DataBaseOperationException("Can't create instance", e);
        }
    }

    private void setDataToEntity(T entity, ResultSet data) {
        entityClassMetaData.getAllFields().forEach(field -> {
            try {
                var value = data.getObject(field.getName());
                field.setAccessible(true);
                field.set(entity, value);
            } catch (SQLException | IllegalAccessException e) {
                throw new DataBaseOperationException("An exception has occurred while setting data to instance: " + entity.getClass().getSimpleName(), e);
            }
        });
    }
}
