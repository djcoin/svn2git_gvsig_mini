package es.prodevelop.gvsig.mini.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.IBitmap;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.android.HandlerAndroid;
import es.prodevelop.gvsig.mini.common.impl.Tile;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;
import es.prodevelop.gvsig.mini.views.overlay.Paints;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.provider.Downloader;
import es.prodevelop.tilecache.provider.TileProvider;
import es.prodevelop.tilecache.provider.filesystem.impl.TileFilesystemProvider;
import es.prodevelop.tilecache.provider.filesystem.strategy.ITileFileSystemStrategy;
import es.prodevelop.tilecache.provider.filesystem.strategy.impl.FileSystemStrategyManager;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.OSMMercatorRenderer;
import es.prodevelop.tilecache.util.Utilities;

public class MapPreview extends View {

	int mapWidth = 300;
	int mapHeight = 200;
	int offset = 10;
	Paint normalPaint = new Paint();
	Paint markerPaint = new Paint();
	Cancellable cancellable = Utilities.getNewCancellable();
	private TileProvider mTileProvider;

	MapRenderer mRendererInfo = OSMMercatorRenderer.getMapnikRenderer();
	Bitmap PERSON_ICON;
	Bitmap START;
	android.graphics.Point START_SPOT;

	public MapPreview(Context context, IContext androidContext, int width,
			int height) {
		super(context);
		mapWidth = width;
		mapHeight = height;
		markerPaint.setColor(Color.RED);
		markerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		markerPaint.setStrokeWidth(2);
		Handler lh = new LoadCallbackHandler(new SimpleInvalidationHandler());
		int mode = TileProvider.MODE_ONLINE;
		String tileName = "tile.gvSIG";
		String dot = ".";
		String strategy = ITileFileSystemStrategy.FLATX;
		ITileFileSystemStrategy t = FileSystemStrategyManager.getInstance()
				.getStrategyByName(strategy);

		String tileSuffix = dot + tileName;
		t = FileSystemStrategyManager.getInstance().getStrategyByName(strategy);
		t.setTileNameSuffix(tileSuffix);

		this.mTileProvider = new TileProvider(androidContext,
				new HandlerAndroid(lh), mapWidth, mapHeight, 256, mode, t);

		mRendererInfo.setZoomLevel(16);

		PERSON_ICON = ResourceLoader.getBitmap(R.drawable.arrowdown);
		this.START = ResourceLoader.getBitmap(R.drawable.startpoi);
		START_SPOT = new android.graphics.Point(START.getWidth()
				- PERSON_ICON.getHeight() / 2, START.getHeight()
				- PERSON_ICON.getHeight() / 4);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec,
			final int heightMeasureSpec) {
		super.onMeasure(mapWidth, mapHeight);
		setMeasuredDimension(mapWidth, mapHeight);
	}

	public void setMapCenterFromLonLat(Point lonLat) {
		WorkQueue.getInstance().clearPendingTasks();
		double[] coords = ConversionCoords.reproject(lonLat.getX(),
				lonLat.getY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS(this.mRendererInfo.getSRS()));
		mRendererInfo.setCenter(coords[0], coords[1]);

		// final Extent mapExtent = ViewPort.calculateExtent(this.mRendererInfo
		// .getCenter(), this.mRendererInfo.resolutions[this.mRendererInfo
		// .getZoomLevel()], mapWidth, mapHeight);
		//
		// setExtent(mapExtent);
	}

	public void setExtent(Extent e) {
		double[] coordsMin = ConversionCoords.reproject(e.getMinX(),
				e.getMinY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS(this.mRendererInfo.getSRS()));
		double[] coordsMax = ConversionCoords.reproject(e.getMaxX(),
				e.getMaxY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS(this.mRendererInfo.getSRS()));
		Extent ex = new Extent(coordsMin[0], coordsMin[1], coordsMax[0],
				coordsMax[1]);
		int z = this.getZoomLevelFitsExtent(ex.getWidth(), ex.getHeight(),
				mRendererInfo.getZoomLevel());

		if (z != -1)
			mRendererInfo.setZoomLevel(z);
//		else if (z > mRendererInfo.getZOOM_MAXLEVEL())
//			mRendererInfo.setZoomLevel(mRendererInfo.getZOOM_MAXLEVEL());
		else
			mRendererInfo.setZoomLevel(16);
	}

	public int getZoomLevelFitsExtent(final double width, final double height,
			final int currentZoomLevel) {
		final Extent mapExtent = ViewPort.calculateExtent(this.mRendererInfo
				.getCenter(), this.mRendererInfo.resolutions[this.mRendererInfo
				.getZoomLevel()], mapWidth, mapHeight);
		final int curZoomLevel = currentZoomLevel;

		final double curWidth = mapExtent.getWidth();
		final double curHeight = mapExtent.getHeight();

		final double diffNeededX = width / curWidth;
		final double diffNeededY = height / curHeight;

		if (Double.isInfinite(diffNeededX) || Double.isInfinite(diffNeededY)) {
			return -1;
		}

		final double diffNeeded = Math.max(diffNeededX, diffNeededY);

		if (diffNeeded > 1) {
			return curZoomLevel - Utils.getNextSquareNumberAbove(diffNeeded);
		} else if (diffNeeded < 0.5) {
			return curZoomLevel
					+ Utils.getNextSquareNumberAbove(1 / diffNeeded) - 1;
		} else {
			return curZoomLevel;
		}
	}

