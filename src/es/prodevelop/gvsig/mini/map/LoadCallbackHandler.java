package es.prodevelop.gvsig.mini.map;

import es.prodevelop.tilecache.provider.Downloader;
import es.prodevelop.tilecache.provider.filesystem.impl.TileFilesystemProvider;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import android.os.Handler;
import android.os.Message;

/**
 * Handler to manage Messages from Downloader and TileFilesystemProvider
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class LoadCallbackHandler extends Handler {

	private Handler mDownloadFinishedListenerHander;
	private final static Logger log = LoggerFactory
			.getLogger(LoadCallbackHandler.class);

	public LoadCallbackHandler(Handler handler) {
		mDownloadFinishedListenerHander = handler;
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
				break;
			case Downloader.MAPTILEDOWNLOADER_OOM_ID:
				// Utils.showSendLogDialog(TileProvider.this.mCtx);
				break;

			case TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID:
				mDownloadFinishedListenerHander
						.sendEmptyMessage(TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID);
				break;
			case TileFilesystemProvider.MAPTILEFSLOADER_FAIL_ID:
				break;
			case TileFilesystemProvider.MAPTILEFSLOADER_OOM_ID:
				// Utils.showSendLogDialog(TileProvider.this.mCtx);
				break;
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
}
