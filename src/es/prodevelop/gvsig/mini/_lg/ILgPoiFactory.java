package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;

public interface ILgPoiFactory {

	ArrayList<LgPOI> makePoints();

	public static class StaticFactory implements ILgPoiFactory {

		public ArrayList<LgPOI> makePoints(){
			// epsg:4326 points taken from the BO
			ArrayList<LgPOI> l = new ArrayList<LgPOI>();

			// TODO: load from a special stuff.
			LgPOI[] p = {new LgPOI(-1.6236443817036, 47.211327779788, "Point1"), 
					 new LgPOI(-1.6230435668843, 47.211005282247, "Point2"), 
					 new LgPOI(-1.6225044428724, 47.210894138572, "Point3"), 
					 new LgPOI(-1.621715873422, 47.211147400049, "Point4"), 
					 new LgPOI(-1.6222912072556, 47.211939973117, "Point5"), 
					 new LgPOI(-1.6232863067993, 47.211338711877, "Point6"), 
					 new LgPOI(-1.6231763362297, 47.211254899163, "Point7"), 
					 new LgPOI(-1.6231521963485, 47.211355110002, "Point8"), 
					 new LgPOI(-1.6217225789444, 47.210863164074, "Point9"), 
					 new LgPOI(-1.6226090490239, 47.211727710462, "Point10")};
			
			// HashMap<String, OsmPOI> hm = new HashMap<String, OsmPOI>();
			for (int i = 0; i < p.length; i++) {			
				l.add(p[i]);
			}
			return l;
		}
		
	}
}
