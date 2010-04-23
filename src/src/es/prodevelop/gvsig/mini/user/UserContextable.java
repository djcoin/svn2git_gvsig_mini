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
 *   2010.
 *   author Miguel Montesinos mmontesinos@prodevelop.es
 *
 *

 */
package es.prodevelop.gvsig.mini.user;

import java.util.HashMap;

/**
 * Interface of classes used for storing application or user context.<p>
 * These classes will be registered in <code>UserContextManager</code> and
 * through the public methods of this interface, their attributes
 * will be persisted to file when exiting, and loaded when starting the application.
 * @author mmontesinos
 *
 */
public interface UserContextable {
	
	/**
	 * Persists to a file a set of String params (key, value) 
	 * @return params a HashMap &lt;String, Object&gt; with the list of attributes of an object to be
	 *                persisted
	 */
	public HashMap<String, Object> saveContext();
	
	/**
	 * Gets the attributes of the context object persisted in a previous session
	 * @param HashMap with the set of parameters corresponding to the object
	 *                attributes for loading them in the object's attributes<p>
	 *                The params are always stored as <code>String</code>, so it's up
	 *                to you to cast to the appropiate data types
	 * @return true if the context was succesfully loaded, false if not.
	 */
	public boolean loadContext(HashMap<String, Object> params);
	
	/**
	 * Gets the name of the file used to persist the context attributes.
	 * Includes the file extension.<p>
	 * It does not include paths, only the file name
	 * @return the file name including the extension<p>
	 *         Example: <code>UserContext.txt</code>
	 */
	public String getFileName();

}
