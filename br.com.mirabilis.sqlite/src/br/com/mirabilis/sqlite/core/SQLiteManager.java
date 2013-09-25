package br.com.mirabilis.sqlite.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import br.com.mirabilis.sqlite.SQLDatabase;
import br.com.mirabilis.sqlite.SQLEntity;
import br.com.mirabilis.sqlite.cypher.CypherFileManager;
import br.com.mirabilis.sqlite.cypher.CypherType;
import br.com.mirabilis.sqlite.exception.SQLManagerException;
import br.com.mirabilis.sqlite.mapping.MappingManager;
import br.com.mirabilis.sqlite.mapping.MappingManager.Mapping;
import br.com.mirabilis.sqlite.mapping.model.DBMap;

public class SQLiteManager {

	private String pathSQLiteFile;
	private String databaseName;
	private Context context;
	private DBMap database;
	public List<SQLEntity> entitys;
	
	private CypherType cypher;
	private CypherFileManager cypherManager;
	private int version;
	
	public SQLiteManager(Context context, int version) {
		this.context = context;
		this.version = version;
	}

	public void start() throws IOException, SQLManagerException {
		
		if(entitys == null){
			mapping();	
		}
		
		try {
			if(this.cypher != null){
				File file = getFileSQLite();
				file.renameTo(new File(file.getAbsolutePath().concat(".db")));
				this.cypherManager = new CypherFileManager(file, cypher);
				this.cypherManager.decrypt();
			}
		} catch (SQLManagerException e) {
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

	public void create() throws SQLManagerException {
		SQLConnection connection = null; 
		
		if(entitys != null){
			connection = new SQLConnection.Builder(context, new SQLDatabase(getFileSQLite().getAbsolutePath()), version).entitys(entitys).build();
		}else{
			connection = new SQLConnection.Builder(context, new SQLDatabase(getFileSQLite().getAbsolutePath()), version).build();
		}
		connection.connect();
	}

	/**
	 * Get path with database folder concat
	 * @return
	 * @throws SQLManagerException
	 */
	private File getFileSQLite() throws SQLManagerException {
		
		if(pathSQLiteFile == null){
			pathSQLiteFile = getPathDatabaseByVersionSDK(android.os.Build.VERSION.SDK_INT);
		}
		
		String absolutePath = pathSQLiteFile.concat(databaseName).concat("/databases");
		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		}
		throw new SQLManagerException("File SQLite not exist in directory :" + absolutePath);
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
	 * Builder of {@link SQLiteManager}
	 * @author Rodrigo Simões Rosa.
	 */
	public class Builder {
		
		private SQLiteManager instance;
		
		public Builder(Context context, int version) {
			this.instance = new SQLiteManager(context, version);
		}
		
		public Builder pathSQLiteFile(String path){
			this.instance.pathSQLiteFile = path;
			return this;
		}
		
		public Builder databaseName(String databaseName){
			this.instance.databaseName = databaseName;
			return this;
		}
		
		public Builder cypher(CypherType type){
			this.instance.cypher = type;
			return this;
		}
		
		public Builder databases(List<SQLEntity> entitys){
			this.instance.entitys = entitys;
			return this;
		}
		
		public SQLiteManager build(){
			return this.instance;
		}
	}
}
