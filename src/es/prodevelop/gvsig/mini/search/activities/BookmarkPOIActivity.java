package es.prodevelop.gvsig.mini.search.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.BookmarkClickListener;
import es.prodevelop.gvsig.mini.search.POIItemClickContextListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.adapter.BookmarkAdapter;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;

public class BookmarkPOIActivity extends ResultSearchActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listener = new BookmarkClickListener(this, R.drawable.pois,
				R.string.NameFinderActivity_0);
	}

	@Override
	public void initializeAdapters() {
		if (POIProviderManager.getInstance().getPOIProvider() == null)
			try {
				POIProviderManager.getInstance()
						.registerPOIProvider(
								new PerstOsmPOIClusterProvider("/sdcard/"
										+ "perst_streets_cluster_cat.db", 18,
										null, 18));
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		listAdapter = new BookmarkAdapter(this);
		filteredListAdapter = new BookmarkAdapter(this);
	}

	public QuadtreeProvider getProvider() {
		return POIProviderManager.getInstance().getBookmarkProvider();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			InvokeIntents.launchListBookmarks(this, new double[] {
					this.getCenter().getX(), this.getCenter().getY() });

			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
