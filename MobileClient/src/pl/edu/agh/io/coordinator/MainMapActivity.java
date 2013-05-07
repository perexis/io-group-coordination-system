package pl.edu.agh.io.coordinator;

import java.util.Set;

import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.utils.net.IJSonProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.CouldNotLogInException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidSessionIDException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.NetworkException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainMapActivity extends Activity {

	private TextView debugInfo;
	private MenuItem actionChat;
	private MenuItem actionLayers;
	private boolean loggingOut = false;

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
			debugInfo.append("\n" + item.getTitle() + "()");
			return true;
		case R.id.actionLayers:
			debugInfo.append("\n" + item.getTitle() + "()");
			new GetUsersInBackground().execute(new Intent());
			return true;
		case R.id.actionLogout:
			if (!loggingOut) {
				loggingOut = true;
				debugInfo.append("\n" + item.getTitle() + "()");
				Intent intent = new Intent();
				new LogoutInBackground().execute(intent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class LogoutInBackground extends AsyncTask<Intent, Void, Exception> {

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				proxy.logout();
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {

			if (result == null) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_logged_out, Toast.LENGTH_LONG);
				MainMapActivity.this.finish();
			} else if (result instanceof NetworkException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_network_problem, Toast.LENGTH_LONG)
						.show();
				loggingOut = false;
			} else if (result instanceof InvalidSessionIDException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_invalid_session_id, Toast.LENGTH_LONG)
						.show();
				MainMapActivity.this.finish();
			}
		}

	}

	private class GetUsersInBackground extends
			AsyncTask<Intent, Void, Exception> {

		private Set<User> users;

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				users = proxy.getUsers();
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {

			if (result == null) {
				for(User user:users)
					debugInfo.append("\n" + user.getName() + " " + user.getSurname());
			} else if (result instanceof NetworkException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_network_problem, Toast.LENGTH_LONG)
						.show();
			} else if (result instanceof InvalidSessionIDException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_invalid_session_id, Toast.LENGTH_LONG)
						.show();
				MainMapActivity.this.finish();
			}
		}

	}

}
