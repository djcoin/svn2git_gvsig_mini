package es.prodevelop.gvsig.mini.views.overlay.factory;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import es.prodevelop.gvsig.mini.context.Contextable;
import es.prodevelop.gvsig.mini.map.ExtentChangedListener;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.map.LayerChangedListener;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;

public interface IMapOverlay extends GeoUtils, Contextable, ExtentChangedListener, LayerChangedListener {

	String getName();

	void destroy();

	boolean onLongPress(MotionEvent e, TileRaster tileRaster);

	boolean onKeyDown(int keyCode, KeyEvent event, TileRaster tileRaster);

	boolean onKeyUp(int keyCode, KeyEvent event, TileRaster tileRaster);

	boolean onTrackballEvent(MotionEvent event, TileRaster tileRaster);

	boolean onTouchEvent(MotionEvent event, TileRaster tileRaster);

	void onManagedDraw(Canvas c, TileRaster tileRaster);

	boolean onSingleTapUp(MotionEvent e, TileRaster tileRaster);

	void setVisible(boolean b);

}
