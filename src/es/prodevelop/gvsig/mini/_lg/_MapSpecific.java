package es.prodevelop.gvsig.mini._lg;

import java.util.logging.Level;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Settings;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.IEvent;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.tasks.tiledownloader.TileDownloadCallbackHandler;
import es.prodevelop.gvsig.mini.tasks.tiledownloader.TileDownloadWaiter;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import es.prodevelop.tilecache.IDownloadWaiter;
import es.prodevelop.tilecache.TileDownloaderTask;
import es.prodevelop.tilecache.generator.impl.FitScreenBufferStrategy;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.provider.TileProvider;
import es.prodevelop.tilecache.provider.filesystem.strategy.ITileFileSystemStrategy;
import es.prodevelop.tilecache.provider.filesystem.strategy.impl.FileSystemStrategyManager;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.util.ConstantsTileCache;
import es.prodevelop.tilecache.util.Utilities;


// Map specific (more specific than other gvSIG mini stuff
// Here for example: download maps.
public class _MapSpecific extends _MapUI implements IDownloadWaiter {

	private TileDownloaderTask t;
	private SeekBar downTilesSeekBar;
	private TextView totalTiles;
	private TextView totalMB;
	private TextView totalZoom;

	private AlertDialog downloadTileAlert;
	private Cancellable downloadCancellable;
	private Button downloadTilesButton;
	
	private LinearLayout downloadTilesLayout;
	private ProgressBar downloadTilesPB;
	
	
	private TileDownloadWaiterDelegate tileWaiter;

	// tileWaiter = new TileDownloadWaiterDelegate(this);

	private class TileDownloadWaiterDelegate extends TileDownloadWaiter {

		public TileDownloadWaiterDelegate(IDownloadWaiter w) {
			super(w);
			// TODO Auto-generated constructor stub
		}

	}

	
	
