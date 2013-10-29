package br.com.mirabilis.sqlite.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anottation of field sqlite.
 * 
 * @author Rodrigo Simões Rosa
 */
@Documented
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SQLiteAnnotationEntity {
	String name();
}
