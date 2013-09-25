package br.com.mirabilis.sqlite;

import java.util.regex.Pattern;

import br.com.mirabilis.sqlite.exception.SQLManagerException;

/**
 * Valida o nome da base de dados.
 * @author Rodrigo Sim�es Rosa
 *
 */
public class SQLDatabase {
	
	private String database;
	
	/**
	 * Recebe uma string que � o nome da base de dados. 
	 * @param databaseName
	 * @throws SQLConnectionException 
	 */
	public SQLDatabase(String databaseName) throws SQLManagerException {
		if(Pattern.matches("^[a-zA-Z]+$",databaseName)){
			this.database = databaseName.concat(".db");
		}else{
			throw new SQLManagerException("O nome da base de dados � inv�lido");
		}
	}

	/**
	 * Retorna o nome da base de dados.
	 * @return
	 */
	public String getDatabaseName(){
		return this.database;
	}
}
