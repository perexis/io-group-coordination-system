package app.model.local;

public class Point {
	
	private Double longitude;
	private Double latitude;
	
	public Point() {
	}
	
	public Point(Double longitude, Double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return String.format("[longitude=%s, latitude=%s]",
				longitude, latitude);
	}
}
