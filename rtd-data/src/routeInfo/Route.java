package routeInfo;


public class Route {

	private String route_long_name;
	private String route_type;
	private String route_text_color;
	private String route_color;
	private String agency_id;
	private String route_id;
	private String route_desc;
	
	
	public Route(String route_long_name, String route_type, String route_text_color, String route_color,
			String agency_id, String route_id, String route_desc) {
		super();
		this.route_long_name = route_long_name;
		this.route_type = route_type;
		this.route_text_color = route_text_color;
		this.route_color = route_color;
		this.agency_id = agency_id;
		this.route_id = route_id;
		this.route_desc = route_desc;
	}
	
	public String toString() {
		
		String s = this.route_long_name + " w/ id " + this.route_id;
		return s;
	}
	
	
}
