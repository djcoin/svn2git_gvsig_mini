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

import android.util.Log;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstBookmarkProvider;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIClusterProvider;
import es.prodevelop.gvsig.mini.exceptions.BaseException;

public class POIProviderManager {

	private static POIProviderManager instance;
	private PerstOsmPOIClusterProvider poiProvider;

	private PerstBookmarkProvider bookmarkProvider;

	public static POIProviderManager getInstance() {
		if (instance == null)
			instance = new POIProviderManager();
		return instance;
	}

	public void registerPOIProvider(PerstOsmPOIClusterProvider provider) {
		if (poiProvider != null)
			unregisterPOIProvider();
		this.poiProvider = provider;
		registerBookmarkPOIProvider(new PerstBookmarkProvider(
				provider.getDatabasePath() + "_fav"));
	}

	public PerstOsmPOIClusterProvider getPOIProvider() throws BaseException {
		if (this.poiProvider == null)
			throw new BaseException("No POI provider registered");
		return this.poiProvider;
	}

	public void registerBookmarkPOIProvider(PerstBookmarkProvider provider) {
		if (bookmarkProvider != null)
			unregisterBookmarkPOIProvider();
		this.bookmarkProvider = provider;
	}

	public PerstBookmarkProvider getBookmarkProvider() {
		return this.bookmarkProvider;
	}

	public void unregisterPOIProvider() {
		try {
			if (this.poiProvider != null)
				this.poiProvider.getHelper().close();
			this.poiProvider = null;
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}

	public void unregisterBookmarkPOIProvider() {
		try {
			if (this.bookmarkProvider != null)
				this.bookmarkProvider.getHelper().close();
			this.bookmarkProvider = null;
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
}
