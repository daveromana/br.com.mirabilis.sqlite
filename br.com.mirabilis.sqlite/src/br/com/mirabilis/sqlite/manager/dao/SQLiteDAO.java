package br.com.mirabilis.sqlite.manager.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteField;
import br.com.mirabilis.sqlite.manager.model.SQLiteTable;
import br.com.phoenix.comanda.connection.sql.entity.Command;

/**
 * Class abstract of DAO.
 * 
 * @author Rodrigo Simões Rosa
 * @param <T>
 *            Object.
 */
public abstract class SQLiteDAO<T extends SQLiteTable> implements DAO<T> {

	protected SQLiteDatabase database;

	/**
	 * Constructor;
	 * 
	 * @param database
	 */
	public SQLiteDAO(SQLiteDatabase database) {
		this.database = database;
	}

	/**
	 * Execute query
	 * 
	 * @param query
	 * @return {@link Cursor}
	 * @throws SQLConnectionException
	 */
	@SuppressWarnings("finally")
	public Cursor query(String query) throws SQLiteManagerException {
		Cursor cursor = null;
		try {
			cursor = this.database.rawQuery(query, null);
			cursor.moveToFirst();
		} catch (Throwable e) {
			throw new SQLiteManagerException("Error " + e.getMessage()
					+ "  on execute query " + query);
		} finally {
			return cursor;
		}
	}

	/**
	 * Open connection;
	 * 
	 * @param sqliteOpenHelper
	 * @return state of connection;
	 */
	@SuppressWarnings("finally")
	public boolean open(SQLiteOpenHelper sqliteOpenHelper) {
		try {
			this.database = sqliteOpenHelper.getWritableDatabase();
		} catch (Throwable e) {
			throw new SQLiteManagerException("Error " + e.getMessage()
					+ " on execute");
		} finally {
			return this.open();
		}
	}

	/**
	 * Open connection;
	 * 
	 * @return state of connection;
	 */
	public boolean open() {
		return this.database.isOpen();
	}

	/**
	 * Close connection;
	 * 
	 * @return state of connection;
	 */
	public boolean close() {
		this.database.close();
		return !this.database.isOpen();
	}

	@Override
	public boolean insert(ContentValues data) throws SQLiteManagerException {
		long row = database.insert(getTable(), null, data);
		return row != -1;
	}

	@Override
	public boolean update(T data) throws SQLiteManagerException {
		ContentValues values = getContentValuesByData(data);
		long row = database.update(getTable(), values, SQLiteField.Field.ID.toString().concat("=?"), new String[] { data.getId().toString() });
		return row != 0;
	}
	
	@Override
	public boolean delete(T data) throws SQLiteManagerException {
		long row = database.delete(getTable(), SQLiteField.Field.ID.toString().concat("=?"), new String[] { data.getId().toString() });
		return row != 0;
	}

	@Override
	public List<T> select() throws SQLiteManagerException {
		List<T> list = new ArrayList<T>();
		Cursor cursor = database.query(getTable(), getColumns(), null, null, null, null, null);
		
		cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	
	      T t = parserCursor(cursor);
	      list.add(t);
	      cursor.moveToNext();
	    }
	    cursor.close();
		return list;
	}
	
	/**
	 * Insert and return T
	 * @param data
	 * @return
	 * @throws SQLiteManagerException
	 */
	public T insertData(ContentValues data) throws SQLiteManagerException {
		if (insert(data)) {
			return selectByID((Integer) data.get(SQLiteField.Field.ID
					.toString()));
		}
		throw new SQLiteManagerException("Is Impossible insert Object in table");
	}

	/**
	 * Return contentValues by Data.
	 * @param data
	 * @return
	 */
	public abstract ContentValues getContentValuesByData(T data);

	/**
	 * Return table name
	 * @return
	 */
	public abstract String getTable();
	
	/**
	 * Return columns names.
	 * @return
	 */
	public abstract String [] getColumns(); 
	

	/**
	 * Return object
	 * @param cursor
	 * @return
	 */
	public abstract T parserCursor(Cursor cursor);

}
