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
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.tasks.yours;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import org.apache.http.util.ByteArrayBuffer;

import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.utiles.Tags;
import es.prodevelop.gvsig.mini.yours.Route;

/**
 * Queries the YOURS service with the start and end point, set by the user, parses 
 * the response and notifies the MapHandler:
 * Map.ROUTE_CANCELED = The user cancels the route task
 * Map.ROUTE_SUCCEEDED = The route has been calculated
 * Map.ROUTE_INITED = THe route task inits
 * Map.ROUTE_NO_CALCULATED = The server can't calculate the route
 * Map.ROUTE_NO_RESPONSE = The server is busy				
 * @author aromeu 
 * @author rblanco
 *
 */
public class YOURSFunctionality extends Functionality {

	Route route;
	int res = TaskHandler.INITED;
	YOURSHandler handler;
	private final static Logger log = LoggerFactory.getLogger(YOURSFunctionality.class);

	public YOURSFunctionality(Map map, int id) {
		super(map, id);
		this.route = map.route;
		handler = new YOURSHandler();
		this.addObserver(handler);
	}
	
	public Handler getHandler() {
		return handler;
	}

	@Override
	public boolean execute() {
		try {
			final Point ini = (Point) route.getStartPoint().clone();
			final Point end = (Point) route.getEndPoint().clone();

			final String routeString = route.toYOURS(ini, end);
			
			log.debug(routeString);

			/* Define the URL we want to load data from. */
			URL parseURL = new URL(routeString);
			/* Open a connection to that URL. */
			if (this.isCanceled()) {
				res = TaskHandler.CANCELED;
				return true;
			}
			URLConnection urlconnec = parseURL.openConnection();

			urlconnec.setRequestProperty("X-Yours-client", "gvSIG");
			urlconnec.setReadTimeout(30000);
			/* Define InputStreams to read from the URLConnection. */
			InputStream is = urlconnec.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * Read bytes to the Buffer until there is nothing more to read(-1).
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				if (isCanceled()) {
					res = TaskHandler.CANCELED;
					return true;
				}
				baf.append((byte) current);
			}

			res = route.fromYOURS(baf.toByteArray());
		} catch (IOException e) {
			res = TaskHandler.NO_RESPONSE;
		} catch (Exception e) {
			res = TaskHandler.ERROR;
			log.error(e);
		} finally {
			// handler.sendEmptyMessage(map.ROUTE_SUCCEEDED);

			try {
				// if (res == -1) {
				// handler.sendEmptyMessage(map.ROUTE_CANCELED);
				if (res == -2) {
					res = TaskHandler.NO_RESPONSE;
				} else if (res == -3) {
					res = TaskHandler.BAD_RESPONSE;
				} else if (res == 1){
					res = TaskHandler.FINISHED;
					if (route.getState() == Tags.ROUTE_WITH_START_AND_PASS_POINT) {
						route.setState(Tags.ROUTE_WITH_N_POINT);
					} else if (route.getState() == Tags.ROUTE_WITH_START_POINT) {
						route.setState(Tags.ROUTE_WITH_2_POINT);
					}
				}
			} catch (Exception e) {
				log.error(e);
			} finally {		
//				super.stop();
			}
			return true;
		}

	}

	@Override
	public int getMessage() {
		return res;
	}

	private class YOURSHandler extends TaskHandler {

		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {						
			case TaskHandler.CANCELED:
				getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_CANCELED);
				break;
			case TaskHandler.FINISHED:
				getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_SUCCEEDED);
				break;
			case TaskHandler.INITED:
				getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_INITED);
				break;
			case TaskHandler.ERROR:
				getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_NO_CALCULATED);
				break;
			case TaskHandler.NO_RESPONSE:
				getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_NO_RESPONSE);
				break;
			case TaskHandler.BAD_RESPONSE:
				getMap().getMapHandler().sendEmptyMessage(Map.ROUTE_NO_CALCULATED);
				break;
			case Map.ROUTE_ORIENTATION_CHANGED:
//				route.deleteRoute();				
//				cancel();
				break;			
			}
		}
	}
}
