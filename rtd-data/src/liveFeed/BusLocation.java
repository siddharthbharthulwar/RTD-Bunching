package liveFeed;

public class BusLocation {

	private double latitude;
	private double longitude;
	private long timestamp;
	
	public String toString() {
		
		String s = "(" + this.latitude + ", " + this.longitude + ") | " + this.timestamp;
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
	
	public long getTimestamp() {
		
		return this.timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		
		this.timestamp = timestamp;
	}
	
	
}
