package es.prodevelop.gvsig.mini.search;

import android.content.Context;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;

public class SearchOptions {
	String filter;
	String sort;

	Context context;
	Point center = new Point(0, 0);

	public SearchOptions(Context context) {
		this.context = context;
	}

	public boolean isPrefixSearch() {
		if (filter == null)
			return false;
		return (filter.compareTo(context.getResources().getString(
				R.string.search_prefix)) == 0);
	}

	public boolean sortResults() {
		if (sort == null)
			return false;
		return (sort.compareTo(context.getResources().getString(
				R.string.sort_distance)) == 0);
	}

}
