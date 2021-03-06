package br.com.mirabilis.sqlite.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.model.SQLiteField;


/**
 * Recovery values of {@link br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity} and
 * {@link br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField}
 *
 * @author Rodrigo Sim�es Rosa
 */
public final class SQLiteParseAnnotation {

    public final static SQLiteEntity getValuesFromAnnotation(
            Class<?> classHasAnnotation) throws SQLiteException {
        SQLiteAnnotationEntity entityAnnotation = null;
        List<SQLiteField> fields = null;
        if (classHasAnnotation
                .isAnnotationPresent(SQLiteAnnotationEntity.class)) {
            entityAnnotation = classHasAnnotation
                    .getAnnotation(SQLiteAnnotationEntity.class);

            /**
             * Get fields of superclass SQLiteTable
             */
            for (Field field : classHasAnnotation.getSuperclass()
                    .getDeclaredFields()) {
                if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {

                    /**
                     * Initialize variable
                     */
                    if (fields == null) {
                        fields = new ArrayList<SQLiteField>();
                    }

                    fields.add(getSQLiteField(field
                            .getAnnotation(SQLiteAnnotationField.class)));
                }
            }

            for (Field field : classHasAnnotation.getDeclaredFields()) {
                if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {

                    /**
                     * Initialize variable
                     */
                    if (fields == null) {
                        fields = new ArrayList<SQLiteField>();
                    }

                    fields.add(getSQLiteField(field
                            .getAnnotation(SQLiteAnnotationField.class)));
                }
            }
        } else {
            throw new SQLiteException("The class : ".concat(
                    classHasAnnotation.getName())
                    .concat(" has not annotation!"));
        }

        SQLiteEntity entity = new SQLiteEntity(entityAnnotation.name(), fields);

        return entity;
    }

    /**
     * Return {@link br.com.mirabilis.sqlite.manager.model.SQLiteField} by {@link br.com.mirabilis.sqlite.annotation.SQLiteParseAnnotation}
     *
     * @param annotation
     * @return
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
     */
    public final static SQLiteField getSQLiteField(
            SQLiteAnnotationField annotation) throws SQLiteException {
        return new SQLiteField.Builder(annotation.name(), annotation.type())
                .autoIncrement(annotation.autoIncrement())
                .notNull(annotation.notNull())
                .primaryKey(annotation.primaryKey())
                .reference(annotation.reference())
                .foreignKey(annotation.foreignKey())
                .foreignKeyModifier(annotation.foreignKeyModifier())
                .action(annotation.action()).build();
    }
}
