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
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.map.GeoUtils;

/**
 * OSMMercatorRenderer for server that serves tiles with a different URL formatting (different from: http://tileserver.com/z/x/y.format)
 * @author aromeu 
 * @author rblanco
 *
 */
public class OSMParmsRenderer extends OSMMercatorRenderer {
	
	private final static Logger logger = LoggerFactory.getLogger(OSMParmsRenderer.class);
	
	String firstPart = "";
	String xParm = "";
	String yParm = "";
	String zParm = "";
	String lastPart = "";

	private OSMParmsRenderer(final String aBaseUrl, final String aName,
			final String aImageFilenameEnding, final int aZoomMax,
			final int aTileSizePX, final int type) {
		super(aBaseUrl, aName, aImageFilenameEnding, aZoomMax, aTileSizePX,
				type);
	}

	public static OSMParmsRenderer getOSMParmsRenderer(final String aBaseUrl,
			final String aName, final String aImageFilenameEnding,
			final int aZoomMax, final int aTileSizePX, final int type) {

		OSMParmsRenderer r = new OSMParmsRenderer(aBaseUrl, aName, aImageFilenameEnding,
				aZoomMax, aTileSizePX, type);
		return r;
	}

	@Override
	public String getTileURLString(final int[] tileID, final int zoomLevel) {
		try {
			if (firstPart.compareTo("") == 0 || xParm.compareTo("") == 0
					|| yParm.compareTo("") == 0 || zParm.compareTo("") == 0) {
				firstPart = this.getBASEURL().substring(0,
						this.getBASEURL().indexOf("#"));
				String secondPart = this.getBASEURL().substring(
						this.getBASEURL().indexOf("#") + 1,
						this.getBASEURL().length());
				xParm = secondPart.substring(0, secondPart.indexOf("#"));
				secondPart = secondPart.substring(secondPart.indexOf("#") + 1,
						secondPart.length());
				yParm = secondPart.substring(secondPart.indexOf("#")+1, secondPart
						.indexOf("#", 1));
				secondPart = secondPart.substring(secondPart.indexOf("#", 1) + 1,
						secondPart.length());
				zParm = secondPart.substring(secondPart.indexOf("#")+1, secondPart
						.indexOf("#", 1));
				secondPart = secondPart.substring(secondPart.indexOf("#", 1) + 1,
						secondPart.length());
				lastPart = secondPart.substring(secondPart.indexOf("#")+1, secondPart
						.length());
			}
		return new StringBuilder().append(firstPart).append(xParm)
				.append(tileID[GeoUtils.MAPTILE_LONGITUDE_INDEX]).append(yParm)
				.append(tileID[GeoUtils.MAPTILE_LATITUDE_INDEX]).append(zParm).append(zoomLevel).append(lastPart)
				.toString();
		} catch (Exception e) {
			logger.error(e);
			return null;
		}	
	}	
	
	@Override
	public int getType() {	
		return MapRenderer.OSMPARMS_RENDERER;
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
}
