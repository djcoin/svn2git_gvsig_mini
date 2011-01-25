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

			for (int i = size; i >= 0; i--) {
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
