package br.com.mirabilis.sqlite.enumeration;

/**
 * Retorna campos padr�es que podem ser criados pelas entidades.
 * @author Rodrigo Sim�es Rosa.
 */
public enum SQLiteFieldDefault {
	ID("_id"), NAME("name");
	
	private String value;
	
	private SQLiteFieldDefault(String value){
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}
