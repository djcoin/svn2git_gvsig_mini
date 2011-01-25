package es.prodevelop.gvsig.mini.activities;

import java.util.ArrayList;
import java.util.logging.Level;

import android.R.color;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.activities.SearchExpandableActivity;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;
import es.prodevelop.gvsig.mini.views.overlay.BookmarkOverlay;
import es.prodevelop.gvsig.mini.views.overlay.CategoriesListView;
import es.prodevelop.gvsig.mini.views.overlay.CategoriesListView.CheckBoxBulletAdapter;
import es.prodevelop.gvsig.mini.views.overlay.NameFinderOverlay;
import es.prodevelop.gvsig.mini.views.overlay.PerstClusterPOIOverlay;
import es.prodevelop.gvsig.mini.views.overlay.ResultSearchOverlay;
import es.prodevelop.gvsig.mini.views.overlay.RouteOverlay;
import es.prodevelop.gvsig.mini.views.overlay.SlidingDrawer2;
import es.prodevelop.gvsig.mini.views.overlay.SlidingDrawer2.OnDrawerCloseListener;
import es.prodevelop.gvsig.mini.views.overlay.SlidingDrawer2.OnDrawerOpenListener;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class MapPOI extends Map {

	ProgressBar loadingResults;
	public boolean isPOISlideShown = false;
	PerstClusterPOIOverlay p;
	private SlidingDrawer2 sliding;

	public final static int SEARCH_MENU = 0;
	public final static int ADV_SEARCH_MENU = 1;
	public final static int MY_LOC_MENU = 2;
	public final static int FAV_MENU = 3;
	public final static int NEAR_LOC_MENU = 4;
	public final static int SETTINGS_MENU = 5;
	public final static int MAPS_MENU = 6;
	public final static int NAV_MENU = 7;
	public final static int LICENSE_MENU = 8;
	public final static int ABOUT_MENU = 9;

	MenuItem locationItem;
	MenuItem searchItem;
	MenuItem advSearchItem;
	MenuItem settingsItem;
	MenuItem nagItem;

	/**
	 * Instantiates the UI: TileRaster, ZoomControls, SlideBar in a
	 * RelativeLayout
	 * 
	 * @param savedInstanceState
	 */
	public void loadUI(Bundle savedInstanceState) {
		try {
			super.loadUI(savedInstanceState);
			final LayoutInflater factory = LayoutInflater.from(this);
			sliding = (SlidingDrawer2) factory.inflate(R.layout.slide, null);
			sliding.setOnDrawerOpenListener(new OnDrawerOpenListener() {

				@Override
				public void onDrawerOpened() {
					try {
						((Button) sliding.getHandle())
								.setBackgroundResource(R.drawable.slide_down_icon);
						isPOISlideShown = true;
						osmap.pauseDraw();
						z.setVisibility(View.INVISIBLE);
						if (s.getVisibility() == View.VISIBLE) {
							wasScaleBarVisible = true;
						} else {
							wasScaleBarVisible = false;
						}
						s.setVisibility(View.INVISIBLE);
					} catch (Exception e) {
						Log.e("", "onDrawerOpened Error");
					}
				}
			});

			sliding.setOnDrawerCloseListener(new OnDrawerCloseListener() {

				@Override
				public void onDrawerClosed() {
					try {
						((Button) sliding.getHandle())
								.setBackgroundResource(R.drawable.slide_up_icon);
						isPOISlideShown = false;
						z.setVisibility(View.VISIBLE);
						if (wasScaleBarVisible)
							s.setVisibility(View.VISIBLE);
						PerstClusterPOIOverlay poiOverlay = (PerstClusterPOIOverlay) osmap
								.getOverlay(PerstClusterPOIOverlay.DEFAULT_NAME);
						if (poiOverlay != null)
							poiOverlay.setCategories(POICategories.selected);

						if (!POICategories.bookmarkSelected)
							osmap.removeOverlay(BookmarkOverlay.DEFAULT_NAME);
						else
							osmap.addOverlay(new BookmarkOverlay(MapPOI.this,
									osmap, BookmarkOverlay.DEFAULT_NAME));

						osmap.getOverlay(ResultSearchOverlay.DEFAULT_NAME)
								.setVisible(POICategories.resultSearchSelected);

						osmap.resumeDraw();

						// Map.this.osmap.poiOverlay.onTouchEvent(null, null);
					} catch (Exception e) {
						// Log.e("", e.getMessage());
					}
				}
			});

			final RelativeLayout.LayoutParams sParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);

			loadingResults = new ProgressBar(this);
			loadingResults.setIndeterminate(true);
			loadingResults.setBackgroundColor(color.background_dark);
			loadingResults.setVisibility(View.INVISIBLE);
			final RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			//
			rl.addView(sliding, sParams);
			rl.addView(loadingResults, lParams);

			/* Creating the main Overlay */
			{

				try {
					if (p == null)
						p = new PerstClusterPOIOverlay(this, osmap,
								PerstClusterPOIOverlay.DEFAULT_NAME, true);
					this.osmap.addOverlay(p);

				} catch (Exception e) {
					Log.e("", e.getMessage());
				}

				this.osmap.addOverlay(new ResultSearchOverlay(this, osmap,
						ResultSearchOverlay.DEFAULT_NAME));				
			}

		} catch (Exception e) {
		} catch (OutOfMemoryError ou) {
			System.gc();
		}
	}

	public void processActionSearch(Intent i) {
		if (Intent.ACTION_SEARCH.equals(i.getAction())) {
			String q = i.getStringExtra(SearchManager.QUERY);
			if (q == null)
				q = i.getDataString();
			final String query = q;
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						handler.sendEmptyMessage(Map.SHOW_LOADING);
						if (query != null && query.length() > 0) {
							final ResultSearchOverlay overlay = (ResultSearchOverlay) osmap
									.getOverlay(ResultSearchOverlay.DEFAULT_NAME);

							ArrayList list = ((PerstOsmPOIProvider) POIProviderManager
									.getInstance().getPOIProvider())
									.fullTextSearch(query);
							if (list != null && list.size() > 0) {
								overlay.onSearchResults(query, list);
								overlay.setVisible(true);
								POICategories.resultSearchSelected = true;
								Message msg = Message.obtain();
								msg.what = Map.SHOW_TOAST;
								msg.obj = String.format(
										MapPOI.this.getResources().getString(
												R.string.num_results_found),
										list.size());
								MapPOI.this.handler.sendMessage(msg);
								CategoriesListView c = (CategoriesListView) sliding
										.findViewById(R.id.categories_list_view);
								CheckBoxBulletAdapter chk = ((CheckBoxBulletAdapter) c
										.getAdapter());
								chk.selected[1] = true;
							} else {
								overlay.setVisible(false);
								POICategories.resultSearchSelected = false;
								Message msg = Message.obtain();
								msg.what = Map.SHOW_TOAST;
								msg.obj = MapPOI.this.getResources().getString(
										R.string.no_results);
								MapPOI.this.handler.sendMessage(msg);
							}
						} else {
							Message msg = Message.obtain();
							msg.what = Map.SHOW_TOAST;
							msg.obj = MapPOI.this.getResources().getString(
									R.string.fill_text);
							MapPOI.this.handler.sendMessage(msg);
						}
					} catch (Exception e) {
						Log.e("", "processActionSearch error");
					} finally {
						handler.sendEmptyMessage(Map.HIDE_LOADING);
					}
				}
			});
			t.start();
			// Intent newIntent = new Intent(this, ResultSearchActivity.class);
			// newIntent.putExtra(SearchActivity.HIDE_AUTOTEXTVIEW, true);
			// newIntent.putExtra(ResultSearchActivity.QUERY, query.toString());
			// fillSearchCenter(newIntent);
			// startActivity(newIntent);
			// return;
		}
	}

	public void setLoadingVisible(boolean visible) {
		if (loadingResults != null)
			if (visible)
				loadingResults.setVisibility(View.VISIBLE);
			else
				loadingResults.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (this.sliding.isOpened()) {
					this.sliding.close();
					return true;
				}

				if (super.onKeyDown(keyCode, event))
					return true;
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			return false;
		}
		// return false;

	}

	public boolean isPOISlideShown() {
		return isPOISlideShown;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean result = true;
		try {
			switch (item.getItemId()) {
			case SEARCH_MENU:
				try {
					onSearchRequested();
				} catch (Exception e) {

				}
				break;
			case ADV_SEARCH_MENU:
				try {
					Intent mainIntent = new Intent(this,
							SearchExpandableActivity.class);
					// Point center = this.osmap.getMRendererInfo().getCenter();
					fillSearchCenter(mainIntent);
					this.startActivity(mainIntent);
				} catch (Exception e) {
					// log.log(Level.SEVERE, "OpenWebsite: ", e);
				}
				break;
			case MY_LOC_MENU:
				try {
					if (this.mMyLocationOverlay.mLocation == null
							|| (this.mMyLocationOverlay.mLocation
									.getLatitudeE6() == 0 && this.mMyLocationOverlay.mLocation
									.getLongitudeE6() == 0)) {
						Toast.makeText(this, R.string.Map_24, Toast.LENGTH_LONG)
								.show();
						return true;
					}
					this.osmap
							.adjustViewToAccuracyIfNavigationMode(this.mMyLocationOverlay.mLocation.acc);
					this.osmap
							.setMapCenterFromLonLat(
									this.mMyLocationOverlay.mLocation
											.getLongitudeE6() / 1E6,
									this.mMyLocationOverlay.mLocation
											.getLatitudeE6() / 1E6);

				} catch (Exception e) {

				}
				break;
			case FAV_MENU:
				try {
					Point center = this.osmap.getMRendererInfo().getCenter();
					double[] lonlat = ConversionCoords.reproject(center.getX(),
							center.getY(), CRSFactory.getCRS(this.osmap
									.getMRendererInfo().getSRS()), CRSFactory
									.getCRS("EPSG:900913"));
					InvokeIntents.launchListBookmarks(this, lonlat);
				} catch (Exception e) {

				}
				break;
			case NEAR_LOC_MENU:
				if (this.mMyLocationOverlay.mLocation == null
						|| (this.mMyLocationOverlay.mLocation.getLatitudeE6() == 0 && this.mMyLocationOverlay.mLocation
								.getLongitudeE6() == 0)) {
					Toast.makeText(this, R.string.Map_24, Toast.LENGTH_LONG)
							.show();
				} else {
					Point p = new Point(
							this.mMyLocationOverlay.mLocation.getLongitudeE6() / 1E6,
							this.mMyLocationOverlay.mLocation.getLatitudeE6() / 1E6);
					InvokeIntents.findPOISNear(this, p.toShortString(2));
				}
				break;
			case SETTINGS_MENU:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivity(i);
				break;
			case MAPS_MENU:
				try {
					viewLayers();
				} catch (Exception e) {

				}
				break;
			case NAV_MENU:
				try {
					recenterOnGPS = !recenterOnGPS;

					if (locationItem != null)
						locationItem.setEnabled(!recenterOnGPS);
					if (searchItem != null)
						searchItem.setEnabled(!recenterOnGPS);
					if (advSearchItem != null)
						advSearchItem.setEnabled(!recenterOnGPS);
					if (settingsItem != null)
						settingsItem.setEnabled(!recenterOnGPS);

					// myGPSButton.setEnabled(!recenterOnGPS);

					if (!recenterOnGPS) {
						z.setVisibility(View.VISIBLE);
						nagItem.setIcon(R.drawable.menu_navigation);

					} else {
						z.setVisibility(View.INVISIBLE);
						nagItem.setIcon(R.drawable.menu_navigation_off);
					}

					//
					if (!navigation) {

						this.initializeSensor(this, true);
						this.showNavigationModeAlert();
					} else {
						try {
							if (Settings.getInstance().getBooleanValue(
									getText(R.string.settings_key_orientation)
											.toString()))
								this.stopSensor(this);
						} catch (Exception e) {

						}
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
						navigation = false;
						osmap.setKeepScreenOn(false);
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
					// log.log(Level.SEVERE, "switchPanMode: ", e);
				}
				break;
			case LICENSE_MENU:
				try {
					showLicense();
				} catch (Exception e) {

				}
				break;
			case ABOUT_MENU:
				try {
					showAboutDialog();
				} catch (Exception e) {

				}
				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu pMenu) {
		try {

			searchItem = pMenu.add(0, SEARCH_MENU, 0,
					R.string.alert_dialog_text_search).setIcon(
					R.drawable.menu00);

			advSearchItem = pMenu.add(0, ADV_SEARCH_MENU, 2,
					R.string.advanced_search).setIcon(R.drawable.menu00);

			locationItem = pMenu.add(0, MY_LOC_MENU, 4, R.string.Map_4)
					.setIcon(R.drawable.menu_location);

			pMenu.add(0, FAV_MENU, 5, R.string.bookmark_title).setIcon(
					R.drawable.bookmark_38);// .setEnabled(connection);

			pMenu.add(0, NEAR_LOC_MENU, 7, R.string.nearest_places).setIcon(
					R.drawable.menu00);

			settingsItem = pMenu.add(0, SETTINGS_MENU, 8, R.string.Map_31)
					.setIcon(android.R.drawable.ic_menu_preferences);

			pMenu.add(0, MAPS_MENU, 9, R.string.Map_5).setIcon(
					R.drawable.menu02);

			nagItem = pMenu.add(0, NAV_MENU, 10, R.string.Map_Navigator)
					.setIcon(R.drawable.menu_navigation).setEnabled(connection);

			pMenu.add(0, LICENSE_MENU, 11, R.string.Map_29);

			pMenu.add(0, ABOUT_MENU, 12, R.string.Map_28);
		} catch (Exception e) {
			// log.log(Level.SEVERE, "onCreateOptionsMenu: ", e);
		}
		return true;
	}

	/**
	 * Starts the LayersActivity with the MapState.gvTilesPath file
	 */
	public void viewLayers() {
		try {
			// log.log(Level.FINE, "Launching load layers activity");
			Intent intent = new Intent(this, POILayersActivity.class);

			intent.putExtra("loadLayers", true);
			intent.putExtra("gvtiles", mapState.gvTilesPath);

			startActivityForResult(intent, 1);
		} catch (Exception e) {
			// log.log(Level.SEVERE, "viewLayers: ", e);
		}
	}
}
