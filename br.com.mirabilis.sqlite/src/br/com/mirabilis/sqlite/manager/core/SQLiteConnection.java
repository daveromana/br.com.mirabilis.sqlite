package br.com.mirabilis.sqlite.manager.core;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mirabilis.sqlite.manager.exception.SQLiteErrorException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;

/**
 * Class of creation database; 
 * @author Rodrigo Simões Rosa
 */
public class SQLiteConnection extends SQLiteOpenHelper {

	private static final String TAG = SQLiteConnection.class.getName();
	private LinkedHashMap<String, SQLiteEntity> entitys;
	private SQLiteDatabase database;
	public Context context;

	public SQLiteConnection(Context context, SQLiteDatabaseFile database, int version) {
		super(context, database.getDatabase(), null, version);
		this.database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		this.database = database;
		if(entitys != null){
			createTables();	
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

	public void createTables() {
		for (Map.Entry<String, SQLiteEntity> e : entitys.entrySet()) {
			String query = e.getValue().getQueryCreateEntity();
			this.database.execSQL(query);
			Log.w(TAG, "ENTITY CREATED : " + query);
		}
	}


	public void connect() throws SQLException, SQLiteErrorException {
		try {
			this.database = this.getWritableDatabase();
			if (this.database.isOpen()) {
				this.database.close();
			}
		} catch (Exception e) {
			throw new SQLiteErrorException("Error with connection SQLite : " + e.getMessage());
		}
	}

	/**
	 * Builder {@link SQLiteConnection}
	 * @author Rodrigo Simões Rosa
	 */
	public static class Builder {

		private SQLiteConnection instance;

		public Builder(Context context, SQLiteDatabaseFile database, int version) {
			this.instance = new SQLiteConnection(context, database, version);
		}

		public Builder entitys(LinkedHashMap<String, SQLiteEntity> entitys) {
			this.instance.entitys = entitys;
			this.instance.createTables();
			return this;
		}

		public Builder entity(SQLiteEntity entity) {
			this.instance.entitys.put(entity.getNameEntity(), entity);
			return this;
		}

		public SQLiteConnection build() {
			return instance;
		}
	}

	public SQLiteDatabase getDatabase() {
		return this.database;
	}

}
