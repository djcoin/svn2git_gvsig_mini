/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2011 Prodevelop.
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
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.offline.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import es.prodevelop.android.common.preferences.offlinemaps.OfflineMapsPreferenceTags;

public class OfflineMapsReader {

	public ArrayList<String> readOfflineMaps() {
		String dirPath = OfflineMapsPreferenceTags.OFFLINE_MAPS_DIR;
		String fileName = OfflineMapsPreferenceTags.OFFLINE_MAPS_FILE;

		ArrayList<String> list = new ArrayList<String>();

		FileReader configReader = null;
		BufferedReader reader = null;
		try {
			File f = new File(dirPath + fileName);
			if (f != null && f.exists()) {
				configReader = new FileReader(f);
				reader = new BufferedReader(configReader);
			} else {
				return null;
			}

			String line = null;
			String[] part;
			HashMap properties = new HashMap();
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}

			return list;
		} catch (Exception e) {
			Log.e("", "error reading offmaps");
			return null;
		}
	}
}
