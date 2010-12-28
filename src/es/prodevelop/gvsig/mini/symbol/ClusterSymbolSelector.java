package es.prodevelop.gvsig.mini.symbol;

import android.graphics.Bitmap;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.views.overlay.PerstClusterPOIOverlay;

public class ClusterSymbolSelector extends SymbolSelector {

	private int[] midIcon = new int[] { 8, 8 };

	private Bitmap STREET;
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

	private Bitmap TRANSPORTATION_BIG;
	private Bitmap TOURISM_BIG;
	private Bitmap RECREATION_BIG;
	private Bitmap FOOD_BIG;
	private Bitmap PUBLIC_BUILDINGS_BIG;
	private Bitmap ARTS_CULTURE_BIG;
	private Bitmap SHOPS_BIG;
	private Bitmap HEALTH_EMERGENCY_BIG;
	private Bitmap ACCOMODATION_BIG;
	private Bitmap ROUTE_BIG;
	private Bitmap PLACES_BIG;

	private Bitmap TRANSPORTATION_SMALL;
	private Bitmap TOURISM_SMALL;
	private Bitmap RECREATION_SMALL;
	private Bitmap FOOD_SMALL;
	private Bitmap PUBLIC_BUILDINGS_SMALL;
	private Bitmap ARTS_CULTURE_SMALL;
	private Bitmap SHOPS_SMALL;
	private Bitmap HEALTH_EMERGENCY_SMALL;
	private Bitmap ACCOMODATION_SMALL;
	private Bitmap ROUTE_SMALL;
	private Bitmap PLACES_SMALL;

	private PerstClusterPOIOverlay overlay;

	public ClusterSymbolSelector(PerstClusterPOIOverlay overlay) {
		this.overlay = overlay;
		STREET = null;
		TRANSPORTATION = ResourceLoader
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_16);
		TOURISM = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_16);
		RECREATION = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_16);
		FOOD = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_16);
		PUBLIC_BUILDINGS = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_16);
		ARTS_CULTURE = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_16);
		SHOPS = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_16);
		HEALTH_EMERGENCY = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_16);
		ACCOMODATION = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_16);
		ROUTE = ResourceLoader.getBitmap(R.drawable.p_route_tourist_castle2_16);
		PLACES = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_16);

		TRANSPORTATION_BIG = ResourceLoader
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_32);
		TOURISM_BIG = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_32);
		RECREATION_BIG = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_32);
		FOOD_BIG = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_32);
		PUBLIC_BUILDINGS_BIG = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_32);
		ARTS_CULTURE_BIG = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_32);
		SHOPS_BIG = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_32);
		HEALTH_EMERGENCY_BIG = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_32);
		ACCOMODATION_BIG = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_32);
		ROUTE_BIG = ResourceLoader
				.getBitmap(R.drawable.p_route_tourist_castle2_32);
		PLACES_BIG = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_32);

		TRANSPORTATION_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_16_poi);
		TOURISM_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_16_poi);
		RECREATION_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_16_poi);
		FOOD_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_food_restaurant_16_poi);
		PUBLIC_BUILDINGS_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_16_poi);
		ARTS_CULTURE_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_16_poi);
		SHOPS_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_16_poi);
		HEALTH_EMERGENCY_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_16_poi);
		ACCOMODATION_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_16_poi);
		ROUTE_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_route_tourist_castle2_16_poi);
		PLACES_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_16_poi);
	}

	@Override
	public Bitmap getSymbol(Point point) {
		Cluster p = (Cluster) point;

		final int maxClusterSize = overlay.getMaxClusterSizeCat().get(
				POICategories.ORDERED_CATEGORIES[p.getCat()]);
		final int step = maxClusterSize / 3;

		final int small = step;
		final int medium = small + step;
		final int big = medium + step;

		final int items = p.getNumItems();

		Bitmap icon = null;
		switch (p.getCat()) {
		case 2:
			if (items <= small)
				icon = this.TRANSPORTATION_SMALL;
			else if (items <= medium && items > small)
				icon = this.TRANSPORTATION;
			else
				icon = this.TRANSPORTATION_BIG;
			break;
		case 3:
			if (items <= small)
				icon = this.TOURISM_SMALL;
			else if (items <= medium && items > small)
				icon = this.TOURISM;
			else
				icon = this.TOURISM_BIG;
			break;
		case 4:
			if (items <= small)
				icon = this.RECREATION_SMALL;
			else if (items <= medium && items > small)
				icon = this.RECREATION;
			else
				icon = this.RECREATION_BIG;
			break;
		case 5:
			if (items <= small)
				icon = this.FOOD_SMALL;
			else if (items <= medium && items > small)
				icon = this.FOOD;
			else
				icon = this.FOOD_BIG;
			break;
		case 6:
			if (items <= small)
				icon = this.PUBLIC_BUILDINGS_SMALL;
			else if (items <= medium && items > small)
				icon = this.PUBLIC_BUILDINGS;
			else
				icon = this.PUBLIC_BUILDINGS_BIG;
			break;
		case 7:
			if (items <= small)
				icon = this.ARTS_CULTURE_SMALL;
			else if (items <= medium && items > small)
				icon = this.ARTS_CULTURE;
			else
				icon = this.ARTS_CULTURE_BIG;
			break;
		case 8:
			if (items <= small)
				icon = this.SHOPS_SMALL;
			else if (items <= medium && items > small)
				icon = this.SHOPS;
			else
				icon = this.SHOPS_BIG;
			break;
		case 9:
			if (items <= small)
				icon = this.HEALTH_EMERGENCY_SMALL;
			else if (items <= medium && items > small)
				icon = this.HEALTH_EMERGENCY;
			else
				icon = this.HEALTH_EMERGENCY_BIG;
			break;
		case 10:
			if (items <= small)
				icon = this.ACCOMODATION_SMALL;
			else if (items <= medium && items > small)
				icon = this.ACCOMODATION;
			else
				icon = this.ACCOMODATION_BIG;
			break;
		case 11:
			if (items <= small)
				icon = this.ROUTE_SMALL;
			else if (items <= medium && items > small)
				icon = this.ROUTE;
			else
				icon = this.ROUTE_BIG;
			break;
		case 12:
			if (items <= small)
				icon = this.PLACES_SMALL;
			else if (items <= medium && items > small)
				icon = this.PLACES;
			else
				icon = this.PLACES_BIG;
			break;
		}
		if (items <= small)
			midIcon = new int[] { 8, 8 };
		else if (items <= medium && items > small)
			midIcon = new int[] { 8, 8 };
		else
			midIcon = new int[] { 16, 16 };

		return icon;
	}

	@Override
	public String getText(Point p) {
		return null;
	}

	@Override
	public int[] getMidSymbol() {
		return midIcon;
	}

}
