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
public class MapHandler extends AMapHandler {

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
		super(m);
		// osmap = map.osmap; // not, has it may change
		dialog2 = m.dialog2; // FIXME
		initialize();
	}
	
	private void initialize(){
		IMessageHandler[] l = {new MsgCommon(), new MsgMisc(), 
							   new MsgPOI(), new MsgRoute(), 
							   new MsgTweet(), new MsgWeather()};
		for (IMessageHandler msgHandler : l) {
			this.bindMessage(msgHandler, msgHandler.getHandledMessages(), true);
		}
	}
	
	@Override
	public void handleMessage(final Message msg) {
		try {
			log.log(Level.FINE, "MapHandler -> handleMessage");
			// TODO ?! Maybe we should rather implement State pattern...
			processMessage(msg);
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

