SQLite		
author: Rodrigo Simões Rosa		
mail: rodrigosimoesrosa@gmail.com		

SQLiteCore core = null;		
try {		
	core = new SQLiteCore.Builder(this, new		
	SQLiteDatabaseFile.Builder("data").build(),1).databases(User.class).build();		
	core.start();		
	} catch (SQLiteErrorException e1) {		
		e1.printStackTrace();		
	} catch (IOException e) {		
		e.printStackTrace();		
	}


