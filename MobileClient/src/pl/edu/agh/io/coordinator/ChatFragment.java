package pl.edu.agh.io.coordinator;

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

	private OnFragmentInteractionListener mListener;

	private TextView chatTextView;
	private EditText inputMessage;

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

		// Inflate the layout for this fragment
		return view;
	}

	public void onSendMessage() {
		if (mListener != null && inputMessage.getText().length()!=0) {
			mListener.onChatSendMessage(inputMessage.getText().toString());
			chatTextView.append("\n-->" + inputMessage.getText());
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

	public void newMessage(Message m) {
		chatTextView.append("\n" + m.getUserID()+" (" + m.getSentTime() +"): " + m.getText());
	}

	public interface OnFragmentInteractionListener {
		public void onChatSendMessage(String text);
	}

}
