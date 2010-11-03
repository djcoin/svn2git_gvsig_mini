package es.prodevelop.gvsig.mini.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;

public class POIItemLongClickListener implements OnItemLongClickListener {

	SearchActivity activity;

	public POIItemLongClickListener(SearchActivity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// ((LazyAdapter) getListAdapter()).pos = arg2;
		// ((LazyAdapter) getListAdapter()).notifyDataSetChanged();

		AlertDialog.Builder alertPOI = new AlertDialog.Builder(activity);

		alertPOI.setCancelable(true);
		alertPOI.setIcon(R.drawable.pois);
		alertPOI.setTitle(R.string.NameFinderActivity_0);

		final ListView lv = new ListView(activity);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				try {
					Intent mIntent;
					mIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("selected", position);
					mIntent.putExtras(bundle);
					switch (position) {
					case 0:
						// log.log(Level.FINE, "setResult = 0");
						activity.setResult(0, mIntent);
						activity.finish();

						break;
					case 1:
						// log.log(Level.FINE, "setResult = 1");
						activity.setResult(1, mIntent);
						activity.finish();
						break;
					case 2:
						// log.log(Level.FINE, "setResult = 2");
						activity.setResult(2, mIntent);
						activity.finish();
						break;
					case 3:
						// TODO LANZAR OTRA ACTIVITY CON LOS DETALLES

						break;
					}
				} catch (Exception e) {
					// log.log(Level.SEVERE,"",e);
				}
			}

		});

		BulletedTextListAdapter adapter = new BulletedTextListAdapter(activity);

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.NameFinderActivity_1), activity.getResources()
				.getDrawable(R.drawable.pinpoi)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.NameFinderActivity_2), activity.getResources()
				.getDrawable(R.drawable.pinoutpoi)));

		adapter.addItem(new BulletedText(activity.getResources().getString(
				R.string.NameFinderActivity_3), activity.getResources()
				.getDrawable(R.drawable.pois)));

		lv.setAdapter(adapter);

		alertPOI.setView(lv);

		alertPOI.setNegativeButton(R.string.back,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alertPOI.show();

		return true;
	}
}
