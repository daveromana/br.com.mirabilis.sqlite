package br.com.mirabilis.sqlite.manager.model;

public class User {
	
	public static final String TABLE_NAME = "user";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MAIL = "mail";
	
	public static String QUERY_USER = "select " + COLUMN_ID 
			+ " from "+ TABLE_NAME + " desc limit 1";
	
	public static String[] columns = { COLUMN_ID, COLUMN_NAME , COLUMN_MAIL };


	public static final String TABLE_QUERY = "create table if not exists "
			+ TABLE_NAME + "(" 
			+ COLUMN_ID +" integer primary key autoincrement, "
			+ COLUMN_NAME +" text not null, "
			+ COLUMN_MAIL +" text not null);";
}
