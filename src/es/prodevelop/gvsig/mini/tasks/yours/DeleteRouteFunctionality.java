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

package es.prodevelop.gvsig.mini.tasks.yours;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;

/**
 * Deletes the route and notifies MapHandler with Map.ROUTE_CLEARED
 * @author aromeu 
 * @author rblanco
 *
 */
public class DeleteRouteFunctionality extends Functionality {
	
	private final static Logger log = LoggerFactory.getLogger(DeleteRouteFunctionality.class);

	public DeleteRouteFunctionality(Map map, int id) {
		super(map, id);		
	}

	@Override
	public boolean execute() {
		try {
			getMap().route.deleteRoute(true);
			getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_CLEARED);
		} catch (Exception e) {
			log.error(e);
		}
		return true;
	}

	@Override
	public int getMessage() {
		return TaskHandler.FINISHED;
	}

}
