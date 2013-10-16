package br.com.mirabilis.sqlite.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.model.SQLiteField;
import br.com.mirabilis.sqlite.manager.model.SQLiteField.SQLiteType;

/**
 * Recovery values of {@link SQLiteAnnotationEntity} and {@link SQLiteAnnotationField}
 * @author Rodrigo Simões Rosa
 */
public class SQLiteAnnotation {
	
	public static SQLiteEntity getValuesFromAnnotation(Class<?> classHasAnnotation) throws SQLiteManagerException{
		SQLiteAnnotationEntity entityAnnotation = null;
		List<SQLiteAnnotationField> fieldsAnnotation = null;
		if(classHasAnnotation.isAnnotationPresent(SQLiteAnnotationEntity.class)){
			entityAnnotation = classHasAnnotation.getAnnotation(SQLiteAnnotationEntity.class);
			for(Field field : classHasAnnotation.getDeclaredFields()){
				if(field.isAnnotationPresent(SQLiteAnnotationField.class)){
					
					/**
					 * Initialize variable
					 */
					if(fieldsAnnotation == null){
						fieldsAnnotation = new ArrayList<SQLiteAnnotationField>();
					}
					
					fieldsAnnotation.add(field.getAnnotation(SQLiteAnnotationField.class));
				}
			}
		}else{
			throw new SQLiteManagerException("The class : ".concat(classHasAnnotation.getName()).concat(" has not annotation!"));
		}
		
		List<SQLiteField> fields = new ArrayList<SQLiteField>();
		for(SQLiteAnnotationField f : fieldsAnnotation){
			fields.add(new SQLiteField.Builder(f.name(), SQLiteType.getValue(f.type())).build());
		}
		SQLiteEntity entity = new SQLiteEntity(entityAnnotation.name(), fields);
		
		return entity;
	}
}
