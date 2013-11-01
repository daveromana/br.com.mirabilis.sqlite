package br.com.mirabilis.sqlite.manager.dao;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.core.SQLiteCore;
import br.com.mirabilis.sqlite.manager.dao.util.SQLiteDataManager;
import br.com.mirabilis.sqlite.manager.exception.SQLiteEmptyException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.exception.SQLiteNotNullFieldException;
import br.com.mirabilis.sqlite.manager.model.SQLiteField;
import br.com.mirabilis.sqlite.manager.model.SQLiteTable;

/**
 * Class abstract of DAO.
 * 
 * @author Rodrigo Simões Rosa
 * @param <T>
 *            Object.
 */
public abstract class SQLiteDAO<T extends SQLiteTable> implements DAO<T> {

	protected SQLiteDatabase database;
	private Class<T> classHasAnnotation;
	private SQLiteCore core;

	/**
	 * Constructor;
	 * 
	 * @param database
	 * @throws IOException
	 * @throws SQLiteException
	 * @throws NoSuchFieldException
	 */
	public SQLiteDAO(SQLiteCore core, Class<T> classHasAnnotation)
			throws SQLiteException, IOException, NoSuchFieldException {
		core.start();
		this.database = core.getConnection().getWritableDatabase();
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
	public final Cursor query(String query) throws SQLiteException {
		Cursor cursor = null;
		try {
			cursor = this.database.rawQuery(query, null);
			cursor.moveToFirst();
		} catch (Throwable e) {
			throw new SQLiteException("Error " + e.getMessage()
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
			throw new SQLiteException("Error " + e.getMessage() + " on execute");
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
	public final long insert(T data) throws SQLiteException,
			SQLiteNotNullFieldException {
		long row = 0;

		try {
			row = database.insertOrThrow(getTable(), null,
					getContentValuesByData(data));
		}catch(SQLException e){
			throw new SQLiteException("Error " + e.getMessage());
		}catch (IllegalArgumentException e) {
			throw new SQLiteException("Error " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new SQLiteException("Error " + e.getMessage());
		}
		return row;
	}

	@Override
	public final boolean add(T data) throws SQLiteException,
			SQLiteNotNullFieldException {
		long row = insert(data);
		return row != -1;
	}

	@Override
	public final T persist(T data) throws SQLiteException,
			SQLiteEmptyException, SQLiteNotNullFieldException {
		long row = insert(data);
		if (row != -1) {
			return selectByID(row);
		}
		throw new SQLiteException("Is Impossible insert Object in table");
	}

	@Override
	public final boolean update(T data) throws SQLiteException,
			SQLiteNotNullFieldException {
		ContentValues values = null;
		long row = 0;
		try {
			values = getContentValuesByData(data);
			row = database.updateWithOnConflict(getTable(), values, SQLiteField.Field.ID
					.toString().concat("=?"), new String[] { String
					.valueOf(data.getId()) }, this.core.getUpdateConflit());
		} catch (IllegalArgumentException e) {
			throw new SQLiteException("Error " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new SQLiteException("Error " + e.getMessage());
		}
		return row != 0;
	}

	@Override
	public final boolean delete(T data) throws SQLiteException {
		long row = 0;
		try {
			row = database.delete(getTable(), SQLiteField.Field.ID.toString()
					.concat("=?"),
					new String[] { String.valueOf(data.getId()) });
		} catch (Throwable a) {
			throw new SQLiteException("Error " + a.getMessage());
		}
		return row != 0;
	}

	@Override
	public final List<T> select(int limit) throws SQLiteException,
			SQLiteEmptyException {
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		try {
			if (limit == 0) {
				cursor = database.query(getTable(), getColumns(), null, null,
						null, null, null);
			} else {
				cursor = database.query(getTable(), getColumns(), null, null,
						null, null, null, String.valueOf(limit));
			}
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				T t = parser(cursor);
				list.add(t);
				cursor.moveToNext();
			}
		} catch (Throwable a) {
			throw new SQLiteException("Error " + a.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		if (list.isEmpty()) {
			throw new SQLiteEmptyException();
		}

		return list;
	}

	@Override
	public final T selectByID(long id) throws SQLiteException,
			SQLiteEmptyException {
		Cursor cursor = null;
		T data = null;
		try {
			cursor = database.query(getTable(), getColumns(),
					SQLiteField.Field.ID.toString().concat("=?"),
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor.moveToFirst() == false) {
				return null;
			}
			data = parser(cursor);
		} catch (Throwable a) {
			throw new SQLiteException("Error " + a.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		if (data == null) {
			throw new SQLiteEmptyException();
		}
		return data;
	}

	@Override
	public final List<T> selectByPage(int limit, int page)
			throws SQLiteException, SQLiteEmptyException {
		List<T> list = new ArrayList<T>();
		Cursor cursor = null;
		int offset = 0;
		try {

			offset = (limit * (page - 1));

			cursor = database.query(
					getTable(),
					getColumns(),
					null,
					null,
					null,
					null,
					null,
					String.valueOf(offset).concat(", ")
							.concat(String.valueOf(limit)));
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				T t = parser(cursor);
				list.add(t);
				cursor.moveToNext();
			}
		} catch (Throwable a) {
			throw new SQLiteException("Error " + a.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		if (list.isEmpty()) {
			throw new SQLiteEmptyException();
		}

		return list;
	}

	@Override
	public final List<T> select() throws SQLiteException, SQLiteEmptyException {
		return select(0);
	}

	/**
	 * Return table name
	 * 
	 * @return
	 * @throws SQLiteException
	 */
	private String getTable() throws SQLiteException {
		if (classHasAnnotation
				.isAnnotationPresent(SQLiteAnnotationEntity.class)) {
			SQLiteAnnotationEntity annotation = classHasAnnotation
					.getAnnotation(SQLiteAnnotationEntity.class);
			return annotation.name();
		}
		throw new SQLiteException(" Class "
				.concat(classHasAnnotation.getName()).concat(
						" isn't valid SQLiteAnnotationEntity"));
	}

	/**
	 * Return columns names.
	 * 
	 * @return
	 * @throws SQLiteException
	 */
	private String[] getColumns() throws SQLiteException {
		List<String> columns = new ArrayList<String>();
		if (classHasAnnotation
				.isAnnotationPresent(SQLiteAnnotationEntity.class)) {

			/**
			 * Recovery superclass field.
			 */
			for (Field field : classHasAnnotation.getSuperclass()
					.getDeclaredFields()) {
				if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {
					columns.add(field
							.getAnnotation(SQLiteAnnotationField.class).name());
				}
			}

			/**
			 * Recovery fields.
			 */
			for (Field field : classHasAnnotation.getDeclaredFields()) {
				if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {
					columns.add(field
							.getAnnotation(SQLiteAnnotationField.class).name());
				}
			}

			if (columns.isEmpty()) {
				throw new SQLiteException(" Not finded fields in class "
						.concat(classHasAnnotation.getName()).concat(
								" valid as SQLiteAnnotationField"));
			}

			String[] temp = new String[columns.size()];
			columns.toArray(temp);
			return temp;
		} else {
			throw new SQLiteException(" Class ".concat(
					classHasAnnotation.getName()).concat(
					" isn't valid SQLiteAnnotationEntity"));
		}
	}

	/**
	 * Return object parsed;
	 * 
	 * @param cursor
	 *            don't forget call cursor.close(), before do parser;
	 * @return
	 */
	private T parser(Cursor cursor) throws InstantiationException, IllegalAccessException {
		T instance = classHasAnnotation.newInstance();
		
		/**
		 * Set value using superclass. 
		 */
		SQLiteDataManager.setValueInstance(cursor, instance, classHasAnnotation.getSuperclass().getDeclaredFields());
		
		/**
		 * Set value using class.
		 */
		SQLiteDataManager.setValueInstance(cursor, instance, classHasAnnotation.getDeclaredFields());
		
		return instance;
	}

	/**
	 * Return contentValues by Data.
	 * 
	 * @param data
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLiteNotNullFieldException
	 * @throws SQLiteException
	 */
	public ContentValues getContentValuesByData(T data)
			throws IllegalArgumentException, IllegalAccessException,
			SQLiteNotNullFieldException, SQLiteException {
		return SQLiteDataManager.getContentValues(data);
	}
}