	private void instantiateTileDownloaderTask(LinearLayout l, int progress) {
		try {
			final boolean updateTiles = ((CheckBox) l
					.findViewById(R.id.download_tiles_overwrite_check))
					.isChecked();

			MapRenderer currentRenderer = _MapSpecific.this.osmap.getMRendererInfo();
			MapRenderer renderer = Layers.getInstance().getRenderer(
					currentRenderer.getNAME());

			String tileName = "tile.gvsig";
			String dot = ".";
			String strategy = ITileFileSystemStrategy.FLATX;
			ITileFileSystemStrategy ts = FileSystemStrategyManager
					.getInstance().getStrategyByName(strategy);

			try {
				tileName = Settings.getInstance().getStringValue(
						getText(R.string.settings_key_tile_name).toString());
			} catch (Exception e) {

			}

			try {
				strategy = Settings.getInstance()
						.getStringValue(
								getText(R.string.settings_key_list_strategy)
										.toString());
			} catch (Exception e) {

			}

			String tileSuffix = dot + tileName;
			ts = FileSystemStrategyManager.getInstance().getStrategyByName(
					strategy);
			ts.setTileNameSuffix(tileSuffix);

			int fromZoomLevel = currentRenderer.getZoomLevel();

			int totalZoomLevels = currentRenderer.getZOOM_MAXLEVEL()
					- fromZoomLevel;

			int z = currentRenderer.getZOOM_MAXLEVEL()
					- currentRenderer.getZoomMinLevel();

			int toZoomLevel = (totalZoomLevels * progress / 100)
					+ fromZoomLevel;

			_MapSpecific.this.totalZoom.setText(_MapSpecific.this
					.getText(R.string.download_tiles_05)
					+ " "
					+ fromZoomLevel
					+ "/" + toZoomLevel);

			Extent extent = ViewPort
					.calculateExtent(currentRenderer.getCenter(),
							currentRenderer.resolutions[currentRenderer
									.getZoomLevel()], TileRaster.mapWidth,
							TileRaster.mapHeight);

			if (extent.area() < currentRenderer.getExtent().area())
				renderer.setExtent(extent);

			/*
			 * Once we set the extent, we have to update the center of the
			 * MapRenderer
			 */
			renderer.setCenter(renderer.getExtent().getCenter().getX(),
					renderer.getExtent().getCenter().getY());

			/*
			 * Get a new Cancellable instance (one different each time the task
			 * is executed)
			 */
			downloadCancellable = Utilities.getNewCancellable();

			/*
			 * Instantiate the download waiter. The implementation should
			 * properly update the UI as the task is processing tiles
			 */

			TileDownloadCallbackHandler callBackHandler = new TileDownloadCallbackHandler(
					_MapSpecific.this);
			ConstantsTileCache.DEFAULT_NUM_THREADS = 4;

			/*
			 * Finally instantiate the TileDownloaderTask. Automatically
			 * launches to the IDownloadWaiter implementation the
			 * onTotalNumTilesRetrieved event (prior to execute the task)
			 * 
			 * This instance will donwload from 0 to 3 zoom level of the whole
			 * world extent of OSM (Mapnik) server. Not apply any ITileSorter
			 * nor ITileBufferIntersector and applies the FlatX file system
			 * strategy with a minimum buffer to fit the map size.
			 */
			int mode = TileProvider.MODE_ONLINE;
			if (updateTiles) {
				mode = TileProvider.MODE_UPDATE;
			}
			t = new TileDownloaderTask(CompatManager.getInstance()
					.getRegisteredContext(), renderer, fromZoomLevel,
					toZoomLevel, downloadCancellable, renderer.getExtent(),
					null, callBackHandler, null, mode, ts,
					new FitScreenBufferStrategy(), true);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	
	/**
	 * Show an AlertDialog to the user to input the query string for NameFinder
	 * addresses
	 */
	public void showDownloadTilesDialog() {
		try {
			this.osmap.pauseDraw();
			log.log(Level.FINE, "show address dialog");
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			final LinearLayout l = (LinearLayout) this.getLayoutInflater()
					.inflate(R.layout.download_tiles, null);
			this.totalMB = (TextView) l
					.findViewById(R.id.download_total_transfer_text);
			this.totalTiles = (TextView) l
					.findViewById(R.id.download_total_tiles_text);
			this.totalZoom = (TextView) l
					.findViewById(R.id.download_zoom_level_text);
			this.downTilesSeekBar = (SeekBar) l
					.findViewById(R.id.download_zoom_level_seekbar);
			this.downTilesSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							try {
								_MapSpecific.this.instantiateTileDownloaderTask(l,
										progress);

							} catch (Exception e) {
								log.log(Level.SEVERE, e.getMessage());
							}
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}
					});
			alert.setIcon(R.drawable.layerdonwload);
			alert.setTitle(R.string.download_tiles_14);
			this.downTilesSeekBar.setProgress(50);
			((CheckBox) l.findViewById(R.id.download_tiles_overwrite_check))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							_MapSpecific.this.instantiateTileDownloaderTask(l,
									_MapSpecific.this.downTilesSeekBar.getProgress());
						}
					});

			alert.setView(l);

			alert.setPositiveButton(R.string.download_tiles_14,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								_MapSpecific.this.resetCounter();
								_MapSpecific.this.downloadTileAlert = _MapSpecific.this
										.getDownloadTilesDialog();
								_MapSpecific.this.downloadTileAlert.show();
								WorkQueue.getExclusiveInstance().execute(t);
							} catch (Exception e) {
								log.log(Level.SEVERE,
										"clickNameFinderAddress: ", e);
							} finally {
								// setRequestedOrientation(ActivityInfo.
								// SCREEN_ORIENTATION_SENSOR);
							}
							return;
						}
					});

			alert.setNegativeButton(R.string.alert_dialog_text_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// setRequestedOrientation(ActivityInfo.
							// SCREEN_ORIENTATION_SENSOR);
							_MapSpecific.this.osmap.resumeDraw();
							_MapSpecific.this.reloadLayerAfterDownload();
						}
					});

			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			alert.show();
		} catch (Exception e) {
			this.osmap.resumeDraw();
			log.log(Level.SEVERE, "", e);
		}
	}

	

	private AlertDialog getDownloadTilesDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg2.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					Toast.makeText(_MapSpecific.this, R.string.download_tiles_13,
							Toast.LENGTH_LONG).show();
				}
				return true;
			}

		});

		builder.setIcon(R.drawable.layerdonwload);
		builder.setTitle(R.string.download_tiles_14);

		this.downloadTilesLayout = (LinearLayout) this.getLayoutInflater()
				.inflate(R.layout.download_tiles_pd, null);

		downloadTilesPB = (ProgressBar) this.downloadTilesLayout
				.findViewById(R.id.ProgressBar01);

		downloadTilesButton = (Button) this.downloadTilesLayout
				.findViewById(R.id.download_tiles_button);

		downloadTilesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				_MapSpecific.this.downloadCancellable.setCanceled(true);
				_MapSpecific.this.downloadTileAlert.dismiss();
			}
		});

		builder.setView(this.downloadTilesLayout);

		return builder.create();
	}
	
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
	

	

	private int previousTime = 0;

	private synchronized void updateDownloadTilesDialog() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					int time = (int) ((System.currentTimeMillis() - tileWaiter
							.getInitDownloadTime()) / 1000);

					if (time - previousTime < 1 && !isDownloadTilesFinished) {
						return;
					}

					if (tileWaiter.getInitDownloadTime() > 0)
						previousTime = time;

					_MapSpecific.this.downloadTilesPB.setMax((int) tileWaiter
							.getTotalTilesToProcess());
					_MapSpecific.this.downloadTilesPB.setProgress((int) tileWaiter
							.getDownloadedNow());
					long totalDownloaded = _MapSpecific.this.tileWaiter
							.getDownloadedNow();
					long total = _MapSpecific.this.tileWaiter.getTotalTilesToProcess();
					int perc = 0;
					if (total != 0)
						perc = (int) ((double) totalDownloaded / (double) total * 100.0);

					((TextView) _MapSpecific.this.downloadTilesLayout
							.findViewById(R.id.download_perc_text))
							.setText(perc + "%" + " - "
									+ tileWaiter.getTilesDownloaded() + "-"
									+ tileWaiter.getTilesFailed() + "-"
									+ tileWaiter.getTilesFromFS() + "-"
									+ tileWaiter.getTilesSkipped() + "-"
									+ tileWaiter.getTilesDeleted() + "-"
									+ tileWaiter.getTilesNotFound() + "-" + "/"
									+ total);

					String downloadedMB = String.valueOf(_MapSpecific.this.tileWaiter
							.getBytesDownloaded() / 1024 / 1024);

					if (downloadedMB.length() > 4) {
						downloadedMB = downloadedMB.substring(0, 4);
					}

					((TextView) _MapSpecific.this.downloadTilesLayout
							.findViewById(R.id.downloaded_mb_text))
							.setText(_MapSpecific.this
									.getText(R.string.download_tiles_09)
									+ " "
									+ downloadedMB + " MB");

					String elapsed = es.prodevelop.gvsig.mini.utiles.Utilities
							.getTimeHoursMinutesSecondsString(time);

					((TextView) _MapSpecific.this.downloadTilesLayout
							.findViewById(R.id.download_time_text))
							.setText(_MapSpecific.this
									.getText(R.string.download_tiles_10)
									+ " "
									+ elapsed);

					if (totalDownloaded == 0)
						totalDownloaded = 1;

					int estimated = (int) (total * time / totalDownloaded)
							- time;

					String estimatedTime = es.prodevelop.gvsig.mini.utiles.Utilities
							.getTimeHoursMinutesSecondsString(estimated);
					((TextView) _MapSpecific.this.downloadTilesLayout
							.findViewById(R.id.download_time_estimated_text))
							.setText(_MapSpecific.this
									.getText(R.string.download_tiles_11)
									+ " "
									+ estimatedTime);
				}
			});
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXx
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public double getBytesDownloaded() {
		return tileWaiter.getBytesDownloaded();
	}

	public IDownloadWaiter getDownloadWaiter() {
		return tileWaiter.getDownloadWaiter();
	}

	public long getDownloadedNow() {
		return tileWaiter.getDownloadedNow();
	}

	public long getEndDownloadTime() {
		return tileWaiter.getEndDownloadTime();
	}

	public Object getHandler() {
		return tileWaiter.getHandler();
	}

	public long getInitDownloadTime() {
		return tileWaiter.getInitDownloadTime();
	}

	public int getTilesDeleted() {
		return tileWaiter.getTilesDeleted();
	}

	public int getTilesDownloaded() {
		return tileWaiter.getTilesDownloaded();
	}

	public int getTilesFailed() {
		return tileWaiter.getTilesFailed();
	}

	public int getTilesFromFS() {
		return tileWaiter.getTilesFromFS();
	}

	public int getTilesNotFound() {
		return tileWaiter.getTilesNotFound();
	}

	public int getTilesSkipped() {
		return tileWaiter.getTilesSkipped();
	}

	public long getTotalTilesToProcess() {
		return tileWaiter.getTotalTilesToProcess();
	}

	public long getTotalTime() {
		return tileWaiter.getTotalTime();
	}

	public void onDownloadCanceled() {
		tileWaiter.onDownloadCanceled();
		runOnUiThread(new Runnable() {
			public void run() {
				_MapSpecific.this.enableGPS();
				Toast.makeText(_MapSpecific.this, R.string.download_tiles_12,
						Toast.LENGTH_LONG).show();
				_MapSpecific.this.reloadLayerAfterDownload();
			}
		});
	}

	private void reloadLayerAfterDownload() {
		_MapSpecific.this.osmap.resumeDraw();
		_MapSpecific.this.osmap.onLayerChanged(_MapSpecific.this.osmap.getMRendererInfo()
				.getFullNAME());
		try {
			_MapSpecific.this.osmap.initializeCanvas(TileRaster.mapWidth,
					TileRaster.mapHeight);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onFailDownload(IEvent event) {
		tileWaiter.onFailDownload(event);
		this.updateDownloadTilesDialog();
	}

	public void onFatalError(IEvent event) {
		tileWaiter.onFatalError(event);
		this.updateDownloadTilesDialog();
	}

	public void onFinishDownload() {
		runOnUiThread(new Runnable() {
			public void run() {
				_MapSpecific.this.enableGPS();
				isDownloadTilesFinished = true;
				_MapSpecific.this.downloadTilesButton
						.setText(R.string.alert_dialog_text_ok);
				_MapSpecific.this.reloadLayerAfterDownload();
			}
		});
	}

	public void onNewMessage(int ID, IEvent event) {
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
			this.onTileDownloaded(event);
			break;
		case IContext.TOTAL_TILES_COUNT:
			this.onTotalNumTilesRetrieved(Integer.valueOf(event.getMessage()));
			break;
		}
	}

	private boolean isDownloadTilesFinished = false;

	public void onStartDownload() {
		this.osmap.getMTileProvider().clearPendingQueue();
		this.disableGPS();
		isDownloadTilesFinished = false;
		this.previousTime = 0;
		tileWaiter.onStartDownload();
		runOnUiThread(new Runnable() {
			public void run() {
				_MapSpecific.this.downloadTilesButton
						.setText(R.string.alert_dialog_cancel);
			}
		});
	}

	public void onTileDeleted(IEvent event) {
		tileWaiter.onTileDeleted(event);
	}

	public void onTileDownloaded(IEvent event) {
		tileWaiter.onTileDownloaded(event);
		this.updateDownloadTilesDialog();
	}

	public void onTileLoadedFromFileSystem(IEvent event) {
		tileWaiter.onTileLoadedFromFileSystem(event);
		this.updateDownloadTilesDialog();
	}

	public void onTileNotFound(IEvent event) {
		tileWaiter.onTileNotFound(event);
		this.updateDownloadTilesDialog();
	}

	public void onTileSkipped(IEvent event) {
		tileWaiter.onTileSkipped(event);
		this.updateDownloadTilesDialog();
	}

	public void onTotalNumTilesRetrieved(final long totalNumTiles) {
		runOnUiThread(new Runnable() {
			public void run() {
				_MapSpecific.this.tileWaiter.onTotalNumTilesRetrieved(totalNumTiles);
				_MapSpecific.this.totalTiles.setText(_MapSpecific.this
						.getText(R.string.download_tiles_06)
						+ " "
						+ totalNumTiles);
				double totalMB = totalNumTiles * 10 / 1024;
				_MapSpecific.this.totalMB.setText(_MapSpecific.this
						.getText(R.string.download_tiles_07) + " " + totalMB);
			}
		});
	}

	public void resetCounter() {
		tileWaiter.resetCounter();
		this.updateDownloadTilesDialog();
	}

	public void updateDataTransfer(int size) {
		tileWaiter.updateDataTransfer(size);
	}

	
}
