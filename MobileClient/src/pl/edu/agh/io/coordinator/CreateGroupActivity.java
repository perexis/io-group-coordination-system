package pl.edu.agh.io.coordinator;

import java.util.Set;
import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.utils.net.IJSonProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.CouldNotCreateGroupException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidSessionIDException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.NetworkException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CreateGroupActivity extends Activity {

	private ArrayAdapter<String> listArrayAdapter;
	private boolean creatingGroupInProgress;
	private CreateGroupInBackground createGroupInBackground;
	private Button createButton;
	private EditText inputGroupName;
	private EditText inputGroupDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);

		listArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		ListView availableGroups = (ListView) findViewById(R.id.listViewAvailableGroups);
		availableGroups.setAdapter(listArrayAdapter);
		new GetGroupsInBackground().execute(new Intent());
		creatingGroupInProgress=false;
		createButton=(Button)findViewById(R.id.buttonCreateGroup);
		inputGroupName=(EditText) findViewById(R.id.inputGroupName);
		inputGroupDescription=(EditText) findViewById(R.id.inputGroupDescription);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	
	public void createGroup(View view) {

		if (creatingGroupInProgress == false)
			createGroupInBackground = new CreateGroupInBackground();
		if (!creatingGroupInProgress) {
			createButton.setText(getString(R.string.button_cancel));
			createGroupInBackground.execute(new Intent());
			creatingGroupInProgress = true;
		} else {
			createGroupInBackground.cancel(false);
			createGroupInBackground = null;
		}
	}
	
	// alerts about invalid sessionID and finishes activity
	private void invalidSessionId() {
		new AlertDialog.Builder(CreateGroupActivity.this)
				.setMessage(R.string.alert_invalid_session_id_logout)
				.setTitle(R.string.alert_invalid_session_id)
				.setCancelable(false)
				.setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CreateGroupActivity.this.finish();
							}
						}).create().show();
	}

	// shows network problem alert
	private void networkProblem() {
		Toast.makeText(getApplicationContext(), R.string.alert_network_problem,
				Toast.LENGTH_LONG).show();
	}

	private class GetGroupsInBackground extends
			AsyncTask<Intent, Void, Exception> {

		private Set<Group> groups;

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				groups = proxy.getGroups();
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
				for (Group group : groups)
					listArrayAdapter.add(group.getId() + ": "
							+ group.getDescription());
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class CreateGroupInBackground extends
			AsyncTask<Intent, Void, Exception> {

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			Group group= new Group(inputGroupName.getText().toString(), inputGroupDescription.getText().toString());
			
			try {
				proxy.createGroup(group);
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			} catch (CouldNotCreateGroupException e) {
				return e;
			}
			return null;
		}
		
		@Override
		protected void onCancelled(Exception result) {
			createButton.setText(getString(R.string.button_create));
			creatingGroupInProgress= false;
		}

		@Override
		protected void onPostExecute(Exception result) {
			creatingGroupInProgress=false;
			createButton.setText(R.string.button_create);
			if (result == null) {
				//TODO: dorobić
				CreateGroupActivity.this.finish();
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			} else if(result instanceof CouldNotCreateGroupException){
				Toast.makeText(getApplicationContext(),
						R.string.alert_could_not_create_group, Toast.LENGTH_LONG)
						.show();
			}
		}

	}

}
