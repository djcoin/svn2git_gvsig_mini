/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.tasks.tiledownloader;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.IEvent;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.tilecache.IDownloadCallbackHandler;
import es.prodevelop.tilecache.IDownloadWaiter;

/**
 * This class takes the notifications from the TileProvider, process the message
 * ID received and send a new notification to IDownloadWaiter
 * TILEPROVIDER_SKIPPED: onTileSkipped(message); TILEPROVIDER_CANCELED:
 * onDownloadCanceled(); TILEPROVIDER_TOTAL_TILES:
 * onTotalNumTilesRetrieved(Integer.parseInt(message));
 * MAPTILEFSLOADER_SUCCESS_ID: onTileLoadedFromFileSystem(message);
 * MAPTILEFSLOADER_FAIL_ID: onFailDownload(message); MAPTILEFSLOADER_OOM_ID:
 * onFatalError(message); MAPTILEDOWNLOADER_SUCCESS_ID:
 * onNewMessage(IContext.TILE_DOWNLOADED, message); MAPTILEDOWNLOADER_FAIL_ID:
 * onFailDownload(message); MAPTILEDOWNLOADER_OOM_ID: onFatalError(message);
 * MAPTILEDOWNLOADER_SUCCESS_SIZE:
 * updateDataTransfer(Integer.parseInt(message)); MAPTILEFSLOADER_DELETED_ID:
 * onTileDeleted(message) MAPTILEFSLOADER_NOTFOUND_ID: onTileNotFound(message)
 * 
 * @author aromeu
 * 
 */
public class TileDownloadCallbackHandler extends Handler implements
		IDownloadCallbackHandler, GeoUtils {

	private IDownloadWaiter downloadWaiter;

	/**
	 * The constructor. The IDownloadWaiter instance is needed to internally
	 * notify it onNewMessage received from the TileProvider
	 * 
	 * @param downloadWaiter
	 */
	public TileDownloadCallbackHandler(IDownloadWaiter downloadWaiter) {
		this.downloadWaiter = downloadWaiter;
	}

	public void handleMessage(Message m) {
		try {
			if (m == null)
				return;
			IEvent event = null;
			if (m.obj != null) {
				event = (IEvent)m.obj;
			}
			this.onNewMessage(m.what, event);
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}

	public synchronized void onNewMessage(int ID, IEvent event) {
		switch (ID) {
		case TILEPROVIDER_SKIPPED:
			this.downloadWaiter.onTileSkipped(event);
			break;
		case TILEPROVIDER_CANCELED:
			this.downloadWaiter.onDownloadCanceled();
			break;
		case GeoUtils.TILEPROVIDER_TOTAL_TILES:
			this.downloadWaiter.onTotalNumTilesRetrieved(Long
					.parseLong(event.getMessage()));
			break;
		case MAPTILEFSLOADER_SUCCESS_ID:
			this.downloadWaiter.onTileLoadedFromFileSystem(event);
			break;
		case MAPTILEFSLOADER_FAIL_ID:
			this.downloadWaiter.onFailDownload(event);
			break;
		case MAPTILEFSLOADER_OOM_ID:
			this.downloadWaiter.onFatalError(event);
			break;
		case MAPTILEDOWNLOADER_SUCCESS_ID:
			this.downloadWaiter.onNewMessage(IContext.TILE_DOWNLOADED, event);
			break;
		case MAPTILEDOWNLOADER_FAIL_ID:
			this.downloadWaiter.onFailDownload(event);
			break;
		case MAPTILEDOWNLOADER_OOM_ID:
			this.downloadWaiter.onFatalError(event);
			break;
		case MAPTILEDOWNLOADER_SUCCESS_SIZE:
			this.downloadWaiter.updateDataTransfer(Integer.parseInt(event.getMessage()));
			break;
		case MAPTILEFSLOADER_DELETED_ID:
			this.downloadWaiter.onTileDeleted(event);
			break;
		case MAPTILEFSLOADER_NOTFOUND_ID:
			this.downloadWaiter.onTileNotFound(event);
			break;
		}
	}

	public Object getHandler() {
		return this;
	}

	/**
	 * The IDownloadWaiter instance
	 */
	public IDownloadWaiter getDownloadWaiter() {
		return this.downloadWaiter;
	}
}
