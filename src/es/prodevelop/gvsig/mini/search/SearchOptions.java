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

package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;

import android.content.Context;
import es.prodevelop.android.spatialindex.poi.POIAlphabeticalQuickSort;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.impl.CollectionQuickSort;
import es.prodevelop.gvsig.mini.common.impl.PointDistanceQuickSort;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.activities.POIDetailsActivity;
import es.prodevelop.gvsig.mini.search.indexer.POIAlphabeticalIndexer;
import es.prodevelop.gvsig.mini.search.indexer.POICategoryDistanceIndexer;
import es.prodevelop.gvsig.mini.search.indexer.POICategoryDistanceQuickSort;
import es.prodevelop.gvsig.mini.search.indexer.POIDistanceIndexer;
import es.prodevelop.gvsig.mini.search.indexer.RestaurantDistanceIndexer;
import es.prodevelop.gvsig.mini.search.indexer.SortSectionIndexer;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class SearchOptions {

	public String sort = "";

	Context context;
	private Point centerMercator = new Point(0, 0);
	private Point centerLatLon = new Point(0, 0);

	private ArrayList categories = new ArrayList();
	private ArrayList subcategories = new ArrayList();

	private Indexed filteredIndexed;

	public SearchOptions(Context context) {
		this.context = context;
	}

	public boolean sortResults() {
		return (sort != null);
	}

	public SortSectionIndexer getSortIndexer(final Point centerMercator) {
		if (sort == null)
			return null;
		if (sort.compareTo(context.getResources().getString(
				R.string.sort_distance)) == 0) {
			return new POIDistanceIndexer(centerMercator);
		}

		if (sort.compareTo(context.getResources().getString(R.string.sort_name)) == 0) {
			return new POIAlphabeticalIndexer();
		}
		
		if (sort.compareTo(context.getResources().getString(R.string.rest_dist)) == 0) {
			return new RestaurantDistanceIndexer(centerMercator);
		}
		return new POICategoryDistanceIndexer(centerMercator);
	}

	public ArrayList getCategories() {
		return this.categories;
	}

	public ArrayList getSubcategories() {
		return this.subcategories;
	}

	public void addCategory(String category) {
		if (!this.categories.contains(category))
			this.categories.add(category);
	}

	public void addSubcategory(String subcategory) {
		if (!this.subcategories.contains(subcategory))
			this.subcategories.add(subcategory);
	}

	public void removeCategory(String category) {
		this.categories.remove(category);
	}

	public void removeSubcategory(String subcategory) {
		this.subcategories.remove(subcategory);
	}

	public void clearCategories() {
		this.categories.clear();
	}

	public void clearSubcategories() {
		this.subcategories.clear();
	}

	public void setCategories(ArrayList categories) {
		this.categories = categories;
	}

	public void setSubcategories(ArrayList subcategories) {
		this.subcategories = subcategories;
	}

	public Indexed getFilteredIndexed() {
		return filteredIndexed;
	}

	public void setFilteredIndexed(Indexed filteredIndexed) {
		this.filteredIndexed = filteredIndexed;
	}

	public Point getCenterMercator() {
		return centerMercator;
	}

	public void setCenterMercator(Point centerMercator) {
		this.centerMercator = centerMercator;
		double[] center = ConversionCoords.reproject(centerMercator.getX(),
				centerMercator.getY(), CRSFactory.getCRS("EPSG:900913"),
				CRSFactory.getCRS("EPSG:4326"));
		this.centerLatLon = new Point(center[0], center[1]);
	}

	public Point getCenterLatLon() {
		return centerLatLon;
	}

	public void setCenterLatLon(Point centerLatLon) {
		this.centerLatLon = centerLatLon;
		double[] centerMercator = ConversionCoords.reproject(
				centerLatLon.getX(), centerLatLon.getY(),
				CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS("EPSG:900913"));
		this.centerMercator = new Point(centerMercator[0], centerMercator[1]);
	}

}
