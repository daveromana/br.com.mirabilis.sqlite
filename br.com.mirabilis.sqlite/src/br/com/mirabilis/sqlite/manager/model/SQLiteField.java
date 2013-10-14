package br.com.mirabilis.sqlite.manager.model;

import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;

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
	 */ 
	private SQLiteField(String name, SQLiteType type) {
		this.name = name;
		this.type = type;
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
	 * AutoIncrement, verify if {@link #type} is {@link SQLiteType #INTEGER}
	 * @param value
	 * @throws SQLiteManagerException
	 */
	public void setAutoIncrement(boolean value) throws SQLiteManagerException {
		if(value){
			if (!(this.type.equals(SQLiteType.INTEGER))){
				throw new SQLiteManagerException("It is not possible to perform an auto increment than the type Integer!");
			}else{
				this.autoIncrement = value;
			}
		}
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
	
	/**
	 * Builder of {@link SQLiteField};
	 * @author Rodrigo Simões Rosa
	 */
	public static class Builder {
		
		private SQLiteField instance;
		
		public Builder(String name, SQLiteType type) {
			this.instance = new SQLiteField(name, type);
		}
		
		public Builder notNull(boolean notNull){
			this.instance.notNull = notNull;
			return this;
		}
		
		public Builder primaryKey(boolean primaryKey){
			this.instance.primaryKey = primaryKey;
			return this;
		}
		
		public Builder autoIncrement(boolean autoIncrement) throws SQLiteManagerException {
			this.instance.setAutoIncrement(autoIncrement);
			return this;
		}
		
		public SQLiteField build(){
			return this.instance;
		}
	}
}



