package es.prodevelop.gvsig.mini.search;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.garret.perst.fulltext.FullTextSearchResult;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.MetadataInitialCharQuickSort;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class LazyAdapter extends BaseAdapter implements SectionIndexer,
		Filterable {

	HashMap<String, Integer> alphaIndexer = new HashMap();

	String[] sections;
	Metadata metadata;
	private SimpleFilter mFilter;
	SearchActivity activity;
	private DecimalFormat formatter = new DecimalFormat("####.00");	

	public LazyAdapter(SearchActivity activity) {
		this.activity = activity;
		int length = 0;
		metadata = ((PerstOsmPOIProvider) activity.getProvider()).getMetadata();
		ArrayList t = metadata.getStreetInitialNumber();

		MetadataInitialCharQuickSort iq = new MetadataInitialCharQuickSort();
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

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (activity.getResultsList() != null)
			return activity.getResultsList().size();
		return metadata.getTotalStreets();
	}

	@Override
	public Object getItem(int arg0) {
		if (activity.getResultsList() != null) {
			return activity.getResultsList().get(arg0);
		}

		return ((SpatialIndexRoot) ((PerstOsmPOIProvider) activity
				.getProvider()).getHelper().getRoot()).getStreetIndex().getAt(
				arg0);
	}

	@Override
	public long getItemId(int arg0) {
		if (activity.getResultsList() != null)
			return ((POI) activity.getResultsList().get(arg0)).getId();
		return ((POI) ((SpatialIndexRoot) ((PerstOsmPOIProvider) activity
				.getProvider()).getHelper().getRoot()).getStreetIndex().getAt(
				arg0)).getId();
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
			convertView = activity.getLayoutInflater().inflate(
					R.layout.street_row, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.desc);
			holder.dist = (TextView) convertView.findViewById(R.id.dist);
			holder.poiImg = (ImageView) convertView.findViewById(R.id.img);
			try {
				LinearLayout l = new LinearLayout(activity);
				final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				zzParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				zzParams.addRule(RelativeLayout.CENTER_VERTICAL);
				// zzParams.setMargins(20, 20, 20, 20);
				MapPreview preview = new MapPreview(activity, CompatManager
						.getInstance().getRegisteredContext(),
						activity.metrics.widthPixels, activity.metrics.heightPixels / 2);
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
				if ((getItem(arg0) instanceof OsmPOIStreet)) {
					holder.previewLayout.setVisibility(View.VISIBLE);
					holder.preview.setMapCenterFromLonLat(p);
					holder.preview.setExtent(((OsmPOIStreet) p)
							.getBoundingBox());
				}
			} else {
				holder.previewLayout.setVisibility(View.GONE);
			}
		}

		// Bind the data efficiently with the holder.
		holder.text.setText(desc);
		holder.dist.setText(activity.getResources()
				.getString(R.string.distance)
				+ " "
				+ formatter.format(activity.getSearchOptions().center
						.distance(ConversionCoords.reproject(p.getX(),
								p.getY(), CRSFactory.getCRS("EPSG:4326"),
								CRSFactory.getCRS("EPSG:900913")))));
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
			mFilter = new SimpleFilter(activity);
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
		activity.setProgressBarIndeterminateVisibility(false);
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
		activity.setProgressBarIndeterminateVisibility(false);
	}

	class ViewHolder {
		TextView text;
		TextView dist;
		MapPreview preview;
		LinearLayout previewLayout;
		ImageView poiImg;
	}
}
