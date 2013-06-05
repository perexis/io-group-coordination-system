package pl.edu.agh.io.coordinator;

import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuListAdapter;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuState;
import android.app.Activity;
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

	private int getExpandedGroup() {
		if (listView.isGroupExpanded(ITEM_POSITION)) {
			return ITEM_POSITION;
		} else if (listView.isGroupExpanded(USER_POSITION)) {
			return USER_POSITION;
		} else if (listView.isGroupExpanded(GROUP_POSITION)) {
			return GROUP_POSITION;
		} else if (listView.isGroupExpanded(LAYER_POSITION)) {
			return LAYER_POSITION;
		} else {
			return -1;
		}
	}
	
	private void setExpandedGroup(int group) {
		Log.d("LayersMenuFragment", "starting setExpandedGroup");
		for (int i = 0; i < GROUP_COUNT; ++i) {
			if (i == group) {
				listView.expandGroup(i);
			} else {
				listView.collapseGroup(i);
			}
		}
	}
	
	public LayersMenuFragment() {
	}

	public boolean getCheck(int group, String child) {
		return adapter.getCheck(group, child);
	}
	
	public void setItems(Set<UserItem> items) {
		MainMapActivity activity = (MainMapActivity) getActivity();
		if ((adapter == null) || (activity == null)) {
			return;
		}
		activity.getUserFilter().clearUserItems();
		for (UserItem ui : items) {
			if ((adapter.getItemsChecks().containsKey(ui.getId())) && (adapter.getItemsChecks().get(ui.getId()) == true)) {
				activity.getUserFilter().addUserItem(ui.getId());
			}
		}
		adapter.setItems(items);
	}

	public void setPeople(Set<User> people) {
		MainMapActivity activity = (MainMapActivity) getActivity();
		if ((adapter == null) || (activity == null)) {
			return;
		}
		activity.getUserFilter().clearUsers();
		for (User u : people) {
			if ((adapter.getPeopleChecks().containsKey(u.getId())) && (adapter.getPeopleChecks().get(u.getId()) == true)) {
				activity.getUserFilter().addUser(u.getId());
			}
		}
		adapter.setPeople(people);
	}

	public void setGroups(Set<Group> groups) {
		MainMapActivity activity = (MainMapActivity) getActivity();
		if ((adapter == null) || (activity == null)) {
			return;
		}
		activity.getUserFilter().clearGroups();
		for (Group g : groups) {
			if ((adapter.getGroupsChecks().containsKey(g.getId())) && (adapter.getGroupsChecks().get(g.getId()) == true)) {
				activity.getUserFilter().addGroup(g.getId());
			}
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
	public void onAttach(Activity activity) {
		Log.d("LayersMenuFragment", "starting onAttach");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onCreate");
		adapter = new LayersMenuListAdapter(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
		return toReturn;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("LayersMenuFragment", "starting onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.d("LayersMenuFragment", "starting onStart");
		MainMapActivity activity = (MainMapActivity) getActivity();
		if (activity.getSavedLayersMenuState() != null) {
			LayersMenuState state = activity.getSavedLayersMenuState();
			adapter.setItems(state.items);
			adapter.setPeople(state.people);
			adapter.setGroups(state.groups);
			adapter.setLayers(state.layers);
			adapter.setAllChecks(state.itemsChecks, state.peopleChecks, state.groupsChecks, state.layersChecks);
			setExpandedGroup(state.expandedGroup);
		}
		super.onStart();
	}
	
	@Override
	public void onResume() {
		Log.d("LayersMenuFragment", "starting onResume");
		super.onResume();
	}
	
	@Override
	public void onPause() {
		Log.d("LayersMenuFragment", "starting onPause");
		LayersMenuState state = new LayersMenuState();
		state.expandedGroup = getExpandedGroup();
		state.items = new HashSet<UserItem>(adapter.getItems());
		state.people = new HashSet<User>(adapter.getPeople());
		state.groups = new HashSet<Group>(adapter.getGroups());
		state.layers = new HashSet<Layer>(adapter.getLayers());
		state.itemsChecks = adapter.getItemsChecks();
		state.peopleChecks = adapter.getPeopleChecks();
		state.groupsChecks = adapter.getGroupsChecks();
		state.layersChecks = adapter.getLayersChecks();
		MainMapActivity activity = (MainMapActivity) getActivity();
		activity.setSavedLayersMenuState(state);
		super.onPause();
	}
	
	@Override
	public void onStop() {
		Log.d("LayersMenuFragment", "starting onStop");
		LayersMenuState state = new LayersMenuState();
		state.expandedGroup = getExpandedGroup();
		state.items = new HashSet<UserItem>(adapter.getItems());
		state.people = new HashSet<User>(adapter.getPeople());
		state.groups = new HashSet<Group>(adapter.getGroups());
		state.layers = new HashSet<Layer>(adapter.getLayers());
		state.itemsChecks = adapter.getItemsChecks();
		state.peopleChecks = adapter.getPeopleChecks();
		state.groupsChecks = adapter.getGroupsChecks();
		state.layersChecks = adapter.getLayersChecks();
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		Log.d("LayersMenuFragment", "starting onDestroyView");
		super.onDestroyView();
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
