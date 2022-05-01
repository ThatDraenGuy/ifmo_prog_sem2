package collection.storage.database;

import collection.classes.Collectible;

/**
 * Small class to handle all java-sql type conversions
 */
public class SQLTypeConverter {


    public static String getSqlType(Class<?> target, String fieldName) {
        if (fieldName.equals("id")) return "bigserial";
        if (target.equals(int.class) || target.equals(Integer.class)) return "int4";
        if (target.equals(long.class) || target.equals(Long.class)) return "int8";
        if (target.equals(String.class)) return "varchar";
        if (target.isEnum()) return target.getSimpleName().toLowerCase();
        if (Collectible.class.isAssignableFrom(target)) return "int8";
        return "varchar";
    }
}
