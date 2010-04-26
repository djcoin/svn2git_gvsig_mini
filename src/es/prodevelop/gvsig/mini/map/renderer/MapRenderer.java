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

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.IGeometry;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

/**
 * A MapRenderer is a class that contains the basic info of a server to retrieve
 * its tiles
 * 
 * @author aromeu 
 * @author rblanco
 * 
 */
public abstract class MapRenderer {

	private static final Logger logger = LoggerFactory
			.getLogger(MapRenderer.class);

	private int zoomLevel = 0;

	private String BASEURL, NAME, IMAGE_FILENAMEENDING;
	private int ZOOM_MAXLEVEL, MAPTILE_SIZEPX;
	String SRS = DEFAULT_SRS;
	private final static String DEFAULT_SRS = "EPSG:4326";
	public double[] resolutions = new double[] { 1.40625, 0.703125, 0.3515625,
			0.17578125, 0.087890625, 0.0439453125, 0.02197265625,
			0.010986328125, 0.0054931640625, 0.00274658203125,
			0.001373291015625, 0.0006866455078125, 0.00034332275390625,
			0.000171661376953125, 0.0000858306884765625,
			0.00004291534423828125, 0.000021457672119140625,
			0.0000107288360595703125, 0.00000536441802978515625,
			0.000002682209014892578125, 0.0000013411045074462890625,
			0.00000067055225372314453125, 0.000000335276126861572265625 };
	// 156543.03392804096153584694438047,
	// 78271.516964020480767923472190235,
	// 39135.758482010240383961736095118,
	// 19567.879241005120191980868047559,
	// 9783.9396205025600959904340237794,
	// 4891.9698102512800479952170118897,
	// 2445.9849051256400239976085059448,
	// 1222.9924525628200119988042529724,
	// 611.49622628141000599940212648621,
	// 305.74811314070500299970106324311,
	// 152.87405657035250149985053162155,
	// 76.437028285176250749925265810776,
	// 38.218514142588125374962632905388,
	// 19.109257071294062687481316452694,
	// 9.5546285356470313437406582263471,
	// 4.7773142678235156718703291131735,
	// 2.3886571339117578359351645565868,
	// 1.1943285669558789179675822782934,
	// 0.59716428347793945898379113914669,
	// 0.29858214173896972949189556957335, 0.14929107086948486474594778478667,
	// 0.074645535434742432372973892393336, 0.037322767717371216186486946196668,
	// 0.018661383858685608093243473098334, 0.009330691929342804046621736549167
	// };

	public final static int TMS_RENDERER = 3;
	public final static int OSM_RENDERER = 0;
	public final static int EQUATOR_RENDERER = 1;
	public final static int QUADKEY_RENDERER = 2;
	public final static int OSMPARMS_RENDERER = 4;
	public final static int WMS_RENDERER = 5;

	public Point center = new Point(0, 0);

	/**
	 * The constructor
	 * 
	 * @param aBaseUrl
	 *            The URL of the layer
	 * @param aName
	 *            The name of the renderer
	 * @param aImageFilenameEnding
	 *            The format of the tile (png, jpg, jpeg, etc.)
	 * @param aZoomMax
	 *            The max zoom level
	 * @param aTileSizePX
	 *            The size in pixels of a tile
	 * @param type
	 *            TMS_RENDERER = 3; OSM_RENDERER = 0; EQUATOR_RENDERER = 1;
	 *            QUADKEY_RENDERER = 2; OSMPARMS_RENDERER = 4; WMS_RENDERER = 5;
	 */
	public MapRenderer(final String aBaseUrl, final String aName,
			final String aImageFilenameEnding, final int aZoomMax,
			final int aTileSizePX, final int type) {
		this.setBASEURL(aBaseUrl);
		this.setNAME(aName);
		this.setZOOM_MAXLEVEL(aZoomMax);
		this.setIMAGE_FILENAMEENDING(aImageFilenameEnding);
		this.setMAPTILE_SIZEPX(aTileSizePX);
	}

	/**
	 * Converts a tile x-y and a given zoom level int a URL to retrieve the tile
	 * 
	 * @param tileID
	 *            The tile x-y
	 * @param zoomLevel
	 *            The zoom level
	 * @return The URL String prepared to download the tile
	 */
	public abstract String getTileURLString(final int[] tileID,
			final int zoomLevel);

	/**
	 * 
	 * @return The max Extent of the renderer
	 */
	public abstract Extent getExtent();

	/**
	 * 
	 * @return The origin in the x axis of the max extent of the layer
	 */
	public abstract double getOriginX();

	/**
	 * 
	 * @return The origin in the y axis of the max extent of the layer
	 */
	public abstract double getOriginY();

