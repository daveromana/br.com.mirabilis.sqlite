SQLite		
author: Rodrigo Simões Rosa		
mail: rodrigosimoesrosa@gmail.com		

Inicialização do banco de dados.		
SQLiteCore core = null;		
try {		
&nbsp core = new SQLiteCore.Builder(this, new		
&nbsp	SQLiteDatabaseFile.Builder("data").build(),1).databases(User.class).build();		
	core.start();		
} catch (SQLiteException e1) {		
	e1.printStackTrace();		
} catch (IOException e) {		
	e.printStackTrace();		
}


