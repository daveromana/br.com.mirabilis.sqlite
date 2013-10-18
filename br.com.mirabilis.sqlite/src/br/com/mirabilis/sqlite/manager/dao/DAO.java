package br.com.mirabilis.sqlite.manager.dao;

import java.util.List;

import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
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
	 * @throws SQLiteManagerException
	 */
	public long insert(T data) throws SQLiteManagerException;
	
	/**
	 * Insert and return boolean
	 * @param data
	 * @return
	 * @throws SQLiteManagerException
	 */
	public boolean add(T data) throws SQLiteManagerException;
	
	/**
	 * Insert and return T
	 * @param data
	 * @return
	 * @throws SQLiteManagerException
	 */
	public T persist(T data) throws SQLiteManagerException;
	
	/**
	 * Delete {@link T} data;
	 * @param data
	 * @return state of transaction;
	 */
	public boolean delete(T data) throws SQLiteManagerException;
	

	/**
	 * Update {@link T} data;
	 * @param data
	 * @return state of transaction;
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public boolean update(T data) throws SQLiteManagerException, IllegalArgumentException, IllegalAccessException;
	
	/**
	 * Select {@link T} object by ID;
	 * @param id parameter to select;
	 * @return {@link T};
	 */
	public T selectByID(long id) throws SQLiteManagerException;
	
	/**
	 * Select all {@link T} objects;
	 * @return {@link List<T>};
	 */
	public List<T> select() throws SQLiteManagerException;
	
	/**
	 * Select all {@link T} by limit;
	 * @param limit Limit of objects to select
	 * @return {@link List<T>}; 
	 */
	public List<T> select(int limit) throws SQLiteManagerException;
	
	/**
	 * Select all {@link T} by limit;
	 * @param limit Limit of objects to select
	 * @param page number of page select that depend of limit;
	 * @return {@link List<T>};
	 */
	public List<T> selectByPage(int limit, int page) throws SQLiteManagerException;
}
