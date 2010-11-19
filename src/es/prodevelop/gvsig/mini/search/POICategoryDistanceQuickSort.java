package es.prodevelop.gvsig.mini.search;

import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class POICategoryDistanceQuickSort extends CollectionQuickSort {

	Point center;

	public POICategoryDistanceQuickSort(Point pointToCompareWith) {
		center = pointToCompareWith;
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

	private boolean calcDistance(Point xx, Point yy) {
		double[] lonlatXX = ConversionCoords.reproject(xx.getX(), xx.getY(),
				CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS("EPSG:900913"));

		double[] lonlatYY = ConversionCoords.reproject(yy.getX(), yy.getY(),
				CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS("EPSG:900913"));

		double ix = center.distance(lonlatXX[0], lonlatXX[1]);
		double iy = center.distance(lonlatYY[0], lonlatYY[1]);

		return (ix < iy);
	}

}
