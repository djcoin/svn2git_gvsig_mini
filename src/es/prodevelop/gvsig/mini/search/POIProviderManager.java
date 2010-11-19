package es.prodevelop.gvsig.mini.search;

import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstBookmarkProvider;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;

public class POIProviderManager {

	private static POIProviderManager instance;
	private PerstOsmPOIClusterProvider poiProvider;

	private PerstBookmarkProvider bookmarkProvider;

	public static POIProviderManager getInstance() {
		if (instance == null)
			instance = new POIProviderManager();
		return instance;
	}

	public void registerPOIProvider(PerstOsmPOIClusterProvider provider) {
		if (poiProvider != null)
			unregisterPOIProvider();
		this.poiProvider = provider;
		registerBookmarkPOIProvider(new PerstBookmarkProvider(
				provider.getDatabasePath() + "_fav"));
	}

	public PerstOsmPOIClusterProvider getPOIProvider() {
		return this.poiProvider;
	}

	public void registerBookmarkPOIProvider(PerstBookmarkProvider provider) {
		if (bookmarkProvider != null)
			unregisterBookmarkPOIProvider();
		this.bookmarkProvider = provider;
	}

	public PerstBookmarkProvider getBookmarkProvider() {
		return this.bookmarkProvider;
	}

	public void unregisterPOIProvider() {
		this.poiProvider.getHelper().close();
		this.poiProvider = null;
	}

	public void unregisterBookmarkPOIProvider() {
		this.bookmarkProvider.getHelper().close();
		this.bookmarkProvider = null;
	}
}
