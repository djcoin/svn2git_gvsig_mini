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
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;

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

	/** Splash Screen gvSIG. */
	@Override
	public void onCreate(Bundle icicle) {
		try {
			super.onCreate(icicle);
			setContentView(R.layout.main);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					try {						
						Intent mainIntent = new Intent(SplashActivity.this,
								Map.class);
						SplashActivity.this.startActivityForResult(mainIntent,
								0);
						// SplashActivity.this.finish();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "", e);
					}
				}
			}, SPLASH_DISPLAY_LENGHT);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		Intent i = getIntent();
		if (i == null) return;
		if (i.hasExtra("exit")) {
			finish();
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		try {
			super.onActivityResult(requestCode, resultCode, intent);

			if (requestCode == 0) {
				switch (resultCode) {
				case RESULT_OK:
					finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					break;				
				default:
//					finish();
					break;
				}
			} else {
//				finish();
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
			logger.log(Level.SEVERE,"onKeyDown: ", e);
			return false;
		}
		// return false;

	}
}