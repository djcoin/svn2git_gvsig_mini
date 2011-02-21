package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini._lg.LgPOI;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.osmpoi.OSMPOIContext;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.symbol.BookmarkSymbolSelector;
import es.prodevelop.gvsig.mini.views.overlay.PointOverlay;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;


public class _PointOverlay extends PointOverlay {

	private ArrayList<LgPOI> pois;
	public final static String DEFAULT_NAME = "OWN_CLUSTER";

	public _PointOverlay(Context context, TileRaster tileRaster, String name, ILgPoiFactory factory) {
		super(context, tileRaster, name);
		setVisible(true);
		setSymbolSelector(new LgSymbolSelector());
		
		if (factory == null)
			System.out.println("FACTORY IS NULL!!!");
		ArrayList<LgPOI> pois = factory.makePoints();
		if (pois != null && pois.size() > 0)
			this.pois = (ArrayList<LgPOI>) pois;
		
		System.out.println("_PointOverlay: getTileRaster().getMRendererInfo().getSRS()" + getTileRaster().getMRendererInfo().getSRS());
		convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo().getSRS(), this.pois, null);
		this.setPoints(this.pois);
		
		// getTileRaster().resumeDraw();
	}

	@Override
	public ItemContext getItemContext() {
		return null;
	}	
	
	public LgPOI findPoiByName(String description){
		for (LgPOI p : this.pois) {
			if (p.getDescription() == description)
				return p;
		}
		return null;
	};
	
}
