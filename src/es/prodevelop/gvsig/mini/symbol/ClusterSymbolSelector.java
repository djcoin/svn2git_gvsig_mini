package es.prodevelop.gvsig.mini.symbol;

import android.graphics.Bitmap;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.util.ResourceLoader;
import es.prodevelop.gvsig.mini.views.overlay.PerstClusterPOIOverlay;

public class ClusterSymbolSelector extends SymbolSelector {

	private int[] midIconSmall = new int[] { 9, 9 };
	private int[] midIconMedium = new int[] { 9, 9 };
	private int[] midIconBig = new int[] { 9, 9 };

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
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_22b);
		TOURISM = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_22b);
		RECREATION = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_22b);
		FOOD = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_22b);
		PUBLIC_BUILDINGS = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_22b);
		ARTS_CULTURE = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_22b);
		SHOPS = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_22b);
		HEALTH_EMERGENCY = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_22b);
		ACCOMODATION = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_22b);
		ROUTE = ResourceLoader
				.getBitmap(R.drawable.p_route_tourist_castle2_22b);
		PLACES = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_22b);

		TRANSPORTATION_BIG = ResourceLoader
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_28b);
		TOURISM_BIG = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_28b);
		RECREATION_BIG = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_28b);
		FOOD_BIG = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_28b);
		PUBLIC_BUILDINGS_BIG = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_28b);
		ARTS_CULTURE_BIG = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_28b);
		SHOPS_BIG = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_28b);
		HEALTH_EMERGENCY_BIG = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_28b);
		ACCOMODATION_BIG = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_28b);
		ROUTE_BIG = ResourceLoader
				.getBitmap(R.drawable.p_route_tourist_castle2_28b);
		PLACES_BIG = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_28b);

		TRANSPORTATION_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_transportation_transport_bus_stop_18b);
		TOURISM_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_tourism_tourist_attraction_18b);
		RECREATION_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_recreation_sport_playground_18b);
		FOOD_SMALL = ResourceLoader.getBitmap(R.drawable.p_food_restaurant_18b);
		PUBLIC_BUILDINGS_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_public_buildings_tourist_monument_18b);
		ARTS_CULTURE_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_arts_culture_tourist_theatre_18b);
		SHOPS_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_shops_shopping_supermarket_18b);
		HEALTH_EMERGENCY_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_health_hospital_18b);
		ACCOMODATION_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_accommodation_hotel_18b);
		ROUTE_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_route_tourist_castle2_18b);
		PLACES_SMALL = ResourceLoader
				.getBitmap(R.drawable.p_places_poi_place_city_18b);

		if (PLACES != null) {
			midIconMedium[0] = PLACES.getWidth();
			midIconMedium[1] = PLACES.getHeight();
		}

		if (PLACES_SMALL != null) {
			midIconSmall[0] = PLACES_SMALL.getWidth();
			midIconSmall[1] = PLACES_SMALL.getHeight();
		}

		if (PLACES_BIG != null) {
			midIconBig[0] = PLACES_BIG.getWidth();
			midIconBig[1] = PLACES_BIG.getHeight();
		}
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

		return icon;
	}

	@Override
	public String getText(Point p) {
		try {
			if (p instanceof Cluster) {
				return String.format(overlay.getContext().getResources()
						.getString(R.string.cluster_elements),
						((Cluster) p).getNumItems());
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public int[] getMidSymbol(Point p) {
		Bitmap b = getSymbol(p);
		return new int[] { (int) (b.getWidth() / 2), (int) (b.getHeight() / 2) };
	}
}
