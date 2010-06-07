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
