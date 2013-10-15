package br.com.mirabilis.sqlite.manager.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import br.com.mirabilis.sqlite.cypher.CypherFileManager;
import br.com.mirabilis.sqlite.cypher.CypherType;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;
import br.com.mirabilis.sqlite.mapping.MappingManager;
import br.com.mirabilis.sqlite.mapping.MappingManager.Mapping;
import br.com.mirabilis.sqlite.mapping.model.DBMap;

/**
 * {@link SQLiteCore} Class of core from br.com.mirabilis.sqlite.
 * @author Rodrigo Sim�es Rosa
 */
public class SQLiteCore {
	
	private int version;
	private String pathSQLiteFile;
	
	private Context context;
	private DBMap database;
	private LinkedHashMap<String, SQLiteEntity> entitys;
	
	private CypherType cypher;
	private CypherFileManager cypherManager;
	private SQLiteDatabaseFile databaseName;
	
	private SQLiteCore(Context context, SQLiteDatabaseFile databaseName, int version) {
		this.context = context;
		this.databaseName = databaseName;
		this.version = version;
	}
	
	public void addEntity(SQLiteEntity entity){
		if(this.entitys == null){
			this.entitys = new LinkedHashMap<String, SQLiteEntity>();
		}
		this.entitys.put(entity.getNameEntity(), entity);
	}

	/**
	 * Initialize database;
	 * @throws IOException
	 * @throws SQLiteManagerException
	 */
	public void start() throws SQLiteManagerException, IOException {
		if(entitys == null){
			try {
				mapping();
			} catch (IOException e) {
				throw new SQLiteManagerException("There is no entity or a mapping file, enter one of these information to initialize the database.");
			}	
		}
		
		try {
			File file = getFileSQLite();
			if(this.cypher == null){
				create();
			}else{
				this.cypherManager = new CypherFileManager(file, cypher);
				this.cypherManager.decrypt();
				create();
			}
		} catch (SQLiteManagerException e) {
			create();
			this.cypherManager = new CypherFileManager(getPathSQLiteDefaultApplication(context, databaseName.getDatabase()), cypher);
			this.cypherManager.encrypt();
		} catch (IOException e) {
			throw new SQLiteManagerException("An error occurred while decrypting the file.");
		}
	}

	/**
	 * Do mapping class entitys
	 * 
	 * @throws IOException
	 */
	private void mapping() throws IOException {
		InputStream in = this.context.getAssets().open(Mapping.MAPPING_FILE.toString());
		MappingManager map = new MappingManager(in);
		database = map.getDatabase();
	}

	/**
	 * Create database;
	 * @throws SQLiteManagerException
	 */
	public void create() throws SQLiteManagerException {
		SQLiteConnection connection = null; 
		
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
	 * @throws SQLiteManagerException
	 */
	private File getFileSQLite() throws SQLiteManagerException {
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
		throw new SQLiteManagerException("File SQLite not exist in directory : " + absolutePath);
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
	 * @author Rodrigo Sim�es Rosa.
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
		
		public Builder databases(List<SQLiteEntity> entitys){
			LinkedHashMap<String, SQLiteEntity> linkedHashMap = new LinkedHashMap<String, SQLiteEntity>();
			for(SQLiteEntity s: entitys){
				linkedHashMap.put(s.getNameEntity(), s);
			}
			this.instance.entitys = linkedHashMap;
			return this;
		}
		
		public SQLiteCore build(){
			return this.instance;
		}
	}
}
