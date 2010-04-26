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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Pequeña y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.GPSPoint;
import es.prodevelop.gvsig.mini.geom.IGeometryDrawer;
import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.geom.MultiLineString;
import es.prodevelop.gvsig.mini.geom.MultiPoint;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.renderer.MapRenderer;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.phonecache.CRSFactory;
import es.prodevelop.gvsig.mini.phonecache.ConversionCoords;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

/**
 * Utility class to draw geometries in Android. @see es.prodevelop.gvsig.mini.geom package
 * @author aromeu 
 * @author rblanco
 *
 */
public class AndroidGeometryDrawer implements IGeometryDrawer {

	protected static Bitmap START;
	protected static Bitmap FINISH;
	String datalog = null;
	protected static Bitmap POIS;
	protected static Bitmap BT;
	private Paint pathPaint;
	private Paint circlePaint;
	private Path path;
	protected android.graphics.Point START_SPOT;
	protected android.graphics.Point FINISH_SPOT;
	protected android.graphics.Point POIS_SPOT;
	protected final android.graphics.Point BT_SPOT = new android.graphics.Point(
			12, 11);
	TileRaster t;
	protected final android.graphics.Point PERSON_HOTSPOT = new android.graphics.Point(
			18, 18);
	protected static Bitmap PERSON_ICON;
	private TileRaster tileRaster;
	protected Pixel firstRoutePixel, oldFirstRoutePixel, lastRoutePixel,
			oldLastRoutePixel;
	protected ArrayList<Pixel> pixelsRoute;
	private final static Logger log = LoggerFactory
			.getLogger(AndroidGeometryDrawer.class);

	public AndroidGeometryDrawer(TileRaster t, Context con) {
		try {
			log.setClientID(this.toString());
			tileRaster = t;
			pixelsRoute = new ArrayList<Pixel>();
			PERSON_ICON = ResourceLoader.getBitmap(R.drawable.arrowdown);
			this.START = ResourceLoader.getBitmap(R.drawable.startpoi);
			this.POIS = ResourceLoader.getBitmap(R.drawable.pois);
			START_SPOT = new android.graphics.Point(START.getWidth()
					- PERSON_ICON.getHeight() / 2, START.getHeight()
					- PERSON_ICON.getHeight() / 4);
			this.FINISH = ResourceLoader.getBitmap(R.drawable.finishpoi);
			FINISH_SPOT = new android.graphics.Point(0, FINISH.getHeight()
					- PERSON_ICON.getHeight() / 4);
			POIS_SPOT = new android.graphics.Point(
					POIS.getWidth()/2, POIS.getHeight());
			BT = ResourceLoader.getBitmap(R.drawable.bt);

			this.t = t;
			path = new Path();
			this.pathPaint = new Paint();
			this.pathPaint.setAntiAlias(false);
			this.pathPaint.setStrokeWidth(4);
			this.pathPaint.setStyle(Paint.Style.STROKE);
			this.pathPaint.setARGB(150, 137, 0, 182);

			this.circlePaint = new Paint();
			this.circlePaint.setAntiAlias(false);
			this.circlePaint.setStrokeWidth(4);
			this.circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
			this.circlePaint.setARGB(50, 137, 0, 182);
		} catch (Exception e) {
			log.error("constructor: " ,e);
		} catch (OutOfMemoryError e) {
			
			System.gc();
			log.error(e);
		}
	}

	private boolean mustDraw(final Extent e, final Point p) {
		try {
			if (e != null) {
				if (e.contains(p)) {
					// Log.i("", "mustDraw");
					return true;
				}
			}
			// Log.i("", "NotmustDraw");
			return false;
		} catch (Exception ex) {
			log.error("mustDraw: " + ex.getMessage());
			return false;
		}
	}

