package br.com.mirabilis.sqlite.manager.dao.util;

import java.lang.reflect.Field;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import android.content.ContentValues;

/**
 * Class that recovery ContentValues by T data. 
 * @author Rodrigo Simões Rosa.
 */
public class ContentValuesCreator {
	
	public static <T> ContentValues creator(T data) throws IllegalArgumentException, IllegalAccessException{
		ContentValues contentValues = new ContentValues();
		Class<?> c = data.getClass(); 
		for(Field field : c.getDeclaredFields()){
			
			if(field.isAnnotationPresent(SQLiteAnnotationField.class)){
				Object obj = field.get(data);
				
				//TODO I know it's smelling like shit, but it was the first solution that I found (XGH) :B
				switch (ContentValuesType.getType(obj)) {
					case BOOLEAN:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getBoolean(data));
					break;

					case BYTE:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getByte(data));
					break;
					
					case BYTE_ARRAY:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (byte[]) field.get(data));
					break;
					
					case DOUBLE:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getDouble(data));
					break;
					
					case FLOAT:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getFloat(data));
					break;
					
					case INTEGER:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getInt(data));
					break;
					
					case LONG:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getLong(data));
					break;
					
					case SHORT:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), field.getShort(data));
					break;
					
					case STRING:
						contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), String.valueOf(field.get(data)));
					break;
					
					default:
						contentValues.putNull(field.getAnnotation(SQLiteAnnotationField.class).name());
					break;
				}
			}
		}
		return contentValues;
	}
}
