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

package es.prodevelop.gvsig.mini.map;

import es.prodevelop.gvsig.mini.geom.Pixel;

/**
 * Container class of a single Tile instance. 
 * @author aromeu 
 * @author rblanco
 *
 */
public class Tile {
	
	public String mURL;
	public int[] tile;
	public Pixel distanceFromCenter;
	
	/**
	 * The constructor
	 * @param url The url of the tile
	 * @param tile The tile x-y
	 * @param distanceFromCenter Distance from the center of the screen in pixels
	 */
	public Tile(String url, int[] tile, Pixel distanceFromCenter) {
		this.mURL = url;
		this.tile = tile;
		this.distanceFromCenter = distanceFromCenter;
	}
	
	/**
	 * Frees memory
	 */
	public void destroy() {
		this.mURL = null;
		tile = null;
		distanceFromCenter = null;
	}
}
