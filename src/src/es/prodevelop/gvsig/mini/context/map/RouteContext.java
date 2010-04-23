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

package es.prodevelop.gvsig.mini.context.map;

import java.util.HashMap;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.yours.DeleteRouteFunctionality;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * ItemContext when RouteOverlay is visible, that means a route has been calculated.
 * Adds to the DefaultContext the DeleteRouteFunctionality
 * @author aromeu 
 * @author rblanco
 *
 */
public class RouteContext extends DefaultContext {
	
	private final static Logger log = LoggerFactory.getLogger(RouteContext.class);

	public RouteContext() {
		super();
		try {
			log.setLevel(Utils.LOG_LEVEL);
			log.setClientID(this.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public RouteContext(Map map) {
		super(map);
		try {
			log.setLevel(Utils.LOG_LEVEL);
			log.setClientID(this.toString());
		} catch (Exception e) {
			log.error(e);
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
			log.error(e);
		}
		return h;
	}

	@Override
	public int[] getViewsId() {
		try {
			return new int[] { R.layout.twitter_image_button,
					R.layout.weather_image_button, R.layout.route_end_image_button,
					R.layout.route_start_image_button,
					R.layout.route_clean_image_button, R.layout.poi_image_button,R.layout.streetview_image_button };
		} catch (Exception e) {
			log.error(e);
			return null;
		}		
	}

	// @Override
	// public int[] getViewsId() {
	// int[] views = super.getViewsId();
	//		
	//		
	// final int length = views.length;
	// int[] v = new int[length + 1];
	//		
	// for (int i = 0; i<length; i++) {
	// v[i] = views[i];
	// }
	// v[length] = R.layout.route_clean_image_button;
	//		
	//		
	// return v;
	// }

}
