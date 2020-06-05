package liveFeed;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.transit.realtime.GtfsRealtime;
import kong.unirest.Unirest;
import routeInfo.RoutesReader;
import routeInfo.Trip;
import routeInfo.TripsReader;


public class FeedPoller {
	
	//Vehicle position data are stored in Google Protocol Buffer format, which is downloadable from server with credentials: 
	
	public final String username = "RTDgtfsRT";
	public final String password = "realT!m3Feed";
	public final String url = "http://www.rtd-denver.com/google_sync/VehiclePosition.pb";
	public final int secondBuffer = 30;
	
	public TripsReader tripsReader;
	//public RoutesReader routesReader;  THIS PROBABLY ISNT NEEDED RIGHT NOW 
	
	public HashMap<String, List<BusLocation>> trips = new HashMap<String, List<BusLocation>>();
	
	public FeedPoller() throws IOException {
		
		this.tripsReader = new TripsReader();
		//this.routesReader = new RoutesReader(); THIS PROBABLY ISNT NEEDED RIGHT NOW
		
		for (Map.Entry<String, Trip> entry: this.tripsReader.getTrips().entrySet()) {
			
			this.trips.put(entry.getKey(), new ArrayList<BusLocation>());
		}
		
	}
	
	//DO NOT CALL THIS FUNCTION MORE THAN ONCE EVERY THIRTY SECONDS
	
	private void getBusPositions() throws IOException
	{
		long previousCallSeconds = this.read();
		Instant instant = Instant.now();
		long currentCallSeconds = instant.getEpochSecond();
		
		if (currentCallSeconds - previousCallSeconds >= this.secondBuffer) {

			Unirest.get(url).basicAuth(this.username, this.password).thenConsume(rawResponse -> {
								
				InputStream stream = rawResponse.getContent();
				try {
					GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(stream);
					
					for (GtfsRealtime.FeedEntity entity: feed.getEntityList()) {

						GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();
						BusLocation location = new BusLocation();
						location.setLatitude(vehiclePosition.getPosition().getLatitude());
						location.setLongitude(vehiclePosition.getPosition().getLongitude());
						location.setTimestamp(vehiclePosition.getTimestamp());
						
						String key = vehiclePosition.getTrip().getTripId();
						
						List<BusLocation> currentList = this.trips.get(key);
						
					    if(currentList == null) {
					    	
					         currentList = new ArrayList<BusLocation>();
					         currentList.add(location);
					         this.trips.put(key, currentList);
					         
					    } else {
					    	
					        if(!currentList.contains(location)) currentList.add(location);
					    }

					}
				} 

				catch (IOException e) {
					e.printStackTrace();
				}
				
			});
			
			this.write();
		}
		else {
			
			System.out.println("ERROR: WAIT " + (this.secondBuffer - (currentCallSeconds - previousCallSeconds)) +  " SECONDS BEFORE CALLING FUNCTION AGAIN");
		}
	}
	
	public long read() throws IOException {
		
		FileReader reader = new FileReader("res/lastupdate.txt");
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String line = bufferedReader.readLine();
		bufferedReader.close();
		
		return Long.valueOf(line);
	}
	
	public void write() throws IOException {
		
		FileWriter writer = new FileWriter("res/lastupdate.txt", false);
		Instant instant = Instant.now();
		long timeStampSeconds = instant.getEpochSecond();
		writer.write(String.valueOf(timeStampSeconds));
		writer.close();
	}
	
	public void timedOperation(long minutes, int timeInterval, FeedPoller poller) {
		
		timeInterval = 1000 * timeInterval;
		Timer timer = new Timer();
		int begin = 0;
		Instant instant = Instant.now();
		long beginningTime = instant.getEpochSecond();
		
		timer.schedule(new TimerTask() {
		   @Override
		   public void run() {
			   try {
				poller.getBusPositions();
				for (Map.Entry<String, List<BusLocation>> entry: poller.trips.entrySet()) {
					String key = entry.getKey();
					List<BusLocation> locationSet = entry.getValue();
					
					if (locationSet.size() > 0) {
						
						System.out.println(poller.tripsReader.getTrips().get(key) + " | #: " + locationSet.size());
						//THIS IS WHERE SAVING FUNCTION GOES
					}
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		       Instant instant2 = Instant.now();
		       long endingTime = instant2.getEpochSecond();
		       System.out.println("Time: " + (endingTime - beginningTime));
		       
		       if ((endingTime - beginningTime) / 60 >= minutes){
		         timer.cancel();
		       }
		   }
		}, begin, timeInterval);
	}
	
	
	//CURRENTLY WORKING ON INITIALIZING ALL TEXT FILES FOR DATA STORAGE
	public static void main(String[] args) throws IOException {
		
		FeedPoller poller = new FeedPoller();
		poller.timedOperation(60, 31, poller);
		
		/*
		for (Map.Entry<String, List<BusLocation>> entry: poller.trips.entrySet()) {
			
			String key = entry.getKe
			String filepath = "data/" + key + ".txt";
			File file = new File(filepath);
			file.createNewFile();
		}
		*/
	}
	
}


