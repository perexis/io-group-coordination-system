package pl.edu.agh.io.coordinator;

import java.util.ArrayList;

import pl.edu.agh.io.coordinator.resources.Message;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatFragment extends Fragment {

	private final static String SAVED_STATE = "pl.edu.agh.io.coordinator.messagesSavedState";

	private OnFragmentInteractionListener mListener;

	private TextView chatTextView;
	private EditText inputMessage;

	private ArrayList<String> messages;

	// private StringBuffer savedState;

	public static ChatFragment newInstance() {
		ChatFragment fragment = new ChatFragment();
		Bundle args = new Bundle();
		/*
		 * args.putString(ARG_PARAM1, param1); args.putString(ARG_PARAM2,
		 * param2);
		 */
		fragment.setArguments(args);

		return fragment;
	}

	public ChatFragment() {
		messages = new ArrayList<String>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			/*
			 * mParam1 = getArguments().getString(ARG_PARAM1); mParam2 =
			 * getArguments().getString(ARG_PARAM2);
			 */
		}
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);

		Button buttonSendMessage = (Button) view
				.findViewById(R.id.buttonSendMessage);
		buttonSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View buttonView) {
				onSendMessage();
			}
		});

		chatTextView = (TextView) view.findViewById(R.id.textViewChat);
		inputMessage = (EditText) view.findViewById(R.id.inputMessage);

		if(savedInstanceState!=null)
			messages=savedInstanceState.getStringArrayList(SAVED_STATE);
		
		for (String mess : messages) {
			chatTextView.append("\n" + mess);
		}

		// Inflate the layout for this fragment
		return view;
	}

	public void onSendMessage() {
		if (mListener != null && inputMessage.getText().length() != 0) {
			mListener.onChatSendMessage(inputMessage.getText().toString());
			chatTextView.append("\n-->" + inputMessage.getText());
			messages.add("-->" + inputMessage.getText());
			inputMessage.getText().clear();
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
	
	 @Override
	 public void onSaveInstanceState(Bundle outState) {
		 super.onSaveInstanceState(outState);
		 outState.putStringArrayList(SAVED_STATE, messages);
	 }
	 

	public void newMessage(Message m) {
		if (chatTextView != null)
			chatTextView.append("\n" + m.getUserID() + " (" + m.getSentTime()
					+ "): " + m.getText());

		messages.add(m.getUserID() + " (" + m.getSentTime() + "): "
				+ m.getText());
	}

	public interface OnFragmentInteractionListener {
		public void onChatSendMessage(String text);
	}

}
