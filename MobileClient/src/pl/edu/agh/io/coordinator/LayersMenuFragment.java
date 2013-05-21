package pl.edu.agh.io.coordinator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuListAdapter;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuState;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LayersMenuFragment extends Fragment {

	private LayersMenuListAdapter adapter;
	private ExpandableListView listView;

	public static final int ITEM_POSITION = 0;
	public static final int USER_POSITION = 1;
	public static final int GROUP_POSITION = 2;
	public static final int LAYER_POSITION = 3;
	public static final int GROUP_COUNT = 4;

	public LayersMenuFragment() {
	}

	public void setItems(Set<UserItem> items) {
		if (adapter == null) {
			return;
		}
		adapter.setItems(items);
	}

	public void setPeople(Set<User> people) {
		if (adapter == null) {
			return;
		}
		adapter.setPeople(people);
	}

	public void setGroups(Set<Group> groups) {
		if (adapter == null) {
			return;
		}
		adapter.setGroups(groups);
	}

	public void setLayers(Set<Layer> layers) {
		if (adapter == null) {
			return;
		}
		adapter.setLayers(layers);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onCreate");
		super.onCreate(savedInstanceState);
		adapter = new LayersMenuListAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("LayersMenuFragment", "starting onCreateView");
		View toReturn = inflater.inflate(R.layout.fragment_layers_menu, container, false);
		listView = (ExpandableListView) toReturn.findViewById(R.id.expandableListView);
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				MainMapActivity activity = (MainMapActivity) LayersMenuFragment.this.getActivity();
				LinearLayout ll = (LinearLayout) v;
				TextView tv = (TextView) ll.getChildAt(0);
				String text = tv.getText().toString();
				boolean newCheck = !adapter.getCheck(groupPosition, text);
				adapter.setCheck(groupPosition, text, newCheck);
				if (newCheck) {
					if (groupPosition == ITEM_POSITION) {
						activity.itemChecked(text);
					} else if (groupPosition == USER_POSITION) {
						activity.userChecked(text);
					} else if (groupPosition == GROUP_POSITION) {
						activity.groupChecked(text);
					} else if (groupPosition == LAYER_POSITION) {
						activity.layerChecked(text);
					}
				} else {
					if (groupPosition == ITEM_POSITION) {
						activity.itemUnchecked(text);
					} else if (groupPosition == USER_POSITION) {
						activity.userUnchecked(text);
					} else if (groupPosition == GROUP_POSITION) {
						activity.groupUnchecked(text);
					} else if (groupPosition == LAYER_POSITION) {
						activity.layerUnchecked(text);
					}
				}
				adapter.notifyDataSetChanged();
				return true;
			}
		});
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if (listView.isGroupExpanded(groupPosition)) {
					listView.collapseGroup(groupPosition);
				} else {
					for (int i = 0; i < GROUP_COUNT; ++i) {
						if (i == groupPosition) {
							listView.expandGroup(i, true);
						} else {
							listView.collapseGroup(i);
						}
					}
				}
				return true;
			}
		});
		MainMapActivity activity = (MainMapActivity) getActivity();
		LayersMenuState state = activity.getSavedState();
		if (state != null) {
			adapter.setItems(state.items);
			adapter.setPeople(state.people);
			adapter.setGroups(state.groups);
			adapter.setLayers(state.layers);
			adapter.setAllChecks(state.itemsChecks, state.peopleChecks, state.groupsChecks, state.layersChecks);
		}
		return toReturn;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("LayersMenuFragment", "starting onSaveInstanceState");
		super.onSaveInstanceState(outState);
		List<UserItem> items = adapter.getItems();
		int itemsLimit = items.size();
		outState.putInt("itemsCount", itemsLimit);
		for (int i = 0; i < itemsLimit; ++i) {
			outState.putParcelable("item" + i, items.get(i));
		}
		List<User> people = adapter.getPeople();
		int peopleLimit = people.size();
		outState.putInt("peopleCount", peopleLimit);
		for (int i = 0; i < peopleLimit; ++i) {
			outState.putParcelable("person" + i, people.get(i));
		}
		List<Group> groups = adapter.getGroups();
		int groupsLimit = groups.size();
		outState.putInt("groupsCount", groupsLimit);
		for (int i = 0; i < groupsLimit; ++i) {
			outState.putParcelable("group" + i, groups.get(i));
		}
		List<Layer> layers = adapter.getLayers();
		int layersLimit = layers.size();
		outState.putInt("layersCount", layersLimit);
		for (int i = 0; i < layersLimit; ++i) {
			outState.putParcelable("layer" + i, layers.get(i));
		}
		Map<String, Boolean> itemsChecks = adapter.getItemsChecks();
		int icLimit = itemsChecks.size();
		outState.putInt("icCount", icLimit);
		Set<String> icSet = itemsChecks.keySet();
		int i1 = 0;
		for (String s : icSet) {
			outState.putString("ick" + i1, s);
			outState.putBoolean("icv" + i1, itemsChecks.get(s));
			++i1;
		}
		Map<String, Boolean> peopleChecks = adapter.getPeopleChecks();
		int pcLimit = peopleChecks.size();
		outState.putInt("pcCount", pcLimit);
		Set<String> pcSet = peopleChecks.keySet();
		int i2 = 0;
		for (String s : pcSet) {
			outState.putString("pck" + i2, s);
			outState.putBoolean("pcv" + i2, peopleChecks.get(s));
			++i2;
		}
		Map<String, Boolean> groupsChecks = adapter.getGroupsChecks();
		int gcLimit = groupsChecks.size();
		outState.putInt("gcCount", gcLimit);
		Set<String> gcSet = groupsChecks.keySet();
		int i3 = 0;
		for (String s : gcSet) {
			outState.putString("gck" + i3, s);
			outState.putBoolean("gcv" + i3, groupsChecks.get(s));
			++i3;
		}
		Map<String, Boolean> layersChecks = adapter.getLayersChecks();
		int lcLimit = layersChecks.size();
		outState.putInt("lcCount", lcLimit);
		Set<String> lcSet = layersChecks.keySet();
		int i4 = 0;
		for (String s : lcSet) {
			outState.putString("lck" + i4, s);
			outState.putBoolean("lcv" + i4, layersChecks.get(s));
			++i4;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onActivityCreated");
		if (savedInstanceState != null) {
			Set<UserItem> items = new HashSet<UserItem>();
			int itemsLimit = savedInstanceState.getInt("itemsCount");
			for (int i = 0; i < itemsLimit; ++i) {
				UserItem item = savedInstanceState.getParcelable("item" + i);
				items.add(item);
			}
			Set<User> people = new HashSet<User>();
			int peopleLimit = savedInstanceState.getInt("peopleCount");
			for (int i = 0; i < peopleLimit; ++i) {
				User person = savedInstanceState.getParcelable("person" + i);
				people.add(person);
			}
			Set<Group> groups = new HashSet<Group>();
			int groupsLimit = savedInstanceState.getInt("groupsCount");
			for (int i = 0; i < groupsLimit; ++i) {
				Group group = savedInstanceState.getParcelable("group" + i);
				groups.add(group);
			}
			Set<Layer> layers = new HashSet<Layer>();
			int layersLimit = savedInstanceState.getInt("layersCount");
			for (int i = 0; i < layersLimit; ++i) {
				Layer layer = savedInstanceState.getParcelable("layer" + i);
				layers.add(layer);
			}
			Map<String, Boolean> itemsChecks = new HashMap<String, Boolean>();
			int icLimit = savedInstanceState.getInt("icCount");
			for (int i = 0; i < icLimit; ++i) {
				itemsChecks.put(savedInstanceState.getString("ick" + i), savedInstanceState.getBoolean("icv" + i));
			}
			Map<String, Boolean> peopleChecks = new HashMap<String, Boolean>();
			int pcLimit = savedInstanceState.getInt("pcCount");
			for (int i = 0; i < pcLimit; ++i) {
				peopleChecks.put(savedInstanceState.getString("pck" + i), savedInstanceState.getBoolean("pcv" + i));
			}
			Map<String, Boolean> groupsChecks = new HashMap<String, Boolean>();
			int gcLimit = savedInstanceState.getInt("gcCount");
			for (int i = 0; i < gcLimit; ++i) {
				groupsChecks.put(savedInstanceState.getString("gck" + i), savedInstanceState.getBoolean("gcv" + i));
			}
			Map<String, Boolean> layersChecks = new HashMap<String, Boolean>();
			int lcLimit = savedInstanceState.getInt("lcCount");
			for (int i = 0; i < lcLimit; ++i) {
				layersChecks.put(savedInstanceState.getString("lck" + i), savedInstanceState.getBoolean("lcv" + i));
			}
			adapter.setItems(items);
			adapter.setPeople(people);
			adapter.setGroups(groups);
			adapter.setAllChecks(itemsChecks, peopleChecks, groupsChecks, layersChecks);
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		Log.d("LayersMenuFragment", "starting onDestroyView");
		super.onDestroyView();
		LayersMenuState state = new LayersMenuState();
		state.items = new HashSet<UserItem>(adapter.getItems());
		state.people = new HashSet<User>(adapter.getPeople());
		state.groups = new HashSet<Group>(adapter.getGroups());
		state.layers = new HashSet<Layer>(adapter.getLayers());
		state.itemsChecks = adapter.getItemsChecks();
		state.peopleChecks = adapter.getPeopleChecks();
		state.groupsChecks = adapter.getGroupsChecks();
		state.layersChecks = adapter.getLayersChecks();
		MainMapActivity activity = (MainMapActivity) getActivity();
		activity.setSavedState(state);
	}

	@Override
	public void onDestroy() {
		Log.d("LayersMenuFragment", "starting onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.d("LayersMenuFragment", "starting onDetach");
		super.onDetach();
	}

}
