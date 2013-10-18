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
	 * @return state of transaction;
	 */
	public boolean insert(ContentValues data) throws SQLiteManagerException;
	
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
	 */
	public boolean update(T data) throws SQLiteManagerException;
	
	/**
	 * Select {@link T} object by ID;
	 * @param id parameter to select;
	 * @return {@link T};
	 */
	public T selectByID(Integer id) throws SQLiteManagerException;
	
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
