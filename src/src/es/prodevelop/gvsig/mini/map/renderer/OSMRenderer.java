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

package es.prodevelop.gvsig.mini.map.renderer;

import java.util.List;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import android.graphics.Path;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.GPSPoint;
import es.prodevelop.gvsig.mini.map.GeoMath;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.phonecache.CRSFactory;
import es.prodevelop.gvsig.mini.phonecache.ConversionCoords;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;

/**
 * OSMRenderer with SRS EPSG:4326
 * @author aromeu 
 * @author rblanco
 *
 */
public class OSMRenderer extends MapRenderer implements GeoUtils {

	private final static Logger logger = LoggerFactory
			.getLogger(OSMRenderer.class);

	OSMRenderer(final String aBaseUrl, final String aName,
			final String aImageFilenameEnding, final int aZoomMax,
			final int aTileSizePX, final int type) {
		super(aBaseUrl, aName, aImageFilenameEnding, aZoomMax, aTileSizePX,
				type);
	}

	public static OSMRenderer getOSMRenderer(final String aBaseUrl,
			final String aName, final String aImageFilenameEnding,
			final int aZoomMax, final int aTileSizePX, final int type) {

		OSMRenderer r = new OSMRenderer(aBaseUrl, aName, aImageFilenameEnding,
				aZoomMax, aTileSizePX, type);
		return r;
	}

//	public static OSMRenderer getMapnikRenderer() {
//		return getOSMRenderer("http://a.tile.openstreetmap.org/",
//				"OPEN STREET MAP", "png", 17, 256, 0);
//	}
//
//	public static OSMRenderer getOsmarenderRenderer() {
//		return getOSMRenderer("http://tah.openstreetmap.org/Tiles/tile/",
//				"OSMARENDER", "png", 17, 256, 0);
//	}
//
//	public static OSMRenderer getCloudmadeRenderer() {
//		return getOSMRenderer(
//				"http://b.tile.cloudmade.com/BC9A493B41014CAABB98F0471D759707/1/256/",
//				"CLOUDMADE", "png", 17, 256, 0);
//	}
//
//	public static OSMRenderer getCycleMapRenderer() {
//		return getOSMRenderer("http://andy.sandbox.cloudmade.com/tiles/cycle/",
//				"CYCLE MAP", "jpg", 17, 256, 0);
//	}

	@Override
	public String getTileURLString(final int[] tileID, final int zoomLevel) {		
		return new StringBuilder().append(this.getBASEURL()).append(zoomLevel)
				.append("/").append(tileID[GeoUtils.MAPTILE_LONGITUDE_INDEX])
				.append("/").append(tileID[GeoUtils.MAPTILE_LATITUDE_INDEX])
				.append(".").append(this.getIMAGE_FILENAMEENDING()).toString();
	}

	@Override
	public Extent getExtent() {
		return new Extent(-180, -90,
				180, 90);
	}

	@Override
	public double getOriginX() {
		return -180;
	}

	@Override
	public double getOriginY() {
		return -90;
	}

	@Override
	public int getType() {
		return MapRenderer.OSM_RENDERER;
	}

	@Override
	public int isTMS() {
		return 1;
	}

	@Override
	public String toString() {
		StringBuffer layer = new StringBuffer();
		try {
			layer.append(this.getNAME()).append(";").append(this.getType())
					.append(",").append(this.getBASEURL()).append(",").append(
							this.getIMAGE_FILENAMEENDING()).append(",").append(
							this.getZOOM_MAXLEVEL()).append(",").append(
							this.getMAPTILE_SIZEPX());
			return layer.toString();
		} catch (Exception e) {
			logger.error("toString: ", e);
			return null;
		}
	}
	
	@Override
	public double[] transformCenter(String toCRS) {
		try {			
			return ConversionCoords.reproject(center.getX(), center.getY(), CRSFactory
					.getCRS(this.getSRS()), CRSFactory.getCRS(toCRS));
		} catch (Exception e) {
			logger.error("transformCoords: ", e);
			return null;
		}
	}

	@Override
	public void setCenter(double x, double y) {
		this.center = new Point(x, y);
	}

	@Override
	public Pixel getCenterE6() {
		return new Pixel((int)(this.center.getX()*1E6), (int)(this.center.getY()*1E6));
	}
	
	@Override
	public Point getCenter() {
		return new Point(this.center.getX(), this.center.getY());
	}
	
	public int[] getMapTileFromCenter() {
		return Utils.getMapTileFromCoordinates(
				this.getCenterE6().getY(), getCenterE6().getX(), getZoomLevel(), null);
	}
	
