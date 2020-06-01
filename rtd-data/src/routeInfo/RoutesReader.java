package routeInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RoutesReader {

	public List<String> route_long_name = new ArrayList<String>();
	public List<String> route_type = new ArrayList<String>();
	public List<String> route_text_color = new ArrayList<String>();
	public List<String> route_color = new ArrayList<String>();
	public List<String> agency_id = new ArrayList<String>();
	public List<String> route_id = new ArrayList<String>();
	public List<String> route_url = new ArrayList<String>();
	public List<String> route_desc = new ArrayList<String>();
	public List<String> route_short_name = new ArrayList<String>();
	public List<Route> routes = new ArrayList<Route>();
	
	public RoutesReader() throws IOException {
		
		this.readRoutes();
		this.parseRoutes();
	}
	
	public void readRoutes() throws IOException {
		
		FileReader reader = new FileReader("res/routes.txt");
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
	            		
	                	route_long_name.add(token);
	                	
	            	}
	            	else if (index == 1) {
	            		
	                	route_type.add(token);

	            	}
	            	else if (index == 2) {
	            		
	            		route_text_color.add(token);

	            	}
	            	else if (index == 3) {

	                	route_color.add(token);

	            	}
	            	else if (index == 4) {
	            		
	                	agency_id.add(token);

	            	}
	            	else if (index == 5) {
	            		
	                	route_id.add(token);

	            	}
	            	/*
	            	else if (index == 6) {
	            		
	                	route_url.add(token);

	            	}
	            	*/
	            	else if (index == 7) {
	            		
	                	route_desc.add(token);
	            		
	            	}
	            	else {
	            		
	                	route_short_name.add(token);	
	          
	            	}
	            	
	            	index++;
	            }
			}
			
			lineIndex++;
			
        }
		
		bufferedReader.close();
	}
	
	public void parseRoutes() {
		
		for (int i = 0; i < this.route_color.size(); i++) {
			
			/*
			System.out.println("long " + route_long_name.get(i));
			System.out.println("type " + route_type.get(i));
			System.out.println("text color " + route_text_color.get(i));
			System.out.println("color " + route_color.get(i));
			System.out.println("agency " + agency_id.get(i));
			System.out.println("id " + route_id.get(i));
			System.out.println("desc " + route_desc.get(i));
			*/
			
			Route r = new Route(this.route_long_name.get(i), this.route_type.get(i), this.route_text_color.get(i),
					this.route_color.get(i), this.agency_id.get(i), this.route_id.get(i),
					this.route_desc.get(i));
			
			this.routes.add(r);
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		RoutesReader routeReader = new RoutesReader();
		
		for (Route r: routeReader.routes) {
			
			System.out.println(r);
		}
		
	}
	
	public List<String> getRoute_long_name() {
		return route_long_name;
	}
	public void setRoute_long_name(List<String> route_long_name) {
		this.route_long_name = route_long_name;
	}
	public List<String> getRoute_type() {
		return route_type;
	}
	public void setRoute_type(List<String> route_type) {
		this.route_type = route_type;
	}
	public List<String> getRoute_text_color() {
		return route_text_color;
	}
	public void setRoute_text_color(List<String> route_text_color) {
		this.route_text_color = route_text_color;
	}
	public List<String> getRoute_color() {
		return route_color;
	}
	public void setRoute_color(List<String> route_color) {
		this.route_color = route_color;
	}
	public List<String> getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(List<String> agency_id) {
		this.agency_id = agency_id;
	}
	public List<String> getRoute_id() {
		return route_id;
	}
	public void setRoute_id(List<String> route_id) {
		this.route_id = route_id;
	}
	public List<String> getRoute_url() {
		return route_url;
	}
	public void setRoute_url(List<String> route_url) {
		this.route_url = route_url;
	}
	public List<String> getRoute_desc() {
		return route_desc;
	}
	public void setRoute_desc(List<String> route_desc) {
		this.route_desc = route_desc;
	}
	public List<String> getRoute_short_name() {
		return route_short_name;
	}
	public void setRoute_short_name(List<String> route_short_name) {
		this.route_short_name = route_short_name;
	}

}
