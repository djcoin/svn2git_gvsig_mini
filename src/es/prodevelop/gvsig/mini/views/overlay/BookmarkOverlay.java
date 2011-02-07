/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2011 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.quadtree.provide.BookmarkProviderListener;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstBookmarkProvider;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.osmpoi.OSMPOIContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.BookmarkSymbolSelector;
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
		refreshPOIs();
		refreshStreets();
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
		// super.onDraw(c, maps);
		final MapRenderer renderer = this.getTileRaster().getMRendererInfo();

		draw(pois, renderer, c);
		draw(streets, renderer, c);
	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		try {
			if (!isVisible())
				return null;

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
		if (pois != null && pois.size() > 0)
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

	@Override
	public ItemContext getItemContext() {
		try {
			Point p = null;
			if (isSelectedPOI)
				p = (Point) pois.get(getSelectedIndex());
			else
				p = (Point) streets.get(getSelectedIndex());
			return new OSMPOIContext(getTileRaster().map, true, false, p);
		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
			return null;
		}
	}

}