	private boolean mustDraw(final Extent e, final double[] p) {
		try {
			if (e != null) {
				if (e.contains(p)) {
					// Log.i("", "mustDraw");
					return true;
				}
			}
			// Log.i("", "NotmustDraw");
			return false;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	@Override
	public void draw(Point p, Object graphics, Extent extent, Object viewPort) {
		try {
			if (mustDraw(extent, p)) {
				Canvas c = (Canvas) graphics;
				int[] coords = tileRaster.getMRendererInfo().toPixels(
						new double[] { p.getX(), p.getY() });
				c.drawCircle(coords[0], coords[1], 5, t.mPaint);
			}
		} catch (Exception e) {
			log.error("drawPoint: " ,e);
		}
	}

	public void drawpoi(Point p, Object graphics, Extent extent, Object viewPort) {
		try {
			if (mustDraw(extent, p)) {
				Canvas c = (Canvas) graphics;
				int[] coords = tileRaster.getMRendererInfo().toPixels(
						new double[] { p.getX(), p.getY() });
				c.drawBitmap(POIS, coords[0] - POIS_SPOT.x, coords[1]
						- POIS_SPOT.y, t.mPaint);
			}
		} catch (Exception e) {
			log.error("drawpoi: " ,e);
		}
	}

	public void drawstart(Point p, Object graphics, Extent extent,
			Object viewPort) {
		try {
			final Canvas c = (Canvas) graphics;
			final MapRenderer renderer = tileRaster.getMRendererInfo();
			final double[] xy = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS("EPSG:4326"), CRSFactory.getCRS(renderer
							.getSRS()));
			final int[] coords = renderer.toPixels(xy);
			c.drawBitmap(START, coords[0] - START_SPOT.x, coords[1]
					- START_SPOT.y, t.mPaint);
		} catch (Exception e) {
			log.error("drawstart: " ,e);
		}
	}

	public void drawend(Point p, Object graphics, Extent extent, Object viewPort) {
		try {
			final Canvas c = (Canvas) graphics;
			final MapRenderer renderer = tileRaster.getMRendererInfo();
			final double[] xy = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS("EPSG:4326"), CRSFactory.getCRS(renderer
							.getSRS()));
			// int[] coords = tileRaster.getMRendererInfo().toPixels(new
			// double[]{p.getX(), p.getY()});
			final int[] coords = renderer.toPixels(xy);
			c.drawBitmap(FINISH, coords[0] - FINISH_SPOT.x, coords[1]
					- FINISH_SPOT.y, t.mPaint);
		} catch (Exception e) {
			log.error("drawend: " ,e);
		}
	}

	@Override
	public void draw(MultiPoint mp, Object graphics, Extent extent,
			Object viewPort) {
		try {
			final int size = mp.getNumPoints();

			Point p;
			for (int i = 0; i < size; i++) {
				p = mp.getPoint(i);
				this.drawpoi(p, graphics, extent, viewPort);
			}
		} catch (Exception e) {
			log.error("drawMultiPoint: " ,e);
		}
	}

	public void drawN(NamedMultiPoint mp, Object graphics, Extent extent,
			Object viewPort) {
		try {
			final int size = mp.getNumPoints();

			Point p;
			for (int i = 0; i < size; i++) {
				p = (Point) mp.getPoint(i).clone();
				this.drawpoi(p, graphics, extent, viewPort);
			}
		} catch (Exception e) {
			log.error("drawN: " ,e);
		}
	}

