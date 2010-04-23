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

/**
 * A Factory of MapRenderers
 * @author aromeu 
 * @author rblanco
 *
 */
public class MapRendererFactory {
	
	private final static Logger logger = LoggerFactory.getLogger(MapRendererFactory.class);
	
	/**
	 * Instantiates a MapRenderer. @see Layers and layers.txt
	 * @param layerTitle The name of the MapRenderer 
	 * @param layerProps The properties of the MapRenderer
	 * @return
	 */
	public static MapRenderer getMapRenderer(String layerTitle, String[] layerProps) {
		MapRenderer renderer = null;
		try {
			int rendererType = Integer.valueOf(layerProps[0]).intValue();
			switch (rendererType) {
			case MapRenderer.EQUATOR_RENDERER:
				logger.debug("EQUATOR_RENDERER " + layerTitle);
				renderer = EquatorRenderer.getEquatorRenderer(
						layerProps[1], layerTitle, layerProps[2], Integer
								.valueOf(layerProps[3]).intValue(), Integer
								.valueOf(layerProps[4]).intValue(), Integer
								.valueOf(layerProps[0]).intValue());					
				break;
			case MapRenderer.OSM_RENDERER:
				logger.debug("OSM_RENDERER " + layerTitle);
				renderer = OSMMercatorRenderer.getOSMMercatorRenderer(layerProps[1],
						layerTitle, layerProps[2], Integer.valueOf(
								layerProps[3]).intValue(), Integer.valueOf(
								layerProps[4]).intValue(), Integer.valueOf(
								layerProps[0]).intValue());					
				break;				
			case MapRenderer.OSMPARMS_RENDERER:
				logger.debug("OSMPARMS_RENDERER " + layerTitle);
				renderer = OSMParmsRenderer
						.getOSMParmsRenderer(layerProps[1], layerTitle,
								layerProps[2], Integer.valueOf(
										layerProps[3]).intValue(), Integer
										.valueOf(layerProps[4]).intValue(),
								Integer.valueOf(layerProps[0]).intValue());					
				break;
			case MapRenderer.QUADKEY_RENDERER:
				logger.debug("QUADKEY_RENDERER " + layerTitle);
				renderer = QuadkeyRenderer.getQuadkeyRenderer(
						layerProps[1], layerTitle, layerProps[2], Integer
								.valueOf(layerProps[3]).intValue(), Integer
								.valueOf(layerProps[4]).intValue(), Integer
								.valueOf(layerProps[0]).intValue());					
				break;
			case MapRenderer.TMS_RENDERER:
				logger.debug("Found a TMS layer" + layerTitle);
				renderer = TMSRenderer
						.getTMSRenderer(layerProps, layerTitle);
				break;
			case MapRenderer.WMS_RENDERER:
				logger.debug("Found a WMS layer: " + layerTitle);
				renderer = WMSRenderer
						.getWMSRenderer(layerProps, layerTitle);
				break;
			}			
			
			return renderer;
		} catch (Exception e) {
			logger.error("getMapRenderer: ", e);
			return renderer;
		}
	}

}
