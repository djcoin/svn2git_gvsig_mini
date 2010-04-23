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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.map.POIContext;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.namefinder.Named;
import es.prodevelop.gvsig.mini.namefinder.NamedMultiPoint;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A MapOverlay that manages draw of NameFinder MultiPoint
 * @author aromeu 
 * @author rblanco
 *
 */
public class NameFinderOverlay extends MapOverlay {
	
	private int indexPOI = -1;
	
	public NameFinderOverlay(Context context, TileRaster tileRaster) {
		super(context, tileRaster);
		log.setClientID(this.toString());
	}

	private final static Logger log = LoggerFactory.getLogger(NameFinderOverlay.class);

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		try {
			final ViewPort vp = maps.map.vp;
			final Extent extent = vp.calculateExtent(maps.mapWidth, maps.mapHeight, maps.getMRendererInfo().getCenter());
			if (maps.map.nameds != null) {
				maps.geomDrawer.drawN(maps.map.nameds, c, extent, vp);
			}
		} catch (Exception e) {
			log.error(e);
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
			int distance = 99999;
			int nearest = -1;

			Named p;
			Pixel pix;
			for (int i = 0; i < nameds.getNumPoints(); i++) {
				p = (Named) nameds.getPoint(i);

				int[] coords = getTileRaster().getMRendererInfo().toPixels(
						new double[] { p.projectedCoordinates.getX(),
								p.projectedCoordinates.getY() });

				pix = new Pixel(coords[0], coords[1]);
				int newDistance = pix.distance(new Pixel(pixel.getX(), pixel.getY()));

				if (newDistance < distance) {
					distance = newDistance;
					nearest = i;
				}
			}

			if (distance > ResourceLoader.MAX_DISTANCE)
				nearest = -1;

			if (nearest != -1) {
				indexPOI = nearest;
				return new Feature((Point) nameds.getPoint(nearest).clone());				
			} else {
				indexPOI = -1;
				return null;
				
			}			
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public ItemContext getItemContext() {		
		return new POIContext(getTileRaster().map);
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
				m.obj = ((Named)getTileRaster().map.nameds.getPoint(indexPOI)).description;
				getTileRaster().map.getMapHandler().sendMessage(m);
			}
			return true;
		} catch (Exception ex) {
			log.error(ex);
			return false;
		}
	}
	
	@Override
	public void destroy() {
		try {
			
		} catch (Exception e) {
			log.error(e);
		}
	}
}
