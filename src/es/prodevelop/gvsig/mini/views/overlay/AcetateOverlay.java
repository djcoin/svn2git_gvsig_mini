/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2009 Prodevelop.
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
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.IGeometry;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.search.activities.POIDetailsActivity;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.Calculator;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

/**
 * A MapOverlay to draw transient geometries. For example the rectangle zoom
 * envelope. It also manages touchevents
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class AcetateOverlay extends MapOverlay {

	int touchCounter = 0;
	final static int TOUCH_COUNTER = 20;
	public final static String DEFAULT_NAME = "ACETATE";
	private int lastZoomLevel = -1;

	private PopupView popupView;

	public AcetateOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		try {
			CompatManager.getInstance().getRegisteredLogHandler()
					.configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			popupView = new PopupView(context);
			path = new Path();
			t = getTileRaster();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	private final static Logger log = Logger.getLogger(AcetateOverlay.class
			.getName());

	private int mTouchDownX;
	private int mTouchDownY;
	public static int mTouchMapOffsetX;
	public static int mTouchMapOffsetY;
	private int fromX = -1;
	private int fromY = -1;
	private int toX = -1;
	private int toY = -1;
	TileRaster t;
	Path path;

	int popupOffsetX;
	int popupOffsetY;

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		try {
			if (lastZoomLevel == -1 || lastZoomLevel == maps.getZoomLevel()) {

				final Feature feature = maps.selectedFeature;

				if (feature == null)
					return;

				IGeometry f = feature.getGeometry();

				if (f != null && f instanceof Point) {
					Point p = (Point) f;
					int[] xy = maps.getMRendererInfo().toPixels(
							new double[] { p.getX(), p.getY() });
					popupView
							.setPos(xy[0] + popupOffsetX, xy[1] - popupOffsetY);
				}

				popupView.dispatchDraw(c);

			} else {
				this.setPopupVisibility(View.INVISIBLE);
			}

			// FIXME
			// if (drawZoomRectangle && rectangle != null) {
			// if (rectangle.width() <= 0 || rectangle.height() <= 0) {
			// rectangle.left -= 1;
			// rectangle.right += 1;
			// rectangle.top -= 1;
			// rectangle.bottom += 1;
			// }
			// c.drawRect(rectangle, Paints.rectanglePaint);
			// }
			// if (toX >= 0 && toY >= 0 && fromX >= 0 && fromY >= 0
			// && !maps.panMode) {
			// path.rewind();
			// path.moveTo(fromX, fromY);
			// path.lineTo(fromX, toY);
			// path.lineTo(toX, toY);
			// path.lineTo(toX, fromY);
			// path.lineTo(fromX, fromY);
			// c.drawPath(path, Paints.filledPaint);
			// c.drawPath(path, Paints.rectanglePaint);
			// }
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub

	}

	private void updateRectangle() {
		int toX = mTouchMapOffsetX + mTouchDownX;
		int toY = mTouchDownY + mTouchMapOffsetY;

		if (this.mTouchDownX < toX) {
			fromX = this.mTouchDownX;
			this.toX = toX;
		} else {
			fromX = toX;
			this.toX = this.mTouchDownX;
		}

		if (this.mTouchDownY < toY) {
			fromY = this.mTouchDownY;
			this.toY = toY;
		} else {
			fromY = toY;
			this.toY = this.mTouchDownY;
		}
	}

	/**
	 * Retrieves the nerest feature from the pressed pixel and pass them to
	 * TileRaster
	 * 
	 * @param e
	 * @param osmtile
	 * @return
	 */
	public boolean onLongPress(MotionEvent e, TileRaster osmtile) {
		try {
			if (onSingleTapUp(e, osmtile))
				return true;
			Pixel pixel = new Pixel((int) e.getX(), (int) e.getY());
			Feature f = getNearestFeature(pixel);
			if (f != null) {
				this.getTileRaster().setSelectedFeature(f);
				return true;
			} else {
				// getTileRaster().map.getPopup().setVisibility(View.INVISIBLE);
				this.getTileRaster().setSelectedFeature(null);
				return false;
			}
		} catch (Exception ex) {
			return false;
		}

	}

	public boolean onTouchEvent(final MotionEvent event) {
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (t.panMode) {
					t.mTouchDownX = (int) event.getX();
					t.mTouchDownY = (int) event.getY();
				} else {
					this.mTouchDownX = (int) event.getX();
					this.mTouchDownY = (int) event.getY();
				}

				t.invalidate();
				return true;
			case MotionEvent.ACTION_MOVE:
				if (t.panMode) {
					t.mTouchMapOffsetX = (int) event.getX() - t.mTouchDownX;
					t.mTouchMapOffsetY = (int) event.getY() - t.mTouchDownY;
					ViewPort.mTouchMapOffsetX = (int) event.getX()
							- t.mTouchDownX;
					ViewPort.mTouchMapOffsetY = (int) event.getY()
							- t.mTouchDownY;
					TileRaster.lastTouchMapOffsetX = ViewPort.mTouchMapOffsetX;
					TileRaster.lastTouchMapOffsetY = ViewPort.mTouchMapOffsetY;
				} else {
					mTouchMapOffsetX = (int) event.getX() - mTouchDownX;
					mTouchMapOffsetY = (int) event.getY() - mTouchDownY;
					this.updateRectangle();
				}
				// getPopup().updatePos(ViewPort.mTouchMapOffsetX,
				// ViewPort.mTouchMapOffsetY);
				t.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:
				final int viewWidth_2 = t.centerPixelX;
				final int viewHeight_2 = t.centerPixelY;

				if (t.panMode) {
					double[] center = t.getMRendererInfo().fromPixels(
							new int[] { viewWidth_2, viewHeight_2 });
					TileRaster.lastTouchMapOffsetX = ViewPort.mTouchMapOffsetX;
					TileRaster.lastTouchMapOffsetY = ViewPort.mTouchMapOffsetY;
					// getPopup().updatePos(TileRaster.lastTouchMapOffsetX,
					// TileRaster.lastTouchMapOffsetY);
					t.mTouchMapOffsetX = 0;
					t.mTouchMapOffsetY = 0;
					ViewPort.mTouchMapOffsetX = 0;
					ViewPort.mTouchMapOffsetY = 0;
					t.setMapCenter(center[0], center[1]);
					if (t.scrollingCenter != null)
						t.scrollingCenter.setCoordinates(center);
				} else {
					this.updateRectangle();

					mTouchMapOffsetX = 0;
					mTouchMapOffsetY = 0;
					double[] minXY = t.getMRendererInfo().fromPixels(
							new int[] { fromX, fromY });
					double[] maxXY = t.getMRendererInfo().fromPixels(
							new int[] { this.toX, this.toY });

					Extent currentExtent = new Extent(minXY[0], minXY[1],
							maxXY[0], maxXY[1]);

					t.zoomToExtent(currentExtent, false);
					t.setMapCenter(currentExtent.getCenter().getX(),
							currentExtent.getCenter().getY());
				}
				resetRectangle();

				//
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} finally {
			return true;
		}

	}

	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		if (popupView.onTouchEvent(e)) {
			popupView.setVisibility(View.VISIBLE);
			Intent i = new Intent(getContext(), POIDetailsActivity.class);
			Feature f = osmtile.selectedFeature;
			if (f != null && f.getGeometry() != null
					&& f.getGeometry() instanceof OsmPOI) {

				final double[] centerLonLat = getTileRaster().getCenterLonLat();
				double[] xy = ConversionCoords.reproject(centerLonLat[0],
						centerLonLat[1], CRSFactory.getCRS("EPSG:4326"),
						CRSFactory.getCRS("EPSG:900913"));

				OsmPOI poi = (OsmPOI) f.getGeometry();
				OsmPOI p = (OsmPOI) poi.clone();
				double[] pxy = ConversionCoords.reproject(p.getX(), p.getY(),
						CRSFactory.getCRS(getTileRaster().getMRendererInfo()
								.getSRS()), CRSFactory.getCRS("EPSG:4326"));
				p.setX(pxy[0]);
				p.setY(pxy[1]);
				InvokeIntents.fillIntentPOIDetails(p, new Point(xy[0], xy[1]),
						i, getTileRaster().map);
				getContext().startActivity(i);
			}
			return true;
		} else {
			popupView.setVisibility(View.INVISIBLE);
			return false;
		}
	}

	private void resetRectangle() {
		fromX = -1;
		fromY = -1;
		toX = -1;
		toY = -1;
	}

	boolean drawZoomRectangle = false;
	Rect rectangle;

	public void drawZoomRectangle(Rect r) {
		try {
			drawZoomRectangle = true;
			rectangle = r;
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public void cleanZoomRectangle() {
		try {
			drawZoomRectangle = false;
			rectangle = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	@Override
	/**
	 * returns null
	 */
	public Feature getNearestFeature(Pixel pixel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * returns null
	 */
	public ItemContext getItemContext() {
		return null;
	}

	@Override
	public void destroy() {
		try {
			Paints.filledPaint = null;
			Paints.rectanglePaint = null;
			path = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public boolean isFirstTouch() {
		return ++touchCounter < TOUCH_COUNTER;
	}

	@Override
	public void onExtentChanged(Extent newExtent, int zoomLevel,
			double resolution) {
		if (lastZoomLevel != zoomLevel)
			setPopupVisibility(View.INVISIBLE);

	}

	@Override
	public void onLayerChanged(String layerName) {
		// TODO Auto-generated method stub

	}

	public PopupView getPopup() {
		return this.popupView;
	}

	public void setPopupVisibility(int visibility) {
		getPopup().setVisibility(visibility);
		lastZoomLevel = getTileRaster().getZoomLevel();
		getTileRaster().resumeDraw();
	}

	public void setPopupText(String text) {
		getPopup().setText(text);
		getTileRaster().resumeDraw();
	}

	public void setPopupPos(int x, int y) {
		getPopup().setPos(x, y);
		getTileRaster().resumeDraw();
	}

	public void setPopupMaxSize(int maxX, int maxY) {
		getPopup().setMaxSize(maxX, maxY);
		getTileRaster().resumeDraw();
	}
}
