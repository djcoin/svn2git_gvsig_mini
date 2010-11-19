package es.prodevelop.gvsig.mini.tasks.poi;

import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.search.POIProviderManager;

public class BookmarkManagerTask {

	private POI p;

	public BookmarkManagerTask(POI p) {
		this.p = p;
	}

	public boolean addBookmark() {
		if (p instanceof OsmPOI)
			return POIProviderManager.getInstance().getBookmarkProvider()
					.addPOI((OsmPOI) p);
		else
			return POIProviderManager.getInstance().getBookmarkProvider()
					.addStreet((OsmPOIStreet) p);
	}

	public boolean removeBookmark() {
		if (p instanceof OsmPOI)
			return POIProviderManager.getInstance().getBookmarkProvider()
					.removePOI((OsmPOI) p);
		else
			return POIProviderManager.getInstance().getBookmarkProvider()
					.removeStreet((OsmPOIStreet) p);
	}
}
