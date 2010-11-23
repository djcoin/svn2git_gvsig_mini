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

import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;

public class BaseIndexer implements SortSectionIndexer {

	String[] sections;
	int[] sectionsPos;
	int totalLength;
	HashMap<String, Integer> indexer = new HashMap<String, Integer>();

	@Override
	public int getPositionForSection(int section) {
		// Log.v("getPositionForSection", ""+section);
		if (sections == null)
			return -1;
		if (section < 0 || section >= sections.length) {
			return -1;
		}
		String letter = sections[section];
		if (letter == null)
			return -1;

		return indexer.get(letter);
	}

	@Override
	public int getSectionForPosition(int position) {
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
		if (sections == null)
			return new String[] { "" };
		return sections; // to string will be called each object, to display
		// the letter
	}

	@Override
	public CollectionQuickSort getQuickSorter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList sortAndIndex(Collection list) {
		return (ArrayList) list;
	}

}
