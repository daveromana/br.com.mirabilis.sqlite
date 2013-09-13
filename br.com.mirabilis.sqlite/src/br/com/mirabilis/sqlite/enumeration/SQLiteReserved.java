package br.com.mirabilis.sqlite.enumeration;

/**
 * Palavras reservadas do SQLite.
 * @author Rodrigo Simões Rosa
 */
public enum SQLiteReserved {
	PRIMARY_KEY("primary key"),NOT_NULL("not null"),AUTOINCREMENT("autoincrement");
	
	private String reserved;
	
	private SQLiteReserved(String value) {
		this.reserved = value;
	}
	
	@Override
	public String toString() {
		return this.reserved;
	}
}
