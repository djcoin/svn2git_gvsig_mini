package es.prodevelop.gvsig.mini._lg;

import android.os.Handler;
import es.prodevelop.gvsig.mini.activities.MapLocation;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import es.prodevelop.gvsig.mini.views.overlay.ViewSimpleLocationOverlay;

// TODO
// Warning: should differenciate the Context (that comes from MapLocation)
// From the Interface brought by IMap.
public abstract class IMap extends MapLocation {

	public TileRaster osmap = null;
	public ViewPort vp = null;
	public boolean navigation = false;
	public NamedMultiPoint nameds = null;
	
	
	public abstract void updateSlider();
	public abstract void persist();
	public abstract boolean isPOISlideShown();
	public abstract void updateZoomControl();
	public abstract ViewSimpleLocationOverlay getmMyLocationOverlay();
	public abstract ItemContext getItemContext();
	public abstract void showContext(ItemContext itemContext);
	public abstract void setOverlayContext(ItemContext itemContext);
	public abstract void showOverlayContext();
	public abstract void switchSlideBar();
	public abstract void setViewPort(ViewPort viewPort);
	public abstract Handler getMapHandler();
	public boolean isLocationHandlerEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	public void obtainCellLocation() {
		// TODO Auto-generated method stub
		
	}

}



//public interface IMap {
//
//	TileRaster osmap = null;
//	ViewPort vp = null;
//	boolean navigation = false;
//	NamedMultiPoint nameds = null;
//	
//	
//	void updateSlider();
//	void persist();
//	Object getText(int loadOffline);
//	boolean isPOISlideShown();
//	void updateZoomControl();
//	ViewSimpleLocationOverlay getmMyLocationOverlay();
//	ItemContext getItemContext();
//	void showContext(ItemContext itemContext);
//	void setOverlayContext(ItemContext itemContext);
//	void showOverlayContext();
//	void switchSlideBar();
//	void setViewPort(ViewPort viewPort);
//	
//	
//
//}
