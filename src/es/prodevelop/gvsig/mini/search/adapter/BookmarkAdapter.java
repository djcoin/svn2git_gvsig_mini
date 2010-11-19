package es.prodevelop.gvsig.mini.search.adapter;

import android.widget.Filter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.filter.BookmarkFilter;

public class BookmarkAdapter extends FilteredLazyAdapter {

	public BookmarkAdapter(SearchActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new BookmarkFilter(activity);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		// pos = -1;
		super.notifyDataSetChanged();
		activity.setTitle(activity.getResources().getString(R.string.bookmarks));
		activity.setProgressBarIndeterminateVisibility(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#notifyDataSetInvalidated()
	 */
	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		// pos = -1;
		super.notifyDataSetInvalidated();
		activity.setTitle(activity.getResources().getString(R.string.bookmarks));
		activity.setProgressBarIndeterminateVisibility(false);
	}

}
