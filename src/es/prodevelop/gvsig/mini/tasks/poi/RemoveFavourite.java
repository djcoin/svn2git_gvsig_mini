package es.prodevelop.gvsig.mini.tasks.poi;

import android.content.Context;
import android.os.Message;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class RemoveFavourite extends Functionality {

	private POI p;
	private Context context;

	public RemoveFavourite(Map map, int id, POI p, Context context) {
		super(map, id);
		this.p = p;
		this.context = context;
	}

	@Override
	public boolean execute() {
		try {
			double[] coords = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS(getMap().osmap.getMRendererInfo()
							.getSRS()), CRSFactory.getCRS("EPSG:4326"));
			p.setCoordinates(coords);
			final BookmarkManagerTask b = new BookmarkManagerTask(p);
			if (b.removeBookmark()) {
				Message msg = Message.obtain();
				msg.what = Map.SHOW_TOAST;
				msg.obj = context.getResources().getString(
						R.string.bookmark_removed);
				getMap().getMapHandler().sendMessage(msg);
			} else {
				Message msg = Message.obtain();
				msg.what = Map.SHOW_TOAST;
				msg.obj = context.getResources().getString(
						R.string.bookmark_failed);
				getMap().getMapHandler().sendMessage(msg);
			}
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
