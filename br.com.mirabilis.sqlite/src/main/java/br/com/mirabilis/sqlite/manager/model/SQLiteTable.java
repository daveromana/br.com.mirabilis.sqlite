package br.com.mirabilis.sqlite.manager.model;


import java.util.Observable;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;

/**
 * Class abstract that represent's Table's inside SQLite.
 *
 * @author Rodrigo Sim�es Rosa
 */
public abstract class SQLiteTable extends Observable {

    @SQLiteAnnotationField(name = "_id", type = SQLiteField.SQLiteFieldType.INTEGER, autoIncrement = true, notNull = true, primaryKey = true)
    protected long _id;

    /**
     * Get ID
     *
     * @return
     */
    public long getId() {
        return _id;
    }

    /**
     * Set ID
     *
     * @param id
     */
    public void setId(long id) {
        this._id = id;
    }

}
