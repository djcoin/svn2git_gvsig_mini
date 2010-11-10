package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.widget.SectionIndexer;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.android.spatialindex.poi.MetadataInitialCharQuickSort;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class LazyAdapter extends FilteredLazyAdapter implements
		SectionIndexer {

	HashMap<String, Integer> alphaIndexer = new HashMap();

	String[] sections;

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
		for (int i = 0; i < size; i++) {
			Metadata.InitialNumber initial = (Metadata.InitialNumber) ordered[i];
			alphaIndexer.put(String.valueOf(initial.initial).toUpperCase(), length);
			length += initial.number;
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
}
