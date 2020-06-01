package liveFeed;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.time.Instant;

import com.google.transit.realtime.GtfsRealtime;
import kong.unirest.Unirest;


public class FeedPoller {
	
	public final String username = "RTDgtfsRT";
	public final String password = "realT!m3Feed";
	public final String url = "http://www.rtd-denver.com/google_sync/VehiclePosition.pb";
	public final int secondBuffer = 31;
	
	//DO NOT CALL THIS FUNCTION MORE THAN ONCE EVERY THIRTY SECONDS
	
	private void getBusPositions() throws IOException
	{
		long previousCallSeconds = this.read();
		Instant instant = Instant.now();
		long currentCallSeconds = instant.getEpochSecond();
		
		if (currentCallSeconds - previousCallSeconds > this.secondBuffer) {
			

			Unirest.get(url).basicAuth(this.username, this.password).thenConsume(rawResponse -> {
								
				InputStream stream = rawResponse.getContent();
				try {
					GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(stream);
					
					for (GtfsRealtime.FeedEntity entity: feed.getEntityList()) {
						
						Location location = new Location();
						BusPosition busPosition = new BusPosition();
						GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();
						location.setLatitude(vehiclePosition.getPosition().getLatitude());
						location.setLongitude(vehiclePosition.getPosition().getLongitude());
						
						busPosition.setTrip_id(vehiclePosition.getTrip().getTripId());
						busPosition.setTimestamp(vehiclePosition.getTimestamp());
						
						System.out.println(busPosition.getTrip_id() + " w/ location " + location);
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
	
	public static void main(String[] args) throws IOException {
		
		FeedPoller poller = new FeedPoller();
		poller.getBusPositions();
	}
	
}
