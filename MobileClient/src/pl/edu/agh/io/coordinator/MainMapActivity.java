package pl.edu.agh.io.coordinator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.TextView;

public class MainMapActivity extends Activity {

	private TextView debugInfo;
	private long sessionID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_map);

		debugInfo = (TextView) findViewById(R.id.debugInfo);
		Intent intent = getIntent();
		sessionID = intent.getLongExtra(LoginPasswordActivity.SESSION_ID, -1);
		String text = "SessionID=" + String.valueOf(sessionID);
		debugInfo.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_map, menu);
		return true;
	}

}
