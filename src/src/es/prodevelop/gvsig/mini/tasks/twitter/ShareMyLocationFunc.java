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
 *   author Ruben Blanco rblanco @prodevelop.es 
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
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;

public class ShareMyLocationFunc extends TweetMyLocationFunc {

	private final static Logger log = LoggerFactory
			.getLogger(ShareMyLocationFunc.class);
	private int res = TaskHandler.FINISHED;

	public ShareMyLocationFunc(Map map, int id) {
		super(map, id);
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
			log.debug("share");
			final Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, text);
			Intent i = Intent.createChooser(intent, getMap().getString(R.string.share));
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//
			getMap().startActivityForResult(i, 2385);
		} catch (Exception e) {
			log.error("share", e);
		}
	}

	@Override
	public boolean execute() {
		try {
		share(buildTweet());
//			String user = getMap().twituser;
//			String pass = getMap().twitpass;
//
//			if (user != null && pass != null) {
//				this.sendMyLocationTweet(user, pass);
//			} else {
//				getMap().getMapHandler()
//						.sendEmptyMessage(Map.SHOW_TWEET_DIALOG);
//			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			return true;
		}
	}

	@Override
	public int getMessage() {
		return res;
	}

}
