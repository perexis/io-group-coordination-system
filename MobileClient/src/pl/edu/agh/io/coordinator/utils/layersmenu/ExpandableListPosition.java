package pl.edu.agh.io.coordinator.utils.layersmenu;

import android.os.Parcel;
import android.os.Parcelable;

public class ExpandableListPosition implements Parcelable {
	
	public int group;
	public int child;
	
	public ExpandableListPosition(int group, int child) {
		this.group = group;
		this.child = child;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ExpandableListPosition) {
			ExpandableListPosition elp = (ExpandableListPosition) obj;
			if ((this.group == elp.group) && (this.child == elp.child)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (this.group << 16) + this.child;
	}

	public static final Creator<ExpandableListPosition> CREATOR = new Creator<ExpandableListPosition>() {
		@Override
		public ExpandableListPosition createFromParcel(Parcel source) {
			int group = source.readInt();
			int child = source.readInt();
			return new ExpandableListPosition(group, child);
		}
		@Override
		public ExpandableListPosition[] newArray(int size) {
			return new ExpandableListPosition[size];
		}	
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(group);
		dest.writeInt(child);
	}
	
}