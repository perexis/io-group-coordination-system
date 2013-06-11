package pl.edu.agh.io.coordinator.utils.chat;

import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ChatState implements Parcelable {

	public List<String> messages;
	public String inputText;
	
	public ChatState() {
	}
	
	public static final Creator<ChatState> CREATOR = new Creator<ChatState>() {
		@Override
		public ChatState createFromParcel(Parcel source) {
			ChatState state = new ChatState();
			int messagesSize = source.readInt();
			state.messages = new LinkedList<String>();
			for (int i = 0; i < messagesSize; ++i) {
				String s = source.readString();
				Log.d("ChatState", "retrieving message from parcel");
				state.messages.add(s);
			}
			state.inputText = source.readString();
			return state;
		}
		@Override
		public ChatState[] newArray(int size) {
			return new ChatState[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(messages.size());
		for (String s : messages) {
			dest.writeString(s);
		}
		dest.writeString(inputText);
	}
	
}
