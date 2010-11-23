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
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;

public class ShareAnyPOITask {

	private Activity activity;
	private POI p;

	public ShareAnyPOITask(Activity a, POI p) {
		this.activity = a;
		this.p = p;
	}

	/**
	 * Launches an Intent chooser to any application to receive the
	 * Intent.ACTION_SEND with a given text
	 * 
	 * @param text
	 *            The text to share, usually my GPS location
	 */
	public void share(String text) {
		try {

			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, text);
			Intent i = Intent.createChooser(intent,
					activity.getString(R.string.share));
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//
			activity.startActivity(i);
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}

	public void execute() {
		try {
			share(buildTweet());
			// String user = getMap().twituser;
			// String pass = getMap().twitpass;
			//
			// if (user != null && pass != null) {
			// this.sendMyLocationTweet(user, pass);
			// } else {
			// getMap().getMapHandler()
			// .sendEmptyMessage(Map.SHOW_TWEET_DIALOG);
			// }
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}

	/**
	 * Trims the current map center to 6 decimals
	 * 
	 * @return The lon/lat map center with 6 decimals
	 */
	public double[] getLatLonTrimed() {
		try {
			double[] coords = new double[] { p.getX(), p.getY() };

			int lat = (int) (coords[1] * 1E6);
			int lon = (int) (coords[0] * 1E6);

			double latDouble = lat / 1E6;
			double lonDouble = lon / 1E6;

			return new double[] { lonDouble, latDouble };
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Builds the tweet message: poi description :
	 * http://www.opentouchmap.org/?lat=39.472393&lon=-0.382493&zoom=14
	 * lat:39.472393 lon:-0.382493
	 * 
	 * @return A string to tweet
	 */
	public String buildTweet() {
		try {

			final double[] res = this.getLatLonTrimed();

			double latDouble = res[1];
			double lonDouble = res[0];

			int zoomin = 15;

			return new StringBuffer().append(p.getDescription())
					.append(" : http://www.opentouchmap.org/?lat=")
					.append(latDouble).append("&lon=").append(lonDouble)
					.append("&zoom=").append(zoomin).append(" ")
					.append(activity.getResources().getString(R.string.lat))
					.append(":").append(latDouble).append(" ")
					.append(activity.getResources().getString(R.string.lon))
					.append(":").append(lonDouble).toString();

		} catch (Exception e) {
			Log.d("", e.getMessage());
			return null;
		}
	}

}
