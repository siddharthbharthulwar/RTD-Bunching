package routeInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class TripsReader {
	
	public List<String> block_id = new ArrayList<String>();
	public List<String> route_id = new ArrayList<String>();
	public List<Integer> direction_id = new ArrayList<Integer>();
	public List<String> trip_headsign = new ArrayList<String>();
	public List<String> shape_id = new ArrayList<String>();
	public List<String> service_id = new ArrayList<String>();
	public List<String> trip_id = new ArrayList<String>();
	
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
	            	
	            	if (index == 0) {
	            		
	                	block_id.add(st.nextToken());
	                	
	            	}
	            	else if (index == 1) {
	            		
	                	route_id.add(st.nextToken());

	            	}
	            	else if (index == 2) {
	            		
	            		String s = st.nextToken();
	            		if (s != null) {
	            			System.out.println(s);
	            			direction_id.add(Integer.valueOf(s));
	            		}

	            	}
	            	else if (index == 3) {

	                	trip_headsign.add(st.nextToken());

	            	}
	            	else if (index == 4) {
	            		
	                	shape_id.add(st.nextToken());

	            	}
	            	else if (index == 5) {
	            		
	                	service_id.add(st.nextToken());
	            		

	            	}
	            	else {
	            		
	                	trip_id.add(st.nextToken());	
	          
	            	}
	            	
	            	index++;
	            }

			}
        }
		
		bufferedReader.close();
	}
	
	public static void main(String[] args) throws IOException
	{
		TripsReader tr = new TripsReader();
		tr.readTrips();
		System.out.println(tr.block_id.size());
		System.out.println(tr.route_id.size());
		System.out.println(tr.direction_id.size());
		System.out.println(tr.trip_headsign.size());
		System.out.println(tr.shape_id.size());
		System.out.println(tr.service_id.size());
		System.out.println(tr.trip_id.size());

		
	}
	
}
