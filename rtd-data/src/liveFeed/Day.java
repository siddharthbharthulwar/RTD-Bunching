package liveFeed;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Day {

	public final LocalDateTime date = LocalDateTime.now();
	public FeedPoller feedPoller;
	
	public Day() throws IOException {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
		System.out.println("Bus Data Collection for " + dtf.format(this.date) + " initialized");
		this.feedPoller = new FeedPoller();
	}
	
	public static void main(String[] args) throws IOException {
		
		Day d = new Day();
	}
	
}
