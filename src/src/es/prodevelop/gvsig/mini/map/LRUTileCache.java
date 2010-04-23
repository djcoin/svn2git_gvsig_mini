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
 *   author Rubén Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm.views.util;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */
package es.prodevelop.gvsig.mini.map;

import java.util.HashMap;
import java.util.LinkedList;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import android.graphics.Bitmap;

/**
 * A Last Recent Used cache of Bitmaps
 * @author aromeu 
 * @author rblanco
 *
 */
public class LRUTileCache extends HashMap<String, Bitmap> {

	private static final long serialVersionUID = 6215141;
	
	private final int maxCacheSize;

	private final LinkedList<String> list;
	private final static Logger log = LoggerFactory.getLogger(LRUTileCache.class);

	/**
	 * The Constructor
	 * @param maxCacheSize The cache size in number of Bitmaps
	 */
	public LRUTileCache(final int maxCacheSize) {
		super(maxCacheSize);
		this.maxCacheSize = Math.max(0, maxCacheSize);
		this.list = new LinkedList<String>();
	}

	/**
	 * Clears the cache
	 */
	public synchronized void clear() {		
		try {
			for (final Bitmap b : this.values()) {
				if (b != null) {
					b.recycle();
				}
			}
			super.clear();
			list.clear();
		} catch (Exception e) {
			log.error("clear cache: ", e);
		}
	}

	/**
	 * Adds a Bitmap to the cache. If the cache is full, removes the last
	 */
	public synchronized Bitmap put(final String key, final Bitmap value) {
		try {
			if (maxCacheSize == 0) {
				return null;
			}

			// if the key isn't in the cache and the cache is full...
			if (!super.containsKey(key) && !list.isEmpty()
					&& list.size() + 1 > maxCacheSize) {
				final Object deadKey = list.removeLast();
				Bitmap bitmap = super.remove(deadKey);
				if (bitmap != null)
					bitmap.recycle();
			}

			updateKey(key);			
		} catch (Exception e) {
			log.error(e);
		}
		return super.put(key, value);		
	}

	/**
	 * Returns a cached Bitmap given a key
	 * @param key The key of the Bitmap stored on the HashMap
	 * @return The Bitmap or null if it is not stored in the cache
	 */
	public synchronized Bitmap get(final String key) {
		try {
			final Bitmap value = super.get(key);
			if (value != null) {
				updateKey(key);
			}
			return value;
		} catch (Exception e) {
			log.error(e);
			return null;
		}		
	} 

	/**
	 * Removes a Bitmap from the cache
	 * @param key The key of the Bitmap to remove
	 */
	public synchronized void remove(final String key) {
		try {
			list.remove(key);			
		} catch (Exception e) {
			log.error(e);			
		}
		Bitmap bitmap = super.remove(key);
		if (bitmap != null)
			bitmap.recycle();		
	}

	/**
	 * The key is touched (recent used) and added to the top of the list
	 * @param key The key to be updated
	 */
	private void updateKey(final String key) {
		try {
			list.remove(key);
			list.addFirst(key);
		} catch (Exception e) {
			log.error(e);
		}		
	}
}
