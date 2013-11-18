package br.com.mirabilis.sqlite.manager.model;

import java.lang.reflect.Field;
import java.util.List;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;

/**
 * Class of {@link SQLiteEntity}
 * 
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
	public enum SQLiteNormalization {
		PRIMARY_KEY("primary key"), NOT_NULL("not null"), AUTOINCREMENT(
				"autoincrement"), FOREIGN_KEY("constraint"), REFERENCES(
				"references");

		private String value;

		private SQLiteNormalization(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	/**
	 * The ON DELETE and ON UPDATE action associated with each foreign key in an
	 * SQLite database is one of "NO ACTION", "RESTRICT", "SET NULL",
	 * "SET DEFAULT" or "CASCADE". If an action is not explicitly specified, it
	 * defaults to "NO ACTION".
	 * 
	 * @author Rodrigo Simões Rosa.
	 * 
	 */
	public enum SQLiteAction {
		ON_DELETE("on delete"), ON_UPDATE("on update");

		private String value;

		private SQLiteAction(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * Enumeration that define type of modifier in foreignkey
	 * 
	 * @author Rodrigo Simões Rosa Foreign key ON DELETE and ON UPDATE clauses
	 *         are used to configure actions that take place when deleting rows
	 *         from the parent table (ON DELETE), or modifying the parent key
	 *         values of existing rows (ON UPDATE). A single foreign key
	 *         constraint may have different actions configured for ON DELETE
	 *         and ON UPDATE. Foreign key actions are similar to triggers in
	 *         many ways.
	 * 
	 *         The ON DELETE and ON UPDATE action associated with each foreign
	 *         key in an SQLite database is one of "NO ACTION", "RESTRICT",
	 *         "SET NULL", "SET DEFAULT" or "CASCADE". If an action is not
	 *         explicitly specified, it defaults to "NO ACTION".
	 */
	public enum SQLiteForeignModifier {

		/**
		 * Configuring "NO ACTION" means just that: when a parent key is
		 * modified or deleted from the database, no special action is taken.
		 */
		NO_ACTION("no action"),

		/**
		 * The "RESTRICT" action means that the application is prohibited from
		 * deleting (for ON DELETE RESTRICT) or modifying (for ON UPDATE
		 * RESTRICT) a parent key when there exists one or more child keys
		 * mapped to it. The difference between the effect of a RESTRICT action
		 * and normal foreign key constraint enforcement is that the RESTRICT
		 * action processing happens as soon as the field is updated - not at
		 * the end of the current statement as it would with an immediate
		 * constraint, or at the end of the current transaction as it would with
		 * a deferred constraint. Even if the foreign key constraint it is
		 * attached to is deferred, configuring a RESTRICT action causes SQLite
		 * to return an error immediately if a parent key with dependent child
		 * keys is deleted or modified.
		 */
		RESTRICT("restrict"),

		/**
		 * If the configured action is "SET NULL", then when a parent key is
		 * deleted (for ON DELETE SET NULL) or modified (for ON UPDATE SET
		 * NULL), the child key columns of all rows in the child table that
		 * mapped to the parent key are set to contain SQL NULL values.
		 */
		SET_NULL("set null"),

		/**
		 * The "SET DEFAULT" actions are similar to "SET NULL", except that each
		 * of the child key columns is set to contain the columns default value
		 * instead of NULL. Refer to the CREATE TABLE documentation for details
		 * on how default values are assigned to table columns.
		 */
		SET_DEFAULT("set default"),

		/**
		 * A "CASCADE" action propagates the delete or update operation on the
		 * parent key to each dependent child key. For an "ON DELETE CASCADE"
		 * action, this means that each row in the child table that was
		 * associated with the deleted parent row is also deleted. For an
		 * "ON UPDATE CASCADE" action, it means that the values stored in each
		 * dependent child key are modified to match the new parent key values.
		 */
		CASCADE("cascade");

		private String value;

		private SQLiteForeignModifier(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	/**
	 * Constructor;
	 * 
	 * @param name
	 * @param fields
	 * @throws SQLiteException
	 */
	public SQLiteEntity(String name, List<SQLiteField> fields)
			throws SQLiteException {
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
	 * 
	 * @throws NoSuchFieldException
	 */
	public String getQueryCreateEntity() throws NoSuchFieldException {
		StringBuilder query = new StringBuilder();
		query.append("create table if not exists ");
		query.append(getNameEntity().concat("("));
		StringBuilder fieldQuery = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			SQLiteField f = fields.get(i);

			fieldQuery.append(f.getName());
			fieldQuery.append(" " + f.getType());

			if (f.isPrimaryKey()) {
				fieldQuery.append(" "
						+ SQLiteNormalization.PRIMARY_KEY.toString());
			}

			if (f.isAutoIncrement()) {
				fieldQuery.append(" "
						+ SQLiteNormalization.AUTOINCREMENT.toString());
			}

			if (f.isNotNull()) {
				fieldQuery
						.append(" " + SQLiteNormalization.NOT_NULL.toString());
			}

			if (f.isForeignKey()) {
				fieldQuery.append(" "
						+ SQLiteNormalization.FOREIGN_KEY.toString());
				fieldQuery.append(" " + f.getName());

				Field fieldForeign = f.getReference().getSuperclass()
						.getDeclaredField(SQLiteField.Field.ID.toString());
				fieldQuery.append(" "
						+ SQLiteNormalization.REFERENCES.toString());
				SQLiteAnnotationEntity entityForeign = f.getReference()
						.getAnnotation(SQLiteAnnotationEntity.class);
				fieldQuery.append(" " + entityForeign.name() + "("
						+ fieldForeign.getName() + ")");
				fieldQuery.append(" " + f.getAction().toString());
				fieldQuery.append(" " + f.getForeignKeyModifier().toString());
			}

			if (i < fields.size() - 1) {
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