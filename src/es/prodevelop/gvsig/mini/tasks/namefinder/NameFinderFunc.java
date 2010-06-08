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

package es.prodevelop.gvsig.mini.tasks.namefinder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Message;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.namefinder.NameFinder;
import es.prodevelop.gvsig.mini.namefinder.Named;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * Requests the name finder service, parses the results and notifies the
 * MapHandler: Map.POI_CANCELED = The user cancels the task 
 * Map.POI_SHOW = THe map should show the results 
 * Map.POI_INITED = The task inits
 * TaskHandler.NO_RESPONSE = No response from the server 
 * @author aromeu 
 * @author rblanco
 * 
 */
public class NameFinderFunc extends Functionality {

	private final static Logger log = Logger
			.getLogger(NameFinderFunc.class.getName());

	public String[] desc;
	private int res;
	public NamedMultiPoint nm;

	public NameFinderFunc(Map map, int id) {
		super(map, id);
		POIHandler handler = new POIHandler();
		this.addObserver(handler);
	}

	@Override
	public boolean execute() {

		NameFinder NFTask = new NameFinder();
		String query = new String(NFTask.URL + NFTask.parms).replaceAll(" ",
				"%20");
		
		try {
			log.log(Level.FINE, query);
			InputStream is = Utils.openConnection(query);
			BufferedInputStream bis = new BufferedInputStream(is);

			/* Read bytes to the Buffer until there is nothing more to read(-1). */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				if (this.isCanceled()) {
					res = TaskHandler.CANCELED;
					return true;
				}
				baf.append((byte) current);

			}

			Vector result = NFTask.parse(baf.toByteArray());

			if (result != null) {
				// handler.sendEmptyMessage(map.POI_SUCCEEDED);
				Named[] searchRes = new Named[result.size()];
				desc = new String[result.size()];
				for (int i = 0; i < result.size(); i++) {
					Named n = (Named) result.elementAt(i);
					desc[i] = n.description;
					searchRes[i] = n;

					if (this.isCanceled()) {
						res = TaskHandler.CANCELED;
						return true;
					}
				}

				if (this.isCanceled()) {
					res = TaskHandler.CANCELED;
					return true;
				}
				nm = new NamedMultiPoint(searchRes);

				res = TaskHandler.FINISHED;
			} else {
				res = TaskHandler.CANCELED;
			}
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {
				res = TaskHandler.NO_RESPONSE;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"Namefinder" + e.getMessage(), e);
		} finally {

		}
		return true;
	}

	@Override
	public int getMessage() {
		return res;
	}

	public class POIHandler extends TaskHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TaskHandler.CANCELED:
				getMap().getMapHandler().sendEmptyMessage(Map.POI_CANCELED);
				break;
			case TaskHandler.FINISHED:
				getMap().getMapHandler().sendEmptyMessage(Map.POI_SHOW);
				break;
			case TaskHandler.INITED:
				getMap().getMapHandler().sendEmptyMessage(Map.POI_INITED);
				break;
			case TaskHandler.ERROR:
				// getMap().getMapHandler().sendEmptyMessage(Map.poi);
				break;
			case TaskHandler.NO_RESPONSE:
				getMap().getMapHandler().sendEmptyMessage(
						TaskHandler.NO_RESPONSE);
				break;
			case TaskHandler.BAD_RESPONSE:
				// getMap().getMapHandler().sendEmptyMessage(Map.
				// ROUTE_NO_CALCULATED);
				break;
			}
		}
	}

}
