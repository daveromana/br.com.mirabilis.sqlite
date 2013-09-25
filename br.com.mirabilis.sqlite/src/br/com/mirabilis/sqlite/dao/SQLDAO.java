package br.com.mirabilis.sqlite.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import br.com.mirabilis.sqlite.SQLDatabase;
import br.com.mirabilis.sqlite.SQLEntity;
import br.com.mirabilis.sqlite.core.SQLConnection;
import br.com.mirabilis.sqlite.exception.SQLManagerException;

/**
 * Manipulação do banco de dados que realiza a abertura de conexão e a manipulação de dados.
 * @author Rodrigo Simões Rosa
 *
 */
public class SQLDAO {

	public SQLiteDatabase database;
	public SQLConnection sqlConnection;
	
	/**
	 * Construtor
	 * @param context
	 * @param entity Entidade
	 * @throws SQLConnectionException 
	 */
	public SQLDAO(Context context,String dbName, SQLEntity entity) throws SQLManagerException {
		List<SQLEntity> list = new ArrayList<SQLEntity>();
		list.add(entity);
		this.sqlConnection = new SQLConnection(context, new SQLDatabase(dbName), list);
	}
	
	/**
	 * Abre a conexão.
	 * @throws SQLException
	 * @throws SQLConnectionException 
	 */
	public void open() throws SQLException, SQLManagerException {
		try{
			this.database = sqlConnection.getWritableDatabase();
		}catch (Exception e) {
			throw new SQLManagerException("Aconteceu um erro na abertura do sqllite");
		}
	}
	
	/**
	 * Executa uma query especifica.
	 * @param q
	 * @return
	 * @throws SQLConnectionException 
	 */
	@SuppressWarnings("finally")
	public Cursor query(String q) {
		Cursor cursor = null;
		try{
			cursor = this.database.rawQuery(q, null);
			cursor.moveToFirst();
		}catch (Throwable e) {
			e.printStackTrace();
		}finally{
			return cursor;
		}
	}
	
	/**
	 * Fecha a conexão com o banco.
	 */
	public void close(){
		if(this.database.isOpen()){
			this.database.close();
		}
	}
}
