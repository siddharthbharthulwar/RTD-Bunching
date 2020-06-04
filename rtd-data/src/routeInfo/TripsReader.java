package routeInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;

public class TripsReader {
	
	public List<String> block_id = new ArrayList<String>();
	public List<String> route_id = new ArrayList<String>();
	public List<Integer> direction_id = new ArrayList<Integer>();
	public List<String> trip_headsign = new ArrayList<String>();
	public List<String> shape_id = new ArrayList<String>();
	public List<String> service_id = new ArrayList<String>();
	public List<String> trip_id = new ArrayList<String>();
	public HashMap<String, Trip> trips = new HashMap<String, Trip>();
	
	public TripsReader() throws IOException {
		
		this.readTrips();
		this.parseTrips();
	}
	
	public void readTrips() throws IOException {
		
		FileReader reader = new FileReader("res/trips.txt");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		int lineIndex = 0;
		
		while ((line = bufferedReader.readLine()) != null) {
			if (lineIndex > 0) {
				
				StringTokenizer st = new StringTokenizer(line, ",");
	            int index = 0;
	            
	            while (st.hasMoreTokens()) {
	            	
	            	String token = st.nextToken();
	            	
	            	if (index == 0) {
	            		
	                	block_id.add(token);
	                	
	            	}
	            	else if (index == 1) {
	            		
	                	route_id.add(token);

	            	}
	            	else if (index == 2) {
	            		
	            		direction_id.add(Integer.valueOf(token));

	            	}
	            	else if (index == 3) {

	                	trip_headsign.add(token);

	            	}
	            	else if (index == 4) {
	            		
	                	shape_id.add(token);

	            	}
	            	else if (index == 5) {
	            		
	                	service_id.add(token);
	            		

	            	}
	            	else {
	            		
	                	trip_id.add(token);	
	          
	            	}
	            	
	            	index++;
	            }
			}
			
			lineIndex++;			
        }
		
		bufferedReader.close();
	}
	
	public void parseTrips() {
		
		for (int i = 0; i < this.direction_id.size(); i++) {
			
			Trip t = new Trip(this.block_id.get(i), this.route_id.get(i), this.direction_id.get(i),
					this.trip_headsign.get(i), this.shape_id.get(i), this.service_id.get(i), 
					this.trip_id.get(i));
			
			this.trips.put(this.trip_id.get(i), t);
		}
	}
	
	public HashMap<String, Trip> getTrips(){
		
		return this.trips;
	}

}
