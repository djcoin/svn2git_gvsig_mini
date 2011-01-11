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

import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Calculator;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class POICategoryDistanceQuickSort extends PointDistanceQuickSort {

	public POICategoryDistanceQuickSort(Point pointToCompareWith) {
		super(pointToCompareWith);
	}

	@Override
	public boolean less(Object x, Object y) {
		Point xx = (Point) x;
		Point yy = (Point) y;

		if (x instanceof OsmPOIStreet) {
			if (y instanceof OsmPOIStreet) {
				return calcDistance(xx, yy);
			} else {
				return true;
			}
		} else {
			if (y instanceof OsmPOIStreet) {
				return false;
			} else {
				OsmPOI pxx = (OsmPOI) xx;
				OsmPOI pyy = (OsmPOI) yy;
				short oxx = (Short) POICategories.CATEGORIES_ORDER.get(pxx
						.getCategory());
				short oyy = (Short) POICategories.CATEGORIES_ORDER.get(pyy
						.getCategory());
				if (oxx == oyy) {
					return calcDistance(pxx, pyy);
				} else if (oxx > oyy) {
					return false;
				} else {
					return true;
				}
			}
		}
	}
}
