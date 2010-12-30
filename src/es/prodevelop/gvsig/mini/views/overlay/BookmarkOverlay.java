package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.provide.BookmarkProviderListener;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstBookmarkProvider;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.BookmarkSymbolSelector;
import es.prodevelop.gvsig.mini.symbol.SymbolSelector;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class BookmarkOverlay extends PointOverlay implements
		BookmarkProviderListener {

	private ArrayList pois;
	private ArrayList streets;
	private BookmarkSymbolSelector selector;

	public final static String DEFAULT_NAME = "POIS_BOOKMARKS";
	private PerstBookmarkProvider provider;

	public BookmarkOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		setSymbolSelector(new BookmarkSymbolSelector());
	}

	public void refreshPOIs() {
		if (getProvider() != null)
			try {
				getProvider().getPOIsAsynch();
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				Log.e("", e.getMessage());
			}
	}

	public void refreshStreets() {
		if (getProvider() != null)
			try {
				getProvider().getStreetsAsynch();
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				Log.e("", e.getMessage());
			}
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		super.onDraw(c, maps);
		final MapRenderer renderer = this.getTileRaster().getMRendererInfo();

		if (pois == null && streets == null) {
			refreshPOIs();
			refreshStreets();
		}

		draw(pois, renderer, c);
		draw(streets, renderer, c);
	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		try {
			// this.getProvider().getHelper().closeDatabase();
			super.destroy();
			pois = null;
			streets = null;
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	public PerstBookmarkProvider getProvider() {
		if (provider == null) {
			provider = POIProviderManager.getInstance().getBookmarkProvider();
			provider.setQuadtreeProviderListener(this);
		}

		return provider;
	}

	@Override
	public void onLayerChanged(String layerName) {
		try {
			super.onLayerChanged(layerName);
			refreshPOIs();
			refreshStreets();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	@Override
	public void onClustersRetrieved(Collection clusters, String category,
			boolean clearPrevious, Cancellable cancellable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPOISRetrieved(Collection pois, boolean clearPrevious,
			Cancellable cancellable) {
		this.pois = (ArrayList) pois;
		convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo()
				.getSRS(), this.pois, null);
		getTileRaster().resumeDraw();

	}

	@Override
	public void onStreetsRetrieved(Collection pois, boolean clearPrevious,
			Cancellable cancellable) {
		this.streets = (ArrayList) pois;
		convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo()
				.getSRS(), this.streets, null);
		getTileRaster().resumeDraw();

	}

	@Override
	public void onPOIAdded(OsmPOI poi) {
		onLayerChanged("");
	}

	@Override
	public void onStreetAdded(OsmPOIStreet street) {
		onLayerChanged("");
	}

	@Override
	public void onPOIRemoved(OsmPOI poi) {
		onLayerChanged("");
	}

	@Override
	public void onStreetRemoved(OsmPOIStreet street) {
		onLayerChanged("");
	}

}
