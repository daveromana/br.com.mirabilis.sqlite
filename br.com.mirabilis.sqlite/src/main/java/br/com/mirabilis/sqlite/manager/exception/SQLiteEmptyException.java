package br.com.mirabilis.sqlite.manager.exception;

/**
 * Exception of control result of query when that is empty.
 *
 * @author Rodrigo Sim�es Rosa.
 */
public class SQLiteEmptyException extends Exception {

    /**
     * Serialization
     */
    private static final long serialVersionUID = 5730852278529610005L;

    public SQLiteEmptyException(String msg) {
        super(msg);
    }

    public SQLiteEmptyException() {
        this("The result of query in sqlite is empty");
    }

    public SQLiteEmptyException(Throwable throwable) {
        super(throwable);
    }
}
