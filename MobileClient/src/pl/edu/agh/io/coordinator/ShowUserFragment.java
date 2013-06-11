package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ShowUserFragment extends Fragment {
	private static final String ARG_USER = "user";

	private String userName;

	public static ShowUserFragment newInstance(String user) {
		ShowUserFragment fragment = new ShowUserFragment();
		Bundle args = new Bundle();
		args.putString(ARG_USER, user);
		fragment.setArguments(args);
		return fragment;
	}

	public ShowUserFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			userName = getArguments().getString(ARG_USER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show_user, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
