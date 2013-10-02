package br.com.mirabilis.sqlite.manager.dao;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;

/**
 * Class abstract of DAO.
 * @author Rodrigo Simões Rosa
 * @param <T> Object.
 */
public abstract class SQLiteDAO<T> {
 
	protected SQLiteDatabase database;

	/**
	 * Constructor;
	 * @param database
	 */
	public SQLiteDAO(SQLiteDatabase database) {
		this.database = database;
	}
	
	/**
	 * Constructor;
	 */
	public SQLiteDAO() {}
	
	/**
	 * Execute query
	 * @param query
	 * @return {@link Cursor}
	 * @throws SQLConnectionException 
	 */
	@SuppressWarnings("finally")
	public Cursor query(String query) throws SQLiteManagerException {
		Cursor cursor = null;
		try{
			cursor = this.database.rawQuery(query, null);
			cursor.moveToFirst();
		}catch (Throwable e) {
			throw new SQLiteManagerException("Error " + e.getMessage() +" on execute query " + query);
		}finally{
			return cursor;
		}
	}
	
	/**
	 * Open connection;
	 * @param sqliteOpenHelper
	 * @return state of connection;
	 */
	public boolean open(SQLiteOpenHelper sqliteOpenHelper){
		this.database = sqliteOpenHelper.getWritableDatabase();
		return this.open();
	}
	
	/**
	 * Open connection;
	 * @return state of connection;
	 */
	public boolean open(){
		return this.database.isOpen();
	}
	
	/**
	 * Close connection;
	 * @return state of connection;
	 */
	public boolean close(){
		this.database.close();
		return !this.database.isOpen();
	}
	
	/**
	 * Insert {@link T} object;
	 * @param data
	 * @return state of transaction;
	 */
	public abstract boolean insert(T data);
	
	/**
	 * Update {@link T} object;
	 * @param data
	 * @return state of transaction;
	 */
	public abstract boolean update(T data);
	
	/**
	 * Delete {@link T} object;
	 * @param data
	 * @return state of transaction;
	 */
	public abstract boolean delete(T data);
	
	/**
	 * Select {@link T} object by ID;
	 * @param id parameter to select;
	 * @return {@link T};
	 */
	public abstract T selectByID(Integer id);
	
	/**
	 * Select all {@link T} objects;
	 * @return List<T>;
	 */
	public abstract List<T> select();
}
