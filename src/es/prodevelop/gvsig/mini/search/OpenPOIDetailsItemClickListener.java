package es.prodevelop.gvsig.mini.search;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.gvsig.mini.search.activities.POIDetailsActivity;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.FilteredLazyAdapter;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;

public class OpenPOIDetailsItemClickListener implements OnItemClickListener {

	private SearchActivity activity;

	public OpenPOIDetailsItemClickListener(SearchActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0.getAdapter() instanceof FilteredLazyAdapter) {
			final FilteredLazyAdapter adapter = (FilteredLazyAdapter) arg0
					.getAdapter();

			Intent i = new Intent(activity, POIDetailsActivity.class);
			Object p = adapter.getItem(arg2);
			if (p != null && p instanceof OsmPOI) {
				OsmPOI poi = (OsmPOI) p;
				InvokeIntents.fillIntentPOIDetails(poi, activity.getCenter(),
						i, activity);

				activity.startActivity(i);

			}
		}
	}
}
