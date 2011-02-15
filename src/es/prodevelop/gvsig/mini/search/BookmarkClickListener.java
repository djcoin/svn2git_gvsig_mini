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

package es.prodevelop.gvsig.mini.search;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.tasks.poi.BookmarkManagerTask;
import es.prodevelop.gvsig.mini.util.ActionItem;
import es.prodevelop.gvsig.mini.util.QuickAction;

public class BookmarkClickListener extends POIItemClickContextListener {

	public BookmarkClickListener(SearchActivity activity, Drawable icon,
			int titleId) {
		super(activity, icon, titleId, true);
		// TODO Auto-generated constructor stub
	}

	public BookmarkClickListener(Activity activity, int iconId, int titleId) {
		super(activity, iconId, titleId, true);
		// TODO Auto-generated constructor stub
	}

	public void addBookmark(final POI p, final QuickAction qa) {
		final ActionItem bookmarkItem = new ActionItem();

		bookmarkItem.setTitle(activity.getResources().getString(
				R.string.remove_bookmark));
		bookmarkItem.setIcon(activity.getResources().getDrawable(
				R.drawable.bt_star_remove_s));
		bookmarkItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// bookmark
					processBookmark(p);
					qa.dismiss();
				} catch (Exception e) {
					Log.e("", "Error on streets near");
				}
			}
		});

		qa.addActionItem(bookmarkItem);
	}

	public void processBookmark(POI p) {
		try {
			final BookmarkManagerTask b = new BookmarkManagerTask(p);
			if (b.removeBookmark()) {
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.bookmark_removed), Toast.LENGTH_LONG)
						.show();
				((TextWatcher) activity).onTextChanged("", 0, 0, 0);
			} else
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.bookmark_failed), Toast.LENGTH_LONG)
						.show();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
}
