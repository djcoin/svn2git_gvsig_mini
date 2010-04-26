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

import es.prodevelop.gvsig.mini.util.Utils;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import android.graphics.Bitmap;

/**
 * Class to manage the memory cache of tiles. The strategy used is a LRUTileCache
 * with a size of 2 * {@link #calculateNumTiles(mapWidth, mapHeight, 256, 256)} * Utils.BUFFER_SIZE
 * @author aromeu 
 * @author rblanco
 *
 */
public class TileCache implements GeoUtils {

	protected HashMap<String, Bitmap> mCachedTiles;
	private final static Logger log = LoggerFactory.getLogger(TileCache.class);

	/**
	 * The constructor. Width, height and tileSize is used to calculate
	 * the optimal size of the cache according to the sizes of the Map and tiles
	 * @param width The width of the Map view
	 * @param height The height of the Map view
	 * @param tileSize The size of a single tile in pixels
	 */
	public TileCache(int width, int height, int tileSize) {
		try {
			int num = this.calculateNumTiles(width, height, tileSize, tileSize);
			log.debug("tile cache size: " + num); 
			if (num != 0)
				this.mCachedTiles = new LRUTileCache(num * 2 * Utils.BUFFER_SIZE);
			else
				this.mCachedTiles = new LRUTileCache(
						this.CACHE_MAPTILECOUNT_DEFAULT * 2 * Utils.BUFFER_SIZE);
		} catch (Exception e) {
			log.error(e);
		}
	}

	private int calculateRowCol(final int width, final int tileWidth) {
		int tilesPerRow = 0;
		try {
			final int fullRow = width / tileWidth;

			if (fullRow == 0) {
				tilesPerRow = 2;
			} else {
				int moduloRow = width % tileWidth;
				if (moduloRow == 0) {
					tilesPerRow = fullRow + 1;
				} else {
					if (moduloRow == 1) {
						tilesPerRow = fullRow + moduloRow;
					} else {
						if (moduloRow >= 2) {
							tilesPerRow = fullRow + 2;
						}
					}
				}
			}
		} catch (final Exception e) {
			log.error(e);
		}
		return tilesPerRow;
	}

	/**
	 * This method sets the row and col attributes according to the size of the
	 * map also sets the size of the Grid
	 * 
	 * @param width
	 * @param height
	 * @param tileWidth
	 * @param tileHeight
	 */
	private int calculateNumTiles(final int width, final int height,
			final int tileWidth, final int tileHeight) {
		try {
			if (height != 0 && width != 0) {
				int col = this.calculateRowCol(width, tileWidth);
				int row = this.calculateRowCol(height, tileHeight);
				return col * row;
			} else {
				// TODO: lanzar excepciÃ³n
				return 0;
			}
		} catch (final Exception e) {
			log.error(e);
			return 0;
		}
	}

	/**
	 * MaxCache&Name
	 */
	public TileCache(final int aMaximumCacheSize) {
		this.mCachedTiles = new LRUTileCache(aMaximumCacheSize);
	}

	public synchronized Bitmap getMapTile(final String aTileURLString) {
		return this.mCachedTiles.get(aTileURLString);
	}

	public synchronized void putTile(final String aTileURLString,
			final Bitmap aTile) {
		this.mCachedTiles.put(aTileURLString, aTile);
	}

	public static String format(final String aTileURLString) {
		return aTileURLString.substring(7).replace("/", "_");
	}

	/**
	 * This method clears the memory cache
	 */
	public synchronized void onLowMemory() {
		try {
			log.debug("onLowMemory");
			for (final Bitmap b : this.mCachedTiles.values()) {
				if (b != null) {
					b.recycle();
				}
			}
			this.mCachedTiles.clear();
		} catch (Exception e) {
			log.error(e);
		}		
	}
	
	public void destroy() {
		try {
			onLowMemory();
			this.mCachedTiles = null;
		} catch (Exception e) {
			log.error(e);
		}
	}
}
