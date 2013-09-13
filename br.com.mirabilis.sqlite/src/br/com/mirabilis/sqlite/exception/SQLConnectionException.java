package br.com.mirabilis.sqlite.exception;


/**
 * Exception do SQLConnection
 * @author Rodrigo Sim�es Rosa
 *
 */

public class SQLConnectionException extends Exception {
	
	/**
	 * Serializa��o
	 */
	private static final long serialVersionUID = 1L;

	public SQLConnectionException() {
		this("Erro no sql connection");
	}
	
	public SQLConnectionException(Throwable ex){
		super(ex);
	}
	
	public SQLConnectionException(String ex){
		super(ex);
	}
}
