package br.com.mirabilis.sqlite.model;

import android.database.sqlite.SQLiteException;
import br.com.mirabilis.sqlite.enumeration.SQLiteType;

/**
 * Classe que estabelece o campo de cada item de entidade.
 * @author Rodrigo
 *
 */
public class SQLiteField {
	
	private String name;
	private Object type;
	private boolean notNull;
	private boolean autoIncrement;
	private boolean primaryKey;
	
	public SQLiteField(String name, Object type, boolean notNull, boolean autoIncrement, boolean primaryKey) throws SQLiteException {
		this.name = name;
		this.type = type;
		this.primaryKey = primaryKey;
		this.notNull = notNull;
		if(autoIncrement){
			if (!(this.type instanceof Integer)){
				throw new SQLiteException("Não é possivel realizar um auto increment que não seja do tipo Integer");
			}else{
				this.autoIncrement = autoIncrement;
			}
		}
	}
	
	public SQLiteField(String name, Object type){
		this(name,type, false, false, false);
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return SQLiteType.getType(this.type);
	}
	
	public boolean isNotNull() {
		return notNull;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	
	public boolean isAutoIncrement() {
		return autoIncrement;
	}
}



