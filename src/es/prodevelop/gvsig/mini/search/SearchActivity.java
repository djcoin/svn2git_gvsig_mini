package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;

/**
 * Base activity for local pois / streets searches
 * 
 * @author aromeu
 * 
 */
public abstract class SearchActivity extends ListActivity implements
		SearchActivityWrapper {

	private QuadtreeProvider provider;
	private AutoCompleteAdapter autoCompleteAdapter;
	private ArrayList resultsList;
	private SearchOptions searchOptions = new SearchOptions(this);
	private MultiAutoCompleteTextView autoCompleteTextView;

	DisplayMetrics metrics = new DisplayMetrics();
	ListAdapter listAdapter;
	ListAdapter filteredListAdapter;

	Spinner spinnerSort;

	public final static String CATEGORY = "category";
	public final static String SUBCATEGORY = "subcategory";

	public final static int SEARCH_DIALOG = 1;
	public final static String HIDE_AUTOTEXTVIEW = "hide_tv";

	int selectedSpinnerPosition = 0;
	ArrayAdapter spinnerArrayAdapter;
	private String text;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		getListView().setTextFilterEnabled(true);

		LinearLayout l = ((LinearLayout) getLayoutInflater().inflate(
				R.layout.search_list, null));
		this.setContentView(l);

		autoCompleteTextView = (MultiAutoCompleteTextView) l
				.findViewById(R.id.EditText01);
		this.setAutoCompleteAdapter(new AutoCompleteAdapter(this));

		autoCompleteTextView.setTokenizer(new SpaceTokenizer());
		autoCompleteTextView.setAdapter(getAutoCompleteAdapter());
		autoCompleteTextView.setThreshold(1);

		spinnerSort = ((Spinner) l.findViewById(R.id.SpinnerSort));

		spinnerSort.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					selectedSpinnerPosition = spinnerSort
							.getSelectedItemPosition();
					getSearchOptions().sort = spinnerSort.getSelectedItem()
							.toString();
					if (selectedSpinnerPosition != 0)
						onTextChanged(autoCompleteTextView.getText(), 0, 0, 0);
				} catch (Exception e) {
					Log.e("", e.getMessage());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		spinnerArrayAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.sort_options));
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		enableSpinner("");
		// getSearchOptions().sort = spinnerSort.getSelectedItem().toString();

		// ((ImageButton) l.findViewById(R.id.search_opts))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// showDialog(SEARCH_DIALOG);
		// }
		// });

		searchOptions.center = getCenter();
		autoCompleteTextView.addTextChangedListener(this);

		final boolean hide = getIntent().getBooleanExtra(
				SearchActivity.HIDE_AUTOTEXTVIEW, false);
		if (hide)
			autoCompleteTextView.setVisibility(View.GONE);
	}

	public abstract void initializeAdapters();

	public QuadtreeProvider getProvider() {
		return provider;
	}

	public void setProvider(QuadtreeProvider provider) {
		this.provider = provider;
	}

	public AutoCompleteAdapter getAutoCompleteAdapter() {
		return autoCompleteAdapter;
	}

	public void setAutoCompleteAdapter(AutoCompleteAdapter adapter) {
		this.autoCompleteAdapter = adapter;
	}

	public ArrayList getResultsList() {
		return resultsList;
	}

	public void setResultsList(ArrayList resultsList) {
		this.resultsList = resultsList;
	}

	public SearchOptions getSearchOptions() {
		return searchOptions;
	}

	public void setSearchOptions(SearchOptions searchOptions) {
		this.searchOptions = searchOptions;
	}

	public MultiAutoCompleteTextView getAutoCompleteTextView() {
		return autoCompleteTextView;
	}

	public void setAutoCompleteTextView(
			MultiAutoCompleteTextView autoCompleteTextView) {
		this.autoCompleteTextView = autoCompleteTextView;
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
		((Filterable) getListView().getAdapter()).getFilter().filter(
				arg0.toString().toLowerCase());
		getAutoCompleteAdapter().getFilter().filter(
				arg0.toString().toLowerCase());
		Log.d("onTextChanged", arg0.toString());
		this.setTitle(R.string.please_wait);
		setProgressBarIndeterminateVisibility(true);
		if (arg0.length() == 0)
			enableSpinner(arg0.toString());
	}

	public void attachFilteredAdapter() {
		this.getListView().setAdapter(this.filteredListAdapter);
		this.setListAdapter(this.filteredListAdapter);
	}

	public void attachSectionedAdapter() {
		this.getListView().setAdapter(listAdapter);
		this.setListAdapter(listAdapter);
	}

	protected void enableSpinner() {
		this.enableSpinner(autoCompleteTextView.getText().toString());
	}

	protected void enableSpinner(String autoCompleteText) {
		// if (this.resultsList != null && this.resultsList.size() > 1) {
		if (autoCompleteText.length() == 0) {
			spinnerSort.setVisibility(View.GONE);
			searchOptions.sort = "no";
			text = autoCompleteText;
			// ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
			// android.R.layout.simple_spinner_item,
			// new String[] { getResources().getString(
			// R.string.sort_results) });
			// spinnerArrayAdapter
			// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// spinnerSort.setAdapter(spinnerArrayAdapter);
		} else {
			if (text.compareTo(autoCompleteText) != 0) {
				spinnerSort.setVisibility(View.VISIBLE);
				spinnerSort.setEnabled(true);
				spinnerSort.setAdapter(spinnerArrayAdapter);
				spinnerSort.setSelection(selectedSpinnerPosition);
				text = autoCompleteText;
			}
		}
		// } else {
		// spinnerSort.setVisibility(View.GONE);
		// searchOptions.sort = "no";
		// }

	}

	public Point getCenter() {
		Intent i = getIntent();
		if (i != null) {
			double lon = i.getDoubleExtra("lon", 0);
			double lat = i.getDoubleExtra("lat", 0);
			return new Point(lon, lat);
		} else {
			return new Point(0, 0);
		}
	}
	
	public abstract POIItemClickContextListener getPOItemClickListener();
}
