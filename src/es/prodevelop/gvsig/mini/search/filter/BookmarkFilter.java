package es.prodevelop.gvsig.mini.search.filter;

import java.util.ArrayList;

import android.widget.ListAdapter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstBookmarkProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;

public class BookmarkFilter extends SimpleFilter {

	public BookmarkFilter(SearchActivity searchActivity) {
		super(searchActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected FilterResults performFiltering(CharSequence prefix) {
		FilterResults results = new FilterResults();

		final ListAdapter adapter;
		adapter = (ListAdapter) activity.getListView().getAdapter();

		ArrayList list = new ArrayList();

		PerstBookmarkProvider provider = POIProviderManager.getInstance()
				.getBookmarkProvider();

		ArrayList pois = provider.getPOIs();
		if (pois != null && pois.size() > 0) {
			list.addAll(pois);
		}

		pois = provider.getStreets();
		if (pois != null && pois.size() > 0) {
			list.addAll(pois);
		}

		if (searchOptions.sortResults()) {
			final CollectionQuickSort qs = searchOptions
					.getQuickSorter(searchOptions.getCenterMercator());

			if (qs != null) {
				Object[] ordered = qs.sort(list);
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
					R.string.no_bookmark));
			list.add(p);
		}

		results.values = list;
		results.count = list.size();

		return results;
	}
}
