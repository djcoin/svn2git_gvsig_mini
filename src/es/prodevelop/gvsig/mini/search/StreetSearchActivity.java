package es.prodevelop.gvsig.mini.search;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;

public class StreetSearchActivity extends SearchActivity {

	ListView lView;
	LinearLayout searchLayout;
	Spinner spinnerSort;
	Button closePar;
	Button openPar;
	Button andOp;
	Button orOp;
	CopyTextOnClickListener buttonListener = new CopyTextOnClickListener();
	MultiAutoCompleteTextView advancedTextView;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SEARCH_DIALOG:
			return new AlertDialog.Builder(StreetSearchActivity.this)
					.setIcon(android.R.drawable.ic_search_category_default)
					.setTitle(R.string.advanced_search)
					.setView(searchLayout)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									getSearchOptions().sort = spinnerSort
											.getSelectedItem().toString();
									// getAutoCompleteTextView().setText(
									// advancedTextView.getText());
									onTextChanged(advancedTextView
											.getText().toString(), 0, 0, 0);
								}
							}).create();
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setProvider(new PerstOsmPOIProvider("/" + "sdcard" + File.separator
				+ "perst_streets.db"));

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
				new POIItemLongClickListener(this));

		searchLayout = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.search_config_panel, null);

		advancedTextView = ((MultiAutoCompleteTextView) searchLayout
				.findViewById(R.id.EditText01));

		advancedTextView.setTokenizer(new SpaceTokenizer());
		advancedTextView.setAdapter(getAutoCompleteAdapter());
		advancedTextView.setThreshold(1);
		advancedTextView.requestFocus();

		closePar = ((Button) searchLayout.findViewById(R.id.bt_close_par));
		openPar = ((Button) searchLayout.findViewById(R.id.bt_open_par));
		andOp = ((Button) searchLayout.findViewById(R.id.bt_op_and));
		orOp = ((Button) searchLayout.findViewById(R.id.bt_op_or));

		closePar.setOnClickListener(buttonListener);
		openPar.setOnClickListener(buttonListener);
		andOp.setOnClickListener(buttonListener);
		orOp.setOnClickListener(buttonListener);

		spinnerSort = ((Spinner) searchLayout.findViewById(R.id.SpinnerSort));

		getSearchOptions().sort = spinnerSort.getSelectedItem().toString();
		getAutoCompleteTextView().addTextChangedListener(this);

		initializeAdapters();
		this.attachSectionedAdapter();
	}

	@Override
	public void onTextChanged(final CharSequence arg0, int arg1, int arg2,
			int arg3) {
		if (arg1 != 0 && arg2 != 0 && arg2 != 0)
			getSearchOptions().sort = getResources().getString(R.string.no);
		super.onTextChanged(arg0, arg1, arg2, arg3);
		// advancedTextView.setText(arg0);
	}

	private class CopyTextOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				String append = ((Button) v).getText().toString();

				advancedTextView.getText().insert(
						advancedTextView.getSelectionStart(),
						" " + append.trim() + " ");
			} catch (Exception e) {
				Log.e("", e.getMessage());
			}
		}
	}
}