package collection.storage.database;

import collection.meta.*;
import collection.storage.StorageHandler;
import commands.CommandAccessLevel;
import exceptions.StorageException;
import exceptions.UnknownAccountException;
import exceptions.ValueNotValidException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.AccountData;
import utility.ListAndId;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.*;

public class DatabaseHandler implements StorageHandler {
    private final ThreadLocal<Integer> counter;

    private final Logger logger;
    private final Connection connection;
    private final StatementCreator statementCreator;
    private final TableChecker tableChecker;


    public DatabaseHandler(Properties properties) throws SQLException {
        counter = ThreadLocal.withInitial(() -> 1);
        logger = LoggerFactory.getLogger("DatabaseHandler");
        statementCreator = new StatementCreator(this);
        String link = properties.getProperty("db_link");
        String user = properties.getProperty("db_user");
        String password = properties.getProperty("db_password");
        logger.info("Attempting to establish connection with database...");
        connection = DriverManager.getConnection("jdbc:postgresql://" + link, user, password);
        connection.setAutoCommit(false);
        logger.info("Successfully established connection: " + link);
        tableChecker = new TableChecker(connection.getMetaData());
    }

    private void handleSQLException(SQLException e) throws StorageException {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
        throw new StorageException(e);
    }

    public void addAccount(String username, AccountData accountData) throws StorageException {
        try {
            connection.setSavepoint();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts VALUES(?, ?, ?, (CAST(? AS accessLevel)))");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, accountData.getPasswordHash());
            preparedStatement.setString(3, accountData.getSalt());
            preparedStatement.setString(4, accountData.getAccessLevel().toString());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public AccountData getAccountData(String username) throws UnknownAccountException, StorageException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String passHash = resultSet.getString("passwordHash");
                String salt = resultSet.getString("salt");
                CommandAccessLevel accessLevel = CommandAccessLevel.valueOf(resultSet.getString("accessLevel"));
                return new AccountData(passHash, salt, accessLevel);
            } else {
                throw new UnknownAccountException("Account with username " + username + " doesn't exist");
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public void clear(String owner) throws StorageException {
        try {
            connection.setSavepoint();
            PreparedStatement clearStatement = statementCreator.getClearStatement();
            clearStatement.setString(1, owner);
            clearStatement.executeUpdate();
            updateCollectionId();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public boolean update(long id, String owner, CollectibleModel object) throws StorageException {
        try {
            connection.setSavepoint();
            counter.set(1);
            PreparedStatement updateStatement = statementCreator.getUpdateStatement();
            updateModel(id, owner, object, updateStatement);
            ResultSet resultSet = updateStatement.executeQuery();
            boolean res = resultSet.next();
            updateCollectionId();
            return res;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    private void updateModel(long id, String owner, CollectibleModel model, PreparedStatement statement) throws SQLException {
        inputValues(model, statement);
        if (id != 0) {
            statement.setLong(counter.get(), id);
            counter.set(counter.get() + 1);
            statement.setString(counter.get(), owner);
            counter.set(counter.get() + 1);
        }
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible())
                updateModel(0, "", fieldModel.getCollectibleModel(), statement);
        }
    }

    private void inputValues(CollectibleModel model, PreparedStatement statement) throws SQLException {
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) continue;
            Object value = fieldModel.getValue();
            Class<?> type = fieldModel.getFieldData().getType();
            if (type.isEnum() || ZonedDateTime.class.isAssignableFrom(type)) {
                if (value != null) statement.setObject(counter.get(), value.toString());
                else statement.setObject(counter.get(), null);
            } else statement.setObject(counter.get(), value);
            counter.set(counter.get() + 1);
        }
    }

    @Override
    public boolean removeABunch(Collection<Long> ids, String owner) throws StorageException {
        try {
            connection.setSavepoint();
            boolean result = false;
            for (long id : ids) {
                boolean res = remove(id, owner);
                if (res) result = true;
            }
            updateCollectionId();
            return result;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public boolean removeById(long id, String owner) throws StorageException {
        try {
            connection.setSavepoint();
            boolean res = remove(id, owner);
            updateCollectionId();
            return res;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    private boolean remove(long id, String owner) throws StorageException {
        try {
            PreparedStatement statement = statementCreator.getRemoveByIdStatement();
            statement.setLong(1, id);
            statement.setString(2, owner);
            int res = statement.executeUpdate();
            return res != 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public long insert(CollectibleModel object) throws StorageException {
        try {
            connection.setSavepoint();
            counter.set(1);
            PreparedStatement insertStatement = statementCreator.getInsertStatement();
            insertModel(object, insertStatement);
            ResultSet resultSet = insertStatement.executeQuery();
            resultSet.next();
            long res = resultSet.getLong("id");
            updateCollectionId();
            return res;
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    private void insertModel(CollectibleModel model, PreparedStatement statement) throws SQLException {
        for (FieldModel fieldModel : model.getValues().values()) {
            if (fieldModel.getFieldData().isCollectible()) {
                insertModel(fieldModel.getCollectibleModel(), statement);
            }
        }
        inputValues(model, statement);
    }

    private void initialize(CollectibleScheme collectibleScheme) throws SQLException {
        try {
            if (!collectibleScheme.equals(statementCreator.getTargetCollectibleScheme()))
                statementCreator.setTargetCollectibleScheme(collectibleScheme);
            tableChecker.checkMetaData(collectibleScheme);
        } catch (StorageException e) {
            logger.error(e.getMessage());
            logger.warn("Attempting to create needed tablespace...");
            connection.setSavepoint();
            PreparedStatement initStatement = statementCreator.getInitStatement();
            System.out.println(initStatement);
            initStatement.executeUpdate();
            connection.commit();
        }
    }

    public ListAndId<CollectibleModel> load(CollectibleScheme collectibleScheme) throws StorageException {
        try {
            initialize(collectibleScheme);
            PreparedStatement statement = statementCreator.getLoadStatement();
            ResultSet resultSet = statement.executeQuery();
            Collection<CollectibleModel> collection = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, InputtedValue> map = loadModel(collectibleScheme, resultSet);
                collection.add(new CollectibleModel(collectibleScheme, map));
            }
            resultSet.close();
            long id = getCollectionId();
            List<CollectibleModel> collectibleModels = new ArrayList<>(collection);
            return new ListAndId<>(id, collectibleModels, CollectibleModel.class);

        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        } catch (ValueNotValidException e) {
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
