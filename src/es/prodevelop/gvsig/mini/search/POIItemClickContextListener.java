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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.tasks.poi.BookmarkManagerTask;
import es.prodevelop.gvsig.mini.tasks.poi.InvokeIntents;
import es.prodevelop.gvsig.mini.tasks.poi.ShareAnyPOITask;
import es.prodevelop.gvsig.mini.tasks.poi.ShowGMapsFromPoint;
import es.prodevelop.gvsig.mini.tasks.poi.ShowStreetViewFromPoint;
import es.prodevelop.gvsig.mini.utiles.Tags;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mini.yours.Route;
import es.prodevelop.gvsig.mini.yours.RouteManager;

public class POIItemClickContextListener {

	Activity activity;
	int iconId;
	int titleId;
	Drawable icon;
	AlertDialog dialog;
	BulletedTextListAdapter adapter;
	private boolean findNear = false;

	public POIItemClickContextListener(Activity activity, int iconId,
			int titleId, boolean findNear) {
		this.activity = activity;
		this.iconId = iconId;
		this.titleId = titleId;
		this.findNear = findNear;
	}

	public POIItemClickContextListener(SearchActivity activity, Drawable icon,
			int titleId, boolean findNear) {
		this.activity = activity;
		this.icon = icon;
		this.titleId = titleId;
		this.findNear = findNear;
	}

