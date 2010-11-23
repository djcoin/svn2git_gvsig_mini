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

import org.garret.perst.fulltext.FullTextSearchResult;

import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.MemoryAdapter;

public class KeywordFilter extends SimpleFilter {

	public KeywordFilter(SearchActivity searchActivity) {
		super(searchActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected FilterResults performFiltering(CharSequence prefix) {
		FilterResults results = new FilterResults();

		boolean byPrefix = true;
		final ListAdapter adapter;
		adapter = (ListAdapter) activity.getListView().getAdapter();

		if (prefix == null) {
			results.count = 0;
			return results;
		}

		if (prefix.toString().length() <= 0) {
			results.count = 0;
			return results;
		}

		ArrayList list = new ArrayList();

		SpatialIndexRoot root = ((SpatialIndexRoot) ((PerstOsmPOIProvider) activity
				.getProvider()).getHelper().getRoot());

		String desc = prefix.toString();

		FullTextSearchResult result = root.getFullTextIndex().search(desc,
				SpatialIndexRoot.DEFAULT_LANGUAGE,
				SpatialIndexRoot.DEFAULT_MAX_RESULTS,
				SpatialIndexRoot.DEFAULT_MAX_TIME);

		final int size = result.hits.length;
		for (int i = 0; i < size; i++) {
			list.add(result.hits[i].getDocument());
		}

		
		list = sortResults(list);
		

		results.values = list;
		results.count = list.size();

		return results;
	}	

	// @Override
	// protected void publishResults(CharSequence constraint, FilterResults
	// results) {
	// try {
	// if (results.count <= 0) {
	// activity.setResultsList(null);
	// // activity.attachSectionedAdapter();
	// activity.getListView().setFastScrollEnabled(true);
	// ((BaseAdapter) activity.getListView().getAdapter())
	// .notifyDataSetChanged();
	// } else {
	// activity.setResultsList((ArrayList) results.values);
	// // activity.attachFilteredAdapter();
	// activity.getListView().setFastScrollEnabled(false);
	// ((BaseAdapter) activity.getListView().getAdapter())
	// .notifyDataSetInvalidated();
	// if (results.count > 1)
	// activity.enableSpinner();
	// }
	// } catch (Exception e) {
	// Log.e("", e.getMessage());
	// } catch (OutOfMemoryError ex) {
	// //FIXME why?
	// System.gc();
	// Log.e("", ex.getMessage());
	// }
	// }

}
