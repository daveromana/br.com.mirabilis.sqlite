package br.com.mirabilis.sqlite.manager.view;

import android.app.Activity;
import android.os.Bundle;
import br.com.mirabilis.sqlite.R;

public class SqliteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlite_main);
	}
}
