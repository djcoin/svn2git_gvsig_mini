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
	public static int MAX_DISTANCE = 32;
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
			imgs.put(R.drawable.add_16, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.add_16));
			imgs.put(R.drawable.add_48, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.add_48));
			imgs.put(R.drawable.add_8, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.add_8));
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
			// POIs 
			imgs.put(R.drawable.p_accommodation_hotel, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre));
			imgs.put(R.drawable.p_food_restaurant, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant));
			imgs.put(R.drawable.p_health_hospital, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital));
			imgs.put(R.drawable.p_places_poi_place_city, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city));
			imgs.put(R.drawable.p_public_buildings_tourist_monument,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument));
			imgs.put(R.drawable.p_recreation_sport_playground, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground));
			imgs.put(R.drawable.p_route_tourist_castle2, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2));
			imgs.put(R.drawable.p_shops_shopping_supermarket, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket));
			imgs.put(R.drawable.p_tourism_tourist_attraction, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction));
			imgs.put(R.drawable.p_transportation_transport_bus_stop,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop));

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
			imgs.put(R.drawable.p_accommodation_hotel_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_f));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_f));
			imgs.put(R.drawable.p_food_restaurant_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_f));
			imgs.put(R.drawable.p_health_hospital_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_f));
			imgs.put(R.drawable.p_places_poi_place_city_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_f));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_f));
			imgs.put(R.drawable.p_recreation_sport_playground_f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_f));
			imgs.put(R.drawable.p_route_tourist_castle2_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_f));
			imgs.put(R.drawable.p_shops_shopping_supermarket_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_f));
			imgs.put(R.drawable.p_tourism_tourist_attraction_f, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_f));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_f,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_f));

			// POIs search
			imgs.put(R.drawable.p_accommodation_hotel_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_s));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_s));
			imgs.put(R.drawable.p_food_restaurant_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_s));
			imgs.put(R.drawable.p_health_hospital_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_s));
			imgs.put(R.drawable.p_places_poi_place_city_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_s));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_s));
			imgs.put(R.drawable.p_recreation_sport_playground_s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_s));
			imgs.put(R.drawable.p_route_tourist_castle2_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_s));
			imgs.put(R.drawable.p_shops_shopping_supermarket_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_s));
			imgs.put(R.drawable.p_tourism_tourist_attraction_s, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_s));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_s,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_s));

			// Cluster low
			imgs.put(R.drawable.p_accommodation_hotel_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_l));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_l,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_l));
			imgs.put(R.drawable.p_food_restaurant_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_l));
			imgs.put(R.drawable.p_health_hospital_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_l));
			imgs.put(R.drawable.p_places_poi_place_city_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_l));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_l,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_l));
			imgs.put(R.drawable.p_recreation_sport_playground_l,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_l));
			imgs.put(R.drawable.p_route_tourist_castle2_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_l));
			imgs.put(R.drawable.p_shops_shopping_supermarket_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_l));
			imgs.put(R.drawable.p_tourism_tourist_attraction_l, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_l));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_l,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_l));

			// Cluster medium
			imgs.put(R.drawable.p_accommodation_hotel_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_m));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_m,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_m));
			imgs.put(R.drawable.p_food_restaurant_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_m));
			imgs.put(R.drawable.p_health_hospital_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_m));
			imgs.put(R.drawable.p_places_poi_place_city_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_m));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_m,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_m));
			imgs.put(R.drawable.p_recreation_sport_playground_m,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_m));
			imgs.put(R.drawable.p_route_tourist_castle2_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_m));
			imgs.put(R.drawable.p_shops_shopping_supermarket_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_m));
			imgs.put(R.drawable.p_tourism_tourist_attraction_m, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_m));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_m,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_m));

			// Cluster high
			imgs.put(R.drawable.p_accommodation_hotel_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_accommodation_hotel_h));
			imgs.put(R.drawable.p_arts_culture_tourist_theatre_h,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_arts_culture_tourist_theatre_h));
			imgs.put(R.drawable.p_food_restaurant_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_food_restaurant_h));
			imgs.put(R.drawable.p_health_hospital_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_health_hospital_h));
			imgs.put(R.drawable.p_places_poi_place_city_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_places_poi_place_city_h));
			imgs.put(R.drawable.p_public_buildings_tourist_monument_h,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_public_buildings_tourist_monument_h));
			imgs.put(R.drawable.p_recreation_sport_playground_h,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_recreation_sport_playground_h));
			imgs.put(R.drawable.p_route_tourist_castle2_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_route_tourist_castle2_h));
			imgs.put(R.drawable.p_shops_shopping_supermarket_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_shops_shopping_supermarket_h));
			imgs.put(R.drawable.p_tourism_tourist_attraction_h, BitmapFactory
					.decodeResource(context.getResources(),
							R.drawable.p_tourism_tourist_attraction_h));
			imgs.put(R.drawable.p_transportation_transport_bus_stop_h,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.p_transportation_transport_bus_stop_h));
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} finally {
//			MAX_DISTANCE = getBitmap(R.drawable.startpoi).getWidth();
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
