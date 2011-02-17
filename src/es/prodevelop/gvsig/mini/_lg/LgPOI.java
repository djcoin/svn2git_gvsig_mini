package es.prodevelop.gvsig.mini._lg;

import es.prodevelop.android.spatialindex.poi.POI;

public class LgPOI extends POI {

	public static enum POIState {VISITED, NOTVISITED}
	private POIState state;
	
	public LgPOI(double x, double y, String description) {
		super(x, y, description);
		setState(POIState.NOTVISITED);
	}

	public void setState(POIState state) {
		this.state = state;
	}

	public POIState getState() {
		return state;
	}
	
	@Override
	public Object clone() {		
		 LgPOI lg = new LgPOI(this.getX(), this.getY(), this.getDescription());
		 lg.setState(this.getState());
		 return lg;
	}
	
}
