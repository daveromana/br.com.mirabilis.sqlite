package br.com.mirabilis.sqlite.manager.dao.util;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException;

/**
 * Class that recovery ContentValues by T data.
 *
 * @author Rodrigo Sim�es Rosa.
 */
public class SQLiteDataManager {

    /**
     * Set values in instance.
     *
     * @param cursor
     * @param instance
     * @param fields
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> void setValue(Cursor cursor, T instance, Field[] fields)
            throws IllegalArgumentException, IllegalAccessException {
        for (Field field : fields) {
            if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {
                field.setAccessible(true);
                SQLiteAnnotationField annotField = field
                        .getAnnotation(SQLiteAnnotationField.class);
                int index = cursor.getColumnIndex(annotField.name());
                Class<?> classType = field.getType();
                switch (annotField.type()) {
                    case TEXT:
                        String stringValue = cursor.getString(index);
                        field.set(instance, stringValue);
                        break;

                    case INTEGER:
                        if (classType.equals(Integer.class)
                                || classType.equals(int.class)) {
                            Integer integerValue = cursor.getInt(index);
                            field.set(instance, integerValue);
                        } else if (classType.equals(Long.class)
                                || classType.equals(long.class)) {
                            Long longValue = cursor.getLong(index);
                            field.set(instance, longValue);
                        } else if (classType.equals(Short.class)
                                || classType.equals(short.class)) {
                            Short shortValue = cursor.getShort(index);
                            field.set(instance, shortValue);
                        } else if (classType.equals(Boolean.class)
                                || classType.equals(boolean.class)) {
                            Boolean booleanValue = cursor.getInt(index) == 1;
                            field.set(instance, booleanValue);
                        }
                        break;

                    case BLOB:
                        byte[] byteValue = cursor.getBlob(index);
                        field.set(instance, byteValue);
                        break;

                    case DATETIME:
                        String dateValue = cursor.getString(index);
                        field.set(instance, dateValue);
                        break;

                    case REAL:
                        if (classType.equals(Double.class)
                                || classType.equals(double.class)) {
                            Double doubleValue = cursor.getDouble(index);
                            field.set(instance, doubleValue);
                        } else if (classType.equals(Float.class)
                                || classType.equals(float.class)) {
                            Float floatValue = cursor.getFloat(index);
                            field.set(instance, floatValue);
                        }
                        break;

                    default:
                        break;
                }
                ;
            }
        }
    }

    /**
     * Creator of {@link android.content.ContentValues}
     *
     * @param data
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
     */
    public static <T> ContentValues getContentValues(T data)
            throws IllegalArgumentException, IllegalAccessException,
            SQLiteNotNullFieldException, SQLiteException {
        ContentValues contentValues = new ContentValues();
        Class<?> c = data.getClass();
        for (Field field : c.getDeclaredFields()) {

            if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {
                SQLiteAnnotationField fieldAnnotation = field
                        .getAnnotation(SQLiteAnnotationField.class);

                field.setAccessible(true);
                Object value = field.get(data);

                if (fieldAnnotation.notNull() && value == null) {
                    throw new SQLiteNotNullFieldException(field.getName());
                }
                if (value == null) {
                    contentValues.putNull(field.getAnnotation(
                            SQLiteAnnotationField.class).name());
                } else {
                    String fieldName = field.getAnnotation(
                            SQLiteAnnotationField.class).name();
                    // TODO I know it's smelling like shit, but it was the first
                    // solution that I found (XGH) :B

                    switch (SQLiteDataType.getType(value)) {
                        case BOOLEAN:
                            contentValues.put(fieldName, (Boolean) value);
                            break;

                        case BYTE:
                            contentValues.put(fieldName, (Byte) value);
                            break;

                        case BYTE_ARRAY:
                            contentValues.put(fieldName, (byte[]) value);
                            break;

                        case DOUBLE:
                            contentValues.put(fieldName, (Double) value);
                            break;

                        case FLOAT:
                            contentValues.put(fieldName, (Float) value);
                            break;

                        case INTEGER:
                            contentValues.put(fieldName, (Integer) value);
                            break;

                        case LONG:
                            contentValues.put(fieldName, (Long) value);
                            break;

                        case SHORT:
                            contentValues.put(fieldName, (Short) value);
                            break;

                        case STRING:
                            contentValues.put(fieldName, (String) value);
                            break;

                        default:
                            throw new SQLiteException("This type is not allowed");
                    }
                }
            }
        }
        return contentValues;
    }
}
