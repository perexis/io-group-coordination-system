package pl.edu.agh.io.coordinator.utils.layersmenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.io.coordinator.LayersMenuFragment;
import pl.edu.agh.io.coordinator.R;
import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.Layer;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class LayersMenuListAdapter extends BaseExpandableListAdapter {

	private class UserItemComparator implements Comparator<UserItem> {

		@Override
		public int compare(UserItem lhs, UserItem rhs) {
			return lhs.getId().compareTo(rhs.getId());
		}

	}

	private class UserComparator implements Comparator<User> {

		@Override
		public int compare(User lhs, User rhs) {
			return lhs.getId().compareTo(rhs.getId());
		}

	}

	private class GroupComparator implements Comparator<Group> {

		@Override
		public int compare(Group lhs, Group rhs) {
			return lhs.getId().compareTo(rhs.getId());
		}

	}

	private class LayerComparator implements Comparator<Layer> {

		@Override
		public int compare(Layer lhs, Layer rhs) {
			return lhs.getName().compareTo(rhs.getName());
		}

	}

	private Context context;
	private List<String> menuGroups;
	private List<UserItem> items;
	private List<User> people;
	private List<Group> groups;
	private List<Layer> layers;
	private Map<String, Boolean> itemsChecks;
	private Map<String, Boolean> peopleChecks;
	private Map<String, Boolean> groupsChecks;
	private Map<String, Boolean> layersChecks;

	public LayersMenuListAdapter(Context context) {
		this.context = context;
		this.menuGroups = new ArrayList<String>();
		this.menuGroups.add(context.getString(R.string.menu_item_items));
		this.menuGroups.add(context.getString(R.string.menu_item_people));
		this.menuGroups.add(context.getString(R.string.menu_item_groups));
		this.menuGroups.add(context.getString(R.string.menu_item_layers));
		this.items = new ArrayList<UserItem>();
		this.people = new ArrayList<User>();
		this.groups = new ArrayList<Group>();
		this.layers = new ArrayList<Layer>();
		this.itemsChecks = new HashMap<String, Boolean>();
		this.peopleChecks = new HashMap<String, Boolean>();
		this.groupsChecks = new HashMap<String, Boolean>();
		this.layersChecks = new HashMap<String, Boolean>();
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

	public List<Layer> getLayers() {
		return this.layers;
	}

	public Map<String, Boolean> getItemsChecks() {
		return itemsChecks;
	}

	public Map<String, Boolean> getPeopleChecks() {
		return peopleChecks;
	}

	public Map<String, Boolean> getGroupsChecks() {
		return groupsChecks;
	}

	public Map<String, Boolean> getLayersChecks() {
		return layersChecks;
	}

	public void setAllChecks(Map<String, Boolean> items, Map<String, Boolean> people, Map<String, Boolean> groups,
			Map<String, Boolean> layers) {
		this.itemsChecks = items;
		this.peopleChecks = people;
		this.groupsChecks = groups;
		this.layersChecks = layers;
		notifyDataSetChanged();
	}

	public void setCheck(int group, String child, boolean check) {
		Map<String, Boolean> map = null;
		if (group == LayersMenuFragment.ITEM_POSITION) {
			map = itemsChecks;
		} else if (group == LayersMenuFragment.USER_POSITION) {
			map = peopleChecks;
		} else if (group == LayersMenuFragment.GROUP_POSITION) {
			map = groupsChecks;
		} else if (group == LayersMenuFragment.LAYER_POSITION) {
			map = layersChecks;
		} else {
			return;
		}
		map.put(child, check);
	}

	public Boolean getCheck(int group, String child) {
		Map<String, Boolean> map = null;
		if (group == LayersMenuFragment.ITEM_POSITION) {
			map = itemsChecks;
		} else if (group == LayersMenuFragment.USER_POSITION) {
			map = peopleChecks;
		} else if (group == LayersMenuFragment.GROUP_POSITION) {
			map = groupsChecks;
		} else if (group == LayersMenuFragment.LAYER_POSITION) {
			map = layersChecks;
		} else {
			return null;
		}
		return map.get(child);
	}

	public void setItems(Set<UserItem> items) {
		ArrayList<UserItem> list = new ArrayList<UserItem>(items);
		Collections.sort(list, new UserItemComparator());
		this.items = list;
		notifyDataSetChanged();
	}

	public void setPeople(Set<User> people) {
		ArrayList<User> list = new ArrayList<User>(people);
		Collections.sort(list, new UserComparator());
		this.people = list;
		notifyDataSetChanged();
	}

	public void setGroups(Set<Group> groups) {
		ArrayList<Group> list = new ArrayList<Group>(groups);
		Collections.sort(list, new GroupComparator());
		this.groups = list;
		notifyDataSetChanged();
	}

	public void setLayers(Set<Layer> layers) {
		ArrayList<Layer> list = new ArrayList<Layer>(layers);
		Collections.sort(list, new LayerComparator());
		this.layers = list;
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
		} else if (groupPosition == LayersMenuFragment.LAYER_POSITION) {
			return layers.get(childPosition);
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
		boolean isChecked = false;
		if (groupPosition == LayersMenuFragment.ITEM_POSITION) {
			text = ((UserItem) object).getId();
			if (itemsChecks.containsKey(text)) {
				isChecked = itemsChecks.get(text);
			} else {
				itemsChecks.put(text, false);
			}
		} else if (groupPosition == LayersMenuFragment.USER_POSITION) {
			text = ((User) object).getId();
			if (peopleChecks.containsKey(text)) {
				isChecked = peopleChecks.get(text);
			} else {
				peopleChecks.put(text, false);
			}
		} else if (groupPosition == LayersMenuFragment.GROUP_POSITION) {
			text = ((Group) object).getId();
			if (groupsChecks.containsKey(text)) {
				isChecked = groupsChecks.get(text);
			} else {
				groupsChecks.put(text, false);
			}
		} else if (groupPosition == LayersMenuFragment.LAYER_POSITION) {
			text = ((Layer) object).getName();
			if (layersChecks.containsKey(text)) {
				isChecked = layersChecks.get(text);
			} else {
				layersChecks.put(text, false);
			}
		}
		tv.setText(text);
		Log.d("LayersMenuListAdapter", "values: text = " + text + ", isChecked = " + isChecked);
		Log.d("LayersMenuListAdapter",
				"values: sizes are " + itemsChecks.size() + ", " + peopleChecks.size() + ", " + groupsChecks.size() + ", "
						+ layersChecks.size());
		if (isChecked) {
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundColor(Color.BLACK);
		} else {
			tv.setTextColor(Color.BLACK);
			tv.setBackgroundColor(Color.WHITE);
		}
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
		} else if (groupPosition == LayersMenuFragment.LAYER_POSITION) {
			return layers.size();
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
		return LayersMenuFragment.GROUP_COUNT;
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