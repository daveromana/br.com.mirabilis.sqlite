package br.com.mirabilis.sqlite.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.mirabilis.sqlite.manager.model.SQLiteEntity.SQLiteRelationship;

@Documented
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)

public @interface SQLiteAnnotationReference {
	String name();
	SQLiteRelationship relationship();
}
