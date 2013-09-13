package br.com.mirabilis.sqlite;


	import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import br.com.mirabilis.sqlite.exception.SQLConnectionException;

	/**
	 * Classe de conexão com o banco de dados e criação da tabela em SQLLite.
	 * @author Rodrigo Simões Rosa
	 *
	 */
	public class SQLConnection extends SQLiteOpenHelper {

		private List<SQLEntity> entitys;

		private SQLiteDatabase database;
		private static final int DATABASE_VERSION = 1;
		
		/**
		 * Inicia o objeto de conexão com o banco de dados sqllite  
		 * @param context
		 * @param database
		 * @param entitys
		 */
		public SQLConnection(Context context,SQLDatabase database, List<SQLEntity> entitys) {
			super(context, database.getDatabaseName(), null, DATABASE_VERSION);
			this.entitys = entitys;
			this.database = getWritableDatabase();
		}
		
		/**
		 * Realiza a criação nas tabelas;
		 */
		@Override
		public void onCreate(SQLiteDatabase database) {
			this.database = database;
			for(SQLEntity e : entitys){
				this.database.execSQL(e.getQueryCreate());
				Log.w("ENTITY CREATED", e.getEntity());
			}
		}

		/**
		 * Realiza o upgrade nas tabelas;
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(SQLConnection.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			for(SQLEntity e : entitys){
				db.execSQL("DROP TABLE IF EXISTS " + e.getEntity());
				onCreate(db);
			}
		}
		
		/**
		 * Abre a conexão, cria as tabelas.
		 * @throws SQLException
		 * @throws SQLConnectionException 
		 */
		public void connect() throws SQLException, SQLConnectionException {
			try{
				this.database = this.getWritableDatabase();
				if(this.database.isOpen()){
					this.database.close();
				}
			}catch (Exception e) {
				throw new SQLConnectionException("Ocorreu um erro na conexão com o banco sqllite.");
			}
		}
	} 
