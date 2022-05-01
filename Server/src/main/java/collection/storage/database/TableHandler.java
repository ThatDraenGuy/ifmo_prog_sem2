package collection.storage.database;

import collection.meta.CollectibleScheme;
import exceptions.StorageException;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableHandler {
    private final DatabaseMetaData metaData;
    private final String schemaName;
    private CollectibleScheme collectibleScheme;

    public TableHandler(DatabaseHandler databaseHandler) throws SQLException {
        this.metaData = databaseHandler.getConnection().getMetaData();
        schemaName = "public";
    }

    public void checkMetaData() throws SQLException {
        ResultSet tables = metaData.getTables(null, schemaName, null, null);
        while (tables.next()) System.out.println(tables.getString("TABLE_NAME"));
        ResultSet types = metaData.getTypeInfo();
        while (types.next()) System.out.println(types.getString("TYPE_NAME"));
    }

    private void checkTable(CollectibleScheme collectibleScheme, List<String> tablesNames) throws StorageException, SQLException {
        String name = collectibleScheme.getSimpleName();
        if (!tablesNames.contains(name)) throw new StorageException("Sufficient table not located");
        ResultSet columns = metaData.getColumns(null, schemaName, name, null);
    }
}
