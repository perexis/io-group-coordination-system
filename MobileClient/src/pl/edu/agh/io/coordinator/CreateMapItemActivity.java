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

import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.Point;
import pl.edu.agh.io.coordinator.utils.Alerts;
import pl.edu.agh.io.coordinator.utils.net.INetworkProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidLayerException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidSessionIDException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.NetworkException;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateMapItemActivity extends Activity implements OnItemSelectedListener{

	public static final int SELECTED_NOTE = 0;
	public static final int SELECTED_IMAGE = 1;
	public static final int SELECTED_VIDEO = 2;
	
	private Location location;
	private NoteCreateDataFragment noteFragment = new NoteCreateDataFragment();
	private ImageCreateDataFragment imageFragment = new ImageCreateDataFragment();
	private VideoCreateDataFragment videoFragment = new VideoCreateDataFragment();
	private Fragment currentFragment = null;
	private Spinner typeList;
	private Button submitButton;
	private boolean inProgress;
	private AddItemToLayerInBackground backgroundThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_map_item);
		
		Intent intent = getIntent();
		location = intent.getParcelableExtra(MainMapActivity.ITEM_POSITION);
		
		TextView positionView = (TextView) findViewById(R.id.textViewPosition);
		double latitude = 0.0d;
		double longitude = 0.0d;
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		positionView.setText(latitude + (latitude >= 0 ? "N" : "S") + " " + longitude + (longitude >= 0 ? "E" : "W"));

		typeList = (Spinner) findViewById(R.id.spinnerType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.item_type_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		typeList.setAdapter(adapter);
		typeList.setOnItemSelectedListener(this);
		
		submitButton = (Button) findViewById(R.id.submitButton);
		
		inProgress = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	//Spinner listener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		Log.d("CreateMapItemActivity", "starting onItemSelected with pos = " + pos);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		//fragmentTransaction.replace(containerViewId, fragment);
		Fragment fragment = null;
		if (pos == SELECTED_NOTE) {
			fragment = noteFragment;
		} else if (pos == SELECTED_IMAGE) {
			fragment = imageFragment;
		} else if (pos == SELECTED_VIDEO) {
			fragment = videoFragment;
		} else {
			fragmentTransaction.commit();
			return;
		}
		if (currentFragment != null) {
			fragmentTransaction.remove(currentFragment);
		}
		fragmentTransaction.add(R.id.additionalData, fragment);
		currentFragment = fragment;
		fragmentTransaction.commit();
	}

	//Spinner listener
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	public void createItem(View view) {
		if (!inProgress) {
			if (currentFragment == noteFragment) {
				submitButton.setText(R.string.button_cancel);
				backgroundThread = new AddItemToLayerInBackground();
				backgroundThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Layer("notes"),
						new Point(location.getLatitude(), location.getLongitude()), noteFragment.getInput());
				inProgress = true;
			}
		} else {
			backgroundThread.cancel(false);
			backgroundThread = null;
		}
	}
	
	private class AddItemToLayerInBackground extends
	AsyncTask<Object, Void, Exception> {

		@Override
		protected Exception doInBackground(Object... params) {
			// params: Layer, Point, String
			INetworkProxy proxy = JSonProxy.getInstance();
			try {
				Layer layer = (Layer) params[0];
				Point point = (Point) params[1];
				String data = (String) params[2];
				proxy.addItemToLayer(layer, point, data);
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			} catch (InvalidLayerException e) {
				return e;
			}

			return null;
		}

		@Override
		protected void onCancelled(Exception result) {
			submitButton.setText(R.string.button_submit);
			inProgress = false;
		}
		
		@Override
		protected void onPostExecute(Exception result) {
			submitButton.setText(R.string.button_submit);
			inProgress = false;
			if (result == null) {
				Alerts.mapItemCreated(CreateMapItemActivity.this);
			} else if (result instanceof NetworkException) {
				Alerts.networkProblem(CreateMapItemActivity.this);
			} else if (result instanceof InvalidSessionIDException) {
				Alerts.invalidSessionId(CreateMapItemActivity.this, null);
			} else if (result instanceof InvalidLayerException) {
				Alerts.invalidLayer(CreateMapItemActivity.this);
			}
		}

	}

}

