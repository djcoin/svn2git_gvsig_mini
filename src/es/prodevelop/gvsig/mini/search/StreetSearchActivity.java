package es.prodevelop.gvsig.mini.search;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;

public class StreetSearchActivity extends SearchActivity {

	ListView lView;
	LinearLayout searchLayout;
	Spinner spinnerSearch;
	Spinner spinnerSort;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SEARCH_DIALOG:
			return new AlertDialog.Builder(StreetSearchActivity.this)
					.setIcon(R.drawable.icon)
					.setTitle("Search options")
					.setView(searchLayout)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									final String type = spinnerSearch
											.getSelectedItem().toString();

									final String filter = spinnerSort
											.getSelectedItem().toString();

									if (type.compareTo(getSearchOptions().filter) != 0
											|| filter
													.compareTo(getSearchOptions().filter) != 0) {
										getSearchOptions().filter = spinnerSearch
												.getSelectedItem().toString();
										getSearchOptions().sort = spinnerSort
												.getSelectedItem().toString();
										onTextChanged(getAutoCompleteTextView()
												.getText().toString(), 0, 0, 0);
									}

									getSearchOptions().filter = spinnerSearch
											.getSelectedItem().toString();
									getSearchOptions().sort = spinnerSort
											.getSelectedItem().toString();

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

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// ((LazyAdapter) getListAdapter()).pos = arg2;
				// ((LazyAdapter) getListAdapter()).notifyDataSetChanged();

				AlertDialog.Builder alertPOI = new AlertDialog.Builder(
						StreetSearchActivity.this);

				alertPOI.setCancelable(true);
				alertPOI.setIcon(R.drawable.pois);
				alertPOI.setTitle(R.string.NameFinderActivity_0);

				final ListView lv = new ListView(StreetSearchActivity.this);

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int position, long arg3) {
						try {
							Intent mIntent;
							mIntent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putInt("selected", position);
							mIntent.putExtras(bundle);
							switch (position) {
							case 0:
								// log.log(Level.FINE, "setResult = 0");
								setResult(0, mIntent);
								finish();

								break;
							case 1:
								// log.log(Level.FINE, "setResult = 1");
								setResult(1, mIntent);
								finish();
								break;
							case 2:
								// log.log(Level.FINE, "setResult = 2");
								setResult(2, mIntent);
								finish();
								break;
							case 3:
								// TODO LANZAR OTRA ACTIVITY CON LOS DETALLES

								break;
							}
						} catch (Exception e) {
							// log.log(Level.SEVERE,"",e);
						}
					}

				});

				BulletedTextListAdapter adapter = new BulletedTextListAdapter(
						StreetSearchActivity.this);

				adapter.addItem(new BulletedText(getResources().getString(
						R.string.NameFinderActivity_1), getResources()
						.getDrawable(R.drawable.pinpoi)));

				adapter.addItem(new BulletedText(getResources().getString(
						R.string.NameFinderActivity_2), getResources()
						.getDrawable(R.drawable.pinoutpoi)));

				adapter.addItem(new BulletedText(getResources().getString(
						R.string.NameFinderActivity_3), getResources()
						.getDrawable(R.drawable.pois)));

				lv.setAdapter(adapter);

				alertPOI.setView(lv);

				alertPOI.setNegativeButton(R.string.back,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});

				alertPOI.show();

				return true;
			}

		});

		searchLayout = (LinearLayout) this.getLayoutInflater().inflate(
				R.layout.search_config_panel, null);

		spinnerSearch = ((Spinner) searchLayout
				.findViewById(R.id.SpinnerSearch));

		spinnerSort = ((Spinner) searchLayout.findViewById(R.id.SpinnerSort));

		getSearchOptions().filter = spinnerSearch.getSelectedItem().toString();
		getSearchOptions().sort = spinnerSort.getSelectedItem().toString();

		setListAdapter(new LazyAdapter(this));
	}

}