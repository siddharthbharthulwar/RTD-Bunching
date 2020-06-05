package liveFeed;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.transit.realtime.GtfsRealtime;
import com.opencsv.CSVWriter;

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
	
	public HashMap<String, BusLocation> returnedBuses = new HashMap<String, BusLocation>();
	
	public FeedPoller() throws IOException {
		
		this.tripsReader = new TripsReader();
		//this.routesReader = new RoutesReader(); THIS PROBABLY ISNT NEEDED RIGHT NOW
		
	}
	
	//DO NOT CALL THIS FUNCTION MORE THAN ONCE EVERY THIRTY SECONDS
	
	private void getBusPositions() throws IOException
	{
		long previousCallSeconds = this.read();
		Instant instant = Instant.now();
		long currentCallSeconds = instant.getEpochSecond();
		
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
		
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
						Trip currentTrip = this.tripsReader.getTrips().get(key);
						
						location.setDirectionID(Integer.toString(currentTrip.getDirection_id()));
						location.setRouteID(currentTrip.getRoute_id());
						location.setTripID(key);
						location.setDateTime(dtf.format(date));
												
						this.returnedBuses.put(key, location);

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
			System.exit(0);
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
	
	public void timedOperation(long minutes, int timeInterval, FeedPoller poller) throws IOException {
		
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");
		String filename = "data/" + dtf.format(date) + ".csv";
		File csv = new File(filename);
		
		FileWriter outputFile = new FileWriter(csv);
		CSVWriter writer = new CSVWriter(outputFile);
		
		String[] header = {"DateTime", "Epoch", "TripID", "RouteID", "DirectionID", "Latitude", "Longitude"};
		writer.writeNext(header);
				
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
				System.out.println(poller.returnedBuses.keySet());
				for (Map.Entry<String, BusLocation> entry: poller.returnedBuses.entrySet()) {
					BusLocation location = entry.getValue();
						
					String[] response = {location.getDateTime(), Long.toString(location.getTimestamp()), location.getTripID(), location.getRouteID(), location.getDirectionID(),
							Double.toString(location.getLatitude()), Double.toString(location.getLongitude())};					
					
					writer.writeNext(response);
					System.out.println("wrote response");
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
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		FeedPoller poller = new FeedPoller();
		poller.timedOperation(2, 31, poller);

	}
	
}


