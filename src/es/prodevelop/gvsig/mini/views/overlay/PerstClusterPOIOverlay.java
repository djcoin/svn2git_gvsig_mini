/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.quadtree.memory.cluster.ClusterNode;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProviderListener;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.POIContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.ExtentChangedListener;
import es.prodevelop.gvsig.mini.map.LayerChangedListener;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.symbol.ClusterSymbolSelector;
import es.prodevelop.gvsig.mini.symbol.ResultSearchSymbolSelector;
import es.prodevelop.gvsig.mini.symbol.SymbolSelector;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class PerstClusterPOIOverlay extends MapOverlay implements
		LayerChangedListener, QuadtreeProviderListener, ExtentChangedListener {

	ArrayList clusters = new ArrayList();
	ArrayList pois = new ArrayList();
	PerstOsmPOIClusterProvider poiProvider;

	Pixel firstPOIPixel = null;
	Pixel lastPOIPixel = null;
	Pixel oldFirstPOIPixel = null;
	Pixel oldLastPOIPixel = null;

	protected ArrayList<Pixel> pixelsPOI = new ArrayList<Pixel>();

	private int indexCluster = -1;
	private int indexPOI = -1;

	private SymbolSelector clusterSymbolSelector;
	private SymbolSelector poiSymbolSelector;

	// Bitmap bufferBitmap;
	// Canvas bufferCanvas.setBitmap(bufferBitmap);

	public PerstClusterPOIOverlay(Context context, TileRaster tileRaster) {
		super(context, tileRaster);
		try {
			poiProvider = new PerstOsmPOIClusterProvider(
					Environment.getExternalStorageDirectory() + File.separator
							+ "gvSIG/pois/london" + File.separator
							+ "perst_streets_cluster_cat.db", tileRaster
							.getMRendererInfo().getZOOM_MAXLEVEL()
							- tileRaster.getMRendererInfo().getZoomMinLevel(),
					this, tileRaster.getMRendererInfo().getZOOM_MAXLEVEL());
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			Log.e("", e.getMessage());
		} finally {
			clusterSymbolSelector = new ClusterSymbolSelector(this);
			poiSymbolSelector = new ResultSearchSymbolSelector(this);

			// TRANSPORTATION_POI = ResourceLoader
			// .getBitmap(R.drawable.p_transportation_transport_bus_stop_16_poi);
			// TOURISM_POI = ResourceLoader
			// .getBitmap(R.drawable.p_tourism_tourist_attraction_16_poi);
			// RECREATION_POI = ResourceLoader
			// .getBitmap(R.drawable.p_recreation_sport_playground_16_poi);
			// FOOD_POI = ResourceLoader
			// .getBitmap(R.drawable.p_food_restaurant_16_poi);
			// PUBLIC_BUILDINGS_POI = ResourceLoader
			// .getBitmap(R.drawable.p_public_buildings_tourist_monument_16_poi);
			// ARTS_CULTURE_POI = ResourceLoader
			// .getBitmap(R.drawable.p_arts_culture_tourist_theatre_16_poi);
			// SHOPS_POI = ResourceLoader
			// .getBitmap(R.drawable.p_shops_shopping_supermarket_16_poi);
			// HEALTH_EMERGENCY_POI = ResourceLoader
			// .getBitmap(R.drawable.p_health_hospital_16_poi);
			// ACCOMODATION_POI = ResourceLoader
			// .getBitmap(R.drawable.p_accommodation_hotel_16_poi);
			// ROUTE_POI = ResourceLoader
			// .getBitmap(R.drawable.p_route_tourist_castle2_16_poi);
			// PLACES_POI = ResourceLoader
			// .getBitmap(R.drawable.p_places_poi_place_city_16_poi);
		}
	}

	@Override
	public ItemContext getItemContext() {
		return new POIContext(getTileRaster().map);
	}

	public void drawCluster(Cluster p, Object graphics, Extent extent) {
		try {
			// if (getTileRaster().geomDrawer.mustDraw(extent, p)) {
			Canvas c = (Canvas) graphics;

			final MapRenderer renderer = this.getTileRaster()
					.getMRendererInfo();
			int[] coords = null;

			coords = renderer.toPixels(new double[] { p.getX(), p.getY() });

			Bitmap icon = clusterSymbolSelector.getSymbol(p);
			int[] midIcon = clusterSymbolSelector.getMidSymbol();

			if (icon != null)
				c.drawBitmap(icon, coords[0] - midIcon[0], coords[1]
						- midIcon[1], Paints.mPaintR);
			else
				getTileRaster().geomDrawer.drawpoi(p, graphics, extent, null);

			// float w = Paints.poiTextPaint.measureText(num, 0,
			// num.length());
			// Rect bounds = new Rect();
			// Paints.poiTextPaint.getTextBounds(num, 0, num.length(),
			// bounds);
			// RectF rect = new RectF();
			// rect.bottom = coords[1] + (bounds.bottom - bounds.top) - 1 -
			// midIcon[1];
			// rect.top = coords[1] - midIcon[1] - 1;
			// rect.left = coords[0] + midIcon[0] + 1;
			// rect.right = coords[0] + midIcon[0]
			// + (bounds.right - bounds.left) + 1;
			//
			// c.drawRoundRect(rect, 2, 2, Paints.poiFillTextPaint);
			//
			// rect.bottom +=1;
			// rect.top -=1;
			// rect.left -= 1;
			// rect.right += 1;
			//
			// c.drawRoundRect(rect, 2, 2, Paints.poiBorderTextPaint);

			String num;
			if (indexCluster != -1) {
				num = String.valueOf(p.getNumItems());
				c.drawText(num, coords[0] + midIcon[0] + 1, coords[1] + 1,
						Paints.poiTextWhitePaint);
				c.drawText(num, coords[0] + midIcon[0], coords[1],
						Paints.poiTextPaint);

			}

			// }
		} catch (Exception e) {

		}
	}

	public void drawOsmPOI(OsmPOI p, Object graphics, Extent extent) {
		try {
			if (getTileRaster().geomDrawer.mustDraw(extent, p)) {
				Canvas c = (Canvas) graphics;
				final MapRenderer renderer = this.getTileRaster()
						.getMRendererInfo();
				int[] coords = null;

				coords = renderer.toPixels(new double[] { p.getX(), p.getY() });

				Bitmap icon = poiSymbolSelector.getSymbol(p);
				int[] midIcon = poiSymbolSelector.getMidSymbol();

				if (icon != null)
					c.drawBitmap(icon, coords[0] - midIcon[0], coords[1]
							- midIcon[1], Paints.mPaintR);
				else
					getTileRaster().geomDrawer.drawpoi(p, graphics, extent,
							null);
			}
		} catch (Exception e) {

		}
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		try {
			// if (bufferBitmap == null) {
			// bufferBitmap = Bitmap.createBitmap(TileRaster.mapWidth,
			// TileRaster.mapHeight,
			// Bitmap.Config.RGB_565);
			// }
			final ViewPort vp = maps.map.vp;
			// final Extent extent = ViewPort.calculateExtent(maps
			// .getMRendererInfo().getLatLonCenter(),
			// ViewPort.LAT_LON_RES[maps.getZoomLevel()],
			// ViewPort.mapWidth, ViewPort.mapHeight);

			final Extent extent = vp.calculateExtent(maps.mapWidth,
					maps.mapHeight, maps.getMRendererInfo().getCenter());

			if (clusters != null) {
				final int size = clusters.size();

				Point p;
				for (int i = 0; i < size; i++) {
					// if (isCanceledDraw())
					// return;
					p = (Point) clusters.get(i);
					drawCluster((Cluster) p, c, extent);
				}
			}

			if (pois != null) {
				final int size = pois.size();

				Point p;
				for (int i = 0; i < size; i++) {
					// if (isCanceledDraw())
					// return;
					p = (Point) pois.get(i);
					drawOsmPOI((OsmPOI) p, c, extent);
				}
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}

		// try {
		// if (maps.getZoomLevel() < ZOOM_THRESHOLD)
		// return;
		// if (pois == null || pois.size() <= 0)
		// return;
		// // final ViewPort vp = maps.map.vp;
		// // final Extent extent = vp.calculateExtent(maps.mapWidth,
		// // maps.mapHeight, maps.getMRendererInfo().getCenter());
		//
		// final MapRenderer renderer = maps.getMRendererInfo();
		//
		// /**
		// *
		// */
		// if (TileRaster.CLEAR_OFF_POIS) {
		// firstPOIPixel = null;
		// lastPOIPixel = null;
		// oldFirstPOIPixel = null;
		// oldLastPOIPixel = null;
		// TileRaster.CLEAR_OFF_POIS = false;
		// }
		//
		// if (firstPOIPixel != null)
		// oldFirstPOIPixel = firstPOIPixel;
		// if (lastPOIPixel != null)
		// oldLastPOIPixel = lastPOIPixel;
		//
		// int[] firstPixel = renderer
		// .toPixels(new double[] { ((Point) pois.get(0)).getX(),
		// ((Point) pois.get(0)).getY() });
		// firstPOIPixel = new Pixel(firstPixel[0], firstPixel[1]);
		//
		// int[] lastPixel = renderer.toPixels(new double[] {
		// ((Point) pois.get(pois.size() - 1)).getX(),
		// ((Point) pois.get(pois.size() - 1)).getY() });
		// lastPOIPixel = new Pixel(lastPixel[0], lastPixel[1]);
		//
		// final int size = pixelsPOI.size();
		// Pixel tempPixel = new Pixel(0, 0);
		// double[] tempCoords = new double[] { 0, 0 };
		// final int length = pois.size();
		//
		// if ((oldFirstPOIPixel != null)
		// && (oldFirstPOIPixel.getX() - firstPOIPixel.getX()) ==
		// (oldLastPOIPixel
		// .getX() - lastPOIPixel.getX())
		// && (oldFirstPOIPixel.getY() - firstPOIPixel.getY()) ==
		// (oldLastPOIPixel
		// .getY() - lastPOIPixel.getY())) {
		// // Log.d("", "Move pixels");
		// Pixel p;
		// for (int i = 0; i < size; i++) {
		// p = new Pixel(this.pixelsPOI.get(i).getX()
		// - (oldFirstPOIPixel.getX() - firstPOIPixel.getX()),
		// this.pixelsPOI.get(i).getY()
		// - (oldFirstPOIPixel.getY() - firstPOIPixel
		// .getY()));
		// this.pixelsPOI.set(i, p);
		// maps.geomDrawer.draw(new int[] { p.getX(), p.getY() }, c);
		// }
		// }
		//
		// else if ((oldLastPOIPixel == null)
		// || (oldLastPOIPixel.getX() != lastPOIPixel.getX())
		// || (oldLastPOIPixel.getY() != lastPOIPixel.getY())) {
		// if (pixelsPOI != null)
		// pixelsPOI.clear();
		// Point p;
		// // Log.d("", "Recalc pixels");
		// for (int i = 0; i < length; i++) {
		// p = ((Point) pois.get(i));
		// tempCoords[0] = p.getX();
		// tempCoords[1] = p.getY();
		// int[] pix = renderer.toPixels(tempCoords);
		// tempPixel = new Pixel(pix[0], pix[1]);
		// // tempPixel.setX(pix[0]);
		// // tempPixel.setY(pix[1]);
		// pixelsPOI.add(tempPixel);
		// maps.geomDrawer.draw(pix, c);
		// }
		// }
		//
		// } catch (Exception e) {
		// Log.e("PerstPOIsOverlay", "onDraw: " + e.getMessage());
		// }

	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub

	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		try {
			boolean found = false;
			final ArrayList<Point> points = (ArrayList<Point>) clusters;
			if (clusters == null && pois == null)
				return null;
			long distance = Long.MAX_VALUE;
			int nearest = -1;

			Point p;
			Pixel pix;
			final int size = points.size();
			for (int i = 0; i < size; i++) {
				p = points.get(i);

				int[] coords = getTileRaster().getMRendererInfo().toPixels(
						new double[] { p.getX(), p.getY() });

				pix = new Pixel(coords[0], coords[1]);
				long newDistance = pix.distance(new Pixel(pixel.getX(), pixel
						.getY()));

				if (newDistance >= 0 && newDistance < distance) {
					distance = newDistance;
					nearest = i;
				}
			}

			if (distance > ResourceLoader.MAX_DISTANCE && distance >= 0)
				nearest = -1;

			if (nearest != -1) {
				indexCluster = nearest;
				Cluster pc = (Cluster) clusters.get(indexCluster);
				Log.d("", "found: " + pc.toString());
				return new Feature(new Point(pc.getX(), pc.getY()));
			} else {
				indexCluster = -1;
				return null;
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
			return null;
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		try {
			super.onSingleTapUp(e, osmtile);

			if (indexCluster == -1)
				return false;
			else {
				Message m = Message.obtain();
				m.what = Map.SHOW_TOAST;
				m.obj = ((ClusterNode) clusters.get(indexCluster))
						.getNumItems()
				// + " - "
				// + ((OsmPOI) pois.get(indexPOI)).getSubcategory() + "\n"
				// + ((POI) pois.get(indexPOI)).getDescription()
				;
				getTileRaster().map.getMapHandler().sendMessage(m);
			}
			return true;
		} catch (Exception ex) {
			Log.d("", ex.getMessage());
			return false;
		}
	}

	@Override
	public void destroy() {
		// poiProvider.getHelper().close();
		//
		// firstPOIPixel = null;
		// lastPOIPixel = null;
		// oldFirstPOIPixel = null;
		// oldLastPOIPixel = null;
		//
		// int size = pixelsPOI.size();
		//
		// Pixel p;
		// for (int i = 0; i < size; i++) {
		// p = pixelsPOI.get(i);
		// p.destroy();
		// p = null;
		// pixelsPOI.remove(i);
		// }
		//
		// pixelsPOI = null;
		//
		// size = pois.size();
		//
		// POI poi;
		// for (int i = 0; i < size; i++) {
		// poi = (POI) pois.get(i);
		// poi.destroy();
		// poi = null;
		// pois.remove(i);
		// }
		//
		// pois = null;
	}

	@Override
	public void onLayerChanged(String layerName) {
		MapRenderer pRenderer = getTileRaster().getMRendererInfo();
		MapRenderer nRenderer;
		try {

			nRenderer = Layers.getInstance().getRenderer(layerName);
			this.convertCoordinates(pRenderer.getSRS(), nRenderer.getSRS(),
					clusters, null);
			this.convertCoordinates(pRenderer.getSRS(), nRenderer.getSRS(),
					pois, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void convertCoordinates(final String srsFrom, final String srsTo,
			final ArrayList pois, final Cancellable cancellable) {		
		if (pois == null)
			return;
		final int size = pois.size();
		Point p;
		Point temp;
		for (int i = 0; i < size; i++) {
			// if (cancellable != null && cancellable.getCanceled())
			// return;
			p = (Point) pois.get(i);
			final double[] xy = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS(srsFrom), CRSFactory.getCRS(srsTo));
			temp = (Point) p.clone();
			temp.setX(xy[0]);
			temp.setY(xy[1]);
			pois.set(i, temp);			
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event,
			final TileRaster mapView) {
		try {

		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
		return true;
	}

	@Override
	public boolean onTrackballEvent(final MotionEvent event,
			final TileRaster mapView) {
		return this.onTouchEvent(event, mapView);
	}

	private boolean cancelDraw = false;

	public synchronized void restoreDraw() {
		cancelDraw = false;
	}

	public synchronized void cancelDraw() {
		cancelDraw = true;
	}

	public synchronized boolean isCanceledDraw() {
		return cancelDraw;
	}

	@Override
	public void onClustersRetrieved(Collection pois, String category,
			boolean clearPrevious, final Cancellable cancellable) {
		// this.cancelDraw();
		if (clearPrevious)
			// if (cancellable == null)
			// this.clusters = (ArrayList) pois;
			// else if (!cancellable.getCanceled())
			this.clusters = (ArrayList) pois;
		else {
			long t1 = android.os.SystemClock.currentThreadTimeMillis();
			convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo()
					.getSRS(), (ArrayList) pois, cancellable);
			Log.d("",
					"Time to convert coordinates: "
							+ (android.os.SystemClock.currentThreadTimeMillis() - t1));
			this.clusters.addAll(pois);
		}

		// this.restoreDraw();
	}

	@Override
	public void onPOISRetrieved(Collection pois, boolean clearPrevious,
			final Cancellable cancellable) {
		// this.cancelDraw();
		if (clearPrevious)
			// if (cancellable == null)
			// this.pois = (ArrayList) pois;
			// else if (!cancellable.getCanceled())
			this.pois = (ArrayList) pois;
		else {

			convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo()
					.getSRS(), (ArrayList) pois, cancellable);

			this.pois.addAll(pois);
		}

		// this.restoreDraw();
	}

	/**
	 * @return the poiProvider
	 */
	public PerstOsmPOIClusterProvider getPoiProvider() {
		return poiProvider;
	}

	@Override
	public void onExtentChanged(Extent newExtent, int zoomLevel,
			double resolution) {
		final ViewPort vp = getTileRaster().map.vp;

		final MapRenderer renderer = getTileRaster().getMRendererInfo();

		double[] minXY = ConversionCoords.reproject(newExtent.getMinX(),
				newExtent.getMinY(), CRSFactory.getCRS(renderer.getSRS()),
				CRSFactory.getCRS("EPSG:4326"));
		double[] maxXY = ConversionCoords.reproject(newExtent.getMaxX(),
				newExtent.getMaxY(), CRSFactory.getCRS(renderer.getSRS()),
				CRSFactory.getCRS("EPSG:4326"));

		final Extent extentGetPOIS = new Extent(minXY[0], minXY[1], maxXY[0],
				maxXY[1]);

		try {
			poiProvider.getPOIsAsynch(extentGetPOIS,
					ViewPort.LAT_LON_RES[zoomLevel] * 64, zoomLevel,
					Utilities.getNewCancellable());
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCategories(ArrayList categories) throws BaseException {
		if (categories == null)
			return;

		poiProvider.setSelectedCategories(categories);
	}

	public Hashtable<String, Integer> getMaxClusterSizeCat() {
		return ((PerstOsmPOIClusterProvider) this.poiProvider)
				.getMaxClusterSizeCat();
	}
}
