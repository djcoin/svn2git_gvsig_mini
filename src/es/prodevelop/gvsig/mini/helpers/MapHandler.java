package es.prodevelop.gvsig.mini.helpers;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.tasks.namefinder.NameFinderFunc;
import es.prodevelop.gvsig.mini.tasks.weather.WeatherFunctionality;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;
import es.prodevelop.gvsig.mini.yours.RouteManager;


/**
 * This class Handles messages from Functionalities
 * 
 * @author aromeu
 * @author rblanco
 * 
 */
// Associe les messages à des actions
// TODO: Transform the handleMessage's ids into functions calls for subclasses ?
// TODO: how to changed the Handler behavior ? Choose composition ?!
// 		 List of over behavior ? MapBehavior ? WeaverBehavior ? etc. ? set get...
// TODO: Should we better choose Runnable() ?!
// TODO: what the fuck with runOnUIThread ?

// Think about runOnUIThread !
// This handler belong to the thread where it was created. 

// When we will use delegation and setBehavior to this Handler we may then put it back in Map itself.
public class MapHandler extends Handler {
	
	private Map map;
	// private TileRaster osmap;
	private ProgressDialog dialog2; // TODO do something about this ; rename etc.

	private static MapHandler instance = null;
	private final static Logger log = Logger.getLogger(MapHandler.class.getName());

	public static MapHandler getInstance(Map m){
		if (instance == null) {
			instance = new MapHandler(m);
		}
		return instance;
	}
	
	public MapHandler(Map m) {
		super();
		map = m;
		// osmap = map.osmap; // not has it may change
		dialog2 = m.dialog2; // FIXME
	}
	
