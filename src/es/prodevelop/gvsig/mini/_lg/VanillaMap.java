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
public class VanillaMap extends MapLocation implements GeoUtils, IDownloadWaiter, IMap {
	SlideBar s;

	boolean wasScaleBarVisible = false;

	public final static int CODE_SETTINGS = 3215;

	AlertDialog downloadTileAlert;
	Cancellable downloadCancellable;
	Button downloadTilesButton;
	private TileDownloaderTask t;
	private SeekBar downTilesSeekBar;
	private TextView totalTiles;
	private TextView totalMB;
	private TextView totalZoom;

	private TileDownloadWaiterDelegate tileWaiter;
	public static String twituser = null;
	public static String twitpass = null;
	public ViewSimpleLocationOverlay mMyLocationOverlay;
	public TileRaster osmap;
	public NamedMultiPoint nameds;
	private Point nearestPOI;
	private int indexNP;
	String datalog = null;
	public float datatransfer2 = 0;
	public RelativeLayout rl;

	ImageView ivSwitch;
	ImageView ivCompassY;
	ImageView ivCompass;
	public ProgressDialog dialog2 = null;
	public boolean recenterOnGPS = false;
	private boolean backpressed = false;
	public boolean backpressedroulette = false;
	private AlertDialog alertP;
	SensorEventListener mTop = null;
	public Handler mHandler;
	public static ViewPort vp;
	int nearopt = 0;
	TextView reportView;
	int cacheCounter = 0;
	MapState mapState;
	boolean cleanVisible = false;
	boolean cleanRoute = false;
	public boolean navigation = false;
	public boolean connection = true;
	boolean listVisible = false;
	boolean cleanCompassVisible = true;
	public ZoomControls z = null;
	public CircularRouleteView c;
	private ItemContext context;
	/**
	 * Whether we currently automatically update the animation.
	 */
	boolean mUpdatingAnimation;
	// PowerManager.WakeLock wl;
	MapHandler handler = new MapHandler();
	protected final static Logger log = Logger.getLogger(VanillaMap.class.getName());
	private UserContextManager contextManager; // singleton with user contexts
	// list
	private UserContext userContext;
	LinearLayout downloadTilesLayout;
	ProgressBar downloadTilesPB;

	public final static int SEARCH_EXP_CODE = 444;
	private ItemContext overlayContext;
	private ActionBar actionBar;

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
				tileWaiter = new TileDownloadWaiterDelegate(this);
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
			rl = new RelativeLayout(this);
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
			this.setContentView(rl);

			LayoutInflater inflater = getLayoutInflater();
			getWindow().addContentView(
					inflater.inflate(R.layout.actionbars, null),
					new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.FILL_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT));

			actionBar = (ActionBar) findViewById(R.id.actionbar);

			/*
			 * Add items and set the contentbar title
			 */

			actionBar.setTitle(R.string.action_bar_title);

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

//	public void addMyLocationAction() {
//	}
//	public void addLayersActivityAction() {
//	}
//	public void addSearchAction() {
//	}
//	public void processActionSearch(Intent i) {
//	}
//	public boolean showPOIs(String[] descr, NamedMultiPoint nm) {
//	}
//	public void viewLastPOIs() {
//	}
//	public void viewLayers() {
//	}
//	public void calculateRoute() {
//	}
//	public void launchRouteCalculation() {
//	}

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

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		try {
			return super.onPrepareOptionsMenu(menu);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu pMenu) {
		return false;
	}

	public void fillSearchCenter(Intent i) {
		Point center = this.mMyLocationOverlay.getLocationLonLat();
		double[] lonlat = ConversionCoords.reproject(center.getX(),
				center.getY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS("EPSG:900913"));
		i.putExtra("lon", lonlat[0]);
		i.putExtra("lat", lonlat[1]);
	}

