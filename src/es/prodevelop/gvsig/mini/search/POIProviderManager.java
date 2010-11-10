package es.prodevelop.gvsig.mini.search;

import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;

public class POIProviderManager {

	private static POIProviderManager instance;
	private PerstOsmPOIClusterProvider poiProvider;

	public static POIProviderManager getInstance() {
		if (instance == null)
			instance = new POIProviderManager();
		return instance;
	}

	public void registerPOIProvider(PerstOsmPOIClusterProvider provider) {
		this.poiProvider = provider;
	}

	public PerstOsmPOIClusterProvider getPOIProvider() {
		return this.poiProvider;
	}
}
