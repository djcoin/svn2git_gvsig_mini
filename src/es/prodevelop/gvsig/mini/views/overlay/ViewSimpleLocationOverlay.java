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
 *   2009.
 *   author Rub�n Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm.views.overlay;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.Settings;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.GPSItemContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.geom.android.GPSPoint;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.Tags;
import es.prodevelop.gvsig.mini.views.overlay.factory.LocationOverlay;
import es.prodevelop.gvsig.mini.views.overlay.factory.MapOverlay;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.OSMMercatorRenderer;

/**
 * A MapOverlay to draw the GPS location
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class ViewSimpleLocationOverlay extends LocationOverlay {

	public final static String DEFAULT_NAME = "LOCATION";
	private int offsetOrientation = PORTRAIT_OFFSET_ORIENTATION;
	private int navigationOrientation = NAVIGATION_MODE;
	
	protected Bitmap ob = null;
	protected Bitmap PERSON_ICON = null;
	public int rotation;
	public int rotationFlag;
	public int rotationBearingFlag;
	public String provider = "";
	Matrix m = new Matrix();
	int compass = -1;
	TileRaster tileraster;
	private final static Logger log = Logger
			.getLogger(ViewSimpleLocationOverlay.class.getName());

	protected final android.graphics.Point PERSON_HOTSPOT = new android.graphics.Point(
			18, 18);

	public GPSPoint mLocation;	
	Path p;
	public double[] reprojectedCoordinates;

	private Pixel lastGPSPosition;

	public ViewSimpleLocationOverlay(final Context ctx,
			final TileRaster tileRaster, String name) {
		super(ctx, tileRaster, name);
		this.tileraster = tileRaster;
		try {
			CompatManager.getInstance().getRegisteredLogHandler()
					.configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// log.setClientID(this.toString());
			PERSON_ICON = ResourceLoader.getBitmap(R.drawable.gps_arrow);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError e) {
			System.gc();
			log.log(Level.SEVERE, "", e);
		}
	}

	public void setLocation(final GPSPoint mp, float acc, String provider) {
		this.mLocation = mp;
		this.mLocation.acc = acc;
		this.provider = provider;
	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster osmv) {
		return;
	}

	private int previousRotation = 999;

	private void drawOrientation(Canvas c, TileRaster osmv, int[] coords) {
		try {
			try {
				if (!Settings.getInstance().getBooleanValue(
						osmv.map.getText(R.string.settings_key_orientation)
								.toString())) {
					log.log(Level.FINE, "orientation is disabled in settings");
					return;
				}
			} catch (NoSuchFieldError e) {
				log.log(Level.SEVERE, "", e);
			}

			if (!osmv.map.navigation) {
				rotation = -compass - offsetOrientation;
				// log.log(Level.FINE, "orientation: " + offsetOrientation);
			} else {
				rotation = -osmv.mBearing - offsetOrientation;
			}
			int rotationToDraw = rotation;
			if (Math.abs(Math.abs(rotation) - Math.abs(previousRotation)) < Utils.MIN_ROTATION) {
				rotationToDraw = previousRotation;
			} else {
				previousRotation = rotation;
			}

			//
			// } else {
			//
			// if (rotationBearingFlag > (osmv.mBearing +
			// Utils.COMPASS_ACCURACY)
			// || (rotationBearingFlag < osmv.mBearing
			// - Utils.COMPASS_ACCURACY)) {
			// rotationFlag = osmv.mBearing;
			// }
			//
			//
			//
			// if (rotationBearingFlag > (osmv.mBearing +
			// Utils.COMPASS_ACCURACY)
			// || rotationBearingFlag < (osmv.mBearing
			// - Utils.COMPASS_ACCURACY)) {
			//
			// rotation = osmv.mBearing;
			//
			// } else {
			// rotation = rotationFlag;
			// }
			//
			// rotationBearingFlag = osmv.mBearing;

			int childRotation = rotationToDraw - 20;
			int parentRotation = rotationToDraw + 20;
			int length = -PERSON_ICON.getWidth() * 2;
			final int childLeft = (int) Math.round((Math.sin(Math
					.toRadians(childRotation)) * length)) + coords[0];
			final int childTop = (int) Math.round((Math.cos(Math
					.toRadians(childRotation)) * length)) + coords[1];

			final int childLeftIni = (int) Math.round((Math.sin(Math
					.toRadians(childRotation)) * 1)) + coords[0];
			final int childTopIni = (int) Math.round((Math.cos(Math
					.toRadians(childRotation)) * 1)) + coords[1];

			final int parentLeft = (int) Math.round((Math.sin(Math
					.toRadians(parentRotation)) * length)) + coords[0];
			final int parentTop = (int) Math.round((Math.cos(Math
					.toRadians(parentRotation)) * length)) + coords[1];

			final int parentLeftIni = (int) Math.round((Math.sin(Math
					.toRadians(parentRotation)) * 1)) + coords[0];
			final int parentTopIni = (int) Math.round((Math.cos(Math
					.toRadians(parentRotation)) * 1)) + coords[1];

			if (p == null)
				p = new Path();
			// path.moveTo(0, 0);
			// path.lineTo(0, height);
			// path.lineTo(width, height);
			// path.lineTo(width, 0);

			// RectF rect = new RectF();rect.set(childLeft, childTop - 2,
			// parentLeft, childTop + 2);
			// c.drawRect(rect, mPaint);

			p.moveTo(childLeftIni, childTopIni);
			p.lineTo(childLeft, childTop);
			// p.lineTo(parentLeftIni, parentTopIni);
			p.lineTo(parentLeft, parentTop);
			p.lineTo(childLeftIni, childTopIni);

			/****
			 * p.moveTo(childLeft, childTop); p.lineTo(childLeftIni,
			 * childTopIni); // p.lineTo(parentLeftIni, parentTopIni);
			 * p.lineTo(parentLeft, parentTop); p.lineTo(childLeft, childTop);
			 *****/

			// p.addOval(rect, Direction.CW);
			c.drawPath(p, Paints.circlePaintV);
			c.drawPath(p, Paints.mPaint);

			p.rewind();

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public void onDraw(final Canvas c, final TileRaster osmv) {
		try {

			if (!osmv.map.isLocationHandlerEnabled())
				return;

			// c.drawCircle(osmv.mapWidth/2, osmv.mapHeight/2, 5, circlePaint);
			if (this.mLocation != null
					&& (this.mLocation.getLatitudeE6() != 0 || this.mLocation
							.getLongitudeE6() != 0)) {

				final MapRenderer renderer = osmv.getMRendererInfo();
				reprojectedCoordinates = ConversionCoords.reproject(
						this.mLocation.getLongitudeE6() / 1E6,
						this.mLocation.getLatitudeE6() / 1E6,
						CRSFactory.getCRS("EPSG:4326"),
						CRSFactory.getCRS(renderer.getSRS()));

				int[] coords = osmv.getMRendererInfo().toPixels(
						reprojectedCoordinates);

				lastGPSPosition = new Pixel(coords[0], coords[1]);

				if (this.mLocation.acc != 0) {
					Path path = new Path();
					if (CRSFactory.getCRS(renderer.getSRS()).getUnitsAbbrev()
							.compareTo("m") == 0
							|| renderer instanceof OSMMercatorRenderer) {
						int distancePixels = (int) (this.mLocation.acc / Tags.RESOLUTIONS[renderer
								.getZoomLevel()]);

						int mapWidth = TileRaster.mapWidth;
						int mapHeight = TileRaster.mapHeight;

						int max = mapHeight;
						if (mapWidth > mapHeight) {
							max = mapWidth;
						}

						if (tileraster.map.navigation)
							tileraster
									.adjustViewToAccuracyIfNavigationMode(mLocation.acc);
						if (distancePixels <= max) {
							path.addCircle(coords[0], coords[1],
									(float) distancePixels, Direction.CCW);
							c.drawPath(path, Paints.circlePaintV);
						}
					}
				}

				// if (Math.abs(osmv.mBearing - compass) > 5) {
				compass = osmv.mBearing;
				// }
				this.drawOrientation(c, osmv, coords);
				c.drawBitmap(PERSON_ICON, coords[0] - PERSON_ICON.getWidth()
						/ 2, coords[1] - PERSON_ICON.getHeight() / 2,
						Paints.mPaint);

			}
		} catch (OutOfMemoryError e) {
			System.gc();
			log.log(Level.SEVERE, "OutOfMemoryError: ", e);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		try {
			if (this.mLocation == null)
				return null;
			log.log(Level.FINE,
					"get Nearest feature viewsimplelocationoverlay: "
							+ pixel.toString());
			double x = (this.mLocation.getLongitudeE6() / 1E6);
			double y = (this.mLocation.getLatitudeE6() / 1E6);

			if (x == 0 && y == 0)
				return null;

			final TileRaster tileRaster = this.getTileRaster();

			double[] co = ConversionCoords.reproject(x, y,
					CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS(tileRaster.getMRendererInfo().getSRS()));

			es.prodevelop.gvsig.mini.geom.Point p = new es.prodevelop.gvsig.mini.geom.Point(
					co[0], co[1]);
			log.log(Level.FINE, "pixel to coordinates: " + p.toString());
			// tileRaster.getMRendererInfo().reprojectGeometryCoordinates(p,
			// "EPSG:4326");
			int[] pxy = tileRaster.getMRendererInfo().toPixels(co);
			log.log(Level.FINE, "coordinates to pixel: " + pxy[0] + ","
					+ pxy[1]);
			final long distance = pixel.distance(new Pixel(pxy[0], pxy[1]));
			log.log(Level.FINE, "distance location overlay: " + distance);
			if (distance < ResourceLoader.MAX_DISTANCE && distance >= 0) {
				return new Feature(p);
			}
			return null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			return null;
		}
	}

	@Override
	public ItemContext getItemContext() {
		return new GPSItemContext(getTileRaster().map);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		try {
			boolean found = super.onSingleTapUp(e, osmtile);

			if (!found)
				return false;
			else {
				Message m = Message.obtain();
				m.what = Map.SHOW_TOAST;
				double lat = this.mLocation.getLatitudeE6() / 1E6;
				double lon = this.mLocation.getLongitudeE6() / 1E6;
				m.obj = this.getContext().getResources()
						.getString(R.string.typeof)
						+ ": "
						+ this.provider
						+ "\n"
						+ this.getContext().getResources()
								.getString(R.string.latitude)
						+ ": "
						+ lat
						+ "\n"
						+ this.getContext().getResources()
								.getString(R.string.longitude) + ": " + lon;
				getTileRaster().map.getMapHandler().sendMessage(m);
			}
			return true;
		} catch (Exception ex) {
			log.log(Level.SEVERE, "", ex);
			return false;
		}
	}

	public void setOffsetOrientation(int offset) {
		this.offsetOrientation = offset;
	}

	public void saveState(Bundle outState) {
		try {
			if (this.mLocation != null) {
				int lon = this.mLocation.getLongitudeE6();
				int lat = this.mLocation.getLatitudeE6();
				float acc = this.mLocation.acc;
				String provider = this.provider;
				outState.putInt("myLocLon", lon);
				outState.putInt("myLocLat", lat);
				outState.putFloat("myLocAcc", acc);
				outState.putString("myLocProv", provider);
				outState.putInt("orientation", this.offsetOrientation);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "saveState", e);
		}
	}

	public void loadState(Bundle outState) {
		try {
			int lon = outState.getInt("myLocLon");
			int lat = outState.getInt("myLocLat");
			float acc = outState.getFloat("myLocAcc");
			mLocation = new GPSPoint(lat, lon);
			mLocation.acc = acc;
			this.provider = outState.getString("myLocProv");
			this.offsetOrientation = outState.getInt("orientation");
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadState", e);
		}
	}

	@Override
	public void destroy() {
		try {
			mLocation = null;
			p = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public int getOffsetOrientation() {
		return offsetOrientation;
	}

	@Override
	public void onExtentChanged(Extent newExtent, int zoomLevel,
			double resolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLayerChanged(String layerName) {
		// TODO Auto-generated method stub

	}

	public Point getLocationLonLat() {
		if (mLocation == null
				|| !getTileRaster().map.isLocationHandlerEnabled())
			return new Point(0, 0);
		return new Point(this.mLocation.getLongitudeE6() / 1E6,
				this.mLocation.getLatitudeE6() / 1E6);
	}

}
