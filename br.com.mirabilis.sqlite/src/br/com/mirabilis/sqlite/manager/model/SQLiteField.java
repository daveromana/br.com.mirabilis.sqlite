package br.com.mirabilis.sqlite.manager.model;

import android.database.sqlite.SQLiteException;

/**
 * Class of field of entity
 * @author Rodrigo Simões Rosa
 */
public class SQLiteField {
	
	/**
	 * Enumeration that contain's types data of sqlite;
	 * @author Rodrigo Simões Rosa
	 */
	public enum SQLiteType{
		NULL("null"),TEXT("text"),INTEGER("integer"),REAL("real"),BLOB("blob");
		
		private String type;
		
		private SQLiteType(String value) {
			this.type = value;
		}
		
		@Override
		public String toString() {
			return this.type; 
		}
	}
	
	/**
	 * Enumeration of {@link SQLiteField} called {@link Field}
	 * @author Rodrigo Simões Rosa
	 */
	public enum Field {
		ID("_id");
		
		private String value;
		
		private Field(String value){
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	private String name;
	private SQLiteType type;
	private boolean notNull;
	private boolean autoIncrement;
	private boolean primaryKey;
	
	/**
	 * Constructor;
	 * @param name
	 * @param type
	 * @param notNull
	 * @param autoIncrement
	 * @param primaryKey
	 * @throws SQLiteException
	 */
	public SQLiteField(String name, SQLiteType type, boolean notNull, boolean autoIncrement, boolean primaryKey) throws SQLiteException {
		this.name = name;
		this.type = type;
		this.primaryKey = primaryKey;
		this.notNull = notNull;
		if(autoIncrement){
			if (!(this.type.equals(SQLiteType.INTEGER))){
				throw new SQLiteException("It is not possible to perform an auto increment than the type Integer!");
			}else{
				this.autoIncrement = autoIncrement;
			}
		}
	}
	
	/**
	 * Construtor;
	 * @param name
	 * @param type
	 */
	public SQLiteField(String name, SQLiteType type){
		this(name,type, false, false, false);
	}

	/**
	 * Return name of field;
	 * @return {@link SQLiteField#name}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return type of field;
	 * @return {@link SQLiteField#type}
	 */
	public String getType() {
		return this.type.toString();
	}
	
	/**
	 * Return if is notNull;
	 * @return {@link SQLiteField#notNull} = true
	 */
	public boolean isNotNull() {
		return notNull;
	}
	
	/**
	 * Return if is field is primaryKey
	 * @return {@link SQLiteField#primaryKey} = true 
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	/**
	 * Return if is field is autoIncrement
	 * @return {@link SQLiteField#autoIncrement} = true
	 */
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
	
	@Override
	public String toString() {
		return name;
	}
}



