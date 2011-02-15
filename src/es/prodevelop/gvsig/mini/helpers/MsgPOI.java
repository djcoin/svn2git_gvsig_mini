package es.prodevelop.gvsig.mini.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Message;
import android.widget.Toast;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.namefinder.NameFinderFunc;
import es.prodevelop.gvsig.mini.tasks.weather.WeatherFunctionality;

public class MsgPOI implements IMessageHandler {
	private static final String MsgId = "POI_Msg";
	private final static Logger log = Logger.getLogger(MsgPOI.class.getName());
	private final static int[] handledMessages = {Map.POI_CANCELED, Map.POI_CLEARED, 
		Map.POI_INITED, Map.POI_LIST, Map.POI_SHOW, Map.POI_SUCCEEDED, Map.SHOW_POI_DIALOG};
	
	@Override
	public int[] getHandledMessages() {	
		return handledMessages;
	}
	
	public String getId() {
		return MsgId;
	}
	
	
	@Override
	public boolean handle(Message msg, final Map map) {
		// ProgressDialog dialog2 = map.dialog2;
		switch (msg.what){
		case Map.POI_LIST:
			log.log(Level.FINE, "POI_LIST");
			map.viewLastPOIs();
			break;
		case Map.POI_CLEARED:
			log.log(Level.FINE, "POI_CLEARED");
			map.updateContext(Map.POI_CLEARED);
			break;
		case Map.POI_SHOW:
			log.log(Level.FINE, "POI_SHOW");
			Functionality nf = map.getItemContext().getExecutingFunctionality();
			if (nf instanceof NameFinderFunc) {
				NameFinderFunc n = (NameFinderFunc) nf;
				map.osmap.getMRendererInfo().reprojectGeometryCoordinates(
						n.nm, "EPSG:4326");
				boolean update = map.showPOIs(n.desc, n.nm);
				if (update)
					map.updateContext(Map.POI_SUCCEEDED);
			} else {
				log.log(Level.FINE,
						"Nof found NameFinder functionality");
			}
			break;
		case Map.POI_INITED:
			log.log(Level.FINE, "POI_INITED");
			map.dialog2 = ProgressDialog.show(map, map
					.getResources().getString(R.string.please_wait),
					map.getResources().getString(R.string.Map_17),
					true);
			map.dialog2.setCancelable(true);
			map.dialog2.setCanceledOnTouchOutside(true);
			map.dialog2.setIcon(R.drawable.pois);
			map.dialog2.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog2) {
					try {
						map.getItemContext().cancelCurrentTask();
						dialog2.dismiss();
					} catch (Exception e) {
						log.log(Level.SEVERE, "onCancelDialog: ", e);
					}
				}
			});
			break;
		case Map.POI_CANCELED:
			log.log(Level.FINE, "POI_CANCELED");
			// ivCleanPois.setVisibility(View.INVISIBLE);
			// ivShowList.setVisibility(View.INVISIBLE);
			if (map.dialog2 != null)
				map.dialog2.dismiss();
			Toast.makeText(map, R.string.task_canceled,
					Toast.LENGTH_LONG).show();
			map.nameds = null;
			map.osmap.postInvalidate();
			break;
		case Map.SHOW_POI_DIALOG:
			log.log(Level.FINE, "SHOW_POI_DIALOG");
			map.showCtrl.showPOIDialog();
			break;
		default:
			return false;
		}
		return true;
	}

}
