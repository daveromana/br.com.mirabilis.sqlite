package br.com.mirabilis.sqlite.enumeration;

/**
 * Enumeração que contem os tipo de dado do sqllite.
 * @author Rodrigo Simões Rosa
 *
 */
public enum SQLiteType{
	NULL("null"),TEXT("text"),INTEGER("integer"),REAL("real"),BLOB("blob");
	
	private String type;
	
	private SQLiteType(String value) {
		this.type = value;
	}
	
	@Override
	public String toString() {
		return this.type; 
	}
	
	/**
	 * Retorna o tipo do objeto a ser inserido no sql lite.
	 * @param type
	 * @return
	 */
	public static String getType(Object type){
		if(type instanceof String){
			return TEXT.toString();
		}else if(type instanceof Integer || type instanceof Boolean){
			return INTEGER.toString();
		}else if(type instanceof Float || type instanceof Double){
			return REAL.toString();
		}else{
			return BLOB.toString();
		}
	}
}