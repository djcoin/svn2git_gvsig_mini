package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;

import android.content.Context;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class SearchOptions {

	public String sort;

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
		if (sort == null)
			return false;
		return (sort.compareTo(context.getResources().getString(
				R.string.sort_distance)) == 0 || sort.compareTo(context
				.getResources().getString(R.string.sort_name)) == 0);
	}

	public boolean isSortByDistance() {
		return (sort.compareTo(context.getResources().getString(
				R.string.sort_distance)) == 0);
	}

	public boolean isSortByName() {
		return (sort.compareTo(context.getResources().getString(
				R.string.sort_name)) == 0);
	}

	public boolean isNoSort() {
		return (sort.compareTo(context.getResources().getString(R.string.no)) == 0);
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
