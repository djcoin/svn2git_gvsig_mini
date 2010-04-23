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
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;

/**
 * A MapRenderer of OSM, taking as SRS EPSG:900913
 * @author aromeu 
 * @author rblanco
 *
 */
public class OSMMercatorRenderer extends TMSRenderer {
	
	private final static Logger logger = LoggerFactory
	.getLogger(OSMMercatorRenderer.class);

	OSMMercatorRenderer(String baseUrl, String name,
			String imageFilenameEnding, int zoomMax, int tileSizePX, int type) {
		super(baseUrl, name, imageFilenameEnding, zoomMax, tileSizePX, type);
		this.resolutions = new double[]{
//				1.40625, 0.703125, 0.3515625,
//				0.17578125, 0.087890625, 0.0439453125, 0.02197265625,
//				0.010986328125, 0.0054931640625, 0.00274658203125,
//				0.001373291015625, 0.0006866455078125, 0.00034332275390625,
//				0.000171661376953125, 0.0000858306884765625,
//				0.00004291534423828125, 0.000021457672119140625,
//				0.0000107288360595703125, 0.00000536441802978515625,
//				0.000002682209014892578125, 0.0000013411045074462890625,
//				0.00000067055225372314453125, 0.000000335276126861572265625 };
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
	0.29858214173896972949189556957335, 0.14929107086948486474594778478667,
	0.074645535434742432372973892393336, 0.037322767717371216186486946196668,
	0.018661383858685608093243473098334, 0.009330691929342804046621736549167
	};
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Instantiates a new OSMMercatorRenderer
	 * @param aBaseUrl The base URL of a OSM layer
	 * @param aName The name of the layer
	 * @param aImageFilenameEnding The format of the tile (png, jpg, jpeg, etc.)
	 * @param aZoomMax The max zoom level
	 * @param aTileSizePX The tile size in pixels (Usually 256)
	 * @param type 
	 * @return
	 */
	public static OSMMercatorRenderer getOSMMercatorRenderer(final String aBaseUrl,
			final String aName, final String aImageFilenameEnding,
			final int aZoomMax, final int aTileSizePX, final int type) {

		OSMMercatorRenderer r = new OSMMercatorRenderer(aBaseUrl, aName, aImageFilenameEnding,
				aZoomMax, aTileSizePX, type);
		return r;
	}
	
	/**
	 * @param aBaseUrl http://a.tile.openstreetmap.org/
	 * @param aName OPEN STREET MAP
	 * @param aImageFilenameEnding png
	 * @param aZoomMax 18
	 * @param aTileSizePX 256
	 * @param type 0
	 * @return
	 */
	public static OSMMercatorRenderer getMapnikRenderer() {
		return getOSMMercatorRenderer("http://a.tile.openstreetmap.org/",
				"OPEN STREET MAP", "png", 18, 256, 0);
	}

	/**
	 * @param aBaseUrl http://tah.openstreetmap.org/Tiles/tile/
	 * @param aName OSMARENDER
	 * @param aImageFilenameEnding png
	 * @param aZoomMax 18
	 * @param aTileSizePX 256
	 * @param type 0
	 * @return
	 */
	public static OSMMercatorRenderer getOsmarenderRenderer() {
		return getOSMMercatorRenderer("http://tah.openstreetmap.org/Tiles/tile/",
				"OSMARENDER", "png", 18, 256, 0);
	}

	/**
	 * @param aBaseUrl http://b.tile.cloudmade.com/BC9A493B41014CAABB98F0471D759707/1/256/
	 * @param aName CLOUDMADE
	 * @param aImageFilenameEnding png
	 * @param aZoomMax 18
	 * @param aTileSizePX 256
	 * @param type 0
	 * @return
	 */
	public static OSMMercatorRenderer getCloudmadeRenderer() {
		return getOSMMercatorRenderer(
				"http://b.tile.cloudmade.com/BC9A493B41014CAABB98F0471D759707/1/256/",
				"CLOUDMADE", "png", 18, 256, 0);
	}

	/**
	 * @param aBaseUrl http://andy.sandbox.cloudmade.com/tiles/cycle/
	 * @param aName CYCLE MAP
	 * @param aImageFilenameEnding jpg
	 * @param aZoomMax 18
	 * @param aTileSizePX 256
	 * @param type 0
	 * @return
	 */
	public static OSMMercatorRenderer getCycleMapRenderer() {
		return getOSMMercatorRenderer("http://andy.sandbox.cloudmade.com/tiles/cycle/",
				"CYCLE MAP", "jpg", 18, 256, 0);
	}

//	@Override
//	public double[] fromPixels(int[] pxy) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Pixel getCenterE6() {
//		// TODO Auto-generated method stub
//		return null;
//	}	
//
//	@Override
//	public int[] getMapTileFromCenter() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Extent getExtent() {
		return new Extent(-2.0037508342789244E7, -2.0037508342789244E7,
				2.0037508342789244E7, 2.0037508342789244E7);
	}

