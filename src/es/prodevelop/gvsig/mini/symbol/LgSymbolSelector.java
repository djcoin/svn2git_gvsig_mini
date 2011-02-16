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

package es.prodevelop.gvsig.mini.symbol;

import android.graphics.Bitmap;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mini.views.overlay.BookmarkOverlay;

public class LgSymbolSelector extends SymbolSelector {

	private int[] midIcon = new int[] { 0, 36 };

	private Bitmap DEFAULT;

	public LgSymbolSelector() {
		DEFAULT = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_f);
	}

	@Override
	public Bitmap getSymbol(Point point) {
		return DEFAULT;
	}

	@Override
	public String getText(Point p) {
		String text = "Default description !";
		if (p instanceof OsmPOI)
			text = ((OsmPOI) p).getDescription();
		else if (p instanceof OsmPOIStreet)
			text = ((OsmPOIStreet) p).getDescription();		
		return Utilities.capitalizeFirstLetters(text);
	}

	@Override
	public int[] getMidSymbol(Point p) {
		return midIcon;
	}

}
