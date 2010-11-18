package es.prodevelop.gvsig.mini.search.activities;

import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.adapter.MemoryAdapter;
import es.prodevelop.gvsig.mini.search.filter.MemoryFilter;

public class FindPOISNearActivity extends ResultSearchActivity {

	protected final static int DEFAULT_DISTANCE = 500;

	@Override
	public void initializeAdapters() {
		listAdapter = new MemoryAdapter(this, MemoryFilter.POIS,
				DEFAULT_DISTANCE, Point.parseString(query));
		filteredListAdapter = new MemoryAdapter(this, MemoryFilter.POIS,
				DEFAULT_DISTANCE, Point.parseString(query));
	}

}
