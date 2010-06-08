package es.prodevelop.gvsig.mini.tasks.tiledownloader;

import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.tilecache.IDownloadWaiter;

/**
 * This class receives notifications of the TileGenerator download process. It's
 * a basic implemntation that basically prints on screen each event received.
 * 
 * It also tracks the number of tilesDownloaded, failed, skipped and loaded from
 * the file system and the total number of bytes downloaded by the
 * TileGenerator. The bytes downloaded represent the size of the data received
 * from the server not the space no disk needed to store the tiles, that can
 * differ a lot depending on the min cluster size of the HDD
 * 
 * @author aromeu
 */
public class TileDownloadWaiter implements IDownloadWaiter, GeoUtils {

	private int tilesDownloaded = 0;
	private int tilesFailed = 0;
	private int tilesSkipped = 0;
	private int tilesFromFS = 0;
	private int tilesNotFound = 0;
	private int tilesDeleted = 0;
	private double bytesDownloaded = 0;
	private long totalTilesToProcess = 0;

	private long initDownloadTime = 0;
	private long endDownloadTime = 0;
	private IDownloadWaiter waiter = null;	

	public TileDownloadWaiter(IDownloadWaiter w) {
		this.waiter = w;
	}

	/**
	 * Check if the number of tiles downloaded, failed, skipped, and loaded from
	 * fileSystem is equals to the number of tiles to be processed by the
	 * TileGenerator
	 * 
	 * @return True if both numbers are equals
	 */
	private boolean checkHasFinished() {
		if (tilesDownloaded + tilesFailed + tilesSkipped + tilesFromFS
				+ tilesDeleted + tilesNotFound == totalTilesToProcess) {
			return true;
		}
		return false;
	}

	public void onDownloadCanceled() {
		System.out.println("onDownloadCanceled");
	}

	public void onFinishDownload() {
		endDownloadTime = System.currentTimeMillis();
		System.out.println("onFinishDownload");
		System.out.println("tilesDownloaded: " + tilesDownloaded);
		System.out.println("tilesFailed: " + tilesFailed);
		System.out.println("tilesSkipped: " + tilesSkipped);
		System.out.println("tilesFromFS: " + tilesFromFS);
		System.out.println("tilesNotFound: " + tilesNotFound);
		System.out.println("tilesDeleted: " + tilesDeleted);
		System.out.println("bytesDownloaded: " + bytesDownloaded);
		System.out.println("totalTime: "
				+ ((endDownloadTime - initDownloadTime) / 1000) + " s.");
		this.waiter.onFinishDownload();
	}

	public void onStartDownload() {

		initDownloadTime = System.currentTimeMillis();
		System.out.println("onStartDownload");
	}

	public void onTileDownloaded(String URL) {
		tilesDownloaded++;
//		System.out.println(tilesDownloaded + "-" + URL);
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public void onTotalNumTilesRetrieved(long totalNumTiles) {
		System.out.println("onTotalNumTilesRetrieved: " + totalNumTiles);
		totalTilesToProcess = totalNumTiles;
	}

	public Object getHandler() {
		return this;
	}

	public void onNewMessage(int ID, String message) {
		switch (ID) {
		case IContext.DOWNLOAD_CANCELED:
			this.onDownloadCanceled();
			break;
		case IContext.FINISH_DOWNLOAD:
			this.onFinishDownload();
			break;
		case IContext.START_DOWNLOAD:
			this.onStartDownload();
			break;
		case IContext.TILE_DOWNLOADED:
			this.onTileDownloaded(message);
			break;
		case IContext.TOTAL_TILES_COUNT:
			this.onTotalNumTilesRetrieved(Integer.valueOf(message));
			break;
		}
	}

	public void onFailDownload(String URL) {
		System.out.println("onFailDownload: " + URL);
		this.tilesFailed++;
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public void onFatalError(String URL) {
		System.out.println("onFatalError: " + URL);
		this.tilesFailed++;
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public void onTileLoadedFromFileSystem(String URL) {
//		System.out.println("onTileLoadedFromFileSystem: " + URL);
		this.tilesFromFS++;
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public void updateDataTransfer(int size) {
		bytesDownloaded += size;
//		System.out.println("Total bytes downloaded: " + bytesDownloaded
//				/ 1024.0);
	}

	public IDownloadWaiter getDownloadWaiter() {
		return this;
	}

	public void onTileSkipped(String URL) {
//		System.out.println("onTileSkipped: " + URL);
		this.tilesSkipped++;
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public void onTileDeleted(String URL) {
//		System.out.println("onTileDeleted: " + URL);
		this.tilesDeleted++;
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public void onTileNotFound(String URL) {
		System.out.println("onTileNotFound: " + URL);
		this.tilesNotFound++;
		if (this.checkHasFinished()) {
			this.onFinishDownload();
		}
	}

	public int getTilesDownloaded() {
		return tilesDownloaded;
	}

	public int getTilesFailed() {
		return tilesFailed;
	}

	public int getTilesSkipped() {
		return tilesSkipped;
	}

	public int getTilesFromFS() {
		return tilesFromFS;
	}

	public int getTilesNotFound() {
		return tilesNotFound;
	}

	public int getTilesDeleted() {
		return tilesDeleted;
	}

	public double getBytesDownloaded() {
		return bytesDownloaded;
	}

	public long getTotalTilesToProcess() {
		return totalTilesToProcess;
	}

	public long getInitDownloadTime() {
		return initDownloadTime;
	}

	public long getEndDownloadTime() {
		return endDownloadTime;
	}

	public long getDownloadedNow() {
		return tilesDownloaded + tilesFailed + tilesSkipped + tilesFromFS
				+ tilesDeleted + tilesNotFound;
	}

	public long getTotalTime() {
		return endDownloadTime - initDownloadTime;
	}

	public void resetCounter() {
		this.tilesDownloaded = 0;
		this.tilesFailed = 0;
		this.tilesSkipped = 0;
		this.tilesFromFS = 0;
		this.tilesNotFound = 0;
		this.tilesDeleted = 0;
		this.bytesDownloaded = 0;		
		this.initDownloadTime = 0;
		this.endDownloadTime = 0;
	}

}