	public Pixel getUpperLeftCornerOfCenterMapTileInScreen(			
			final Pixel reuse) {
		final Pixel out = (reuse != null) ? reuse : new Pixel(0,0);

		final int viewWidth = TileRaster.mapWidth;
		final int viewWidth_2 = viewWidth / 2;
		final int viewHeight = TileRaster.mapHeight;
		final int viewHeight_2 = viewHeight / 2;

		final GeoMath bb = Utils.getBoundingBoxFromMapTile(getMapTileFromCenter(),
				this.getZoomLevel());
		final float[] relativePositionInCenterMapTile = bb
				.getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(
						this.getCenterE6().getY(), this.getCenterE6().getX(), null);

		final int centerMapTileScreenLeft = viewWidth_2
				- (int) (0.5f + (relativePositionInCenterMapTile[MAPTILE_LONGITUDE_INDEX] * this.getMAPTILE_SIZEPX()));
		final int centerMapTileScreenTop = viewHeight_2
				- (int) (0.5f + (relativePositionInCenterMapTile[MAPTILE_LATITUDE_INDEX] * this.getMAPTILE_SIZEPX()));

		out.setX(centerMapTileScreenLeft);
		out.setY(centerMapTileScreenTop);
		return out;
	}
	
	public double getLatitudeSpan() {
		return this.getDrawnGeoMath().getLongitudeSpanE6() / 1E6;
	}

	public int getLatitudeSpanE6() {
		return this.getDrawnGeoMath().getLatitudeSpanE6();
	}

	public double getLongitudeSpan() {
		return this.getDrawnGeoMath().getLatitudeSpanE6() / 1E6;
	}

	public int getLongitudeSpanE6() {
		return this.getDrawnGeoMath().getLatitudeSpanE6();
	}

	public GeoMath getDrawnGeoMath() {
		return getBoundingBox(TileRaster.mapWidth, TileRaster.mapHeight);
	}

	public GeoMath getVisibleGeoMath() {
		return getBoundingBox(TileRaster.mapWidth, TileRaster.mapHeight);
	}

	private GeoMath getBoundingBox(final int pViewWidth, final int pViewHeight) {

		final int[] centerMapTileCoords = Utils.getMapTileFromCoordinates(
				this.getCenterE6().getY(), this.getCenterE6().getX(), this.getZoomLevel(), null);

		final GeoMath tmp = Utils.getBoundingBoxFromMapTile(
				centerMapTileCoords, this.getZoomLevel());

		final int mLatitudeSpan_2 = (int) (1.0f * tmp.getLatitudeSpanE6()
				* pViewHeight / getMAPTILE_SIZEPX()) / 2;
		final int mLongitudeSpan_2 = (int) (1.0f * tmp.getLongitudeSpanE6()
				* pViewWidth / getMAPTILE_SIZEPX()) / 2;

		
		final int north = this.getCenterE6().getY() + mLatitudeSpan_2;
		final int south = this.getCenterE6().getY() - mLatitudeSpan_2;
		final int west = this.getCenterE6().getX() - mLongitudeSpan_2;
		final int east = this.getCenterE6().getX() + mLongitudeSpan_2;

		return new GeoMath(north, east, south, west);
	}
	
	public class OpenStreetMapViewProjection {

		final int viewWidth;
		final int viewHeight;
		final GeoMath bb;
		final int zoomLevel;
		final int tileSizePx;
		final int[] centerMapTileCoords;
		final Pixel upperLeftCornerOfCenterMapTile;

		public OpenStreetMapViewProjection() {
			viewWidth = TileRaster.mapWidth;
			viewHeight = TileRaster.mapHeight;

			zoomLevel = getZoomLevel();
			tileSizePx = getMAPTILE_SIZEPX();
			centerMapTileCoords = Utils.getMapTileFromCoordinates(
					getCenterE6().getY(), getCenterE6().getX(),
					zoomLevel, null);
			upperLeftCornerOfCenterMapTile = getUpperLeftCornerOfCenterMapTileInScreen(
					null);

			bb = getDrawnGeoMath();
		}

		public GPSPoint fromPixels(float x, float y) {

			x -= TileRaster.mTouchMapOffsetX;
			y -= TileRaster.mTouchMapOffsetY;
			
			return bb.getGeoPointOfRelativePositionWithLinearInterpolation(x
					/ viewWidth, y / viewHeight);
		}

		private static final int EQUATORCIRCUMFENCE = 40075004;

		public float metersToEquatorPixels(final float aMeters) {
			return aMeters / EQUATORCIRCUMFENCE
					* getMAPTILE_SIZEPX();
		}

		public android.graphics.Point toPixels(final GPSPoint in, final android.graphics.Point reuse) {
			return toPixels(in, reuse, true);
		}

