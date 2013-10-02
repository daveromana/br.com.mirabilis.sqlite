package br.com.mirabilis.sqlite.mapping.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias ("entity")
public class EntityMap {

	private String classPath;

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
}
