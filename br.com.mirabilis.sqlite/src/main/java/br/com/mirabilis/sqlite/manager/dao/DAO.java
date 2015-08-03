package br.com.mirabilis.sqlite.manager.dao;

import java.util.List;

import br.com.mirabilis.sqlite.manager.exception.SQLiteEmptyException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException;


/**
 * Interface for DAO.
 *
 * @param <T>
 * @author Rodrigo Sim�es Rosa.
 */
public interface DAO<T> {

    /**
     * Insert {@link android.content.ContentValues} data;
     *
     * @param data
     * @return
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public long insert(T data) throws SQLiteException,
            SQLiteNotNullFieldException;

    /**
     * Insert and return boolean
     *
     * @param data
     * @return
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException
     */
    public boolean add(T data) throws SQLiteException,
            SQLiteNotNullFieldException;

    /**
     * Insert and return T
     *
     * @param data
     * @return
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException
     */
    public T persist(T data) throws SQLiteException, SQLiteEmptyException,
            SQLiteNotNullFieldException;

    /**
     * Delete {@link T} data;
     *
     * @param data
     * @return state of transaction;
     */
    public boolean delete(T data) throws SQLiteException;

    /**
     * Update {@link T} data;
     *
     * @param data
     * @return state of transaction;
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException
     */
    public boolean update(T data) throws SQLiteException,
            SQLiteNotNullFieldException;

    /**
     * Select {@link T} object by ID;
     *
     * @param id parameter to select;
     * @return {@link T};
     */
    public T selectByID(long id) throws SQLiteException, SQLiteEmptyException;

    /**
     * Select all {@link T} objects;
     *
     * @return {@link java.util.List<T>};
     */
    public List<T> select() throws SQLiteException, SQLiteEmptyException;

    /**
     * Delete all itens.
     *
     * @return
     * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
     */
    public boolean delete() throws SQLiteException;

    /**
     * Select all {@link T} by limit;
     *
     * @param limit Limit of objects to select
     * @return {@link java.util.List<T>};
     */
    public List<T> select(int limit) throws SQLiteException,
            SQLiteEmptyException;

    /**
     * Clean all register from table.
     *
     * @return
     */
    public boolean clean() throws SQLiteException;

    /**
     * Select all {@link T} by limit;
     *
     * @param limit Limit of objects to select
     * @param page  number of page select that depend of limit;
     * @return {@link java.util.List<T>};
     */
    public List<T> selectByPage(int limit, int page) throws SQLiteException,
            SQLiteEmptyException;
}
