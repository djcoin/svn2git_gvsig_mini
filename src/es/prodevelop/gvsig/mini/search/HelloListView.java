package es.prodevelop.gvsig.mini.search;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.garret.perst.fulltext.FullTextIndex;
import org.garret.perst.fulltext.FullTextSearchResult;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class HelloListView extends ListActivity implements TextWatcher {

	final PerstOsmPOIProvider osmStreetProvider = new PerstOsmPOIProvider("/"
			+ "sdcard" + File.separator + "perst_streets.db");
	ListView lView;
	LazyAdapter adapter;
	LinearLayout searchLayout;
	MultiAutoCompleteTextView autoCompleteTextView;

	LayoutInflater mInflater;
	Point center = new Point(0, 0);
	DecimalFormat formatter = new DecimalFormat("####.00");
	SearchOptions searchOptions = new SearchOptions();
	Spinner spinnerSearch;
	Spinner spinnerSort;
	AutoCompleteAdapter autoCompleteAdapter;
	DisplayMetrics metrics = new DisplayMetrics();

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			return new AlertDialog.Builder(HelloListView.this)
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

									if (type.compareTo(searchOptions.filter) != 0
											|| filter
													.compareTo(searchOptions.filter) != 0) {
										searchOptions.filter = spinnerSearch
												.getSelectedItem().toString();
										searchOptions.sort = spinnerSort
												.getSelectedItem().toString();
										onTextChanged(
												HelloListView.this.autoCompleteTextView
														.getText().toString(),
												0, 0, 0);
									}

									searchOptions.filter = spinnerSearch
											.getSelectedItem().toString();
									searchOptions.sort = spinnerSort
											.getSelectedItem().toString();

								}
							}).create();
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		// long t1 = System.currentTimeMillis();
		// Iterator it =
		// osmStreetProvider.getStreetsByPrefixName("c").iterator();
		// // Iterator it =
		// //
		// ((SpatialIndexRoot)osmStreetProvider.getHelper().getRoot()).getStreetIndex().iterator();
		// //
		// Log.d("time", (System.currentTimeMillis() - t1) + " ms");
		// ArrayList list = new ArrayList();
		// while (it.hasNext()) {
		// list.add((((OsmPOIStreet) it.next()).getDescription()));
		// }

		// setListAdapter(new ArrayAdapter<Object>(this,
		// android.R.layout.simple_list_item_1, list.toArray()));
		getListView().setTextFilterEnabled(true);
		// getListView().setFilterText("ca");
		mInflater = this.getLayoutInflater();
		LinearLayout l = ((LinearLayout) mInflater.inflate(
				R.layout.search_street, null));
		this.setContentView(l);
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
				((LazyAdapter) getListAdapter()).pos = arg2;
				((LazyAdapter) getListAdapter()).notifyDataSetChanged();
				return true;
			}

		});
		adapter = new LazyAdapter();
		setListAdapter(adapter);
		// final LayoutInflater factory = LayoutInflater.from(this);
		// LinearLayout layout = (LinearLayout) factory.inflate(R.layout.main,
		// null);
		// lView = (ListView) layout.findViewById(R.id.ListView01);
		// lView.setAdapter(new LazyAdapter());
		// EditText e = new EditText(this);
		// LinearLayout l = new LinearLayout(this);
		// ListView lView = new ListView(this);
		// lView.setAdapter(new LazyAdapter());
		// l.addView(e);
		// l.addView(lView);
		// this.setContentView(l);
		// getListView().setTextFilterEnabled(true);
		// EditText e = (EditText) layout.findViewById(R.id.EditText01);
		// e.addTextChangedListener(this);
		autoCompleteTextView = (MultiAutoCompleteTextView) l
				.findViewById(R.id.EditText01);
		autoCompleteAdapter = new AutoCompleteAdapter();
		autoCompleteTextView.setTokenizer(new SpaceTokenizer());
		autoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// Log.d("", autoCompleteAdapter.list.get(arg2).toString());
			}
		});
		autoCompleteTextView.setAdapter(autoCompleteAdapter);
		autoCompleteTextView.setThreshold(1);

		searchLayout = (LinearLayout) mInflater.inflate(
				R.layout.search_config_panel, null);

		spinnerSearch = ((Spinner) searchLayout
				.findViewById(R.id.SpinnerSearch));

		spinnerSort = ((Spinner) searchLayout.findViewById(R.id.SpinnerSort));

		searchOptions.filter = spinnerSearch.getSelectedItem().toString();
		searchOptions.sort = spinnerSort.getSelectedItem().toString();

		((ImageButton) l.findViewById(R.id.search_opts))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						showDialog(0);
					}
				});

		autoCompleteTextView.addTextChangedListener(this);
		// e.setAdapter(adapter);

	}

	private class LazyAdapter extends BaseAdapter implements SectionIndexer,
			Filterable {

		HashMap<String, Integer> alphaIndexer = new HashMap();

		String[] sections;
		Metadata metadata;
		private SimpleFilter mFilter;

		public LazyAdapter() {
			int length = 0;
			metadata = osmStreetProvider.getMetadata();
			ArrayList t = metadata.getStreetInitialNumber();

			InitialQuickSort iq = new InitialQuickSort();
			Object[] ordered = iq.sort(t);

			final int size = ordered.length;
			sections = new String[size];
			for (int i = 0; i < size; i++) {
				Metadata.InitialNumber initial = (Metadata.InitialNumber) ordered[i];
				alphaIndexer.put(initial.initial, length);
				length += initial.number;
				sections[i] = initial.initial.toUpperCase();
			}
		}

		@Override
		public int getPositionForSection(int section) {
			// Log.v("getPositionForSection", ""+section);
			String letter = sections[section];

			return alphaIndexer.get(letter.toLowerCase());
		}

		@Override
		public int getSectionForPosition(int position) {

			// you will notice it will be never called (right?)
			Log.v("getSectionForPosition", "called");
			return 0;
		}

		@Override
		public Object[] getSections() {

			return sections; // to string will be called each object, to display
			// the letter
		}

		int pos = -1;
		ArrayList list = null;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (list != null)
				return list.size();
			return metadata.getTotalStreets();
		}

		@Override
		public Object getItem(int arg0) {
			if (list != null) {
				return list.get(arg0);
			}

			return ((SpatialIndexRoot) osmStreetProvider.getHelper().getRoot())
					.getStreetIndex().getAt(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			if (list != null)
				return ((POI) list.get(arg0)).getId();
			return ((POI) ((SpatialIndexRoot) osmStreetProvider.getHelper()
					.getRoot()).getStreetIndex().getAt(arg0)).getId();
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.street_row, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.desc);
				holder.dist = (TextView) convertView.findViewById(R.id.dist);
				holder.poiImg = (ImageView) convertView.findViewById(R.id.img);
				try {
					LinearLayout l = new LinearLayout(HelloListView.this);
					final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					zzParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
					zzParams.addRule(RelativeLayout.CENTER_VERTICAL);
					// zzParams.setMargins(20, 20, 20, 20);
					MapPreview preview = new MapPreview(HelloListView.this,
							CompatManager.getInstance().getRegisteredContext(),
							metrics.widthPixels, metrics.heightPixels / 2);
					l.addView(preview);
					((LinearLayout) convertView).addView(l, zzParams);
					holder.previewLayout = l;
					holder.preview = preview;
				} catch (BaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// ((TextView) arg1).setTextSize(26);
			// ((TextView) arg1).setTextColor(Color.WHITE);
			POI p = (POI) getItem(arg0);
			String desc = Utilities.capitalizeFirstLetters(p.getDescription());

			if (holder.preview != null) {
				if (arg0 == pos) {
					holder.previewLayout.setVisibility(View.VISIBLE);
					holder.preview.setMapCenterFromLonLat(p);
					holder.preview.setExtent(((OsmPOIStreet) p)
							.getBoundingBox());
				} else {
					holder.previewLayout.setVisibility(View.GONE);
				}
			}

			// Bind the data efficiently with the holder.
			holder.text.setText(desc);
			holder.dist.setText(HelloListView.this.getResources().getString(
					R.string.distance)
					+ " "
					+ formatter.format(center.distance(new double[] { p.getX(),
							p.getY() })));
			holder.poiImg.setImageBitmap(ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_32));
			// holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 :
			// mIcon2);

			return convertView;
			// if (arg0 == pos) {
			// LinearLayout v = new LinearLayout(HelloListView.this);
			// v.setOrientation(LinearLayout.VERTICAL);
			// v.addView(new EditText(HelloListView.this));
			// v.addView(new EditText(HelloListView.this));
			// TextView t = new TextView(HelloListView.this);
			// t.setText("ABCDEFGHASDFAS )AS DFASD" + "FASDFASD " + "AFDASD"
			// + "FSADFADFASDF");
			// Button ob = new Button(HelloListView.this);
			// v.addView(t);
			// v.addView(ob);
			// return v;
			// }
		}

		@Override
		public Filter getFilter() {
			if (mFilter == null) {
				mFilter = new SimpleFilter();
			}
			return mFilter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#notifyDataSetChanged()
		 */
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			setProgressBarIndeterminateVisibility(false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#notifyDataSetInvalidated()
		 */
		@Override
		public void notifyDataSetInvalidated() {
			// TODO Auto-generated method stub
			super.notifyDataSetInvalidated();
			setProgressBarIndeterminateVisibility(false);
		}

		class ViewHolder {
			TextView text;
			TextView dist;
			MapPreview preview;
			LinearLayout previewLayout;
			ImageView poiImg;
		}

		/**
		 * <p>
		 * An array filters constrains the content of the array adapter with a
		 * prefix. Each item that does not start with the supplied prefix is
		 * removed from the list.
		 * </p>
		 */
		private class SimpleFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence prefix) {
				FilterResults results = new FilterResults();

				String filter = searchOptions.filter;

				boolean byPrefix = searchOptions.isPrefixSearch();

				final LazyAdapter adapter;
				adapter = (LazyAdapter) getListView().getAdapter();

				if (prefix == null) {
					results.count = 0;
					return results;
				}

				if (prefix.toString().length() <= 0) {
					results.count = 0;
					return results;
				}

				ArrayList list = new ArrayList();
				if (prefix.toString().contains(" ")) {

					SpatialIndexRoot root = ((SpatialIndexRoot) HelloListView.this.osmStreetProvider
							.getHelper().getRoot());

					String desc = prefix.toString().trim()
							.replaceAll(" ", " AND ");
					FullTextSearchResult result = root.getFullTextStreetIndex()
							.search(desc, "en", 1000, 1000);

					final int size = result.hits.length;
					for (int i = 0; i < size; i++) {
						list.add(result.hits[i].getDocument());
					}

					/*
					 * Sort POIs alphabetically
					 */
					// POIAlphabeticalQuickSort quickSort = new
					// POIAlphabeticalQuickSort();
					// Object[] pois = quickSort.sort(list);
					//
					// list = new ArrayList();
					// final int l = pois.length;
					// for (int i = 0; i<l; i++) {
					// list.add(pois[i]);
					// }
				} else {
					if (byPrefix) {
						list = (ArrayList) HelloListView.this.osmStreetProvider
								.getStreetsByPrefixName(prefix.toString());
					} else {
						long t1 = System.currentTimeMillis();

						Iterator it = (Iterator) HelloListView.this.osmStreetProvider
								.getStreetsByName(prefix.toString(), false);

						Log.d("TIME", (System.currentTimeMillis() - t1) + "");
						t1 = System.currentTimeMillis();
						while (it.hasNext())
							list.add(it.next());
						Log.d("TIME", (System.currentTimeMillis() - t1) + "");
					}

					if (searchOptions.sortResults()) {
						final DistanceQuickSort dq = new DistanceQuickSort();
						Object[] ordered = dq.sort(list);
						final int length = ordered.length;

						list = new ArrayList();
						for (int i = 0; i < length; i++) {
							list.add(ordered[i]);
						}
					}
				}

				if (list.size() == 0) {
					POI p = new POI();
					p.setDescription(HelloListView.this.getResources()
							.getString(R.string.no_results));
					list.add(p);
				}

				results.values = list;
				results.count = list.size();

				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// noinspection unchecked
				// mData = (List<Map<String, ?>>) results.values;
				if (results.count <= 0) {
					list = null;
					getListView().setFastScrollEnabled(true);
					notifyDataSetChanged();
				} else {
					list = (ArrayList) results.values;
					getListView().setFastScrollEnabled(false);
					notifyDataSetInvalidated();
				}
			}
		}
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
		if (arg0.length() < 5 && !searchOptions.isPrefixSearch()) {
			Toast.makeText(HelloListView.this, R.string.type,
					Toast.LENGTH_SHORT).show();
			return;
		}
		adapter.getFilter().filter(arg0.toString().toLowerCase());
		autoCompleteAdapter.getFilter().filter(arg0.toString().toLowerCase());
		setProgressBarIndeterminateVisibility(true);
	}

	private class DistanceQuickSort extends CollectionQuickSort {

		@Override
		public boolean less(Object x, Object y) {
			Point xx = (Point) x;
			Point yy = (Point) y;

			double ix = center.distance(xx);
			double iy = center.distance(yy);

			return (ix <= iy);
		}

	}

	private class InitialQuickSort extends CollectionQuickSort {

		@Override
		public boolean less(Object x, Object y) {
			Metadata.InitialNumber xx = (Metadata.InitialNumber) x;
			Metadata.InitialNumber yy = (Metadata.InitialNumber) y;

			String ix = xx.initial;
			String iy = yy.initial;

			return (ix.charAt(0) < iy.charAt(0));
		}

	}

	private class SearchOptions {
		String filter;
		String sort;

		public boolean isPrefixSearch() {
			if (filter == null)
				return false;
			return (filter.compareTo(getResources().getString(
					R.string.search_prefix)) == 0);
		}

		public boolean sortResults() {
			if (sort == null)
				return false;
			return (sort.compareTo(getResources().getString(
					R.string.sort_distance)) == 0);
		}
	}

	private class AutoCompleteAdapter extends BaseAdapter implements Filterable {

		private AutoCompleteFilter mFilter;
		ArrayList list = null;

		@Override
		public int getCount() {
			if (list != null)
				return list.size();
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			if (list != null) {
				return list.get(arg0);
			}

			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.street_row, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.desc);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			String desc = Utilities.capitalizeFirstLetters(getItem(arg0)
					.toString());

			// Bind the data efficiently with the holder.
			holder.text.setTextColor(Color.BLACK);
			holder.text.setTextSize(16);
			holder.text.setText(desc);

			return convertView;
		}

		@Override
		public Filter getFilter() {
			if (mFilter == null) {
				mFilter = new AutoCompleteFilter();
			}
			return mFilter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#notifyDataSetChanged()
		 */
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			// setProgressBarIndeterminateVisibility(false);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.BaseAdapter#notifyDataSetInvalidated()
		 */
		@Override
		public void notifyDataSetInvalidated() {
			// TODO Auto-generated method stub
			super.notifyDataSetInvalidated();
			// setProgressBarIndeterminateVisibility(false);
		}

		class ViewHolder {
			TextView text;
		}

		/**
		 * <p>
		 * An array filters constrains the content of the array adapter with a
		 * prefix. Each item that does not start with the supplied prefix is
		 * removed from the list.
		 * </p>
		 */
		private class AutoCompleteFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence prefix) {
				FilterResults results = new FilterResults();

				SpatialIndexRoot root = ((SpatialIndexRoot) HelloListView.this.osmStreetProvider
						.getHelper().getRoot());

				if (prefix == null) {
					results.count = 0;
					return results;
				}

				if (prefix.toString().length() <= 0) {
					results.count = 0;
					return results;
				}

				Iterator<FullTextIndex.Keyword> it = root
						.getFullTextStreetIndex()
						.getKeywords(prefix.toString());

				ArrayList list = new ArrayList();
				while (it.hasNext()) {
					list.add(it.next().getNormalForm());
				}

				if (list.size() == 0) {
					results.count = 0;
					return results;
				}

				results.values = list;
				results.count = list.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// noinspection unchecked
				// mData = (List<Map<String, ?>>) results.values;
				if (results.count <= 0) {
					list = null;
					// getListView().setFastScrollEnabled(true);
					notifyDataSetChanged();
				} else {
					list = (ArrayList) results.values;
					// getListView().setFastScrollEnabled(false);
					notifyDataSetInvalidated();
				}
			}
		}
	}

	private class SpaceTokenizer implements Tokenizer {

		@Override
		public int findTokenEnd(CharSequence text, int cursor) {
			int i = cursor;
			int len = text.length();

			while (i < len) {
				if (text.charAt(i) == ' ') {
					return i;
				} else {
					i++;
				}
			}
			return len;
		}

		@Override
		public int findTokenStart(CharSequence text, int cursor) {
			int i = cursor;

			while (i > 0 && text.charAt(i - 1) != ' ') {
				i--;
			}

			while (i < cursor && text.charAt(i) == ' ') {
				i++;
			}

			return i;
		}

		@Override
		public CharSequence terminateToken(CharSequence text) {
			int i = text.length();

			while (i > 0 && text.charAt(i - 1) == ' ') {
				i--;
			}

			if (i > 0 && text.charAt(i - 1) == ' ') {
				return text;
			} else {
				if (text instanceof Spanned) {
					SpannableString sp = new SpannableString(text + " ");
					TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
							Object.class, sp, 0);
					return sp;
				} else {
					return text + " ";
				}
			}
		}
	}

	private class POIAlphabeticalQuickSort extends CollectionQuickSort {

		@Override
		public boolean less(Object x, Object y) {
			POI xx = (POI) x;
			POI yy = (POI) y;

			String ix = xx.getDescription();
			String iy = yy.getDescription();

			int compare = ix.compareTo(iy);
			return (compare <= 0);
		}

	}
}