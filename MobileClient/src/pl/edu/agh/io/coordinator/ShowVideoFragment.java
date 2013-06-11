package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShowVideoFragment extends Fragment {
	private static final String ARG_VIDEO = "video";

	private String videoUrl;

	public static ShowVideoFragment newInstance(String video) {
		ShowVideoFragment fragment = new ShowVideoFragment();
		Bundle args = new Bundle();
		args.putString(ARG_VIDEO, video);
		fragment.setArguments(args);
		return fragment;
	}

	public ShowVideoFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			videoUrl = getArguments().getString(ARG_VIDEO);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_show_video, container, false);
	}

	public void onButtonPressed(Uri uri) {
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
