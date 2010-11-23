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

package es.prodevelop.gvsig.mini.tasks.async;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.provide.LoadCategoryListener;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.tilecache.renderer.MapRenderer;

public class LoadClusterIndexAsyncTask extends
		AsyncTask<ArrayList, String, Integer> implements LoadCategoryListener {

	PerstOsmPOIClusterProvider provider;
	ProgressDialog pDialog;
	Map map;

	public LoadClusterIndexAsyncTask(Map map,
			PerstOsmPOIClusterProvider provider) {
		this.provider = provider;
		this.map = map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (pDialog != null)
			pDialog.dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Integer result) {
		try {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pDialog != null)
				pDialog.dismiss();

//			MapRenderer renderer = this.map.osmap.getMRendererInfo();
//
//			this.map.osmap.poiOverlay.onExtentChanged(renderer.getCurrentExtent(),
//					renderer.getZoomLevel(), renderer.getCurrentRes());
		} catch (Exception e) {
			
		} finally {
			this.map.osmap.resumeDraw();			
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {			
		// TODO Auto-generated method stub
		this.map.osmap.pauseDraw();
		super.onPreExecute();
		pDialog = ProgressDialog.show(map,
				map.getResources().getString(R.string.loading_poi_categories),
				"");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if (pDialog != null)
			pDialog.setMessage(values[0] + " " + values[1]);

		int icon = -1;
		final String cat = values[1];
		if (cat != null) {
			if (cat.compareTo(POICategories.TRANSPORTATION) == 0)
				icon = R.drawable.p_transportation_transport_bus_stop_32;
			else if (cat.compareTo(POICategories.TOURISM) == 0)
				icon = R.drawable.p_tourism_tourist_attraction_32;
			else if (cat.compareTo(POICategories.RECREATION) == 0)
				icon = R.drawable.p_recreation_sport_playground_32;
			else if (cat.compareTo(POICategories.FOOD) == 0)
				icon = R.drawable.p_food_restaurant_32;
			else if (cat.compareTo(POICategories.PUBLIC_BUILDINGS) == 0)
				icon = R.drawable.p_public_buildings_tourist_monument_32;
			else if (cat.compareTo(POICategories.ARTS_CULTURE) == 0)
				icon = R.drawable.p_arts_culture_tourist_theatre_32;
			else if (cat.compareTo(POICategories.SHOPS) == 0)
				icon = R.drawable.p_shops_shopping_supermarket_32;
			else if (cat.compareTo(POICategories.HEALTH_EMERGENCY) == 0)
				icon = R.drawable.p_health_hospital_32;
			else if (cat.compareTo(POICategories.ACCOMODATION) == 0)
				icon = R.drawable.p_accommodation_hotel_32;
			else if (cat.compareTo(POICategories.ROUTE) == 0)
				icon = R.drawable.p_route_tourist_castle2_32;
			else if (cat.compareTo(POICategories.PLACES) == 0)
				icon = R.drawable.p_places_poi_place_city_32;

			if (icon != -1 && pDialog != null)
				pDialog.setIcon(icon);
		}

	}

	@Override
	protected Integer doInBackground(ArrayList... params) {
		// TODO Auto-generated method stub
		try {
			this.map.osmap.poiOverlay.getPoiProvider().loadCategories(
					params[0], this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onLoadingCategoryIndex(String category) {
		publishProgress(new String[] {
				map.getResources().getString(R.string.loading_poi_category),
				category });
	}

	@Override
	public void onCategoryIndexLoaded(String category) {
		// publishProgress(category);
	}

	@Override
	public void onCategoryIndexRemoved(String category) {
		// publishProgress(category);
	}

	@Override
	public void onRemovingCategoryIndex(String category) {
		publishProgress(new String[] {
				map.getResources().getString(R.string.removing_poi_category),
				category });
	}

}
