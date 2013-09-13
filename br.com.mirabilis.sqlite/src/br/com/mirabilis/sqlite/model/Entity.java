package br.com.mirabilis.sqlite.model;

/**
 * Interface de criação de entidades para o SQLite.
 * @author Rodrigo Simões Rosa.
 *
 */
public interface Entity {
	
	public String getQueryCreateEntity();
	public String getNameEntity();
	
	public String setNameEntity(String name);
}
