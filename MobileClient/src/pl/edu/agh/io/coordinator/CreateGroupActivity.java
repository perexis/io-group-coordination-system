package pl.edu.agh.io.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.utils.Alerts;
import pl.edu.agh.io.coordinator.utils.net.INetworkProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.CouldNotCreateGroupException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidSessionIDException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.NetworkException;
import android.app.Activity;
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
	private List<String> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);

		groups = new ArrayList<String>();
		listArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, groups);
		ListView availableGroups = (ListView) findViewById(R.id.listViewAvailableGroups);
		availableGroups.setAdapter(listArrayAdapter);
		new GetGroupsInBackground().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Intent());
		creatingGroupInProgress = false;
		createButton = (Button) findViewById(R.id.buttonCreateGroup);
		inputGroupName = (EditText) findViewById(R.id.inputGroupName);
		inputGroupDescription = (EditText) findViewById(R.id.inputGroupDescription);

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
			createGroupInBackground.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Intent());
			creatingGroupInProgress = true;
		} else {
			createGroupInBackground.cancel(false);
			createGroupInBackground = null;
		}
	}

	private class GetGroupsInBackground extends
			AsyncTask<Intent, Void, Exception> {

		private Set<Group> retSet;

		@Override
		protected Exception doInBackground(Intent... params) {
			INetworkProxy proxy = JSonProxy.getInstance();

			try {
				retSet = proxy.getGroups();
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
				groups.clear();
				for (Group group : retSet)
					groups.add(group.toString());
				listArrayAdapter.notifyDataSetChanged();
			} else if (result instanceof NetworkException) {
				Alerts.networkProblem(CreateGroupActivity.this);
			} else if (result instanceof InvalidSessionIDException) {
				Alerts.invalidSessionId(CreateGroupActivity.this);
			}
		}

	}

	private class CreateGroupInBackground extends
			AsyncTask<Intent, Void, Exception> {

		@Override
		protected Exception doInBackground(Intent... params) {
			INetworkProxy proxy = JSonProxy.getInstance();

			Group group = new Group(inputGroupName.getText().toString(),
					inputGroupDescription.getText().toString());

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
			creatingGroupInProgress = false;
		}

		@Override
		protected void onPostExecute(Exception result) {
			creatingGroupInProgress = false;
			createButton.setText(R.string.button_create);
			if (result == null) {
				Alerts.groupCreated(CreateGroupActivity.this);
				inputGroupName.setText("");
				inputGroupDescription.setText("");
				// refresh group list
				new GetGroupsInBackground().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Intent());
			} else if (result instanceof NetworkException) {
				Alerts.networkProblem(CreateGroupActivity.this);
			} else if (result instanceof InvalidSessionIDException) {
				Alerts.invalidSessionId(CreateGroupActivity.this);
			} else if (result instanceof CouldNotCreateGroupException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_could_not_create_group,
						Toast.LENGTH_LONG).show();
			}
		}

	}

}
