package br.com.mirabilis.sqlite.manager.model;

/**
 * Interface of entitys;
 * @author Rodrigo Sim�es Rosa. 
 */
public interface Entity {
	
	public String getQueryCreateEntity();
	public String getNameEntity();
	
	public String setNameEntity(String name);
}
