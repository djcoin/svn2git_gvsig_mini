package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.gvsig.mini.R;

/**
 * Base activity for local pois / streets searches
 * 
 * @author aromeu
 * 
 */
public abstract class SearchActivity extends ListActivity implements TextWatcher {

	private QuadtreeProvider provider;
	private AutoCompleteAdapter autoCompleteAdapter;
	private ArrayList resultsList;
	private SearchOptions searchOptions = new SearchOptions(this);
	private MultiAutoCompleteTextView autoCompleteTextView;

	DisplayMetrics metrics = new DisplayMetrics();
	ListAdapter listAdapter;
	ListAdapter filteredListAdapter;
	
	public final static String CATEGORY = "category";
	public final static String SUBCATEGORY = "subcategory";

	public final static int SEARCH_DIALOG = 1;

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

		((ImageButton) l.findViewById(R.id.search_opts))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showDialog(SEARCH_DIALOG);
					}
				});

		autoCompleteTextView.addTextChangedListener(this);
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
		this.setTitle(R.string.searching);
		setProgressBarIndeterminateVisibility(true);
	}

	public void attachFilteredAdapter() {
		this.getListView().setAdapter(this.filteredListAdapter);
		this.setListAdapter(this.filteredListAdapter);
	}

	public void attachSectionedAdapter() {
		this.getListView().setAdapter(listAdapter);
		this.setListAdapter(listAdapter);
	}
}
