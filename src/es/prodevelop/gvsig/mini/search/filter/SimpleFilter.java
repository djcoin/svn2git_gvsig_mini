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
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.SearchOptions;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.PinnedHeaderListAdapter;
import es.prodevelop.gvsig.mini.search.indexer.SortSectionIndexer;

public class SimpleFilter extends Filter {

	protected SearchOptions searchOptions;
	protected SearchActivity activity;
	private SectionIndexer indexer;
	public final static String WILDCARD = "*";

	public SimpleFilter(SearchActivity searchActivity) {
		this.searchOptions = searchActivity.getSearchOptions();
		this.activity = searchActivity;
	}

	protected StringBuffer buildQuery(String prefix, ArrayList cat) {
		StringBuffer temp = new StringBuffer();

		final int size = cat.size();
		for (int i = 0; i < size; i++) {
			temp.append(prefix.trim().toString().replaceAll(" ", " AND "));
			temp.append(" AND 0"
					+ cat.get(i).toString().toLowerCase().replaceAll("_", "")
					+ "0");

			if (i != size - 1)
				temp.append(" OR ");
		}
		return temp;
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
		if (prefix.toString().contains(" ")) {

			String desc = prefix.toString();
			if (!desc.contains(" 'or' ") && !desc.contains(" 'and' ")) {
				StringBuffer temp = buildQuery(desc,
						this.searchOptions.getCategories());
				temp.append(buildQuery(desc,
						this.searchOptions.getSubcategories()));
				desc = temp.toString();
				Log.d("SIMPLE FILTER", desc);
			} else {
				desc = desc.replaceAll(" 'or' ", " OR ");
				desc = desc.replaceAll(" 'and' ", " AND ");
			}

			list = ((PerstOsmPOIProvider) activity.getProvider())
					.fullTextSearch(desc);
			list = sortResults(list);

			results.values = list;
			results.count = list.size();
		} else {
			// if (byPrefix) {
			// list = (ArrayList) ((PerstOsmPOIProvider) activity
			// .getProvider()).getStreetsByPrefixName(prefix
			// .toString());
			// } else {
			// long t1 = System.currentTimeMillis();
			//
			// Iterator it = (Iterator) ((PerstOsmPOIProvider) activity
			// .getProvider()).getStreetsByName(prefix.toString(),
			// false);
			//
			// Log.d("TIME", (System.currentTimeMillis() - t1) + "");
			// t1 = System.currentTimeMillis();
			// while (it.hasNext())
			// list.add(it.next());
			// Log.d("TIME", (System.currentTimeMillis() - t1) + "");
			// }
			//
			// if (searchOptions.sortResults()) {
			// final PointDistanceQuickSort dq = new PointDistanceQuickSort(
			// searchOptions.center);
			// Object[] ordered = dq.sort(list);
			// final int length = ordered.length;
			//
			// list = new ArrayList();
			// for (int i = 0; i < length; i++) {
			// list.add(ordered[i]);
			// }
			// }
		}

		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		try {
			// First disable fast scroll... or it won't change the
			// SectionIndexer
			activity.getListView().setFastScrollEnabled(false);
			((PinnedHeaderListAdapter) activity.getListAdapter())
					.setIndexer(indexer);
			if (results.count <= 0) {
				activity.setResultsList(null);
				((PinnedHeaderListAdapter) activity.getListAdapter())
						.setDefaultIndexer();
				// activity.attachSectionedAdapter();
				// activity.getListView().setFastScrollEnabled(true);
				((BaseAdapter) activity.getListView().getAdapter())
						.notifyDataSetChanged();
			} else {
				activity.setResultsList((ArrayList) results.values);
				// activity.attachFilteredAdapter();
				// activity.getListView().setFastScrollEnabled(false);
				((BaseAdapter) activity.getListView().getAdapter())
						.notifyDataSetChanged();
				if (results.count > 1)
					activity.enableSpinner();
			}
			if (indexer == null) {
				activity.getListView().setFastScrollEnabled(false);
			} else {
				activity.getListView().setFastScrollEnabled(true);
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		} catch (OutOfMemoryError ex) {
			// FIXME why?
			System.gc();
			Log.e("", ex.getMessage());
		}
	}

	public ArrayList sortResults(ArrayList list) {
		this.indexer = null;
		if (list.size() == 0) {
			POI p = new POI();
			p.setDescription(getNoResultsText());
			list.add(p);
		} else {
			if (searchOptions.sortResults()) {
				final SortSectionIndexer indexer = searchOptions
						.getSortIndexer(getCenterMercator());
				list = indexer.sortAndIndex(list);
				this.indexer = indexer;
			}
		}

		return list;
	}

	public Point getCenterMercator() {
		return searchOptions.getCenterMercator();
	}

	public String getNoResultsText() {
		return activity.getResources().getString(R.string.no_results);
	}
}
