package es.prodevelop.gvsig.mini.search.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.SpaceTokenizer;
import es.prodevelop.gvsig.mini.search.adapter.AutoCompleteAdapter;
import es.prodevelop.gvsig.mini.search.adapter.CheckboxExpandableListAdapter;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class SearchExpandableActivity extends ExpandableListActivity implements
		SearchActivityWrapper {

	PerstOsmPOIClusterProvider provider;
	CheckboxExpandableListAdapter expListAdapter;
	private MultiAutoCompleteTextView autoCompleteTextView;
	private AutoCompleteAdapter autoCompleteAdapter;

	private Point center;

	private final static int SHOW_DIALOG_FILL_TEXT = 0;
	private final static int SHOW_DIALOG_FILL_CATEGORIES = 1;

	private String[] categories;
	private String[][] subcategories;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// setContentView(R.layout.search_expandable_main);
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

		LinearLayout l = ((LinearLayout) getLayoutInflater().inflate(
				R.layout.search_expandable_main, null));
		this.setContentView(l);

		autoCompleteTextView = (MultiAutoCompleteTextView) l
				.findViewById(R.id.EditText01);
		this.setAutoCompleteAdapter(new AutoCompleteAdapter(this));

		ImageButton searchButton = (ImageButton) l.findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// FIXME launch the search -> Check if a category is selected
				// and any text has been input
				if (autoCompleteTextView.getText().length() == 0) {
					// SHOW dialog
					showDialog(SHOW_DIALOG_FILL_TEXT);
					return;
				}

				final boolean[] groups = expListAdapter.parentChecked;
				final int length = groups.length;

				boolean anyChecked = false;
				for (int i = 0; i < length; i++) {
					if (groups[i]) {
						anyChecked = true;
						break;
					}
				}

				if (anyChecked) {
					launchSearch();
					return;
				} else {
					final boolean[][] childs = expListAdapter.childChecked;
					final int cLength = childs.length;

					for (int i = 0; i < cLength; i++) {
						final int ccLength = childs[i].length;
						for (int j = 0; j < ccLength; j++) {
							if (childs[i][j]) {
								anyChecked = true;
								break;
							}
						}
					}
				}

				if (anyChecked) {
					launchSearch();
					return;
				} else {
					// show dialog
					showDialog(SHOW_DIALOG_FILL_CATEGORIES);
				}
			}
		});

		this.setAutoCompleteAdapter(new AutoCompleteAdapter(this));
		autoCompleteTextView.setTokenizer(new SpaceTokenizer());
		autoCompleteTextView.setAdapter(getAutoCompleteAdapter());
		autoCompleteTextView.setThreshold(1);

		setContentView(l);

		// getSearchOptions().sort = spinnerSort.getSelectedItem().toString();
		setListAdapter(expListAdapter);
		double lon = getIntent().getDoubleExtra("lon", 0);
		double lat = getIntent().getDoubleExtra("lat", 0);
		this.setCenter(new Point(lon, lat));
		autoCompleteTextView.addTextChangedListener(this);
	}

	private void launchSearch() {
		ArrayList list = getSelectedCategoriesAndSubcategories();
		String[] keywords = autoCompleteTextView.getText().toString()
				.split(" ");

		String part1 = buildKeyWordString(keywords);

		final int size = list.size();

		String cat;
		StringBuffer query = new StringBuffer();
		for (int i = 0; i < size; i++) {
			cat = list.get(i).toString();
			query.append(part1).append(cat).append(" ) ");
			if (i != size - 1) {
				query.append(" OR ");
			}
		}

		Intent i = new Intent(this, ResultSearchActivity.class);
		fillCenter(i);
		i.putExtra(SearchActivity.HIDE_AUTOTEXTVIEW, true);
		i.putExtra(ResultSearchActivity.QUERY, query.toString());
		startActivity(i);
	}

	private void fillCenter(Intent i) {
		i.putExtra("lon", getCenter().getX());
		i.putExtra("lat", getCenter().getY());
	}

	private String buildKeyWordString(String[] keywords) {
		if (keywords != null && keywords.length > 0) {
			final int length = keywords.length;
			StringBuffer sb = new StringBuffer();
			sb.append(" ( ");
			for (int i = 0; i < length; i++) {
				sb.append(keywords[i]).append(" AND ");
			}
			return sb.toString();
		} else {
			return "";
		}
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

	private ArrayList getSelectedCategoriesAndSubcategories() {
		ArrayList list = new ArrayList();
		final boolean[] groups = expListAdapter.parentChecked;
		final int length = groups.length;

		final boolean[][] childs = expListAdapter.childChecked;
		// final int cLength = childs.length;

		// iterate categories
		for (int i = 0; i < length; i++) {
			// if is checked iterate subcategories
			if (groups[i]) {
				ArrayList subc = new ArrayList();
				// for (int n = 0; n < cLength; n++) {
				final int ccLength = subcategories[i].length;
				boolean allChecked = true;
				for (int j = 0; j < ccLength; j++) {
					if (!childs[i][j]) {
						allChecked = false;
					} else {
						subc.add(this.subcategories[i][j]);
					}
				}
				// if are all checked add the category,
				// else add the subcategories checked
				if (allChecked) {
					Log.d("", "Added category: " + this.categories[i]);
					list.add(this.categories[i]);
				} else {
					final int size = subc.size();
					for (int m = 0; m < size; m++) {
						Log.d("", "Added subcategory: "
								+ subc.get(m).toString());
						list.add(subc.get(m).toString());
					}
				}
				// }
			}
		}
		return list;
	}

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
		this.categories = new String[size + 1];
		this.categories[0] = "0" + POICategories.STREETS + "0";
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
				this.categories[i + 1] = "0"
						+ category.name.replaceAll("_", "") + "0";
			}
		}
		return (List) result;
	}

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
		this.subcategories = new String[length + 1][];
		this.subcategories[0] = new String[0];
		for (int i = 0; i < length; i++) {
			// Second-level lists
			category = (Metadata.Category) categories.get(i);
			ArrayList secList = new ArrayList();
			final int size = category.subcategories.size();
			int total = 0;
			this.subcategories[i + 1] = new String[size];
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
					this.subcategories[i + 1][n] = "0"
							+ subcategory.name.replaceAll("_", "") + "0";
				}
			}
			if (total > 0)
				result.add(secList);
		}
		return result;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(final CharSequence arg0, int arg1, int arg2,
			int arg3) {
		getAutoCompleteAdapter().getFilter().filter(
				arg0.toString().toLowerCase());
//		this.setTitle(R.string.please_wait);
		setProgressBarIndeterminateVisibility(true);
	}

	@Override
	public QuadtreeProvider getProvider() {
		return this.provider;
	}

	@Override
	public void setProvider(QuadtreeProvider provider) {
		this.provider = (PerstOsmPOIClusterProvider) provider;
	}

	@Override
	public AutoCompleteAdapter getAutoCompleteAdapter() {
		return this.autoCompleteAdapter;
	}

	@Override
	public void setAutoCompleteAdapter(AutoCompleteAdapter adapter) {
		this.autoCompleteAdapter = adapter;
	}

	@Override
	public MultiAutoCompleteTextView getAutoCompleteTextView() {
		return this.autoCompleteTextView;
	}

	@Override
	public void setAutoCompleteTextView(
			MultiAutoCompleteTextView autoCompleteTextView) {
		this.autoCompleteTextView = autoCompleteTextView;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Builder b = new AlertDialog.Builder(this).setIcon(
				android.R.drawable.ic_search_category_default).setTitle(
				R.string.warning);
		TextView t = new TextView(this);
		t.setTextSize(16);
		switch (id) {
		case SHOW_DIALOG_FILL_TEXT:
			t.setText(getResources().getString(R.string.fill_text));
			b.setView(t);
			break;
		case SHOW_DIALOG_FILL_CATEGORIES:
			t.setText(getResources().getString(R.string.fill_category));
			b.setView(t);
			break;
		}
		return b.create();
	}
}
