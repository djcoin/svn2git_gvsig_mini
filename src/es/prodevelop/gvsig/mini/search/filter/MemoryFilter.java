package es.prodevelop.gvsig.mini.search.filter;

import java.util.ArrayList;

import org.garret.perst.RectangleR2;
import org.garret.perst.SpatialIndexR2;

import android.widget.ListAdapter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.POICategoryDistanceQuickSort;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.MemoryAdapter;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class MemoryFilter extends SimpleFilter {

	public final static int STREETS = 0;
	public final static int POIS = 1;

	private int type;
	private int distance;

	public MemoryFilter(SearchActivity searchActivity, int type, int distance) {
		super(searchActivity);
		this.type = type;
		this.distance = distance;
	}

	@Override
	protected FilterResults performFiltering(CharSequence prefix) {
		FilterResults results = new FilterResults();

		if (prefix == null) {
			results.count = 0;
			return results;
		}

		if (prefix.toString().length() <= 0) {
			results.count = 0;
			return results;
		}

		final ListAdapter adapter;
		adapter = (ListAdapter) activity.getListView().getAdapter();

		ArrayList list = new ArrayList();

		SpatialIndexRoot root = ((SpatialIndexRoot) ((PerstOsmPOIProvider) activity
				.getProvider()).getHelper().getRoot());

		final SpatialIndexR2 index;

		if (type == POIS) {
			index = root.getPoiIndex();
		} else {
			index = root.getStreetSpatialIndex();
		}

		// if result list is null
		// parse query
		// get arraylist from spatial index
		// set result list
		// else
		// sort results

		Point point = Point.parseString(prefix.toString());
		if (activity.getResultsList() == null) {
			double[] xy = ConversionCoords.reproject(point.getX(),
					point.getY(), CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS("EPSG:900913"));
			double[] minXY = new double[] { xy[0] - distance, xy[1] - distance };
			double[] maxXY = new double[] { xy[0] + distance, xy[1] + distance };

			double[] minLonLat = ConversionCoords.reproject(minXY[0], minXY[1],
					CRSFactory.getCRS("EPSG:900913"),
					CRSFactory.getCRS("EPSG:4326"));
			double[] maxLonLat = ConversionCoords.reproject(maxXY[0], maxXY[1],
					CRSFactory.getCRS("EPSG:900913"),
					CRSFactory.getCRS("EPSG:4326"));

			final RectangleR2 rect = new RectangleR2(minLonLat[1],
					minLonLat[0], maxLonLat[1], maxLonLat[0]);
			list = (ArrayList) index.getList(rect);

			POICategoryDistanceQuickSort q = new POICategoryDistanceQuickSort(
					((MemoryAdapter) this.activity.listAdapter)
							.getCenterMercator());
			Object[] ordered = q.sort(list);
			final int length = ordered.length;

			list = new ArrayList();
			for (int i = 0; i < length; i++) {
				list.add(ordered[i]);
			}
		} else {
			if (searchOptions.sortResults()) {
				if (searchOptions.isSortByDistance()) {
					// double[] lonlat = ConversionCoords.reproject(
					// searchOptions.center.getX(),
					// searchOptions.center.getY(),
					// CRSFactory.getCRS("EPSG:900913"),
					// CRSFactory.getCRS("EPSG:4326"));
					final PointDistanceQuickSort dq = new PointDistanceQuickSort(
							/* new Point(lonlat[0], lonlat[1]) */((MemoryAdapter) this.activity.listAdapter)
									.getCenterMercator());
					Object[] ordered = dq.sort(activity.getResultsList());
					final int length = ordered.length;

					list = new ArrayList();
					for (int i = 0; i < length; i++) {
						list.add(ordered[i]);
					}
				} else {
					final POIAlphabeticalQuickSort dq = new POIAlphabeticalQuickSort();
					Object[] ordered = dq.sort(activity.getResultsList());
					final int length = ordered.length;

					list = new ArrayList();
					for (int i = 0; i < length; i++) {
						list.add(ordered[i]);
					}
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

}
