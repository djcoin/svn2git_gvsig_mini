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
import es.prodevelop.gvsig.mini.yours.RouteManager;

public class MsgRoute implements IMessageHandler {
	private static final String MsgId = "POI_Msg";
	private final static Logger log = Logger.getLogger(MsgRoute.class.getName());
	private final static int[] handledMessages = {Map.ROUTE_CANCELED, Map.ROUTE_CLEARED, 
		Map.ROUTE_INITED, Map.ROUTE_NO_CALCULATED, Map.ROUTE_NO_RESPONSE, 
		Map.ROUTE_SUCCEEDED, Map.CALCULATE_ROUTE}; // Map.ROUTE_ORIENTATION_CHANGED, 
	
	@Override
	public int[] getHandledMessages() {	
		return handledMessages;
	}
	public String getId() {
		return MsgId;
	}
	
	@Override
	public boolean handle(Message msg, final Map map) {
		ProgressDialog dialog2 = map.dialog2;
		switch (msg.what){

		case Map.ROUTE_NO_RESPONSE:
			log.log(Level.FINE, "ROUTE_NO_RESPONSE");
			Toast.makeText(map, R.string.server_busy,
					Toast.LENGTH_LONG).show();
			// ivCleanRoute.setVisibility(View.INVISIBLE);
			if (dialog2 != null)
				dialog2.dismiss();
			break;
		case Map.ROUTE_SUCCEEDED:
			log.log(Level.FINE, "ROUTE_SUCCEEDED");
			// ivCleanRoute.setVisibility(View.VISIBLE);
			map.osmap.CLEAR_ROUTE = true;
			if (dialog2 != null)
				dialog2.dismiss();
			map.osmap.getMRendererInfo().reprojectGeometryCoordinates(
					RouteManager.getInstance().getRegisteredRoute()
							.getRoute().getFeatureAt(0).getGeometry(),
					"EPSG:4326");
			map.updateContext(Map.ROUTE_SUCCEEDED);
			map.osmap.resumeDraw();
			break;
		case Map.ROUTE_NO_CALCULATED:
			log.log(Level.FINE, "ROUTE_NO_CALCULATED");
			Toast.makeText(map, R.string.Map_16, Toast.LENGTH_LONG)
					.show();
			// ivCleanRoute.setVisibility(View.INVISIBLE);
			if (dialog2 != null)
				dialog2.dismiss();
			break;
		
		case Map.ROUTE_CANCELED:
			log.log(Level.FINE, "ROUTE_CANCELED");
			RouteManager.getInstance().getRegisteredRoute()
					.deleteRoute(false);
			// route.deleteEndPoint();
			// route.deleteStartPoint();
			// yoursFunc.cancel();
			if (dialog2 != null)
				dialog2.dismiss();
			// ivCleanRoute.setVisibility(View.INVISIBLE);
			Toast.makeText(map, R.string.Map_18, Toast.LENGTH_LONG)
					.show();
			map.osmap.postInvalidate();
			break;
		
		case Map.CALCULATE_ROUTE:
			log.log(Level.FINE, "CALCULATE_ROUTE");
			map.calculateRoute();
			break;
		case Map.ROUTE_INITED:
			log.log(Level.FINE, "ROUTE_INITED");
			RouteManager.getInstance().getRegisteredRoute()
					.deleteRoute(false);
			map.dialog2 = ProgressDialog.show(map, map
					.getResources().getString(R.string.please_wait),
					map.getResources().getString(R.string.Map_19),
					true);
			map.dialog2.setCancelable(true);
			map.dialog2.setCanceledOnTouchOutside(true);
			map.dialog2.setIcon(R.drawable.routes);

			map.dialog2.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog2) {
					try {
						RouteManager.getInstance().getRegisteredRoute()
								.deleteRoute(false);
						// ivCleanRoute.setVisibility(View.INVISIBLE);
						map.osmap.resumeDraw();
						map.getItemContext().cancelCurrentTask();
					} catch (Exception e) {
						log.log(Level.SEVERE, "onCancelDialog: ", e);
					}
				}
			});
			break;
		case Map.ROUTE_CLEARED:
			log.log(Level.FINE, "ROUTE_CLEARED");
			map.updateContext(Map.ROUTE_CLEARED);
			break;	
		default:
			return false;
		}
		return true;
	}

}