	@Override
	public void onDraw(final Canvas c) {

		try {

			MapRenderer renderer = this.mRendererInfo;

			if (renderer.getCenter().getX() == 0)
				return;
			final int zoomLevel = renderer.getZoomLevel();

			ViewPort.mapWidth = mapWidth;
			ViewPort.mapHeight = mapHeight;
			final int viewWidth = this.mapWidth;
			final int viewHeight = this.mapHeight;
			// this.centerPixelX = this.getWidth() / 2;
			// this.centerPixelY = this.getHeight() / 2;
			final int tileSizePx = renderer.getMAPTILE_SIZEPX();

			int[] centerMapTileCoords = renderer.getMapTileFromCenter();

			final int additionalTilesNeededToLeftOfCenter;
			final int additionalTilesNeededToRightOfCenter;
			final int additionalTilesNeededToTopOfCenter;
			final int additionalTilesNeededToBottomOfCenter;

			Pixel upperLeftCornerOfCenterMapTile = renderer
					.getUpperLeftCornerOfCenterMapTileInScreen(null);

			final int centerMapTileScreenLeft = upperLeftCornerOfCenterMapTile
					.getX();
			final int centerMapTileScreenTop = upperLeftCornerOfCenterMapTile
					.getY();

			final int centerMapTileScreenRight = centerMapTileScreenLeft
					+ tileSizePx;
			final int centerMapTileScreenBottom = centerMapTileScreenTop
					+ tileSizePx;

			additionalTilesNeededToLeftOfCenter = (int) Math
					.ceil((float) centerMapTileScreenLeft / tileSizePx);
			additionalTilesNeededToRightOfCenter = (int) Math
					.ceil((float) (viewWidth - centerMapTileScreenRight)
							/ tileSizePx);
			additionalTilesNeededToTopOfCenter = (int) Math
					.ceil((float) centerMapTileScreenTop / tileSizePx);
			additionalTilesNeededToBottomOfCenter = (int) Math
					.ceil((float) (viewHeight - centerMapTileScreenBottom)
							/ tileSizePx);

			final int[] mapTileCoords = new int[] { centerMapTileCoords[0],
					centerMapTileCoords[1] };

			final int size = (additionalTilesNeededToBottomOfCenter
					+ additionalTilesNeededToTopOfCenter + 1)
					* (additionalTilesNeededToRightOfCenter
							+ additionalTilesNeededToLeftOfCenter + 1);
			Tile[] tiles = new Tile[size];
			int cont = 0;

			final Extent maxExtent = renderer.getExtent();
			final Extent viewExtent = ViewPort.calculateExtent(
					renderer.getCenter(),
					renderer.resolutions[renderer.getZoomLevel()], mapWidth,
					mapHeight);
			// this.getMTileProvider().setViewExtent(viewExtent);

			final String layerName = renderer.getNAME();

			boolean process = true;

			for (int y = -additionalTilesNeededToTopOfCenter; y <= additionalTilesNeededToBottomOfCenter; y++) {
				for (int x = -additionalTilesNeededToLeftOfCenter; x <= additionalTilesNeededToRightOfCenter; x++) {
					process = true;
					if (viewExtent.intersect(maxExtent)) {
						final int tileLeft = centerMapTileScreenLeft
								+ (x * tileSizePx);
						final int tileTop = centerMapTileScreenTop
								+ (y * tileSizePx);

						double[] coords = new double[] {
								maxExtent.getLefBottomCoordinate().getX(),
								maxExtent.getLefBottomCoordinate().getY() };

						final int[] leftBottom = renderer.toPixels(coords);
						coords = new double[] {
								maxExtent.getRightTopCoordinate().getX(),
								maxExtent.getRightTopCoordinate().getY() };
						final int[] rightTop = renderer.toPixels(coords);

						final Rect r = new Rect();
						r.bottom = leftBottom[1];
						r.left = leftBottom[0];
						r.right = rightTop[0];
						r.top = rightTop[1];

						process = r.intersects(tileLeft, tileTop, tileLeft
								+ tileSizePx / 2, tileTop + tileSizePx / 2);

					}

					if (process) {
						mapTileCoords[0] = centerMapTileCoords[0]
								+ this.mRendererInfo.isTMS() * y;

						mapTileCoords[1] = centerMapTileCoords[1] + x;

						final String tileURLString = this.mRendererInfo
								.getTileURLString(mapTileCoords, zoomLevel);

						final int[] tile = new int[] { mapTileCoords[0],
								mapTileCoords[1] };
						final int tileLeft = centerMapTileScreenLeft
								+ (x * tileSizePx);
						final int tileTop = centerMapTileScreenTop
								+ (y * tileSizePx);

						// final Extent ext =
						// TileConversor.tileMeterBounds(tile[0], tile[1],
						// resolution, -originX, -originY);
						// System.out.println("Tile: " + tile[0] + ", " +
						// tile[1] + "Extent: " + ext.toString());
						final Tile t = new Tile(tileURLString, tile, new Pixel(
								tileLeft, tileTop), zoomLevel, layerName,
								this.cancellable, null);
						if (cont < tiles.length)
							tiles[cont] = t;

					}
					cont++;
				}
			}

			this.sortTiles(tiles);
			final int length = tiles.length;
			Tile temp;
			// c.drawRect(0, 0, mapWidth, mapHeight, Paints.whitePaint);
			// bufferCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
			c.clipRect(offset, offset, mapWidth - offset, mapHeight - offset);
			for (int j = 0; j < length; j++) {
				temp = tiles[j];
				if (temp != null) {
					final IBitmap currentMapTile = this.mTileProvider
							.getMapTile(temp, cancellable, null);
					if (currentMapTile != null) {
						c.drawBitmap((Bitmap) currentMapTile.getBitmap(),
								temp.distanceFromCenter.getX(),
								temp.distanceFromCenter.getY(), normalPaint);

						temp = null;
					}
				}
			}
			tiles = null;

			final double res = renderer.resolutions[renderer.getZoomLevel()];
			;

			int[] result = ViewPort.fromMapPoint(new double[] {
					renderer.getCenter().getX(), renderer.getCenter().getY() },
					viewExtent.getMinX(), viewExtent.getMaxY(), res);

			c.drawBitmap(START, result[0] - START_SPOT.x, result[1]
					- START_SPOT.y, Paints.mPaintR);
			// c.drawCircle(result[0], result[1], 5, markerPaint);
		} catch (Exception e) {

		}
	}