	/**
	 * 
	 * @return The base URL of the renderer
	 */
	public String getBASEURL() {
		return BASEURL;
	}

	/**
	 * The directory where the tiles are stored on the file system matches the
	 * name returned by this method
	 * 
	 * @return The name of the renderer
	 */
	public String getNAME() {
		return NAME;
	}

	/**
	 * The tile format
	 * 
	 * @return (png, jpg, jpeg, etc.)
	 */
	public String getIMAGE_FILENAMEENDING() {
		return IMAGE_FILENAMEENDING;
	}

	/**
	 * 
	 * @return The max zoom level of the layer
	 */
	public int getZOOM_MAXLEVEL() {
		return ZOOM_MAXLEVEL;
	}

	/**
	 * 
	 * @return The tile size in pixels (Usually 256)
	 */
	public int getMAPTILE_SIZEPX() {
		return MAPTILE_SIZEPX;
	}

	public void setBASEURL(String value) {
		BASEURL = value;
	}

	public void setNAME(String value) {
		NAME = value;
	}

	public void setIMAGE_FILENAMEENDING(String value) {
		IMAGE_FILENAMEENDING = value;
	}

	public void setZOOM_MAXLEVEL(int value) {
		ZOOM_MAXLEVEL = value;
	}

	public void setMAPTILE_SIZEPX(int value) {
		MAPTILE_SIZEPX = value;
	}

	public abstract int getType();

	/**
	 * -1 == isTMS
	 * 
	 * @return
	 */
	public abstract int isTMS();

	/**
	 * The String returned by this method is the one used to persist a
	 * MapRenderer into the layers.txt @see Layers and LayersActivity
	 */
	public abstract String toString();

	/**
	 * 
	 * @return The SRS code of the renderer
	 */
	public String getSRS() {
		return SRS;
	}

	public void setSRS(String srs) {
		SRS = srs;
	}

	/**
	 * Transforms the current center of the map view from the current SRS of the
	 * MapRenderer to a specified SRS
	 * 
	 * @param toCRS The SRS to convert to the map center
	 * @return A double[] with xy
	 */
	public abstract double[] transformCenter(String toCRS);

	/**
	 * Sets the current map view center
	 * @param x
	 * @param y
	 */
	public abstract void setCenter(double x, double y);

	/**
	 * 
	 * @return The current center of the map. The coordinates are expressed in the current
	 * SRS of the MapRenderer
	 */
	public abstract Point getCenter();

	/**
	 * The map center (@see getCenter()) but normalized (*1E6)
	 * @return
	 */
	public abstract Pixel getCenterE6();

	/**
	 * 
	 * @return The current zoom level of the map view
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	/**
	 * Returns the coordinates in screen pixels of the upper left corner of center map tile
	 * @param reuse
	 * @return
	 */
	public abstract Pixel getUpperLeftCornerOfCenterMapTileInScreen(
			final Pixel reuse);

	/**
	 * 
	 * @return The tile x-y corresponding to the tile of the center of the screen
	 */
	public abstract int[] getMapTileFromCenter();

	/**
	 * Converts pixels coordinates to real world coordinates expressed in the SRS of 
	 * the MapRenderer
	 * @param pxy A screen pixel xy
	 * @return A double[] containing real world coordinates
	 */
	public abstract double[] fromPixels(int[] pxy);

	/**
	 * Converts real world coordinates into screen pixels
	 * @param pxy Real world coordinates
	 * @return An array of int containing pixels coordinates
	 */
	public abstract int[] toPixels(double[] pxy);

	/**
	 * Sets the center of the renderer in the center of the max extent @see {@link #getExtent()}
	 */
	public abstract void centerOnBBox();

	/**
	 * Reprojects the coordinates of a IGeometry expressed in a SRS to the SRS of
	 * the MapRenderer
	 * @param geometry The geometry to transform its coordinates
	 * @param fromCRS The SRS in which the coordinates of the IGeometry are expressed
	 */
	public void reprojectGeometryCoordinates(IGeometry geometry, String fromCRS) {
		try {
			final Point[] points = geometry.getCoordinates();
			final int size = points.length;

			Point temp = null;
			double[] coords;
			for (int i = 0; i < size; i++) {
				temp = points[i];
				if (temp != null) {
					coords = ConversionCoords.reproject(temp.getX(), temp
							.getY(), CRSFactory.getCRS(fromCRS), CRSFactory
							.getCRS(getSRS()));
					temp.setX(coords[0]);
					temp.setY(coords[1]);
					points[i] = temp;
				}
			}
			geometry.setCoordinates(points);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
