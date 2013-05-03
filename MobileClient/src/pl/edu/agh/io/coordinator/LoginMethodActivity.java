package pl.edu.agh.io.coordinator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

public class LoginMethodActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_method);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_method, menu);
		return true;
	}

	public void clickLoginMethod(View view) {
		Intent intent = null;
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupLoginMethod);
		int option = radioGroup.getCheckedRadioButtonId();
		switch (option) {
		case R.id.radioLoginPassword:
			intent = new Intent(LoginMethodActivity.this,
					LoginPasswordActivity.class);
			break;
		case R.id.radioNFC:
		case R.id.radioFingerprint:
		default:
			intent = new Intent(LoginMethodActivity.this,
					NotImplementedYetActivity.class);

		}
		startActivity(intent);
	}

}