	@Override
	public void draw(GPSPoint arg0, Object arg1, Extent arg2, Object arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Pixel arg0, Object arg1, Extent arg2, Object arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(MultiLineString arg0, Object arg1, Extent arg2, Object arg3) {
		try {
			final LineString[] lineStrings = arg0.getLineStrings();

			final int size = lineStrings.length;
			for (int i = 0; i < size; i++) {
				draw(lineStrings[i], arg1, arg2, arg3);
			}
		} catch (Exception e) {
			log.error("drawMultiLine: " ,e);
		}
	}

	@Override
	public void draw(LineString arg0, Object arg1, Extent arg2, Object arg3) {
		try {
			final double[] xCoords = arg0.getXCoords();
			final double[] yCoords = arg0.getYCoords();
			
			if (xCoords.length == 0) return;

			final MapRenderer renderer = tileRaster.getMRendererInfo();

			if (tileRaster.CLEAR_ROUTE) {
				firstRoutePixel = null;
				lastRoutePixel = null;
				oldFirstRoutePixel = null;
				oldLastRoutePixel = null;
				tileRaster.CLEAR_ROUTE = false;
			}

			if (firstRoutePixel != null)
				oldFirstRoutePixel = firstRoutePixel;
			if (lastRoutePixel != null)
				oldLastRoutePixel = lastRoutePixel;

			int[] firstPixel = renderer.toPixels(new double[] { xCoords[0],
					yCoords[0] });
			firstRoutePixel = new Pixel(firstPixel[0], firstPixel[1]);

			final int length = xCoords.length;
			int[] lastPixel = renderer.toPixels(new double[] {
					xCoords[length - 1], yCoords[length - 1] });
			lastRoutePixel = new Pixel(lastPixel[0], lastPixel[1]);

			final int size = pixelsRoute.size();
			Pixel tempPixel = new Pixel(0,0);
			double[] tempCoords = new double[]{0,0};

			if ((oldFirstRoutePixel != null)
					&& (oldFirstRoutePixel.getX() - firstRoutePixel.getX()) == (oldLastRoutePixel
							.getX() - lastRoutePixel.getX())
					&& (oldFirstRoutePixel.getY() - firstRoutePixel.getY()) == (oldLastRoutePixel
							.getY() - lastRoutePixel.getY())) {
				for (int i = 0; i < size; i++) {
					this.pixelsRoute.set(i, new Pixel(this.pixelsRoute.get(i)
							.getX()
							- (oldFirstRoutePixel.getX() - firstRoutePixel
									.getX()), this.pixelsRoute.get(i).getY()
							- (oldFirstRoutePixel.getY() - firstRoutePixel
									.getY())));
				}
			}
			

			else if ((oldLastRoutePixel == null)
					|| (oldLastRoutePixel.getX() != lastRoutePixel.getX())
					|| (oldLastRoutePixel.getY() != lastRoutePixel.getY())) {
				if (pixelsRoute != null)
					pixelsRoute.clear();
				for (int i = 0; i < length; i++) {
					tempCoords[0] = xCoords[i];
					tempCoords[1] = yCoords[i];
					int[] pix = renderer.toPixels(tempCoords);	
					tempPixel = new Pixel(pix[0], pix[1]);
//					tempPixel.setX(pix[0]);
//					tempPixel.setY(pix[1]);
					pixelsRoute.add(tempPixel);
				}
			}

			if (path == null)
				path = new Path();
			path.rewind();

			Canvas c = (Canvas) arg1;

			final int s = pixelsRoute.size();
			for (int i = 0; i < s; i++) {
				Pixel current = pixelsRoute.get(i);
				if (i == 0)
					path.moveTo(current.getX(), current.getY());
				else
					path.lineTo(current.getX(), current.getY());
			}
			c.drawPath(path, this.pathPaint);

		} catch (Exception e) {
			log.error("drawLine: " ,e);
		}
	}

	public static int metersToPixels(final double mx, final int zoomLevel) {
		int px = (int) ((mx) / RESOLUTIONS[zoomLevel]);
		return px;
	}

	public static final double[] RESOLUTIONS = new double[] {
			156543.03392804096153584694438047,
			78271.516964020480767923472190235,
			39135.758482010240383961736095118,
			19567.879241005120191980868047559,
			9783.9396205025600959904340237794,
			4891.9698102512800479952170118897,
			2445.9849051256400239976085059448,
			1222.9924525628200119988042529724,
			611.49622628141000599940212648621,
			305.74811314070500299970106324311,
			152.87405657035250149985053162155,
			76.437028285176250749925265810776,
			38.218514142588125374962632905388,
			19.109257071294062687481316452694,
			9.5546285356470313437406582263471,
			4.7773142678235156718703291131735,
			2.3886571339117578359351645565868,
			1.1943285669558789179675822782934,
			0.59716428347793945898379113914669,
			0.29858214173896972949189556957335,
			0.14929107086948486474594778478667,
			0.074645535434742432372973892393336,
			0.037322767717371216186486946196668,
			0.018661383858685608093243473098334,
			0.009330691929342804046621736549167 };

}
