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

