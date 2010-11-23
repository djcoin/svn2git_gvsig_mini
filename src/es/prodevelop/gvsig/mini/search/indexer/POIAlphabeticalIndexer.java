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
import java.util.Collection;

import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;

public class POIAlphabeticalIndexer extends BaseIndexer {

	POIAlphabeticalQuickSort qs;

	public POIAlphabeticalIndexer() {
		qs = new POIAlphabeticalQuickSort();
	}

	@Override
	public CollectionQuickSort getQuickSorter() {
		return qs;
	}

	@Override
	public ArrayList sortAndIndex(Collection list) {
		Object[] sorted = qs.sort(list);
		int size = sorted.length;

		int length = 0;

		size = sorted.length;
		ArrayList sections = new ArrayList();
		ArrayList sectionsPos = new ArrayList();
		char currentChar = '*';

		POI p;
		ArrayList ordered = new ArrayList();
		int counter = 0;
		for (int i = 0; i < size; i++) {
			p = (POI) sorted[i];
			char poiChar = p.getDescription().toUpperCase().charAt(0);
			if (poiChar != currentChar) {
				indexer.put(String.valueOf(poiChar).toUpperCase(), length);
				currentChar = poiChar;
				sectionsPos.add(length);
				sections.add(String.valueOf(poiChar).toUpperCase());
				counter++;
			}

			length++;
			ordered.add(sorted[i]);
		}
		totalLength = length;

		final int l = sectionsPos.size();
		this.sections = new String[l];
		this.sectionsPos = new int[l];
		for (int i = 0; i < l; i++) {
			this.sections[i] = (String) sections.get(i);
			this.sectionsPos[i] = (Integer) sectionsPos.get(i);
		}

		return ordered;
	}
}
