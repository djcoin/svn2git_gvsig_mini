package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.widget.SectionIndexer;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.MetadataInitialCharQuickSort;

public class LazyAdapter extends FilteredLazyAdapter implements SectionIndexer {

	HashMap<String, Integer> alphaIndexer = new HashMap();

	String[] sections;	

	public LazyAdapter(SearchActivity activity) {
		super(activity);
		int length = 0;
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
}
