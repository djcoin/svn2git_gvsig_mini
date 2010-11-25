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

package es.prodevelop.gvsig.mini.search.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.POICategoryIcon;
import es.prodevelop.gvsig.mini.search.POIItemClickContextListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.ToggleItemClickListener;
import es.prodevelop.gvsig.mini.search.adapter.PinnedHeaderListAdapter;
import es.prodevelop.gvsig.mini.search.view.PinnedHeaderListView;

public class POISearchActivity extends SearchActivity {

	private POIItemClickContextListener listener;
	String category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {

			setProvider(POIProviderManager.getInstance().getPOIProvider());

			getListView().setOnItemClickListener(new ToggleItemClickListener());

			category = this.getIntent().getStringExtra(SearchActivity.CATEGORY);
			if (category == null)
				category = this.getIntent().getStringExtra(
						SearchActivity.SUBCATEGORY);

			listener = new POIItemClickContextListener(this,
					POICategoryIcon.getDrawable32ForCategory(category, this),
					R.string.NameFinderActivity_0, true);
			getListView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							return getPOItemClickListener().onPOIClick(arg2,
									(POI) getListAdapter().getItem(arg2));
						}
					});

			// searchLayout = (LinearLayout) this.getLayoutInflater().inflate(
			// R.layout.search_config_panel, null);
			// // EditText text = (EditText)
			// // findViewById(com.android.internal.R.id.search_src_text);
			// // searchLayout.addView(text);
			//
			// advancedTextView = ((MultiAutoCompleteTextView) searchLayout
			// .findViewById(R.id.EditText01));
			//
			// advancedTextView.setTokenizer(new SpaceTokenizer());
			// advancedTextView.setAdapter(getAutoCompleteAdapter());
			// advancedTextView.setThreshold(1);
			// advancedTextView.requestFocus();
			//
			// closePar = ((Button)
			// searchLayout.findViewById(R.id.bt_close_par));
			// openPar = ((Button) searchLayout.findViewById(R.id.bt_open_par));
			// andOp = ((Button) searchLayout.findViewById(R.id.bt_op_and));
			// orOp = ((Button) searchLayout.findViewById(R.id.bt_op_or));
			//
			// closePar.setOnClickListener(buttonListener);
			// openPar.setOnClickListener(buttonListener);
			// andOp.setOnClickListener(buttonListener);
			// orOp.setOnClickListener(buttonListener);
			//
			// spinnerSort = ((Spinner)
			// searchLayout.findViewById(R.id.SpinnerSort));
			//
			// getSearchOptions().sort =
			// spinnerSort.getSelectedItem().toString();
			// getAutoCompleteTextView().addTextChangedListener(this);
			// if (category != null
			// && (category.toLowerCase().compareTo(POICategories.RESTAURANT) ==
			// 0)) {
			// spinnerArrayAdapter = new ArrayAdapter(this,
			// android.R.layout.simple_spinner_item, getResources()
			// .getStringArray(R.array.rest_sort_options));
			// } else {
			spinnerArrayAdapter = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.poi_sort_options));
			// }

			spinnerArrayAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			enableSpinner("");

			this.getSearchOptions().clearCategories();
			this.getSearchOptions().clearSubcategories();
			this.getSearchOptions().addCategory(category);
			this.getSearchOptions().setFilteredIndexed(
					getProvider().getPOIMetadata().getCatOrSubcat(category));

			initializeAdapters();
			this.attachSectionedAdapter();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	public void initializeAdapters() {
		View pinnedHeader = getLayoutInflater().inflate(R.layout.list_header,
				getListView(), false);
		((PinnedHeaderListView) getListView()).setPinnedHeaderView(pinnedHeader
				.findViewById(R.id.section_text));
		listAdapter = new PinnedHeaderListAdapter(this);
		this.getListView().setAdapter(this.listAdapter);
		this.setListAdapter(this.listAdapter);
	}

	public void attachSectionedAdapter() {
		this.getListView().setAdapter(listAdapter);
		// ((PinnedHeaderListAdapter) listAdapter).updateIndexer();
		getListView().setOnScrollListener(
				((PinnedHeaderListAdapter) listAdapter));
		this.setListAdapter(listAdapter);
	}

	@Override
	public POIItemClickContextListener getPOItemClickListener() {
		return listener;
	}

	@Override
	public String getQuery() {
		return category != null ? category : "";
	}
}
