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

import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class RestaurantDistanceIndexer extends BaseIndexer {

	RestaurantInfoDistanceQuickSort qs;

	public RestaurantDistanceIndexer(Point centerMercator) {
		qs = new RestaurantInfoDistanceQuickSort(centerMercator);
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
		String currentSection = "*";

		POI p;
		ArrayList ordered = new ArrayList();
		int counter = 0;
		String temp;
		for (int i = 0; i < size; i++) {
			p = (POI) sorted[i];
			String info = "";
			if (p instanceof OsmPOI) {
				info = ((OsmPOI) p).getInfo();
			}

			if (info == null)
				info = "";

			if (info.compareTo(currentSection) != 0) {
				temp = Utilities.capitalizeFirstLetters(info);
				indexer.put(temp, length);
				currentSection = info;
				sectionsPos.add(length);
				sections.add(temp);
				counter++;
			}

			length++;
			ordered.add(sorted[i]);
		}

		final int l = sectionsPos.size();
		this.sections = new String[l];
		this.sectionsPos = new int[l];
		for (int i = 0; i < l; i++) {
			this.sections[i] = (String) sections.get(i);
			this.sectionsPos[i] = (Integer) sectionsPos.get(i);
		}
		totalLength = length;

		return ordered;
	}

	private class RestaurantInfoDistanceQuickSort extends CollectionQuickSort {

		Point center;

		public RestaurantInfoDistanceQuickSort(Point center) {
			this.center = center;
		}

		public boolean less(Object x, Object y) {
			String info = ((OsmPOI) x).getInfo();
			if (info == null)
				info = "_";
			if (info.length() == 0)info = "_";
			String xx = info.toLowerCase();

			info = ((OsmPOI) y).getInfo();
			if (info == null)
				info = "_";
			if (info.length() == 0)info = "_";

			String yy = info.toLowerCase();

			char ix = xx.charAt(0);
			char iy = yy.charAt(0);

			int length = Math.min(xx.length(), yy.length());

			for (int i = 1; i < length; i++) {
				if (ix == iy) {
					ix = xx.charAt(i);
					iy = yy.charAt(i);
				} else {
					return (ix < iy);
				}
			}
			return calcDistance((Point) x, (Point) y);
		}

		private boolean calcDistance(Point xx, Point yy) {
			double[] lonlatXX = ConversionCoords.reproject(xx.getX(),
					xx.getY(), CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS("EPSG:900913"));

			double[] lonlatYY = ConversionCoords.reproject(yy.getX(),
					yy.getY(), CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS("EPSG:900913"));

			double ix = center.distance(lonlatXX[0], lonlatXX[1]);
			double iy = center.distance(lonlatYY[0], lonlatYY[1]);

			return (ix < iy);
		}
	}
}
