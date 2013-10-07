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
	public SQLiteDatabaseFile(String databaseName, String path) throws SQLiteManagerException {
		if (Pattern.matches("^[a-zA-Z]+$", databaseName)) {
			this.database = databaseName.concat(".db");
			this.path = path;
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

	public String getAbsolutePath() {
		return path.concat(database);
	}
}
