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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.util.ResourceLoader;

public class CategoriesListView extends ListView {
	
	CheckBoxBulletAdapter adapter;

	public CategoriesListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public CategoriesListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public CategoriesListView(Context context) {
		super(context);
		initialize();
	}

	public void initialize() {
		this.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		final int size = POICategories.ORDERED_CATEGORIES.length;

		String[] list = getResources().getStringArray(R.array.poi_categories);

//		int[] imgs = getResources().getIntArray(R.array.poi_icons);
		 int[] imgs = new int[list.length];
		// // TRANSPORTATION, TOURISM, RECREATION, FOOD,
		// // PUBLIC_BUILDINGS, ARTS_CULTURE, SHOPS, HEALTH_EMERGENCY,
		// // ACCOMODATION, ROUTE, PLACES };
//		 imgs[0] = -1;
		 imgs[0] = R.drawable.p_transportation_transport_bus_stop_32;
		 imgs[1] = R.drawable.p_tourism_tourist_attraction_32;
		 imgs[2] = R.drawable.p_recreation_sport_playground_32;
		 imgs[3] = R.drawable.p_food_restaurant_32;
		 imgs[4] = R.drawable.p_public_buildings_tourist_monument_32;
		 imgs[5] = R.drawable.p_arts_culture_tourist_theatre_32;
		 imgs[6] = R.drawable.p_shops_shopping_supermarket_32;
		 imgs[7] = R.drawable.p_health_hospital_32;
		 imgs[8] = R.drawable.p_accommodation_hotel_32;
		 imgs[9] = R.drawable.p_route_tourist_castle2_32;
		 imgs[10] = R.drawable.p_places_poi_place_city_32;

		adapter = new CheckBoxBulletAdapter(list, imgs);
		this.setAdapter(adapter);
	}
	
	public ArrayList getCheckedCategories() {
		try {
			final CheckBoxBulletAdapter adapter = this.adapter;
			boolean[] selected = adapter.selected;
			
			final int size = selected.length;
			
			ArrayList list = new ArrayList();
			for (int i = 0; i<size; i++) {
				if (selected[i])
					list.add(POICategories.CATEGORIES.get(i).toString().toLowerCase());
			}
			return list;
		} catch (Exception e) {
			Log.e("", e.getMessage());
			return null;
		}		
	}

	protected class CheckBoxBulletAdapter extends BaseAdapter {

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

//				if (convertView == null) {
					btv = new BulletedCheckBox(
							CategoriesListView.this.getContext(),
							this.texts[pos],
							ResourceLoader.getBitmap(idsImages[pos]));
//				} else {
//					btv = (BulletedCheckBox) convertView;
//					btv.setText(this.texts[position]);
//					btv.setBullet(ResourceLoader.getBitmap(idsImages[position]));
//				}

				// final ImageView img = new ImageView(
				// CategoriesListView.this.getContext());
				// img.setAdjustViewBounds(true);

				btv.setClickable(true);
				btv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {							
							selected[pos] = !selected[pos];
							POICategories.selected = getCheckedCategories();
							btv.setChecked(selected[pos]);
						} catch (Exception e) {
							// log.log(Level.SEVERE,"",e);
						}
					}
				});

				btv.setChecked(selected[pos]);

				mainView.addView(btv, 0);
				// mainView.addView(img, 1);

			} catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
			}
			return mainView;
		}
	}

	public static class BulletedCheckBox extends LinearLayout {

		public CheckBox mCheck;
		private ImageView mBullet;

		/* (non-Javadoc)
		 * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
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
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
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
