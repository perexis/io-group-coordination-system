package pl.edu.agh.io.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.edu.agh.io.coordinator.resources.Group;
import pl.edu.agh.io.coordinator.resources.MapItem;
import pl.edu.agh.io.coordinator.resources.User;
import pl.edu.agh.io.coordinator.resources.UserItem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class LayersMenuFragment extends Fragment {
	
	private class LayersMenuListAdapter extends BaseExpandableListAdapter {

		private static final int ITEM_POSITION = 0;
		private static final int USER_POSITION = 1;
		private static final int GROUP_POSITION = 2;
		
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
		
		public void setItems(Set<UserItem> items) {
			this.items = new ArrayList<UserItem>(items);
		}
		
		public void setPeople(Set<User> people) {
			this.people = new ArrayList<User>(people);
		}
		
		public void setGroups(Set<Group> groups) {
			this.groups = new ArrayList<Group>(groups);
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
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment LayersMenuFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static LayersMenuFragment newInstance(String param1, String param2) {
		LayersMenuFragment fragment = new LayersMenuFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public LayersMenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}		
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater
				.inflate(R.layout.fragment_layers_menu, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
