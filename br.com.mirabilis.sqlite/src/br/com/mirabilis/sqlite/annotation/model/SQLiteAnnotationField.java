package br.com.mirabilis.sqlite.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.mirabilis.sqlite.manager.model.SQLiteField.SQLiteType;

/**
 * Anottation of field sqlite.
 * 
 * @author Rodrigo Simões Rosa
 */
@Documented
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SQLiteAnnotationField {
	String name();

	SQLiteType type();

	boolean primaryKey() default false;

	boolean autoIncrement() default false;

	boolean notNull() default false;
}