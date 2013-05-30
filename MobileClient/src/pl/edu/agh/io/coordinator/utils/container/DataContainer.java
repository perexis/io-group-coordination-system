package pl.edu.agh.io.coordinator.utils.container;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.io.coordinator.MainMapActivity;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.MapItem;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserState;
import android.app.Activity;
import android.util.Log;

public class DataContainer {

	private OnDataContainerChangesListener listener;
	private MainMapActivity activity;
	private Map<MapItem, Layer> mapItems = Collections.synchronizedMap(new HashMap<MapItem, Layer>());
	private Map<User, UserState> users = Collections.synchronizedMap(new HashMap<User, UserState>());
	
	public DataContainer(Activity activity) {
		try {
			this.listener = (OnDataContainerChangesListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDataContainerChangesListener");
		}
		this.activity = (MainMapActivity) activity;
	}

	public synchronized void newUsersSet(Map<User, UserState> newUsers) {
		HashSet<User> toRemove = new HashSet<User>();
		for (User u : users.keySet()) {
			if (!newUsers.containsKey(u)) {
				listener.userRemoved(u);
				toRemove.add(u);
			}
		}
		for (User u : toRemove) {
			users.remove(u);
		}
		for (User u : newUsers.keySet()) {
			if ((!users.containsKey(u)) || (!users.get(u).equals(newUsers.get(u)))) {
				users.put(u, newUsers.get(u));
				listener.userAdded(u, newUsers.get(u));
			}
		}
	}
	
	public synchronized void newMapItemsSet(Layer layer, Set<MapItem> newMapItems) {
		HashSet<MapItem> toRemove = new HashSet<MapItem>();
		for (MapItem i : mapItems.keySet()) {
			if (mapItems.get(i).equals(layer) && (!newMapItems.contains(i))) {
				Log.d("DataContainer", "removing mapItem " + i.getData() + " from layer " + layer.getName());
				listener.mapItemRemoved(layer, i);
				toRemove.add(i);
			}
		}
		for (MapItem i : toRemove) {
			mapItems.remove(i);
		}
		for (MapItem i : newMapItems)
			if (!mapItems.keySet().contains(i)) {
				mapItems.put(i, layer);
				if (activity.isLayerActive(layer.getName())) {
					listener.mapItemAdded(layer, i);
				}
			}
	}

	public synchronized void showLayer(String layer) {
		for (MapItem i : mapItems.keySet()) {
			if (mapItems.get(i).getName().equals(layer)) {
				listener.mapItemAdded(new Layer(layer), i);
			}
		}
	}
	
	public synchronized void hideLayer(String layer) {
		for (MapItem i : mapItems.keySet()) {
			if (mapItems.get(i).getName().equals(layer)) {
				listener.mapItemRemoved(new Layer(layer), i);
			}
		}
	}
	
	public interface OnDataContainerChangesListener {
		public void mapItemAdded(Layer layer, MapItem mapItem);
		public void mapItemRemoved(Layer layer, MapItem mapItem);
		public void userAdded(User user, UserState state);
		public void userRemoved(User user);
	}
}
