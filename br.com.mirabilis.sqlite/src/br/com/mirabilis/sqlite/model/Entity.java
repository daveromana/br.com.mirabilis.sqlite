package br.com.mirabilis.sqlite.model;

/**
 * Interface de cria��o de entidades para o SQLite.
 * @author Rodrigo Sim�es Rosa.
 *
 */
public interface Entity {
	
	public String getQueryCreateEntity();
	public String getNameEntity();
	
	public String setNameEntity(String name);
}
