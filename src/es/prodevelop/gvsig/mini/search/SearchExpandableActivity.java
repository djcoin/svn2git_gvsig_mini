package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class SearchExpandableActivity extends ExpandableListActivity {

	PerstOsmPOIClusterProvider provider;

	CheckboxExpandableListAdapter expListAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.search_expandable_main);
		try {
			if (POIProviderManager.getInstance().getPOIProvider() == null)
				POIProviderManager.getInstance()
						.registerPOIProvider(
								new PerstOsmPOIClusterProvider("/sdcard/"
										+ "perst_streets_cluster_cat.db", 18,
										null, 18));
			provider = POIProviderManager.getInstance().getPOIProvider();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		expListAdapter = new CheckboxExpandableListAdapter(this,
				createGroupList(), // groupData describes the first-level
									// entries
				R.layout.search_expandable_group, // Layout for the first-level
													// entries
				new String[] { "cat", "cat_num" }, // Key in the groupData maps
													// to
													// display
				new int[] { R.id.cat, R.id.cat_num }, // Data under "colorName"
														// key goes
				// into this TextView
				createChildList(), // childData describes second-level entries
				R.layout.search_expandable_child, // Layout for second-level
													// entries
				new String[] { "sub", "sub_num" }, // Keys in childData maps
													// to display
				new int[] { R.id.sub, R.id.sub_num } // Data under the keys
														// above go into these
														// TextViews
		);
		setListAdapter(expListAdapter);
	}

	public void onContentChanged() {
		super.onContentChanged();
		Log.d("", "onContentChanged");
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		Log.d("", "onChildClick: " + childPosition);
		// CheckBox cb = (CheckBox)v.findViewById( R.id.check_child );
		//
		// if( cb != null )
		// cb.toggle();

		expListAdapter.toggleChild(groupPosition, childPosition);
		return false;
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		// TODO Auto-generated method stub
		super.onGroupCollapse(groupPosition);
	}

	public void onGroupExpand(int groupPosition) {
		super.onGroupExpand(groupPosition);
		Log.d("", "onGroupExpand: " + groupPosition);
	}

	/**
	 * Creates the group list out of the colors[] array according to the
	 * structure required by SimpleExpandableListAdapter. The resulting List
	 * contains Maps. Each Map contains one entry with key "colorName" and value
	 * of an entry in the colors[] array.
	 */
	private List createGroupList() {
		Metadata me = provider.getPOIMetadata();
		Metadata ms = provider.getStreetMetadata();
		ArrayList categories = me.getCategories();
		ArrayList result = new ArrayList();
		String cat;
		int num = 0;
		Metadata.Category category;
		final int size = categories.size();
		num = ms.getCategory(POICategories.STREETS).total;
		HashMap m1 = new HashMap();
		m1.put("cat", (Utilities.capitalizeFirstLetters(POICategories.STREETS)));
		m1.put("cat_num", String.valueOf(num));
		result.add(m1);
		for (int i = 0; i < size; i++) {
			category = (Metadata.Category) categories.get(i);
			cat = category.name;
			cat = Utilities.capitalizeFirstLetters(cat.replaceAll("_", " "));
			num = category.total;
			if (num != 0) {
				HashMap m = new HashMap();
				m.put("cat", (cat));
				m.put("cat_num", String.valueOf(num));
				result.add(m);
			}
		}
		return (List) result;
	}

	/**
	 * Creates the child list out of the shades[] array according to the
	 * structure required by SimpleExpandableListAdapter. The resulting List
	 * contains one list for each group. Each such second-level group contains
	 * Maps. Each such Map contains two keys: "shadeName" is the name of the
	 * shade and "rgb" is the RGB value for the shade.
	 */
	private List createChildList() {
		Metadata me = provider.getPOIMetadata();
		ArrayList categories = me.getCategories();
		ArrayList result = new ArrayList();
		String subc;
		int num = 0;
		Metadata.Category category;
		Metadata.Subcategory subcategory;
		result.add(new ArrayList());

		final int length = categories.size();
		for (int i = 0; i < length; i++) {
			// Second-level lists
			category = (Metadata.Category) categories.get(i);
			ArrayList secList = new ArrayList();
			final int size = category.subcategories.size();
			int total = 0;
			for (int n = 0; n < size; n++) {
				subcategory = (Metadata.Subcategory) category.subcategories
						.get(n);
				num = subcategory.total;
				total += num;
				if (num != 0) {
					HashMap child = new HashMap();
					subc = subcategory.name;
					subc = Utilities.capitalizeFirstLetters(subc.replaceAll(
							"_", " "));
					child.put("sub", subc);
					child.put("sub_num", String.valueOf(num));
					secList.add(child);
				}
			}
			if (total > 0)
				result.add(secList);
		}
		return result;
	}
}
