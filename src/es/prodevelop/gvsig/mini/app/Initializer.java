/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 *	 2010.
 *   author Alberto Romeu - aromeu@prodevelop.es
 *   
 */

package es.prodevelop.gvsig.mini.app;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.common.IContext;
import es.prodevelop.gvsig.mini.common.android.AndroidContext;
import es.prodevelop.gvsig.mini.common.android.LogHandler;
import es.prodevelop.gvsig.mini.location.Config;
import es.prodevelop.gvsig.mini.settings.Settings;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Constants;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.provider.filesystem.strategy.impl.FileSystemStrategyManager;
import es.prodevelop.tilecache.provider.filesystem.strategy.impl.FlatXFileSystemStrategy;
import es.prodevelop.tilecache.provider.filesystem.strategy.impl.QuadKeyFileSystemStrategy;
import es.prodevelop.tilecache.renderer.MapRendererManager;
import es.prodevelop.tilecache.renderer.wms.WMSMapRendererFactory;

/**
 * A Facade to do all the initialization stuff
 * 
 * @author aromeu
 * 
 */
public class Initializer {

	private static Initializer instance;
	private Context applicationContext;

	private IContext aContext;
	
	public static boolean isInitialized = false;
	private Handler handler;
	
	public final static int INITIALIZE_STARTED = 0;
	public final static int INITIALIZE_FINISHED = 1;

	/**
	 * A Singleton
	 * 
	 * @return
	 */
	public static Initializer getInstance() {
		if (instance == null)
			instance = new Initializer();
		return instance;
	}

	public void initialize(Context applicationContext) throws Exception {
		if (this.handler != null) 
			handler.sendEmptyMessage(INITIALIZE_STARTED);
		
		this.applicationContext = applicationContext;

		CompatManager.getInstance().registerContext(
				new AndroidContext(this.applicationContext));
		CompatManager.getInstance().registerLogHandler(new LogHandler());
		CompatManager.getInstance().getRegisteredLogHandler().configureLog();
		
		Settings.getInstance().initializeFromSharedPreferences(
				applicationContext);

		FileSystemStrategyManager.getInstance().registerFileSystemStrategy(
				new FlatXFileSystemStrategy());
		FileSystemStrategyManager.getInstance().registerFileSystemStrategy(
				new QuadKeyFileSystemStrategy());

		MapRendererManager.getInstance().registerMapRendererFactory(
				new WMSMapRendererFactory());

		Config.setContext(this.getApplicationContext());
		ResourceLoader.initialize(this.getApplicationContext());

		aContext = new AndroidContext(this.getApplicationContext());
		CompatManager.getInstance().registerContext(aContext);
		Layers.getInstance().initialize(true);

		Constants.ROOT_DIR = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		
		if (this.handler != null) 
			handler.sendEmptyMessage(INITIALIZE_FINISHED);
		
		isInitialized = true;
	}

	/**
	 * The application context
	 * 
	 * @return
	 */
	public Context getApplicationContext() {
		return applicationContext;
	}
	
	public void addInitializeListener(Handler handler) {
		this.handler =  handler;
	}

}
