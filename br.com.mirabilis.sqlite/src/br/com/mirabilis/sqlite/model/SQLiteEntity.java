package br.com.mirabilis.sqlite.model;

import java.util.List;

import br.com.mirabilis.sqlite.enumeration.SQLiteFieldDefault;
import br.com.mirabilis.sqlite.enumeration.SQLiteReserved;

/**
 * Classe abstrata de uma entidade do SQLite.
 * @author Rodrigo Simões Rosa
 */
public abstract class SQLiteEntity<T> implements Entity {

	private List<SQLiteField> fields;
	private String name;
	
	
	/**
	 * Construtor.
	 * @param fields
	 */
	public SQLiteEntity(String name, List<SQLiteField> fields) {
		this.name = name;
		this.fields = fields;
		setPrimaryKey();
	}
	
	private void setPrimaryKey(){
		boolean value = false;
		for(SQLiteField field : this.fields){
			if(field.isPrimaryKey()){
				value = true;
			}
		}
		if(!value){
			this.fields.add(0, new SQLiteField(SQLiteFieldDefault.ID.toString(), String.class,false,true,true));	
		}
	}
	
	/**
	 * Retorna todos os campos.
	 * @return
	 */
	public List<SQLiteField> getFields() {
		return fields;
	}
	
	/**
	 * Retorna uma query de criação da entidade.
	 */
	public String getQueryCreateEntity() {
		StringBuilder query = new StringBuilder();
		query.append("create table if not exists ");
		query.append(getNameEntity().concat("("));
		StringBuilder fieldQuery = new StringBuilder();
		for(SQLiteField field : fields){
			fieldQuery.append(field.getName());
			fieldQuery.append(" " + field.getType());
			if(field.isPrimaryKey()){
				fieldQuery.append(" " + SQLiteReserved.PRIMARY_KEY.toString());
			}
			if(field.isAutoIncrement()){
				fieldQuery.append(" "+ SQLiteReserved.AUTOINCREMENT.toString());
			}
			
			if(field.isNotNull()){
				fieldQuery.append(" "+ SQLiteReserved.NOT_NULL.toString());
			}
			fieldQuery.append(");");
		}
		return query.append(fieldQuery).toString();	
	}
	
	public String getNameEntity() {
		return this.name;
	}
}
