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

package es.prodevelop.gvsig.mini.context;

import java.util.HashMap;

import es.prodevelop.gvsig.mini._lg.IMap;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;

/**
 * An ItemContext consists on a set of Functionalities that can be applied
 * in a determined moment to a Contextable object.
 * @author aromeu 
 * @author rblanco
 *
 */
public interface ItemContext {	
	
	/**
	 * The funcionalitites that can be applied to a Contextable instance.
	 * The keys of the HashMap are the IDs of the Drawable resources used
	 * to represent the Functionalities with buttons @see {@link #getViewsId()}
	 * @return A set of Functionalities @see Functionality
	 */
	public HashMap getFunctionalities();
	
	/**
	 * Each Functionality should be represented by a button
	 * @return An array of the Resources id that represent the Drawable objects
	 * of the Functionalities
	 */
	public int[] getViewsId();
	
	/**
	 * The current Functionality that is executing on the WorkQueue
	 * @return The executing Functionality
	 */
	public Functionality getExecutingFunctionality();
	
	/**
	 * Call this method to set the current executing Functionality. Usually this method
	 * is called by a Functionality itself when enters its run method
	 * @param f
	 */
	public void setExecutingFunctionality(Functionality  f);
	
	/**
	 * Returns a Functionality passing the ID of the Resource that represents it
	 * @param id The id of the Drawable resource (R.id.whatever)
	 * @return The Functionality
	 */
	public Functionality getFunctionalityByID(int id);	
	
	/**
	 * Gets the current executing Functionality and cancels it
	 */
	public void cancelCurrentTask();
	
	/**
	 * Sets the current Map Activity instance
	 * @param map A Map instance
	 */
	public void setMap(Map imap);
}
