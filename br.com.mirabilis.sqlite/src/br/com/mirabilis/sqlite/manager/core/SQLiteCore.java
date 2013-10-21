package br.com.mirabilis.sqlite.manager.core;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.com.mirabilis.sqlite.annotation.SQLiteParseAnnotation;
import br.com.mirabilis.sqlite.cypher.CypherFileManager;
import br.com.mirabilis.sqlite.cypher.CypherType;
import br.com.mirabilis.sqlite.manager.exception.SQLiteErrorException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;

/**
 * {@link SQLiteCore} Class of core from br.com.mirabilis.sqlite.
 * @author Rodrigo Simões Rosa
 */
public class SQLiteCore {
	
	private int version;
	private String pathSQLiteFile;
	
	private Context context;
	private LinkedHashMap<String, SQLiteEntity> entitys;
	
	private CypherType cypher;
	private CypherFileManager cypherManager;
	private SQLiteDatabaseFile databaseName;
	private SQLiteConnection connection;
	
	/**
	 * Default crypto;
	 */
	{
		cypher = CypherType.BASE64;
	}
	
	private SQLiteCore(Context context, SQLiteDatabaseFile databaseName, int version) {
		this.context = context;
		this.databaseName = databaseName;
		this.version = version;
	}

	/**
	 * Initialize database;
	 * @throws IOException
	 * @throws SQLiteErrorException
	 */
	public void start() throws SQLiteErrorException, IOException {
		if(this.entitys == null || this.entitys.isEmpty()){
			throw new SQLiteErrorException("There is no entity or a mapping file, enter one of these information to initialize the database.");
		}
		
		//try {
		//	File file = getFileSQLite();
			if(this.cypher == null){
				create();
			}else{
				//this.cypherManager = new CypherFileManager(file, cypher);
				//this.cypherManager.decrypt();
				create();
			}
		//} catch (SQLiteManagerException e) {
			create();
			//this.cypherManager = new CypherFileManager(getPathSQLiteDefaultApplication(context, databaseName.getDatabase()), cypher);
			//this.cypherManager.encrypt();
	//	} catch (IOException e) {
	//		throw new SQLiteManagerException("An error occurred while decrypting the file.");
	//	}
	}
	
	/**
	 * Return {@link SQLiteDatabase}
	 * @return
	 */
	public SQLiteDatabase getDatabase(){
		if(connection.getDatabase().isOpen()){
			return connection.getDatabase();
		}
		return null;
	}
	
	public SQLiteConnection getConnection() {
		return connection;
	}

	/**
	 * Create database;
	 * @throws SQLiteErrorException
	 */
	public void create() throws SQLiteErrorException {
		connection = null; 
		
		if(entitys != null){
			connection = new SQLiteConnection.Builder(context, databaseName, version).entitys(entitys).build();
		}else{
			connection = new SQLiteConnection.Builder(context, databaseName, version).build();
		}
		connection.connect();
	}

	/**
	 * Get path with database folder concat
	 * @return
	 * @throws SQLiteErrorException
	 */
	private File getFileSQLite() throws SQLiteErrorException {
		String absolutePath = null;
		
		if(pathSQLiteFile == null){
			absolutePath = getPathSQLiteDefaultApplication(context, databaseName.getDatabase()).getAbsolutePath();
		}else{
			absolutePath = pathSQLiteFile;
		}
		
		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		}
		throw new SQLiteErrorException("File SQLite not exist in directory : " + absolutePath);
	}

	/**
	 * Recovery path of file .db for application.
	 * @param context
	 * @param databaseName DATABASENAME + ".db"
	 * @return File of database
	 */
	public static File getPathSQLiteDefaultApplication(Context context, String databaseName){
		return context.getDatabasePath(databaseName);
	}
	
	/**
	 * Retorn o path depend of Version SDK.
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
	 * @author Rodrigo Simões Rosa.
	 */
	public static class Builder {
		
		private SQLiteCore instance;
		
		public Builder(Context context, SQLiteDatabaseFile databaseName, int version) {
			this.instance = new SQLiteCore(context, databaseName, version);
		}
		
		public Builder pathSQLiteFile(String path){
			this.instance.pathSQLiteFile = path;
			return this;
		}
		
		public Builder cypher(CypherType type){
			this.instance.cypher = type;
			return this;
		}
		
		public Builder databases(Class<?> ... entitys) throws SQLiteErrorException{
			if(this.instance.entitys == null){
				this.instance.entitys = new LinkedHashMap<String, SQLiteEntity>();
			}
			for(Class<?> classHasAnnotation: entitys){
				SQLiteEntity s = SQLiteParseAnnotation.getValuesFromAnnotation(classHasAnnotation);
				this.instance.entitys.put(s.getNameEntity(), s);
			}
			return this;
		}
		
		public Builder database(List<Class<?>> list) throws SQLiteErrorException {
			databases((Class<?> [])list.toArray());
			return this;
		}
		
		public SQLiteCore build(){
			return this.instance;
		}
	}
}
