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

package es.prodevelop.gvsig.mini.tasks.twitter;

import java.io.IOException;
import java.net.UnknownHostException;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import winterwell.jtwitter.Twitter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;

public class TweetMyLocationFunc extends Functionality {

	private final static Logger log = LoggerFactory
			.getLogger(TweetMyLocationFunc.class);
	private int res = TaskHandler.FINISHED;

	public TweetMyLocationFunc(Map map, int id) {
		super(map, id);
	}

	@Override
	public boolean execute() {
		try {
			String user = getMap().twituser;
			String pass = getMap().twitpass;

			if (user != null && pass != null) {
				this.sendMyLocationTweet(user, pass);
			} else {
				getMap().getMapHandler()
						.sendEmptyMessage(Map.SHOW_TWEET_DIALOG);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			return true;
		}
	}

	private void sendMyLocationTweet(String user, String pass) {
		try {			
			Twitter twitter = new Twitter(user, pass);
			double[] lonlat = this.getLatLonTrimed();
			
			twitter.setStatus(this.buildTweet(), String.valueOf(lonlat[1]), String.valueOf(lonlat[0]));
			getMap().getMapHandler().sendEmptyMessage(Map.TWEET_SENT);		
		} catch (Exception e) {
			log.error(e);
			Message msg = getMap().getMapHandler().obtainMessage();
			msg.obj = e.getMessage();
			msg.what = Map.TWEET_ERROR;
			getMap().getMapHandler().sendMessage(msg);
		}
	}

	@Override
	public int getMessage() {
		return res;
	}
	
	/**
	 * Trims the current map center to 6 decimals
	 * @return The lon/lat map center with 6 decimals
	 */
	public double[] getLatLonTrimed() {
		try {
			double[] coords = getMap().osmap.getCenterLonLat();			
			
			int lat = (int) (coords[1] * 1E6);
			int lon = (int) (coords[0] * 1E6);
			
			double latDouble = lat / 1E6;
			double lonDouble = lon / 1E6;
			
			return new double[]{lonDouble, latDouble};
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * Builds the tweet message: 
	 * Mi Ubicación : http://www.opentouchmap.org/?lat=39.472393&lon=-0.382493&zoom=14 lat:39.472393 lon:-0.382493
	 * @return A string to tweet
	 */
	public String buildTweet() {
		try {
			
			final double[] res = this.getLatLonTrimed();
			
			double latDouble = res[1];
			double lonDouble = res[0];
			
			int zoomin = getMap().osmap.getZoomLevel();
			
			return new StringBuffer().append(getMap().getResources().getString(
					R.string.TweetMyLocationFunc_0)).append(
					" : http://www.opentouchmap.org/?lat=").append(
					latDouble).append("&lon=").append(lonDouble).append(
					"&zoom=").append(
					zoomin).append(
					" ").append(getMap().getResources().getString(R.string.lat)).append(":")
					.append(
					latDouble).append(
					" ").append( 
					getMap().getResources().getString(R.string.lon)).append(
					":").append(
					lonDouble).toString();
			
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

}
