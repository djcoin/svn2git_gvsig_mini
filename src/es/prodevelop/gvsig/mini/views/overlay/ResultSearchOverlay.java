package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;

import es.prodevelop.android.spatialindex.quadtree.provide.FullTextSearchListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.ResultSearchSymbolSelector;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class ResultSearchOverlay extends PointOverlay implements
		FullTextSearchListener {

	public final static String DEFAULT_NAME = "RESULT_SEARCH";
	private String textSearch;
	private boolean hasConvertedCoordinates = false;

	public ResultSearchOverlay(Context context, TileRaster tileRaster,
			String name) {
		super(context, tileRaster, name);
		setVisible(false);
		setSymbolSelector(new ResultSearchSymbolSelector());
		POIProviderManager.getInstance().getPOIProvider().setListener(this);
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		if (!hasConvertedCoordinates) {
			this.convertCoordinates("EPSG:4326", getTileRaster()
					.getMRendererInfo().getSRS(), getPoints(), null);
			hasConvertedCoordinates = true;
		}
		super.onDraw(c, maps);
	}

	@Override
	public void onSearchResults(String textSearch, ArrayList list) {
		this.setPoints(list);
		this.textSearch = textSearch;
		hasConvertedCoordinates = false;
	}

	public void setVisible(boolean isVisible) {
		if (this.isVisible() && !isVisible) {
			getTileRaster().acetate.setPopupVisibility(View.INVISIBLE);
		}
		super.setVisible(isVisible);
	}
}
