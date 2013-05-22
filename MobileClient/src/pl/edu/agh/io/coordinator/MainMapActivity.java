package pl.edu.agh.io.coordinator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.MapItem;
import pl.edu.agh.io.coordinator.resources.Message;
import pl.edu.agh.io.coordinator.resources.Point;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import pl.edu.agh.io.coordinator.utils.container.DataContainer;
import pl.edu.agh.io.coordinator.utils.container.DataContainer.OnDataContainerChangesListener;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuListener;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuState;
import pl.edu.agh.io.coordinator.utils.net.IJSonProxy;
import pl.edu.agh.io.coordinator.utils.net.JSonProxy;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidLayerException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidMapItemException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.InvalidSessionIDException;
import pl.edu.agh.io.coordinator.utils.net.exceptions.NetworkException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapActivity extends Activity implements
		ChatFragment.OnFragmentInteractionListener, LayersMenuListener,
		OnDataContainerChangesListener {

	private static final LatLng DEFAULT_POSITION = new LatLng(50.061368,
			19.936924); // Cracow

	private DataContainer dataContainer = new DataContainer(this);
	private boolean loggingOut = false;
	private LayersMenuFragment layersFragment = new LayersMenuFragment();
	private LayersMenuState savedState = null;

	private GoogleMap googleMap;

	private ChatFragment chatFragment = ChatFragment.newInstance();

	private Set<UserItem> userItems;
	private Set<User> users;
	private Set<Group> groups;

	private Set<Layer> layers;

	private HashMap<MapItem, Marker> mapItemToMarker = new HashMap<MapItem, Marker>();

	private Set<Thread> threads = new HashSet<Thread>();

	public LayersMenuState getSavedState() {
		return this.savedState;
	}

	public void setSavedState(LayersMenuState state) {
		this.savedState = state;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main_map);

		
		
		// TODO: implement better ----vvv-----
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.layersFrame, layersFragment);
		fragmentTransaction.hide(layersFragment);
		//fragmentTransaction.add(layersFragment, "layersFragment");
		fragmentTransaction.commit();

		/*fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(layersFragment);
		fragmentTransaction.commit();*/

		// TODO: ----------^^^---------
		
		new GetLayersInBackground().execute(new Intent());

		setUpMapIfNeeded();

		Thread mainThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						new GetUsersInBackground().execute(new Intent());
						new GetUserItemsInBackground().execute(new Intent());
						new GetGroupsInBackground().execute(new Intent());
						new GetMessagesInBackground().execute();
						if (layers != null)
							for (Layer layer : layers)
								new GetMapItemsInBackground().execute(layer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		threads.add(mainThread);
		mainThread.start();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
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
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (item.isChecked()) {
				fragmentTransaction.add(R.id.chatFrame, chatFragment);
			} else
				fragmentTransaction.remove(chatFragment);
			fragmentTransaction.commit();
			return true;
		case R.id.actionLayers:
			item.setChecked(!item.isChecked());
			FragmentManager fragmentManager2 = getFragmentManager();
			FragmentTransaction fragmentTransaction2 = fragmentManager2
					.beginTransaction();
			if (item.isChecked()) {
				//fragmentTransaction2.add(R.id.layersFrame, layersFragment);
				fragmentTransaction2.show(layersFragment);
			} else
				//fragmentTransaction2.remove(layersFragment);
				fragmentTransaction2.hide(layersFragment);
			fragmentTransaction2.commit();
			return true;
		case R.id.actionCreateGroup:
			Intent intentCreateGroup = new Intent(MainMapActivity.this,
					CreateGroupActivity.class);
			startActivity(intentCreateGroup);
			return true;
		case R.id.actionRemoveGroup:
			Intent intentRemoveGroup = new Intent(MainMapActivity.this,
					RemoveGroupActivity.class);
			startActivity(intentRemoveGroup);
			return true;
		case R.id.actionCreateItem:
			Intent intentCreateItem = new Intent(MainMapActivity.this,
					CreateUserItemActivity.class);
			startActivity(intentCreateItem);
			return true;
		case R.id.actionSettings:
			Intent intentSettings = new Intent(MainMapActivity.this,
					NotImplementedYetActivity.class);
			startActivity(intentSettings);
			return true;
		case R.id.actionLogout:
			if (!loggingOut) {
				loggingOut = true;
				Intent intent = new Intent();
				new LogoutInBackground().execute(intent);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setUpMapIfNeeded() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.mainMapFragment)).getMap();
			if (googleMap == null) {
				// TODO: print error
			} else {
				googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
						DEFAULT_POSITION, 16.0f);
				googleMap.moveCamera(cameraUpdate);
				googleMap.setMyLocationEnabled(true);
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);

				googleMap
						.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

							@Override
							public void onInfoWindowClick(Marker marker) {
								Toast.makeText(getApplicationContext(),
										marker.getSnippet(), Toast.LENGTH_SHORT)
										.show();
							}
						});

			}
		}
	}

	// alerts about invalid sessionID and finishes activity
	private void invalidSessionId() {
		new AlertDialog.Builder(MainMapActivity.this)
				.setMessage(R.string.alert_invalid_session_id_logout)
				.setTitle(R.string.alert_invalid_session_id)
				.setCancelable(false)
				.setIcon(R.drawable.alerts_and_states_warning)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainMapActivity.this.finish();
							}
						}).create().show();
	}

	// shows network problem alert
	private void networkProblem() {
		Toast.makeText(getApplicationContext(), R.string.alert_network_problem,
				Toast.LENGTH_LONG).show();
	}

	private void invalidLayer() {
		Toast.makeText(getApplicationContext(), "Invalid layer!!!",
				Toast.LENGTH_LONG).show();
	}

	private void invalidMapItem() {
		Toast.makeText(getApplicationContext(), "Invalid map item!!!",
				Toast.LENGTH_LONG).show();
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
				MainMapActivity.this.finish();
			} else if (result instanceof NetworkException) {
				networkProblem();
				loggingOut = false;
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class GetUsersInBackground extends
			AsyncTask<Intent, Void, Exception> {

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
				if (MainMapActivity.this.layersFragment != null)
					MainMapActivity.this.layersFragment.setPeople(users);
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class GetLayersInBackground extends
			AsyncTask<Intent, Void, Exception> {

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				layers = proxy.getLayers();
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
				layersFragment.setLayers(layers);
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class GetMapItemsInBackground extends
			AsyncTask<Layer, Void, Exception> {

		Set<MapItem> mapItems;
		Layer layer;

		@Override
		protected Exception doInBackground(Layer... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				mapItems = proxy.getMapItems(params[0]);
				layer = params[0];
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
		protected void onPostExecute(Exception result) {
			if (result == null) {
				if (layersFragment != null)
					dataContainer.newMapItemsSet(layer, mapItems);
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			} else if (result instanceof InvalidLayerException) {
				invalidLayer();
			}
		}

	}

	private class AddItemTolayerInBackground extends
			AsyncTask<Intent, Void, Exception> {

		// private Set<MapItem> notesItems;
		MapItem mapItem;

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				mapItem = proxy.addItemToLayer(new Layer("notes"), new Point(
						13.34, 57.78), "Testowa notatka");
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
		protected void onPostExecute(Exception result) {

			if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			} else if (result instanceof InvalidLayerException) {
				invalidLayer();
			}
		}

	}

	private class RemoveMapItemInBackground extends
			AsyncTask<MapItem, Void, Exception> {

		@Override
		protected Exception doInBackground(MapItem... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				proxy.removeMapItem(params[0]);
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			} catch (InvalidMapItemException e) {
				return e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {

			if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			} else if (result instanceof InvalidMapItemException) {
				invalidMapItem();
			}
		}

	}

	private class GetGroupsInBackground extends
			AsyncTask<Intent, Void, Exception> {

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
				if (layersFragment != null)
					layersFragment.setGroups(groups);
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class GetUserItemsInBackground extends
			AsyncTask<Intent, Void, Exception> {

		@Override
		protected Exception doInBackground(Intent... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				userItems = proxy.getPossibleUserItems();
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
				if (layersFragment != null)
					layersFragment.setItems(userItems);
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class SendMessageInBackground extends
			AsyncTask<String, Void, Exception> {

		@Override
		protected Exception doInBackground(String... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				proxy.sendMessage(params[0]);
			} catch (InvalidSessionIDException e) {
				return e;
			} catch (NetworkException e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {

			if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	private class GetMessagesInBackground extends
			AsyncTask<Void, Void, Exception> {

		List<Message> messages;

		@Override
		protected Exception doInBackground(Void... params) {
			IJSonProxy proxy = JSonProxy.getInstance();

			try {
				messages = proxy.getMessages();
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
				for (Message m : messages) {
					chatFragment.newMessage(m);
				}
			} else if (result instanceof NetworkException) {
				networkProblem();
			} else if (result instanceof InvalidSessionIDException) {
				invalidSessionId();
			}
		}

	}

	@Override
	public void onChatSendMessage(String text) {
		new SendMessageInBackground().execute(text);
	}

	@Override
	public void itemChecked(String item) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing itemChecked, item = " + item);
	}

	@Override
	public void itemUnchecked(String item) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing itemUnhecked, item = " + item);
	}

	@Override
	public void userChecked(String user) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing userChecked, user = " + user);
	}

	@Override
	public void userUnchecked(String user) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing userUnchecked, user = " + user);
	}

	@Override
	public void groupChecked(String group) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing groupChecked, group = " + group);
	}

	@Override
	public void groupUnchecked(String group) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing groupUnchecked, group = " + group);
	}

	@Override
	public void layerChecked(String layer) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing layerChecked, layer = " + layer);
	}

	@Override
	public void layerUnchecked(String layer) {
		// TODO Auto-generated method stub
		Log.d("MainMapActivity", "executing layerUnchecked, layer = " + layer);
	}

	@Override
	public void mapItemAdded(Layer layer, MapItem mapItem) {
		Log.d("MainMapActivity", "adding mapItem " + mapItem.getData()
				+ " to layer " + layer.getName());
		if (layer.getName().equals("notes")) {
			Marker marker = googleMap
					.addMarker(new MarkerOptions()
							.position(
									new LatLng(mapItem.getPosition()
											.getLatitude(), mapItem
											.getPosition().getLongitude()))
							.title(getString(R.string.layer_note))
							.snippet(mapItem.getData())
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
			// .icon(BitmapDescriptorFactory.fromResource(R.drawable.action_help)));
			mapItemToMarker.put(mapItem, marker);
		} else if (layer.getName().equals("images")) {
			Marker marker = googleMap
					.addMarker(new MarkerOptions()
							.position(
									new LatLng(mapItem.getPosition()
											.getLatitude(), mapItem
											.getPosition().getLongitude()))
							.title(getString(R.string.layer_image))
							.snippet(mapItem.getData())
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
			mapItemToMarker.put(mapItem, marker);
		} else if (layer.getName().equals("videos")) {
			Marker marker = googleMap.addMarker(new MarkerOptions()
					.position(mapItem.getPosition().getLatLng())
					.title(getString(R.string.layer_video))
					.snippet(mapItem.getData())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			mapItemToMarker.put(mapItem, marker);
		}
	}

	@Override
	public void mapItemRemoved(Layer layer, MapItem mapItem) {
		Log.d(this.toString(), "removing mapItem " + mapItem.getData()
				+ " from layer " + layer.getName());
		mapItemToMarker.get(mapItem).remove(); // remove Marker from map
		mapItemToMarker.remove(mapItem);
	}

}
