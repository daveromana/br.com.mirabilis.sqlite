SQLite
============================================================
br.com.mirabilis.sqlite		
author: Rodrigo Simões Rosa		
mail: rodrigosimoesrosa@gmail.com

### Startup database.				

    SQLiteCore core = null;
	try {
		core = new SQLiteCore.Builder(this, new
		SQLiteDatabaseFile.Builder("databasename").build(),1).databases(User.class).build();
		core.start();
	} catch (SQLiteException e1) {
		e1.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
### Class that represents the User in SQLite

	import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
	import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
	import br.com.mirabilis.sqlite.manager.model.SQLiteTable;
	import br.com.mirabilis.sqlite.manager.model.SQLiteField.SQLiteFieldType;
	
	@SQLiteAnnotationEntity(name="user")
	public class User extends SQLiteTable {
	
		@SQLiteAnnotationField(name="name", type=SQLiteFieldType.TEXT)
		private String name;
	
		@SQLiteAnnotationField(name="value", type=SQLiteFieldType.INTEGER)
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

### ForeignKey

	import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;		
	import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;		
	import br.com.mirabilis.sqlite.manager.model.SQLiteField.SQLiteFieldType;		
	import br.com.mirabilis.sqlite.manager.model.SQLiteTable;

	@SQLiteAnnotationEntity(name = "delivery")
	public class Delivery extends SQLiteTable {
	
		@SQLiteAnnotationField(name = "idRequest", type = SQLiteFieldType.INTEGER, foreignKey = true , foreignKeyModifier = SQLiteForeignModifier.CASCADE, reference= Request.class)
		private long idRequest;
		
		@SQLiteAnnotationField(name = "idUser", type = SQLiteFieldType.INTEGER, foreignKey = true , foreignKeyModifier = SQLiteForeignModifier.CASCADE, reference= User.class)
		private long idUser;
		
		@SQLiteAnnotationField(name = "numDelivery", type = SQLiteFieldType.INTEGER)
		private Integer numDelivery;		
	
	}
	
	
### Dao class that implements the model manipulations User in SQLite

	import java.io.IOException;
	import android.database.Cursor;
	import br.com.mirabilis.sqlite.manager.core.SQLiteCore;
	import br.com.mirabilis.sqlite.manager.dao.SQLiteDAO;
	import br.com.mirabilis.sqlite.manager.exception.SQLiteException;
	
	public class UserDAO extends SQLiteDAO<User> {
		
		public UserDAO(SQLiteCore core) throws SQLiteException, IOException {
			super(core, User.class);
		}
		
		//Make your custom's methods for UserDAO
	}
	
### Select default in UserDAO

	UserDAO dao = null;
	List<User> list = null;
	try {
		dao = new UserDAO(core);
		list = dao.select();
	} catch (SQLiteEmptyException e) {
		e.printStackTrace();
	} catch (SQLiteException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

### Select using paging default in UserDAO

	UserDAO dao = null;
	List<User> list = null;
	try {
		dao = new UserDAO(core);
		u = dao.selectByPage(10, 1);
	} catch (SQLiteException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (SQLiteEmptyException e) {
		e.printStackTrace();
	}

### Update default in UserDAO

	UserDAO dao = null;
	try {
		dao = new UserDAO(core);
		user.setName("newName");
		boolean successfully = dao.update(user);
	} catch (SQLiteException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

### Persist default in UserDAO

	UserDAO dao = null;
	User user = new User();
	try {
		dao = new UserDAO(core);
		user.setName("name");
		user.setValue(1);
		user = dao.persist(user);
	} catch (SQLiteException e) {
		e.printStackTrace();
	} catch (SQLiteEmptyException e) {
		e.printStackTrace();
	} catch (SQLiteNotNullFieldException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
### Insert default in UserDAO

	UserDAO dao = null;
	User user = new User();
	try {
		dao = new UserDAO(core);
		user.setName("name");
		user.setValue(1);
		long id = dao.insert(user);
	} catch (SQLiteException e) {
		e.printStackTrace();
	} catch (SQLiteNotNullFieldException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

### Delete default in UserDAO

	UserDAO dao = null;
	try {
		dao = new UserDAO(core);
		boolean sucessfully = dao.delete(user);
	} catch (SQLiteException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
