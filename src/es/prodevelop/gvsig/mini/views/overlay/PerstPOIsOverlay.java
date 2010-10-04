package es.prodevelop.gvsig.mini.views.overlay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.TestConstants;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.POIContext;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.LayerChangedListener;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class PerstPOIsOverlay extends MapOverlay implements
		LayerChangedListener {

	PerstPOIsRunnable poiTask;
	ArrayList pois;
	PerstOsmPOIProvider poiProvider;
	Extent extent = new Extent();

	Pixel firstPOIPixel = null;
	Pixel lastPOIPixel = null;
	Pixel oldFirstPOIPixel = null;
	Pixel oldLastPOIPixel = null;

	protected ArrayList<Pixel> pixelsPOI = new ArrayList<Pixel>();
	
	Handler handler = new PerstPOIsHandler();
	
	private final static int ZOOM_THRESHOLD = 14;

	private int indexPOI = -1;

	public PerstPOIsOverlay(Context context, TileRaster tileRaster) {
		super(context, tileRaster);
		poiTask = new PerstPOIsRunnable((Map) context, handler);
		poiProvider = new PerstOsmPOIProvider("sdcard/gvSIG/pois/london"
				+ File.separator + TestConstants.PERST_SIMPLE_R_DATABASE);
		// try {
		// pois = (ArrayList) poiProvider.getPOIs(extent);
		// convertCoordinates("EPSG:4326", tileRaster.getMRendererInfo()
		// .getSRS());
		// } catch (BaseException e) {
		// // TODO Auto-generated catch block
		// Log.e("PerstPOIsOverlay", e.getMessage());
		// }
	}

	@Override
	public ItemContext getItemContext() {
		return new POIContext(getTileRaster().map);
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
//		final ViewPort vp = maps.map.vp;
//		final Extent extent = vp.calculateExtent(maps.mapWidth,
//				maps.mapHeight, maps.getMRendererInfo().getCenter());
//		if (pois != null) {
//			final int size = pois.size();
//
//			Point p;
//			for (int i = 0; i < size; i++) {
//				p = (Point) pois.get(i);
//				maps.geomDrawer.drawpoi(p, c, extent, vp);
//			}
//		}
		try {
			if (maps.getZoomLevel() < ZOOM_THRESHOLD)
				return;
			if (pois == null || pois.size() <= 0)
				return;
//			final ViewPort vp = maps.map.vp;
//			final Extent extent = vp.calculateExtent(maps.mapWidth,
//					maps.mapHeight, maps.getMRendererInfo().getCenter());

			final MapRenderer renderer = maps.getMRendererInfo();

			/**
			 * 
			 */
			if (TileRaster.CLEAR_OFF_POIS) {
				firstPOIPixel = null;
				lastPOIPixel = null;
				oldFirstPOIPixel = null;
				oldLastPOIPixel = null;
				TileRaster.CLEAR_OFF_POIS = false;
			}

			if (firstPOIPixel != null)
				oldFirstPOIPixel = firstPOIPixel;
			if (lastPOIPixel != null)
				oldLastPOIPixel = lastPOIPixel;

			int[] firstPixel = renderer
					.toPixels(new double[] { ((Point) pois.get(0)).getX(),
							((Point) pois.get(0)).getY() });
			firstPOIPixel = new Pixel(firstPixel[0], firstPixel[1]);

			int[] lastPixel = renderer.toPixels(new double[] {
					((Point) pois.get(pois.size() - 1)).getX(),
					((Point) pois.get(pois.size() - 1)).getY() });
			lastPOIPixel = new Pixel(lastPixel[0], lastPixel[1]);

			final int size = pixelsPOI.size();
			Pixel tempPixel = new Pixel(0, 0);
			double[] tempCoords = new double[] { 0, 0 };
			final int length = pois.size();

			if ((oldFirstPOIPixel != null)
					&& (oldFirstPOIPixel.getX() - firstPOIPixel.getX()) == (oldLastPOIPixel
							.getX() - lastPOIPixel.getX())
					&& (oldFirstPOIPixel.getY() - firstPOIPixel.getY()) == (oldLastPOIPixel
							.getY() - lastPOIPixel.getY())) {
				// Log.d("", "Move pixels");
				Pixel p;
				for (int i = 0; i < size; i++) {
					p = new Pixel(this.pixelsPOI.get(i).getX()
							- (oldFirstPOIPixel.getX() - firstPOIPixel.getX()),
							this.pixelsPOI.get(i).getY()
									- (oldFirstPOIPixel.getY() - firstPOIPixel
											.getY()));
					this.pixelsPOI.set(i, p);
					maps.geomDrawer.draw(new int[] { p.getX(), p.getY() }, c);
				}
			}

			else if ((oldLastPOIPixel == null)
					|| (oldLastPOIPixel.getX() != lastPOIPixel.getX())
					|| (oldLastPOIPixel.getY() != lastPOIPixel.getY())) {
				if (pixelsPOI != null)
					pixelsPOI.clear();
				Point p;
				// Log.d("", "Recalc pixels");
				for (int i = 0; i < length; i++) {
					p = ((Point) pois.get(i));
					tempCoords[0] = p.getX();
					tempCoords[1] = p.getY();
					int[] pix = renderer.toPixels(tempCoords);
					tempPixel = new Pixel(pix[0], pix[1]);
					// tempPixel.setX(pix[0]);
					// tempPixel.setY(pix[1]);
					pixelsPOI.add(tempPixel);
					maps.geomDrawer.draw(pix, c);
				}
			}

		} catch (Exception e) {
			Log.e("PerstPOIsOverlay", "onDraw: " + e.getMessage());
		}
			
			

	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub

	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		try {
			final ArrayList<Pixel> poisList = this.pixelsPOI;
			if (poisList == null)
				return null;
			long distance = Long.MAX_VALUE;
			int nearest = -1;

			final int size = pixelsPOI.size();
			Pixel pix;
			long newDistance;
			for (int i = 0; i < size; i++) {
				pix = poisList.get(i);
				newDistance = pix.distance(pixel);

				if (newDistance >= 0 && newDistance < distance) {
					distance = newDistance;
					nearest = i;
				}
			}

			if (distance > ResourceLoader.MAX_DISTANCE && distance >= 0)
				nearest = -1;

			if (nearest != -1) {
				indexPOI = nearest;
				POI p = (POI) pois.get(indexPOI);
				Log.d("", "found: " + p.toString());
				return new Feature(new Point(p.getX(), p.getY()));
			} else {
				indexPOI = -1;
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

			if (indexPOI == -1)
				return false;
			else {
				Message m = Message.obtain();
				m.what = Map.SHOW_TOAST;
				m.obj = ((POI) pois.get(indexPOI)).getDescription();
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
		poiProvider.getHelper().close();

		firstPOIPixel = null;
		lastPOIPixel = null;
		oldFirstPOIPixel = null;
		oldLastPOIPixel = null;

		int size = pixelsPOI.size();

		Pixel p;
		for (int i = 0; i < size; i++) {
			p = pixelsPOI.get(i);
			p.destroy();
			p = null;
			pixelsPOI.remove(i);
		}

		pixelsPOI = null;

		size = pois.size();

		POI poi;
		for (int i = 0; i < size; i++) {
			poi = (POI) pois.get(i);
			poi.destroy();
			poi = null;
			pois.remove(i);
		}

		pois = null;
	}

	@Override
	public void onLayerChanged(String layerName) {
		MapRenderer pRenderer = getTileRaster().getMRendererInfo();
		MapRenderer nRenderer;
		try {
			nRenderer = Layers.getInstance().getRenderer(layerName);
			this.convertCoordinates(pRenderer.getSRS(), nRenderer.getSRS());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void convertCoordinates(String srsFrom, String srsTo) {
		final int size = pois.size();
		Point p;
		for (int i = 0; i < size; i++) {
			p = (Point) pois.get(i);
			final double[] xy = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS(srsFrom), CRSFactory.getCRS(srsTo));
			p.setX(xy[0]);
			p.setY(xy[1]);
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event,
			final TileRaster mapView) {
		try {
			poiTask.cancel();
			poiTask = new PerstPOIsRunnable((Map) getContext(), handler);	
			poiTask.execute();
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
	
	private class PerstPOIsRunnable implements Runnable {
		
		Map context;
		private boolean isCanceled = false;
		ArrayList pois;
		Handler handler;
		
		public PerstPOIsRunnable(Map context, Handler handler) {
			this.context = context;
			this.handler = handler;
		}
		
		public void cancel() {
			isCanceled = true;
		}

		@Override
		public void run() {
			if (context.osmap.getZoomLevel() < ZOOM_THRESHOLD)
				return;
			final ViewPort vp = context.vp;
			final Extent extent = vp.calculateExtent(context.osmap.mapWidth,
					context.osmap.mapHeight, context.osmap.getMRendererInfo()
							.getCenter());
			if (extent.equals(PerstPOIsOverlay.this.extent))
				return;
			try {
				double[] minXY = ConversionCoords.reproject(extent.getMinX(),
						extent.getMinY(), CRSFactory.getCRS(context.osmap
								.getMRendererInfo().getSRS()), CRSFactory
								.getCRS("EPSG:4326"));
				double[] maxXY = ConversionCoords.reproject(extent.getMaxX(),
						extent.getMaxY(), CRSFactory.getCRS(context.osmap
								.getMRendererInfo().getSRS()), CRSFactory
								.getCRS("EPSG:4326"));
				pois = (ArrayList) PerstPOIsOverlay.this.poiProvider
						.getPOIs(new Extent(minXY[0], minXY[1], maxXY[0],
								maxXY[1]));				
				convertCoordinates("EPSG:4326", context.osmap
						.getMRendererInfo().getSRS());				
				
				if (!isCanceled) {
					PerstPOIsOverlay.this.pois = pois;
					PerstPOIsOverlay.this.extent = extent;
					TileRaster.CLEAR_OFF_POIS = true;
				}
			} catch (Exception e) {
				Log.e("", e.getMessage());
			} 
		}
		
		protected void convertCoordinates(String srsFrom, String srsTo) {
			final int size = pois.size();
			OsmPOI p;
			OsmPOI temp;
			for (int i = 0; i < size; i++) {
				if (isCanceled) return;
				p = (OsmPOI) pois.get(i);
				final double[] xy = ConversionCoords.reproject(p.getX(),
						p.getY(), CRSFactory.getCRS(srsFrom),
						CRSFactory.getCRS(srsTo));
				temp = (OsmPOI)p.clone();
				temp.setX(xy[0]);
				temp.setY(xy[1]);
				pois.set(i, temp);
			}
		}
		
		public void execute() {
			WorkQueue.getExclusiveInstance().execute(this);
		}
		
	}
	
	private class PerstPOIsHandler extends Handler {

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	}
}
