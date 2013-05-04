package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

public class MainMapActivity extends Activity {

	private TextView debugInfo;
	private long sessionID;
	private MenuItem actionChat;
	private MenuItem actionLayers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_map);

		actionLayers = (MenuItem) findViewById(R.id.actionLayers);
		actionChat = (MenuItem) findViewById(R.id.actionChat);

		debugInfo = (TextView) findViewById(R.id.debugInfo);
		debugInfo.setText("Debug");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionChat:
			item.setChecked(!item.isChecked());
		case R.id.actionLayers:
			debugInfo.append("\n" + item.getTitle() + "()");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
