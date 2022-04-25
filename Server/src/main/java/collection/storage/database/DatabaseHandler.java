package collection.storage.database;

import collection.classes.Dragon;
import collection.meta.*;
import collection.storage.StorageHandler;
import exceptions.StorageException;
import exceptions.ValueNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.ArrayListWithID;
import utility.CollectionWithID;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.*;

public class DatabaseHandler implements StorageHandler {
    private int counter;
    private final Logger logger;
    private final Connection connection;
    private StatementCreator statementCreator;

    private PreparedStatement simpleQuery;

    public DatabaseHandler() throws SQLException {
        counter = 1;
        logger = LoggerFactory.getLogger("DatabaseHandler");
        statementCreator = new StatementCreator(this);
        Properties info = new Properties();
        info.put("user", "server");
        info.put("password", "aboba");
        //TODO config
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/progdb", info);
        connection.setAutoCommit(false);
        logger.info("Successfully established connection: " + connection);
    }

    private void debug() throws SQLException {
        simpleQuery = connection.prepareStatement("SELECT * FROM dragon");
        ResultSet results = simpleQuery.executeQuery();
        if (results.next()) logger.debug(Integer.valueOf(results.getInt(1)).toString());
        simpleQuery.close();
        PreparedStatement testStatement = connection.prepareStatement("INSERT INTO dragonCave (depth) VALUES (-20)");
        testStatement.executeUpdate();
        statementCreator.setTargetCollectibleScheme(new CollectibleScheme(Dragon.class));
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
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public boolean update(long id, CollectibleModel object) throws StorageException {
        try {
            connection.setSavepoint();
            counter = 1;
            PreparedStatement updateStatement = statementCreator.getUpdateStatement();
            updateModel(id, object, updateStatement);
            //TODO remove
            System.out.println(updateStatement);
            ResultSet resultSet = updateStatement.executeQuery();
            boolean res = resultSet.next();
            updateCollectionId();
            return res;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void updateModel(long id, CollectibleModel model, PreparedStatement statement) throws SQLException {
        inputValues(model, statement);
        if (id != 0) {
            statement.setLong(counter, id);
            counter++;
        }
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) updateModel(0, fieldModel.getCollectibleModel(), statement);
        }
    }

    private void inputValues(CollectibleModel model, PreparedStatement statement) throws SQLException {
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) continue;
            Object value = fieldModel.getValue();
            Class<?> type = fieldModel.getFieldData().getType();
            if (type.isEnum() || ZonedDateTime.class.isAssignableFrom(type)) {
                if (value != null) statement.setObject(counter, value.toString());
                else statement.setObject(counter, null);
            } else statement.setObject(counter, value);
            counter++;
        }
    }

    @Override
    public void removeABunch(Collection<Long> ids) throws StorageException {
        try {
            connection.setSavepoint();
            for (long id : ids) remove(id);
            updateCollectionId();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public void removeById(long id) throws StorageException {
        try {
            connection.setSavepoint();
            remove(id);
            updateCollectionId();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void remove(long id) throws StorageException {
        try {
            PreparedStatement statement = statementCreator.getRemoveByIdStatement();
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public long insert(CollectibleModel object) throws StorageException {
        try {
            connection.setSavepoint();
            counter = 1;
            PreparedStatement insertStatement = statementCreator.getInsertStatement();
            insertModel(object, insertStatement);
            //TODO remove
            System.out.println(insertStatement);
            ResultSet resultSet = insertStatement.executeQuery();
            resultSet.next();
            long res = resultSet.getLong("id");
            updateCollectionId();
            return res;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    private void insertModel(CollectibleModel model, PreparedStatement statement) throws SQLException {
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) {
                insertModel(fieldModel.getCollectibleModel(), statement);
            }
        }
        inputValues(model, statement);
        System.out.println(statement);
    }

    public CollectionWithID<CollectibleModel> load(CollectibleScheme collectibleScheme) throws StorageException {
        try {
            if (!collectibleScheme.equals(statementCreator.getTargetCollectibleScheme()))
                statementCreator.setTargetCollectibleScheme(collectibleScheme);
            PreparedStatement statement = statementCreator.getLoadStatement();
            ResultSet resultSet = statement.executeQuery();
            Collection<CollectibleModel> collection = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, InputtedValue> map = loadModel(collectibleScheme, resultSet);
                System.out.println(map);
                //TODO remove
                collection.add(new CollectibleModel(collectibleScheme, map));
            }
            resultSet.close();
            long id = getCollectionId();
            CollectionWithID<CollectibleModel> collectibleModels = new ArrayListWithID<>(id);
            collectibleModels.addAll(collection);
            return collectibleModels;

        } catch (SQLException | ValueNotValidException e) {
            throw new StorageException(e);
        }
    }

    private Map<String, InputtedValue> loadModel(CollectibleScheme collectibleScheme, ResultSet resultSet) throws SQLException {
        Map<String, InputtedValue> map = new HashMap<>();
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            if (!fieldData.isCollectible()) map.put(field, new InputtedValue(resultSet.getObject(field)));
            else map.put(field, new InputtedValue(loadModel(fieldData.getCollectibleScheme(), resultSet)));
        }
        return map;
    }

    public long getCollectionId() {
        try {
            PreparedStatement statement = statementCreator.getCollectionIdGetterStatement();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getLong("last_value");
            return 0L;
        } catch (SQLException ignored) {
            return 0L;
        }
    }

    private void updateCollectionId() throws SQLException {
        PreparedStatement statement = statementCreator.getCollectionIdUpdateStatement();
        statement.executeQuery();
        connection.commit();
    }

}
