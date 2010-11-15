package es.prodevelop.gvsig.mini.search;

import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import android.widget.Filter;

public class MultiKeywordFilteredAdapter extends FilteredLazyAdapter {

	public MultiKeywordFilteredAdapter(SearchActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new KeywordFilter(activity);
		}
		return mFilter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (activity.getResultsList() != null)
			return activity.getResultsList().size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (activity.getResultsList() != null) {
			return activity.getResultsList().get(arg0);
		}

		return null;
	}

	@Override
	public long getItemId(int arg0) {
		if (activity.getResultsList() != null)
			return ((POI) activity.getResultsList().get(arg0)).getId();
		return -1;
	}

}
