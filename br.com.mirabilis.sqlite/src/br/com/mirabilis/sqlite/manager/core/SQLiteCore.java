package br.com.mirabilis.sqlite.manager.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

public class SQLiteCore {
	
	private int version;
	private String pathSQLiteFile;
	
	private Context context;
	private DBMap database;
	public List<SQLiteEntity> entitys;
	
	private CypherType cypher;
	private CypherFileManager cypherManager;
	private SQLiteDatabaseFile databaseName;
	
	public SQLiteCore(Context context, SQLiteDatabaseFile databaseName, int version) {
		this.context = context;
		this.databaseName = databaseName;
		this.version = version;
	}

	/**
	 * Initialize database;
	 * @throws IOException
	 * @throws SQLiteManagerException
	 */
	public void start() throws IOException, SQLiteManagerException {
		
		if(entitys == null){
			mapping();	
		}
		
		try {
			if(this.cypher != null){
				File file = getFileSQLite();
				this.cypherManager = new CypherFileManager(file, cypher);
				this.cypherManager.decrypt();
			}
		} catch (SQLiteManagerException e) {
			create();
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
		
		String absolutePath = pathSQLiteFile.concat(databaseName.getAbsolutePath()).concat("/databases");
		
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
	public class Builder {
		
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
			this.instance.entitys = entitys;
			return this;
		}
		
		public SQLiteCore build(){
			return this.instance;
		}
	}
}
