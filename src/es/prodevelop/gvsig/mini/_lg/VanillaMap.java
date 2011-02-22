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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm.samples;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */

package es.prodevelop.gvsig.mini._lg;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.anddev.android.weatherforecast.weather.WeatherCurrentCondition;
import org.anddev.android.weatherforecast.weather.WeatherForecastCondition;
import org.anddev.android.weatherforecast.weather.WeatherSet;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;

import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.LogFeedbackActivity;
import es.prodevelop.gvsig.mini.activities.MapLocation;
import es.prodevelop.gvsig.mini.activities.OnSettingsChangedListener;
import es.prodevelop.gvsig.mini.activities.Settings;
import es.prodevelop.gvsig.mini.activities.SettingsActivity;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.IEvent;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.DefaultContext;
import es.prodevelop.gvsig.mini.context.map.GPSItemContext;
import es.prodevelop.gvsig.mini.context.map.POIContext;
import es.prodevelop.gvsig.mini.context.map.RouteContext;
import es.prodevelop.gvsig.mini.context.map.RoutePOIContext;
import es.prodevelop.gvsig.mini.context.map.wms.WMSGPSItemContext;
import es.prodevelop.gvsig.mini.context.map.wms.WMSPOIContext;
import es.prodevelop.gvsig.mini.context.map.wms.WMSRouteContext;
import es.prodevelop.gvsig.mini.context.map.wms.WMSRoutePOIContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.FeatureCollection;
import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.geom.android.GPSPoint;
import es.prodevelop.gvsig.mini.location.LocationTimer;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.map.MapState;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.namefinder.Named;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.PlaceSearcher;
import es.prodevelop.gvsig.mini.search.activities.SearchExpandableActivity;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.tasks.map.GetCellLocationFunc;
import es.prodevelop.gvsig.mini.tasks.namefinder.NameFinderFunc;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;
import es.prodevelop.gvsig.mini.tasks.tiledownloader.TileDownloadCallbackHandler;
import es.prodevelop.gvsig.mini.tasks.tiledownloader.TileDownloadWaiter;
import es.prodevelop.gvsig.mini.tasks.weather.WeatherFunctionality;
import es.prodevelop.gvsig.mini.tasks.yours.YOURSFunctionality;
import es.prodevelop.gvsig.mini.user.UserContext;
import es.prodevelop.gvsig.mini.user.UserContextManager;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.Constants;
import es.prodevelop.gvsig.mini.utiles.Tags;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;
import es.prodevelop.gvsig.mini.views.overlay.CircularRouleteView;
import es.prodevelop.gvsig.mini.views.overlay.LongTextAdapter;
import es.prodevelop.gvsig.mini.views.overlay.NameFinderOverlay;
import es.prodevelop.gvsig.mini.views.overlay.RouteOverlay;
import es.prodevelop.gvsig.mini.views.overlay.SlideBar;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import es.prodevelop.gvsig.mini.views.overlay.ViewSimpleLocationOverlay;
import es.prodevelop.gvsig.mini.yours.RouteManager;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.IDownloadWaiter;
import es.prodevelop.tilecache.TileDownloaderTask;
import es.prodevelop.tilecache.generator.impl.FitScreenBufferStrategy;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.provider.TileProvider;
import es.prodevelop.tilecache.provider.filesystem.strategy.ITileFileSystemStrategy;
import es.prodevelop.tilecache.provider.filesystem.strategy.impl.FileSystemStrategyManager;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.MapRendererManager;
import es.prodevelop.tilecache.renderer.OSMMercatorRenderer;
import es.prodevelop.tilecache.renderer.wms.OSRenderer;
import es.prodevelop.tilecache.util.ConstantsTileCache;
import es.prodevelop.tilecache.util.Utilities;

