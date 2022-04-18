package collection.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class DatabaseHandler {
    private final Logger logger;
    private final Connection connection;
    private PreparedStatement simpleQuery;

    public DatabaseHandler() throws SQLException {
        logger = LoggerFactory.getLogger("DatabaseHandler");
        Properties info = new Properties();
        info.put("user", "server");
        info.put("password", "aboba");
        //TODO config
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/progdb", info);
        logger.info("Successfully established connection: " + connection);
        debug();
    }

    private void debug() throws SQLException {
        simpleQuery = connection.prepareStatement("SELECT * FROM dragon");
        ResultSet results = simpleQuery.executeQuery();
        results.next();
        logger.debug(Integer.valueOf(results.getInt(1)).toString());
        simpleQuery.close();
    }
}
