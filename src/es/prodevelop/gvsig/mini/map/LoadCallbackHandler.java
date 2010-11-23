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

package es.prodevelop.gvsig.mini.map;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.tilecache.provider.Downloader;
import es.prodevelop.tilecache.provider.filesystem.impl.TileFilesystemProvider;

/**
 * Handler to manage Messages from Downloader and TileFilesystemProvider
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class LoadCallbackHandler extends Handler {

	private Handler mDownloadFinishedListenerHander;
	private final static Logger log = Logger
			.getLogger(LoadCallbackHandler.class.getName());

	public LoadCallbackHandler(Handler handler) {
		try {
			CompatManager.getInstance().getRegisteredLogHandler()
					.configureLogger(log);
			mDownloadFinishedListenerHander = handler;
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public void handleMessage(final Message msg) {
		try {
			final int what = msg.what;
			switch (what) {
			case Downloader.MAPTILEDOWNLOADER_SUCCESS_ID:
				mDownloadFinishedListenerHander
						.sendEmptyMessage(Downloader.MAPTILEDOWNLOADER_SUCCESS_ID);
				break;
			case Downloader.MAPTILEDOWNLOADER_FAIL_ID:
				mDownloadFinishedListenerHander
						.sendEmptyMessage(Downloader.MAPTILEDOWNLOADER_FAIL_ID);
				break;
			case Downloader.MAPTILEDOWNLOADER_OOM_ID:
				// Utils.showSendLogDialog(TileProvider.this.mCtx);
				break;

			case TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID:
				mDownloadFinishedListenerHander
						.sendEmptyMessage(TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID);
				break;
			case TileFilesystemProvider.MAPTILEFSLOADER_FAIL_ID:
				mDownloadFinishedListenerHander
						.sendEmptyMessage(TileFilesystemProvider.MAPTILEFSLOADER_FAIL_ID);
				break;
			case TileFilesystemProvider.MAPTILEFSLOADER_OOM_ID:
				// Utils.showSendLogDialog(TileProvider.this.mCtx);
				break;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
}
