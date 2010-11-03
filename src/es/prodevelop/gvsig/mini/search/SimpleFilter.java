package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;
import java.util.Iterator;

import org.garret.perst.fulltext.FullTextSearchResult;

import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Filter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;

public class SimpleFilter extends Filter {

	private SearchOptions searchOptions;
	private SearchActivity activity;

	public SimpleFilter(SearchActivity searchActivity) {
		this.searchOptions = searchActivity.getSearchOptions();
		this.activity = searchActivity;
	}

	@Override
	protected FilterResults performFiltering(CharSequence prefix) {
		FilterResults results = new FilterResults();

		boolean byPrefix = true;
		final LazyAdapter adapter;
		adapter = (LazyAdapter) activity.getListView().getAdapter();

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

			SpatialIndexRoot root = ((SpatialIndexRoot) ((PerstOsmPOIProvider) activity
					.getProvider()).getHelper().getRoot());

			String desc = prefix.toString().trim();
			if (!desc.contains(" 'or' ") && !desc.contains(" 'and' ")) {
				desc = prefix.toString().trim().replaceAll(" ", " AND ");
			} else {
				desc = desc.replaceAll(" 'or' ", " OR ");
				desc = desc.replaceAll(" 'and' ", " AND ");
			}

			FullTextSearchResult result = root.getFullTextStreetIndex().search(
					desc, "en", 1000, 1000);

			final int size = result.hits.length;
			for (int i = 0; i < size; i++) {
				list.add(result.hits[i].getDocument());
			}

			if (searchOptions.sortResults()) {
				if (searchOptions.isSortByDistance()) {
					final PointDistanceQuickSort dq = new PointDistanceQuickSort(
							searchOptions.center);
					Object[] ordered = dq.sort(list);
					final int length = ordered.length;

					list = new ArrayList();
					for (int i = 0; i < length; i++) {
						list.add(ordered[i]);
					}
				} else {
					final POIAlphabeticalQuickSort dq = new POIAlphabeticalQuickSort();
					Object[] ordered = dq.sort(list);
					final int length = ordered.length;

					list = new ArrayList();
					for (int i = 0; i < length; i++) {
						list.add(ordered[i]);
					}
				}
			}

			/*
			 * Sort POIs alphabetically
			 */
			// PointDistanceQuickSort quickSort = new
			// PointDistanceQuickSort(searchOptions.center);
			// Object[] pois = quickSort.sort(list);
			//
			// list = new ArrayList();
			// final int l = pois.length;
			// for (int i = 0; i < l; i++) {
			// list.add(pois[i]);
			// }
		} else {
			if (byPrefix) {
				list = (ArrayList) ((PerstOsmPOIProvider) activity
						.getProvider()).getStreetsByPrefixName(prefix
						.toString());
			} else {
				long t1 = System.currentTimeMillis();

				Iterator it = (Iterator) ((PerstOsmPOIProvider) activity
						.getProvider()).getStreetsByName(prefix.toString(),
						false);

				Log.d("TIME", (System.currentTimeMillis() - t1) + "");
				t1 = System.currentTimeMillis();
				while (it.hasNext())
					list.add(it.next());
				Log.d("TIME", (System.currentTimeMillis() - t1) + "");
			}

			if (searchOptions.sortResults()) {
				final PointDistanceQuickSort dq = new PointDistanceQuickSort(
						searchOptions.center);
				Object[] ordered = dq.sort(list);
				final int length = ordered.length;

				list = new ArrayList();
				for (int i = 0; i < length; i++) {
					list.add(ordered[i]);
				}
			}
		}

		if (list.size() == 0) {
			POI p = new POI();
			p.setDescription(activity.getResources().getString(
					R.string.no_results));
			list.add(p);
		}

		results.values = list;
		results.count = list.size();

		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		if (results.count <= 0) {
			activity.setResultsList(null);
			// activity.attachSectionedAdapter();
			activity.getListView().setFastScrollEnabled(true);
			((BaseAdapter) activity.getListView().getAdapter())
					.notifyDataSetChanged();
		} else {
			activity.setResultsList((ArrayList) results.values);
			// activity.attachFilteredAdapter();
			activity.getListView().setFastScrollEnabled(false);
			((BaseAdapter) activity.getListView().getAdapter())
					.notifyDataSetInvalidated();
		}
	}
}
