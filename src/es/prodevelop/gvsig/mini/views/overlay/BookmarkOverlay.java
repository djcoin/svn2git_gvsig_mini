package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.provide.BookmarkProviderListener;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstBookmarkProvider;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.namefinder.Named;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.BookmarkSymbolSelector;
import es.prodevelop.gvsig.mini.symbol.SymbolSelector;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class BookmarkOverlay extends PointOverlay implements
		BookmarkProviderListener {

	private ArrayList pois;
	private ArrayList streets;

	public final static String DEFAULT_NAME = "POIS_BOOKMARKS";
	private PerstBookmarkProvider provider;

	private boolean isSelectedPOI = false;

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
		try {

			final ArrayList pois = this.pois;
			final ArrayList streets = this.streets;

			int nearestPOI = findNearestIndexToPixel(pixel, pois);
			int nearestStreet = findNearestIndexToPixel(pixel, streets);

			if (nearestPOI == -1 && nearestStreet == -1) {
				setSelectedIndex(-1);
				return null;
			} else {
				if (nearestPOI == -1) {
					setSelectedIndex(nearestStreet);
					Point selected = (Point) streets.get(nearestStreet);
					isSelectedPOI = false;
					return new Feature(selected);
				} else if (nearestStreet == -1) {
					setSelectedIndex(nearestPOI);
					Point selected = (Point) pois.get(nearestPOI);
					isSelectedPOI = true;
					return new Feature(selected);
				} else {
					Point nPOI = (Point) pois.get(nearestPOI);
					Point nStreet = (Point) streets.get(nearestStreet);

					int[] coords = getTileRaster().getMRendererInfo().toPixels(
							new double[] { nPOI.getX(), nPOI.getY() });

					Pixel pix = new Pixel(coords[0], coords[1]);
					long distancePOI = pix.distance(new Pixel(pixel.getX(),
							pixel.getY()));

					coords = getTileRaster().getMRendererInfo().toPixels(
							new double[] { nStreet.getX(), nStreet.getY() });

					pix = new Pixel(coords[0], coords[1]);
					long distanceStreet = pix.distance(new Pixel(pixel.getX(),
							pixel.getY()));

					if (distancePOI < distanceStreet) {
						setSelectedIndex(nearestPOI);
						isSelectedPOI = true;
						return new Feature(nPOI);
					} else {
						setSelectedIndex(nearestStreet);
						isSelectedPOI = false;
						return new Feature(nStreet);
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		try {
			super.onSingleTapUp(e, osmtile);

			if (getSelectedIndex() == -1) {
				return false;
			} else {
				Point p;
				if (isSelectedPOI) {
					p = (Point) pois.get(getSelectedIndex());
				} else {
					p = (Point) streets.get(getSelectedIndex());
				}
				updatePopup(p);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
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

	@Override
	public void onClusterExpanded(Collection pois, boolean clearPrevious,
			Cancellable cancellable, Cluster clusterExpanded) {
		// TODO Auto-generated method stub
		
	}

}
