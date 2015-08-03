package br.com.mirabilis.sqlite.manager.dao.util;


/**
 * Enumeration that return type of object.
 *
 * @author Rodrigo Sim�es Rosa.
 */
public enum SQLiteDataType {

    BOOLEAN, BYTE, BYTE_ARRAY, DOUBLE, FLOAT, LONG, SHORT, INTEGER, STRING;

    /**
     * Return type of {@link br.com.mirabilis.sqlite.manager.dao.util.SQLiteDataType}
     *
     * @param obj
     * @return
     */
    public static SQLiteDataType getType(Object obj) {

        if (obj == null) {
            return null;
        } else if (obj instanceof Boolean) {
            return BOOLEAN;
        } else if (obj instanceof Byte) {
            return BYTE;
        } else if (obj instanceof byte[]) {
            return BYTE_ARRAY;
        } else if (obj instanceof Double) {
            return DOUBLE;
        } else if (obj instanceof Float) {
            return FLOAT;
        } else if (obj instanceof Long) {
            return LONG;
        } else if (obj instanceof Short) {
            return SHORT;
        } else if (obj instanceof Integer) {
            return INTEGER;
        } else if (obj instanceof String) {
            return STRING;
        }
        return null;
    }
}
