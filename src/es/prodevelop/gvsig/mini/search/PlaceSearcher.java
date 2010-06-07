package es.prodevelop.gvsig.mini.search;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.namefinder.NameFinder;
import es.prodevelop.gvsig.mini.tasks.namefinder.NameFinderFunc;
import android.content.Context;
import android.provider.SearchRecentSuggestions;

/**
 * Class for performing searches of places or addreses in a common way for all
 * the application
 * @author mmontesinos
 *
 */
public class PlaceSearcher {

	private Map map;
	private final static Logger log = LoggerFactory.getLogger(PlaceSearcher.class);

	/**
	 * Class simple constructor, that performs a search with a query string only 
	 * @param query Text to be sought as a place, POI or address
	 */
	public PlaceSearcher(Map map, String query){
		this.map = map;
		//First save the query for the recent suggestion provider
		saveQuery(this.map.getBaseContext(),query);
		
		doSearch(query);
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
		//First save the query for the recent suggestion provider
		//Save before adding "near" suffixes
		saveQuery(this.map.getBaseContext(),query);
		
		query += " near " + centerY + "," + centerX;
		doSearch(query);
	}
	
	private void doSearch(String query){
		try {
			if (!query.trim().equals("")) {
				
				NameFinder.parms = query;
				NameFinderFunc func = new NameFinderFunc(
					this.map, 0);
				func.onClick(null);
			}
		} catch (Exception e) {
			log.error("PlaceSearcher.doSearch() error: ", e);
		}
	}
	
	private void saveQuery(Context context, String query){
		try {
			// Record the query string in the recent 
	        // queries suggestions provider.
	        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(context, 
	                SearchSuggestionsMiniProvider.AUTHORITY, 
	                SearchSuggestionsMiniProvider.MODE);
	        suggestions.saveRecentQuery(query, null);
		} catch (Exception e) {
			log.error("PlaceSearcher.saveQuery() error: ", e);
		}

	}
}
