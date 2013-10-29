package br.com.mirabilis.sqlite.manager.exception;

/**
 * Exception from {@link SQLiteException}
 * 
 * @author Rodrigo Simões Rosa
 */
public class SQLiteException extends Exception {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -4913326707914327547L;

	public SQLiteException() {
		super("SQLiteManagerException exception");
	}

	public SQLiteException(Throwable throwable) {
		super(throwable);
	}

	public SQLiteException(String exception) {
		super(exception);
	}
}
