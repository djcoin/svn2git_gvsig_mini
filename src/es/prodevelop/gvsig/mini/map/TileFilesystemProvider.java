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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.WorkQueue;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.projection.TileConversor;

/**
 * A provider of tiles stored on the file system
 * @author aromeu 
 * @author rblanco
 *
 */
public class TileFilesystemProvider implements GeoUtils {

	public static final int MAPTILEFSLOADER_SUCCESS_ID = 1000;
	public static final int MAPTILEFSLOADER_FAIL_ID = MAPTILEFSLOADER_SUCCESS_ID + 1;
	public static final int MAPTILEFSLOADER_OOM_ID = MAPTILEFSLOADER_FAIL_ID + 1;
	public Map map;
	String datalog = null;
	public Downloader down;
	// public byte[] datas = null;
//	protected ExecutorService mThreadPool = Executors.newFixedThreadPool(2);
	protected final TileCache mCache;
	public Handler mHandler;
	protected HashSet<String> mPending = new HashSet<String>();
	private static String SD_DIR;
	private final static Logger log = LoggerFactory
			.getLogger(TileFilesystemProvider.class);

	/**
	 * The constructor
	 * @param aMaxFSCacheByteSize Not used at the moment
	 * @param aCache The TileCache instance
	 */
	public TileFilesystemProvider(final int aMaxFSCacheByteSize,
			final TileCache aCache) {
		this.mCache = aCache;
		try {
			log.setLevel(Utils.FS_LOG_LEVEL);
			log.setClientID(this.toString());
		} catch (Exception e) {
			log.error(e);
		}

	}

	private static final String getSDDir() {
		try {
			if (SD_DIR == null)
				SD_DIR = Environment.getExternalStorageDirectory().getPath();
			return SD_DIR;
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	/**
	 * Checks if the tile to download is already on the pending queue and if not
	 * launches a background task to find it on the file system and load it to the
	 * TileCache
	 * @param aTileURLString The URL of the tile to retrieve
	 * @param callback The Handler callback
	 * @param tile The tile x-y
	 * @param layerName The name of the layer to know in which folder search
	 * @param zoomLevel The current zoom level
	 * @throws IOException
	 */
	public synchronized void loadMapTileToMemCacheAsync(final String aTileURLString,
			final Handler callback, final int[] tile, final String layerName,
			final int zoomLevel) throws IOException {

		if (this.mPending.contains(aTileURLString))
			return;

		// final String formattedTileURLString =
		// TileCache.format(aTileURLString);
		final StringBuffer quad = TileConversor.tileXYToQuadKey(tile[1],
				tile[0], zoomLevel + 1);
		final String quadDir = TileConversor.quadKeyToDirectory(quad)
				.toString();

		String st = getSDDir();
		File f = new File(st + "/" + Utils.APP_DIR + "/" + Utils.MAPS_DIR
				+ "/" + layerName + "/" + quadDir + "a.tile.gvsig");		

		if (!f.exists()) {
			f = new File(st + "/" + Utils.APP_DIR + "/" + Utils.MAPS_DIR
					+ "/" + layerName + "/" + quadDir + "a.png");
			if (!f.exists())
				throw new IOException("File not exists");
		} else {
			if (f.length() <= 0) {
				f.delete();
				throw new IOException("File length was 0");				
			}
		}

		final FileInputStream in = new FileInputStream(f);

		this.mPending.add(aTileURLString);

		WorkQueue.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				OutputStream out = null;
				ByteArrayOutputStream dataStream = null;

				try {
					dataStream = new ByteArrayOutputStream();

					out = new BufferedOutputStream(dataStream,
							Utils.IO_BUFFER_SIZE);
					Utils.copy(in, out);
					out.flush();

					final byte[] data = dataStream.toByteArray();
					if (data.length <= 0) {
						throw new IOException("");
					}
					final Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
							data.length);

					TileFilesystemProvider.this.mCache.putTile(aTileURLString,
							bmp);

					final Message successMessage = Message.obtain(callback,
							MAPTILEFSLOADER_SUCCESS_ID);
					successMessage.sendToTarget();
//					log.debug("Loaded from disk: " + aTileURLString);
				} catch (java.lang.OutOfMemoryError ex) {					
					System.gc();
					log.error("OutOfMemoryError: ", ex);
					TileFilesystemProvider.this.mCache.onLowMemory();
					final Message failMessage = Message.obtain(callback,
							MAPTILEFSLOADER_OOM_ID);
					failMessage.sendToTarget();
				} catch (Exception e) {
					final Message failMessage = Message.obtain(callback,
							MAPTILEFSLOADER_FAIL_ID);
					failMessage.sendToTarget();
				} finally {
					Utils.closeStream(in);
					Utils.closeStream(dataStream);
					Utils.closeStream(out);
				}
				TileFilesystemProvider.this.mPending.remove(aTileURLString);
			}
		});
	}

	/**
	 * Saves a tile to disk following a Quadtree strategy
	 * @param aURLString The URL of the tile
	 * @param someData The array of bytes representing the tile
	 * @param tile The tile x-y
	 * @param layerName The name of the layer
	 * @param zoomLevel The current zoom level
	 * @throws IOException
	 */
	public synchronized void saveFile(final String aURLString, final byte[] someData,
			final int[] tile, String layerName, int zoomLevel)
			throws IOException {
		// final String filename = TileCache.format(aURLString);

		final StringBuffer quad = TileConversor.tileXYToQuadKey(tile[1],
				tile[0], zoomLevel + 1);
		final String quadDir = TileConversor.quadKeyToDirectory(quad)
				.toString();

		String st = getSDDir();
		final File f = new File(st + "/" + Utils.APP_DIR + "/" + Utils.MAPS_DIR
				+ "/" + layerName + "/" + quadDir + "a.tile.gvsig");

		if (f.exists()) {
			if (f.length() <= 0) {
				log.debug("Deleting invalid file");
				f.delete();
				f.createNewFile();
			}
		} else {
//			String path = f.getAbsolutePath();
//			path = path.substring(0, path.lastIndexOf("/"));
//			final File f1 = new File(path);
			log.debug("mkdirs: " );
			File dir = new File(f.getParent());
			if (dir.mkdirs()) {
				log.debug("directories created: " + aURLString);
			} else {
				if (f.getParent() == null) {
					log.debug("parent = null");
				}
				log.debug("directories failed " + aURLString);
			}
			
			if (f.getParentFile().exists() && f.createNewFile()) {
				log.debug("new file created " + aURLString);
			} else {
				log.debug("new file failed " + aURLString);
			}
//			path = null;
			
		}
		final FileOutputStream fos = new FileOutputStream(f);
		final BufferedOutputStream bos = new BufferedOutputStream(fos,
				Utils.IO_BUFFER_SIZE);

		try {
			log.debug("prepared to write " + aURLString);
			bos.write(someData);
			bos.flush();
			bos.close();
			log.debug("Tile stored " + aURLString);
		} catch (Exception e) {
			log.error("save file", e);
		}

		try {
			fos.close();
		} catch (Exception e) {
			log.error("save file", e);
		}
		// datas = someData;
	}
	
	public void destroy() {
		try {			
			
		} catch (Exception e) {
			log.error(e);
		}
	}
}
