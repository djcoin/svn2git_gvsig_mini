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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;



import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Message;
import android.view.MotionEvent;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.POIContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.namefinder.Named;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

/**
 * A MapOverlay that manages draw of NameFinder MultiPoint
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class NameFinderOverlay extends MapOverlay {

	private int indexPOI = -1;
	public final static String DEFAULT_NAME = "NAME_FINDER";

	public NameFinderOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		log.setClientID(this.toString());
	}

	private final static Logger log = Logger
			.getLogger(NameFinderOverlay.class.getName());

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		try {
			final ViewPort vp = maps.map.vp;
			final Extent extent = vp.calculateExtent(maps.mapWidth,
					maps.mapHeight, maps.getMRendererInfo().getCenter());
			if (maps.map.nameds != null) {
				maps.geomDrawer.drawN(maps.map.nameds, c, extent, vp);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	protected void onDrawFinished(Canvas c, TileRaster maps) {

	}

	@Override
	public Feature getNearestFeature(Pixel pixel) {
		try {
			boolean found = false;
			final NamedMultiPoint nameds = getTileRaster().map.nameds;
			if (nameds == null)
				return null;
			long distance = Long.MAX_VALUE;
			int nearest = -1;

			Named p;
			Pixel pix;
			for (int i = 0; i < nameds.getNumPoints(); i++) {
				p = (Named) nameds.getPoint(i);

				int[] coords = getTileRaster().getMRendererInfo().toPixels(
						new double[] { p.projectedCoordinates.getX(),
								p.projectedCoordinates.getY() });

//				log.log(Level.FINE, "coordinates to pixel: " + coords[0] + "," + coords[1]);
				
				pix = new Pixel(coords[0], coords[1]);
				long newDistance = pix.distance(new Pixel(pixel.getX(), pixel
						.getY()));
//				log.log(Level.FINE, "distance: " + newDistance);

				if (newDistance >= 0 && newDistance < distance) {
					distance = newDistance;
					nearest = i;
				}
			}

			if (distance > ResourceLoader.MAX_DISTANCE && distance >= 0)
				nearest = -1;

			if (nearest != -1) {
				indexPOI = nearest;
				Named n = (Named) nameds.getPoint(nearest);
				log.log(Level.FINE, "found: " + n.toString());
				return new Feature(new Point(n.projectedCoordinates.getX(),
						n.projectedCoordinates.getY()));
			} else {
				indexPOI = -1;
				return null;

			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			return null;
		}
	}

	@Override
	public ItemContext getItemContext() {
		return new POIContext((Map)getTileRaster().map);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, TileRaster osmtile) {
		try {
			super.onSingleTapUp(e, osmtile);

			if (indexPOI == -1)
				return false;
			else {
				Message m = Message.obtain();
				m.what = Map.SHOW_TOAST;
				m.obj = ((Named) getTileRaster().map.nameds.getPoint(indexPOI)).description;
				getTileRaster().map.getMapHandler().sendMessage(m);
			}
			return true;
		} catch (Exception ex) {
			log.log(Level.SEVERE,"",ex);
			return false;
		}
	}

	@Override
	public void destroy() {
		try {

		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	public void onExtentChanged(Extent newExtent, int zoomLevel,
			double resolution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLayerChanged(String layerName) {
		// TODO Auto-generated method stub
		
	}
}
