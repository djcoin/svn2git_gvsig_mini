package es.prodevelop.gvsig.mini.search;

import java.util.ArrayList;

import android.content.Context;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;

public class SearchOptions {

	String sort;

	Context context;
	Point center = new Point(0, 0);

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

}
