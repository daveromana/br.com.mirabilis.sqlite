package br.com.mirabilis.sqlite.manager.exception;

import java.lang.reflect.Field;

/**
 * Exception of notnull field.
 * 
 * @author Rodrigo Simões Rosa.
 */
public class SQLiteNotNullFieldException extends Exception {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -1178871606123303993L;

	public SQLiteNotNullFieldException(Field field) {
		this("The field " + field.getName() + "is not allowed to null!");
	}

	public SQLiteNotNullFieldException(String value) {
		super(value);
	}
}
