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
 */

package es.prodevelop.gvsig.mini.map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Environment;
import es.prodevelop.gvsig.mini._lg.IMap;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.geom.android.GPSPoint;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.tilecache.layers.Layers;

/**
 * Class to persist phisically the map state between sesions. The state
 * is persisted into sdcard/gvsig/Utils.CONFIG_DIR/mapstate.txt
 * @author aromeu 
 * @author rblanco
 *
 */
public class MapState {	
	
	GPSPoint center;
	int zoomLevel;
	String layerName;
	private static final String fileName = "mapstate.txt";
	private String dirPath;
	public String gvTilesPath = null;
	IMap map;
	private static final String LAT = "lat";
	private static final String LON = "lon";
	private static final String ZOOM = "zoom";
	private static final String LAYER = "layer";
	private static final String X = "x";
	private static final String Y = "y";
	private static final String GVTILES = "gvtiles";	
	private final static Logger log = Logger.getLogger(MapState.class.getName());
	
	/**
	 * The constructor
	 * @param map
	 */
	public MapState(IMap map) {	
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
			this.map = map;
			String SDDIR = Environment.getExternalStorageDirectory().getPath();
			String appDir = Utils.APP_DIR;
			String configDir = Utils.CONFIG_DIR;
			dirPath = SDDIR + File.separator + appDir + File.separator + configDir + File.separator;			
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}		
	}
	
	/**
	 * Persists the map state
	 */
	public synchronized void persist() {		
		if(true)
			return;
		try {
			if (map != null) {				
				File f = new File(dirPath + fileName);
				log.log(Level.FINE, "Persist map state to: " + f.getAbsolutePath());
				if (!f.exists()) {
					File dirFile = new File(dirPath);
					if (dirFile.mkdirs())
						f.createNewFile();	
				}
//				} else {
//					f.delete();
//					f.createNewFile();
//				}
				FileWriter logwriter = new FileWriter(f, false);

				BufferedWriter out = new BufferedWriter(logwriter);
				out.write(ZOOM + "=" + map.osmap.getZoomLevel()+"\n");
				out.write(LAYER + "=" + map.osmap.getMRendererInfo().getFullNAME()+"\n");
				
				
				out.write(X + "=" + map.osmap.getMRendererInfo().getCenter().getX() +"\n");
				out.write(Y + "=" + map.osmap.getMRendererInfo().getCenter().getY() +"\n");
				
				out.write(GVTILES +"=" +gvTilesPath);
				
				out.close();	
				log.log(Level.FINE, "Map state sucessfully persisted");
			}			
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}
	
	/**
	 * Loads the map state from the previous state
	 * @return True if the map state is loaded correctly
	 */
	public synchronized boolean load() {		
		FileReader configReader = null;
		BufferedReader reader = null;
		try {
			File f = new File(dirPath + fileName);
			System.out.println("File: " + f.getAbsolutePath());
			if (f != null && f.exists()) {
				System.out.println("File does exist");
				log.log(Level.FINE, "load map state: " + f.getAbsolutePath());
				configReader = new FileReader(f);
				reader = new BufferedReader(configReader);	
				
			} else {
				log.log(Level.FINE, "Map state not exists in disk");
				return false;				
			}
			
			String line = null;
			String[] part;
			HashMap properties = new HashMap();
			while ((line = reader.readLine()) != null) {
				System.out.println("Reading: " + line);
				part = line.split("=");
				properties.put(part[0], part[1]);
				log.log(Level.FINE, part[1]);
			}
			
			double x = 0.0;
			double y = 0.0;
			
			try {
				x = Double.valueOf(properties.get(X).toString()).doubleValue();
				y = Double.valueOf(properties.get(Y).toString()).doubleValue();
			} catch (Exception ignore) {
				
			}
			
			int zoom = Integer.valueOf(properties.get(ZOOM).toString()).intValue();
			String layer = properties.get(LAYER).toString();	
			String gvTilesPath = properties.get(GVTILES).toString();			
				
			if (gvTilesPath.compareTo("") != 0 && gvTilesPath.compareToIgnoreCase("null") != 0) {
				log.log(Level.FINE, "gvtilesPath: " + gvTilesPath);
				this.gvTilesPath = (gvTilesPath);
				Layers.getInstance().loadProperties(this.gvTilesPath);
			}
			map.osmap.onLayerChanged(layer);
			map.osmap.setMapCenter(x, y);
			map.osmap.setZoomLevel(zoom);
			
			return true;
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			try {
				myDefault();
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
				return false;
			}
			return true;
		}
	}

	private void myDefault() {
		
		String layer="maze";
		map.osmap.onLayerChanged(layer);
		map.osmap.setMapCenter(-181002.93165638, 5976152.5836365);
		map.osmap.setZoomLevel(17);
		
//		zoom=17
//		layer=maze
//		x=-181002.93165638
//		y=5976152.5836365
//		gvtiles=null
		
	}
}
