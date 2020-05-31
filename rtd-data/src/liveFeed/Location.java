package liveFeed;

public class Location {

	private double latitude;
	private double longitude;
	
	public String toString() {
		
		String s = this.latitude + ", " + this.longitude;
		return s;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
