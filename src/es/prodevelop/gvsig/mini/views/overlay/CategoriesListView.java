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
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.search.MapPreview;
import es.prodevelop.gvsig.mini.search.view.PinnedHeaderListView;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

public class CategoriesListView extends PinnedHeaderListView {

	CheckBoxBulletAdapter adapter;

	private boolean mDisplaySectionHeaders = true;

	private MapPreview preview;
	private LinearLayout lastPreviewLayout;
	private Context context;

	String[] sections;
	int[] sectionsPos;
	int totalLength;
	HashMap<String, Integer> indexer = new HashMap<String, Integer>();

	/**
	 * An approximation of the background color of the pinned header. This color
	 * is used when the pinned header is being pushed up. At that point the
	 * header "fades away". Rather than computing a faded bitmap based on the
	 * 9-patch normally used for the background, we will use a solid color,
	 * which will provide better performance and reduced complexity.
	 */
	private int mPinnedHeaderBackgroundColor = 0xff202020;

	public CategoriesListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initialize();
	}

	public CategoriesListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initialize();
	}

	public CategoriesListView(Context context) {
		super(context);
		this.context = context;
		initialize();
	}

	public void initialize() {
		this.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		String[] list = getResources().getStringArray(R.array.poi_categories);

		// int[] imgs = getResources().getIntArray(R.array.poi_icons);
		int[] imgs = new int[list.length];
		// // TRANSPORTATION, TOURISM, RECREATION, FOOD,
		// // PUBLIC_BUILDINGS, ARTS_CULTURE, SHOPS, HEALTH_EMERGENCY,
		// // ACCOMODATION, ROUTE, PLACES };
		// imgs[0] = -1;
		sections = new String[] { context.getString(R.string.poi_all),
				context.getString(R.string.poi_layers),
				context.getString(R.string.poi_categories) };
		sectionsPos = new int[] { 0, 1, 3 };
		indexer.put(context.getString(R.string.poi_all), 0);
		indexer.put(context.getString(R.string.poi_layers), 1);
		indexer.put(context.getString(R.string.poi_categories), 3);
		imgs[0] = -1;
		imgs[1] = -1;
		imgs[2] = -1;
		imgs[3] = R.drawable.p_transportation_transport_bus_stop_32;
		imgs[4] = R.drawable.p_tourism_tourist_attraction_32;
		imgs[5] = R.drawable.p_recreation_sport_playground_32;
		imgs[6] = R.drawable.p_food_restaurant_32;
		imgs[7] = R.drawable.p_public_buildings_tourist_monument_32;
		imgs[8] = R.drawable.p_arts_culture_tourist_theatre_32;
		imgs[9] = R.drawable.p_shops_shopping_supermarket_32;
		imgs[10] = R.drawable.p_health_hospital_32;
		imgs[11] = R.drawable.p_accommodation_hotel_32;
		imgs[12] = R.drawable.p_route_tourist_castle2_32;
		imgs[13] = R.drawable.p_places_poi_place_city_32;

		totalLength = 14;

		adapter = new CheckBoxBulletAdapter(list, imgs);
		this.setAdapter(adapter);
	}

	public ArrayList getCheckedCategories() {
		try {
			final CheckBoxBulletAdapter adapter = this.adapter;
			boolean[] selected = adapter.selected;

			final int size = selected.length;

			ArrayList list = new ArrayList();
			for (int i = 3; i < size; i++) {
				if (selected[i])
					list.add(POICategories.CATEGORIES.get(i - 3).toString()
							.toLowerCase());
			}
			
			POICategories.resultSearchSelected = selected[1];
			POICategories.bookmarkSelected = selected[2];
			
			return list;
		} catch (Exception e) {
			Log.e("", e.getMessage());
			return null;
		}
	}

	protected class CheckBoxBulletAdapter extends BaseAdapter implements
			PinnedHeaderListView.PinnedHeaderAdapter, OnScrollListener,
			SectionIndexer {

		String[] texts;
		int[] idsImages;
		boolean[] selected;

		public CheckBoxBulletAdapter(String[] texts, int[] idsImages) {
			try {
				this.texts = texts;
				this.idsImages = idsImages;
				selected = new boolean[texts.length];
			} catch (Exception e) {

			}
		}

		@Override
		public int getCount() {
			try {
				return texts.length;
			} catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LinearLayout mainView = new LinearLayout(
					CategoriesListView.this.getContext());
			mainView.setOrientation(LinearLayout.VERTICAL);
			try {
				final BulletedCheckBox btv;
				final int pos = position;

				// if (convertView == null) {
				btv = new BulletedCheckBox(
						CategoriesListView.this.getContext(), this.texts[pos],
						ResourceLoader.getBitmap(idsImages[pos]));
				// } else {
				// btv = (BulletedCheckBox) convertView;
				// btv.setText(this.texts[position]);
				// btv.setBullet(ResourceLoader.getBitmap(idsImages[position]));
				// }

				// final ImageView img = new ImageView(
				// CategoriesListView.this.getContext());
				// img.setAdjustViewBounds(true);

				btv.setClickable(true);
				btv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							selected[pos] = !selected[pos];
							if (pos == 0) {
								for (int i = 0; i < selected.length; i++) {
									selected[i] = selected[pos];
								}
							} else {
								selected[0] = false;
							}
							POICategories.selected = getCheckedCategories();
							btv.setChecked(selected[pos]);
							notifyDataSetChanged();
						} catch (Exception e) {
							// log.log(Level.SEVERE,"",e);
						}
					}
				});

				btv.setChecked(selected[pos]);

				int realPosition = getRealPosition(position);

				bindSectionHeader(mainView, realPosition,
						mDisplaySectionHeaders);
				mainView.addView(btv, 1);
				// mainView.addView(img, 1);

			} catch (Exception e) {
				Log.e("", e.getMessage());
				// log.log(Level.SEVERE,"",e);
			}
			return mainView;
		}

		private void bindSectionHeader(View mainView, int position,
				boolean displaySectionHeaders) {
			// final LinearLayout view = (LinearLayout) itemView;
			TextView t = new TextView(context, null,
					android.R.attr.listSeparatorTextViewStyle);

			if (!displaySectionHeaders) {
				t.setVisibility(View.GONE);
				// view.setDividerVisible(true);
			} else {
				final int section = getSectionForPosition(position);
				if (getPositionForSection(section) == position) {
					String title = (String) getSections()[section];
					// view.setSectionHeader(title);
					t.setText(title);
					t.setVisibility(View.VISIBLE);
				} else {
					t.setVisibility(View.GONE);
					// view.setDividerVisible(false);
					// view.setSectionHeader(null);
				}
			}
			((LinearLayout) mainView).addView(t, 0);
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (view instanceof PinnedHeaderListView) {
				((PinnedHeaderListView) view)
						.configureHeaderView(firstVisibleItem);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		/**
		 * Computes the state of the pinned header. It can be invisible, fully
		 * visible or partially pushed up out of the view.
		 */
		public int getPinnedHeaderState(int position) {

			int realPosition = getRealPosition(position);
			if (realPosition < 0) {
				return PINNED_HEADER_GONE;
			}

			// The header should get pushed up if the top item shown
			// is the last item in a section for a particular letter.
			int section = getSectionForPosition(realPosition);
			int nextSectionPosition = getPositionForSection(section + 1);
			if (nextSectionPosition != -1
					&& realPosition == nextSectionPosition - 1) {
				return PINNED_HEADER_PUSHED_UP;
			}

			return PINNED_HEADER_VISIBLE;
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			PinnedHeaderCache cache = (PinnedHeaderCache) header.getTag();
			if (cache == null) {
				cache = new PinnedHeaderCache();
				cache.titleView = (TextView) header
						.findViewById(R.id.section_text);
				cache.textColor = cache.titleView.getTextColors();
				cache.background = header.getBackground();
				header.setTag(cache);
			}

			int realPosition = getRealPosition(position);
			int section = getSectionForPosition(realPosition);

			if (section < 0) {
				section = 0;
				mDisplaySectionHeaders = false;
			} else {
				mDisplaySectionHeaders = true;
			}

			String title = (String) getSections()[section];
			cache.titleView.setText(title);

			if (alpha == 255) {
				// Opaque: use the default background, and the original text
				// color
				header.setBackgroundDrawable(cache.background);
				cache.titleView.setTextColor(cache.textColor);
			} else {
				// Faded: use a solid color approximation of the background, and
				// a translucent text color
				header.setBackgroundColor(Color.rgb(
						Color.red(mPinnedHeaderBackgroundColor) * alpha / 255,
						Color.green(mPinnedHeaderBackgroundColor) * alpha / 255,
						Color.blue(mPinnedHeaderBackgroundColor) * alpha / 255));

				int textColor = cache.textColor.getDefaultColor();
				cache.titleView.setTextColor(Color.argb(alpha,
						Color.red(textColor), Color.green(textColor),
						Color.blue(textColor)));
			}
		}

		public int getRealPosition(int pos) {
			return pos;
		}

		@Override
		public int getPositionForSection(int section) {
			// Log.v("getPositionForSection", ""+section);
			if (sections == null)
				return -1;
			if (section < 0 || section >= sections.length) {
				return -1;
			}
			String letter = sections[section];
			if (letter == null)
				return -1;

			return indexer.get(letter);
		}

		@Override
		public int getSectionForPosition(int position) {
			if (position < 0 || position >= totalLength) {
				return -1;
			}

			int index = Arrays.binarySearch(sectionsPos, position);

			/*
			 * Consider this example: section positions are 0, 3, 5; the
			 * supplied position is 4. The section corresponding to position 4
			 * starts at position 3, so the expected return value is 1. Binary
			 * search will not find 4 in the array and thus will return
			 * -insertPosition-1, i.e. -3. To get from that number to the
			 * expected value of 1 we need to negate and subtract 2.
			 */
			return index >= 0 ? index : -index - 2;

		}

		@Override
		public Object[] getSections() {
			if (sections == null)
				return new String[] { "" };
			return sections; // to string will be called each object, to display
			// the letter
		}
	}

	final static class PinnedHeaderCache {
		public TextView titleView;
		public ColorStateList textColor;
		public Drawable background;
	}

	public static class BulletedCheckBox extends LinearLayout {

		public CheckBox mCheck;
		private ImageView mBullet;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.view.View#setOnClickListener(android.view.View.OnClickListener
		 * )
		 */
		@Override
		public void setOnClickListener(OnClickListener l) {
			if (mCheck != null)
				mCheck.setOnClickListener(l);
		}

		public BulletedCheckBox(Context context, String text, Object bullet) {
			super(context);
			try {
				this.setOrientation(HORIZONTAL);

				mBullet = new ImageView(context);
				if (bullet != null) {
					if (bullet instanceof Drawable)
						mBullet.setImageDrawable((Drawable) bullet);
					else
						mBullet.setImageBitmap((Bitmap) bullet);

					// left, top, right, bottom
					mBullet.setPadding(0, 5, 5, 0);

					addView(mBullet, new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
				}

				mCheck = new CheckBox(context);
				mCheck.setText(text);
				addView(mCheck, new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			} catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
			}
		}

		public void setText(String words) {
			try {
				mCheck.setText(words);
			} catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
			}
		}

		public void setChecked(boolean checked) {
			try {
				mCheck.setChecked(checked);
			} catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
			}
		}

		public void setBullet(Object bullet) {
			try {
				if (bullet == null)
					return;
				if (bullet instanceof Drawable)
					mBullet.setImageDrawable((Drawable) bullet);
				else
					mBullet.setImageBitmap((Bitmap) bullet);
			} catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
			}
		}
	}

}
