package br.com.mirabilis.sqlite.manager.core;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;

/**
 * Class of creation database;
 * 
 * @author Rodrigo Simões Rosa
 */
public final class SQLiteConnection extends SQLiteOpenHelper {

	private static final String TAG = SQLiteConnection.class.getName();
	private LinkedHashMap<String, SQLiteEntity> entitys;
	private SQLiteDatabase database;
	public Context context;

	/**
	 * Construtor
	 * 
	 * @param context
	 * @param database
	 * @param version
	 */
	public SQLiteConnection(Context context, SQLiteDatabaseFile database,
			int version) {
		super(context, database.getDatabase(), null, version);
		this.database = getWritableDatabase();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if(!db.isReadOnly()){
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		this.database = database;
		if (entitys != null) {
			try {
				createTables();
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		for (Map.Entry<String, SQLiteEntity> e : entitys.entrySet()) {
			db.execSQL("DROP TABLE IF EXISTS " + e.getValue().getNameEntity());
			onCreate(db);
		}
	}

	/**
	 * Create tables
	 * @throws NoSuchFieldException 
	 */
	public void createTables() throws NoSuchFieldException {
		for (Map.Entry<String, SQLiteEntity> e : entitys.entrySet()) {
			String query = e.getValue().getQueryCreateEntity();
			this.database.execSQL(query);
			Log.w(TAG, "ENTITY CREATED : " + query);
		}
	}

	/**
	 * Do connection with database
	 * 
	 * @throws SQLException
	 * @throws SQLiteException
	 */
	public void connect() throws SQLException, SQLiteException {
		try {
			this.database = this.getWritableDatabase();
			if (this.database.isOpen()) {
				this.database.close();
			}
		} catch (Exception e) {
			throw new SQLiteException("Error with connection SQLite : "
					+ e.getMessage());
		}
	}

	/**
	 * Get {@link SQLiteDatabase}
	 * 
	 * @return
	 */
	public SQLiteDatabase getDatabase() {
		return this.database;
	}

	/**
	 * Builder {@link SQLiteConnection}
	 * 
	 * @author Rodrigo Simões Rosa
	 */
	public static class Builder {

		private SQLiteConnection instance;

		/**
		 * Constructor
		 * 
		 * @param context
		 * @param database
		 * @param version
		 */
		public Builder(Context context, SQLiteDatabaseFile database, int version) {
			this.instance = new SQLiteConnection(context, database, version);
		}

		/**
		 * Set entitys
		 * 
		 * @param entitys
		 * @return
		 * @throws NoSuchFieldException 
		 */
		public Builder entitys(LinkedHashMap<String, SQLiteEntity> entitys) throws NoSuchFieldException {
			this.instance.entitys = entitys;
			this.instance.createTables();
			return this;
		}

		/**
		 * Set entity
		 * 
		 * @param entity
		 * @return
		 */
		public Builder entity(SQLiteEntity entity) {
			this.instance.entitys.put(entity.getNameEntity(), entity);
			return this;
		}

		/**
		 * Build {@link SQLiteConnection}
		 * 
		 * @return
		 */
		public SQLiteConnection build() {
			return instance;
		}
	}

}
