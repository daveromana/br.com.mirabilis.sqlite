package br.com.mirabilis.sqlite.manager.dao.util;

import java.util.Calendar;

/**
 * Enumeration that return type of object.
 * @author Rodrigo Simões Rosa.
 */
public enum ContentValuesType{
	
	BOOLEAN, CALENDAR, BYTE, BYTE_ARRAY, DOUBLE, FLOAT, LONG, SHORT, INTEGER, STRING;
	
	public static ContentValuesType getType(Object obj){
		
		if(obj == null){
			return null;
		}else if(obj instanceof Boolean){
			return BOOLEAN;
		}else if(obj instanceof Calendar){
			return CALENDAR;
		}else if(obj instanceof Byte){
			return BYTE;
		}else if(obj instanceof byte[]){
			return BYTE_ARRAY;
		}else if(obj instanceof Double){
			return DOUBLE;
		}else if(obj instanceof Float){
			return FLOAT;
		}else if(obj instanceof Long){
			return LONG;
		}else if(obj instanceof Short){
			return SHORT;
		}else if(obj instanceof Integer){
			return INTEGER;
		}else if(obj instanceof String){
			return STRING;
		}
		return null;
	}
}
