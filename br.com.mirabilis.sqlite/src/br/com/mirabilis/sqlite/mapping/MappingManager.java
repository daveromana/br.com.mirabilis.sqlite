package br.com.mirabilis.sqlite.mapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import br.com.mirabilis.sqlite.mapping.model.DBMap;
import br.com.mirabilis.sqlite.mapping.model.EntityMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

/**
 * Mapping SQLite database of project.
 * @author Rodrigo Simões Rosa
 */
public class MappingManager {

	private DBMap database;
	
	public enum Mapping{
		
		MAPPING_FILE("mapping.xml");
		
		private String value;
		
		private Mapping(String value) {
			this.value = value;
		}
		
		public String toString() {
			return value;
		};
	}
	
	/**
	 * Reader mapping xml file;
	 * @param in
	 * @throws IOException
	 */
	public MappingManager(InputStream in) throws IOException {
		XStream xStream = new XStream(new Dom4JDriver());
		xStream.autodetectAnnotations(true);
		xStream.alias("database", DBMap.class);
		xStream.alias("entity", EntityMap.class);
		xStream.alias("entitys", ArrayList.class);
		
		database = (DBMap) xStream.fromXML(in);
		in.close();
	}
	
	public DBMap getDatabase() {
		return database;
	}
}
