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
		if (o instanceof Layer)
			return ((Layer) o).getName().equals(this.getName());
		else
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