	public boolean onPOIClick(int position, final POI p) {
		try {
			// ((LazyAdapter) getListAdapter()).pos = arg2;
			// ((LazyAdapter) getListAdapter()).notifyDataSetChanged();
			if (p.getX() == 0 && p.getY() == 0)
				return true;

			final AlertDialog.Builder alertPOI = new AlertDialog.Builder(
					activity);

			alertPOI.setCancelable(true);
			if (p instanceof OsmPOI)
				icon = POICategoryIcon.getDrawable32ForCategory(
						((OsmPOI) p).getCategory(), activity);
			else
				icon = POICategoryIcon.getDrawable32ForCategory(
						POICategories.STREETS, activity);

			if (icon != null)
				alertPOI.setIcon(icon);
			else
				alertPOI.setIcon(iconId);
			alertPOI.setTitle(Utilities.capitalizeFirstLetters(p
					.getDescription()));

			final ListView lv = new ListView(activity);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						final int position, long arg3) {
					try {
						if (dialog != null)
							dialog.dismiss();
						switch (position) {
						case 0:
							// SHOW ON MAP
							Intent i = new Intent(activity, Map.class);
							i.putExtra("zoom", 15);
							i.putExtra("lon", p.getX());
							i.putExtra("lat", p.getY());
							// ShowGMapsFromPoint sg = new ShowGMapsFromPoint(
							// activity, p);
							// sg.execute();
							activity.startActivity(i);
							activity.finish();
							break;
						case 1:
							// FROM HERE
							RouteManager.getInstance().getRegisteredRoute()
									.setStartPoint(p);
							Toast.makeText(
									activity,
									activity.getResources().getString(
											R.string.start_point),
									Toast.LENGTH_LONG).show();
							checkCalculateRoute();
							break;
						case 2:
							// TO HERE
							RouteManager.getInstance().getRegisteredRoute()
									.setEndPoint(p);
							Toast.makeText(
									activity,
									activity.getResources().getString(
											R.string.end_point),
									Toast.LENGTH_LONG).show();
							checkCalculateRoute();
							break;
						case 3:
							// BOOKMARK
							processBookmark(p);
							break;
						case 4:
							// GEO SHARE
							ShareAnyPOITask sh = new ShareAnyPOITask(activity,
									p);
							sh.execute();
							break;
						case 5:
							if (findNear)
							// FIND POIS NEAR
							InvokeIntents.findPOISNear(activity,
									p.toShortString(2));
							else {
								ShowStreetViewFromPoint s = new ShowStreetViewFromPoint(
										activity, p);
								s.execute();
							}
							break;
						case 6:
							if (findNear)
							// FIND STREETS
							InvokeIntents.findStreetsNear(activity,
									p.toShortString(2));
							else {
								ShowStreetViewFromPoint s = new ShowStreetViewFromPoint(
										activity, p);
								s.execute();
							}	
							break;

						case 7:
							// STREET VIEW
							ShowStreetViewFromPoint s = new ShowStreetViewFromPoint(
									activity, p);
							s.execute();
							break;
						// OPTIONAL
						case 8:
							// CALL NUMBER
							if (p instanceof OsmPOI) {
								OsmPOI poi = (OsmPOI) p;
								if (poi.getPhone() != null
										&& poi.getPhone().length() > 0) {
									InvokeIntents.invokeCallActivity(activity,
											poi.getPhone());
								} else if (poi.getWikipedia() != null
										&& poi.getWikipedia().length() > 0) {
									InvokeIntents.openBrowser(activity,
											poi.getWikipedia());
								} else {
									InvokeIntents.openBrowser(activity,
											poi.getWebsite());
								}
							}
							break;
						case 9:
							// WIKI
							if (p instanceof OsmPOI) {
								OsmPOI poi = (OsmPOI) p;
								if (poi.getWikipedia() != null
										&& poi.getWikipedia().length() > 0) {
									if (poi.getWebsite() != null
											&& poi.getWebsite().length() > 0) {
										InvokeIntents.openBrowser(activity,
												poi.getWebsite());
									} else {
										InvokeIntents.openBrowser(activity,
												poi.getWikipedia());
									}
								} else {
									InvokeIntents.openBrowser(activity,
											poi.getWebsite());
								}
							}
							break;
						case 10:
							// WEB
							if (p instanceof OsmPOI) {
								OsmPOI poi = (OsmPOI) p;
								InvokeIntents.openBrowser(activity,
										poi.getWebsite());
							}
							break;

						}
					} catch (Exception e) {
						// log.log(Level.SEVERE,"",e);
					}
				}
			});

			adapter = new BulletedTextListAdapter(activity);

			adapter.addItem(new BulletedText(activity.getResources().getString(
					R.string.show_map), activity.getResources().getDrawable(
					R.drawable.bt_poi)));

			adapter.addItem(new BulletedText(activity.getResources().getString(
					R.string.NameFinderActivity_1), activity.getResources()
					.getDrawable(R.drawable.bt_start)));

			adapter.addItem(new BulletedText(activity.getResources().getString(
					R.string.NameFinderActivity_2), activity.getResources()
					.getDrawable(R.drawable.bt_finish)));

			addBookmark();

			adapter.addItem(new BulletedText(activity.getResources().getString(
					R.string.share), activity.getResources().getDrawable(
					R.drawable.twitter_button)));

			if (findNear) {
				adapter.addItem(new BulletedText(activity.getResources()
						.getString(R.string.find_pois_near), activity
						.getResources().getDrawable(R.drawable.bt_poi)));

				// FIND STREETS
				adapter.addItem(new BulletedText(activity.getResources()
						.getString(R.string.find_streets_near), activity
						.getResources().getDrawable(R.drawable.bt_poi)));
			}

			adapter.addItem(new BulletedText(activity.getResources().getString(
					R.string.street_view), activity.getResources().getDrawable(
					R.drawable.bt_streetview)));

			if (p instanceof OsmPOI) {
				OsmPOI poi = (OsmPOI) p;
				if (poi.getPhone() != null && poi.getPhone().length() > 0)
					adapter.addItem(new BulletedText(activity.getResources()
							.getString(R.string.call_number)
							+ " ["
							+ poi.getPhone() + "]", activity.getResources()
							.getDrawable(R.drawable.bt_poi)));

				// WIKI
				if (poi.getWikipedia() != null
						&& poi.getWikipedia().length() > 0)
					adapter.addItem(new BulletedText(activity.getResources()
							.getString(R.string.show_wiki)
							+ " ["
							+ poi.getWikipedia() + "]", activity.getResources()
							.getDrawable(R.drawable.bt_wikipedia)));

				// WEB
				if (poi.getWebsite() != null && poi.getWebsite().length() > 0)
					adapter.addItem(new BulletedText(activity.getResources()
							.getString(R.string.show_web)
							+ " ["
							+ poi.getWebsite() + "]", activity.getResources()
							.getDrawable(R.drawable.bt_openbrowser)));
			}

			lv.setAdapter(adapter);

			alertPOI.setView(lv);

			alertPOI.setNegativeButton(R.string.back,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			dialog = alertPOI.show();

		} catch (Exception ex) {
			Log.e("POIItem", ex.getMessage());
		}

		return true;
	}

	// String uri = "google.streetview:cbll=" +
	// getMap().osmap.getCenterLonLat()[1] + ","
	// + getMap().osmap.getCenterLonLat()[0]
	// + "&cbp=1,45,,45,1.0&mz=" + getMap().osmap.getZoomLevel();
	// Intent intent = new Intent();
	// intent.setData(Uri.parse(uri));
	// intent.setAction("android.intent.action.VIEW");
	// getMap().startActivity(intent);

	private void checkCalculateRoute() {
		final Route r = RouteManager.getInstance().getRegisteredRoute();
		if (r == null)
			return;

		if (r.getState() == Tags.ROUTE_WITH_2_POINT) {
			showRouteOKDialog();
		}
	}

	private void showRouteOKDialog() {

		try {

			AlertDialog.Builder alert = new AlertDialog.Builder(activity);

			alert.setTitle(R.string.info);
			TextView text = new TextView(activity);
			text.setText("          "
					+ activity.getResources().getString(
							R.string.calculate_route));
			alert.setView(text);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								InvokeIntents.routeModified(activity);
								activity.finish();
							} catch (Exception e) {

							}
						}
					});

			alert.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});

			dialog = alert.show();
		} catch (Exception e) {

		}
	}

	public void addBookmark() {
		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.bookmark), activity.getResources().getDrawable(
				R.drawable.bt_star_add)));
	}

	public void processBookmark(POI p) {
		try {
			BookmarkManagerTask b = new BookmarkManagerTask(p);
			if (b.addBookmark())
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.bookmark_added), Toast.LENGTH_LONG)
						.show();
			else
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.bookmark_exists), Toast.LENGTH_LONG)
						.show();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
}
