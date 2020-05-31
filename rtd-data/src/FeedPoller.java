import java.io.IOException;
import java.io.InputStream;

import com.google.transit.realtime.GtfsRealtime;
import kong.unirest.Unirest;


public class FeedPoller {
	
	public final String username = "RTDgtfsRT";
	public final String password = "realT!m3Feed";
	public final String url = "http://www.rtd-denver.com/google_sync/VehiclePosition.pb";
	
	
	private void getBusPositions()
	{
		
		Unirest.get(url).basicAuth(this.username, this.password).thenConsume(rawResponse -> {
			
			InputStream stream = rawResponse.getContent();
			try {
				GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(stream);
				
				for (GtfsRealtime.FeedEntity entity: feed.getEntityList()) {
					
					Location location = new Location();
					GtfsRealtime.VehiclePosition vehiclePosition = entity.getVehicle();
					location.setLatitude(vehiclePosition.getPosition().getLatitude());
					location.setLongitude(vehiclePosition.getPosition().getLongitude());
					
					System.out.println(location);
					
				}
			} 
			
			catch (IOException e) {
				e.printStackTrace();
			}
			
			
		});
	}
	
	public static void main(String[] args) {
		
		FeedPoller poller = new FeedPoller();
		
		poller.getBusPositions();
	}
	
}
