package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.osmpoi.OSMPOIContext;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.symbol.BookmarkSymbolSelector;
import es.prodevelop.gvsig.mini.symbol.LgSymbolSelector;




public class _PointOverlay extends PointOverlay {

	private ArrayList pois;
	public final static String DEFAULT_NAME = "OWN_CLUSTER";

	public _PointOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		setSymbolSelector(new LgSymbolSelector());
		setVisible(true);
		// TODO Auto-generated constructor stub*
		
		ArrayList<Point> pois = makePoints();
		if (pois != null && pois.size() > 0)
			this.pois = (ArrayList) pois;
		// convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo().getSRS(), pois, null);
		System.out.println("_PointOverlay: getTileRaster().getMRendererInfo().getSRS()" + getTileRaster().getMRendererInfo().getSRS());
		// convertCoordinates("EPSG:4326", "EPSG:900913", pois, null);
		convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo().getSRS(), pois, null);
		this.setPoints(pois);
		
		getTileRaster().resumeDraw();
	}

	@Override
	public ItemContext getItemContext() {
		try {
			return new OSMPOIContext(getTileRaster().map, false, false,
					(POI) getPoints().get(getSelectedIndex()));
		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
			return null;
		}
	}
	
//	E/        (  545): es.prodevelop.gvsig.mini.geom.Point
//	I/System.out(  545): Center is: -1.623176855593768/47.2111290117751
//	I/System.out(  545): Center is (reprojected!): -1.4581245782089471E-5/4.236771973984895E-4
//	I/System.out(  545): Center is (reprojected!): -1.4581245782089471E-5/4.236771973984895E-4

	private ArrayList<Point> makePoints(){
		// epsg:4326 points taken from the BO
		List<Point> l = new ArrayList<Point>();
//		Point[] p = {new Point(-1.6236443817036, 47.211327779788), 
//					 new Point(-1.6230435668843, 47.211005282247), 
//					 new Point(-1.6225044428724, 47.210894138572), 
//					 new Point(-1.621715873422, 47.211147400049), 
//					 new Point(-1.6222912072556, 47.211939973117), 
//					 new Point(-1.6232863067993, 47.211338711877), 
//					 new Point(-1.6231763362297, 47.211254899163), 
//					 new Point(-1.6231521963485, 47.211355110002), 
//					 new Point(-1.6217225789444, 47.210863164074), 
//					 new Point(-1.6226090490239, 47.211727710462)}; 
		
		Point[] p = {new OsmPOI(-1.6236443817036, 47.211327779788, "Point1"), 
				 new OsmPOI(-1.6230435668843, 47.211005282247, "Point2"), 
				 new OsmPOI(-1.6225044428724, 47.210894138572, "Point3"), 
				 new OsmPOI(-1.621715873422, 47.211147400049, "Point4"), 
				 new OsmPOI(-1.6222912072556, 47.211939973117, "Point5"), 
				 new OsmPOI(-1.6232863067993, 47.211338711877, "Point6"), 
				 new OsmPOI(-1.6231763362297, 47.211254899163, "Point7"), 
				 new OsmPOI(-1.6231521963485, 47.211355110002, "Point8"), 
				 new OsmPOI(-1.6217225789444, 47.210863164074, "Point9"), 
				 new OsmPOI(-1.6226090490239, 47.211727710462, "Point10")};
		
		// POICategories.TRANSPORTATION
		// OsmPOI poi = new OsmPOI(x, y, name)
		
		for (int i = 0; i < p.length; i++) {			
			l.add(p[i]);
		}
		return (ArrayList<Point>) l;
	}
}
