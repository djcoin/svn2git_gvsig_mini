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
 */

package es.prodevelop.gvsig.mini.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.views.overlay.LongTextAdapter;

public class AboutActivity extends LogFeedbackActivity {
	
	private final static Logger log = Logger.getLogger(AboutActivity.class.getName());
	
	public void onCreate(Bundle savedInstanceState) {
		try {			
			super.onCreate(savedInstanceState);
			try {
				CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
			} catch (BaseException e) {
				
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreate", e);
		}
	}
	
	public void showDialogFromFile(String assetsFile, int id) {
		try {
			String license = "";
			// String aFile = "about.txt";
			InputStream is = this.getAssets().open(assetsFile);
			try {
				// Resources resources = getPackageManager()
				// .getResourcesForApplication(packagename);

				// Read in the license file as a big String
				BufferedReader input = new BufferedReader(
						new InputStreamReader(is));
				// BufferedReader in
				// = new BufferedReader(new InputStreamReader(
				// resources.openRawResource(resourceid)));
				String line;
				StringBuilder sb = new StringBuilder();
				try {
					while ((line = input.readLine()) != null) { // Read line per
						// line.
						if (TextUtils.isEmpty(line)) {
							// Empty line: Leave line break
							sb.append("\n\n");
						} else {
							sb.append(line);
							sb.append(" ");
						}
					}
					license = sb.toString();
				} catch (IOException e) {
					// Should not happen.
					e.printStackTrace();
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, "", e);
			}

			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(id);
			ListView l = new ListView(this);
			l.setAdapter(new LongTextAdapter(this, license, false));
			l.setClickable(false);
			l.setLongClickable(false);
			l.setFocusable(false);
			alert.setView(l);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public void showAboutDialog() {
		try {
			this.showDialogFromFile("about.txt", R.string.Map_28);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public void showLicense() {
		try {
			this.showDialogFromFile("license.txt", R.string.Map_29);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public void showWhatsNew() {
		try {
			this.showDialogFromFile("whatsnew.txt", R.string.Map_30);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

}