	@Override
	public double getOriginX() {
		return -2.0037508342789244E7;
	}

	@Override
	public double getOriginY() {
		return -2.0037508342789244E7;
	}

	@Override
	public int getType() {
		return MapRenderer.OSM_RENDERER;
	}

	@Override
	public int isTMS() {
		return 1;
	}

//	@Override
//	public Pixel getUpperLeftCornerOfCenterMapTileInScreen(Pixel reuse) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int[] toPixels(double[] pxy) {
//		// TODO Auto-generated method stub
//		return null;
//	}

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
	
	public String getSRS() {
		return "EPSG:3785";
	}
	
	public Pixel getUpperLeftCornerOfCenterMapTileInScreen(			
			final Pixel reuse) {
		final Pixel out = (reuse != null) ? reuse
				: new Pixel(0,0);
//		Pixel tile = TileConversor.latLonToTileG(this.getCenter().getX(), this
//				.getCenter().getY(), Map.vp.getDist1Pixel());	
		Point center = TileConversor.mercatorToLatLon(getCenter().getX(), getCenter().getY());
		Pixel tile = TileConversor.getTileNumber(center.getX(), center.getY(), this.getZoomLevel()
				);	
//		Extent e1 = TileConversor.tileBoundsG(tile.getX(), tile.getY(),
//				Map.vp.getDist1Pixel());		
		Extent e1 = TileConversor.tileOSMMercatorBounds(tile.getX(), tile.getY(),
				this.getZoomLevel());
		Extent e2 = Map.vp.calculateExtent(TileRaster.mapWidth,
				TileRaster.mapHeight, new es.prodevelop.gvsig.mini.geom.Point(
						this.getCenter().getX(), this.getCenter().getY()));
		int[] centerTopLeft = Map.vp.fromMapPoint(new double[] { e1.getMinX(),
				e1.getMaxY() }, e2.getMinX(), e2.getMaxY());
		out.setX(centerTopLeft[0]);
		out.setY(centerTopLeft[1]);
		return out;
	}
	
	public int[] getMapTileFromCenter() {
//		Pixel tile = TileConversor.latLonToTileG(this.getCenter().getX(), this
//				.getCenter().getY(), Map.vp.getDist1Pixel());
		Point center = TileConversor.mercatorToLatLon(getCenter().getX(), getCenter().getY());
		Pixel tile = TileConversor.getTileNumber(center.getX(), center.getY(), this.getZoomLevel()
				);
		return new int[]{tile.getY(), tile.getX()};
	}
	
	@Override
	public String getTileURLString(final int[] tileID, final int zoomLevel) {		
		return new StringBuilder().append(this.getBASEURL()).append(zoomLevel)
				.append("/").append(tileID[GeoUtils.MAPTILE_LONGITUDE_INDEX])
				.append("/").append(tileID[GeoUtils.MAPTILE_LATITUDE_INDEX])
				.append(".").append(this.getIMAGE_FILENAMEENDING()).toString();
	}
}
