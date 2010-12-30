package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;

import es.prodevelop.android.spatialindex.quadtree.provide.FullTextSearchListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.ResultSearchSymbolSelector;
import android.content.Context;

public class ResultSearchOverlay extends PointOverlay implements
		FullTextSearchListener {

	public final static String DEFAULT_NAME = "RESULT_SEARCH";
	private String textSearch;

	public ResultSearchOverlay(Context context, TileRaster tileRaster,
			String name) {
		super(context, tileRaster, name);
		setVisible(false);
		setSymbolSelector(new ResultSearchSymbolSelector());
		POIProviderManager.getInstance().getPOIProvider().setListener(this);
	}

	@Override
	public void onSearchResults(String textSearch, ArrayList list) {
		this.setPoints(list);
		this.convertCoordinates("EPSG:4326", getTileRaster().getMRendererInfo()
				.getSRS(), getPoints(), null);
		this.textSearch = textSearch;
	}
}
