package br.com.mirabilis.sqlite.manager.dao.util;

import java.lang.reflect.Field;
import java.util.Calendar;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException;
import br.com.mirabilis.sqlite.util.CalendarUtils;
import android.content.ContentValues;

/**
 * Class that recovery ContentValues by T data. 
 * @author Rodrigo Simões Rosa.
 */
public class ContentValuesCreator {
	
	public static <T> ContentValues creator(T data) throws IllegalArgumentException, IllegalAccessException, SQLiteNotNullFieldException, SQLiteException{
		ContentValues contentValues = new ContentValues();
		Class<?> c = data.getClass(); 
		for(Field field : c.getDeclaredFields()){
			
			if(field.isAnnotationPresent(SQLiteAnnotationField.class)){
				SQLiteAnnotationField fieldAnnotation = field.getAnnotation(SQLiteAnnotationField.class);
				
				field.setAccessible(true);
				Object value = field.get(data);
				
				if(fieldAnnotation.notNull() && value == null){
					throw new SQLiteNotNullFieldException(field.getName());
				}
				if(value == null){
					contentValues.putNull(field.getAnnotation(SQLiteAnnotationField.class).name());
				}else{
					//TODO I know it's smelling like shit, but it was the first solution that I found (XGH) :B
					switch (ContentValuesType.getType(value)) {
						case BOOLEAN:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (Boolean) value);
						break;
						
						case CALENDAR:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), CalendarUtils.getString((Calendar) value));
						break;
						
						case BYTE:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (Byte) value);
						break;
						
						case BYTE_ARRAY:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (byte[]) value);
						break;
						
						case DOUBLE:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (Double) value);
						break;
						
						case FLOAT:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (Float) value);
						break;
						
						case INTEGER:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(),  (Integer) value);
						break;
						
						case LONG:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(),  (Long) value);
						break;
						
						case SHORT:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (Short) value);
						break;
						
						case STRING:
							contentValues.put(field.getAnnotation(SQLiteAnnotationField.class).name(), (String) value);
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
