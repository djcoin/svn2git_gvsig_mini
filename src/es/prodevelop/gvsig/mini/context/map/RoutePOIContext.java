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
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.context.map;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.tasks.yours.DeleteRouteFunctionality;

/**
 * ItemContext to show when RouteOverlay and NameFinderOverlay are visible. 
 * @author aromeu 
 * @author rblanco
 *
 */
public class RoutePOIContext extends POIContext {
	
	private final static Logger log = Logger.getLogger(RoutePOIContext.class.getName());
	
	public RoutePOIContext() {
		super();
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,e.getMessage());
		}
	}

	public RoutePOIContext(Map map) {
		super(map);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	public HashMap getFunctionalities() {
		h = super.getFunctionalities();
		try {			
			DeleteRouteFunctionality dr = new DeleteRouteFunctionality(map,
					R.layout.route_clean_image_button);
			h.put(dr.getID(), dr);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}		
		return h;
	}

	@Override
	public int[] getViewsId() {
		try {
			return new int[] { R.layout.twitter_image_button,				
					R.layout.weather_image_button, R.layout.route_end_image_button,
					R.layout.route_start_image_button,
					R.layout.route_clean_image_button, R.layout.poi_image_button,
					R.layout.poi_clean_image_button, R.layout.poi_list_image_button,R.layout.streetview_image_button };
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			return null;
		}
	}

}
