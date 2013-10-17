package br.com.mirabilis.sqlite.manager.model;

import java.util.List;

import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;

/**
 * Class of {@link SQLiteEntity}
 * @author Rodrigo Simões Rosa
 */
public final class SQLiteEntity {

	private List<SQLiteField> fields;
	private String name;

	/**
	 * Enumeration that retain types of data and normalization rules;
	 * 
	 * @author Rodrigo Simões Rosa
	 */
	public enum Normalization {
		PRIMARY_KEY("primary key"), NOT_NULL("not null"), AUTOINCREMENT(
				"autoincrement");

		private String reserved;

		private Normalization(String value) {
			this.reserved = value;
		}

		@Override
		public String toString() {
			return this.reserved;
		}
	}

	/**
	 * Constructor;
	 * 
	 * @param name
	 * @param fields
	 * @throws SQLiteManagerException 
	 */
	public SQLiteEntity(String name, List<SQLiteField> fields) throws SQLiteManagerException {
		this.name = name;
		this.fields = fields;
	}

	/**
	 * Return all fields;
	 * 
	 * @return {@link List<SQLiteField>}
	 */
	public List<SQLiteField> getFields() {
		return fields;
	}

	/**
	 * Return query that create entity;
	 */
	public String getQueryCreateEntity() {
		StringBuilder query = new StringBuilder();
		query.append("create table if not exists ");
		query.append(getNameEntity().concat("("));
		StringBuilder fieldQuery = new StringBuilder();
		for(int i = 0; i < fields.size(); i++){
			SQLiteField f = fields.get(i);
			
			fieldQuery.append(f.getName());
			fieldQuery.append(" " + f.getType());

			if (f.isPrimaryKey()) {
				fieldQuery.append(" " + Normalization.PRIMARY_KEY.toString());
			}

			if (f.isAutoIncrement()) {
				fieldQuery.append(" " + Normalization.AUTOINCREMENT.toString());
			}

			if (f.isNotNull()) {
				fieldQuery.append(" " + Normalization.NOT_NULL.toString());
			}
			
			if(i <  fields.size() - 1){
				fieldQuery.append(",");
			}
		}
		fieldQuery.append(");");
		return query.append(fieldQuery).toString();
	}

	/**
	 * Return name entity;
	 */
	public String getNameEntity() {
		return this.name;
	}

	/**
	 * Return array of fields;
	 * 
	 * @return
	 */
	public Object[] getColumns() {
		return this.fields.toArray();
	}
}