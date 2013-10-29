package br.com.mirabilis.sqlite.manager.dao;

import java.util.List;

import br.com.mirabilis.sqlite.manager.exception.SQLiteEmptyException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException;
import android.content.ContentValues;

/**
 * Interface for DAO.
 * @author Rodrigo Simões Rosa.
 * @param <T>
 */
public interface DAO<T> {

	/**
	 * Insert {@link ContentValues} data;
	 * @param data
	 * @return
	 * @throws SQLiteException
	 * @throws SQLiteNotNullFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public long insert(T data) throws SQLiteException, SQLiteNotNullFieldException;
	
	/**
	 * Insert and return boolean
	 * @param data
	 * @return
	 * @throws SQLiteException
	 * @throws SQLiteNotNullFieldException 
	 */
	public boolean add(T data) throws SQLiteException, SQLiteNotNullFieldException;
	
	/**
	 * Insert and return T
	 * @param data
	 * @return
	 * @throws SQLiteException
	 * @throws SQLiteNotNullFieldException 
	 */
	public T persist(T data) throws SQLiteException, SQLiteEmptyException, SQLiteNotNullFieldException;
	
	/**
	 * Delete {@link T} data;
	 * @param data
	 * @return state of transaction;
	 */
	public boolean delete(T data) throws SQLiteException;
	

	/**
	 * Update {@link T} data;
	 * @param data
	 * @return state of transaction;
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SQLiteNotNullFieldException 
	 */
	public boolean update(T data) throws SQLiteException, SQLiteNotNullFieldException;
	
	/**
	 * Select {@link T} object by ID;
	 * @param id parameter to select;
	 * @return {@link T};
	 */
	public T selectByID(long id) throws SQLiteException, SQLiteEmptyException;
	
	/**
	 * Select all {@link T} objects;
	 * @return {@link List<T>};
	 */
	public List<T> select() throws SQLiteException, SQLiteEmptyException;
	
	/**
	 * Select all {@link T} by limit;
	 * @param limit Limit of objects to select
	 * @return {@link List<T>}; 
	 */
	public List<T> select(int limit) throws SQLiteException, SQLiteEmptyException;
	
	/**
	 * Select all {@link T} by limit;
	 * @param limit Limit of objects to select
	 * @param page number of page select that depend of limit;
	 * @return {@link List<T>};
	 */
	public List<T> selectByPage(int limit, int page) throws SQLiteException, SQLiteEmptyException;
}
