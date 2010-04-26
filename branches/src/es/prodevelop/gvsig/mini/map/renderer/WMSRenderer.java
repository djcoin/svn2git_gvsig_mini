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

import java.net.URL;
import java.util.Vector;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import org.gvsig.remoteclient.utils.Utilities;
import org.gvsig.remoteclient.wms.WMSStatus;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mini.wms.FMapWMSDriverFactory;
import es.prodevelop.gvsig.mini.wms.WMSCancellable;

/**
 * MapRenderer for WMS servers
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class WMSRenderer extends TMSRenderer {

	private final static Logger logger = LoggerFactory
			.getLogger(TMSRenderer.class);

	String[] layers;
	String version = "";
	WMSCancellable neverCanceled = new WMSCancellable();
	public static final int DEFAULT_MAX_ZOOM_LEVEL = 20;

	WMSRenderer(String baseUrl, String name, String imageFilenameEnding,
			int zoomMax, int tileSizePX, String[] layers, Extent bbox,
			String srs, String version) {
		super(baseUrl, name, imageFilenameEnding, zoomMax, tileSizePX,
				MapRenderer.WMS_RENDERER);
		try {
			this.BBOX = bbox;
			this.originX = bbox.getMinX();
			this.originY = bbox.getMinY();
			SRS = srs;
			this.layers = layers;

			double width = BBOX.getWidth();
			double height = BBOX.getHeight();
			this.version = version;

			if (width > height)
				calculateResolutions(width, zoomMax);
			else
				calculateResolutions(height, zoomMax);
		} catch (Exception e) {
			logger.error("WMSRenderer: ", e);
		}
	}

	/**
	 * Instantiates a WMSRenderer. @see Layers and layers.txt
	 * 
	 * @param props
	 *            The properties of the WMSRenderer
	 * @param layerName
	 *            The name of the layer
	 * @return A WMSRenderer
	 */
	public static WMSRenderer getWMSRenderer(String[] props, String layerName) {
		try {

			TMSRenderer w = (TMSRenderer) TMSRenderer.getTMSRenderer(props,
					layerName);
			String[] layers = props[13].split(":");
			WMSRenderer wmsR = new WMSRenderer(w.getBASEURL(), w.getNAME(), w
					.getIMAGE_FILENAMEENDING(), w.getZOOM_MAXLEVEL(), w
					.getMAPTILE_SIZEPX(), layers, w.BBOX, w.SRS, props[14]);

			return wmsR;
		} catch (Exception e) {
			logger.error("getWMSRenderer: ", e);
			return null;
		}
	}

	public static WMSRenderer getWMSRenderer(String baseUrl, String name,
			String imageFilenameEnding, int zoomMax, int tileSizePX,
			String[] layers, Extent bbox, String srs, String version) {
		try {
			WMSRenderer r = new WMSRenderer(baseUrl, name, imageFilenameEnding,
					zoomMax, tileSizePX, layers, bbox, srs, version);
			return r;
		} catch (Exception e) {
			logger.error("getWMSRenderer: ", e);
			return null;
		}
	}

	private void calculateResolutions(double size, int zoomMax) {
		try {
			resolutions = new double[zoomMax + 1];
			double iniRes = size / this.getMAPTILE_SIZEPX();
			for (int i = 0; i <= zoomMax; i++) {
				resolutions[i] = iniRes;
				size /= 2;
				iniRes = size / this.getMAPTILE_SIZEPX();
			}
		} catch (Exception e) {
			logger.error("calculateResolutions: ", e);
		}
	}

	public WMSRenderer(String baseUrl, String name, String imageFilenameEnding,
			int zoomMax, int tileSizePX, int type) {
		super(baseUrl, name, imageFilenameEnding, zoomMax, tileSizePX, type);
	}

	@Override
	public String getTileURLString(int[] tileID, int zoomLevel) {
		try {
			// logger.debug("tile: " + tileID[0] + ", " + tileID[1]);
			final WMSStatus status = new WMSStatus();			
			if (layers.length > 1) {
				final int length = layers.length;
				for (int i = 0; i < length; i++) {
					status.addLayerName(layers[i]);
				}
			} else {
				status.addLayerName(layers[0]);
			}

			status.setSrs(this.SRS);
			status.setFormat(this.getIMAGE_FILENAMEENDING());

			// if (CRSFactory.getCRS(this.SRS).getUnitsAbbrev().compareTo("g")
			// == 0) {
			// BBOX = TileConversor.tileBoundsG(tileID[1], tileID[0],
			// resolutions[zoomLevel]);
			// } else {

			Extent BBOX = TileConversor.tileMeterBounds(tileID[1], tileID[0],
					resolutions[zoomLevel]);
			// }

			status.setExtent(BBOX);
			status.setWidth(this.getMAPTILE_SIZEPX());
			status.setHeight(this.getMAPTILE_SIZEPX());

			final StringBuffer req = new StringBuffer();
			status.setDisconnectedServerURL(this.getBASEURL());
			// String info = FMapWMSDriverFactory.getFMapDriverForURL(
			// new URL(this.getBASEURL())).getFeatureInfo(status, 0, 0,
			// 10, this.neverCanceled);
			req.append(this.getBASEURL());
			String symbol = "?";
			if (this.getBASEURL().charAt(this.getBASEURL().length() - 1) == '?') {
				symbol = "";
			}
			req.append(symbol).append("REQUEST=GetMap&SERVICE=WMS&VERSION=")
					.append(version).append("&");
			req.append(getPartialQuery(status));
			return req.toString().replaceAll(" ", "%20");

		} catch (Exception e) {
			logger.error("getTileURLString: ", e);
			return null;
		}
	}

	protected String getPartialQuery(WMSStatus status) {
		final String fmt = status.getFormat();
		final StringBuffer req = new StringBuffer();
		req.append("LAYERS=" + Utilities.Vector2CS(status.getLayerNames()))
				.append("&SRS=" + status.getSrs()).append(
						"&BBOX=" + status.getExtent().getMinX() + ",").append(
						status.getExtent().getMinY() + ",").append(
						status.getExtent().getMaxX() + ",").append(
						status.getExtent().getMaxY()).append(
						"&WIDTH=" + status.getWidth()).append(
						"&HEIGHT=" + status.getHeight()).append(
						"&FORMAT=" + fmt).append("&STYLES=");

		Vector v = status.getStyles();
		if (v != null && v.size() > 0)
			req.append(Utilities.Vector2CS(v));
		v = status.getDimensions();
		if (v != null && v.size() > 0)
			req.append("&" + Utilities.Vector2URLParamString(v));
		if (status.getTransparency()) {
			req.append("&TRANSPARENT=TRUE");
		}
		return req.toString();
	}

	@Override
	public int getType() {
		return MapRenderer.WMS_RENDERER;
	}

	@Override
	public int isTMS() {
		return -1;
	}

	@Override
	public String toString() {
		StringBuffer layer = new StringBuffer();
		try {
			String base = super.toString();

			final int length = layers.length;
			StringBuffer layerNames = new StringBuffer();
			for (int i = 0; i < length; i++) {
				layerNames.append(layers[i]);
				if (i != length - 1) {
					layerNames.append(":");
				}
			}

			layer.append(base).append(",").append(layerNames);
			layer.append(",").append(version);
			return layer.toString();

		} catch (Exception e) {
			logger.error("toString: ", e);
			return null;
		}
	}
}
