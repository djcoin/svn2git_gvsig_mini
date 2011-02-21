package es.prodevelop.gvsig.mini._lg;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ZoomControls;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.MapLocation;
import es.prodevelop.gvsig.mini.activities.Settings;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.common.IEvent;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.tasks.tiledownloader.TileDownloadWaiter;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.views.overlay.NameFinderOverlay;
import es.prodevelop.gvsig.mini.views.overlay.RouteOverlay;
import es.prodevelop.gvsig.mini.views.overlay.SlideBar;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import es.prodevelop.gvsig.mini.views.overlay.ViewSimpleLocationOverlay;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.IDownloadWaiter;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.OSMMercatorRenderer;

public class VanillaMap extends MapLocation implements GeoUtils, IDownloadWaiter {

	protected final static Logger log = Logger.getLogger(Map.class.getName());
	private TileDownloadWaiterDelegate tileWaiter;
	private ViewSimpleLocationOverlay mMyLocationOverlay;
	private TileRaster osmap;
	private SlideBar s;
	public RelativeLayout rl;
	public ZoomControls z = null;
	private boolean recenterOnGPS;
	private boolean navigation;
	private boolean connection;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreate", e);		
		}
			
		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);		
		tileWaiter = new TileDownloadWaiterDelegate(this);

		// onNewIntent(getIntent());		
	}
	
	private class TileDownloadWaiterDelegate extends TileDownloadWaiter {

		public TileDownloadWaiterDelegate(IDownloadWaiter w) {
			super(w);
			// TODO Auto-generated constructor stub
		}

	}
	@Override
	public void onResume() {
		try {
			log.log(Level.FINE, "onResume");
			super.onResume();
			osmap.resumeDraw();
			//if (navigation && recenterOnGPS)
			osmap.setKeepScreenOn(true);			
		} catch (Exception e) {
			log.log(Level.SEVERE, "onResume: ", e);
		}
	}
	
	@Override
	public void onPause() {
		try {
			log.log(Level.FINE, "onPause");
			super.onPause();
			// if (wl != null && wl.isHeld())
			osmap.pauseDraw();
			osmap.setKeepScreenOn(false);

		} catch (Exception e)

		{
			log.log(Level.SEVERE, "onPause: ", e);
		}
	}
	
	public void loadMap(Bundle outState) throws BaseException {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		OSMMercatorRenderer t = OSMMercatorRenderer.getMapnikRenderer();
		
		this.osmap = new TileRaster(this, CompatManager.getInstance()
				.getRegisteredContext(), t, metrics.widthPixels,
				metrics.heightPixels);
		// osmap.initializeCanvas(metrics.widthPixels,
		// metrics.heightPixels);
		
//			ItemContext context = (ItemContext) Class.forName(contextName)
//					.newInstance();
//			if (context != null) {
//				context.setMap(this);
//				this.setContext(context);
//			}
	}
	
	
	/**
	 * Instantiates the UI: TileRaster, ZoomControls, SlideBar in a
	 * RelativeLayout
	 * 
	 * @param savedInstanceState
	 */
	public void loadUI(Bundle savedInstanceState) {
		try {
			log.log(Level.FINE, "load UI");
			final LayoutInflater factory = LayoutInflater.from(this);
			rl.addView(this.osmap, new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			z = new ZoomControls(this);
			final TextView l = new TextView(this);
			final RelativeLayout.LayoutParams sParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			//
			rl.addView(l, sParams);

			/* Creating the main Overlay */
			{
				this.mMyLocationOverlay = new ViewSimpleLocationOverlay(this,
						osmap, ViewSimpleLocationOverlay.DEFAULT_NAME);

				this.osmap.addOverlay(new NameFinderOverlay(this, osmap,
						NameFinderOverlay.DEFAULT_NAME));
				this.osmap.addOverlay(new RouteOverlay(this, osmap,
						RouteOverlay.DEFAULT_NAME));
				this.osmap.addOverlay(mMyLocationOverlay);
			}

			final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			zzParams.addRule(RelativeLayout.ALIGN_BOTTOM);
			zzParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rl.addView(z, zzParams);
			z.setId(107);
			z.setVisibility(View.VISIBLE);

			z.setOnZoomInClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						VanillaMap.this.osmap.zoomIn();

					} catch (Exception e) {
						log.log(Level.SEVERE, "onZoomInClick: ", e);
					}
				}
			});
			z.setOnZoomOutClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						VanillaMap.this.osmap.zoomOut();
						// Map.this.osmap.switchPanMode();
					} catch (Exception e) {
						log.log(Level.SEVERE, "onZoomOutClick: ", e);
					}
				}
			});

			s = new SlideBar(this, this.osmap);

			final RelativeLayout.LayoutParams slideParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			// slideParams.addRule(RelativeLayout.ABOVE,
			// z.getId());
			s.setVisibility(View.INVISIBLE);
			slideParams.addRule(RelativeLayout.ALIGN_TOP);
			slideParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			slideParams.addRule(RelativeLayout.ALIGN_RIGHT);
			slideParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					int zoom = (int) Math.floor(progress
							* (VanillaMap.this.osmap.getMRendererInfo()
									.getZOOM_MAXLEVEL() + 1 - VanillaMap.this.osmap
									.getMRendererInfo().getZoomMinLevel())
							/ 100);
					osmap.drawZoomRectangle(zoom);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					try {
						int zoom = (int) Math.floor(seekBar.getProgress()
								* (VanillaMap.this.osmap.getMRendererInfo()
										.getZOOM_MAXLEVEL() + 1 - VanillaMap.this.osmap
										.getMRendererInfo().getZoomMinLevel())
								/ 100);
						// int zoom = ((SlideBar)seekBar).portions;
						VanillaMap.this.updateSlider(zoom);
						VanillaMap.this.osmap.setZoomLevel(zoom, true);
						VanillaMap.this.osmap.cleanZoomRectangle();
					} catch (Exception e) {
						log.log(Level.SEVERE, "onStopTrackingTouch: ", e);
					}
				}
			});

			rl.addView(s, slideParams);

			/* Controls */
			{
				this.updateSlider();
			}
			log.log(Level.FINE, "ui loaded");
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError ou) {
			System.gc();
			log.log(Level.SEVERE, "", ou);
		}
	}
	
	
	/**
	 * This method sinchronizes the SlideBar position with the current
	 * TileRaster zoom level
	 */
	public void updateSlider() {
		try {
			int progress = Math
					.round(this.osmap.getTempZoomLevel()
							* 100
							/ (this.osmap.getMRendererInfo().getZOOM_MAXLEVEL() + 1 - this.osmap
									.getMRendererInfo().getZoomMinLevel()));
			// if (progress == 0)
			// progress = 1;
			this.s.setProgress(progress);
			this.updateZoomControl();
		} catch (Exception e) {
			log.log(Level.SEVERE, "updateSlider: ", e);
		}
	}

	/**
	 * Forces to update the SlideBar to an specific zoom level
	 * 
	 * @param zoom
	 *            The zoom level
	 */
	public void updateSlider(int zoom) {
		try {
			int progress = zoom
					* 100
					/ (this.osmap.getMRendererInfo().getZOOM_MAXLEVEL() + 1 - this.osmap
							.getMRendererInfo().getZoomMinLevel());
			// if (progress == 0)
			// progress = 1;
			this.s.setProgress(progress);
			this.updateZoomControl();
		} catch (Exception e) {
			log.log(Level.SEVERE, "updateSlider: ", e);
		}
	}
	

	/**
	 * Synchronize zoomcontrols with TileRaster.MapRenderer zoom level
	 */
	public void updateZoomControl() {
		try {
			MapRenderer r = osmap.getMRendererInfo();

			if (r.getZoomLevel() > r.getZoomMinLevel())
				z.setIsZoomOutEnabled(true);
			else
				z.setIsZoomOutEnabled(false);

			if (r.getZOOM_MAXLEVEL() > r.getZoomLevel()) {
				z.setIsZoomInEnabled(true);
			} else {
				z.setIsZoomInEnabled(false);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "updateZoomControl: ", e);
		}
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		try {
			boolean repaint = osmap.getMRendererInfo().getCurrentExtent()
					.contains(this.mMyLocationOverlay.reprojectedCoordinates);

			this.osmap.mBearing = (int) event.values[0];
			long current = System.currentTimeMillis();
			if (!navigation && repaint) {
				osmap.resumeDraw();
			} else {
				if (current - last > Utils.REPAINT_TIME && repaint) {
					osmap.resumeDraw();
					last = current;
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "onSensorChanged: ", e);
		}
	}

	@Override
	public void onLocationChanged(Location pLoc) {
		try {
			if (pLoc == null)
				return;
			log.log(Level.FINE,
					"onLocationChanged (lon, lat): " + pLoc.getLongitude()
							+ ", " + pLoc.getLatitude());
			this.mMyLocationOverlay.setLocation(
					MapLocation.locationToGeoPoint(pLoc), pLoc.getAccuracy(),
					pLoc.getProvider());
			if (recenterOnGPS && pLoc.getLatitude() != 0
					&& pLoc.getLongitude() != 0 && navigation == true) {

				double[] coords = ConversionCoords.reproject(
						pLoc.getLongitude(), pLoc.getLatitude(),
						CRSFactory.getCRS("EPSG:4326"),
						CRSFactory.getCRS(osmap.getMRendererInfo().getSRS()));
				osmap.animateTo(coords[0], coords[1]);
			}
			// osmap.animateTo(pLoc.getLongitude(), pLoc
			// .getLatitude());
			if (mMyLocationOverlay.mLocation != null) {
				connection = true;
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "onLocationChanged: ", e);
		} finally {

		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownloadCanceled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailDownload(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFatalError(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinishDownload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartDownload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTileDeleted(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTileDownloaded(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTileLoadedFromFileSystem(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTileNotFound(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTileSkipped(IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTotalNumTilesRetrieved(long totalNumTiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDataTransfer(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IDownloadWaiter getDownloadWaiter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onNewMessage(int ID, IEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
