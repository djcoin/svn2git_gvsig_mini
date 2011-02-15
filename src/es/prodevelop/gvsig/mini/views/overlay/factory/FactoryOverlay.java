package es.prodevelop.gvsig.mini.views.overlay.factory;

import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import es.prodevelop.gvsig.mini.views.overlay.ViewSimpleLocationOverlay;
import android.content.Context;


public class FactoryOverlay implements IFactoryOverlay {
	
	public LocationOverlay createLocationOverlay(Context ctx, TileRaster tileRaster, String name){
		return new ViewSimpleLocationOverlay(ctx, tileRaster, name);
	}
	
}
