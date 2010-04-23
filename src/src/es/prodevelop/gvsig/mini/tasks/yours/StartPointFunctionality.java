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
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.tasks.yours;

import android.os.Message;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.renderer.MapRenderer;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;

/**
 * Sets the start point of the route
 * @author aromeu 
 * @author rblanco
 *
 */
public class StartPointFunctionality extends Functionality {

	private final static Logger log = LoggerFactory
			.getLogger(StartPointFunctionality.class);

	public StartPointFunctionality(Map map, int id) {
		super(map, id);
		RoutePointHandler handler = new RoutePointHandler(map);
		this.addObserver(handler);
	}

	@Override
	public boolean execute() {
		try {
			MapRenderer mapRenderer = getMap().osmap.getMRendererInfo();
			double[] p = mapRenderer.transformCenter("EPSG:4326");
			Point startPoint = new Point(p[0], p[1]);			
			getMap().route.setStartPoint(startPoint);
		} catch (Exception e) {
			log.error("StartPointFunc execute: ", e);
		} finally {
//			super.stop();
		}
		return true;
	}

	@Override
	public int getMessage() {
		// TODO Auto-generated method stub
		return TaskHandler.FINISHED;
	}
}
