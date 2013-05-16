package pl.edu.agh.io.coordinator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.MapItem;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LayersMenuFragment extends Fragment {

	private class LayersMenuListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private List<String> menuGroups;
		private List<UserItem> items;
		private List<User> people;
		private List<Group> groups;

		public LayersMenuListAdapter(Context context) {
			this.context = context;
			this.menuGroups = new ArrayList<String>();
			this.menuGroups.add("Items");
			this.menuGroups.add("People");
			this.menuGroups.add("Groups");
			this.items = new ArrayList<UserItem>();
			this.people = new ArrayList<User>();
			this.groups = new ArrayList<Group>();
		}

		public List<UserItem> getItems() {
			return this.items;
		}
		
		public List<User> getPeople() {
			return this.people;
		}
		
		public List<Group> getGroups() {
			return this.groups;
		}
		
		public void setItems(Set<UserItem> items) {
			this.items = new ArrayList<UserItem>(items);
			notifyDataSetChanged();
		}

		public void setPeople(Set<User> people) {
			this.people = new ArrayList<User>(people);
			notifyDataSetChanged();
		}

		public void setGroups(Set<Group> groups) {
			this.groups = new ArrayList<Group>(groups);
			notifyDataSetChanged();
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			if (groupPosition == ITEM_POSITION) {
				return items.get(childPosition);
			} else if (groupPosition == USER_POSITION) {
				return people.get(childPosition);
			} else if (groupPosition == GROUP_POSITION) {
				return groups.get(childPosition);
			} else {
				return null;
			}
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			Object object = getChild(groupPosition, childPosition);
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.child_layout, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
			String text = null;
			if (groupPosition == ITEM_POSITION) {
				text = ((UserItem) object).getId();
			} else if (groupPosition == USER_POSITION) {
				text = ((User) object).getId();
			} else if (groupPosition == GROUP_POSITION) {
				text = ((Group) object).getId();
			}
			tv.setText("   " + text);
			tv.setTextColor(Color.BLACK);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (groupPosition == ITEM_POSITION) {
				return items.size();
			} else if (groupPosition == USER_POSITION) {
				return people.size();
			} else if (groupPosition == GROUP_POSITION) {
				return groups.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			return menuGroups.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return 3;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			String group = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.group_layout, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
			tv.setText(group);
			tv.setTextColor(Color.BLACK);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}

	}

	private class ExpandableListPosition {
		
		public int group;
		public int child;
		
		public ExpandableListPosition(int group, int child) {
			this.group = group;
			this.child = child;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ExpandableListPosition) {
				ExpandableListPosition elp = (ExpandableListPosition) obj;
				if ((this.group == elp.group) && (this.child == elp.child)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return (this.group << 16) + this.child;
		}
		
	}
	
	private static final int ITEM_POSITION = 0;
	private static final int USER_POSITION = 1;
	private static final int GROUP_POSITION = 2;
	
	private LayersMenuListAdapter adapter;
	private ExpandableListView listView;
	private List<ExpandableListPosition> checked;

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
		adapter.setItems(items);
		clearGroupChecks(ITEM_POSITION);
	}
	
	public void setPeople(Set<User> people) {
		adapter.setPeople(people);
		clearGroupChecks(USER_POSITION);
	}
	
	public void setGroups(Set<Group> groups) {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("LayersMenuFragment", "starting onCreateView");
		View toReturn = inflater.inflate(R.layout.fragment_layers_menu, container, false);
		listView = (ExpandableListView) toReturn.findViewById(R.id.expandableListView);
		listView.setAdapter(adapter);
		listView.setOnChildClickListener(new OnChildClickListener() {	
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				LinearLayout ll = (LinearLayout) v;
				TextView tv = (TextView) ll.getChildAt(0);
				tv.setBackgroundColor(Color.GRAY);
				checked.add(new ExpandableListPosition(groupPosition, childPosition));
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
