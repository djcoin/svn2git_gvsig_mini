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

package es.prodevelop.gvsig.mini._lg;

import android.graphics.Bitmap;
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini._lg.LgPOI;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.symbol.SymbolSelector;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mini.views.overlay.BookmarkOverlay;

public class LgSymbolSelector extends SymbolSelector {

	final static private String TAG = LgSymbolSelector.class.getName();
	
	private int[] midIcon = new int[] { 0, 36 };

	private Bitmap NOTVISITED, VISITED, DEFAULT, WRONG;


	public LgSymbolSelector() {
		NOTVISITED = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_f);
		VISITED = ResourceLoader.getBitmap(R.drawable.p_health_hospital);
		WRONG = ResourceLoader.getBitmap(R.drawable.p_accommodation_hotel);
		DEFAULT = NOTVISITED;
	}

	@Override
	public Bitmap getSymbol(Point p) {
		LgPOI lgpoi;
		Bitmap bm = DEFAULT;

		if (p instanceof LgPOI){
			lgpoi = (LgPOI) p;

			switch (lgpoi.getState()) {
			case NOTVISITED:
					Log.d(TAG, "The state of POI is NOTVISITED!");
					bm = this.NOTVISITED;
				break;
			case VISITED:
					Log.d(TAG, "The state of POI is VISITED!");
					bm = this.VISITED;
				break;
			default:
				Log.d(TAG, "The state of POI could not be determine" + lgpoi.getState());
					bm = this.WRONG;
				break;
			}
		} else {
			Log.d(TAG, "Pointis NOT a LGPoi.");
		}
		return bm;
	}

	@Override
	public String getText(Point p) {
		String text = "Default description !";
		if (p instanceof LgPOI){
			LgPOI lgpoi = (LgPOI) p;
			text = lgpoi.getDescription();
		}
	
		return Utilities.capitalizeFirstLetters(text);
	}

	@Override
	public int[] getMidSymbol(Point p) {
		return midIcon;
	}

}
