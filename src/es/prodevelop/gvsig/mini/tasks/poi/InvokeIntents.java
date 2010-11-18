package es.prodevelop.gvsig.mini.tasks.poi;

import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.search.activities.FindPOISNearActivity;
import es.prodevelop.gvsig.mini.search.activities.FindStreetsNearActivity;
import es.prodevelop.gvsig.mini.search.activities.ResultSearchActivity;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.yours.RouteManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class InvokeIntents {

	public static void invokeCallActivity(Context context, String phone) {
		try {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("tel:" + phone));
			context.startActivity(intent);
		} catch (Exception e) {
			Log.e("", "Failed to invoke call", e);
		}
	}

	public static void invokeURI(Context context, String uri) {
		Intent intent = new Intent();
		intent.setData(Uri.parse(uri));
		intent.setAction("android.intent.action.VIEW");
		context.startActivity(intent);
	}

	public static void openBrowser(Context context, String url) {
		if (url == null)
			return;
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			url = "http://" + url;
		Intent browserIntent = new Intent("android.intent.action.VIEW",
				Uri.parse(url));
		context.startActivity(browserIntent);
	}

	public static void routeModified(Context context) {
		Intent i = new Intent(context, Map.class);
		i.putExtra(RouteManager.ROUTE_MODIFIED, true);
		context.startActivity(i);
	}

	public static void findPOISNear(Context context, String point) {
		Intent i = new Intent(context, FindPOISNearActivity.class);
		i.putExtra(ResultSearchActivity.QUERY, point);
		i.putExtra(SearchActivity.HIDE_AUTOTEXTVIEW, true);
		context.startActivity(i);
	}

	public static void findStreetsNear(Context context, String point) {
		Intent i = new Intent(context, FindStreetsNearActivity.class);
		i.putExtra(ResultSearchActivity.QUERY, point);
		i.putExtra(SearchActivity.HIDE_AUTOTEXTVIEW, true);
		context.startActivity(i);
	}

}
