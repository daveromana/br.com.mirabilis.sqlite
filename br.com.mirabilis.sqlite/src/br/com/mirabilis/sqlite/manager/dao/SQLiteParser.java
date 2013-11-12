package br.com.mirabilis.sqlite.manager.dao;

import br.com.mirabilis.sqlite.manager.model.SQLiteTable;

/**
 * Interface to parser <T> to <U> or <U> to <T>
 * @author Rodrigo Simões Rosa.
 *
 * @param <T> {@link SQLiteTable}.
 * @param <U> Model (TO);
 */
public interface SQLiteParser<T,U> {

	/**
	 * Convert U Model (TO) to T {@link SQLiteTable}
	 * @param model
	 * @return
	 */
	public T convert(U model);
	
	/**
	 * Convert T {@link SQLiteTable} to U Model (TO)
	 * @param model
	 * @return
	 */
	public U parse(T table);
}
