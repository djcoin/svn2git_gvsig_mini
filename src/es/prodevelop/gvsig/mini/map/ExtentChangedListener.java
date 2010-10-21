package es.prodevelop.gvsig.mini.map;

import es.prodevelop.gvsig.mini.geom.Extent;

public interface ExtentChangedListener {
	
	public void onExtentChanged(Extent newExtent, int zoomLevel, double resolution);

}
