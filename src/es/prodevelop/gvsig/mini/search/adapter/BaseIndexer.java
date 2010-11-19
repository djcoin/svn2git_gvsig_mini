package es.prodevelop.gvsig.mini.search.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.util.Log;
import android.widget.SectionIndexer;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.android.spatialindex.poi.MetadataInitialCharQuickSort;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class BaseIndexer implements SectionIndexer {

	HashMap<String, Integer> alphaIndexer = new HashMap();

	String[] sections;
	int[] sectionsPos;
	int totalLength;

	public BaseIndexer(SearchActivity activity) {
		Indexed category = activity.getSearchOptions().getFilteredIndexed();
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
		sectionsPos = new int[size];
		// int c = 0;
		// sectionsPos = new int[size];
		for (int i = 0; i < size; i++) {
			// c++;
			Metadata.InitialNumber initial = (Metadata.InitialNumber) ordered[i];
			alphaIndexer.put(String.valueOf(initial.initial).toUpperCase(),
					length);
			sectionsPos[i] = length;
			length += initial.number;
			// length += c;
			// sectionsPos[i] = length;
			sections[i] = String.valueOf(initial.initial).toUpperCase();
		}
		totalLength = length;
	}

	private void fillCategorySectionIndexer(Metadata.Category category) {
		if (category.name.compareTo(POICategories.STREETS) == 0)
			fillSectionIndexer(category);
		else {
			int length = 0;

			final int size = category.subcategories.size();
			sections = new String[size];
			sectionsPos = new int[size];
			Indexed ix;
			String text;
			for (int i = 0; i < size; i++) {
				ix = (Indexed) category.subcategories.get(i);
				text = Utilities.capitalizeFirstLetters(ix.name.replaceAll("_",
						" "));
				alphaIndexer.put(text, length);
				sectionsPos[i] = length;
				length += ix.total;
				sections[i] = text;
			}
			totalLength = length;
		}
	}

	@Override
	public int getPositionForSection(int section) {
		// Log.v("getPositionForSection", ""+section);
		if (section < 0 || section >= sections.length) {
			return -1;
		}
		String letter = sections[section];

		return alphaIndexer.get(letter);
	}

	@Override
	public int getSectionForPosition(int position) {

		// you will notice it will be never called (right?)
		Log.v("getSectionForPosition", "called");
		if (position < 0 || position >= totalLength) {
			return -1;
		}

		int index = Arrays.binarySearch(sectionsPos, position);

		/*
		 * Consider this example: section positions are 0, 3, 5; the supplied
		 * position is 4. The section corresponding to position 4 starts at
		 * position 3, so the expected return value is 1. Binary search will not
		 * find 4 in the array and thus will return -insertPosition-1, i.e. -3.
		 * To get from that number to the expected value of 1 we need to negate
		 * and subtract 2.
		 */
		return index >= 0 ? index : -index - 2;

	}

	@Override
	public Object[] getSections() {
		return sections; // to string will be called each object, to display
		// the letter
	}

	// @Override
	// public View getView(int arg0, View convertView, ViewGroup arg2) {
	// Log.d("LazyAdapter", "getView");
	// int pos = arg0;
	// final int size = sectionsPos.length;
	// for (int i = 0; i < size; i++) {
	// if (sectionsPos[i] == pos) {
	// Log.d("LazyAdapter", "Found position");
	// LinearLayout v = (LinearLayout) this.activity
	// .getLayoutInflater()
	// .inflate(R.layout.list_header, null);
	// TextView t = (TextView) v.findViewById(R.id.list_header_title);
	// t.setText(String.valueOf(this.getSectionForPosition(pos)));
	// return v;
	// }
	// }
	// return super.getView(arg0, convertView, arg2);
	// }

}