		protected android.graphics.Point toPixels(final GPSPoint in, final android.graphics.Point reuse,
				final boolean doGudermann) {

			final android.graphics.Point out = (reuse != null) ? reuse : new android.graphics.Point();

			final int[] underGeopointTileCoords = Utils
					.getMapTileFromCoordinates(in.getLatitudeE6(), in
							.getLongitudeE6(), zoomLevel, null);

			final GeoMath bb = Utils.getBoundingBoxFromMapTile(
					underGeopointTileCoords, zoomLevel);

			final float[] relativePositionInCenterMapTile;
			if (doGudermann && zoomLevel < 7)
				relativePositionInCenterMapTile = bb
						.getRelativePositionOfGeoPointInBoundingBoxWithExactGudermannInterpolation(
								in.getLatitudeE6(), in.getLongitudeE6(), null);
			else
				relativePositionInCenterMapTile = bb
						.getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(
								in.getLatitudeE6(), in.getLongitudeE6(), null);

			final int tileDiffX = centerMapTileCoords[MAPTILE_LONGITUDE_INDEX]
					- underGeopointTileCoords[MAPTILE_LONGITUDE_INDEX];
			final int tileDiffY = centerMapTileCoords[MAPTILE_LATITUDE_INDEX]
					- underGeopointTileCoords[MAPTILE_LATITUDE_INDEX];
			final int underGeopointTileScreenLeft = upperLeftCornerOfCenterMapTile.getX()
					- (tileSizePx * tileDiffX);
			final int underGeopointTileScreenTop = upperLeftCornerOfCenterMapTile.getY()
					- (tileSizePx * tileDiffY);

			final int x = underGeopointTileScreenLeft
					+ (int) (relativePositionInCenterMapTile[MAPTILE_LONGITUDE_INDEX] * tileSizePx);
			final int y = underGeopointTileScreenTop
					+ (int) (relativePositionInCenterMapTile[MAPTILE_LATITUDE_INDEX] * tileSizePx);

			out.set(x + TileRaster.mTouchMapOffsetX, y
					+ TileRaster.mTouchMapOffsetY);
			return out;
		}

		public Path toPixels(final List<GPSPoint> in, final Path reuse) {
			return toPixels(in, reuse, true);
		}

		protected Path toPixels(final List<GPSPoint> in, final Path reuse,
				final boolean doGudermann) throws IllegalArgumentException {

			if (in.size() < 2)
				throw new IllegalArgumentException("Need at least 2 Points");

			final Path out = (reuse != null) ? reuse : new Path();

			int i = 0;
			for (GPSPoint gp : in) {
				i++;
				final int[] underGeopointTileCoords = Utils
						.getMapTileFromCoordinates(gp.getLatitudeE6(), gp
								.getLongitudeE6(), zoomLevel, null);

				final GeoMath bb = Utils.getBoundingBoxFromMapTile(
						underGeopointTileCoords, zoomLevel);

				final float[] relativePositionInCenterMapTile;
				if (doGudermann && zoomLevel < 7)
					relativePositionInCenterMapTile = bb
							.getRelativePositionOfGeoPointInBoundingBoxWithExactGudermannInterpolation(
									gp.getLatitudeE6(), gp.getLongitudeE6(),
									null);
				else
					relativePositionInCenterMapTile = bb
							.getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(
									gp.getLatitudeE6(), gp.getLongitudeE6(),
									null);

				final int tileDiffX = centerMapTileCoords[MAPTILE_LONGITUDE_INDEX]
						- underGeopointTileCoords[MAPTILE_LONGITUDE_INDEX];
				final int tileDiffY = centerMapTileCoords[MAPTILE_LATITUDE_INDEX]
						- underGeopointTileCoords[MAPTILE_LATITUDE_INDEX];
				final int underGeopointTileScreenLeft = upperLeftCornerOfCenterMapTile.getX()
						- (tileSizePx * tileDiffX);
				final int underGeopointTileScreenTop = upperLeftCornerOfCenterMapTile.getY()
						- (tileSizePx * tileDiffY);

				final int x = underGeopointTileScreenLeft
						+ (int) (relativePositionInCenterMapTile[MAPTILE_LONGITUDE_INDEX] * tileSizePx);
				final int y = underGeopointTileScreenTop
						+ (int) (relativePositionInCenterMapTile[MAPTILE_LATITUDE_INDEX] * tileSizePx);

				if (i == 0)
					out.moveTo(x + TileRaster.mTouchMapOffsetX, y
							+ TileRaster.mTouchMapOffsetY);

				else
					try {
						out.lineTo(x + TileRaster.mTouchMapOffsetX, y
								+ TileRaster.mTouchMapOffsetY);
					} catch (Exception e) {
						logger.error(e);
					}
			}

			return out;

		}
	}

	@Override
	public double[] fromPixels(int[] pxy) {
		GPSPoint p = getProjection().fromPixels(pxy[0], pxy[1]);
		double[] res = new double[2];
		res[0] = p.getLongitudeE6()/1E6;
		res[1] = p.getLatitudeE6()/1E6;
		return res;
	}

	@Override
	public int[] toPixels(double[] pxy) {
		int[] res = new int[2];
		android.graphics.Point p = getProjection().toPixels(new GPSPoint((int)(pxy[1]*1E6), (int)(pxy[0]*1E6)), null);
		res[0] = p.x;
		res[1] = p.y;
		return res;		
	}
	
	public OpenStreetMapViewProjection getProjection() {
		return new OpenStreetMapViewProjection();
	}

	@Override
	public void centerOnBBox() {
		try {
			this.center = new Point(0,0);
			this.setZoomLevel(3);
		} catch (Exception e) {
			logger.error(e);
		}		
	}
}
