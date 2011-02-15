package es.prodevelop.gvsig.mini.tasks.poi;

import android.content.Context;
import android.util.Log;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class CommonPOITask extends Functionality {

	private POI p;
	private Context context;
	private int task = -1;

	public final static int FIND_POI = 0;
	public final static int FIND_STREET = 1;
	public final static int CALL = 2;
	public final static int WIKI = 3;
	public final static int WEB = 4;

	/**
	 * 
	 * @param map
	 * @param id
	 * @param p
	 * @param c
	 * @param task
	 */
	public CommonPOITask(Map map, int id, POI p, Context c, int task) {
		super(map, id);
		this.p = p;
		this.context = c;
		this.task = task;
	}

	@Override
	public boolean execute() {
		try {
			double[] coords = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS(getMap().osmap.getMRendererInfo()
							.getSRS()), CRSFactory.getCRS("EPSG:4326"));
			String pnt = new Point(coords[0], coords[1]).toShortString(6);
			switch (task) {
			case FIND_POI:
				// FIND POIS NEAR
				InvokeIntents.findPOISNear(context, pnt);
				break;
			case FIND_STREET:
				// FIND STREETS
				InvokeIntents.findStreetsNear(context, pnt);
				break;
			case CALL:
				InvokeIntents.invokeCallActivity(context,
						((OsmPOI) p).getPhone());
				break;
			case WIKI:
				InvokeIntents.openBrowser(context, ((OsmPOI) p).getWikipedia());
				break;
			case WEB:
				InvokeIntents.openBrowser(context, ((OsmPOI) p).getWebsite());
				break;
			}
		} catch (Exception e) {
			if (e != null && e.getMessage() != null) {
				Log.e("", e.getMessage());
			}
			return false;
		}
		return true;
	}

	@Override
	public int getMessage() {
		return TaskHandler.FINISHED;
	}

}
