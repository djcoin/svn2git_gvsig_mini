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

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.settings.LogHandler;
import es.prodevelop.gvsig.mini.util.Utils;

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
						LogHandler.getInstance().configureLog();
						Intent mainIntent = new Intent(SplashActivity.this,
								Map.class);
						SplashActivity.this.startActivity(mainIntent);
						SplashActivity.this.finish();
					} catch (Exception e) {
						logger.log(Level.SEVERE, "", e);
					}
				}
			}, SPLASH_DISPLAY_LENGHT);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

}