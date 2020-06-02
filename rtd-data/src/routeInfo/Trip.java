package routeInfo;

import java.util.ArrayList;
import java.util.List;

import liveFeed.BusLocation;

public class Trip {

	private String block_id;
	private String route_id;
	private int direction_id;
	private String trip_headsign;
	private String shape_id;
	private String service_id;
	private String trip_id;
	
	public List<BusLocation> locations = new ArrayList<BusLocation>();
	
	public Trip(String block_id, String route_id, int direction_id, String trip_headsign, String shape_id,
			String service_id, String trip_id) {
		super();
		this.block_id = block_id;
		this.route_id = route_id;
		this.direction_id = direction_id;
		this.trip_headsign = trip_headsign;
		this.shape_id = shape_id;
		this.service_id = service_id;
		this.trip_id = trip_id;
	}
	
	public String toString() {
		
		String s = this.trip_id + " in direction " + this.direction_id + " : " + this.trip_headsign;
		return s;
	}

	public String getBlock_id() {
		return block_id;
	}

	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}

	public String getRoute_id() {
		return route_id;
	}

	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}

	public int getDirection_id() {
		return direction_id;
	}

	public void setDirection_id(int direction_id) {
		this.direction_id = direction_id;
	}

	public String getTrip_headsign() {
		return trip_headsign;
	}

	public void setTrip_headsign(String trip_headsign) {
		this.trip_headsign = trip_headsign;
	}

	public String getShape_id() {
		return shape_id;
	}

	public void setShape_id(String shape_id) {
		this.shape_id = shape_id;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getTrip_id() {
		return trip_id;
	}

	public void setTrip_id(String trip_id) {
		this.trip_id = trip_id;
	}
	
}
