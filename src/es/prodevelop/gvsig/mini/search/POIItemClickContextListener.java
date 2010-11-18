package es.prodevelop.gvsig.mini.search;

import java.util.logging.Level;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import es.prodevelop.gvsig.mini.activities.LogFeedbackActivity;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
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

	public POIItemClickContextListener(Activity activity, int iconId,
			int titleId) {
		this.activity = activity;
		this.iconId = iconId;
		this.titleId = titleId;
	}

	public POIItemClickContextListener(SearchActivity activity, Drawable icon,
			int titleId) {
		this.activity = activity;
		this.icon = icon;
		this.titleId = titleId;
	}

	public boolean onPOIClick(int position, final POI p) {
		// ((LazyAdapter) getListAdapter()).pos = arg2;
		// ((LazyAdapter) getListAdapter()).notifyDataSetChanged();

		final AlertDialog.Builder alertPOI = new AlertDialog.Builder(activity);

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
		alertPOI.setTitle(Utilities.capitalizeFirstLetters(p.getDescription()));

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
						ShowGMapsFromPoint sg = new ShowGMapsFromPoint(
								activity, p);
						sg.execute();
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
										R.string.end_point), Toast.LENGTH_LONG)
								.show();
						checkCalculateRoute();
						break;
					case 3:
						// BOOKMARK
						break;
					case 4:
						// GEO SHARE
						ShareAnyPOITask sh = new ShareAnyPOITask(activity, p);
						sh.execute();
						break;
					case 5:
						// FIND POIS NEAR
						InvokeIntents.findPOISNear(activity, p.toShortString(2));
						break;
					case 6:
						// FIND STREETS
						InvokeIntents.findStreetsNear(activity,
								p.toShortString(2));
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

		BulletedTextListAdapter adapter = new BulletedTextListAdapter(activity);

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.show_map), activity.getResources().getDrawable(
				R.drawable.bt_poi)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.NameFinderActivity_1), activity.getResources()
				.getDrawable(R.drawable.bt_start)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.NameFinderActivity_2), activity.getResources()
				.getDrawable(R.drawable.bt_finish)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.bookmark), activity.getResources().getDrawable(
				R.drawable.bt_poi)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.share), activity.getResources().getDrawable(
				R.drawable.twitter_button)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.find_pois_near), activity.getResources().getDrawable(
				R.drawable.bt_poi)));

		// FIND STREETS
		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.find_streets_near), activity.getResources()
				.getDrawable(R.drawable.bt_poi)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.street_view), activity.getResources().getDrawable(
				R.drawable.bt_poi)));

		if (p instanceof OsmPOI) {
			OsmPOI poi = (OsmPOI) p;
			if (poi.getPhone() != null && poi.getPhone().length() > 0)
				adapter.addItem(new BulletedText(activity.getResources()
						.getString(R.string.call_number)
						+ " ["
						+ poi.getPhone() + "]", activity.getResources()
						.getDrawable(R.drawable.bt_poi)));

			// WIKI
			if (poi.getWikipedia() != null && poi.getWikipedia().length() > 0)
				adapter.addItem(new BulletedText(activity.getResources()
						.getString(R.string.show_wiki)
						+ " ["
						+ poi.getWikipedia() + "]", activity.getResources()
						.getDrawable(R.drawable.bt_poi)));

			// WEB
			if (poi.getWebsite() != null && poi.getWebsite().length() > 0)
				adapter.addItem(new BulletedText(activity.getResources()
						.getString(R.string.show_web)
						+ " ["
						+ poi.getWebsite()
						+ "]", activity.getResources().getDrawable(
						R.drawable.bt_poi)));
		}

		lv.setAdapter(adapter);

		alertPOI.setView(lv);

		alertPOI.setNegativeButton(R.string.back,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		dialog = alertPOI.show();

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
}
