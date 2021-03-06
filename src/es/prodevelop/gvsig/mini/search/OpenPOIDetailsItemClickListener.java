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

package es.prodevelop.gvsig.mini.search;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.gvsig.mini.search.activities.POIDetailsActivity;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.adapter.FilteredLazyAdapter;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;

public class OpenPOIDetailsItemClickListener implements OnItemClickListener {

	private SearchActivity activity;

	public OpenPOIDetailsItemClickListener(SearchActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0.getAdapter() instanceof FilteredLazyAdapter) {
			final FilteredLazyAdapter adapter = (FilteredLazyAdapter) arg0
					.getAdapter();

			Intent i = new Intent(activity, POIDetailsActivity.class);
			Object p = adapter.getItem(arg2);
			if (p != null && p instanceof OsmPOI) {
				OsmPOI poi = (OsmPOI) p;
				InvokeIntents.fillIntentPOIDetails(poi, activity.getCenter(),
						i, activity);

				activity.startActivity(i);

			}
		}
	}
}
