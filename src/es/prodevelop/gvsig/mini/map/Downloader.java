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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.exception.DeliverableException;
import es.prodevelop.gvsig.mini.tasks.WorkQueue;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * Class which download tiles launching a background task on the WorkQueue and 
 * saves the Bitmap generated on the TileFilesystemProvider
 * @author aromeu 
 * @author rblanco
 *
 */
public class Downloader implements GeoUtils {

	private final static Logger log = LoggerFactory.getLogger(Downloader.class);
	public static final int MAPTILEDOWNLOADER_SUCCESS_ID = 0;
	public static final int MAPTILEDOWNLOADER_FAIL_ID = MAPTILEDOWNLOADER_SUCCESS_ID + 1;
	public static final int MAPTILEDOWNLOADER_OOM_ID = MAPTILEDOWNLOADER_FAIL_ID + 1;
	protected TileCache mTileCache;
	protected Map mMap;
	protected HashSet<String> mPending = new HashSet<String>();
	protected Context mCtx;
	public int transfer = 0;
	String datalog = null;
	protected TileFilesystemProvider mMapTileFSProvider;		
//	protected ExecutorService mThreadPool = Executors.newFixedThreadPool(3);	

	/**
	 * The constructor
	 */
	public Downloader(final Context ctx,
			final TileFilesystemProvider aMapTileFSProvider) {
		try {
			log.setLevel(Utils.FS_LOG_LEVEL);
			log.setClientID(this.toString());
			this.mCtx = ctx;
			this.mMap = (Map) ctx;
			this.mMapTileFSProvider = aMapTileFSProvider;			
		} catch (Exception e) {
			log.error("Downloader constructor: ", e);
		}				
	}

	/**
	 * Executes a background task in the Workqueue which downloads a resource from 
	 * a tile server, saves the file to a TileFilesystemProvider and sends a callback
	 * to a Handler
	 * @param aURLString The URL of the resource to download
	 * @param callback The Handler to callback
	 * @param tile The tile x-y
	 * @param layerName The name of the layer
	 * @param zoomLevel The zoomLevel of the tile
	 */
	public synchronized void getRemoteImageAsync(final String aURLString,
			final Handler callback, final int[] tile, final String layerName,
			final int zoomLevel, final String cacheURL) {
		try {
			WorkQueue.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					InputStream in = null;
					OutputStream out = null;					

					try {
						in = new BufferedInputStream(new URL(aURLString)
								.openStream(), Utils.IO_BUFFER_SIZE);
						final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
						out = new BufferedOutputStream(dataStream,
								Utils.IO_BUFFER_SIZE);
						Utils.copy(in, out);
						out.flush();
						final byte[] data = dataStream.toByteArray();
						log.debug("downloaded data from server");
						final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
								data.length);						
						Downloader.this.mMapTileFSProvider.mCache.putTile(cacheURL,
								bmp);
						log.debug("Bitmap put into memory cache");
						

						if (Utils.isSDMounted()) {
							log.debug("trying to save file to disk " + aURLString);
							Downloader.this.mMapTileFSProvider.saveFile(aURLString,
									data, tile, layerName, zoomLevel);
						}
							

//						mPending.remove(aURLString);
//						mPending.add(aURLString);
						transfer = data.length + transfer;
						int transfer2 = transfer / 1024;
						mMap.datatransfer2 = transfer2;					
						final Message successMessage = Message.obtain(callback,
								MAPTILEDOWNLOADER_SUCCESS_ID);
						successMessage.sendToTarget();
//						log.debug("Loaded from server: ");
//						mMapTileFSProvider.datas = null;
						mPending.remove(cacheURL);						
					} catch (OutOfMemoryError oe) {						
						System.gc();
						log.error(aURLString);
						log.error("OutOfMemory: ", oe);
						Downloader.this.mMapTileFSProvider.mCache.onLowMemory();
						final Message failMessage = Message.obtain(callback,
								MAPTILEDOWNLOADER_OOM_ID);
						failMessage.sendToTarget();
					} catch (Exception e) {
						//TODO throw SocketExceptions "The connection was reset" 
						final Message failMessage = Message.obtain(callback,
								MAPTILEDOWNLOADER_FAIL_ID);
						failMessage.sendToTarget();
						log.error("Tile Loading Error " + aURLString);
						log.error(e + aURLString);		
					} finally {
						Utils.closeStream(in);
						Utils.closeStream(out);						
					}
				}
			});
		} catch (Exception e) {
			log.error("getRemoteImageAsync: ", e);
		}		
	}

	/**
	 * Check if the resource to request is in the pending queue and if not, calls
	 * the background task to download it.
	 * @param aURLString The URL of the resource to download
	 * @param callback THe handler to callback
	 * @param tile The tile x-y
	 * @param layerName The name of the layer
	 * @param zoomLevel The zoom level of the tile
	 */
	public synchronized void requestMapTileAsync(final String aURLString,
			final Handler callback, final int[] tile, final String layerName,
			final int zoomLevel, final String cacheURL) {
		try {
			if (this.mPending.contains(cacheURL)) {
				return;
			}
			this.mPending.add(cacheURL);
			getRemoteImageAsync(aURLString, callback, tile, layerName, zoomLevel, cacheURL);
		} catch (Exception e) {
			log.error("requestMapTileAsync: ", e);
		}		
	}
}