	/**
	 * Sorts the tiles from the center of the screen to outside to load the
	 * tiles in spiral.
	 * 
	 * @param array2
	 *            the array of tiles
	 */
	private void sortTiles(final Tile[] array2) {
		try {
			final Pixel center = new Pixel(mapWidth / 2, mapHeight / 2);
			double e1, e2;
			final int length = array2.length;
			Tile t;
			Tile t1;
			final Pixel temp = new Pixel(
					(this.mRendererInfo.getMAPTILE_SIZEPX() / 2),
					(this.mRendererInfo.getMAPTILE_SIZEPX() / 2));
			for (int pass = 1; pass < length; pass++) {
				for (int element = 0; element < length - 1; element++) {
					t = array2[element];
					t1 = array2[element + 1];
					if (t != null && t1 != null) {
						e1 = (t.distanceFromCenter.add(temp)).distance(center);
						e2 = (t1.distanceFromCenter.add(temp)).distance(center);
						if (e1 > e2) {
							swap(array2, element, element + 1);
						}
					}
				}
			}
		} catch (final Exception e) {

		}
	}

	private void swap(final Tile[] array3, final int first, final int second) {
		try {
			final Tile hold = array3[first];

			array3[first] = array3[second];
			array3[second] = hold;
		} catch (final Exception e) {

		}
	}

	private class SimpleInvalidationHandler extends Handler {

		@Override
		public void handleMessage(final Message msg) {
			try {
				switch (msg.what) {
				case Downloader.MAPTILEDOWNLOADER_SUCCESS_ID:
					invalidate();
					// if (!Utils.isSDMounted()) {
					// TileRaster.this.invalidate();
					// TileRaster.this.mTileProvider.getMFSTileProvider().getPendingQueue().remove(((TileEvent)msg.obj).getTile().getTileString());
					// }
					break;
				case Downloader.MAPTILEDOWNLOADER_FAIL_ID:
					// bufferCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
					break;
				// case Downloader.REMOVE_CACHE_URL:
				// // if (!Utils.isSDMounted()) {
				// TileRaster.this.getMTileProvider().getDownloader().
				// // }
				// break;
				case TileFilesystemProvider.MAPTILEFSLOADER_FAIL_ID:
					break;
				case TileFilesystemProvider.MAPTILEFSLOADER_SUCCESS_ID:
					invalidate();
					// TileRaster.this.invalidate();
					// TileRaster.this.mTileProvider.getMFSTileProvider().getPendingQueue().remove(((TileEvent)msg.obj).getTile().getTileString());
					break;
				}
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Handler to manage Messages from Downloader and TileFilesystemProvider
	 * 
	 * @author aromeu
	 * @author rblanco
	 * 
	 */
	private class LoadCallbackHandler extends Handler {

		private Handler mDownloadFinishedListenerHander;

		public LoadCallbackHandler(Handler handler) {
			try {
				mDownloadFinishedListenerHander = handler;
			} catch (Exception e) {

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

			}
		}
	}
}
