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
 * Original source code downloaded from
 * http://www.anddev.org/simple_splash_screen_-_alternative-t815.html
 * package org.anddev.android.andnav;
 * 
 * No License stated in that website
 *  
 * 
 */

package es.prodevelop.gvsig.mini.app;



import net.sf.microlog.android.appender.SDCardAppender;
import net.sf.microlog.core.Level;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import net.sf.microlog.core.appender.ConsoleAppender;
import android.content.Intent;
import android.os.Bundle;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.util.Utils;
import android.app.Activity;
import android.os.Handler;

/**
 * gvSIG Mini splash. Waits SPLASH_DISPLAY_LENGHT to start the Map Activity
 * @author aromeu 
 * @author rblanco
 *
 */
public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 500;
	private final static Logger log = LoggerFactory.getLogger(SplashActivity.class);

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
						Intent mainIntent = new Intent(SplashActivity.this, Map.class);
						SplashActivity.this.startActivity(mainIntent);
						SplashActivity.this.finish();
					} catch (Exception e) {
						log.error(e);
					}					
				}
			}, SPLASH_DISPLAY_LENGHT);
		} catch (Exception e) {
			log.error(e);
		}
	}

}