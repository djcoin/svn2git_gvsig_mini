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
