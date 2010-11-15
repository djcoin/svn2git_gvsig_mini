package es.prodevelop.gvsig.mini.search;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Filterable;
import es.prodevelop.gvsig.mini.R;

public class ResultSearchActivity extends SearchActivity {

	public final static String QUERY = "query";
	private String query;
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
				((FilteredLazyAdapter) getListAdapter()).pos = arg2;
				((FilteredLazyAdapter) getListAdapter()).notifyDataSetChanged();
			}

		});

		listener = new POIItemClickContextListener(this, R.drawable.pois,
				R.string.NameFinderActivity_0);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				return getPOItemClickListener().onItemClick(arg0, arg1, arg2,
						arg3);
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
					if (selectedSpinnerPosition != 0)
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
		query = getIntent().getStringExtra(QUERY);
		onTextChanged(query, 0, 0, 0);
	}

	@Override
	protected void enableSpinner(String autoCompleteText) {
		spinnerSort.setVisibility(View.VISIBLE);
		spinnerSort.setEnabled(true);
		spinnerSort.setAdapter(spinnerArrayAdapter);
		if (spinnerSort.getSelectedItemPosition() != selectedSpinnerPosition)
			spinnerSort.setSelection(selectedSpinnerPosition);
	}

	@Override
	public void initializeAdapters() {
		listAdapter = new MultiKeywordFilteredAdapter(this);
		filteredListAdapter = new MultiKeywordFilteredAdapter(this);
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
}
