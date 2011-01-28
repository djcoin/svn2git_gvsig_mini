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
import es.prodevelop.gvsig.mini.views.overlay.PerstClusterPOIOverlay;

public class ResultSearchSymbolSelector extends SymbolSelector {

	private int[] midIcon = new int[] { 0, 28 };

	private Bitmap TRANSPORTATION;
	private Bitmap TOURISM;
	private Bitmap RECREATION;
	private Bitmap FOOD;
	private Bitmap PUBLIC_BUILDINGS;
	private Bitmap ARTS_CULTURE;
	private Bitmap SHOPS;
	private Bitmap HEALTH_EMERGENCY;
	private Bitmap ACCOMODATION;
	private Bitmap ROUTE;
	private Bitmap PLACES;

	public ResultSearchSymbolSelector() {

		TRANSPORTATION = ResourceLoader
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_16s);
		TOURISM = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_16s);
		RECREATION = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_16s);
		FOOD = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_16s);
		PUBLIC_BUILDINGS = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_16s);
		ARTS_CULTURE = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_16s);
		SHOPS = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_16s);
		HEALTH_EMERGENCY = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_16s);
		ACCOMODATION = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_16s);
		ROUTE = ResourceLoader
				.getBitmap(R.drawable.p_route_tourist_castle2_16s);
		PLACES = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_16s);
		
		if (PLACES != null) {
			midIcon[1] = PLACES.getHeight();
		}
	}

	@Override
	public Bitmap getSymbol(Point point) {

		Bitmap icon = null;

		if (point instanceof OsmPOIStreet) {
			icon = PLACES;
		} else {
			OsmPOI p = (OsmPOI) point;
			final String cat = p.getCategory();
			if (cat.compareTo(POICategories.TRANSPORTATION) == 0)
				icon = this.TRANSPORTATION;
			else if (cat.compareTo(POICategories.TOURISM) == 0)
				icon = this.TOURISM;
			else if (cat.compareTo(POICategories.RECREATION) == 0)
				icon = this.RECREATION;
			else if (cat.compareTo(POICategories.FOOD) == 0)
				icon = this.FOOD;
			else if (cat.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
				icon = this.PUBLIC_BUILDINGS;
			else if (cat.compareTo(POICategories.ARTS_CULTURE) == 0)
				icon = this.ARTS_CULTURE;
			else if (cat.compareTo(POICategories.SHOPS) == 0)
				icon = this.SHOPS;
			else if (cat.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
				icon = this.HEALTH_EMERGENCY;
			else if (cat.compareTo(POICategories.ACCOMODATION) == 0)
				icon = this.ACCOMODATION;
			else if (cat.compareTo(POICategories.ROUTE) == 0)
				icon = this.ROUTE;
			else if (cat.compareTo(POICategories.PLACES) == 0)
				icon = this.PLACES;
		}

		return icon;
	}

	@Override
	public String getText(Point p) {
		String text = "";
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
	
	@Override
	public int[] getMidPopup(Point p) {
		int[] midPopup = new int[2];
		if (PLACES != null) {
			midPopup[0] = PLACES.getWidth() / 2;
			midPopup[1] = PLACES.getHeight() / 2;
		} else {
			midPopup = midIcon;
		}
		
		return midPopup;
	}
}
