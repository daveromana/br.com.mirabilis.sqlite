package br.com.mirabilis.sqlite.exception;

/**
 * {@link SQLManagerException}
 * @author Rodrigo Simões Rosa
 */
public class SQLManagerException extends Exception {

	private static final long serialVersionUID = 6622124490764832880L;
	/**
	 * Serialization
	 */

	public SQLManagerException() {
		this("SQLManager Error");
	}
	
	public SQLManagerException(String value) {
		super(value);
	}
	
	public SQLManagerException(Throwable a) {
		super(a);
	}
}
