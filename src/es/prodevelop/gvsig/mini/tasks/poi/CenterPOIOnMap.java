package es.prodevelop.gvsig.mini.tasks.poi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.util.Utils;

public class CenterPOIOnMap extends Functionality {

	private Point p;
	private Context c;

	public CenterPOIOnMap(Map map, int id, Point p, Context c) {
		super(map, id);
		this.p = p;
		this.c = c;
	}

	@Override
	public boolean execute() {
		try {
			Intent i = new Intent(c, Utils.DEFAULT_MAP_CLASS);
			i.putExtra("zoom", 17);
			i.putExtra("lon", p.getX());
			i.putExtra("lat", p.getY());
			// ShowGMapsFromPoint sg = new ShowGMapsFromPoint(
			// activity, p);
			// sg.execute();
			c.startActivity(i);
		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public int getMessage() {
		return TaskHandler.FINISHED;
	}

}
