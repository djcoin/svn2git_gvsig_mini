package es.prodevelop.gvsig.mini.search.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.android.spatialindex.poi.MetadataInitialCharQuickSort;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.FilteredLazyAdapter.ViewHolder;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class LazyAdapter extends FilteredLazyAdapter implements SectionIndexer {

	HashMap<String, Integer> alphaIndexer = new HashMap();

	String[] sections;
//	int[] sectionsPos;

	public LazyAdapter(SearchActivity activity) {
		super(activity);
		if (category instanceof Metadata.Subcategory) {
			fillSectionIndexer(category);
		} else {
			fillCategorySectionIndexer((Metadata.Category) category);
		}
	}

	private void fillSectionIndexer(Indexed category) {
		int length = 0;
		ArrayList t = category.initialNumbers;

		MetadataInitialCharQuickSort iq = new MetadataInitialCharQuickSort();
		Object[] ordered = iq.sort(t);

		final int size = ordered.length;
		sections = new String[size];
//		int c = 0;
//		sectionsPos = new int[size];
		for (int i = 0; i < size; i++) {
//			c++;
			Metadata.InitialNumber initial = (Metadata.InitialNumber) ordered[i];
			alphaIndexer.put(String.valueOf(initial.initial).toUpperCase(),
					length);
			length += initial.number;
//			length += c;
//			sectionsPos[i] = length;
			sections[i] = String.valueOf(initial.initial).toUpperCase();
		}
	}

	private void fillCategorySectionIndexer(Metadata.Category category) {
		if (category.name.compareTo(POICategories.STREETS) == 0)
			fillSectionIndexer(category);
		else {
			int length = 0;

			final int size = category.subcategories.size();
			sections = new String[size];
			Indexed ix;
			String text;
			for (int i = 0; i < size; i++) {
				ix = (Indexed) category.subcategories.get(i);
				text = Utilities.capitalizeFirstLetters(ix.name.replaceAll("_",
						" "));
				alphaIndexer.put(text, length);
				length += ix.total;
				sections[i] = text;
			}
		}
	}

	@Override
	public int getPositionForSection(int section) {
		// Log.v("getPositionForSection", ""+section);
		String letter = sections[section];

		return alphaIndexer.get(letter);
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

//	@Override
//	public View getView(int arg0, View convertView, ViewGroup arg2) {
//		Log.d("LazyAdapter", "getView");
//		int pos = arg0;
//		final int size = sectionsPos.length;
//		for (int i = 0; i < size; i++) {
//			if (sectionsPos[i] == pos) {
//				Log.d("LazyAdapter", "Found position");
//				LinearLayout v = (LinearLayout) this.activity
//						.getLayoutInflater()
//						.inflate(R.layout.list_header, null);
//				TextView t = (TextView) v.findViewById(R.id.list_header_title);
//				t.setText(String.valueOf(this.getSectionForPosition(pos)));
//				return v;
//			}
//		}
//		return super.getView(arg0, convertView, arg2);
//	}
}
