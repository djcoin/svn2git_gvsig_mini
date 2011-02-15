package es.prodevelop.gvsig.mini.views.overlay.factory;

import android.content.Context;
import android.os.Bundle;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.geom.android.GPSPoint;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;

public abstract class LocationOverlay extends MapOverlay {
		
	public LocationOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		// TODO Auto-generated constructor stub
	}

	public final static int PORTRAIT_OFFSET_ORIENTATION = 0;
	public final static int LANDSCAPE_OFFSET_ORIENTATION = 90;
	public final static int NAVIGATION_MODE = 0;
	
	public static final double[] reprojectedCoordinates = null;
	public static final GPSPoint mLocation = null;
	
	public abstract void setLocation(GPSPoint locationToGeoPoint, float accuracy, String provider);

	public abstract void loadState(Bundle outState);

	public abstract void saveState(Bundle outState);

	public abstract void setOffsetOrientation(int portraitOffsetOrientation);

	public abstract Point getLocationLonLat();

	public abstract int getOffsetOrientation();
}
