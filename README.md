SQLite
============================================================
br.com.mirabilis.sqlite
author: Rodrigo Simões Rosa		
mail: rodrigosimoesrosa@gmail.com

### Inicialização do banco de dados.				

    SQLiteCore core = null;
	try {
		core = new SQLiteCore.Builder(this, new
		SQLiteDatabaseFile.Builder("data").build(),1).databases(User.class).build();
		core.start();
	} catch (SQLiteException e1) {
		e1.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
### Class that represent's table in SQLite

	import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
	import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
	import br.com.mirabilis.sqlite.manager.model.SQLiteTable;
	
	@SQLiteAnnotationEntity(name="user")
	public class User extends SQLiteTable {
	
		@SQLiteAnnotationField(name="name", type="text")
		private String name;
	
		@SQLiteAnnotationField(name="value", type="integer")
		private Integer value;
	
		public void setName(String name) {
			this.name = name;
		}
	
		public void setValue(Integer value) {
			this.value = value;
		}
	
		public String getName() {
			return name;
		}
	
		public Integer getValue() {
			return value;
		}
	}		
	
### Class that represent's dao model of User table in SQLite

	import java.io.IOException;
	import android.database.Cursor;
	import br.com.mirabilis.sqlite.manager.core.SQLiteCore;
	import br.com.mirabilis.sqlite.manager.dao.SQLiteDAO;
	import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
	
	public class UserDAO extends SQLiteDAO<User> {
		
		public UserDAO(SQLiteCore core) throws SQLiteException, IOException {
			super(core, User.class);
		}
		
		@Override
		public User parser(Cursor cursor) {
			User user = new User();
			user.setId(cursor.getInt(0));
			user.setName(cursor.getString(1));
			user.setValue(cursor.getInt(2));
			return user;
		}
	}