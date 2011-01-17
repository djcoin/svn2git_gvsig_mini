package es.prodevelop.gvsig.mini.search.suggestions;

import java.util.ArrayList;
import java.util.Iterator;

import org.garret.perst.fulltext.FullTextIndex;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import es.prodevelop.android.spatialindex.quadtree.persist.perst.SpatialIndexRoot;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstQuadtreeProvider;
import es.prodevelop.gvsig.mini.search.POIProviderManager;

public class POIFullTextSuggestionProvider extends ContentProvider {

	private PerstQuadtreeProvider poiProvider = null;

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
		if (poiProvider == null)
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
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
