/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.context.map.wms;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.context.map.RouteContext;
import es.prodevelop.gvsig.mini.tasks.wms.GetFeatureInfoFunc;

public class WMSRouteContext extends RouteContext {

	private final static Logger log = Logger
			.getLogger(WMSRouteContext.class.getName());

	public WMSRouteContext() {
		super();
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	public WMSRouteContext(Map map) {
		super(map);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	public HashMap getFunctionalities() {
		HashMap h = super.getFunctionalities();
		try {
			h.put(R.layout.info_image_button, new GetFeatureInfoFunc(map,
					R.layout.info_image_button));
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
		return h;
	}

	@Override
	public int[] getViewsId() {
		int[] views = super.getViewsId();
		final int size = views.length;

		int[] v = new int[size + 1];
		try {

			for (int i = 0; i < size; i++) {
				v[i] = views[i];
			}
			v[size] = R.layout.info_image_button;
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			return null;
		}
		return v;
	}

}
