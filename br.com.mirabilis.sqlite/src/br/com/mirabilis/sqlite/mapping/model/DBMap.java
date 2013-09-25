package br.com.mirabilis.sqlite.mapping.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("database")
public class DBMap {

	private String name;
	private List<EntityMap> entitys;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<EntityMap> getEntitys() {
		return entitys;
	}
	public void setEntitys(List<EntityMap> entitys) {
		this.entitys = entitys;
	}
}
