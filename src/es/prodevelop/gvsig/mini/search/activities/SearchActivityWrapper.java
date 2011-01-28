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

import android.text.TextWatcher;
import android.widget.MultiAutoCompleteTextView;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.adapter.AutoCompleteAdapter;

public interface SearchActivityWrapper extends TextWatcher {

	public final static int SEARCH_ROUTE_START = 0;
	public final static int SEARCH_ROUTE_END = 1;
	public final static int SEARCH_CENTER = 2;
	public final static int SEARCH_BOOKMARK = 3;
	public final static int SEARCH_ADD_CONTACT = 4;
	public final static int SEARCH_CALL = 5;
	public final static int SEARCH_FIND_NEAR = 6;

	public QuadtreeProvider getProvider() throws BaseException;	

	public AutoCompleteAdapter getAutoCompleteAdapter();

	public void setAutoCompleteAdapter(AutoCompleteAdapter adapter);

	public MultiAutoCompleteTextView getAutoCompleteTextView();

	public void setAutoCompleteTextView(
			MultiAutoCompleteTextView autoCompleteTextView);

	public Point getCenter();

}
