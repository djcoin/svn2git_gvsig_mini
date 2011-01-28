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

package es.prodevelop.gvsig.mini.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.widget.SimpleExpandableListAdapter;
import es.prodevelop.gvsig.mini.R;

public class POILayersActivity extends LayersActivity {

	protected void loadLayersList(Hashtable layers) {
		try {
			// log.log(Level.FINE, "loadLayersList");
			List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
			List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
			final int size = layers.size();

			for (int i = size - 1; i >= 0; i--) {
				if (i != 1) {
					Map<String, String> curGroupMap = new HashMap<String, String>();
					groupData.add(curGroupMap);
					Vector layersList = (Vector) layers.get(new Integer(i));
					String text = null;
					switch (i) {
					case 0:
						text = this.getResources().getString(
								R.string.other_maps);

						break;
					case 1:
						// text = this.getResources().getString(
						// R.string.LayersActivity_9);

						break;
					case 2:
						text = this.getResources()
								.getString(R.string.my_guides);

						break;
					}

					curGroupMap.put(LAYER, text);

					List<Map<String, String>> children = new ArrayList<Map<String, String>>();
					if (layersList == null) {
						// if (text != null)
						// groupData.remove(curGroupMap);
					} else {

						final int length = layersList.size();

						for (int j = 0; j < length; j++) {
							Map<String, String> curChildMap = new HashMap<String, String>();
							;
							children.add(curChildMap);

							curChildMap.put(LAYER, layersList.elementAt(j)
									.toString());
						}
					}

					childData.add(children);
				}
			}

			// Set up our adapter
			mAdapter = new SimpleExpandableListAdapter(this, groupData,
					android.R.layout.simple_expandable_list_item_1,
					new String[] { LAYER, CHILD }, new int[] {
							android.R.id.text1, android.R.id.text2 },
					childData, android.R.layout.simple_expandable_list_item_2,
					new String[] { LAYER, CHILD }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			setListAdapter(mAdapter);
		} catch (Exception e) {
			// log.log(Level.SEVERE, "loadLayersList: ", e);
		}
	}

}
