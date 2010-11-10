package es.prodevelop.gvsig.mini.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

public class POICategoryIcon {
	
	public static Bitmap getBitmap16ForCategory(String category) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_accommodation_hotel_16);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_arts_culture_tourist_theatre_16);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_food_restaurant_16);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_health_hospital_16);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_places_poi_place_city_16);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_public_buildings_tourist_monument_16);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_recreation_sport_playground_16);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_route_tourist_castle2_16);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_shops_shopping_supermarket_16);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_places_poi_place_city_16);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_tourism_tourist_attraction_16);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_transportation_transport_bus_stop_16);
		else return null;
	}
	
	public static Drawable getDrawable16ForCategory(String category, Context context) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return context.getResources().getDrawable(R.drawable.p_accommodation_hotel_16);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return context.getResources().getDrawable(R.drawable.p_arts_culture_tourist_theatre_16);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return context.getResources().getDrawable(R.drawable.p_food_restaurant_16);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return context.getResources().getDrawable(R.drawable.p_health_hospital_16);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return context.getResources().getDrawable(R.drawable.p_places_poi_place_city_16);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return context.getResources().getDrawable(R.drawable.p_public_buildings_tourist_monument_16);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return context.getResources().getDrawable(R.drawable.p_recreation_sport_playground_16);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return context.getResources().getDrawable(R.drawable.p_route_tourist_castle2_16);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return context.getResources().getDrawable(R.drawable.p_shops_shopping_supermarket_16);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return context.getResources().getDrawable(R.drawable.p_places_poi_place_city_16);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return context.getResources().getDrawable(R.drawable.p_tourism_tourist_attraction_16);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return context.getResources().getDrawable(R.drawable.p_transportation_transport_bus_stop_16);
		else return null;
	}
	
	public static Bitmap getBitmap32ForCategory(String category) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_accommodation_hotel_32);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_arts_culture_tourist_theatre_32);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_food_restaurant_32);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_health_hospital_32);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_public_buildings_tourist_monument_32);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_recreation_sport_playground_32);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_route_tourist_castle2_32);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_shops_shopping_supermarket_32);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_tourism_tourist_attraction_32);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return ResourceLoader.getBitmap(R.drawable.p_transportation_transport_bus_stop_32);
		else return null;
	}
	
	public static Drawable getDrawable32ForCategory(String category, Context context) {
		if (category.compareTo(POICategories.ACCOMODATION) == 0)
			return context.getResources().getDrawable(R.drawable.p_accommodation_hotel_32);
		else if (category.compareTo(POICategories.ARTS_CULTURE) == 0)
			return context.getResources().getDrawable(R.drawable.p_arts_culture_tourist_theatre_32);
		else if (category.compareTo(POICategories.FOOD) == 0)
			return context.getResources().getDrawable(R.drawable.p_food_restaurant_32);
		else if (category.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
			return context.getResources().getDrawable(R.drawable.p_health_hospital_32);
		else if (category.compareTo(POICategories.PLACES) == 0)
			return context.getResources().getDrawable(R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
			return context.getResources().getDrawable(R.drawable.p_public_buildings_tourist_monument_32);
		else if (category.compareTo(POICategories.RECREATION) == 0)
			return context.getResources().getDrawable(R.drawable.p_recreation_sport_playground_32);
		else if (category.compareTo(POICategories.ROUTE) == 0)
			return context.getResources().getDrawable(R.drawable.p_route_tourist_castle2_32);
		else if (category.compareTo(POICategories.SHOPS) == 0)
			return context.getResources().getDrawable(R.drawable.p_shops_shopping_supermarket_32);
		else if (category.compareTo(POICategories.STREETS) == 0)
			return context.getResources().getDrawable(R.drawable.p_places_poi_place_city_32);
		else if (category.compareTo(POICategories.TOURISM) == 0)
			return context.getResources().getDrawable(R.drawable.p_tourism_tourist_attraction_32);
		else if (category.compareTo(POICategories.TRANSPORTATION) == 0)
			return context.getResources().getDrawable(R.drawable.p_transportation_transport_bus_stop_32);
		else return null;
	}

}