/**
 * The main activity of the application. Consists on a RelativeLayout with some
 * zoom controls and the TileRaster that manages the MapView
 * 
 * This class manages the Messages sent by Functionalities with the MapHandler
 * 
 * Also has instances of Route and NamedMultiPoint
 * 
 * Needs to be refactored
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public abstract class VanillaMap extends IMap implements GeoUtils {

	private static final String TAG = VanillaMap.class.getName();
	protected final static Logger log = Logger.getLogger(VanillaMap.class.getName());
		
	public ViewSimpleLocationOverlay mMyLocationOverlay;
	
	MapState mapState;
	MapHandler handler = new MapHandler();	
	
	public boolean recenterOnGPS = false;
	private boolean backpressed = false;
	public boolean backpressedroulette = false;
	public boolean navigation = false;
	public boolean connection = true;

	private UserContextManager contextManager; // singleton with user contexts
	protected UserContext userContext;
	private ItemContext context;
	private ItemContext overlayContext;
	
	public float datatransfer2 = 0;

// Unused
//	boolean wasScaleBarVisible = false; // used in MapPOI
//	public NamedMultiPoint nameds;
//	private Point nearestPOI;
//	private int indexNP;
//	String datalog = null;
//	ImageView ivSwitch;
//	ImageView ivCompassY;
//	ImageView ivCompass;
	
// public ProgressDialog dialog2 = null;
//	int nearopt = 0;
	// TextView reportView;
//	int cacheCounter = 0;
//	boolean cleanVisible = false;
//	boolean cleanRoute = false;
//	public final static int CODE_SETTINGS = 3215;
//	private AlertDialog alertP;
//	SensorEventListener mTop = null;
//	boolean listVisible = false;
//	boolean cleanCompassVisible = true;
//	public final static int SEARCH_EXP_CODE = 444;
// boolean mUpdatingAnimation;
// PowerManager.WakeLock wl;
// 	public Handler mHandler;
	
	/**
	 * Called when the activity is first created.
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			try {
				setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
				CompatManager.getInstance().getRegisteredLogHandler()
						.configureLogger(log);
				// Settings.getInstance().addOnSettingsChangedListener(this);
				
				log.log(Level.FINE, "on create");

				// PowerManager pm = (PowerManager)
				// getSystemService(Context.POWER_SERVICE);
				// wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
				// "Prueba de ScreenPower");

				// if (getIntent() != null
				// && getIntent().getAction().compareTo(
				// SplashActivity.OFFLINE_INTENT_ACTION) != 0)
				onNewIntent(getIntent());
			} catch (Exception e) {
				log.log(Level.SEVERE, "onCreate", e);
				// log.log(Level.SEVERE,e.getMessage());
			} finally {

			}

			mapState = new MapState(this);
			// this.setContext(new DefaultContext(this)); //CHANGED

			// nameFinderTask = new NameFinderTask(this, handler);
			
		
			preCreate();
			
			// TMSRenderer t = TMSRenderer.getTMSRenderer(
			// "http://www.idee.es/wms-c/PNOA/PNOA/1.0.0/PNOA/");
			loadSettings(savedInstanceState);
			loadMap(savedInstanceState);

			// Load UserContexts
			// First create/get UserContextManager singleton instance
			contextManager = UserContextManager.getInstance();
			// Create UserContext and register it in the manager
			userContext = new UserContext();
			contextManager.Register(userContext);
			// add new user contexts here
			// Load previously persisted user context data
			contextManager.loadContexts();
			userContext.incExecutionCounter(); // update execution number

			boolean isSaved = false;
			try {
				if (savedInstanceState != null)
					isSaved = savedInstanceState.getBoolean("isSaved", false);
			} catch (Exception e) {
				log.log(Level.SEVERE, "isSaved: ", e);
			}

			if (isSaved) {
				log.log(Level.FINE, "Restoring from previous state");
//				loadRoute(savedInstanceState);
//				loadPois(savedInstanceState);
				loadUI(savedInstanceState);
				loadMap(savedInstanceState);
				loadCenter(savedInstanceState);
			} else {
				log.log(Level.FINE, "Not restoring from previous state");
				loadUI(savedInstanceState);
				boolean succeed = mapState.load();
				if (!succeed) {
					log.log(Level.FINE,
							"map state was not persisted. Loading Mapnik");
					this.osmap.setZoomLevel(3);
					this.osmap.setMapCenter(0, 0);
					this.osmap.setRenderer(OSMMercatorRenderer
							.getMapnikRenderer());
				}
			}
			
			// create the ui...
			postCreate();
		

//			addLayersActivityAction();
//			addMyLocationAction();			
//			addSearchAction();

			// this.addContentView(actionbar, R.layout.pruebas);
			// if (isSaved) setContentView(null);
			// enableAcelerometer(savedInstanceState);

			/*
			 * if (!userContext.isUsedCircleMenu()) { // The Circle Context Menu
			 * (Roulette) has never been displayed, // so let's // give the user
			 * a hint about it Toast t = Toast.makeText(this, R.string.Map_23,
			 * Toast.LENGTH_LONG); t.show(); }
			 */

			// Intercept a possible intent with a search executed from the
			// search
			// dialog, towards the application
			Intent i = getIntent();

			// this.processActionSearch(i);
			this.processGeoAction(i);
			this.processOfflineIntentActoin(i);

			int hintId = 0;
			hintId = userContext.getHintMessage();
			if (hintId != 0) {
				Toast t = Toast.makeText(this, hintId, Toast.LENGTH_LONG);
				t.show();
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			LogFeedbackActivity.showSendLogDialog(this);
		} finally {
			// this.obtainCellLocation();
		}
	}

	// used to load UI stuff (copy/pasted) code that was here.
	protected abstract void preCreate();
	protected abstract void postCreate();
	
	public abstract void loadUI(Bundle savedInstanceState);
	
	
	/**
	 * Manages the results of the NameFinderActivity, LayersActivity
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	}

	@Override
	public void onLocationChanged(final Location pLoc) {
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
//				if (myNavigator != null)
//					myNavigator.setEnabled(connection);
			}

		} catch (Exception e) {
			log.log(Level.SEVERE, "onLocationChanged: ", e);
		} finally {

		}
	}

	public void fillSearchCenter(Intent i) {
		Point center = this.mMyLocationOverlay.getLocationLonLat();
		double[] lonlat = ConversionCoords.reproject(center.getX(),
				center.getY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS("EPSG:900913"));
		i.putExtra("lon", lonlat[0]);
		i.putExtra("lat", lonlat[1]);
	}

	
	@Override
	public void onDestroy() {
		try {
			super.onDestroy();
			log.log(Level.FINE, "onDestroy map activity");

			try {
				log.log(Level.FINE, "release wake lock");
				osmap.setKeepScreenOn(false);
			} catch (Exception e) {
				log.log(Level.SEVERE, "release wake lock", e);

			}

			try {
				log.log(Level.FINE, "persist map");
				mapState.persist();
			} catch (Exception e) {
				log.log(Level.SEVERE, "Persist mapstate: ", e);
			}

//			if (dialog2 != null)
//				dialog2.dismiss();

			osmap.clearCache();
			this.stopSensor(this);
			if (backpressed) {
				log.log(Level.FINE, "back key pressed");
				// cosas a hacer para la limpieza

				// this.getGPSManager().stopLocationProviders();
				WorkQueue.getInstance().finalize();
				WorkQueue.getExclusiveInstance().finalize();

				backpressed = false;

			}
			try {
				contextManager.saveContexts();
			} catch (Exception e) {
				log.log(Level.SEVERE,
						"onDestroy. contextManager.saveContexts(): ", e);
			}

			this.destroy();

		} catch (Exception e) {
			log.log(Level.SEVERE, "onDestroy: ", e);
		}
	}

	/**
	 * Frees memory
	 */
	public void destroy() {
		try {
			log.log(Level.FINE, "destroy");
			osmap.destroy();
		} catch (Exception e) {
			log.log(Level.SEVERE, "destroy", e);
		}
	}


	/**
	 * Load map info stored on a Bundle when the configuration has changed or
	 * the Activity has been restarted
	 * 
	 * @param outState
	 *            The Bundle @see {@link #onSaveInstanceState(Bundle)}
	 * @throws BaseException
	 */
	public void loadMap(Bundle outState) throws BaseException {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		try {
			Log.d(TAG, "Try loading map from saved instance ; may throw null pointer exception");
			log.log(Level.FINE, "load map from saved instance");
			
			String mapLayer;
			if(outState != null)
				mapLayer = outState.getString("maplayer");
			else
				mapLayer = "maze";
			
			log.log(Level.FINE, "previous layer: " + mapLayer);
			// OSMMercatorRenderer t = OSMMercatorRenderer.getMapnikRenderer();
			// this.osmap = new TileRaster(this, aContext, t,
			// metrics.widthPixels,
			// metrics.heightPixels);
			osmap.onLayerChanged(mapLayer); // this IS the null stuff OMG..
			this.mMyLocationOverlay.loadState(outState);
			log.log(Level.FINE, "map loaded");
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadMap: ", e);
			OSMMercatorRenderer t = OSMMercatorRenderer.getMapnikRenderer();
			try {
				this.osmap = new TileRaster(this, CompatManager.getInstance()
						.getRegisteredContext(), t, metrics.widthPixels,
						metrics.heightPixels);
				System.out.println("Just created osmap..!");
				// osmap.initializeCanvas(metrics.widthPixels,
				// metrics.heightPixels);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (outState == null)
					return;
				String contextName = outState.getString("contextClassName");
				log.log(Level.FINE, "loading previous context: " + contextName);
//				ItemContext context = (ItemContext) Class.forName(contextName)
//						.newInstance();
//				if (context != null) {
//					context.setMap(this);
//					this.setContext(context);
//				}
//			} catch (IllegalAccessException e) {
//				log.log(Level.SEVERE, "", e);
//			} catch (InstantiationException e) {
//				log.log(Level.SEVERE, "", e);
//			} catch (ClassNotFoundException e) {
//				log.log(Level.SEVERE, "", e);
			} catch (Exception e) {
				log.log(Level.SEVERE, "", e);
			}
		}
	}

	/**
	 * Save settings when the configuration has changed or the Activity has been
	 * restarted
	 * 
	 * @param outState
	 *            The Bundle @see {@link #onSaveInstanceState(Bundle)}
	 */
	public void saveSettings(Bundle outState) {
		try {
			outState.putFloat("dataTransfer", this.datatransfer2);
			outState.putBoolean("dataNavigation", this.navigation);
			outState.putBoolean("dataRecent", this.recenterOnGPS);
		} catch (Exception e) {
			log.log(Level.SEVERE, "saveSettings: ", e);
		}
	}

	/**
	 * Load settings stored on a Bundle when the configuration has changed or
	 * the Activity has been restarted
	 * 
	 * @param outState
	 *            The Bundle @see {@link #onSaveInstanceState(Bundle)}
	 */
	public void loadSettings(Bundle outState) {
		try {
			if (outState == null)
				return;
			this.datatransfer2 = outState.getFloat("dataTransfer");
			this.navigation = outState.getBoolean("dataNavigation");
			this.recenterOnGPS = outState.getBoolean("dataRecent");
			if (navigation && recenterOnGPS)
				osmap.setKeepScreenOn(true);
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadSettings: ", e);
		}
	}

	/**
	 * Save map info (current layer, GPS location, ItemContext...) when the
	 * configuration has changed or the Activity has been restarted
	 * 
	 * @param outState
	 *            The Bundle @see {@link #onSaveInstanceState(Bundle)}
	 */
	public void saveMap(Bundle outState) {
		try {
			log.log(Level.FINE, "save map to bundle");
			outState.putString("maplayer", osmap.getMRendererInfo()
					.getFullNAME());
			this.mMyLocationOverlay.saveState(outState);
//			ItemContext context = this.getItemContext();
//			if (context != null)
//				outState.putString("contextClassName", context.getClass()
//						.getName());
//			else
//				outState.putString("contextClassName",
//						DefaultContext.class.getName());
			log.log(Level.FINE, "map saved");
		} catch (Exception e) {
			log.log(Level.SEVERE, "saveMap: ", e);
		}
	}

	
	

	/**
	 * Loads the last map center and zoom level stored on a Bundle when the
	 * configuration has changed or the Activity has been restarted
	 * 
	 * @param outState
	 *            The Bundle @see {@link #onSaveInstanceState(Bundle)}
	 */
	public void loadCenter(Bundle savedInstanceState) {
		try {
			log.log(Level.FINE, "load center from saved instance");
			double lat = savedInstanceState.getDouble("lat");
			double longit = savedInstanceState.getDouble("longit");
			int zoomlvl = savedInstanceState.getInt("zoomlvl");
			log.log(Level.FINE, "lat, lon, zoom: " + lat + ", " + longit + ", "
					+ zoomlvl);

			this.osmap.setMapCenter(longit, lat);
			this.osmap.setZoomLevel(zoomlvl);
			log.log(Level.FINE, "lat, lon, zoom: " + longit + ", " + lat + ", "
					+ zoomlvl);
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadCenter: ", e);
			osmap.getMRendererInfo().centerOnBBox();
		}
	}

	/**
	 * Saves the map center and zoom level on a Bundle when the configuration
	 * has changed or the Activity has been restarted
	 * 
	 * @param outState
	 *            The Bundle @see {@link #onSaveInstanceState(Bundle)}
	 */
	public void saveCenter(Bundle outState) {
		try {
			log.log(Level.FINE, "save center to bundle");
			final Point center = this.osmap.getMRendererInfo().getCenter();
			final int zoomLevel = this.osmap.getMRendererInfo().getZoomLevel();
			outState.putDouble("lat", center.getY());
			outState.putDouble("longit", center.getX());
			outState.putInt("zoomlvl", zoomLevel);
			log.log(Level.FINE, "lat, lon, zoom: " + center.getY() + ", "
					+ center.getX() + ", " + zoomLevel);
		} catch (Exception e) {
			log.log(Level.SEVERE, "saveCenter: ", e);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			log.log(Level.FINE, "onSaveInstanceState");
			outState.putBoolean("isSaved", true);
			super.onSaveInstanceState(outState);
			saveSettings(outState);
			saveMap(outState);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onSaveInstanceState: ", e);
		}
		try {
			saveCenter(outState);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
//		if (false) {
//		try {
//			saveRoute(outState);
//		} catch (Exception e) {
//			log.log(Level.SEVERE, "", e);
//		}
//		try {
//			savePois(outState);
//		} catch (Exception e) {
//			log.log(Level.SEVERE, "", e);
//		}
//		}
	}

	public void setLoadingVisible(boolean visible) {
	}
	
	
	// public void enableAcelerometer(Bundle savedInstanceState) {
	// try {
	// log.log(Level.FINE, "enable accelerometer");
	// // mSensorManager = (SensorManager)
	// // getSystemService(Context.SENSOR_SERVICE);
	// // mSensorManager.registerListener(mSensorListener, mSensorManager
	// // .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	// // SensorManager.SENSOR_DELAY_UI);
	// } catch (Exception e) {
	// log.log(Level.SEVERE,"", e);
	// }
	// }

	// private final SensorEventListener mSensorListener = new
	// SensorEventListener() {
	//
	// public void onSensorChanged(SensorEvent event) {
	// if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	// double forceThreshHold = 4.0f;
	// double totalForce = 0.0f;
	// totalForce += Math.pow(event.values[SensorManager.DATA_X]
	// / SensorManager.GRAVITY_EARTH, 2.0);
	// totalForce += Math.pow(event.values[SensorManager.DATA_Y]
	// / SensorManager.GRAVITY_EARTH, 2.0);
	// totalForce += Math.pow(event.values[SensorManager.DATA_Z]
	// / SensorManager.GRAVITY_EARTH, 2.0);
	// totalForce = Math.sqrt(totalForce);
	// if (totalForce > 1.5 && totalForce < forceThreshHold) {
	// recenterOnGPS = !recenterOnGPS;
	// if (mMyLocationOverlay.mLocation != null && recenterOnGPS) {
	// osmap.setZoomLevel(15);
	// osmap.setMapCenter(mMyLocationOverlay.mLocation);
	// }
	// }
	// }
	// }
	//
	// public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// }
	// };

	@Override
	public void onResume() {
		try {
			log.log(Level.FINE, "onResume");
			super.onResume();
			osmap.resumeDraw();
			processGeoAction(getIntent());
			// processRouteAction(getIntent());
			processOfflineIntentActoin(getIntent());
			if (navigation && recenterOnGPS)
				osmap.setKeepScreenOn(true);
			// if (navigation && recenterOnGPS)
			// wl.acquire();
			// mSensorManager.registerListener(mSensorListener, mSensorManager
			// .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
			// SensorManager.SENSOR_DELAY_FASTEST);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onResume: ", e);
		}
	}

//	private void processRouteAction(Intent i) {
//	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		try {
			return this.osmap.onTrackballEvent(event);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onTrackBallEvent: ", e);
			return false;
		}
	}

	@Override
	protected void onStop() {
		try {
			// mSensorManager.unregisterListener(mSensorListener);
			super.onStop();
		} catch (Exception e) {
			log.log(Level.SEVERE, "onStop: ", e);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private long last = 0;

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

	// context stuff that are directly link to a representation : we dont want those
	private ItemContext updateContextWMS(ItemContext context) {
		return context;	
	}
	public void showContext(ItemContext context) {	
	}
	public void showCircularView(ItemContext context) {		
	}
	public void showOverlayContext() {		
	}
	public void clearContext() {
	}
	public ItemContext getItemContext() {
		return context;
	}
	
	private int state = 0; //VanillaMap.VOID;
	public void updateContext(int state) {
	}

	
	/**
	 * @see MapHandler
	 * @return The MapHandler
	 */
	public Handler getMapHandler() {
		return handler;
	}
	
	class MapHandler extends Handler {
	
	}

	/**
	 * Sets the current ItemContext of the application
	 * 
	 * @param context
	 */
	public void setContext(ItemContext context) {
//		try {
//			if (context == null) {
//				this.setContext(new DefaultContext(this));
//				log.log(Level.FINE, "setContext: " + "DefaultContext");
//			} else {
//				try {
//					Functionality f = this.getItemContext()
//							.getExecutingFunctionality();
//					if (f != null)
//						context.setExecutingFunctionality(f);
//				} catch (Exception e) {
//					log.log(Level.SEVERE, "setContext", e);
//
//				}
//				this.context = null;
//				this.context = context;
//
//				log.log(Level.FINE, "setContext: "
//						+ context.getClass().getName());
//			}
//		} catch (Exception e) {
//			log.log(Level.SEVERE, "setContext: ", e);
//		}
	}

	// deleted twitter stuff


	@Override
	public void onConfigurationChanged(Configuration config) {
		try {
			super.onConfigurationChanged(config);
			log.log(Level.FINE, "onConfigurationChanged");
			if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
				this.mMyLocationOverlay
						.setOffsetOrientation(ViewSimpleLocationOverlay.PORTRAIT_OFFSET_ORIENTATION);
			} else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				this.mMyLocationOverlay
						.setOffsetOrientation(ViewSimpleLocationOverlay.LANDSCAPE_OFFSET_ORIENTATION);
			}
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			osmap.initializeCanvas(metrics.widthPixels, metrics.heightPixels);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onConfigurationChanged: ", e);
		}
	}

	public boolean isPOISlideShown() {
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {

				if (osmap.removeExpanded()) {
					return true;
				}

				if (osmap.acetate.getPopupVisibility() == View.VISIBLE) {
					osmap.acetate.setPopupVisibility(View.INVISIBLE);
					return true;
				}

				log.log(Level.FINE, "KEY BACK pressed");
				if (!backpressedroulette) {

					backpressed = true;
					this.onDestroy();
					Intent i = new Intent();
					i.putExtra("exit", true);
					setResult(RESULT_OK, i);
					finish();

					// return false;
					super.onKeyDown(keyCode, event);
				} else {
					clearContext();
					backpressedroulette = false;
					return true;
				}
			} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
				try {
					if (POIProviderManager.getInstance().getPOIProvider() != null) {
						Intent mainIntent = new Intent(this,
								SearchExpandableActivity.class);
						// Point center =
						// this.osmap.getMRendererInfo().getCenter();
						fillSearchCenter(mainIntent);
						this.startActivity(mainIntent);
						return true;
					}
				} catch (BaseException e) {
					return false;
				}
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onKeyDown: ", e);
			return false;
		}
		// return false;

	}

	@Override
	public void onLowMemory() {
		try {
			log.log(Level.FINE, "onLowMemory");
			osmap.getMTileProvider().onLowMemory();
			super.onLowMemory();
			// Toast.makeText(this, R.string.Map_25, Toast.LENGTH_LONG).show();
			mapState.persist();
			System.gc();
		} catch (Exception e) {
			log.log(Level.SEVERE, "onLowMemory: ", e);
			System.gc();
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
		GPS_STATUS = status;

	}

	/**
	 * Launches GetCellLocationFunc
	 */
	public void obtainCellLocation() {
//		try {
//			GetCellLocationFunc cellLocationFunc = new GetCellLocationFunc(this, 0);
//			cellLocationFunc.launch();
//		} catch (Exception e) {
//			log.log(Level.SEVERE, "", e);
//		}
	}

	public static int GPS_STATUS;

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

	// Base action to handle offline action: load a specific layer 
	// from a URL_STRING specified in the Intent
	private void processOfflineIntentActoin(Intent i) throws Exception {
		Log.d("Map", "OFFLINE_INTENT_ACTION");
		String completeURLString = i.getStringExtra(Constants.URL_STRING);
		if (completeURLString == null)
			return;
		i.removeExtra(Constants.URL_STRING);
		completeURLString = completeURLString.replaceAll("&gt;",
				MapRenderer.NAME_SEPARATOR);
		String urlString = completeURLString.split(";")[1];
		String layerName = completeURLString.split(";")[0];
		MapRenderer renderer = MapRendererManager.getInstance()
				.getMapRendererFactory()
				.getMapRenderer(layerName, urlString.split(","));
		// if (renderer.isOffline())
		// renderer.setNAME(renderer.getNAME() + MapRenderer.NAME_SEPARATOR
		// + renderer.getOfflineLayerName());
		Layers.getInstance().addLayer(completeURLString);
		Layers.getInstance().persist();
		Log.d("Map", renderer.getFullNAME());
		osmap.onLayerChanged(renderer.getFullNAME());
	}

	private void processGeoAction(Intent i) {
		if (i == null)
			return;
		final int zoom = i.getIntExtra("zoom", -1);
		if (zoom == -1)
			return;
		i.putExtra("zoom", -1);

		final double lat = i.getDoubleExtra("lat", 0);
		final double lon = i.getDoubleExtra("lon", 0);

		osmap.setMapCenterFromLonLat(lon, lat);
		osmap.setZoomLevel(zoom);
	}

	@Override
	public void onNewIntent(Intent i) {
		try {
			if (i == null) {
				Log.d("Map", "intent is null");
				return;
			}

			setIntent(i);
			processGeoAction(i);
			String mapLayer = i.getStringExtra("layer");
			log.log(Level.FINE, "previous layer: " + mapLayer);
			if (mapLayer != null) {
				osmap.onLayerChanged(mapLayer);
				log.log(Level.FINE, "map loaded");
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "onNewIntent", e);
		}
	}

	/**
	 * persists the MapState
	 */
	public void persist() {
		try {
			log.log(Level.FINE, "map persist");
			mapState.persist();
		} catch (Exception e) {
			log.log(Level.SEVERE, "persist", e);
		}
	}

	public ViewSimpleLocationOverlay getmMyLocationOverlay() {
		return mMyLocationOverlay;
	}

	public void setmMyLocationOverlay(
			ViewSimpleLocationOverlay mMyLocationOverlay) {
		this.mMyLocationOverlay = mMyLocationOverlay;
	}

	/**
	 * Sets the TileRaster center on the GPS location
	 */
	public void centerOnGPSLocation() {
		try {
			final GPSPoint location = this.mMyLocationOverlay.mLocation;
			if (location != null) {
				this.osmap.setMapCenter(location);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * starts the LocationHandler
	 */
	@Override
	public void enableGPS() {
		if(true)
			return;
		try {
			log.log(Level.FINE, "enableGPS");
			super.enableGPS();
			boolean enabled = this.isLocationHandlerEnabled();

// MenuItem : presentation related stuff
//			if (myLocationButton != null && myNavigator != null) {
//				this.myLocationButton.setEnabled(enabled);
//				this.myNavigator.setEnabled(enabled);
//			}
			// this.myGPSButton.setTitle(R.string.Map_27);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * Stops the location handler
	 */
	public void disableGPS() {
		if(true)
			return;
		try {
			log.log(Level.FINE, "disableGPS");
			super.disableGPS();
			boolean enabled = this.isLocationHandlerEnabled();

//			if (myLocationButton != null && myNavigator != null) {
//				this.myLocationButton.setEnabled(enabled);
//				this.myNavigator.setEnabled(enabled);
//			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public void setOverlayContext(ItemContext context) {
		this.overlayContext = context;
	}


	@Override
	public void setViewPort(ViewPort viewPort) {
		this.vp = viewPort;
	}

}