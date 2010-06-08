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
 *	 2010.
 *   author Miguel Montesinos - mmontesinos@prodevelop.es
 *   
 */


package es.prodevelop.gvsig.mini.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Creates suggestions (as the user types) based on recent queries to be used
 * by the <code>SearchManager</code> after using the Android's search key adn
 * typing some text
 * @author mmontesinos
 *
 */
public class SearchSuggestionsMiniProvider extends SearchRecentSuggestionsProvider {
    //Provider authority identifier.  
    final static String AUTHORITY = ".search.SearchSuggestionsMiniProvider";
    final static int MODE = DATABASE_MODE_QUERIES;
    
    /**
     * The main resposibility of the constructor is to call
     * {@link #setupSuggestions(String, int)} so that it configures
     * the provider to match the requirements of the searchable activity.
     */
    public SearchSuggestionsMiniProvider() {
        super();
        setupSuggestions(AUTHORITY, MODE);
    }

}
