package br.com.mirabilis.sqlite;

/**
 * Entidade modelo toda e qualquer entidade a ser criada no banco 
 * @author Rodrigo Sim�es Rosa
 *
 */
public class SQLEntity {
	
	private String entity;
	private String queryCreate;
	
	/**
	 * Retorna o nome da entidade; 
	 * @return
	 */
	public String getEntity() {
		return entity;
	}
	
	/**
	 * Seta o nome da entidade;
	 * @param entity
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	/**
	 * Retorna a query de cria��o da entidade;
	 * @return
	 */
	public String getQueryCreate() {
		return queryCreate;
	}
	
	/**
	 * Seta a query de cria��o da entidade;
	 * @param queryCreate
	 */
	public void setQueryCreate(String queryCreate) {
		this.queryCreate = queryCreate;
	}
}
