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

package es.prodevelop.gvsig.mini.search.filter;

import java.util.ArrayList;
import java.util.Iterator;

import org.garret.perst.fulltext.FullTextIndex;

import android.app.Activity;
import android.util.Log;
import android.widget.Filter;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstQuadtreeProvider;
import es.prodevelop.gvsig.mini.search.activities.SearchActivityWrapper;

/**
 * <p>
 * An array filters constrains the content of the array adapter with a prefix.
 * Each item that does not start with the supplied prefix is removed from the
 * list.
 * </p>
 */
public class AutoCompleteFilter extends Filter {

	private SearchActivityWrapper activity;

	public AutoCompleteFilter(SearchActivityWrapper activity) {
		this.activity = activity;
	}

	@Override
	protected FilterResults performFiltering(CharSequence prefix) {
		FilterResults results = new FilterResults();

		SpatialIndexRoot root = ((SpatialIndexRoot) ((PerstQuadtreeProvider) activity
				.getProvider()).getHelper().getRoot());

		if (prefix == null) {
			results.count = 0;
			return results;
		}

		if (prefix.toString().length() <= 0) {
			results.count = 0;
			return results;
		}

		Iterator<FullTextIndex.Keyword> it = root.getFullTextIndex()
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
	protected void publishResults(CharSequence constraint, FilterResults results) {
		// noinspection unchecked
		// mData = (List<Map<String, ?>>) results.values;
		Log.d("", "AutoComplete publish results");
		if (results.count <= 0) {
			activity.getAutoCompleteAdapter().setList(null);
			// getListView().setFastScrollEnabled(true);
			activity.getAutoCompleteAdapter().notifyDataSetChanged();
		} else {
			activity.getAutoCompleteAdapter().setList(
					(ArrayList) results.values);
			// getListView().setFastScrollEnabled(false);
			activity.getAutoCompleteAdapter().notifyDataSetInvalidated();
		}	
	}
}
