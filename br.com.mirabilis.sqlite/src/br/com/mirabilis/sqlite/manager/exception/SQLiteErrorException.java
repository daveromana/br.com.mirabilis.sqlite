package br.com.mirabilis.sqlite.manager.exception;

/**
 * Exception from {@link SQLiteErrorException}
 * @author Rodrigo Simões Rosa
 */
public class SQLiteErrorException extends Exception {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -4913326707914327547L;
	
	public SQLiteErrorException() {
		super("SQLiteManagerException exception");
	}
	
	public SQLiteErrorException(Throwable throwable){
		super(throwable);
	}
	
	public SQLiteErrorException(String exception){
		super(exception);
	}
}
