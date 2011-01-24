/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2009 Prodevelop.
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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Pequeña y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Rubén Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm.views.overlay;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */

package es.prodevelop.gvsig.mini.views.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import es.prodevelop.gvsig.mini.context.Contextable;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.map.ExtentChangedListener;
import es.prodevelop.gvsig.mini.map.GeoUtils;
import es.prodevelop.gvsig.mini.map.LayerChangedListener;
import es.prodevelop.gvsig.mini.map.ViewPort;

/**
 * A very very simple abstraction of a GIS Layer
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public abstract class MapOverlay implements GeoUtils, Contextable,
		ExtentChangedListener, LayerChangedListener {

	private TileRaster tileRaster;
	private Context context;

	private String name = "";
	private boolean isVisible = true;

	/**
	 * The constructor
	 * 
	 * @param context
	 * @param tileRaster
	 */
	public MapOverlay(final Context context, final TileRaster tileRaster,
			String name) {
		this.context = context;
		this.tileRaster = tileRaster;
		this.name = name;
	}

	/**
	 * Called when drawing the TileRaster view
	 * 
	 * @param c
	 *            The canvas to draw in
	 * @param maps
	 *            The TileRaster instance
	 */
	public void onManagedDraw(final Canvas c, final TileRaster maps) {
		if (!isVisible())
			return;
		onDraw(c, maps);
		onDrawFinished(c, maps);
	}

	/**
	 * Draws the MapOVerlay
	 * 
	 * @param c
	 *            The canvas to draw in
	 * @param maps
	 *            The TileRaster instance
	 */
	protected abstract void onDraw(final Canvas c, final TileRaster maps);

	/**
	 * Actions to do after onDraw
	 * 
	 * @param c
	 *            The canvas to draw in
	 * @param maps
	 *            The TileRaster instance
	 */
	protected abstract void onDrawFinished(final Canvas c, final TileRaster maps);

	public boolean onKeyDown(final int keyCode, KeyEvent event,
			final TileRaster mapView) {
		return false;
	}

	public boolean onKeyUp(final int keyCode, KeyEvent event,
			final TileRaster mapView) {
		return false;
	}

	public boolean onTouchEvent(final MotionEvent event,
			final TileRaster mapView) {
		return false;
	}

	public boolean onTrackballEvent(final MotionEvent event,
			final TileRaster mapView) {
		return false;
	}

	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		return onLongPress(e, osmtile);
	}

	/**
	 * Retrieves the nerest feature from the pressed pixel and pass them to
	 * TileRaster
	 * 
	 * @param e
	 * @param osmtile
	 * @return
	 */
	public boolean onLongPress(MotionEvent e, TileRaster osmtile) {
		try {
			Pixel pixel = new Pixel((int) e.getX(), (int) e.getY());
			Feature f = getNearestFeature(pixel);
			if (f != null) {
				this.getTileRaster().setSelectedFeature(f);
				return true;
			} else {
				// getTileRaster().map.getPopup().setVisibility(View.INVISIBLE);
				this.getTileRaster().setSelectedFeature(null);
				return false;
			}
		} catch (Exception ex) {
			return false;
		}

	}

	/**
	 * Returns the nearest Feature in the MapOverlay to a pixel x-y
	 * 
	 * @param pixel
	 * @return
	 */
	public abstract Feature getNearestFeature(Pixel pixel);

	public Context getContext() {
		return context;
	}

	public TileRaster getTileRaster() {
		return tileRaster;
	}

	/**
	 * Used to free memory
	 */
	public void destroy() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
