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

package es.prodevelop.gvsig.mini.util;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;

/**
 * Utility class to load once Drawable resources and deliver them staticly to
 * Activities
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
public class ResourceLoader {

	private static final Logger log = Logger.getLogger(ResourceLoader.class
			.getName());
	static Context context;
	static Hashtable imgs;
	public static int MAX_DISTANCE = 50;
	public static int MIN_PAN = 5;

	/**
	 * Fills a Hashtable with the Drawable resources
	 * 
	 * @param context
	 *            The context to take the resources from
	 */
	public static void initialize(Context context) {
		try {
			CompatManager.getInstance().getRegisteredLogHandler()
					.configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ResourceLoader.context = context;
			if (imgs != null)
				return;
			imgs = new Hashtable();
			imgs.put(R.drawable.gps_arrow, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.gps_arrow));
			imgs.put(R.drawable.zoom_scales, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.zoom_scales));
			imgs.put(R.drawable.maptile_loading, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.maptile_loading));
			imgs.put(R.drawable.maptile_loadingoffline, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.maptile_loadingoffline));
			imgs.put(R.drawable.arrowdown, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.arrowdown));
			imgs.put(R.drawable.startpoi, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.startpoi));
			imgs.put(R.drawable.finishpoi, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.finishpoi));
			imgs.put(R.drawable.pois, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.pois));
			imgs.put(R.drawable.bt, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.bt));
			imgs.put(R.drawable.center_focus, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.center_focus));
			imgs.put(R.drawable.zoom_scales_rotate, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.zoom_scales_rotate));
			imgs.put(R.drawable.zoom_scales_on_rotate, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.zoom_scales_on_rotate));
			imgs.put(R.drawable.arrow, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.arrow));
			imgs.put(R.drawable.arrow_on, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.arrow_on));
			imgs.put(R.drawable.arrow, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.arrow));
			imgs.put(R.drawable.arrow_on, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.arrow_on));
			imgs.put(R.drawable.layer_icon, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.layer_icon));
			imgs.put(R.drawable.layer_icon_on, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.layer_icon_on));
			// POIs 16
			imgs.put(R.drawable.p_accommodation_hotel_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_16));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_16,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_16));
			imgs.put(R.drawable.p_food_restaurant_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_16));
			imgs.put(R.drawable.p_health_hospital_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_16));
			imgs.put(R.drawable.p_places_poi_place_city_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_16));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_16,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_16));
			imgs.put(R.drawable.p_recreation_sport_playground_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_16));
			imgs.put(R.drawable.p_route_tourist_castle2_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_16));
			imgs.put(R.drawable.p_shops_shopping_supermarket_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_16));
			imgs.put(R.drawable.p_tourism_tourist_attraction_16, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_16));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_16,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_16));
			
			// POIs 24
			imgs.put(R.drawable.p_accommodation_hotel_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_24));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_24,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_24));
			imgs.put(R.drawable.p_food_restaurant_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_24));
			imgs.put(R.drawable.p_health_hospital_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_24));
			imgs.put(R.drawable.p_places_poi_place_city_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_24));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_24,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_24));
			imgs.put(R.drawable.p_recreation_sport_playground_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_24));
			imgs.put(R.drawable.p_route_tourist_castle2_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_24));
			imgs.put(R.drawable.p_shops_shopping_supermarket_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_24));
			imgs.put(R.drawable.p_tourism_tourist_attraction_24, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_24));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_24,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_24));

			// POIs 32
			imgs.put(R.drawable.p_accommodation_hotel_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_32));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_32,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_32));
			imgs.put(R.drawable.p_food_restaurant_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_32));
			imgs.put(R.drawable.p_health_hospital_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_32));
			imgs.put(R.drawable.p_places_poi_place_city_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_32));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_32,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_32));
			imgs.put(R.drawable.p_recreation_sport_playground_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_32));
			imgs.put(R.drawable.p_route_tourist_castle2_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_32));
			imgs.put(R.drawable.p_shops_shopping_supermarket_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_32));
			imgs.put(R.drawable.p_tourism_tourist_attraction_32, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_32));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_32,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_32));
			
			// POIs bookmark
			imgs.put(R.drawable.p_accommodation_hotel_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_16f));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_16f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_16f));
			imgs.put(R.drawable.p_food_restaurant_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_16f));
			imgs.put(R.drawable.p_health_hospital_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_16f));
			imgs.put(R.drawable.p_places_poi_place_city_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_16f));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_16f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_16f));
			imgs.put(R.drawable.p_recreation_sport_playground_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_16f));
			imgs.put(R.drawable.p_route_tourist_castle2_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_16f));
			imgs.put(R.drawable.p_shops_shopping_supermarket_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_16f));
			imgs.put(R.drawable.p_tourism_tourist_attraction_16f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_16f));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_16f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_16f));
			
			// POIs search
			imgs.put(R.drawable.p_accommodation_hotel_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_16s));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_16s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_16s));
			imgs.put(R.drawable.p_food_restaurant_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_16s));
			imgs.put(R.drawable.p_health_hospital_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_16s));
			imgs.put(R.drawable.p_places_poi_place_city_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_16s));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_16s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_16s));
			imgs.put(R.drawable.p_recreation_sport_playground_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_16s));
			imgs.put(R.drawable.p_route_tourist_castle2_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_16s));
			imgs.put(R.drawable.p_shops_shopping_supermarket_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_16s));
			imgs.put(R.drawable.p_tourism_tourist_attraction_16s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_16s));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_16s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_16s));
			
			// Cluster 18
			imgs.put(R.drawable.p_accommodation_hotel_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_18b));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_18b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_18b));
			imgs.put(R.drawable.p_food_restaurant_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_18b));
			imgs.put(R.drawable.p_health_hospital_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_18b));
			imgs.put(R.drawable.p_places_poi_place_city_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_18b));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_18b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_18b));
			imgs.put(R.drawable.p_recreation_sport_playground_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_18b));
			imgs.put(R.drawable.p_route_tourist_castle2_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_18b));
			imgs.put(R.drawable.p_shops_shopping_supermarket_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_18b));
			imgs.put(R.drawable.p_tourism_tourist_attraction_18b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_18b));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_18b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_18b));
			
			// Cluster 38
			imgs.put(R.drawable.p_accommodation_hotel_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_38b));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_38b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_38b));
			imgs.put(R.drawable.p_food_restaurant_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_38b));
			imgs.put(R.drawable.p_health_hospital_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_38b));
			imgs.put(R.drawable.p_places_poi_place_city_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_38b));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_38b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_38b));
			imgs.put(R.drawable.p_recreation_sport_playground_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_38b));
			imgs.put(R.drawable.p_route_tourist_castle2_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_38b));
			imgs.put(R.drawable.p_shops_shopping_supermarket_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_38b));
			imgs.put(R.drawable.p_tourism_tourist_attraction_38b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_38b));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_38b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_38b));
			
			// Cluster 28
			imgs.put(R.drawable.p_accommodation_hotel_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_28b));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_28b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_28b));
			imgs.put(R.drawable.p_food_restaurant_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_28b));
			imgs.put(R.drawable.p_health_hospital_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_28b));
			imgs.put(R.drawable.p_places_poi_place_city_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_28b));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_28b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_28b));
			imgs.put(R.drawable.p_recreation_sport_playground_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_28b));
			imgs.put(R.drawable.p_route_tourist_castle2_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_28b));
			imgs.put(R.drawable.p_shops_shopping_supermarket_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_28b));
			imgs.put(R.drawable.p_tourism_tourist_attraction_28b, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_28b));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_28b,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_28b));
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} finally {
			MAX_DISTANCE = getBitmap(R.drawable.startpoi).getWidth();
			MIN_PAN = MAX_DISTANCE / 4;
		}
	}

	/**
	 * Returns a Bitmap given its R.id
	 * 
	 * @param id
	 * @return
	 */
	public static Bitmap getBitmap(int id) {
		try {
			return (Bitmap) imgs.get(id);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			return null;
		}
	}

}
