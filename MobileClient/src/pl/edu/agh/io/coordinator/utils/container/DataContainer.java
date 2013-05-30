package pl.edu.agh.io.coordinator.utils.container;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.io.coordinator.MainMapActivity;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.MapItem;
import android.app.Activity;
import android.util.Log;

public class DataContainer {

	private OnDataContainerChangesListener listener;
	private MainMapActivity activity;
	private Map<MapItem, Layer> mapItems = Collections.synchronizedMap(new HashMap<MapItem, Layer>());

	public DataContainer(Activity activity) {
		try {
			this.listener = (OnDataContainerChangesListener) activity;
			this.activity = (MainMapActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnDataContainerChangesListener");
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
	}
}
