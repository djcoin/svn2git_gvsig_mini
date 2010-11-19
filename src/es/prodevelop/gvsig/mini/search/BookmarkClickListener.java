package es.prodevelop.gvsig.mini.search;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.tasks.poi.BookmarkManagerTask;

public class BookmarkClickListener extends POIItemClickContextListener {

	public BookmarkClickListener(SearchActivity activity, Drawable icon,
			int titleId) {
		super(activity, icon, titleId);
		// TODO Auto-generated constructor stub
	}

	public BookmarkClickListener(Activity activity, int iconId, int titleId) {
		super(activity, iconId, titleId);
		// TODO Auto-generated constructor stub
	}

	public void addBookmark() {
		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.remove_bookmark), activity.getResources().getDrawable(
				R.drawable.bt_poi)));
	}

	public void processBookmark(POI p) {
		try {
			BookmarkManagerTask b = new BookmarkManagerTask(p);
			if (b.removeBookmark()) {
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.bookmark_removed), Toast.LENGTH_LONG)
						.show();
				((TextWatcher) activity).onTextChanged("", 0, 0, 0);
			} else
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.bookmark_failed), Toast.LENGTH_LONG)
						.show();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
}
