/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.edu.agh.io.coordinator;

import pl.edu.agh.io.coordinator.utils.net.INetworkProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.CouldNotLogInException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.NetworkException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginPasswordActivity extends Activity {

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
			loginInBackground.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, intent);
			loginInProgress = true;
		} else {
			loginInBackground.cancel(false);
			loginInBackground = null;
		}
	}

	private class LoginInBackground extends AsyncTask<Intent, Void, Exception> {

		@Override
		protected void onPreExecute() {
			loginProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Exception doInBackground(Intent... params) {
			INetworkProxy proxy = JSonProxy.getInstance();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				proxy.login(inputLogin.getText().toString(), inputPassword
						.getText().toString());
			} catch (CouldNotLogInException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			}
			// publishProgress(null);
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			//
		}

		@Override
		protected void onCancelled(Exception result) {
			loginButton.setText(getString(R.string.button_login));
			loginProgressBar.setVisibility(View.INVISIBLE);
			loginInProgress = false;
		}

		@Override
		protected void onPostExecute(Exception result) {
			Intent intent = new Intent(LoginPasswordActivity.this,
					MainMapActivity.class);
			loginButton.setText(getString(R.string.button_login));
			loginProgressBar.setVisibility(View.INVISIBLE);
			loginInProgress = false;

			if (result == null) {
				startActivity(intent);
			} else if (result instanceof CouldNotLogInException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_could_not_login, Toast.LENGTH_LONG)
						.show();
			} else if (result instanceof NetworkException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_network_problem, Toast.LENGTH_LONG)
						.show();
			}
		}

	}

}

