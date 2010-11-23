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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Filterable;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.POIItemClickContextListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.ToggleItemClickListener;
import es.prodevelop.gvsig.mini.search.adapter.MultiKeywordFilteredAdapter;
import es.prodevelop.gvsig.mini.search.adapter.PinnedHeaderListAdapter;
import es.prodevelop.gvsig.mini.search.view.PinnedHeaderListView;

public class ResultSearchActivity extends SearchActivity {

	public final static String QUERY = "query";
	String query;
	protected POIItemClickContextListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		query = getIntent().getStringExtra(QUERY);

		setProvider(POIProviderManager.getInstance().getPOIProvider());

		getListView().setOnItemClickListener(new ToggleItemClickListener());

		listener = new POIItemClickContextListener(this, R.drawable.pois,
				R.string.NameFinderActivity_0);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return getPOItemClickListener().onPOIClick(arg2,
						(POI) getListAdapter().getItem(arg2));
			}
		});

		this.getSearchOptions().clearCategories();
		this.getSearchOptions().clearSubcategories();

		initializeAdapters();
		this.attachSectionedAdapter();

		spinnerSort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					if (spinnerSort.getSelectedItemPosition() == selectedSpinnerPosition)
						return;

					selectedSpinnerPosition = spinnerSort
							.getSelectedItemPosition();
					getSearchOptions().sort = spinnerSort.getSelectedItem()
							.toString();
					// if (selectedSpinnerPosition != 0)
					onTextChanged(query, 0, 0, 0);
				} catch (Exception e) {
					Log.e("", e.getMessage());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		enableSpinner("");
		onTextChanged(query, 0, 0, 0);
	}
	
	public void attachSectionedAdapter() {
		this.getListView().setAdapter(listAdapter);
		// ((PinnedHeaderListAdapter) listAdapter).updateIndexer();
		getListView().setOnScrollListener(
				((PinnedHeaderListAdapter) listAdapter));
		this.setListAdapter(listAdapter);
	}

	@Override
	protected void enableSpinner(String autoCompleteText) {
		View pinnedHeader = getLayoutInflater().inflate(R.layout.list_header,
				getListView(), false);
		((PinnedHeaderListView) getListView()).setPinnedHeaderView(pinnedHeader
				.findViewById(R.id.section_text));
		spinnerSort.setVisibility(View.VISIBLE);
		spinnerSort.setEnabled(true);
		spinnerSort.setAdapter(spinnerArrayAdapter);
		if (spinnerSort.getSelectedItemPosition() != selectedSpinnerPosition)
			spinnerSort.setSelection(selectedSpinnerPosition);
	}

	@Override
	public void initializeAdapters() {
		listAdapter = new MultiKeywordFilteredAdapter(this);
		this.getListView().setAdapter(this.listAdapter);
		this.setListAdapter(this.listAdapter);
	}

	@Override
	public void onTextChanged(final CharSequence arg0, int arg1, int arg2,
			int arg3) {
		((Filterable) getListView().getAdapter()).getFilter().filter(
				arg0.toString());
		Log.d("onTextChanged", arg0.toString());
		this.setTitle(R.string.please_wait);
		setProgressBarIndeterminateVisibility(true);
		// if (arg0.length() == 0)
		// enableSpinner(arg0.toString());
	}

	@Override
	public POIItemClickContextListener getPOItemClickListener() {
		return listener;
	}

	@Override
	public String getQuery() {
		return query;
	}
}
