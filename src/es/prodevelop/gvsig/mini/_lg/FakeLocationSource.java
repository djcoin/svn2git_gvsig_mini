package es.prodevelop.gvsig.mini._lg;

import android.location.Location;

public class FakeLocationSource {

	// List of location
	private static Double[] location_ll = {
		-1.6236443817036, 47.211327779788, 
		-1.6230435668843, 47.211005282247, 
		-1.6225044428724, 47.210894138572, 
		-1.621715873422, 47.211147400049, 
		-1.6222912072556, 47.211939973117, 
		-1.6232863067993, 47.211338711877, 
		-1.6231763362297, 47.211254899163, 
		-1.6231521963485, 47.211355110002, 
		-1.6217225789444, 47.210863164074, 
		-1.6226090490239, 47.211727710462 
	}; 

	Location l;
	private int cpt;
	private String mockProvider;

	private final boolean loop;
	
	public FakeLocationSource(String mockProvider, boolean loop) {
		this.loop = loop;
		this.cpt = 0;
		this.mockProvider = mockProvider;
		this.l = new Location(mockProvider);
	}
	
	public Location getNextLocation() {
		if (cpt + 1 > location_ll.length){
			if (!loop)
				return null; // no more location
			cpt = 0;
		}
		
		l.setLongitude(location_ll[cpt]);
		l.setLatitude(location_ll[cpt+1]);		
		l.setAltitude(0);		
		cpt += 2;
		
		return l;
	}

}
