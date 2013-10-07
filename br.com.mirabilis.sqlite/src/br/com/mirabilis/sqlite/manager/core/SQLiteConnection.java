package br.com.mirabilis.sqlite.manager.core;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;

/**
 * Class of creation database; 
 * @author Rodrigo Simões Rosa
 */
public class SQLiteConnection extends SQLiteOpenHelper {

	private static final String TAG = SQLiteConnection.class.getName();
	private List<SQLiteEntity> entitys;
	private SQLiteDatabase database;
	public Context context;

	public SQLiteConnection(Context context, SQLiteDatabaseFile database, int version) {
		super(context, database.getDatabase(), null, version);
		this.database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		for (SQLiteEntity e : entitys) {
			db.execSQL("DROP TABLE IF EXISTS " + e.getNameEntity());
			onCreate(db);
		}
	}


	public void createTables() {
		for (SQLiteEntity e : entitys) {
			this.database.execSQL(e.getQueryCreateEntity());
			Log.w(TAG, "ENTITY CREATED : " + e.getNameEntity());
		}
	}


	public void connect() throws SQLException, SQLiteManagerException {
		try {
			this.database = this.getWritableDatabase();
			if (this.database.isOpen()) {
				this.database.close();
			}
		} catch (Exception e) {
			throw new SQLiteManagerException("Error with connection SQLite");
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

		public Builder entitys(List<SQLiteEntity> entitys) {
			this.instance.entitys = entitys;
			return this;
		}

		public Builder entity(SQLiteEntity entity) {
			this.instance.entitys.add(entity);
			return this;
		}

		public SQLiteConnection build() {
			return instance;
		}
	}

}
