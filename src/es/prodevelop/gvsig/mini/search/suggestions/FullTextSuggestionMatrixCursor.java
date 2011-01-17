package es.prodevelop.gvsig.mini.search.suggestions;

import java.util.ArrayList;

import es.prodevelop.gvsig.mini.utiles.Utilities;

import android.app.SearchManager;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

public class FullTextSuggestionMatrixCursor extends MatrixCursor {

	public final static String[] SUGGESTION_COLUMN_NAMES = new String[] {
			BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2,
			/*
			 * SearchManager.SUGGEST_COLUMN_ICON_1,
			 * SearchManager.SUGGEST_COLUMN_ICON_2,
			 */
			/*
			 * SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
			 */
			SearchManager.SUGGEST_COLUMN_INTENT_DATA,
			/*
			 * SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
			 * SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
			 */
			SearchManager.SUGGEST_COLUMN_QUERY,
			SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
			SearchManager.SUGGEST_COLUMN_SPINNER_WHILE_REFRESHING };

	public FullTextSuggestionMatrixCursor(String[] columnNames) {
		super(columnNames);
	}

	public void fillMatrix(ArrayList words) {
		if (words == null)
			return;

		final int size = words.size();

		String word;
		for (int i = 0; i < size; i++) {
			word = words.get(i).toString();
			if (word != null && word.length() != 0) {
				word = Utilities.capitalizeFirstLetters(word);
				this.addRow(new Object[] { -1, word, "", /*
														 * 0, 0, null,
														 */word,
				/*
				 * null, null,
				 */null, null, null });
			}
		}
	}
}
