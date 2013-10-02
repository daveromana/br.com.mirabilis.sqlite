package br.com.mirabilis.sqlite.manager.exception;

/**
 * Exception from {@link SQLiteManagerException}
 * @author Rodrigo Simões Rosa
 */
public class SQLiteManagerException extends Exception {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -4913326707914327547L;
	
	public SQLiteManagerException() {
		super("SQLiteManagerException exception");
	}
	
	public SQLiteManagerException(Throwable throwable){
		super(throwable);
	}
	
	public SQLiteManagerException(String exception){
		super(exception);
	}
}
