package br.com.mirabilis.sqlite.manager.core;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.mirabilis.sqlite.annotation.SQLiteParseAnnotation;
import br.com.mirabilis.sqlite.cypher.CypherFileManager;
import br.com.mirabilis.sqlite.cypher.CypherFileManager.CypherType;
import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;

/**
 * {@link SQLiteCore} Class of core from br.com.mirabilis.sqlite.
 * 
 * @author Rodrigo Simões Rosa
 */
public final class SQLiteCore {

	private int version;
	private String pathSQLiteFile;

	private Context context;
	private LinkedHashMap<String, SQLiteEntity> entitys;

	private CypherType cypher;
	private CypherFileManager cypherManager;
	private SQLiteDatabaseFile databaseFile;
	private SQLiteConnection connection;
	private File file;
	
	private int updateConflit;
	
	/**
	 * Boot block
	 */
	{
		this.updateConflit = SQLiteDatabase.CONFLICT_FAIL;
	}

	/**
	 * Constructor default
	 * 
	 * @param context
	 * @param databaseName
	 * @param version
	 */
	private SQLiteCore(Context context, SQLiteDatabaseFile databaseName,
			int version) {
		this.context = context;
		this.databaseFile = databaseName;
		this.version = version;
	}

	/**
	 * Initialize database;
	 * 
	 * @throws IOException
	 * @throws SQLiteException
	 * @throws NoSuchFieldException 
	 */
	public void start() throws SQLiteException, IOException, NoSuchFieldException {
		if (this.entitys == null || this.entitys.isEmpty()) {
			throw new SQLiteException(
					"There is no entity or a mapping file, enter one of these information to initialize the database.");
		}

		try {
			this.file = getFileSQLite(context, databaseFile, pathSQLiteFile);
			if (this.cypher == null) {
				connect();
			} else {
				decrypt(this.file);
				connect();
			}
		} catch (SQLiteException e) {
			connect();
			this.file = getPathSQLiteDefaultApplication(context,
					databaseFile.getDatabase());
			crypt(this.file);
		} catch (IOException e) {
			throw new SQLiteException(
					"An error occurred while decrypting the file.");
		} catch (NoSuchFieldException e) {
			throw new SQLiteException(e.getMessage());
		}
	}

	/**
	 * Set type action when exist conflit in update.
	 * @param updateConflit
	 */
	public void setUpdateConflit(int updateConflit) {
		this.updateConflit = updateConflit;
	}
	
	/**
	 * Return type action when exist conflit.
	 * @return
	 */
	public int getUpdateConflit() {
		return updateConflit;
	}
	
	/**
	 * Decrypt file of sqlite.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void decrypt(File file) throws IOException {
		this.cypherManager = new CypherFileManager(file, cypher);
		this.cypherManager.decrypt();
	}

	/**
	 * Decrypt file of sqlite.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void decrypt() throws IOException {
		decrypt(file);
	}

	/**
	 * Crypt file of sqlite.
	 * 
	 * @throws IOException
	 */
	public void crypt(File file) throws IOException {
		if (this.cypher != null) {
			this.cypherManager = new CypherFileManager(file, cypher);
			this.cypherManager.encrypt();
		}
	}

	/**
	 * Crypt file of sqlite.
	 * 
	 * @throws IOException
	 */
	public void crypt() throws IOException {
		crypt(this.file);
	}

	/**
	 * Return {@link SQLiteDatabase}
	 * 
	 * @return
	 */
	public SQLiteDatabase getDatabase() {
		if (this.connection.getDatabase().isOpen()) {
			return this.connection.getDatabase();
		}
		return null;
	}

	/**
	 * Return {@link String} of pathSQLiteFile
	 * 
	 * @return
	 */
	public String getPathSQLiteFile() {
		return this.pathSQLiteFile;
	}

	/**
	 * Return {@link SQLiteDatabaseFile}
	 * 
	 * @return
	 */
	public SQLiteDatabaseFile getDatabaseFile() {
		return this.databaseFile;
	}