//	private class MyCenterLocation extends AbstractAction {
//	}
//
//	private class SearchAction extends AbstractAction {
//	}
//
//	private class LayersActivityAction extends AbstractAction {
//	}
//
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {	
//	}
//	protected void showNavigationModeAlert() {		
//	}
//	private void startCache(MapRenderer mr, int minZoom, int maxZoom) {	
//	}
//	public void showWeather(WeatherSet ws) {
//	}
//	public void getWeather(double x, double y, String SRS) {
//	}
//	public void showRouteError() {
//	}
//	public void loadPois(Bundle outState) {	
//	}
//	public void savePois(Bundle outState) {
//	}
//	public void loadRoute(Bundle outState) {
//	}
//	public void saveRoute(Bundle outState) {	
//	}
	
	
	
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

			if (dialog2 != null)
				dialog2.dismiss();

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
			log.log(Level.FINE, "load map from saved instance");
			String mapLayer = outState.getString("maplayer");
			log.log(Level.FINE, "previous layer: " + mapLayer);
			// OSMMercatorRenderer t = OSMMercatorRenderer.getMapnikRenderer();
			// this.osmap = new TileRaster(this, aContext, t,
			// metrics.widthPixels,
			// metrics.heightPixels);
			osmap.onLayerChanged(mapLayer);
			this.mMyLocationOverlay.loadState(outState);
			log.log(Level.FINE, "map loaded");
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadMap: ", e);
			OSMMercatorRenderer t = OSMMercatorRenderer.getMapnikRenderer();
			try {
				this.osmap = new TileRaster(this, CompatManager.getInstance()
						.getRegisteredContext(), t, metrics.widthPixels,
						metrics.heightPixels);
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
				ItemContext context = (ItemContext) Class.forName(contextName)
						.newInstance();
				if (context != null) {
					context.setMap(this);
					this.setContext(context);
				}
			} catch (IllegalAccessException e) {
				log.log(Level.SEVERE, "", e);
			} catch (InstantiationException e) {
				log.log(Level.SEVERE, "", e);
			} catch (ClassNotFoundException e) {
				log.log(Level.SEVERE, "", e);
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
			ItemContext context = this.getItemContext();
			if (context != null)
				outState.putString("contextClassName", context.getClass()
						.getName());
			else
				outState.putString("contextClassName",
						DefaultContext.class.getName());
			log.log(Level.FINE, "map saved");
		} catch (Exception e) {
			log.log(Level.SEVERE, "saveMap: ", e);
		}
	}

	/**
	 * Synchronize zoomcontrols with TileRaster.MapRenderer zoom level
	 */
	public void updateZoomControl() {
		try {
			MapRenderer r = VanillaMap.this.osmap.getMRendererInfo();

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

				// View ivLayers = (View) factory.inflate(
				// R.layout.layers_image_button, null);
				// ivLayers.setId(117);
				// final RelativeLayout.LayoutParams layersParams = new
				// RelativeLayout.LayoutParams(
				// RelativeLayout.LayoutParams.WRAP_CONTENT,
				// RelativeLayout.LayoutParams.WRAP_CONTENT);
				// layersParams.addRule(RelativeLayout.ALIGN_TOP);
				// layersParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				//
				// rl.addView(ivLayers, layersParams);
				//
				// ivLayers.setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// try {
				// viewLayers();
				// } catch (Exception e) {
				// log.log(Level.SEVERE, "onLayersClick: ", e);
				// osmap.postInvalidate();
				// }
				// }
				// });
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
	
	
	private void instantiateTileDownloaderTask(LinearLayout l, int progress) {
		try {
			final boolean updateTiles = ((CheckBox) l
					.findViewById(R.id.download_tiles_overwrite_check))
					.isChecked();

			MapRenderer currentRenderer = VanillaMap.this.osmap.getMRendererInfo();
			MapRenderer renderer = Layers.getInstance().getRenderer(
					currentRenderer.getNAME());

			String tileName = "tile.gvsig";
			String dot = ".";
			String strategy = ITileFileSystemStrategy.QUADKEY;
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

			VanillaMap.this.totalZoom.setText(VanillaMap.this
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
					VanillaMap.this);
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
								VanillaMap.this.instantiateTileDownloaderTask(l,
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
							VanillaMap.this.instantiateTileDownloaderTask(l,
									VanillaMap.this.downTilesSeekBar.getProgress());
						}
					});

			alert.setView(l);

			alert.setPositiveButton(R.string.download_tiles_14,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								VanillaMap.this.resetCounter();
								VanillaMap.this.downloadTileAlert = VanillaMap.this
										.getDownloadTilesDialog();
								VanillaMap.this.downloadTileAlert.show();
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
							VanillaMap.this.osmap.resumeDraw();
							VanillaMap.this.reloadLayerAfterDownload();
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
					Toast.makeText(VanillaMap.this, R.string.download_tiles_13,
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
				VanillaMap.this.downloadCancellable.setCanceled(true);
				VanillaMap.this.downloadTileAlert.dismiss();
			}
		});

		builder.setView(this.downloadTilesLayout);

		return builder.create();
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
		try {
			if (context == null) {
				this.setContext(new DefaultContext(this));
				log.log(Level.FINE, "setContext: " + "DefaultContext");
			} else {
				try {
					Functionality f = this.getItemContext()
							.getExecutingFunctionality();
					if (f != null)
						context.setExecutingFunctionality(f);
				} catch (Exception e) {
					log.log(Level.SEVERE, "setContext", e);

				}
				this.context = null;
				this.context = context;

				log.log(Level.FINE, "setContext: "
						+ context.getClass().getName());
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "setContext: ", e);
		}
	}

	// deleted twitter stuff

	/**
	 * This method sinchronizes the SlideBar position with the current
	 * TileRaster zoom level
	 */
	public void updateSlider() {
		try {
			int progress = Math
					.round(this.osmap.getTempZoomLevel()
							* 100
							/ (this.osmap.getMRendererInfo().getZOOM_MAXLEVEL() + 1 - VanillaMap.this.osmap
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
					/ (this.osmap.getMRendererInfo().getZOOM_MAXLEVEL() + 1 - VanillaMap.this.osmap
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
	 * Switchs from visible to invisible an viceversa the SlideBar
	 */
	public void switchSlideBar() {
		try {
			int size = rl.getChildCount();
			View v;
			boolean hasCircularView = false;
			for (int i = 0; i < size; i++) {
				v = rl.getChildAt(i);
				if (v instanceof CircularRouleteView) {
					hasCircularView = true;
				}
			}
			if (hasCircularView) {
				clearContext();
				return;
			}

			if (s.getVisibility() == View.VISIBLE) {
				s.setVisibility(View.INVISIBLE);
			} else {
				s.setVisibility(View.VISIBLE);
			}

			clearContext();
			// Update context for the Scale bar has been displayed
			userContext.setLastExecScaleBar();

		} catch (Exception e) {
			log.log(Level.SEVERE, "switchSlideBar: ", e);
		}
	}

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
		try {
			GetCellLocationFunc cellLocationFunc = new GetCellLocationFunc(this, 0);
			cellLocationFunc.launch();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
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

			try {

				setIntent(i);
				processGeoAction(i);
//				if (Intent.ACTION_SEARCH.equals(i.getAction())) {
//					processActionSearch(i);
//					return;
//				} else {
					String mapLayer = i.getStringExtra("layer");
					log.log(Level.FINE, "previous layer: " + mapLayer);
					if (mapLayer != null) {
						osmap.onLayerChanged(mapLayer);
						log.log(Level.FINE, "map loaded");
					}
//				}
			} catch (Exception e) {
				log.log(Level.SEVERE, "onNewIntent", e);

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

					VanillaMap.this.downloadTilesPB.setMax((int) tileWaiter
							.getTotalTilesToProcess());
					VanillaMap.this.downloadTilesPB.setProgress((int) tileWaiter
							.getDownloadedNow());
					long totalDownloaded = VanillaMap.this.tileWaiter
							.getDownloadedNow();
					long total = VanillaMap.this.tileWaiter.getTotalTilesToProcess();
					int perc = 0;
					if (total != 0)
						perc = (int) ((double) totalDownloaded / (double) total * 100.0);

					((TextView) VanillaMap.this.downloadTilesLayout
							.findViewById(R.id.download_perc_text))
							.setText(perc + "%" + " - "
									+ tileWaiter.getTilesDownloaded() + "-"
									+ tileWaiter.getTilesFailed() + "-"
									+ tileWaiter.getTilesFromFS() + "-"
									+ tileWaiter.getTilesSkipped() + "-"
									+ tileWaiter.getTilesDeleted() + "-"
									+ tileWaiter.getTilesNotFound() + "-" + "/"
									+ total);

					String downloadedMB = String.valueOf(VanillaMap.this.tileWaiter
							.getBytesDownloaded() / 1024 / 1024);

					if (downloadedMB.length() > 4) {
						downloadedMB = downloadedMB.substring(0, 4);
					}

					((TextView) VanillaMap.this.downloadTilesLayout
							.findViewById(R.id.downloaded_mb_text))
							.setText(VanillaMap.this
									.getText(R.string.download_tiles_09)
									+ " "
									+ downloadedMB + " MB");

					String elapsed = es.prodevelop.gvsig.mini.utiles.Utilities
							.getTimeHoursMinutesSecondsString(time);

					((TextView) VanillaMap.this.downloadTilesLayout
							.findViewById(R.id.download_time_text))
							.setText(VanillaMap.this
									.getText(R.string.download_tiles_10)
									+ " "
									+ elapsed);

					if (totalDownloaded == 0)
						totalDownloaded = 1;

					int estimated = (int) (total * time / totalDownloaded)
							- time;

					String estimatedTime = es.prodevelop.gvsig.mini.utiles.Utilities
							.getTimeHoursMinutesSecondsString(estimated);
					((TextView) VanillaMap.this.downloadTilesLayout
							.findViewById(R.id.download_time_estimated_text))
							.setText(VanillaMap.this
									.getText(R.string.download_tiles_11)
									+ " "
									+ estimatedTime);
				}
			});
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

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
				VanillaMap.this.enableGPS();
				Toast.makeText(VanillaMap.this, R.string.download_tiles_12,
						Toast.LENGTH_LONG).show();
				VanillaMap.this.reloadLayerAfterDownload();
			}
		});
	}

	private void reloadLayerAfterDownload() {
		VanillaMap.this.osmap.resumeDraw();
		VanillaMap.this.osmap.onLayerChanged(VanillaMap.this.osmap.getMRendererInfo()
				.getFullNAME());
		try {
			VanillaMap.this.osmap.initializeCanvas(TileRaster.mapWidth,
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
				VanillaMap.this.enableGPS();
				isDownloadTilesFinished = true;
				VanillaMap.this.downloadTilesButton
						.setText(R.string.alert_dialog_text_ok);
				VanillaMap.this.reloadLayerAfterDownload();
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
				VanillaMap.this.downloadTilesButton
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
				VanillaMap.this.tileWaiter.onTotalNumTilesRetrieved(totalNumTiles);
				VanillaMap.this.totalTiles.setText(VanillaMap.this
						.getText(R.string.download_tiles_06)
						+ " "
						+ totalNumTiles);
				double totalMB = totalNumTiles * 10 / 1024;
				VanillaMap.this.totalMB.setText(VanillaMap.this
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

	private class TileDownloadWaiterDelegate extends TileDownloadWaiter {

		public TileDownloadWaiterDelegate(IDownloadWaiter w) {
			super(w);
			// TODO Auto-generated constructor stub
		}

	}

	public void setOverlayContext(ItemContext context) {
		this.overlayContext = context;
	}

	public ActionBar getActionbar() {
		return this.actionBar;
	}
	
	public void setActionbar(ActionBar actionbar) {
		this.actionBar = actionbar;
	}

}
