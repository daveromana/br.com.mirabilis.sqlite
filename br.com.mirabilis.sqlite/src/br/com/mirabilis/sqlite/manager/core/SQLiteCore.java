package br.com.mirabilis.sqlite.manager.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import br.com.mirabilis.sqlite.cypher.CypherFileManager;
import br.com.mirabilis.sqlite.cypher.CypherType;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile;
import br.com.mirabilis.sqlite.mapping.MappingManager;
import br.com.mirabilis.sqlite.mapping.MappingManager.Mapping;
import br.com.mirabilis.sqlite.mapping.model.DBMap;

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
	public void start() throws SQLiteManagerException {
		
		if(entitys == null){
			try {
				mapping();
			} catch (IOException e) {
				throw new SQLiteManagerException("There is no entity or a mapping file, enter one of these information to initialize the database.");
			}	
		}
		
		try {
			File file = getFileSQLite();
			if(file.exists()){
				if(this.cypher != null){
					this.cypherManager = new CypherFileManager(file, cypher);
					this.cypherManager.decrypt();
				}else{
					//TODO do something
				}
			}
		} catch (SQLiteManagerException e) {
			create();
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
		
		if(pathSQLiteFile == null){
			pathSQLiteFile = getPathDatabaseByVersionSDK(android.os.Build.VERSION.SDK_INT);
		}
		
		String absolutePath = Environment.getDataDirectory().getPath().concat("/databases/").concat(databaseName.getAbsolutePath());
		
		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		}
		throw new SQLiteManagerException("File SQLite not exist in directory : " + absolutePath);
	}

	public String getPathDatabaseByVersionSDK(int sdk) {
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
