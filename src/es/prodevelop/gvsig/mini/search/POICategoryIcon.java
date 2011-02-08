/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 */

package es.prodevelop.gvsig.mini.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

public class POICategoryIcon {

	private final static int DEFAULT_ICON_ID = R.drawable.pois;

	public static Bitmap getBitmap16ForCategory(String category) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_accommodation_hotel);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_arts_culture_tourist_theatre);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_food_restaurant);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_health_hospital);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_public_buildings_tourist_monument);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_recreation_sport_playground);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_route_tourist_castle2);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_shops_shopping_supermarket);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_tourism_tourist_attraction);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_transportation_transport_bus_stop);
		else
			return ResourceLoader.getBitmap(DEFAULT_ICON_ID);
	}

	public static Drawable getDrawable16ForCategory(String category,
			Context context) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_accommodation_hotel);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_arts_culture_tourist_theatre);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_food_restaurant);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_health_hospital);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_public_buildings_tourist_monument);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_recreation_sport_playground);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_route_tourist_castle2);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_shops_shopping_supermarket);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_tourism_tourist_attraction);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_transportation_transport_bus_stop);
		else
			return context.getResources().getDrawable(DEFAULT_ICON_ID);
	}

	public static Bitmap getBitmap32ForCategory(String category) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_accommodation_hotel_32);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_arts_culture_tourist_theatre_32);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_food_restaurant_32);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_health_hospital_32);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_public_buildings_tourist_monument_32);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_recreation_sport_playground_32);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_route_tourist_castle2_32);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_shops_shopping_supermarket_32);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_tourism_tourist_attraction_32);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_transportation_transport_bus_stop_32);
		else
			return ResourceLoader.getBitmap(DEFAULT_ICON_ID);
	}

	public static Drawable getDrawable32ForCategory(String category,
			Context context) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_accommodation_hotel_32);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_arts_culture_tourist_theatre_32);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_food_restaurant_32);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_health_hospital_32);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_public_buildings_tourist_monument_32);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_recreation_sport_playground_32);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_route_tourist_castle2_32);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_shops_shopping_supermarket_32);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_tourism_tourist_attraction_32);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_transportation_transport_bus_stop_32);
		else
			return context.getResources().getDrawable(DEFAULT_ICON_ID);
	}

	public static Bitmap getBitmapFavForCategory(String category) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_accommodation_hotel_f);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_arts_culture_tourist_theatre_f);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_food_restaurant_f);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_health_hospital_f);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_f);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_public_buildings_tourist_monument_f);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_recreation_sport_playground_f);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_route_tourist_castle2_f);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_shops_shopping_supermarket_f);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_f);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_tourism_tourist_attraction_f);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_transportation_transport_bus_stop_f);
		else
			return ResourceLoader.getBitmap(DEFAULT_ICON_ID);
	}

	public static Drawable getDrawableFavForCategory(String category,
			Context context) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_accommodation_hotel_f);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_arts_culture_tourist_theatre_f);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_food_restaurant_f);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_health_hospital_f);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city_f);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_public_buildings_tourist_monument_f);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_recreation_sport_playground_f);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_route_tourist_castle2_f);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_shops_shopping_supermarket_f);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city_f);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_tourism_tourist_attraction_f);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_transportation_transport_bus_stop_f);
		else
			return context.getResources().getDrawable(DEFAULT_ICON_ID);
	}

	public static Bitmap getBitmapSearchCategory(String category) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_accommodation_hotel_s);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_arts_culture_tourist_theatre_s);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_food_restaurant_s);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_health_hospital_s);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_s);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_public_buildings_tourist_monument_s);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_recreation_sport_playground_s);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_route_tourist_castle2_s);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_shops_shopping_supermarket_s);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_places_poi_place_city_s);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_tourism_tourist_attraction_s);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return ResourceLoader
					.getBitmap(R.drawable.p_transportation_transport_bus_stop_s);
		else
			return ResourceLoader.getBitmap(DEFAULT_ICON_ID);
	}

	public static Drawable getDrawableSearchForCategory(String category,
			Context context) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_accommodation_hotel_s);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_arts_culture_tourist_theatre_s);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_food_restaurant_s);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_health_hospital_s);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city_s);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_public_buildings_tourist_monument_s);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_recreation_sport_playground_s);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_route_tourist_castle2_s);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_shops_shopping_supermarket_s);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_places_poi_place_city_s);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_tourism_tourist_attraction_s);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return context.getResources().getDrawable(
					R.drawable.p_transportation_transport_bus_stop_s);
		else
			return context.getResources().getDrawable(DEFAULT_ICON_ID);
	}
}
