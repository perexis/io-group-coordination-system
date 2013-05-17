package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class CreateUserItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_user_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_user_item, menu);
		return true;
	}

}
