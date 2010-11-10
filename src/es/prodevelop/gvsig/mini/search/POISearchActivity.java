package es.prodevelop.gvsig.mini.search;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import es.prodevelop.gvsig.mini.R;

public class POISearchActivity extends SearchActivity {

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

		getListView().setOnItemLongClickListener(
				new POIItemLongClickListener(this,
						R.drawable.p_places_poi_place_city_32,
						R.string.street_options));

		String category = this.getIntent().getStringExtra(
				SearchActivity.CATEGORY);
		if (category == null)
			category = this.getIntent().getStringExtra(
					SearchActivity.SUBCATEGORY);

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
		// closePar = ((Button) searchLayout.findViewById(R.id.bt_close_par));
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
		// getSearchOptions().sort = spinnerSort.getSelectedItem().toString();
		// getAutoCompleteTextView().addTextChangedListener(this);

		this.getSearchOptions().clearCategories();
		this.getSearchOptions().clearSubcategories();
		this.getSearchOptions().addCategory(category);
		this.getSearchOptions().setFilteredIndexed(
				getProvider().getPOIMetadata().getCatOrSubcat(category));

		initializeAdapters();
		this.attachSectionedAdapter();
	}

	public void initializeAdapters() {
		listAdapter = new LazyAdapter(this);
		filteredListAdapter = new FilteredLazyAdapter(this);
	}

}
