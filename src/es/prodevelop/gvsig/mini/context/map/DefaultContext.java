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
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.map.ShowStreetView;
import es.prodevelop.gvsig.mini.tasks.namefinder.ShowNameFinderAddressDialog;
import es.prodevelop.gvsig.mini.tasks.twitter.ShareMyLocationFunc;
import es.prodevelop.gvsig.mini.tasks.weather.WeatherFunctionality;
import es.prodevelop.gvsig.mini.tasks.yours.FinishPointFunctionality;
import es.prodevelop.gvsig.mini.tasks.yours.StartPointFunctionality;

/**
 * DefaultContext is an ItemContext with common Functionalities, normally is applied
 * when long pressing a void area of the map
 * @author aromeu 
 * @author rblanco
 *
 */
public class DefaultContext implements ItemContext {

	private final static Logger log = Logger.getLogger(DefaultContext.class.getName());
	protected Map map;
	WeatherFunctionality weatherFunc;
	protected HashMap h;
	Functionality executing;
	
	/**
	 * Don't use this constructor. This is needed to use newInstance method of Class
	 */
	public DefaultContext() {
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	/**
	 * The constructor
	 * @param map The instance of the Map Activity
	 */
	public DefaultContext(Map map) {
		this();
		try {
			this.map = map;
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}		
	}

	@Override
	public HashMap getFunctionalities() {
		h = new HashMap();
		try {			
			log.log(Level.FINE, "getFunctionalities");
			ShowStreetView ssv = new ShowStreetView(map,
					R.layout.streetview_image_button);
			StartPointFunctionality sp = new StartPointFunctionality(map,
					R.layout.route_start_image_button);
			FinishPointFunctionality fp = new FinishPointFunctionality(map,
					R.layout.route_end_image_button);
			weatherFunc = new WeatherFunctionality(map, R.layout.weather_image_button);
			ShareMyLocationFunc tp = new ShareMyLocationFunc(map, R.layout.twitter_image_button);
			ShowNameFinderAddressDialog sn = new ShowNameFinderAddressDialog(map, R.layout.poi_image_button, ShowNameFinderAddressDialog.POI_DIALOG);
			h.put(sp.getID(), sp);
			h.put(fp.getID(), fp);
			h.put(weatherFunc.getID(), weatherFunc);
			h.put(tp.getID(), tp);
			h.put(sn.getID(), sn);
			h.put(ssv.getID(), ssv);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}		
		return h;
	}

	@Override
	public int[] getViewsId() {
		try {
			return new int[] { R.layout.twitter_image_button, R.layout.weather_image_button,
					R.layout.route_end_image_button, R.layout.route_start_image_button, R.layout.poi_image_button, R.layout.streetview_image_button };
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			return null;
		}		
	}

	@Override
	public Functionality getExecutingFunctionality() {
		return executing;
	}

	@Override
	public Functionality getFunctionalityByID(int id) {
		return (Functionality)h.get(id);
	}

	@Override
	public void setExecutingFunctionality(Functionality f) {
		this.executing = f;		
	}

	@Override
	public void cancelCurrentTask() {
		try {
			if (executing != null)
				executing.cancel();
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	public void setMap(Map map) {
		this.map = map;
	}

}
