package br.com.mirabilis.sqlite.view;

import android.app.Activity;
import android.os.Bundle;
import br.com.mirabilis.sqlite.R;

/**
 * {@link Activity} of library
 * 
 * @author Rodrigo Simões Rosa
 */
public class SqliteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlite_main);
	}
}
