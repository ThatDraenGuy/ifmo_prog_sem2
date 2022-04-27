package collection.storage.database;

import collection.classes.Collectible;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StatementCreator {
    // statements:
    // + add (insert)
    // + remove_by_id
    // + update
    // + remove_lower
    // + remove_first
    // - clear
    // + load
    // + *init
    @Getter
    private CollectibleScheme targetCollectibleScheme;
    @Getter
    private PreparedStatement insertStatement;
    @Getter
    private PreparedStatement loadStatement;
    @Getter
    private PreparedStatement collectionIdGetterStatement;
    @Getter
    private PreparedStatement collectionIdUpdateStatement;
    @Getter
    private PreparedStatement removeByIdStatement;
    @Getter
    private PreparedStatement updateStatement;
    @Getter
    private PreparedStatement clearStatement;
    private final DatabaseHandler databaseHandler;


    public StatementCreator(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public void setTargetCollectibleScheme(CollectibleScheme collectibleScheme) throws SQLException {
        targetCollectibleScheme = collectibleScheme;
        generateStatements();
    }

    private void generateStatements() throws SQLException {
        generateClear();
        generateInsert();
        generateLoad();
        generateInit();
        generateRemoveById();
        generateUpdate();
        collectionIdGetterStatement = databaseHandler.prepareStatement("SELECT last_value FROM collection_id");
        collectionIdUpdateStatement = databaseHandler.prepareStatement("SELECT nextval('collection_id')");
    }

    private void generateClear() throws SQLException {
        String name = targetCollectibleScheme.getSimpleName();
        clearStatement = databaseHandler.prepareStatement("DELETE FROM " + name + " WHERE owner = ?");
    }


    private void generateUpdate() throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("WITH ");
        genUpdate(targetCollectibleScheme, builder, true, "", "");
        builder.deleteCharAt(builder.length() - 2);
        builder.append("(SELECT id FROM ").append(targetCollectibleScheme.getSimpleName()).append("_ids)");
        String result = builder.toString();
        updateStatement = databaseHandler.prepareStatement(result);
    }

    private void genUpdate(CollectibleScheme collectibleScheme, StringBuilder builder, boolean isFirst, String previousName, String fieldName) {
        builder.append(collectibleScheme.getSimpleName()).append("_ids AS (\n");
        builder.append("UPDATE ").append(collectibleScheme.getSimpleName()).append(" SET (");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (field.equals("id")) continue;
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (fieldData.isCollectible()) continue;
            builder.append(field).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") = (");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (field.equals("id")) continue;
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (fieldData.isCollectible()) continue;
            if (fieldData.getType().isEnum())
                builder.append("(CAST(? AS ").append(fieldData.getType().getSimpleName()).append("))");
            else builder.append("?");
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") WHERE id = ");
        if (isFirst) builder.append("? AND owner = ?");
        else builder.append("(SELECT ").append(fieldName).append(" FROM ").append(previousName).append("_ids)");
        builder.append(" RETURNING ");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (fieldData.isCollectible()) builder.append(field).append(",");
        }
        builder.append("id\n), ");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (fieldData.isCollectible())
                genUpdate(fieldData.getCollectibleScheme(), builder, false, collectibleScheme.getSimpleName(), field);
        }
    }

    private void generateRemoveById() throws SQLException {
        String name = targetCollectibleScheme.getSimpleName();
        removeByIdStatement = databaseHandler.prepareStatement("DELETE FROM " + name + " WHERE id = ? AND owner = ?");
    }

    private void generateInit() {
        StringBuilder builder = new StringBuilder();
        genInit(targetCollectibleScheme, builder);
    }

    private void genInit(CollectibleScheme collectibleScheme, StringBuilder builder) {
        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
            if (fieldData.isCollectible()) genInit(fieldData.getCollectibleScheme(), builder);
            else if (fieldData.getType().isEnum()) genEnumInit(fieldData.getType(), builder);
        }
        builder.append("CREATE TABLE ").append(collectibleScheme.getSimpleName()).append(" (\n");
        builder.append("id BIGSERIAL PRIMARY KEY,\n");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (field.equals("id")) continue;
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            builder.append(field).append(" ").append(getSqlType(fieldData.getType()));
            if (fieldData.isNotNull()) builder.append(" NOT NULL");
            if (fieldData.isLowerBounded())
                builder.append(" CHECK (").append(field).append(">").append(fieldData.getLowerBoundedValue()).append(")");
            if (fieldData.isCollectible())
                builder.append(" REFERENCES ").append(fieldData.getCollectibleScheme().getSimpleName()).append("(id)");
            builder.append(",\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append("\n);\n");
    }

    private <T> void genEnumInit(Class<T> target, StringBuilder builder) {
        builder.append("CREATE TYPE ").append(target.getSimpleName()).append(" AS ENUM (");
        for (T value : target.getEnumConstants()) {
            builder.append("'").append(value).append("',");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(");\n");
    }

    private String getSqlType(Class<?> target) {
        if (target.equals(int.class) || target.equals(Integer.class)) return "INT";
        else if (target.equals(long.class) || target.equals(Long.class) || Collectible.class.isAssignableFrom(target))
            return "BIGINT";
        else if (target.equals(String.class) || target.equals(ZonedDateTime.class)) return "VARCHAR(99)";
        else if (target.isEnum()) return target.getSimpleName();
        else return "";
    }

    private void generateLoad() throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(targetCollectibleScheme.getSimpleName());
        genLoad(targetCollectibleScheme, builder);
        String result = builder.toString();
        loadStatement = databaseHandler.prepareStatement(result);
    }

    private void genLoad(CollectibleScheme collectibleScheme, StringBuilder builder) {
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (fieldData.isCollectible()) {
                String name = fieldData.getSimpleName();
                builder.append(" JOIN ").append(name);
                builder.append(" on ").append(name).append(".id = ").append(collectibleScheme.getSimpleName()).append(".").append(field);
                genLoad(fieldData.getCollectibleScheme(), builder);
            }
        }
    }

    private void generateInsert() throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("WITH ");
        String finalString = genInsert(targetCollectibleScheme, builder);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" ").append(finalString);
        String result = builder.toString();
        insertStatement = databaseHandler.prepareStatement(result);
    }

    private String genInsert(CollectibleScheme collectibleScheme, StringBuilder builder) {
        List<String> idGetters = new ArrayList<>();
        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
            if (fieldData.isCollectible()) idGetters.add(genInsert(fieldData.getCollectibleScheme(), builder));
        }
        Iterator<String> idGetterIterator = idGetters.iterator();
        String collectibleName = collectibleScheme.getSimpleName();
        builder.append(collectibleName).append("_id AS (\nINSERT INTO ").append(collectibleName).append(" (");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (!field.equals("id")) builder.append(field).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") VALUES (");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (field.equals("id")) continue;
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (fieldData.isCollectible()) builder.append(idGetterIterator.next());
            else if (fieldData.getType().isEnum())
                builder.append("(CAST(? AS ").append(fieldData.getType().getSimpleName()).append("))");
            else builder.append("?");
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") RETURNING id\n),");
        return "(SELECT id FROM " + collectibleName + "_id)";
    }

}
