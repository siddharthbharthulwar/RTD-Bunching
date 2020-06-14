package liveFeed;

public class BusLocation {

	private double latitude;
	private double longitude;
	private long timestamp;
	private String tripID;
	private String routeID;
	private String directionID;
	private String dateTime;
	
	public String getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getTripID() {
		return tripID;
	}

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getRouteID() {
		return routeID;
	}

	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}

	public String getDirectionID() {
		return directionID;
	}

	public void setDirectionID(String directionID) {
		this.directionID = directionID;
	}

	public String toString() {
		
		String s = "(" + this.latitude + ", " + this.longitude + ") | " + this.dateTime;
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