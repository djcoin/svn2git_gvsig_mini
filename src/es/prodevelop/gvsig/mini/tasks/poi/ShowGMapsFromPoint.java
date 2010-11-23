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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 *   
 */

package es.prodevelop.gvsig.mini.tasks.poi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import es.prodevelop.gvsig.mini.geom.Point;

public class ShowGMapsFromPoint {

	private Activity activity;
	private Point p;
	private final static int DEFAULT_ZOOM = 17;

	/**
	 * 
	 * @param activity
	 * @param p
	 *            lon lat point
	 */
	public ShowGMapsFromPoint(Activity activity, Point p) {
		this.activity = activity;
		this.p = p;
	}

	/**
	 * Launches gmaps
	 */
	public void execute() {
		try {
			String uri = "geo:" + p.getY() + "," + p.getX() + "?z="
					+ DEFAULT_ZOOM;
			InvokeIntents.invokeURI(activity, uri);
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}

}
