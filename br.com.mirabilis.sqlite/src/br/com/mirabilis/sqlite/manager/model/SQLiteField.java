package br.com.mirabilis.sqlite.manager.model;

import br.com.mirabilis.sqlite.manager.exception.SQLiteException;

/**
 * Class of field of entity
 * 
 * @author Rodrigo Simões Rosa
 */
public final class SQLiteField {

	/**
	 * Enumeration that contain's types data of sqlite;
	 * 
	 * @author Rodrigo Simões Rosa
	 */
	public enum SQLiteFieldType {
		NULL("null"), TEXT("text"), INTEGER("integer"), REAL("real"), BLOB(
				"blob"), DATETIME("datetime");

		private String type;

		private SQLiteFieldType(String value) {
			this.type = value;

		}

		@Override
		public String toString() {
			return this.type;
		}

		/**
		 * Recovery {@link SQLiteFieldType} by {@link String}
		 * 
		 * @param type
		 * @return
		 */
		public static SQLiteFieldType getValue(String type) {
			for (SQLiteField.SQLiteFieldType t : values()) {
				if (t.toString().equalsIgnoreCase(type)) {
					return t;
				}
			}
			return null;
		}
	}

	/**
	 * Enumeration of {@link SQLiteField} called {@link Field}
	 * 
	 * @author Rodrigo Simões Rosa
	 */
	public enum Field {
		ID("_id");

		private String value;

		private Field(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	private String name;
	private SQLiteFieldType type;
	private boolean notNull;
	private boolean autoIncrement;
	private boolean primaryKey;

	/**
	 * Constructor;
	 * 
	 * @param name
	 * @param type
	 */
	private SQLiteField(String name, SQLiteFieldType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Return name of field;
	 * 
	 * @return {@link SQLiteField#name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return type of field;
	 * 
	 * @return {@link SQLiteField#type}
	 */
	public String getType() {
		return this.type.toString();
	}

	/**
	 * Return if is notNull;
	 * 
	 * @return {@link SQLiteField#notNull} = true
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * Return if is field is primaryKey
	 * 
	 * @return {@link SQLiteField#primaryKey} = true
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * AutoIncrement, verify if {@link #type} is {@link SQLiteFieldType #INTEGER}
	 * 
	 * @param value
	 * @throws SQLiteException
	 */
	public void setAutoIncrement(boolean value) throws SQLiteException {
		if (value) {
			if (!(this.type.equals(SQLiteFieldType.INTEGER))) {
				throw new SQLiteException(
						"It is not possible to perform an auto increment than the type Integer!");
			} else {
				this.autoIncrement = value;
			}
		}
	}

	/**
	 * Return if is field is autoIncrement
	 * 
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
	 * 
	 * @author Rodrigo Simões Rosa
	 */
	public static class Builder {

		private SQLiteField instance;

		/**
		 * Constructor
		 * 
		 * @param name
		 * @param type
		 */
		public Builder(String name, SQLiteFieldType type) {
			this.instance = new SQLiteField(name, type);
		}

		/**
		 * Set field as notnull
		 * 
		 * @param notNull
		 * @return
		 */
		public Builder notNull(boolean notNull) {
			this.instance.notNull = notNull;
			return this;
		}

		/**
		 * Set field as primary key
		 * 
		 * @param primaryKey
		 * @return
		 */
		public Builder primaryKey(boolean primaryKey) {
			this.instance.primaryKey = primaryKey;
			return this;
		}

		/**
		 * Set field as autoIncrement
		 * 
		 * @param autoIncrement
		 * @return
		 * @throws SQLiteException
		 */
		public Builder autoIncrement(boolean autoIncrement)
				throws SQLiteException {
			this.instance.setAutoIncrement(autoIncrement);
			return this;
		}

		/**
		 * Build {@link SQLiteField}
		 * 
		 * @return
		 */
		public SQLiteField build() {
			return this.instance;
		}
	}
}
