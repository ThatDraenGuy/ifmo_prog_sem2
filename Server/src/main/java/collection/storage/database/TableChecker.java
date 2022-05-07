package collection.storage.database;

import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import exceptions.StorageException;
import lombok.Getter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for validating database's tables. Checks that they have correct columns with correct data types. Currently, doesn't check column restraints.
 */
public class TableChecker {
    private final DatabaseMetaData metaData;

    public TableChecker(DatabaseMetaData metaData) {
        this.metaData = metaData;
    }

    public void checkMetaData(CollectibleScheme collectibleScheme) throws SQLException, StorageException {
        ResultSet tables = metaData.getTables(null, "s336765", null, null);
        List<String> tablesNames = new ArrayList<>();
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            tablesNames.add(tableName);
        }
        checkTable(collectibleScheme, tablesNames);
    }

    private void checkTable(CollectibleScheme collectibleScheme, List<String> tablesNames) throws StorageException, SQLException {
        String name = collectibleScheme.getSimpleName().toLowerCase();
        if (!tablesNames.contains(name)) throw new StorageException("Sufficient table not located");
        ResultSet columns = metaData.getColumns(null, "s336765", name, null);
        List<ColumnInfo> columnsInfo = new ArrayList<>();
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String columnType = columns.getString("TYPE_NAME");
            int columnNullable = columns.getInt("NULLABLE");
            ColumnInfo columnInfo = new ColumnInfo(columnName, columnType, columnNullable);
            columnsInfo.add(columnInfo);
        }
        for (String field : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(field);
            String lowerCasedField = field.toLowerCase();
            if (fieldData.isCollectible()) checkTable(fieldData.getCollectibleScheme(), tablesNames);
            Optional<ColumnInfo> columnInfoOptional = columnsInfo.stream().filter(x -> x.getName().equals(lowerCasedField)).findFirst();
            if (columnInfoOptional.isEmpty())
                throw new StorageException("Column " + field + " is missing from " + name);
            ColumnInfo columnInfo = columnInfoOptional.get();
            if (!SQLTypeConverter.getSqlType(fieldData.getType(), field).equals(columnInfo.getType()))
                throw new StorageException("Wrong column type: " + field);
        }
    }


    private static class ColumnInfo {
        @Getter
        private final String name;
        @Getter
        private final String type;
        @Getter
        private final boolean notNull;

        public ColumnInfo(String name, String type, int nullable) {
            this.name = name;
            this.type = type;
            this.notNull = nullable != 1;
        }

        @Override
        public String toString() {
            return "ColumnInfo{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", notNull=" + notNull +
                    '}';
        }
    }
}
