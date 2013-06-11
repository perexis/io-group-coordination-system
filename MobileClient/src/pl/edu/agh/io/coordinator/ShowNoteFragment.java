package pl.edu.agh.io.coordinator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ShowNoteFragment extends Fragment {
	private static final String ARG_NOTE = "note";
	
	private String note;
	
	public static ShowNoteFragment newInstance(String note) {
		ShowNoteFragment fragment = new ShowNoteFragment();
		Bundle args = new Bundle();
		args.putString(ARG_NOTE, note);
		fragment.setArguments(args);
		return fragment;
	}

	public ShowNoteFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			note = getArguments().getString(ARG_NOTE);
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_show_note, container, false);
		
		TextView text =(TextView)( view.findViewById(R.id.noteContent));
		text.setText(note);
		
		return view;
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
