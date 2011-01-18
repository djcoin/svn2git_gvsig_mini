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

}
