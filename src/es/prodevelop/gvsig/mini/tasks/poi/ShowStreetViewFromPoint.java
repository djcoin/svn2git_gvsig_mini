package es.prodevelop.gvsig.mini.tasks.poi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import es.prodevelop.gvsig.mini.geom.Point;

/**
 * Launches the URI for the street view activity.
 * 
 * @author aromeu
 * 
 */
public class ShowStreetViewFromPoint {

	private Activity activity;
	private Point p;
	private final static int DEFAULT_ZOOM = 17;

	/**
	 * 
	 * @param activity
	 * @param p
	 *            lon lat point
	 */
	public ShowStreetViewFromPoint(Activity activity, Point p) {
		this.activity = activity;
		this.p = p;
	}

	/**
	 * Launches street view
	 */
	public void execute() {
		try {
			String uri = "google.streetview:cbll=" + p.getY() + "," + p.getX()
					+ "&cbp=1,45,,45,1.0&mz=" + DEFAULT_ZOOM;
			InvokeIntents.invokeURI(activity, uri);
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}
}
