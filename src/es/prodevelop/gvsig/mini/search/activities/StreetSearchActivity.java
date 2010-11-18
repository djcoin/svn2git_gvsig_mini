package es.prodevelop.gvsig.mini.search.activities;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.POIItemClickContextListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.adapter.FilteredLazyAdapter;
import es.prodevelop.gvsig.mini.search.adapter.LazyAdapter;

public class StreetSearchActivity extends SearchActivity {

	ListView lView;
	private POIItemClickContextListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setProvider(POIProviderManager.getInstance().getPOIProvider());

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				((LazyAdapter) getListAdapter()).pos = arg2;
				((LazyAdapter) getListAdapter()).notifyDataSetChanged();
			}

		});

		listener = new POIItemClickContextListener(this,
				R.drawable.p_places_poi_place_city_32, R.string.street_options);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return getPOItemClickListener().onPOIClick(arg2,
						(POI)getListAdapter().getItem(arg2));
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

		this.getSearchOptions().clearCategories();
		this.getSearchOptions().clearSubcategories();
		this.getSearchOptions().addCategory(POICategories.STREETS);
		this.getSearchOptions().setFilteredIndexed(
				getProvider().getStreetMetadata().getCategory(
						POICategories.STREETS));

		initializeAdapters();
		this.attachSectionedAdapter();
	}

	public void initializeAdapters() {
		listAdapter = new LazyAdapter(this);
		filteredListAdapter = new FilteredLazyAdapter(this);
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

	// private class CopyTextOnClickListener implements OnClickListener {
	//
	// @Override
	// public void onClick(View v) {
	// try {
	// String append = ((Button) v).getText().toString();
	//
	// advancedTextView.getText().insert(
	// advancedTextView.getSelectionStart(),
	// " " + append.trim() + " ");
	// } catch (Exception e) {
	// Log.e("", e.getMessage());
	// }
	// }
	// }

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// switch (id) {
	// case SEARCH_DIALOG:
	// return new AlertDialog.Builder(StreetSearchActivity.this)
	// .setIcon(android.R.drawable.ic_search_category_default)
	// .setTitle(R.string.advanced_search)
	// .setView(searchLayout)
	// .setPositiveButton(R.string.ok,
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	//
	// getSearchOptions().sort = spinnerSort
	// .getSelectedItem().toString();
	// // getAutoCompleteTextView().setText(
	// // advancedTextView.getText());
	// onTextChanged(advancedTextView.getText()
	// .toString(), 0, 0, 0);
	// }
	// }).create();
	// }
	// return null;
	// }
}