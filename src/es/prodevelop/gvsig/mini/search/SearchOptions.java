package es.prodevelop.gvsig.mini.search;

import android.content.Context;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;

public class SearchOptions {

	String sort;

	Context context;
	Point center = new Point(0, 0);

	public SearchOptions(Context context) {
		this.context = context;
	}

	public boolean sortResults() {
		if (sort == null)
			return false;
		return (sort.compareTo(context.getResources().getString(
				R.string.sort_distance)) == 0);
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

}
