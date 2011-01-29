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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.OpenPOIDetailsItemClickListener;
import es.prodevelop.gvsig.mini.search.POIItemClickContextListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.ToggleItemClickListener;
import es.prodevelop.gvsig.mini.search.adapter.PinnedHeaderListAdapter;
import es.prodevelop.gvsig.mini.search.view.PinnedHeaderListView;

public class StreetSearchActivity extends SearchActivity {

	ListView lView;
	private POIItemClickContextListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setProvider(POIProviderManager.getInstance().getPOIProvider());

			getListView().setOnItemClickListener(
					new OpenPOIDetailsItemClickListener(this));

			listener = new POIItemClickContextListener(this,
					R.drawable.p_places_poi_place_city_32,
					R.string.street_options, true);
			getListView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							return getPOItemClickListener().onPOIClick(arg2,
									(POI) getListAdapter().getItem(arg2), arg1);
						}
					});

			getAutoCompleteTextView().setOnItemClickListener(
					new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Log.d("", "Item click");
							setTitle(R.string.please_wait);
							setProgressBarIndeterminateVisibility(true);
						}
					});

			getAutoCompleteTextView().addTextChangedListener(this);

			spinnerArrayAdapter = new ArrayAdapter(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.poi_sort_options));
			spinnerArrayAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			enableSpinner("");

			this.getSearchOptions().clearCategories();
			this.getSearchOptions().clearSubcategories();
			this.getSearchOptions().addCategory(POICategories.STREETS);
			this.getSearchOptions().setFilteredIndexed(
					getProvider().getStreetMetadata().getCategory(
							POICategories.STREETS));

			initializeAdapters();
			this.attachSectionedAdapter();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	// public void initializeAdapters() {
	// listAdapter = new LazyAdapter(this);
	// filteredListAdapter = new FilteredLazyAdapter(this);
	// }

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
	public void onTextChanged(final CharSequence arg0, int arg1, int arg2,
			int arg3) {
		if (arg1 != 0 && arg2 != 0 && arg2 != 0)
			getSearchOptions().sort = getResources().getString(R.string.no);
		super.onTextChanged(arg0, arg1, arg2, arg3);
		// advancedTextView.setText(arg0);
	}

	@Override
	public POIItemClickContextListener getPOItemClickListener() {
		return listener;
	}

	@Override
	public String getQuery() {
		return POICategories.STREETS;
	}
}