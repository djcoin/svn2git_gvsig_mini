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
package es.prodevelop.gvsig.mini.tasks.namefinder;



import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;

/**
 * Notifies the MapHandler with the message Map.POI_LIST
 * @author aromeu 
 * @author rblanco
 *
 */
public class ListPOIFunc extends Functionality {

	private final static Logger log = Logger.getLogger(ListPOIFunc.class.getName());
	public ListPOIFunc(Map map, int id) {
		super(map, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute() {
		try {
			getMap().getMapHandler().sendEmptyMessage(Map.POI_LIST);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
		return true;
	}

	@Override
	public int getMessage() {
		return TaskHandler.FINISHED;
	}

}
