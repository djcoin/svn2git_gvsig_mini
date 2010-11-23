/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.search.indexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import android.util.Log;
import android.widget.SectionIndexer;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.android.spatialindex.poi.MetadataInitialCharQuickSort;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.utiles.Utilities;

public class CategoryIndexer extends BaseIndexer {

	public CategoryIndexer(SearchActivity activity) {
		Indexed category = activity.getSearchOptions().getFilteredIndexed();
		if (category == null)
			return;
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
			indexer.put(String.valueOf(initial.initial).toUpperCase(), length);
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
				indexer.put(text, length);
				sectionsPos[i] = length;
				length += ix.total;
				sections[i] = text;
			}
			totalLength = length;
		}
	}
}
