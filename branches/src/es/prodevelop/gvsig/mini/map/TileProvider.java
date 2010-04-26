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

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.tasks.WorkQueue;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * A class to manage tile downloading, saving/loading to/from disk and putting/getting
 * to/from memory cache
 * @author aromeu 
 * @author rblanco
 *
 */
public class TileProvider implements GeoUtils {

	public Bitmap mLoadingMapTile = null;
	protected Context mCtx;
	String datalog = null;
	protected TileCache mTileCache;
	protected TileFilesystemProvider mFSTileProvider;
	protected Downloader mTileDownloader;
	private Handler mLoadCallbackHandler = new LoadCallbackHandler();
	private Handler mDownloadFinishedListenerHander;
	private final static Logger log = LoggerFactory
			.getLogger(TileProvider.class);

	/**
	 * The constructor
	 * @param ctx A Context instance
	 * @param aDownloadFinishedListener A Handler to send callbacks
	 * @param width The map width to calculate memory cache size
	 * @param height The map height to calculate memory cache size
	 * @param tileSize A single tile size in pixels to calculate memory cache size
	 */
	public TileProvider(final Context ctx,
			final Handler aDownloadFinishedListener, final int width,
			final int height, final int tileSize) {
		try {
			this.mTileCache = new TileCache(width, height, tileSize);
			this.mFSTileProvider = new TileFilesystemProvider(4 * 1024 * 1024,
					this.mTileCache);
			this.mTileDownloader = new Downloader(ctx, this.mFSTileProvider);
			this.mDownloadFinishedListenerHander = aDownloadFinishedListener;
			this.mCtx = ctx;
			try {
				this.mLoadingMapTile = ResourceLoader.getBitmap(R.drawable.maptile_loading);
			} catch (OutOfMemoryError e) {
				System.gc();
				log.error(e);			
				this.onLowMemory();
				Utils.showSendLogDialog(ctx, R.string.fatal_error);
			} catch (Exception e) {
				log.error(e);
			}
			log.setLevel(Utils.FS_LOG_LEVEL);
			log.setClientID(this.toString());
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * Clears the pending tile queues of Downloader and TileFilesystemProvider instances 
	 * of this TileProvider. Also clear pending tasks on WorkQueue
	 */
	public void clearPendingQueue() {
		try {
			log.debug("clear pending queue");
			this.mFSTileProvider.mPending.clear();
			this.mTileDownloader.mPending.clear();		
			WorkQueue.getInstance().clearPendingTasks();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * The strategy to get Tiles is:
	 * If the tile is in memory cache, then is returned and put the first (LRU strategy)
	 * If not then the tile is searched on the FileSystem if the tile is found, is put 
	 * into the memory cache if not then is downloaded from the server, save to file system
	 * and finally put into memroy cache
	 * @param aTileURLString The URL of the tile to retrieve
	 * @param tile The tile x-y
	 * @param layerName The name of the layer
	 * @param zoomLevel The current zoom level
	 * @return A Bitmap representing the tile
	 */
	public Bitmap getMapTile(final String aTileURLString, int[] tile,
			String layerName, int zoomLevel) {
		try {
			Bitmap ret = this.mTileCache.getMapTile(aTileURLString);
			if (ret != null) {				
//					log.debug("MapTileCache succeded for: " + aTileURLString);
			} else {				
//					log.debug("Cache failed, trying from FS.");
				try {					

					if (Utils.isSDMounted()) {
						if (!this.mTileDownloader.mPending.contains(aTileURLString)) {
							log.debug("load from disk");
							this.mFSTileProvider.loadMapTileToMemCacheAsync(
									aTileURLString, this.mLoadCallbackHandler,
									tile, layerName, zoomLevel);							
						}										
						ret = this.mLoadingMapTile;
					} else {
						ret = this.mLoadingMapTile;
						log.debug("load from server");
						this.mTileDownloader.requestMapTileAsync(aTileURLString,
								this.mLoadCallbackHandler, tile, layerName,
								zoomLevel);	
					}

				} catch (Exception e) {
					ret = this.mLoadingMapTile;
					log.debug("load from server");
					this.mTileDownloader.requestMapTileAsync(aTileURLString,
							this.mLoadCallbackHandler, tile, layerName,
							zoomLevel);	
//					log.error(e);
				}
//				if (ret == null) {
//									
//				}
			}			
			// if (ret == this.mLoadingMapTile) return null;
			return ret;

		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	/**
	 * Handler to manage Messages from Downloader and TileFilesystemProvider
	 * @author aromeu 
 * @author rblanco
	 *
	 */
	private class LoadCallbackHandler extends Handler {
		@Override
		public void handleMessage(final Message msg) {
			try {
				final int what = msg.what;
				switch (what) {
				case Downloader.MAPTILEDOWNLOADER_SUCCESS_ID:
					TileProvider.this.mDownloadFinishedListenerHander
							.sendEmptyMessage(Downloader.MAPTILEDOWNLOADER_SUCCESS_ID);					
					break;
				case Downloader.MAPTILEDOWNLOADER_FAIL_ID:
					break;
				case Downloader.MAPTILEDOWNLOADER_OOM_ID:
//					Utils.showSendLogDialog(TileProvider.this.mCtx);
					break;

				case TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID:
					TileProvider.this.mDownloadFinishedListenerHander
							.sendEmptyMessage(TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID);					
					break;
				case TileFilesystemProvider.MAPTILEFSLOADER_FAIL_ID:
					break;
				case TileFilesystemProvider.MAPTILEFSLOADER_OOM_ID:
//					Utils.showSendLogDialog(TileProvider.this.mCtx);
					break;
				}
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * Clears the TileCache instance
	 */
	public void onLowMemory() {
		try {
			this.mTileCache.onLowMemory();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	/**
	 * Frees memory
	 */
	public void destroy() {
		try {
			mLoadingMapTile = null;
			mTileCache.destroy();
			mTileCache = null;
			mFSTileProvider.destroy();
			mFSTileProvider = null;			
			mTileDownloader = null;
		} catch (Exception e) {
			log.error(e);
		}
	}
}
