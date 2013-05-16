package pl.edu.agh.io.coordinator.utils.layersmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.LayersMenuFragment;
import pl.edu.agh.io.coordinator.R;
import pl.edu.agh.io.coordinator.R.id;
import pl.edu.agh.io.coordinator.R.layout;
import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class LayersMenuListAdapter extends BaseExpandableListAdapter {

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
		if (groupPosition == LayersMenuFragment.ITEM_POSITION) {
			return items.get(childPosition);
		} else if (groupPosition == LayersMenuFragment.USER_POSITION) {
			return people.get(childPosition);
		} else if (groupPosition == LayersMenuFragment.GROUP_POSITION) {
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
		if (groupPosition == LayersMenuFragment.ITEM_POSITION) {
			text = ((UserItem) object).getId();
		} else if (groupPosition == LayersMenuFragment.USER_POSITION) {
			text = ((User) object).getId();
		} else if (groupPosition == LayersMenuFragment.GROUP_POSITION) {
			text = ((Group) object).getId();
		}
		tv.setText("   " + text);
		tv.setTextColor(Color.BLACK);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == LayersMenuFragment.ITEM_POSITION) {
			return items.size();
		} else if (groupPosition == LayersMenuFragment.USER_POSITION) {
			return people.size();
		} else if (groupPosition == LayersMenuFragment.GROUP_POSITION) {
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