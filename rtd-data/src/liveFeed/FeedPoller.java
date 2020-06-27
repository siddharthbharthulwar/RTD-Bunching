package liveFeed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.transit.realtime.GtfsRealtime;
import com.opencsv.CSVWriter;

import kong.unirest.Unirest;
import routeInfo.Trip;

public class FeedPoller extends Thread {
	
	    //Vehicle position data are stored in Google Protocol Buffer format, which is downloadable from server with credentials: 
	
		public final String username = "RTDgtfsRT";
		public final String password = "realT!m3Feed";
		public final String url = "http://www.rtd-denver.com/google_sync/VehiclePosition.pb";
		public final int secondBuffer = 30;
		
		private int iterations;
		private int seconds;
		
		
		public CSVWriter writer;
		//public RoutesReader routesReader;  THIS PROBABLY ISNT NEEDED RIGHT NOW 
		
		public HashMap<String, BusLocation> polls = new HashMap<String, BusLocation>();
		
		public FeedPoller(int iterations, int seconds) throws IOException {
			
			this.iterations = iterations;
			this.seconds = seconds;
			
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yy");
			String filename = "data/" + dtf.format(date) + ".csv";
			File csv = new File(filename);
			
			FileWriter outputFile = new FileWriter(csv);
			this.writer = new CSVWriter(outputFile);
			
			String[] header = {"DateTime", "Epoch", "TripID", "RouteID", "DirectionID", "Latitude", "Longitude"};
			writer.writeNext(header);
			
		}
		
		public static String pollDateTime() {
			
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmm");
			return dtf.format(date);
		}
		
		//DO NOT CALL THIS FUNCTION MORE THAN ONCE EVERY THIRTY SECONDS
		
		private void getBusPositions() throws IOException
		{
			this.polls.clear();
			
			long previousCallSeconds = this.read();
			Instant instant = Instant.now();
			long currentCallSeconds = instant.getEpochSecond();

			if (currentCallSeconds - previousCallSeconds >= this.secondBuffer) {

				Unirest.get(url).basicAuth(this.username, this.password).thenConsume(rawResponse -> {
									
					InputStream stream = rawResponse.getContent();
					try {
						GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(stream);
						
						int index = 0;
						
						for (GtfsRealtime.FeedEntity entity: feed.getEntityList()) {

							GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();

							String routeID = vehiclePosition.getTrip().getRouteId();
							
							if (index > 0 && !routeID.isEmpty()){
								
								BusLocation location = new BusLocation();
								location.setLatitude(vehiclePosition.getPosition().getLatitude());
								location.setLongitude(vehiclePosition.getPosition().getLongitude());
								location.setTimestamp(vehiclePosition.getTimestamp());
								location.setRouteID(routeID);
								location.setTripID(vehiclePosition.getTrip().getTripId());
								location.setDirectionID(Integer.toString(vehiclePosition.getTrip().getDirectionId()));
								location.setDateTime(pollDateTime());					
								String key = vehiclePosition.getTrip().getTripId();
								this.polls.put(key, location);
							}
							
							index++;

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

		

	public void run() {
		
		int index = 0;
		
		while(index < this.iterations) {
			
			try {
				this.getBusPositions();
				
				for (Map.Entry<String, BusLocation> entry: this.polls.entrySet()) {
					
					BusLocation location = entry.getValue();
					
					String[] response = {location.getDateTime(), Long.toString(location.getTimestamp()), location.getTripID(), location.getRouteID(), location.getDirectionID(),
							Double.toString(location.getLatitude()), Double.toString(location.getLongitude())};	
					System.out.println(Arrays.toString(response));					
					this.writer.writeNext(response);
				}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			try {
				System.out.println(" ..... thread is sleeping....");
				Thread.sleep(1000 * this.seconds);
				index++;
				System.out.println(" ..... thread is DONE sleeping....");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
	
    public static void main(String[] args) throws IOException {
		
		FeedPoller poller = new FeedPoller(720, 60);		
		poller.start();
	}
	

}
