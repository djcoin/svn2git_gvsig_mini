package es.prodevelop.gvsig.mini.search;

import android.text.TextWatcher;
import android.widget.MultiAutoCompleteTextView;
import es.prodevelop.android.spatialindex.quadtree.provide.QuadtreeProvider;
import es.prodevelop.gvsig.mini.geom.Point;

public interface SearchActivityWrapper extends TextWatcher {

	public final static int SEARCH_ROUTE_START = 0;
	public final static int SEARCH_ROUTE_END = 1;
	public final static int SEARCH_CENTER = 2;
	public final static int SEARCH_BOOKMARK = 3;
	public final static int SEARCH_ADD_CONTACT = 4;
	public final static int SEARCH_CALL = 5;
	public final static int SEARCH_FIND_NEAR = 6;

	public QuadtreeProvider getProvider();

	public void setProvider(QuadtreeProvider provider);

	public AutoCompleteAdapter getAutoCompleteAdapter();

	public void setAutoCompleteAdapter(AutoCompleteAdapter adapter);

	public MultiAutoCompleteTextView getAutoCompleteTextView();

	public void setAutoCompleteTextView(
			MultiAutoCompleteTextView autoCompleteTextView);

	public Point getCenter();

}
