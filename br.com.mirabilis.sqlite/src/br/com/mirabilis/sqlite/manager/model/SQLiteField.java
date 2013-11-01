package br.com.mirabilis.sqlite.manager.model;

import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity.SQLiteAction;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity.SQLiteForeignModifier;

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
		NULL("null"), TEXT("text"), INTEGER("integer"), REAL("real"), BLOB("blob"), DATETIME("datetime");

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

		private final String value;

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
	private boolean foreignKey;
	private SQLiteForeignModifier foreignKeyModifier;
	private Class<? extends SQLiteTable> reference;
	public SQLiteAction action;

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
	
	/**
	 * Return if is field is foreignkey.
	 * 
	 * @return
	 */
	public boolean isForeignKey() {
		return foreignKey;
	}

	/**
	 * Return ForeignKeyModifier.
	 * @return
	 */
	public SQLiteForeignModifier getForeignKeyModifier() {
		return foreignKeyModifier;
	}
	
	/**
	 * Return reference from that primarykey in other table that here is foreignKey.
	 * @return
	 */
	public Class<? extends SQLiteTable> getReference() {
		return reference;
	}
	
	/**
	 * Return action from foreignkeymodifier.
	 * @return
	 */
	public SQLiteAction getAction() {
		return action;
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
		 * Set field as foreignkey.
		 * @param foreignKey
		 * @return
		 */
		public Builder foreignKey(boolean foreignKey) {
			this.instance.foreignKey = foreignKey;
			return this;
		}
		
		/**
		 * Set field as foreignKeyModifier.
		 * @param foreignKeyModifier
		 * @return
		 */
		public Builder foreignKeyModifier(SQLiteForeignModifier foreignKeyModifier){
			this.instance.foreignKeyModifier = foreignKeyModifier;
			return this;
		}
		
		/**
		 * Set field reference.
		 * @param reference
		 * @return
		 */
		public Builder reference(Class<? extends SQLiteTable> reference){
			this.instance.reference = reference;
			return this;
		}

		/**
		 * Set field action
		 * @param action
		 * @return
		 */
		public Builder action(SQLiteAction action) {
			this.instance.action = action;
			return this;
		}

		/**
		 * Build {@link SQLiteField}
		 * 
		 * @return
		 * @throws SQLiteException 
		 */
		public SQLiteField build() throws SQLiteException {
			if(this.instance.foreignKey && this.instance.reference == null){
				throw new SQLiteException("You must inform the reference to mark this field as foreignKey!");
			}
			return this.instance;
		}

	}

}
