package liveFeed;

public class BusPosition {
	
	private long timestamp;
	private String trip_id;
	private String route_id;
	private Location location;
	private String direction_id;
	private String schedule_relationship;
	private int current_status;
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getTrip_id() {
		return trip_id;
	}
	public void setTrip_id(String trip_id) {
		this.trip_id = trip_id;
	}
	public String getRoute_id() {
		return route_id;
	}
	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getDirection_id() {
		return direction_id;
	}
	public void setDirection_id(String direction_id) {
		this.direction_id = direction_id;
	}
	public String getSchedule_relationship() {
		return schedule_relationship;
	}
	public void setSchedule_relationship(String schedule_relationship) {
		this.schedule_relationship = schedule_relationship;
	}
	public int getCurrent_status() {
		return current_status;
	}
	public void setCurrent_status(int current_status) {
		this.current_status = current_status;
	}
	
	public String toString(){
		
		String s = "Bus " + this.getTrip_id() ;//+ " with lat " + this.getLocation().getLatitude() + ", lon " + this.getLocation().getLongitude();
		return s;
	}
	
}
