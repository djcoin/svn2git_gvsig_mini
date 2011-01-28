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

package es.prodevelop.gvsig.mini.search.suggestions;

import java.util.ArrayList;
import java.util.Iterator;

import org.garret.perst.fulltext.FullTextIndex;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstQuadtreeProvider;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.search.POIProviderManager;

public class POIFullTextSuggestionProvider extends ContentProvider {

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		PerstQuadtreeProvider poiProvider;
		try {
			poiProvider = ((PerstQuadtreeProvider) POIProviderManager
					.getInstance().getPOIProvider());

			if (poiProvider == null)
				return null;

			final SpatialIndexRoot root = (SpatialIndexRoot) poiProvider
					.getHelper().getRoot();

			String prefix = selectionArgs[0];

			if (prefix == null) {
				return null;
			}

			if (prefix.toString().length() <= 0) {
				return null;
			}

			Iterator<FullTextIndex.Keyword> it = root.getFullTextIndex()
					.getKeywords(prefix.toString().toLowerCase());

			ArrayList list = new ArrayList();
			while (it.hasNext()) {
				list.add(it.next().getNormalForm());
			}

			if (list.size() == 0) {
				return null;
			}

			// fill a matrix cursor
			FullTextSuggestionMatrixCursor cursor = new FullTextSuggestionMatrixCursor(
					FullTextSuggestionMatrixCursor.SUGGESTION_COLUMN_NAMES);
			cursor.fillMatrix(list);

			return cursor;
		} catch (BaseException e) {
			Log.e("", e.getMessage());
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
