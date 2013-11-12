package br.com.mirabilis.sqlite.manager.model;

/**
 * Abstract TO.
 * @author Rodrigo Sim�e Rosa.
 *
 * @param <T>
 */
public abstract class SQLiteTO<T> {

	protected T instance;
	
	public SQLiteTO(T instance) {
		this.instance = instance;
	}
	
	public T getInstance() {
		return instance;
	}
}
