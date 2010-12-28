package es.prodevelop.gvsig.mini.views.overlay;

import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import android.content.Context;
import android.graphics.Canvas;

public class BookmarkOverlay extends MapOverlay {

	public BookmarkOverlay(Context context, TileRaster tileRaster) {
		super(context, tileRaster);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemContext getItemContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
