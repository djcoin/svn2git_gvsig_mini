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

package es.prodevelop.gvsig.mini.search.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.BookmarkClickListener;
import es.prodevelop.gvsig.mini.search.POIProviderManager;
import es.prodevelop.gvsig.mini.search.adapter.BookmarkAdapter;
import es.prodevelop.gvsig.mini.search.adapter.FilteredLazyAdapter;
import es.prodevelop.gvsig.mini.search.view.PinnedHeaderListView;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;

public class BookmarkPOIActivity extends ResultSearchActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			listener = new BookmarkClickListener(this, R.drawable.pois,
					R.string.NameFinderActivity_0);
		} catch (Exception e) {
			Log.e("BookmarkAct", e.getMessage());
		}		
	}

	@Override
	public void initializeAdapters() {
		View pinnedHeader = getLayoutInflater().inflate(R.layout.list_header,
				getListView(), false);
		((PinnedHeaderListView) getListView()).setPinnedHeaderView(pinnedHeader
				.findViewById(R.id.section_text));
		if (POIProviderManager.getInstance().getPOIProvider() == null)
			try {
				POIProviderManager.getInstance()
						.registerPOIProvider(
								new PerstOsmPOIClusterProvider("/sdcard/gvSIG/pois/london/"
										+ "perst_streets_cluster_cat.db", 18,
										null, 18));
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		listAdapter = new BookmarkAdapter(this);
		this.getListView().setAdapter(this.listAdapter);
		this.setListAdapter(this.listAdapter);
	}

	public QuadtreeProvider getProvider() {
		return POIProviderManager.getInstance().getBookmarkProvider();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		try {
			switch (item.getItemId()) {
			case 0:
				InvokeIntents.launchListBookmarks(this, new double[] {
						this.getCenter().getX(), this.getCenter().getY() });

				break;
			}
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}		
		return super.onMenuItemSelected(featureId, item);
	}
}
