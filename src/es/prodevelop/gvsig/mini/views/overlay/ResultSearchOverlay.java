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

import java.util.ArrayList;

import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.quadtree.provide.FullTextSearchListener;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.context.ItemContext;
import es.prodevelop.gvsig.mini.context.osmpoi.OSMPOIContext;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.symbol.ResultSearchSymbolSelector;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class ResultSearchOverlay extends PointOverlay implements
		FullTextSearchListener {

	public final static String DEFAULT_NAME = "RESULT_SEARCH";
	private String textSearch;
	private boolean hasConvertedCoordinates = false;

	public ResultSearchOverlay(Context context, TileRaster tileRaster,
			String name) {
		super(context, tileRaster, name);
		setVisible(false);
		setSymbolSelector(new ResultSearchSymbolSelector());		
	}

	@Override
	protected void onDraw(Canvas c, TileRaster maps) {
		if (!hasConvertedCoordinates) {
			this.convertCoordinates("EPSG:4326", getTileRaster()
					.getMRendererInfo().getSRS(), getPoints(), null);
			hasConvertedCoordinates = true;
		}
		super.onDraw(c, maps);
	}

	@Override
	public void onSearchResults(String textSearch, ArrayList list) {
		this.setPoints(list);
		this.textSearch = textSearch;
		hasConvertedCoordinates = false;
	}

	public void setVisible(boolean isVisible) {
		if (this.isVisible() && !isVisible) {
			getTileRaster().acetate.setPopupVisibility(View.INVISIBLE);
		}
		super.setVisible(isVisible);
	}
	
	public String getQuery() {
		return this.textSearch;
	}
	
	@Override
	public ItemContext getItemContext() {
		try {
			return new OSMPOIContext((Map) getTileRaster().map, false, true,
					(POI) getPoints().get(getSelectedIndex()));
		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
			return null;
		}
	}
}
