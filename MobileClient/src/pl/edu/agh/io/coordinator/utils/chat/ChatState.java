/*
 * Copyright 2013
 * Piotr Bryk, Wojciech Grajewski, Rafał Szalecki, Piotr Szmigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http: *www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

