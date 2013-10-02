package br.com.mirabilis.sqlite.core;

import java.util.List;
import br.com.mirabilis.sqlite.SQLiteDatabase;
import br.com.mirabilis.sqlite.SQLEntity;
import br.com.mirabilis.sqlite.exception.SQLManagerException;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

	/**
	 * Classe de conexão com o banco de dados e criação da tabela em SQLite.
	 * @author Rodrigo Simões Rosa
	 *
	 */
	public class SQLConnection extends SQLiteOpenHelper {

		private static final String TAG = SQLConnection.class.getName();
		private List<SQLEntity> entitys;
		private SQLiteDatabase database;
		public Context context;
		
		/**
		 * Inicia o objeto de conexão com o banco de dados sqlite  
		 * @param context
		 * @param database
		 * @param entitys
		 */
		public SQLConnection(Context context,SQLiteDatabase database, int version) {
			super(context, database.getDatabaseName(), null, version);
			this.database = getWritableDatabase();
		}
		
		@Override
		public void onCreate(SQLiteDatabase database) {
			this.database = database;
		}

		/**
		 * Realiza o upgrade nas tabelas;
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			for(SQLEntity e : entitys){
				db.execSQL("DROP TABLE IF EXISTS " + e.getEntity());
				onCreate(db);
			}
		}
		
		/**
		 * Create tables;
		 */
		public void createTables(){
			for(SQLEntity e : entitys){
				this.database.execSQL(e.getQueryCreate());
				Log.w(TAG, "ENTITY CREATED : "+ e.getEntity());
			}
		}
		
		/**
		 * Abre a conexão, cria as tabelas.
		 * @throws SQLException
		 * @throws SQLConnectionException 
		 */
		public void connect() throws SQLException, SQLManagerException {
			try{
				this.database = this.getWritableDatabase();
				if(this.database.isOpen()){
					this.database.close();
				}
			}catch (Exception e) {
				throw new SQLManagerException("Error with connection SQLite");
			}
		}
		
		/**
		 * Builder {@link SQLConnection}
		 * @author Rodrigo Simões Rosa
		 */
		public static class Builder {
			
			private SQLConnection instance;
			
			public Builder(Context context, SQLiteDatabase database, int version) {
				this.instance = new SQLConnection(context, database, version);
			}
			
			public Builder entitys(List<SQLEntity> entitys){
				this.instance.entitys = entitys;
				return this;
			}
			
			public Builder entity(SQLEntity entity) {
				this.instance.entitys.add(entity);
				return this;
			}
			
			public SQLConnection build() {
				return instance;
			}
		}
	} 
