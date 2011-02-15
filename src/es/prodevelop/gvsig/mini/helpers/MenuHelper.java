package es.prodevelop.gvsig.mini.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.Settings;
import es.prodevelop.gvsig.mini.activities.SettingsActivity;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.activities.SearchExpandableActivity;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 *
 * MenuHelper : take input from the Map menu and controls the map back.
 * @author sim
 *
 */
public class MenuHelper {
	
	private static MenuHelper instance = null;
	
	private Map map;
	private final static Logger log = Logger.getLogger(Map.class.getName());
	ShowController showCtrl;
	
	
	private MenuItem myNavigator;
	private MenuItem myLocationButton;
	private MenuItem myZoomRectangle;
	private MenuItem mySearchDirection;
	private MenuItem myDownloadLayers;
	private MenuItem myDownloadTiles;
	private MenuItem mySettings;
	private MenuItem myAbout;
	private MenuItem myWhats;
	private MenuItem myLicense;

	private MenuHelper(Map m){
		this.map = m;
		showCtrl = ShowController.getInstance(m);
	}
	
	public static MenuHelper getInstance(Map m){
		if (instance == null) {
			instance = new MenuHelper(m);
		}
		return instance;
	}
	
	// FixME on prepareMenu...
	public void prepareGps(Boolean enabled) {
		if (myLocationButton != null && myNavigator != null) {
			this.myLocationButton.setEnabled(enabled);
			this.myNavigator.setEnabled(enabled);
		}
	}

	public void prepareNavigator(boolean connection) {
		if (myNavigator != null)
			myNavigator.setEnabled(connection);
	}
	
	public boolean onCreateOptionsMenu(Menu pMenu) {
		try {
			mySearchDirection = pMenu.add(0, 0, 0, R.string.Map_3).setIcon(
					R.drawable.menu00);
			// pMenu.add(0, 1, 1, "Mock provider options");
			myLocationButton = pMenu.add(0, 2, 2, R.string.Map_4).setIcon(
					R.drawable.menu_location);
			pMenu.add(0, 3, 3, R.string.Map_5).setIcon(R.drawable.menu02);
			myDownloadTiles = pMenu.add(0, 4, 4, R.string.download_tiles_14)
			.setIcon(R.drawable.layerdonwload);
			// myZoomRectangle = pMenu.add(0, 4, 4, R.string.Map_6).setIcon(
			// R.drawable.mv_rectangle);
			// pMenu.add(0, 4, 4, "Weather").setIcon(R.drawable.menu03);
			// pMenu.add(0, 5, 5, "Tweetme").setIcon(R.drawable.menu04);
			myNavigator = pMenu.add(0, 5, 5, R.string.Map_Navigator)
			.setIcon(R.drawable.menu_navigation).setEnabled(map.connection);
			myDownloadLayers = pMenu.add(0, 6, 6, R.string.download_tiles_01)
			.setIcon(R.drawable.menu_download);
			// myGPSButton = pMenu.add(0, 7, 7, R.string.Map_27).setIcon(
			// R.drawable.menu_location).setCheckable(true).setChecked(
			// true);
			mySettings = pMenu.add(0, 7, 7, R.string.Map_31).setIcon(
					android.R.drawable.ic_menu_preferences);
			myWhats = pMenu.add(0, 8, 8, R.string.Map_30).setIcon(
					R.drawable.menu_location);
			myLicense = pMenu.add(0, 9, 9, R.string.Map_29).setIcon(
					R.drawable.menu_location);
			myAbout = pMenu.add(0, 10, 10, R.string.Map_28).setIcon(
					R.drawable.menu_location);
			//			pMenu.add(0, 11, 11, R.string.search_local);
			//			pMenu.add(0, 12, 12, R.string.bookmarks);
		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreateOptionsMenu: ", e);
		}
		return true;
	}


