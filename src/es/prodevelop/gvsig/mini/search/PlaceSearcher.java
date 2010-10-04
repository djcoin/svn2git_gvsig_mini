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



import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.provider.SearchRecentSuggestions;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.namefinder.NameFinder;
import es.prodevelop.gvsig.mini.tasks.namefinder.NameFinderFunc;

/**
 * Class for performing searches of places or addreses in a common way for all
 * the application, as well as being a utility for clearing searches history
 * @author mmontesinos
 *
 */
public class PlaceSearcher {

	private Map map;
	private Context ctx;
	private final static Logger log = Logger.getLogger(PlaceSearcher.class.getName());

	/**
	 * Simple constructor aimed "only" at clearing recent suggestions history
	 * @param ctx: Context to be used for clearing the history
	 */
	public PlaceSearcher(Context ctx){
		this.ctx = ctx;
	}
	
	/**
	 * Class simple constructor, that performs a search with a query string only 
	 * @param query Text to be sought as a place, POI or address
	 */
	public PlaceSearcher(Map map, String query){
		try {
//			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
			this.map = map;			 
			//First save the query for the recent suggestion provider
			saveQuery(query);
			
			doSearch(query);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}		
	}
	
	/**
	 * Class constructor, that performs a search with a query string and a center
	 * point of the search 
	 * @param query Text to be sought as a place, POI or address
	 * @param centerX X coordinate of the center point of the search where to 
	 *                search near to
	 * @param centerY Y coordinate of the center point of the search where to 
	 *                search near to
	 */
	public PlaceSearcher(Map map, String query, double centerX, double centerY){
		this.map = map;
		this.ctx = map.getBaseContext();
		//First save the query for the recent suggestion provider
		//Save before adding "near" suffixes
		saveQuery(query);
		
		query += " near " + centerY + "," + centerX;
		doSearch(query);
	}
	
	/**
	 * Erases the history of strings used for searching
	 */
	public void clearSearchHistory(){
		try {
			// Get an instance of SearchRecentSuggestions and call ClearHistory()
	        // queries suggestions provider.
	        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this.ctx, 
	                SearchSuggestionsMiniProvider.AUTHORITY, 
	                SearchSuggestionsMiniProvider.MODE);
	        suggestions.clearHistory();
		} catch (Exception e) {
			log.log(Level.SEVERE,"PlaceSearcher.Clearhistory() error: ", e);
		}
		
	}
	
	private void doSearch(String query){
		try {
			if (this.map != null) {
				if (!query.trim().equals("")) {
					
					NameFinder.parms = query;
					NameFinderFunc func = new NameFinderFunc(
						this.map, 0);
					func.onClick(null);
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"PlaceSearcher.doSearch() error: ", e);
		}
	}
	
	private void saveQuery(String query){
		try {
			// Record the query string in the recent 
	        // queries suggestions provider.
	        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this.ctx, 
	                SearchSuggestionsMiniProvider.AUTHORITY, 
	                SearchSuggestionsMiniProvider.MODE);
	        suggestions.saveRecentQuery(query, "");
		} catch (Exception e) {
			log.log(Level.SEVERE,"PlaceSearcher.saveQuery() error: ", e);
		}

	}
}
