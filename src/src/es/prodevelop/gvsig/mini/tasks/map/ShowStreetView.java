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
 */

package es.prodevelop.gvsig.mini.tasks.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;

/**
 * Sends an Intent action to launch the Google Street View activity with the
 * current lon, lat of the Map Activity 
 * @author rblanco
 *
 */
public class ShowStreetView extends Functionality {
	private final static Logger log = LoggerFactory.getLogger(ShowStreetView.class);
	public ShowStreetView(Map map, int id) {
		super(map, id);
	}
	
	@Override
	public boolean execute() {
		try {
			String uri = "google.streetview:cbll=" + getMap().osmap.getCenterLonLat()[1]  + "," 
			                         + getMap().osmap.getCenterLonLat()[0]       
			                         + "&cbp=1,45,,45,1.0&mz=" + getMap().osmap.getZoomLevel(); 
			Intent intent = new Intent(); 
			intent.setData(Uri.parse(uri)); 
			intent.setAction("android.intent.action.VIEW"); 
			getMap().startActivity(intent);
				
			} catch (Exception e) {
				log.error("Street View", e);
			}
			return true;
	}

	@Override
	public int getMessage() {		
		return TaskHandler.FINISHED;
	}
	
	

}
