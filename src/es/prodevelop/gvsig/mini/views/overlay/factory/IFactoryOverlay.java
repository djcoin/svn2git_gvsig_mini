package es.prodevelop.gvsig.mini.views.overlay.factory;

import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import android.content.Context;


// TODO: create stub overlays
public interface IFactoryOverlay {

	public LocationOverlay createLocationOverlay(Context ctx, TileRaster tileRaster, String name);
	
}
