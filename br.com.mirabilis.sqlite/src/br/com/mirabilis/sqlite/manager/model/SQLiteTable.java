package br.com.mirabilis.sqlite.manager.model;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;

/**
 * Class abstract that represent's Table's inside SQLite.
 * @author Rodrigo Simões Rosa
 */
public abstract class SQLiteTable {

	@SQLiteAnnotationField(name="_id", type="integer", autoIncrement = true, notNull = true, primaryKey = true)
	protected long _id;
	
	/**
	 * Get ID
	 * @return
	 */
	public long getId() {
		return _id;
	}
	
	/**
	 * Set ID
	 * @param id
	 */
	public void setId(long id){
		this._id = id;
	}

	
}
