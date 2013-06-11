package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShowImageFragment extends Fragment {
	private static final String ARG_IMAGE = "image";

	private String imageUrl;

	public static ShowImageFragment newInstance(String image) {
		ShowImageFragment fragment = new ShowImageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_IMAGE, image);
		fragment.setArguments(args);
		return fragment;
	}

	public ShowImageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			imageUrl = getArguments().getString(ARG_IMAGE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show_image, container, false);
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