	public boolean onMenuItemSelected(int featureId, MenuItem item, Boolean resultMap) {
		boolean result = true;
		try {
			result = resultMap;
			switch (item.getItemId()) {
			case 0:
				try {
					showCtrl.showSearchDialog();
				} catch (Exception e) {
					log.log(Level.SEVERE, "showAddressDialog: ", e);
				}
				break;
			case 1:
				//

				break;
			case 2:
				try {

					// recenterOnGPS = !recenterOnGPS;
					// // osmap.switchPanMode();
					//
					// if (recenterOnGPS) {
					// log.log(Level.FINE,
					// "recentering on GPS after check MyLocation on");
					// myLocation.setIcon(R.drawable.menu01);
					//
					// } else {
					// log
					// .log(Level.FINE,
					// "stop recentering on GPS after check MyLocation off"
					// );
					// myLocation.setIcon(R.drawable.menu01_2);
					// }
					//
					// if (map.mMyLocationOverlay.mLocation != null &&
					// recenterOnGPS) {
					log.log(Level.FINE, "click on my location menu item");
					if (map.locationOverlay.mLocation == null
							|| (map.locationOverlay.mLocation
									.getLatitudeE6() == 0 && map.locationOverlay.mLocation
									.getLongitudeE6() == 0)) {
						Toast.makeText(map, R.string.Map_24, Toast.LENGTH_LONG)
						.show();
						return true;
					}
					map.osmap
					.adjustViewToAccuracyIfNavigationMode(map.locationOverlay.mLocation.acc);
					map.osmap
					.setMapCenterFromLonLat(
							map.locationOverlay.mLocation
							.getLongitudeE6() / 1E6,
							map.locationOverlay.mLocation
							.getLatitudeE6() / 1E6);
					// }
				} catch (Exception e) {
					log.log(Level.SEVERE, "My location: ", e);
				}
				break;
			case 3:
				map.viewLayers();
				break;
			case 4:
				if (Utils.isSDMounted()) {
					if (map.osmap.getMRendererInfo().allowsMassiveDownload()) {
						showCtrl.showDownloadTilesDialog();
					} else {
//TODO
						map.showOKDialog(map.getText(R.string.not_download_tiles).toString(), R.string.warning, false);
					}
				}

				else
					Toast.makeText(map, R.string.LayersActivity_1,
							Toast.LENGTH_LONG).show();
				// try {
				// log.log(Level.FINE, "switch pan mode");
				// osmap.switchPanMode();
				// if (osmap.isPanMode()) {
				// item.setIcon(R.drawable.mv_rectangle).setTitle(
				// R.string.Map_6);
				// } else {
				// item.setIcon(R.drawable.mv_pan)
				// .setTitle(R.string.Map_7);
				// }
				// } catch (Exception e) {
				// log.log(Level.SEVERE,"switchPanMode: ", e);
				// }
				break;
			case 6:
				try {
					showCtrl.showDownloadDialog();
				} catch (Exception e) {
					log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			case 5:
				try {
					map.recenterOnGPS = !map.recenterOnGPS;

					if (myLocationButton != null)
						myLocationButton.setEnabled(!map.recenterOnGPS);
					if (myZoomRectangle != null)
						myZoomRectangle.setEnabled(!map.recenterOnGPS);
					if (myDownloadLayers != null)
						myDownloadLayers.setEnabled(!map.recenterOnGPS);
					if (mySearchDirection != null)
						mySearchDirection.setEnabled(!map.recenterOnGPS);
					if (mySettings != null)
						mySettings.setEnabled(!map.recenterOnGPS);

					// myGPSButton.setEnabled(!recenterOnGPS);

					if (!map.recenterOnGPS) {
						log.log(Level.FINE,
						"recentering on GPS after check MyLocation on");
						map.z.setVisibility(View.VISIBLE);
						myNavigator.setIcon(R.drawable.menu_navigation);

					} else {
						log.log(Level.FINE,
						"stop recentering on GPS after check MyLocation off");//
						map.z.setVisibility(View.INVISIBLE);
						myNavigator.setIcon(R.drawable.menu_navigation_off);
					}
					// if (recenterOnGPS || mMyLocationOverlay.mLocation != null
					// ) {
					// log.log(Level.FINE,
					// "recentering on GPS after check MyLocation on");
					// myNavigator.setIcon(R.drawable.menu_navigation_off);
					//
					// } else {
					// log
					// .log(Level.FINE,
					// "stop recentering on GPS after check MyLocation off"
					// );
					// myNavigator.setIcon(R.drawable.menu_navigation);
					// }
					//
					if (!map.navigation) {
						// wl.acquire();
						// setRequestedOrientation(ActivityInfo.
						// SCREEN_ORIENTATION_PORTRAIT);
						// navigation = true;
						map.initializeSensor(map, true);
						map.showNavigationModeAlert();
					} else {
						try {
							if (Settings.getInstance().getBooleanValue(map.getText(R.string.settings_key_orientation).toString()))
								map.stopSensor(map);
						} catch (Exception e) {
							log.log(Level.SEVERE, "navigation mode off", e);
						}

						log.log(Level.FINE, "navigation mode off");
						map.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						map.navigation = false;
						map.osmap.setKeepScreenOn(false);
					}

					//
					// osmap.switchPanMode();
					// if (osmap.isPanMode()) {
					// item.setIcon(R.drawable.mv_rectangle).setTitle(
					// R.string.Map_6);
					// } else {
					// item.setIcon(R.drawable.mv_pan).setTitle(R.string.Map_7);
					// }
				} catch (Exception e) {
					log.log(Level.SEVERE, "switchPanMode: ", e);
				}
				break;
			case 7:
				Intent i = new Intent(map, SettingsActivity.class);
				map.startActivity(i);
				break;
			case 8:
				try {
					map.showWhatsNew();
				} catch (Exception e) {
					log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			case 9:
				try {
					map.showLicense();
				} catch (Exception e) {
					log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			case 10:
				try {
					map.showAboutDialog();
				} catch (Exception e) {
					log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			case 11:
				try {
					Intent mainIntent = new Intent(map,
							SearchExpandableActivity.class);
					// Point center = map.osmap.getMRendererInfo().getCenter();
					map.fillSearchCenter(mainIntent);
					map.startActivity(mainIntent);
				} catch (Exception e) {
					log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			case 12:
				try {
					Point center = map.osmap.getMRendererInfo().getCenter();
					double[] lonlat = ConversionCoords.reproject(center.getX(),
							center.getY(), CRSFactory.getCRS(map.osmap
									.getMRendererInfo().getSRS()), CRSFactory
									.getCRS("EPSG:900913"));
					InvokeIntents.launchListBookmarks(map, lonlat);
				} catch (Exception e) {
					log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		return result;
	}

	
}
