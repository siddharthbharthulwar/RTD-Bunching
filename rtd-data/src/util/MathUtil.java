package util;

import liveFeed.Location;

public class MathUtil {

	public static Double toRad(Double value) {
		return value * Math.PI / 180;
	}
	
	public static Double haversine(Location l1, Location l2) {
		
		final int R = 6371;
		Double latDistance = toRad(l1.getLatitude()-l2.getLatitude());
		Double lonDistance = toRad(l1.getLongitude()-l1.getLongitude());
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
		Math.cos(toRad(l1.getLatitude())) * Math.cos(toRad(l2.getLatitude())) * 
		Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = R * c;
		
		return distance;
	}
	
	public static void main(String[] args)
	{
		Location l1 = new Location();
		l1.setLatitude(0);
		l1.setLongitude(0);
		
		Location l2 = new Location();
		l2.setLatitude(1);
		l2.setLongitude(1);
		
		System.out.println(haversine(l1, l2));
	}
}
