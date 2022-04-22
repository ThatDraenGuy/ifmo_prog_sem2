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

public class StatementCreator {
    // statements:
    // + add (insert)
    // - remove_by_id
    // - update
    // - remove_lower
    // - remove_first
    // - clear
    // +- load
    // + *init
    @Getter
    private CollectibleScheme targetCollectibleScheme;
    @Getter
    private PreparedStatement insertStatement;
    @Getter
    private PreparedStatement loadStatement;
    @Getter
    private final ArrayList<String> clearStatement;
    private final DatabaseHandler databaseHandler;


    public StatementCreator(DatabaseHandler databaseHandler) throws SQLException {
        this.databaseHandler = databaseHandler;
        clearStatement = new ArrayList<>();
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
    }

    private void generateInit() {
        StringBuilder builder = new StringBuilder();
        genInit(targetCollectibleScheme, builder);
        System.out.println(builder);
    }

    private void genInit(CollectibleScheme collectibleScheme, StringBuilder builder) {
        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
            if (fieldData.isCollectible()) genInit(fieldData.getCollectibleScheme(), builder);
            else if (fieldData.getType().isEnum()) genEnumInit(fieldData.getType(), builder);
        }
        builder.append("CREATE TABLE ").append(collectibleScheme.getSimpleName()).append(" (\n");
        builder.append("id BIGSERIAL PRIMARY KEY,\n");
        //TODO NOT NULL;
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            builder.append(field).append(" ").append(getSqlType(fieldData.getType()));
            if (fieldData.isNotNull()) builder.append(" NOT NULL");
            if (fieldData.isLowerBounded())
                builder.append(" CHECK (").append(field).append(">").append(fieldData.getLowerBoundedValue()).append(")");
            if (fieldData.isCollectible())
                builder.append(" REFERENCES ").append(fieldData.getCollectibleScheme().getSimpleName()).append("(id)");
            //TODO
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
        System.out.println(result);
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
        System.out.println(result);
        //TODO remove print
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
            builder.append(field).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") VALUES (");
        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
            if (fieldData.isCollectible()) builder.append(idGetterIterator.next());
            else if (fieldData.getType().isEnum())
                builder.append("(CAST(? AS ").append(fieldData.getType().getSimpleName()).append("))");
            else builder.append("?");
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(") RETURNING id\n),");
        return "(SELECT id FROM " + collectibleName + "_id)";
//        WITH coordinates_id AS  (
//                INSERT INTO coordinates (x,y) VALUES (13, 13)
//                RETURNING coordinates_id
//        ), dragoncave_id AS (
//                INSERT INTO dragoncave (depth) VALUES (140)
//                RETURNING cave_id
//        ), dragon_id AS (
//                INSERT INTO dragon (name, coordinates, creationdate, age, color, type, character, cave)
//                VALUES ('Smaug', (SELECT coordinates_id FROM coordinates_id), 'now', 10000, 'GREEN', 'WATER', 'WISE',
//                (SELECT cave_id FROM dragoncave_id))
//                RETURNING id
//        ) SELECT id FROM dragon_id
    }


    private void generateClear() {
        clearStatement.clear();
        genClear(targetCollectibleScheme);
    }

    private void genClear(CollectibleScheme collectibleScheme) {
        clearStatement.add("TRUNCATE TABLE " + collectibleScheme.getSimpleName());
        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
            if (fieldData.isCollectible()) genClear(fieldData.getCollectibleScheme());
        }
    }
}
