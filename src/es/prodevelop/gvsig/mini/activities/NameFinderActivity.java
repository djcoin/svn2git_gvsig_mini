/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2009 Prodevelop.
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
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.activities;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;

/**
 * Activity to show the results of the NameFinder service. Consists on a ListActivity
 * with a BulletedTextAdapter that shows the description of every result and a thumbnail.
 * Clicking an item allows the user to select one of three options: Set the POI as the
 * start point of a route, as the end point or center the map on the POI. 
 */
public class NameFinderActivity extends ListActivity {

	private int pos = -1;
	private final static Logger log = Logger
			.getLogger(NameFinderActivity.class.getName());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
			log.log(Level.FINE, "onCreate NameFinder actvity");			
			super.onCreate(savedInstanceState);

			BulletedTextListAdapter itla = new BulletedTextListAdapter(this);

			String[] results = getIntent().getStringArrayExtra("test");

			final int size = results.length;
			log.log(Level.FINE, "Starting namefinder activity with " + size + " results");
			for (int i = 0; i < results.length; i++) {
				itla.addItem(new BulletedText(results[i], getResources()
						.getDrawable(R.drawable.pois)));
			}

			setListAdapter(itla);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			LogFeedbackActivity.showSendLogDialog(this);
		}
	}

	@Override
	public void onStop() {
		try {
			super.onStop();
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if (keyCode == KeyEvent.KEYCODE_SEARCH) {
				return true;
			}
			return super.onKeyDown(keyCode, event);
		} catch (Exception e) {			
			return false;
		}
	}

	@Override
	protected void onListItemClick(android.widget.ListView l,
			android.view.View v, int position, long id) {
		try {
			super.onListItemClick(l, v, position, id);
			pos = position;
			log.log(Level.FINE, "Click on position: " + pos);
			AlertDialog.Builder alertPOI = new AlertDialog.Builder(this);

			alertPOI.setCancelable(true);
			alertPOI.setIcon(R.drawable.pois);
			alertPOI.setTitle(R.string.NameFinderActivity_0);

			final ListView lv = new ListView(this);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					try {
						Intent mIntent;
						mIntent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putInt("selected", pos);
						mIntent.putExtras(bundle);
						switch (position) {
						case 0:
							log.log(Level.FINE, "setResult = 0");
							setResult(0, mIntent);
							finish();
							
							break;
						case 1:
							log.log(Level.FINE, "setResult = 1");
							setResult(1, mIntent);
							finish();
							break;
						case 2:
							log.log(Level.FINE, "setResult = 2");
							setResult(2, mIntent);
							finish();
							break;
						case 3:
							// TODO LANZAR OTRA ACTIVITY CON LOS DETALLES

							break;
						}
					} catch (Exception e) {
						log.log(Level.SEVERE,"",e);
					}
				}

			});

			BulletedTextListAdapter adapter = new BulletedTextListAdapter(this);

			adapter.addItem(new BulletedText(this.getResources().getString(R.string.NameFinderActivity_1), getResources()
					.getDrawable(R.drawable.pinpoi)));

			adapter.addItem(new BulletedText(this.getResources().getString(R.string.NameFinderActivity_2), getResources()
					.getDrawable(R.drawable.pinoutpoi)));

			adapter.addItem(new BulletedText(this.getResources().getString(R.string.NameFinderActivity_3), getResources()
					.getDrawable(R.drawable.pois)));

			lv.setAdapter(adapter);

			alertPOI.setView(lv);

			alertPOI.setNegativeButton(R.string.back,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alertPOI.show();
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	public static class BulletedTextView extends LinearLayout {

		public TextView mText;
		private ImageView mBullet;

		public BulletedTextView(Context context, String text, Object bullet) {
			super(context);
			try {
				this.setOrientation(HORIZONTAL);

				mBullet = new ImageView(context);				
				if (bullet instanceof Drawable)
					mBullet.setImageDrawable((Drawable) bullet);
				else
					mBullet.setImageBitmap((Bitmap) bullet);

				// left, top, right, bottom
				mBullet.setPadding(0, 2, 5, 0);
				addView(mBullet, new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				mText = new TextView(context);
				mText.setText(text);
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				lParams.gravity = Gravity.CENTER_VERTICAL;
				addView(mText, lParams);
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
			}
		}

		public void setText(String words) {
			try {
				mText.setText(words);
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
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
				log.log(Level.SEVERE,"",e);
			}
		}
	}

	public static class BulletedTextListAdapter extends BaseAdapter {

		/**
		 * Remember our context so we can use it when constructing views.
		 */
		private Context mContext;

		private List<BulletedText> mItems;

		public BulletedTextListAdapter(Context context) {
			try {
				mContext = context;
				mItems = new ArrayList<BulletedText>();
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
			}
		}

		public void addItem(BulletedText bt) {
			try {
				mItems.add(bt);
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
			}
		}

		public void setListItems(List<BulletedText> bti) {
			mItems = bti;
		}

		public int getCount() {
			try {
				return mItems.size();
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
				return 0;
			}
		}

		public Object getItem(int position) {
			try {
				return mItems.get(position);
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
				return null;
			}
		}

		public boolean areAllItemsSelectable() {
			return false;
		}

		public boolean isSelectable(int position) {
			try {
				return mItems.get(position).isSelectable();
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
				return false;
			}
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a BulletedTextView to hold each row.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				BulletedTextView btv;

//				AnimationSet set = new AnimationSet(true);
//
//				Animation animation = new AlphaAnimation(0.0f, 1.0f);
//				animation.setDuration(100);
//				set.addAnimation(animation);
//
//				animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//						0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//						Animation.RELATIVE_TO_SELF, -1.0f,
//						Animation.RELATIVE_TO_SELF, 0.0f);
//				animation.setDuration(500);
//
//				set.addAnimation(animation);
//
//				LayoutAnimationController controller = new LayoutAnimationController(
//						set, 0.25f);

				if (convertView == null) {
					btv = new BulletedTextView(mContext, mItems.get(position)
							.getText(), mItems.get(position).getBullet());
				} else {
					btv = (BulletedTextView) convertView;
					btv.setText(mItems.get(position).getText());
					btv.setBullet(mItems.get(position).getBullet());
				}
//				btv.setLayoutAnimation(controller);
				return btv;

			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
				return null;
			}
		}
	}

	public static class BulletedText {

		private String mText = "";
		private Object mIcon;
		private boolean mSelectable = true;

		public BulletedText(String text, Object bullet) {
			mIcon = bullet;
			mText = text;
		}

		public String getText() {
			return this.mText;
		}

		public Object getBullet() {
			return this.mIcon;
		}

		public BulletedText setSelectable(boolean selectable) {
			this.mSelectable = selectable;
			return this;
		}

		public boolean isSelectable() {
			return this.mSelectable;
		}

		public static Bitmap getRemoteImage(URL aURL) {
			try {
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
				return bm;
			} catch (IOException e) {
				log.log(Level.SEVERE,"",e);
				return null;
			} catch (OutOfMemoryError e) {
				System.gc();
				System.gc();
				log.log(Level.SEVERE,"",e);
				return null;
			}
		}
	}
}
