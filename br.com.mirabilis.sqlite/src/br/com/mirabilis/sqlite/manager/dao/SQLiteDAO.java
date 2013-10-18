package br.com.mirabilis.sqlite.manager.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.dao.util.ContentValuesCreator;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteField;
import br.com.mirabilis.sqlite.manager.model.SQLiteTable;

/**
 * Class abstract of DAO.
 * @author Rodrigo Simões Rosa
 * @param <T>
 * Object.
 */
public abstract class SQLiteDAO<T extends SQLiteTable> implements DAO<T> {

	protected SQLiteDatabase database;
	protected Class<T> classHasAnnotation;

	/**
	 * Constructor;
	 * 
	 * @param database
	 */
	public SQLiteDAO(SQLiteDatabase database, Class<T> classHasAnnotation) {
		this.database = database;
		this.classHasAnnotation = classHasAnnotation;
	}

	/**
	 * Execute query
	 * 
	 * @param query
	 * @return {@link Cursor}
	 * @throws SQLConnectionException
	 */
	@SuppressWarnings("finally")
	public final Cursor query(String query) throws SQLiteManagerException {
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
	public final boolean open(SQLiteOpenHelper sqliteOpenHelper) {
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
	public final boolean open() {
		return this.database.isOpen();
	}

	/**
	 * Close connection;
	 * 
	 * @return state of connection;
	 */
	public final boolean close() {
		this.database.close();
		return !this.database.isOpen();
	}

	@Override
	public final long insert(T data) throws SQLiteManagerException {
		long row = 0;
		try{
			row = database.insert(getTable(), null, getContentValuesByData(data));
		}catch(Throwable a){
			throw new SQLiteManagerException("Error " + a.getMessage());
		}
		return row;
	}
	
	@Override
	public final boolean add(T data) throws SQLiteManagerException{
		long row = insert(data);
		return row != -1;
	}
	
	@Override
	public final T persist(T data) throws SQLiteManagerException {
		long row = insert(data);
		if (row != -1) {
			return selectByID(row);
		}
		throw new SQLiteManagerException("Is Impossible insert Object in table");
	}

	@Override
	public final boolean update(T data) throws SQLiteManagerException, IllegalArgumentException, IllegalAccessException {
		ContentValues values = getContentValuesByData(data);
		long row = 0; 
		try{
			row = database.update(getTable(), values, SQLiteField.Field.ID.toString().concat("=?"), new String[] { String.valueOf(data.getId()) });	
		}catch(Throwable a){
			throw new SQLiteManagerException("Error " + a.getMessage());
		}
		return row != 0;
	}
	
	@Override
	public final boolean delete(T data) throws SQLiteManagerException {
		long row = 0;
		try{
			row = database.delete(getTable(), SQLiteField.Field.ID.toString().concat("=?"), new String[] { String.valueOf(data.getId()) });	
		}catch(Throwable a){
			throw new SQLiteManagerException("Error " + a.getMessage());
		}
		return row != 0;
	}
	
	@Override
	public final List<T> select(int limit) throws SQLiteManagerException {
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try{
			if(limit == 0){
				cursor = database.query(getTable(), getColumns(), null, null, null, null, null);
			}else{
				cursor = database.query(getTable(), getColumns(), null, null, null, null, String.valueOf(limit));
			}
			cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	T t = parser(cursor);
		    	list.add(t);
		    	cursor.moveToNext();
		    }
		}catch(Throwable a){
			throw new SQLiteManagerException("Error " + a.getMessage());
		}finally{
			cursor.close();
		}
		return list;
	}
	
	@Override
	public final T selectByID(long id) throws SQLiteManagerException {
		Cursor cursor = null;
		T data = null;
		try{
			cursor = database.query(getTable(), getColumns(), SQLiteField.Field.ID.toString().concat("=?"), new String[] { String.valueOf(id) }, null, null, null, null);
			if(cursor.moveToFirst() == false){
				return null;
			}
			data = parser(cursor);
		}catch(Throwable a){
			throw new SQLiteManagerException("Error " + a.getMessage());
		}finally{
			cursor.close();
		}
		return data; 
	}
	
	@Override
	public final List<T> selectByPage(int limit, int page) throws SQLiteManagerException {
		List<T> list = new ArrayList<T>();
		Cursor cursorCount = null;
		Cursor cursor = null;
		String queryCount = "select count(*) from ".concat(getTable());
		int count = 0;
		int offset  = 0;
		try{
			cursorCount = database.rawQuery(queryCount, null);
			cursorCount.moveToFirst();
			if (cursorCount.getCount() > 0 && cursorCount.getColumnCount() > 0) {
				count = cursorCount.getInt(0);
			}
			offset = (count / limit) * page; 	
			
			cursor = database.query(getTable(), getColumns(), null, null, null, null, String.valueOf(limit).concat(" offset ").concat(String.valueOf(offset)));
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				T t = parser(cursor);
				list.add(t);
				cursor.moveToNext();
			}
		}catch(Throwable a){
			throw new SQLiteManagerException("Error " + a.getMessage());
		}finally{
			cursorCount.close();
			cursor.close();
		}
		return list;
	}

	@Override
	public final List<T> select() throws SQLiteManagerException {
		return select(0);
	}

	/**
	 * Return table name
	 * @return
	 * @throws SQLiteManagerException 
	 */
	private String getTable() throws SQLiteManagerException{
		if(classHasAnnotation.isAnnotationPresent(SQLiteAnnotationEntity.class)){
			SQLiteAnnotationEntity annotation = classHasAnnotation.getAnnotation(SQLiteAnnotationEntity.class);
			return annotation.name();
		}
		throw new SQLiteManagerException(" Class ".concat(classHasAnnotation.getName()).concat(" isn't valid SQLiteAnnotationEntity"));
	}
	
	/**
	 * Return columns names.
	 * @return
	 * @throws SQLiteManagerException 
	 */
	private String [] getColumns() throws SQLiteManagerException{
		List<String> columns = new ArrayList<String>();
		if(classHasAnnotation.isAnnotationPresent(SQLiteAnnotationEntity.class)){
			
			/**
			 * Recovery superclass field.
			 */
			for(Field field : classHasAnnotation.getSuperclass().getDeclaredFields()){
				if(field.isAnnotationPresent(SQLiteAnnotationField.class)){
					columns.add(field.getAnnotation(SQLiteAnnotationField.class).name());	
				}
			}
			
			/**
			 * Recovery fields.
			 */
			for(Field field : classHasAnnotation.getDeclaredFields()){
				if(field.isAnnotationPresent(SQLiteAnnotationField.class)){
					columns.add(field.getAnnotation(SQLiteAnnotationField.class).name());	
				}
			}
			
			if(columns.isEmpty()){
				throw new SQLiteManagerException(" Not finded fields in class ".concat(classHasAnnotation.getName()).concat(" valid as SQLiteAnnotationField"));
			}
			
			return (String []) columns.toArray();
		}else{
			throw new SQLiteManagerException(" Class ".concat(classHasAnnotation.getName()).concat(" isn't valid SQLiteAnnotationEntity"));
		}
	}
	
	/**
	 * Return object parsed;
	 * @param cursor don't forget call cursor.close(), before do parser;
	 * @return
	 */
	public abstract T parser(Cursor cursor);

	/**
	 * Return contentValues by Data.
	 * @param data
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public ContentValues getContentValuesByData(T data) throws IllegalArgumentException, IllegalAccessException{
		return ContentValuesCreator.creator(data);
	}
}
