package collection.storage.database;

import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StatementCreator {
    // statements:
    // - add (insert)
    // - remove_by_id
    // - update
    // - remove_lower
    // - remove_first
    // - clear
    @Getter
    private CollectibleScheme targetCollectibleScheme;
    @Getter
    private PreparedStatement insertStatement;
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
    }

    private void generateInsert() throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("WITH ");
        String finalString = genInsert(targetCollectibleScheme, builder);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" ").append(finalString);
        String result = builder.toString();
        System.out.println(result);
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
        builder.append(") RETURNING ").append(collectibleName).append("_id\n),");
        return "(SELECT " + collectibleName + "_id FROM " + collectibleName + "_id)";


//        for (FieldData fieldData : collectibleScheme.getFieldsData().values()) {
//            if (fieldData.isCollectible()) genInsert(fieldData.getCollectibleScheme());
//        }
//        StringBuilder builder = new StringBuilder();
//        builder.append("INSERT INTO").append(collectibleScheme.getSimpleName()).append(" (");
//        for (String column : collectibleScheme.getFieldsData().keySet()) {
//            builder.append(column).append(",");
//        }
//        builder.deleteCharAt(builder.length() - 1).append(") VALUES (");
//        builder.append("?,".repeat(collectibleScheme.getFieldsData().keySet().size()));
//        builder.deleteCharAt(builder.length() - 1).append(")");
//        insertStatement.add(builder.toString());
//        String idGetter = "SELECT last_value FROM "+collectibleScheme.getSimpleName()+"_"+collectibleScheme.getSimpleName()+"_id_seq";
//        insertStatement.add(idGetter);
        // TODO REDO for result like this:
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
