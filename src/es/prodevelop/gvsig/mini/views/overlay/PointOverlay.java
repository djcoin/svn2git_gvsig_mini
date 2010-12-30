package es.prodevelop.gvsig.mini.views.overlay;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.GPSItemContext;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.symbol.SymbolSelector;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class PointOverlay extends MapOverlay {

	private SymbolSelector selector;
	private ArrayList points;
	private MapRenderer renderer;

	public PointOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		renderer = tileRaster.getMRendererInfo();
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemContext getItemContext() {
		return new GPSItemContext(getTileRaster().map);
	}

	protected void convertCoordinates(final String srsFrom, final String srsTo,
			final ArrayList pois, final Cancellable cancellable) {
		if (pois == null)
			return;
		final int size = pois.size();
		Point p;
		Point temp;
		for (int i = 0; i < size; i++) {
			// if (cancellable != null && cancellable.getCanceled())
			// return;
			p = (Point) pois.get(i);
			final double[] xy = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS(srsFrom), CRSFactory.getCRS(srsTo));
			temp = (Point) p.clone();
			temp.setX(xy[0]);
			temp.setY(xy[1]);
			pois.set(i, temp);
		}
	}

	protected void draw(final ArrayList pois, final MapRenderer renderer,
			final Canvas c) {
		if (pois != null) {
			final int size = pois.size();

			for (int i = 0; i < size; i++) {
				POI p = (POI) pois.get(i);
				draw(p, renderer, c);
			}
		}
	}

	protected void draw(final Point p, final MapRenderer renderer,
			final Canvas c) {
		try {
			final Extent viewExtent = getTileRaster().getMRendererInfo()
					.getCurrentExtent();

			if (!viewExtent.contains(p.getX(), p.getY()))
				return;
			int[] coords = renderer
					.toPixels(new double[] { p.getX(), p.getY() });

			Bitmap icon = getSymbolSelector().getSymbol(p);
			int[] midIcon = getSymbolSelector().getMidSymbol();

			if (icon != null)
				c.drawBitmap(icon, coords[0] - midIcon[0], coords[1]
						- midIcon[1], Paints.mPaintR);
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	@Override
	public void onExtentChanged(Extent newExtent, int zoomLevel,
			double resolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLayerChanged(String layerName) {
		if (getPoints() == null)
			return;
		MapRenderer newRenderer;
		try {
			newRenderer = Layers.getInstance().getRenderer(layerName);
			convertCoordinates(renderer.getSRS(), newRenderer.getSRS(),
					getPoints(), null);
			renderer = newRenderer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("", e.getMessage());
		}
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		if (isVisible())
			draw(getPoints(), maps.getMRendererInfo(), c);
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
		try {
			setPoints(null);
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	public SymbolSelector getSymbolSelector() {
		return selector;
	}

	public void setSymbolSelector(SymbolSelector selector) {
		this.selector = selector;
	}

	public ArrayList getPoints() {
		return points;
	}

	public void setPoints(ArrayList points) {
		this.points = points;
	}

}
