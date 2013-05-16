package pl.edu.agh.io.coordinator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import pl.edu.agh.io.coordinator.utils.layersmenu.ExpandableListPosition;
import pl.edu.agh.io.coordinator.utils.layersmenu.LayersMenuListAdapter;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LayersMenuFragment extends Fragment {

	private LayersMenuListAdapter adapter;
	private ExpandableListView listView;
	private List<ExpandableListPosition> checked;

	public static final int ITEM_POSITION = 0;
	public static final int USER_POSITION = 1;
	public static final int GROUP_POSITION = 2;

	private void clearGroupChecks(int group) {
		for (ExpandableListPosition elp : checked) {
			if (elp.group == group) {
				checked.remove(elp);
			}
		}
	}

	public LayersMenuFragment() {
		checked = new LinkedList<ExpandableListPosition>();
	}

	public void setItems(Set<UserItem> items) {
		if (adapter == null) {
			return;
		}
		adapter.setItems(items);
		clearGroupChecks(ITEM_POSITION);
	}

	public void setPeople(Set<User> people) {
		if (adapter == null) {
			return;
		}
		adapter.setPeople(people);
		clearGroupChecks(USER_POSITION);
	}

	public void setGroups(Set<Group> groups) {
		if (adapter == null) {
			return;
		}
		adapter.setGroups(groups);
		clearGroupChecks(GROUP_POSITION);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onCreate");
		adapter = new LayersMenuListAdapter(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onCreateView");
		View toReturn = inflater.inflate(R.layout.fragment_layers_menu,
				container, false);
		listView = (ExpandableListView) toReturn
				.findViewById(R.id.expandableListView);
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				LinearLayout ll = (LinearLayout) v;
				TextView tv = (TextView) ll.getChildAt(0);
				tv.setBackgroundColor(Color.GRAY);
				checked.add(new ExpandableListPosition(groupPosition,
						childPosition));
				return true;
			}
		});
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
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onActivityCreated");
		super.onActivityCreated(savedInstanceState);
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
			adapter.setItems(items);
			adapter.setPeople(people);
			adapter.setGroups(groups);
		}
	}

}
