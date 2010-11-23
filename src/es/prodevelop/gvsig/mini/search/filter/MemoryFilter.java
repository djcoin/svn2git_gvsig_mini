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

import org.garret.perst.RectangleR2;
import org.garret.perst.SpatialIndexR2;

import android.widget.ListAdapter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.MemoryAdapter;
import es.prodevelop.gvsig.mini.search.indexer.POICategoryDistanceQuickSort;
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
			list = activity.getResultsList();
		}
		list = sortResults(list);

		results.values = list;
		results.count = list.size();

		return results;
	}

	public Point getCenterMercator() {
		return ((MemoryAdapter) this.activity.listAdapter).getCenterMercator();
	}
}
