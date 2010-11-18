package es.prodevelop.gvsig.mini.yours;

public class RouteManager {

	private static RouteManager instance;
	private Route route;
	
	public final static String ROUTE_MODIFIED = "ROUTE MODIFIED";

	public static RouteManager getInstance() {
		if (instance == null) {
			instance = new RouteManager();
		}
		return instance;
	}

	public void registerRoute(Route r) {
		this.route = r;
	}

	public Route getRegisteredRoute() {
		return route;
	}
}
