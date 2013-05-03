package pl.edu.agh.io.coordinator;

import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LoginPasswordActivity extends Activity {

	public final static String SESSION_ID = "pl.edu.agh.io.coordinator.sessionID";

	private Button loginButton;
	private EditText inputLogin;
	private EditText inputPassword;
	private ProgressBar loginProgressBar;
	private LoginInBackground loginInBackground;
	private boolean loginInProgress = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_password);

		loginButton = (Button) findViewById(R.id.buttonLogin);
		loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
		inputLogin = (EditText) findViewById(R.id.inputLogin);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_password, menu);
		return true;
	}

	public void clickLogin(View view) {

		if (loginInProgress == false)
			loginInBackground = new LoginInBackground();
		if (!loginInProgress) {
			loginButton.setText(getString(R.string.button_cancel));
			Intent intent = new Intent();
			loginInBackground.execute(intent);
			loginInProgress = true;
		} else {
			loginInBackground.cancel(false);
			loginInBackground = null;
		}
	}

	private class LoginInBackground extends AsyncTask<Intent, Void, Long> {

		@Override
		protected void onPreExecute() {
			loginProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Long doInBackground(Intent... params) {
			JSonProxy proxy = JSonProxy.getInstance();
			Long sessionID;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sessionID = Long.valueOf(proxy.login(inputLogin.getText()
					.toString(), inputPassword.getText().toString()));
			return sessionID;
			// publishProgress(null);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			//
		}

		@Override
		protected void onCancelled(Long result) {
			loginButton.setText(getString(R.string.button_login));
			loginProgressBar.setVisibility(View.INVISIBLE);
			loginInProgress = false;
		}

		@Override
		protected void onPostExecute(Long result) {
			Intent intent = new Intent(LoginPasswordActivity.this,
					MainMapActivity.class);
			loginButton.setText(getString(R.string.button_login));
			loginProgressBar.setVisibility(View.INVISIBLE);
			loginInProgress = false;
			//TODO: implement better :)
			if(result!=-1){
				intent.putExtra(SESSION_ID, result);
				startActivity(intent);
			}else{
				//TODO:
				//Print login error
			}
		}

	}

}
