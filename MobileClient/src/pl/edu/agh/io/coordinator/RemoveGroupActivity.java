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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.utils.net.INetworkProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidGroupException;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class RemoveGroupActivity extends Activity {

	private ArrayAdapter<String> listArrayAdapter;
	private boolean removingGroupInProgress;
	private RemoveGroupInBackground removeGroupInBackground;
	private Button removeButton;
	private List<String> groups;

	private String selectedItem = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_group);

		groups = new ArrayList<String>();
		listArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, groups);
		ListView availableGroups = (ListView) findViewById(R.id.listViewAvailableGroups);
		availableGroups.setAdapter(listArrayAdapter);

		availableGroups.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItem = (String) parent.getItemAtPosition(position);
				/*((ListView) findViewById(R.id.listViewAvailableGroups))
						.setSelection(position);
				view.getFocusables(position);
				view.setSelected(true);
				listArrayAdapter.notifyDataSetChanged();*/
			}

		});

		new GetGroupsInBackground().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Intent());
		removingGroupInProgress = false;
		removeButton = (Button) findViewById(R.id.buttonRemoveGroup);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	public void removeGroup(View view) {

		if (removingGroupInProgress == false)
			removeGroupInBackground = new RemoveGroupInBackground();
		if (!removingGroupInProgress) {
			removeButton.setText(getString(R.string.button_cancel));
			removeGroupInBackground.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Intent());
			removingGroupInProgress = true;
		} else {
			removeGroupInBackground.cancel(false);
			removeGroupInBackground = null;
		}
	}

	private void groupRemoved() {
		new AlertDialog.Builder(RemoveGroupActivity.this)
				.setMessage(R.string.alert_group_removed)
				.setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok, null).create().show();
	}

	// alerts about invalid sessionID and finishes activity
	private void invalidSessionId() {
		new AlertDialog.Builder(RemoveGroupActivity.this)
				.setMessage(R.string.alert_invalid_session_id_logout)
				.setTitle(R.string.alert_invalid_session_id)
				.setCancelable(false)
				.setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								RemoveGroupActivity.this.finish();
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
					groups.add(group.getId());
				listArrayAdapter.notifyDataSetChanged();
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class RemoveGroupInBackground extends
			AsyncTask<Intent, Void, Exception> {

		@Override
		protected Exception doInBackground(Intent... params) {
			INetworkProxy proxy = JSonProxy.getInstance();

			Group group = new Group(selectedItem, "");

			try {
				proxy.removeGroup(group);
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			} catch (InvalidGroupException e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onCancelled(Exception result) {
			removeButton.setText(getString(R.string.button_remove));
			removingGroupInProgress = false;
		}

		@Override
		protected void onPostExecute(Exception result) {
			removingGroupInProgress = false;
			removeButton.setText(R.string.button_remove);
			if (result == null) {
				groupRemoved();
				// refresh group list
				new GetGroupsInBackground().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Intent());
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			} else if (result instanceof InvalidGroupException) {
				Toast.makeText(getApplicationContext(),
						R.string.alert_could_not_remove_group,
						Toast.LENGTH_LONG).show();
			}
		}

	}

}

