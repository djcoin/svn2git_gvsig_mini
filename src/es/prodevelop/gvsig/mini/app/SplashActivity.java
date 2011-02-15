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
 *   author Rub�n Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from
 * http://www.anddev.org/simple_splash_screen_-_alternative-t815.html
 * package org.anddev.android.andnav;
 * 
 * No License stated in that website
 *  
 * 
 */

package es.prodevelop.gvsig.mini.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.LogFeedbackActivity;
import es.prodevelop.gvsig.mini.activities.MapPOI;
import es.prodevelop.gvsig.mini.activities.Settings;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.Constants;

/**
 * gvSIG Mini splash. Waits SPLASH_DISPLAY_LENGHT to start the Map Activity
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 500;
	private final static Logger logger = Logger.getLogger(SplashActivity.class
			.getName());
	private Handler handler = new InitializerHandler();
	private boolean singleTaskActivityResulted = false;
	public final static String OFFLINE_INTENT_ACTION = "es.prodevelop.action.OFFLINEMAP";
	private boolean intentProcessed = false;
	private double lat = 0;
	private double lon = 0;
	private int z = -1;	

	/** Splash Screen gvSIG. */
	@Override
	public void onCreate(Bundle icicle) {
		try {
			Log.d("Splash", "onCreate");
			super.onCreate(icicle);
			setContentView(R.layout.main);
			((ProgressBar) SplashActivity.this.findViewById(R.id.ProgressBar01))
					.setVisibility(View.INVISIBLE);
			Settings.getInstance().initializeFromSharedPreferences(
					getApplicationContext());
			onNewIntent(getIntent());
		} catch (Exception e) {
			Log.d("Splash", e.getMessage());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		try {
			Log.d("Splash", "onNewIntent");
			super.onNewIntent(intent);
			Log.d("Splash", "Intent:" + intent.getAction() + "/" + intent.getCategories().toString());
			if (intent != null
					&& intent.getAction().compareTo(OFFLINE_INTENT_ACTION) == 0) {
				Log.d("", "OFFLINE_INTENT_ACTION");
				this.setIntent(intent);
			} else if (intent.getAction().equals(
					android.content.Intent.ACTION_VIEW)) {
				setIntent(intent);
			}
			
			// Maybe used when the application is not launched for the first time
			if (Initializer.isInitialized) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						try {

							Intent mainIntent = new Intent(SplashActivity.this,
									Utils.DEFAULT_MAP_CLASS);
							// Initializer.getInstance().initialize(
							// getApplicationContext());
							fillIntent(mainIntent);
							SplashActivity.this.startActivityForResult(
									mainIntent, 0);
							// SplashActivity.this.finish();
						} catch (Exception e) {
							logger.log(Level.SEVERE, "", e);
						}
					}
				}, SPLASH_DISPLAY_LENGHT);
			} else {
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						Initializer.getInstance()
								.addInitializeListener(handler);
						try {
							Initializer.getInstance().initialize(
									getApplicationContext());
						} catch (Exception e) {
							logger.log(Level.SEVERE, "onCreate", e);
							LogFeedbackActivity
									.showSendLogDialog(SplashActivity.this);
						}
					}
				}).start();
			}
		} catch (Exception e) {
			Log.d("Splash", e.getMessage());
		}
	}

	public void onPause() {
		try {
			super.onPause();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	public void onResume() {
		try {
			if (singleTaskActivityResulted) {
				((TextView) SplashActivity.this.findViewById(R.id.app_name))
						.setText(R.string.relaunch);
			}
			if (Initializer.isInitialized)
				singleTaskActivityResulted = true;
			super.onResume();
			Intent i = getIntent();
			if (i == null)
				return;
			if (i.hasExtra("exit") /* || singleTaskActivityResulted */) {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		try {
			super.onActivityResult(requestCode, resultCode, intent);

			if (requestCode == 0) {
				// singleTaskActivityResulted = true;
				switch (resultCode) {
				case RESULT_OK:
					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					break;
				default:
					// finish();
					break;
				}
			} else {
				// finish();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				logger.log(Level.FINE, "KEY BACK pressed");
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "onKeyDown: ", e);
			return false;
		}
		// return false;
	}

	public boolean onTouchEvent(MotionEvent e) {
		try {
			if (singleTaskActivityResulted)
				this.handler.sendEmptyMessage(Initializer.INITIALIZE_FINISHED);
			return true;
		} catch (Exception e1) {
			logger.log(Level.SEVERE, "onKeyDown: ", e1);
			return true;
		}
	}
	
	// Fill an intent base on its own intent
	private void fillIntent(Intent mainIntent) {
		Intent activityIntent = this.getIntent();
		if (activityIntent == null) {
			Log.d("Splash", "Intent of splash is null:");
			return;
		}
		mainIntent.setAction(SplashActivity.OFFLINE_INTENT_ACTION);
		mainIntent.putExtra("lat", 0);
		mainIntent.putExtra("lon", 0);
		mainIntent.putExtra("zoom", 0);
		// Map.processOffline...
		// String URL="OPEN STREET MAP;0,http://a.tile.openstreetmap.org/>http://b.tile.openstreetmap.org/>http://c.tile.openstreetmap.org/,png,18,0,256,-2.0037508342789244E7,-2.0037508342789244E7,-2.0037508342789244E7,-2.0037508342789244E7,2.0037508342789244E7,2.0037508342789244E7,EPSG:900913,156543.03392804096153584694438047:78271.516964020480767923472190235:39135.758482010240383961736095118:19567.879241005120191980868047559:9783.9396205025600959904340237794:4891.9698102512800479952170118897:2445.9849051256400239976085059448:1222.9924525628200119988042529724:611.49622628141000599940212648621:305.74811314070500299970106324311:152.87405657035250149985053162155:76.437028285176250749925265810776:38.218514142588125374962632905388:19.109257071294062687481316452694:9.5546285356470313437406582263471:4.7773142678235156718703291131735:2.3886571339117578359351645565868:1.1943285669558789179675822782934:0.59716428347793945898379113914669:0.29858214173896972949189556957335:0.14929107086948486474594778478667:0.074645535434742432372973892393336:0.037322767717371216186486946196668:0.018661383858685608093243473098334:0.009330691929342804046621736549167";
		// taken from layers.txt
		String URL="OPEN STREET MAP;0[],http://a.tile.openstreetmap.org/>http://b.tile.openstreetmap.org/>http://c.tile.openstreetmap.org/,png,18,0,256,-2.0037508342789244E7,-2.0037508342789244E7,-2.0037508342789244E7,-2.0037508342789244E7,2.0037508342789244E7,2.0037508342789244E7,EPSG:900913,156543.03392804096153584694438047:78271.516964020480767923472190235:39135.758482010240383961736095118:19567.879241005120191980868047559:9783.9396205025600959904340237794:4891.9698102512800479952170118897:2445.9849051256400239976085059448:1222.9924525628200119988042529724:611.49622628141000599940212648621:305.74811314070500299970106324311:152.87405657035250149985053162155:76.437028285176250749925265810776:38.218514142588125374962632905388:19.109257071294062687481316452694:9.5546285356470313437406582263471:4.7773142678235156718703291131735:2.3886571339117578359351645565868:1.1943285669558789179675822782934:0.59716428347793945898379113914669:0.29858214173896972949189556957335:0.14929107086948486474594778478667:0.074645535434742432372973892393336:0.037322767717371216186486946196668:0.018661383858685608093243473098334:0.009330691929342804046621736549167";
		mainIntent.putExtra(Constants.URL_STRING, URL);
		if (true)
			return;
		
		final Uri data = activityIntent.getData();
		if (data != null && data.getScheme().equals("geo")) {
			
			Log.d("Splash", "Intent content:" + activityIntent.getData().toString());
			
			final String coordsString = activityIntent.getData()
					.getSchemeSpecificPart();
			if (coordsString.length() > 0) {
				final String[] coordinates = coordsString.split(",");
				try {
					lat = Double.parseDouble(coordinates[0]);
					int indexZ = coordinates[1].toLowerCase().indexOf("z");
					if (indexZ == -1)
						lon = Double.parseDouble(coordinates[1]);
					else {
						String c = coordinates[1].substring(0, indexZ - 1);
						lon = Double.parseDouble(c);
					}

					z = 15;
				} catch (final NumberFormatException nfe) {
					Log.d("", activityIntent.getData().toString());
				} catch (Exception e) {
					Log.d("", e.getMessage());
				}
			}
		}
		mainIntent.putExtra("lat", lat);
		mainIntent.putExtra("lon", lon);
		mainIntent.putExtra("zoom", z);

		// FIXME
		// String URL = activityIntent.getStringExtra(Constants.URL_STRING);

		if (URL != null) {
			mainIntent.putExtra(Constants.URL_STRING, URL);
			mainIntent.setAction(SplashActivity.OFFLINE_INTENT_ACTION);
		}
	}

	private class InitializerHandler extends Handler {

		public void handleMessage(final Message msg) {
			try {
				switch (msg.what) {
				case Initializer.INITIALIZE_STARTED:
					ProgressBar t = (ProgressBar) SplashActivity.this
							.findViewById(R.id.ProgressBar01);
					t.setVisibility(View.VISIBLE);
					t.setIndeterminate(true);
					break;
				case Initializer.INITIALIZE_FINISHED:
					Intent mainIntent = new Intent(SplashActivity.this, Utils.DEFAULT_MAP_CLASS);
					fillIntent(mainIntent);
					SplashActivity.this.startActivityForResult(mainIntent, 0);
					((ProgressBar) SplashActivity.this
							.findViewById(R.id.ProgressBar01))
							.setVisibility(View.INVISIBLE);
					break;
				default:
					runOnUiThread(new Runnable() {
						public void run() {
							TextView t1 = (TextView) SplashActivity.this
									.findViewById(R.id.app_name);
							t1.setText(String.valueOf(msg.what));
						}
					});

					break;
				}
			} catch (Exception e) {
				Log.d("", e.getMessage());
			}
		}
	}
}