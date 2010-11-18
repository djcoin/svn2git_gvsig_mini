package es.prodevelop.gvsig.mini.tasks.poi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import es.prodevelop.gvsig.mini.geom.Point;

public class ShowGMapsFromPoint {

	private Activity activity;
	private Point p;
	private final static int DEFAULT_ZOOM = 17;

	/**
	 * 
	 * @param activity
	 * @param p
	 *            lon lat point
	 */
	public ShowGMapsFromPoint(Activity activity, Point p) {
		this.activity = activity;
		this.p = p;
	}

	/**
	 * Launches gmaps
	 */
	public void execute() {
		try {
			String uri = "geo:" + p.getY() + "," + p.getX() + "?z="
					+ DEFAULT_ZOOM;
			InvokeIntents.invokeURI(activity, uri);
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}

}
