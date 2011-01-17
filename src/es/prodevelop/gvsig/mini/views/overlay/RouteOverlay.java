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
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.FeatureCollection;
import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.yours.RouteManager;

/**
 * A Map Overlay that manages draw of Route service LineString
 * @author aromeu 
 * @author rblanco
 *
 */
public class RouteOverlay extends MapOverlay {

	public final static String DEFAULT_NAME = "YOURS";
	
	public RouteOverlay(Context context, TileRaster tileRaster, String name) {
		super(context, tileRaster, name);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		log.setClientID(this.toString());
		// TODO Auto-generated constructor stub
	}

	protected final android.graphics.Point CIRCLE_SPOT = new android.graphics.Point(
			7, 7);	
	private final static Logger log = Logger
			.getLogger(RouteOverlay.class.getName());	

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		try {
			FeatureCollection r = RouteManager.getInstance().getRegisteredRoute().getRoute();
			final ViewPort vp = maps.map.vp;
			final Extent extent = vp.calculateExtent(maps.mapWidth,
					maps.mapHeight, maps.getMRendererInfo().getCenter());
			if (r != null && r.getSize() > 0) {
				Feature f = r.getFeatureAt(0);
				LineString l = (LineString) f.getGeometry();

				if (l != null)
					maps.geomDrawer.draw(l, c, extent, vp);
			}

			es.prodevelop.gvsig.mini.geom.Point m = RouteManager.getInstance().getRegisteredRoute()
					.getStartPoint();

			if (m != null)
				maps.geomDrawer.drawstart(m, c, extent, vp);

			es.prodevelop.gvsig.mini.geom.Point m1 = RouteManager.getInstance().getRegisteredRoute()
					.getEndPoint();

			if (m1 != null)
				maps.geomDrawer.drawend(m1, c, extent, vp);
			// c.drawBitmap(CIRCLE, maps.centerPixelX - CIRCLE_SPOT.x,
			// maps.centerPixelY - CIRCLE_SPOT.y, maps.mPaint);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
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
	public ItemContext getItemContext() {
		// TODO Auto-generated method stub
		return null;
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
