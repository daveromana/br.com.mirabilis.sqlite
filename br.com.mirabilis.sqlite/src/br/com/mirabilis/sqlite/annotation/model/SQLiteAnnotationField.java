package br.com.mirabilis.sqlite.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.mirabilis.sqlite.manager.model.SQLiteEntity.SQLiteAction;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity.SQLiteForeignModifier;
import br.com.mirabilis.sqlite.manager.model.SQLiteField.SQLiteFieldType;
import br.com.mirabilis.sqlite.manager.model.SQLiteTable;

/**
 * Anottation of field sqlite.
 * 
 * @author Rodrigo Simões Rosa
 */
@Documented
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SQLiteAnnotationField {
	
	/**
	 * name field in sqlite.
	 * @return
	 */
	String name();

	/**
	 * type field in sqlite.
	 * @return
	 */
	SQLiteFieldType type();

	/**
	 * if field is primarykey in table.
	 * @return
	 */
	boolean primaryKey() default false;

	/**
	 * if field is foreignkey in table. ** Only will be functional if set reference. 
	 * @return
	 */
	boolean foreignKey() default false;

	/**
	 * reference of foreignkey.
	 * @return
	 */
	Class<? extends SQLiteTable> reference() default SQLiteTable.class;

	/**
	 * autoincrement in field.
	 * @return
	 */
	boolean autoIncrement() default false;
	
	/**
	 * modifier of foreign key.
	 * @return
	 */
	SQLiteForeignModifier foreignKeyModifier() default SQLiteForeignModifier.RESTRICT;

	/**
	 * not null in field.
	 * @return
	 */
	boolean notNull() default false;

	/**
	 * action in field foreignkey
	 * @return
	 */
	SQLiteAction action() default SQLiteAction.ON_DELETE;
}