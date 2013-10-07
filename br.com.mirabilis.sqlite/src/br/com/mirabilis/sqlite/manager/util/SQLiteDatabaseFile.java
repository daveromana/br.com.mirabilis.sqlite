package br.com.mirabilis.sqlite.manager.util;

import java.util.regex.Pattern;

import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;

/**
 * Validate name of database;
 * @author Rodrigo Simões Rosa
 */
public class SQLiteDatabaseFile {

	private String database;
	private String path;

	/**
	 * Receipt {@link String} that of name from database; 
	 * @param databaseName
	 * @throws SQLConnectionException
	 */
	private SQLiteDatabaseFile(String databaseName) throws SQLiteManagerException {
		if (Pattern.matches("^[a-zA-Z]+$", databaseName)) {
			this.database = databaseName.concat(".db");
		} else {
			throw new SQLiteManagerException("O nome da base de dados é inválido");
		}
	}

	/**
	 * Return databaseName;
	 * @return
	 */
	public String getDatabase() {
		return database;
	}
	
	/**
	 * Return path;
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Return absolutePath
	 * @return
	 */
	public String getAbsolutePath() {
		if(path != null){
			return path.concat(database);	
		}
		return database;
	}
	
	/**
	 * Builder of {@link SQLiteDatabaseFile}
	 * @author Rodrigo Simões Rosa.
	 */
	public static class Builder {
		
		private SQLiteDatabaseFile instance;
		
		public Builder(String databaseName) throws SQLiteManagerException {
			this.instance = new SQLiteDatabaseFile(databaseName);
		}
		
		public Builder path(String path){
			this.instance.path = path;
			return this;
		}
		
		public SQLiteDatabaseFile build(){
			return this.instance;
		}
	}
}
