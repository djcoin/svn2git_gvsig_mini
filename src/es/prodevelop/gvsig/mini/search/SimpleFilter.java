package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;

import org.garret.perst.fulltext.FullTextSearchResult;

import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListAdapter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class SimpleFilter extends Filter {

	protected SearchOptions searchOptions;
	protected SearchActivity activity;

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

			SpatialIndexRoot root = ((SpatialIndexRoot) ((PerstOsmPOIProvider) activity
					.getProvider()).getHelper().getRoot());

			// FIXME filter by category || subcategory -> SearchOptions
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

			FullTextSearchResult result = root.getFullTextIndex().search(desc,
					SpatialIndexRoot.DEFAULT_LANGUAGE,
					SpatialIndexRoot.DEFAULT_MAX_RESULTS,
					SpatialIndexRoot.DEFAULT_MAX_TIME);

			final int size = result.hits.length;
			for (int i = 0; i < size; i++) {
				list.add(result.hits[i].getDocument());
			}

			if (searchOptions.sortResults()) {
				if (searchOptions.isSortByDistance()) {
//					double[] lonlat = ConversionCoords.reproject(
//							searchOptions.center.getX(),
//							searchOptions.center.getY(),
//							CRSFactory.getCRS("EPSG:900913"),
//							CRSFactory.getCRS("EPSG:4326"));
					final PointDistanceQuickSort dq = new PointDistanceQuickSort(
							/*new Point(lonlat[0], lonlat[1])*/searchOptions.center);
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

			if (list.size() == 0) {
				POI p = new POI();
				p.setDescription(activity.getResources().getString(
						R.string.no_results));
				list.add(p);
			}

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
				if (results.count > 1)
					activity.enableSpinner();
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		} catch (OutOfMemoryError ex) {
			//FIXME why?
			System.gc();
			Log.e("", ex.getMessage());
		}
	}
}
