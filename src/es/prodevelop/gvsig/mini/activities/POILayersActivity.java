package es.prodevelop.gvsig.mini.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

import android.widget.SimpleExpandableListAdapter;
import es.prodevelop.gvsig.mini.R;

public class POILayersActivity extends LayersActivity {

	protected void loadLayersList(Hashtable layers) {
		try {
			// log.log(Level.FINE, "loadLayersList");
			List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
			List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
			final int size = layers.size();

			for (int i = size; i >= 0; i--) {
				Map<String, String> curGroupMap = new HashMap<String, String>();
				groupData.add(curGroupMap);
				Vector layersList = (Vector) layers.get(new Integer(i));
				String text = null;
				switch (i) {
				case 0:
					text = this.getResources().getString(
							R.string.LayersActivity_8);

					break;
				case 1:
					text = this.getResources().getString(
							R.string.LayersActivity_9);

					break;
				case 2:
					text = this.getResources().getString(
							R.string.LayersActivity_15);

					break;
				}
				curGroupMap.put(LAYER, text);

				// switch (i) {
				// case MapRenderer.OSM_RENDERER:
				//
				// break;
				// case MapRenderer.OSMPARMS_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Tile service (with different parameter encoding)";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.TMS_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Tile Map Service";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.QUADKEY_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Quadkey URL format";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.EQUATOR_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Y origin at equator";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.WMS_RENDERER:
				// text = "WMS layers";
				// curGroupMap.put(LAYER, text);
				// break;
				// }

				if (i == 2) {
					Map<String, String> curGroupMap1 = new HashMap<String, String>();
					groupData.add(curGroupMap1);
					curGroupMap1.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_10));

					List<Map<String, String>> children1 = new ArrayList<Map<String, String>>();

					Map<String, String> curChildMap = new HashMap<String, String>();
					curChildMap.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_11));
					curChildMap.put(
							CHILD,
							this.getResources().getString(
									R.string.LayersActivity_12));
					children1.add(curChildMap);

					Map<String, String> curChildMap2 = new HashMap<String, String>();
					curChildMap2.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_16));
					curChildMap2.put(
							CHILD,
							this.getResources().getString(
									R.string.LayersActivity_17));
					children1.add(curChildMap2);

					Map<String, String> curChildMap1 = new HashMap<String, String>();
					curChildMap1.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_13));
					curChildMap1.put(
							CHILD,
							this.getResources().getString(
									R.string.LayersActivity_14));
					children1.add(curChildMap1);
					childData.add(children1);
				}

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
