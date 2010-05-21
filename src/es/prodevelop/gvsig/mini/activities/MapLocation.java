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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Pequeña y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Rubén Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */

package es.prodevelop.gvsig.mini.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.sf.microlog.android.appender.SDCardAppender;
import net.sf.microlog.core.Level;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import net.sf.microlog.core.appender.ConsoleAppender;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.android.GPSPoint;
import es.prodevelop.gvsig.mini.location.Config;
import es.prodevelop.gvsig.mini.location.LocationHandler;
import es.prodevelop.gvsig.mini.location.LocationTimer;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * A Base Activity which Handles LocationProvider events and Sensor events
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public abstract class MapLocation extends Activity implements GeoUtils,
		SensorEventListener, LocationListener {

	private boolean isLocationHandlerEnabled = false;
	protected static final String PROVIDER_NAME = LocationManager.GPS_PROVIDER;
	protected LocationManager mLocationManager;
	protected int mBearing = 0;
	public int mNumSatellites = NOT_SET;
	private final static Logger log = LoggerFactory
			.getLogger(MapLocation.class);
	// private GPSManager manager;
	private LocationHandler locationHandler;

	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			String logFile = Utils.exitedCorrectly();
			if (logFile != null) {
				this.showSendLogDialog();
			}

			log.addAppender(new ConsoleAppender());
			SDCardAppender appender = new SDCardAppender();
			long milis = System.currentTimeMillis();
			appender.setDir(Utils.APP_DIR + File.separator + Utils.LOG_DIR);
			appender.setFileName("log" + String.valueOf(milis) + ".txt");
			appender.setContext(this);
			log.addAppender(appender);
			log.setLevel(Utils.LOG_LEVEL);
			log.setClientID(this.toString());
			log.error("Testing to log error message with Microlog.");
		} catch (Exception e) {
			log.error(e);
		}
		log.setLevel(Level.DEBUG);
		log.debug("onCreate splash actiivity");
		try {
			Config.setContext(this);
			log.setLevel(Utils.LOG_LEVEL);
			locationHandler = new LocationHandler(
					(LocationManager) getSystemService(Context.LOCATION_SERVICE),
					this, this);
			locationHandler
					.setLocationTimer(new LocationTimer(locationHandler));
			// manager = new GPSManager(getLocationManager(), this, this/*,
			// true*/);
			// manager.setGPSTask(new LocationTimer(manager));
			initLocation();
			this.initializeSensor(this);
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected void onPause() {
		try {
			log.debug("onPause MapLocation");
			super.onPause();
			this.stopSensor(this);
			// manager.stopLocationProviders();
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected void onResume() {
		try {
			log.debug("on resume MapLocation");
			super.onResume();
			this.initializeSensor(this);
			// manager.startLocationProviders();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void initializeSensor(Context context) {
		try {
			log.debug("initialize sensor");
			SensorManager mSensorManager = (SensorManager) context
					.getSystemService(Context.SENSOR_SERVICE);
			mSensorManager.registerListener(this, mSensorManager
					.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_NORMAL);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void stopSensor(Context context) {
		try {
			log.debug("stop sensor");
			SensorManager mSensorManager = (SensorManager) context
					.getSystemService(Context.SENSOR_SERVICE);
			mSensorManager.unregisterListener(this);
		} catch (Exception e) {
			log.error(e);
		}
	}

	private LocationManager getLocationManager() {
		if (this.mLocationManager == null)
			this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return this.mLocationManager;
	}

	public void initLocation() {
		try {
			List providers = this.getLocationManager().getProviders(true);
			if (providers.size() == 0) {
				log.debug("no location providers enabled");
				this.showLocationSourceDialog();
			} else {
				log.debug("location handler start");
				locationHandler.start();
				this.setLocationHandlerEnabled(true);
				// manager.initLocation();
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showLocationSourceDialog() {
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			// alert.setIcon(R.drawable.menu00);
			alert.setTitle(R.string.MapLocation_0);
			TextView text = new TextView(this);
			text.setText(R.string.MapLocation_1);

			alert.setView(text);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								startActivity(new Intent(
										"android.settings.LOCATION_SOURCE_SETTINGS"));
							} catch (Exception e) {
								log.error(e);
							}
						}
					});

			alert.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showDownloadDialog() {
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			// alert.setIcon(R.drawable.menu00);
			alert.setTitle(R.string.download_tiles_01);
			TextView text = new TextView(this);
			text.setText(R.string.download_tiles_02);

			alert.setView(text);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								Utils.downloadLayerFile(MapLocation.this);
							} catch (Exception e) {
								log.error(e);
							}
						}
					});

			alert.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showDialogFromFile(String assetsFile, int id) {
		try {
			String license = "";
			// String aFile = "about.txt";
			InputStream is = this.getAssets().open(assetsFile);
			try {
				// Resources resources = getPackageManager()
				// .getResourcesForApplication(packagename);

				// Read in the license file as a big String
				BufferedReader input = new BufferedReader(
						new InputStreamReader(is));
				// BufferedReader in
				// = new BufferedReader(new InputStreamReader(
				// resources.openRawResource(resourceid)));
				String line;
				StringBuilder sb = new StringBuilder();
				try {
					while ((line = input.readLine()) != null) { // Read line per
																// line.
						if (TextUtils.isEmpty(line)) {
							// Empty line: Leave line break
							sb.append("\n\n");
						} else {
							sb.append(line);
							sb.append(" ");
						}
					}
					license = sb.toString();
				} catch (IOException e) {
					// Should not happen.
					e.printStackTrace();
				}

			} catch (Exception e) {
				log.error(e);
			}

			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(id);
			ListView l = new ListView(this);
			l.setAdapter(new LongTextAdapter(license));
			l.setClickable(false);
			l.setLongClickable(false);
			l.setFocusable(false);
			alert.setView(l);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showAboutDialog() {
		try {
			this.showDialogFromFile("about.txt", R.string.Map_28);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showLicense() {
		try {
			this.showDialogFromFile("license.txt", R.string.Map_29);			
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showWhatsNew() {
		try {
			this.showDialogFromFile("whatsnew.txt", R.string.Map_30);			
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void showSendLogDialog() {
		try {
			log.debug("show send log dialog");
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(R.string.warning);
			TextView text = new TextView(this);
			text.setText(R.string.MapLocation_2);

			alert.setView(text);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								Utils.sendExceptionEmail(MapLocation.this);
							} catch (Exception e) {
								log.error(e);
							}
						}
					});

			alert.setNegativeButton(R.string.not_ask,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Utils.clearLogs();
						}
					});

			alert.show();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public GPSPoint getLastLocation() {
		try {
			return locationToGeoPoint(this.mLocationManager
					.getLastKnownLocation(PROVIDER_NAME));
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	/**
	 * Unregisters LocationListener.
	 */
	@Override
	protected void onDestroy() {
		try {
			log.debug("on destroy MapLocation");
			super.onDestroy();
			locationHandler.stop();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public static GPSPoint locationToGeoPoint(final Location aLoc) {
		return new GPSPoint((int) (aLoc.getLatitude() * 1E6), (int) (aLoc
				.getLongitude() * 1E6));
	}

	public LocationHandler getLocationHandler() {
		return this.locationHandler;
	}

	/**
	 * starts the LocationHandler
	 */
	public void enableGPS() {
		try {
			this.initLocation();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * Stops the location handler
	 */
	public void disableGPS() {
		try {
			this.locationHandler.stop();
			this.setLocationHandlerEnabled(false);
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void setLocationHandlerEnabled(boolean isLocationHandlerEnabled) {
		this.isLocationHandlerEnabled = isLocationHandlerEnabled;
	}

	public boolean isLocationHandlerEnabled() {
		return isLocationHandlerEnabled;
	}

	private class LongTextAdapter extends BaseAdapter {

		String text = "hola";

		public LongTextAdapter(String longText) {
			this.text = longText;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return text;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView t = new TextView(MapLocation.this);
			t.setText(text);
			return t;
		}

	}

	// public GPSManager getGPSManager() {
	// return this.manager;
	// }
}
