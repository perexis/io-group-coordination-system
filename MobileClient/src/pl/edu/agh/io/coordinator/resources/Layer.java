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

package pl.edu.agh.io.coordinator.resources;

import android.os.Parcel;
import android.os.Parcelable;

public class Layer implements Parcelable {

	private String name;

	public Layer(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static final Creator<Layer> CREATOR = new Creator<Layer>() {
		@Override
		public Layer createFromParcel(Parcel source) {
			String name = source.readString();
			return new Layer(name);
		}

		@Override
		public Layer[] newArray(int size) {
			return new Layer[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Layer) {
			return ((Layer) o).getName().equals(this.getName());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
	}

}

