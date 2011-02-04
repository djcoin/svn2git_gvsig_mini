/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2011 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.GPSItemContext;
import es.prodevelop.gvsig.mini.context.map.RoutePOIContext;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.symbol.SymbolSelector;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.MapRenderer;

public abstract class PointOverlay extends MapOverlay {

	private SymbolSelector selector;
	private ArrayList points;
	private MapRenderer renderer;

	private int selectedIndex = -1;

	public PointOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		renderer = tileRaster.getMRendererInfo();
		// TODO Auto-generated constructor stub
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
				Point p = (Point) pois.get(i);
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
			int[] midIcon = getSymbolSelector().getMidSymbol(p);

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

		if (getPoints() == null || layerName.length() <= 0)
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
		try {
			if (isVisible())
				draw(getPoints(), maps.getMRendererInfo(), c);
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {
		// TODO Auto-generated method stub

	}

	public int findNearestIndexToPixel(final Pixel pixel, final ArrayList points) {
		if (points == null || !isVisible())
			return -1;
		long distance = Long.MAX_VALUE;
		int nearest = -1;

		Point p = null;
		Pixel pix;
		final int size = points.size();
		int[] offset;
		int[] coords;
		for (int i = 0; i < size; i++) {
			p = (Point) points.get(i);
			offset = getSymbolSelector().getMidPopup(p);
			coords = getTileRaster().getMRendererInfo().toPixels(
					new double[] { p.getX(), p.getY() });

			// log.log(Level.FINE, "coordinates to pixel: " + coords[0] +
			// "," + coords[1]);

			pix = new Pixel(coords[0], coords[1]);

			// pix.setX(pix.getX() - offset[0]);
			// pix.setY(pix.getY() - offset[1]);
			long newDistance = pix.distance(new Pixel(pixel.getX(), pixel
					.getY()));
			// log.log(Level.FINE, "distance: " + newDistance);

			if (newDistance >= 0 && newDistance < distance) {
				distance = newDistance;
				nearest = i;
			}
		}

		int maxDistance = ResourceLoader.MAX_DISTANCE;
		if (p != null)
			maxDistance = getSymbolSelector().getMaxTouchDistance(p);

		if (distance > maxDistance && distance >= 0)
			nearest = -1;

		return nearest;
	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		try {
			if (!isVisible())
				return null;
			boolean found = false;
			final ArrayList pois = this.getPoints();

			int nearest = findNearestIndexToPixel(pixel, pois);

			if (nearest != -1) {
				setSelectedIndex(nearest);
				Point selected = (Point) pois.get(nearest);
				return new Feature(selected);
			} else {
				setSelectedIndex(-1);
				return null;

			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void destroy() {
		try {
			super.destroy();
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

	public void updatePopup(Point p) {
		if (p == null) {
			// getTileRaster().acetate.setPopupVisibility(View.INVISIBLE);
			return;
		}

		String text = getSymbolSelector().getText(p);
		int[] coords = getTileRaster().getMRendererInfo().toPixels(
				new double[] { p.getX(), p.getY() });
		getTileRaster().acetate.popupOffsetX = getSymbolSelector().getMidPopup(
				p)[0];
		getTileRaster().acetate.popupOffsetY = getSymbolSelector().getMidPopup(
				p)[1];
		getTileRaster().acetate.setPopupPos(coords[0]
				- getSymbolSelector().getMidPopup(p)[0], coords[1]
				- getSymbolSelector().getMidPopup(p)[1]);
		getTileRaster().acetate.setPopupText(text);
		getTileRaster().acetate.setPopupVisibility(View.VISIBLE);
		// getTileRaster().acetate.popupOffsetX =
		// getSymbolSelector().getSymbol(p)
		// .getWidth() / 2;
		getTileRaster().acetate.popupOffsetY = getSymbolSelector().getSymbol(p)
				.getHeight();
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		try {
			if (!isVisible())
				return false;
			super.onSingleTapUp(e, osmtile);

			if (getSelectedIndex() == -1) {
				return false;
			} else {
				Point p = (Point) getPoints().get(getSelectedIndex());
				updatePopup(p);
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

}
