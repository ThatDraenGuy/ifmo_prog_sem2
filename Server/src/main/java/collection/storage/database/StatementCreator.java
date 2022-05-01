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

/**
 * Class that handles all statements needed to work with database.
 * WARNING: NOT OPTIMIZED AT ALL. This class could create all statements using, like, 300 less for-loops.
 * However, since it realistically only runs once on server initialization, its optimization has low priority
 * (which means it probably won't be optimized)
 */
public class StatementCreator {
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
    @Getter
    private PreparedStatement initStatement;
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

    private void generateInit() throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("""
                DROP SEQUENCE is exists collection_id;
                CREATE SEQUENCE collection_id;
                DROP TYPE is exists accessLevel;
                CREATE TYPE accessLevel AS ENUM ('DISCONNECTED', 'GUEST', 'USER', 'INTERNAL', 'DEV');
                DROP TABLE is exists accounts;
                CREATE TABLE accounts (
                    username VARCHAR(99) PRIMARY KEY,
                    passwordHash CHAR(128) NOT NULL,
                    salt VARCHAR(10) NOT NULL,
                    accessLevel accessLevel NOT NULL
                );
                INSERT INTO accounts VALUES ('server', 'e7a3b24fdb51f80d3de64c1c58920d15dc7e6bbf724f475d866d3ffe5d3d6b113a2367a662691c14fa6bcb7026ce9ed5ef3388b086ff693592ec7b1e5fd0dd89',
                'j()&b4g,>6', (CAST('DEV' AS accessLevel)));
                INSERT INTO accounts VALUES ('guest', 'bc7f52843f7e8e48baa45792211be60212688ff42f89b3aa6a85b3ef9333be6abbd337b9d24f0f68acaecc20cb4ecc2f726c8c83ea474556e23eac42ab31a428',
                'j()&b4g,>6', (CAST('GUEST' AS accessLevel)));
                                
                """);
        genInit(targetCollectibleScheme, builder);
        initStatement = databaseHandler.prepareStatement(builder.toString());
    }

    private void genInit(CollectibleScheme collectibleScheme, StringBuilder builder) {
        builder.append("DROP TABLE is exists ").append(collectibleScheme.getSimpleName()).append(";\n");
        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
            if (fieldData.isCollectible()) genInit(fieldData.getCollectibleScheme(), builder);
            else if (fieldData.getType().isEnum()) genEnumInit(fieldData.getType(), builder);
        }
        builder.append("CREATE TABLE ").append(collectibleScheme.getSimpleName()).append(" (\n");
        builder.append("id BIGSERIAL PRIMARY KEY,\n");
        builder.append("owner VARCHAR(99) NOT NULL REFERENCES accounts(username),\n");
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            if (field.equals("id") || field.equals("owner")) continue;
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
        builder.append("DROP TYPE if exists ").append(target.getSimpleName());
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
