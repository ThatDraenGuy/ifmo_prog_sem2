package collection.storage.database;

import collection.classes.Color;
import collection.classes.Dragon;
import collection.classes.MainCollectible;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.FieldModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.function.Function;

public class DatabaseHandler {
    private final Logger logger;
    private final Connection connection;
    private StatementCreator statementCreator;

    private PreparedStatement simpleQuery;

    public DatabaseHandler() throws SQLException {
        logger = LoggerFactory.getLogger("DatabaseHandler");
        statementCreator = new StatementCreator(this);
        Properties info = new Properties();
        info.put("user", "server");
        info.put("password", "aboba");
        //TODO config
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/progdb", info);
        connection.setAutoCommit(false);
        logger.info("Successfully established connection: " + connection);
        debug();
    }

    private void debug() throws SQLException {
        simpleQuery = connection.prepareStatement("SELECT * FROM dragon");
        ResultSet results = simpleQuery.executeQuery();
        results.next();
        logger.debug(Integer.valueOf(results.getInt(1)).toString());
        simpleQuery.close();
        PreparedStatement testStatement = connection.prepareStatement("INSERT INTO dragonCave (depth) VALUES (-20)");
        testStatement.executeUpdate();
        PreparedStatement preparedStatement = connection.prepareStatement("" +
                "WITH coordinates_id AS  (\n" +
                "                INSERT INTO coordinates (x,y) VALUES (?, ?)\n" +
                "                RETURNING coordinates_id\n" +
                "        ), dragoncave_id AS (\n" +
                "                INSERT INTO dragoncave (depth) VALUES (?)\n" +
                "                RETURNING cave_id\n" +
                "        ), dragon_id AS (\n" +
                "                INSERT INTO dragon (name, coordinates, creationdate, age, color, type, character, cave)\n" +
                "                VALUES (?, (SELECT coordinates_id FROM coordinates_id), ?, ?, (CAST(? AS color)), (CAST(? AS type)), (CAST (? AS dragoncharacter)),\n" +
                "                (SELECT cave_id FROM dragoncave_id))\n" +
                "                RETURNING id\n" +
                "        ) SELECT id FROM dragon_id");

        preparedStatement.setObject(1, 10);
        preparedStatement.setLong(2, 20);
        preparedStatement.setInt(3, 140);
        preparedStatement.setString(4, "Preparator2.0");
        preparedStatement.setString(5, "long time ago");
        preparedStatement.setLong(6, 10000);
        preparedStatement.setObject(7, "YELLOW");
        preparedStatement.setObject(8, "WATER");
        preparedStatement.setObject(9, "WISE");
        ResultSet testRes = preparedStatement.executeQuery();
        testRes.next();
        logger.debug(String.valueOf(testRes.getLong(1)));
        connection.commit();
        statementCreator.setTargetCollectibleScheme(new CollectibleScheme(Dragon.class));
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public long insert(CollectibleModel object) throws SQLException {
        connection.setSavepoint();
        int counter = 1;
        PreparedStatement insertStatement = statementCreator.getInsertStatement();
        insertModel(object, insertStatement, counter);
        ResultSet resultSet = insertStatement.executeQuery();
        resultSet.next();
        return resultSet.getLong(1);
    }

    private void insertModel(CollectibleModel model, PreparedStatement statement, int counter) throws SQLException {
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) {
                insertModel(fieldModel.getCollectibleModel(), statement, counter);
            }
        }
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) continue;
            if (fieldModel.getFieldData().getType().isEnum())
                statement.setObject(counter, fieldModel.getValue().toString());
            else statement.setObject(counter, fieldModel.getValue());
            counter++;
        }
    }
//    private long insert(CollectibleModel collectibleModel) throws SQLException {
//        if (!collectibleModel.getCollectibleScheme().equals(statementCreator.getTargetCollectibleScheme())) {
//            statementCreator.setTargetCollectibleScheme(collectibleModel.getCollectibleScheme());
//        } // TODO pulling and re-creating statements
//        ArrayList<String> commands = statementCreator.getInsertStatement();
//        connection.setSavepoint();
//        long result = insertCycle(collectibleModel, commands.iterator());
//        connection.commit();
//        return result;
//    }
//    private long insertCycle(CollectibleModel collectibleModel, Iterator<String> statementIterator) throws SQLException {
//        List<Long> collectibleIds = new LinkedList<>();
//        for (FieldModel field : collectibleModel.getValues().values()) {
//            if (field.getFieldData().isCollectible()) {
//                collectibleIds.add(insertCycle(field.getCollectibleModel(), statementIterator));
//            }
//        }
//        PreparedStatement preparedStatement = connection.prepareStatement(statementIterator.next());
//        int i = 1;
//        Iterator<Long> collectibleIdsIterator = collectibleIds.iterator();
//        for (FieldModel field : collectibleModel.getValues().values()) {
//            if (!field.getFieldData().isCollectible()) preparedStatement.setObject(i, field.getValue());
//            else preparedStatement.setLong(i, collectibleIdsIterator.next());
//        }
//        preparedStatement.executeUpdate();
//        preparedStatement.close();
//        PreparedStatement idGetterStatement = connection.prepareStatement(statementIterator.next());
//        ResultSet id = idGetterStatement.executeQuery();
//        if (id.next()) return id.getLong(1);
//        return 0; //TODO exception?
//    }


}