	/**
	 * Return {@link Context}
	 * 
	 * @return
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * Get {@link SQLiteConnection}
	 * 
	 * @return
	 */
	public SQLiteConnection getConnection() {
		return this.connection;
	}

	/**
	 * Create database;
	 * 
	 * @throws SQLiteException
	 * @throws NoSuchFieldException 
	 */
	public void connect() throws SQLiteException, NoSuchFieldException {
		connection = null;

		if (entitys != null) {
			connection = new SQLiteConnection.Builder(context, databaseFile,
					version).entitys(entitys).build();
		} else {
			connection = new SQLiteConnection.Builder(context, databaseFile,
					version).build();
		}
		connection.connect();
	}

	/**
	 * Get {@link CypherType}
	 * 
	 * @return
	 */
	public CypherType getCypher() {
		return this.cypher;
	}
	
	/**
	 * Get version SQLite of Android.
	 * @return
	 */
	public static String getSQLiteVersion(){
		Cursor cursor = SQLiteDatabase.openOrCreateDatabase(":memory:", null).rawQuery("select sqlite_version() as sqlite_version", null);
		StringBuilder sqliteVersion = new StringBuilder();
		while(cursor.moveToNext()){
			sqliteVersion.append(cursor.getString(0));
		}
		return sqliteVersion.toString();
	}

	/**
	 * Get path with database folder concat
	 * 
	 * @return
	 * @throws SQLiteException
	 */
	public static File getFileSQLite(Context context,
			SQLiteDatabaseFile dbFile, String path) throws SQLiteException {
		String absolutePath = null;

		if (path == null) {
			absolutePath = getPathSQLiteDefaultApplication(context,
					dbFile.getDatabase()).getAbsolutePath();
		} else {
			absolutePath = path;
		}

		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		}
		throw new SQLiteException("File SQLite not exist in directory : "
				+ absolutePath);
	}

	/**
	 * Recovery path of file .db for application.
	 * 
	 * @param context
	 * @param databaseName
	 *            DATABASENAME + ".db"
	 * @return File of database
	 */
	public static File getPathSQLiteDefaultApplication(Context context,
			String databaseName) {
		return context.getDatabasePath(databaseName);
	}

	/**
	 * Retorn o path depend of Version SDK.
	 * 
	 * @param context
	 * @param sdk
	 * @return
	 */
	public static String getPathDatabaseByVersionSDK(Context context, int sdk) {
		StringBuilder path = new StringBuilder();
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			path.append("/data/data/");
		} else {
			path.append(context.getApplicationInfo().dataDir);
		}
		return path.toString();
	}

	/**
	 * Builder of {@link SQLiteCore}
	 * 
	 * @author Rodrigo Simões Rosa.
	 */
	public static class Builder {

		private SQLiteCore instance;

		public Builder(Context context, SQLiteDatabaseFile databaseName,
				int version) {
			this.instance = new SQLiteCore(context, databaseName, version);
		}

		public Builder pathSQLiteFile(String path) {
			this.instance.pathSQLiteFile = path;
			return this;
		}

		public Builder cypher(CypherType type) {
			this.instance.cypher = type;
			return this;
		}

		public Builder databases(Class<?>... entitys) throws SQLiteException {
			if (this.instance.entitys == null) {
				this.instance.entitys = new LinkedHashMap<String, SQLiteEntity>();
			}
			for (Class<?> classHasAnnotation : entitys) {
				SQLiteEntity s = SQLiteParseAnnotation
						.getValuesFromAnnotation(classHasAnnotation);
				this.instance.entitys.put(s.getNameEntity(), s);
			}
			return this;
		}

		public Builder database(List<Class<?>> list) throws SQLiteException {
			databases((Class<?>[]) list.toArray());
			return this;
		}
		
		public Builder updateConflit(int updateConflit){
			this.instance.updateConflit = updateConflit;
			return this;
		}

		public SQLiteCore build() {
			return this.instance;
		}
	}
}