	@Override
	public void handleMessage(final Message msg) {
		try {
			log.log(Level.FINE, "MapHandler -> handleMessage");
			final int what = msg.what;
			switch (what) {
			case Map.SHOW_LOADING:
				map.setLoadingVisible(true);
				break;
			case Map.HIDE_LOADING:
				map.setLoadingVisible(false);
				break;
			case TaskHandler.NO_RESPONSE:
				log.log(Level.FINE, "task handler NO_RESPONSE");
				if (dialog2 != null)
					dialog2.dismiss();
				Toast.makeText(map, R.string.Map_22, Toast.LENGTH_LONG)
						.show();
				break;
			case Map.SHOW_TOAST:
				log.log(Level.FINE, "SHOW_TOAST");
				Toast t = Toast.makeText(map, msg.obj.toString(),
						Toast.LENGTH_LONG);
				t.show();
				if (dialog2 != null)
					dialog2.dismiss();
				break;
			case Map.SHOW_OK_DIALOG:
				log.log(Level.FINE, "SHOW_OK_DIALOG");
				if (dialog2 != null)
					dialog2.dismiss();
				map.showOKDialog(msg.obj.toString(),
						R.string.getFeatureInfo, true);
				break;
			case Map.POI_LIST:
				log.log(Level.FINE, "POI_LIST");
				map.viewLastPOIs();
				break;
			case Map.POI_CLEARED:
				log.log(Level.FINE, "POI_CLEARED");
				map.updateContext(Map.POI_CLEARED);
				break;
			case Map.ROUTE_CLEARED:
				log.log(Level.FINE, "ROUTE_CLEARED");
				map.updateContext(Map.ROUTE_CLEARED);
				break;
			case Map.WEATHER_INITED:
				log.log(Level.FINE, "WEATHER_INITED");
				dialog2 = ProgressDialog.show(map, map
						.getResources().getString(R.string.please_wait),
						map.getResources().getString(R.string.Map_14),
						true);
				dialog2.setCancelable(true);
				dialog2.setCanceledOnTouchOutside(true);
				dialog2.setIcon(R.drawable.menu03);
				dialog2.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog2) {
						try {
							log.log(Level.FINE, "weather canceled");
							map.getItemContext().cancelCurrentTask();
							dialog2.dismiss();
						} catch (Exception e) {
							log.log(Level.SEVERE, "onCancelDialog: ", e);
						}
					}
				});
				break;
			case Map.GETFEATURE_INITED:
				log.log(Level.FINE, "GETFEATURE_INITED");
				dialog2 = ProgressDialog.show(map, map
						.getResources().getString(R.string.please_wait),
						"GetFeatureInfo...", true);
				dialog2.setCancelable(true);
				dialog2.setCanceledOnTouchOutside(true);
				dialog2.setIcon(R.drawable.infobutton);
				dialog2.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog2) {
						try {
							log.log(Level.FINE, "getFeature canceled");
							map.getItemContext().cancelCurrentTask();
							dialog2.dismiss();
						} catch (Exception e) {
							log.log(Level.SEVERE, "onCancelDialog: ", e);
						}
					}
				});
				break;

			case Map.WEATHER_CANCELED:
				log.log(Level.FINE, "WEATHER_CANCELED");
				Toast.makeText(map, R.string.Map_15, Toast.LENGTH_LONG)
						.show();
				break;
			case Map.WEATHER_ERROR:
				log.log(Level.FINE, "WEATHER_ERROR");
				Toast.makeText(map, R.string.Map_8, Toast.LENGTH_LONG)
						.show();
				if (dialog2 != null)
					dialog2.dismiss();
				break;
			case Map.WEATHER_SHOW:
				log.log(Level.FINE, "WEATHER_SHOW");
				Functionality f = map.getItemContext()
						.getExecutingFunctionality();
				if (f instanceof WeatherFunctionality)
					map.showCtrl.showWeather(((WeatherFunctionality) f).ws);
				else {
					log.log(Level.FINE, "Nof found Weather functionality");
				}
				break;
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
				dialog2 = ProgressDialog.show(map, map
						.getResources().getString(R.string.please_wait),
						map.getResources().getString(R.string.Map_17),
						true);
				dialog2.setCancelable(true);
				dialog2.setCanceledOnTouchOutside(true);
				dialog2.setIcon(R.drawable.pois);
				dialog2.setOnCancelListener(new OnCancelListener() {
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
			case Map.POI_CANCELED:
				log.log(Level.FINE, "POI_CANCELED");
				// ivCleanPois.setVisibility(View.INVISIBLE);
				// ivShowList.setVisibility(View.INVISIBLE);
				if (dialog2 != null)
					dialog2.dismiss();
				Toast.makeText(map, R.string.task_canceled,
						Toast.LENGTH_LONG).show();
				map.nameds = null;
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
				dialog2 = ProgressDialog.show(map, map
						.getResources().getString(R.string.please_wait),
						map.getResources().getString(R.string.Map_19),
						true);
				dialog2.setCancelable(true);
				dialog2.setCanceledOnTouchOutside(true);
				dialog2.setIcon(R.drawable.routes);

				dialog2.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog2) {
						try {
							RouteManager.getInstance().getRegisteredRoute()
									.deleteRoute(false);
							// ivCleanRoute.setVisibility(View.INVISIBLE);
							osmap.resumeDraw();
							map.getItemContext().cancelCurrentTask();
						} catch (Exception e) {
							log.log(Level.SEVERE, "onCancelDialog: ", e);
						}
					}
				});
				break;
			case Map.TWEET_SENT:
				log.log(Level.FINE, "TWEET_SENT");
				Toast.makeText(map, R.string.Map_20, Toast.LENGTH_LONG)
						.show();
				break;
			case Map.TWEET_ERROR:
				log.log(Level.FINE, "TWEET_ERROR");
				Toast t1 = Toast.makeText(map, msg.obj.toString(),
						Toast.LENGTH_LONG);
				t1.show();
				break;
			case Map.SHOW_TWEET_DIALOG:
				log.log(Level.FINE, "SHOW_TWEET_DIALOG");
				map.showCtrl.showTweetDialog();
				break;
			case Map.SHOW_TWEET_DIALOG_SETTINGS:
				log.log(Level.FINE, "SHOW_TWEET_DIALOG_SETTINGS");
				map.showCtrl.showTweetDialogSettings();
				break;
			case Map.SHOW_POI_DIALOG:
				log.log(Level.FINE, "SHOW_POI_DIALOG");
				map.showCtrl.showPOIDialog();
				break;
			case Map.SHOW_ADDRESS_DIALOG:
				log.log(Level.FINE, "SHOW_ADDRESS_DIALOG");
				map.showCtrl.showSearchDialog();
				break;
			case Map.VOID:
				if (dialog2 != null)
					dialog2.dismiss();
				break;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "handleMessage: ", e);
			if (dialog2 != null)
				dialog2.dismiss();
			// Toast.makeText(map,
			// "Operation could not finish. Please try again.",
			// 2000).show();
		} finally {
			try {
				map.clearContext();
				map.osmap.invalidate();
			} catch (Exception e) {
				log.log(Level.SEVERE, "", e);
			}
		}
	}
}

