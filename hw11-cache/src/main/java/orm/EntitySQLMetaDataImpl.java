package orm;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entitySQLMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entitySQLMetaData) {
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + getEntityName();
    }

    @Override
    public String getSelectByIdSql() {
        var idField = entitySQLMetaData.getIdField();

        return "select * from " + getEntityName() + " where " + idField.getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        return "insert into "
                + getEntityName()
                + "("
                + getFieldsWithoutIdOrderByName()
                + ") values ("
                + getQuestionMarksBasedOnFieldsWithoutIdCount() + ")";
    }

    @Override
    public String getUpdateSql() {
        var idField = entitySQLMetaData.getIdField();
        return "update " + getEntityName() + " set " + getUpdateStatementBasedOnFieldsWithoutId() + " where " + idField.getName() + " = ?";
    }

    private String getEntityName() {
        return entitySQLMetaData.getName();
    }

    private String getUpdateStatementBasedOnFieldsWithoutId() {
        return entitySQLMetaData.getFieldsWithoutId()
                .stream()
                .sorted(Comparator.comparing(Field::getName))
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
    }

    private String getQuestionMarksBasedOnFieldsWithoutIdCount() {
        return entitySQLMetaData.getFieldsWithoutId()
                .stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
    }

    private String getFieldsWithoutIdOrderByName() {
        return entitySQLMetaData.getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
