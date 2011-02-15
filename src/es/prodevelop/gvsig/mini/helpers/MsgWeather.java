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
import es.prodevelop.gvsig.mini.tasks.weather.WeatherFunctionality;

public class MsgWeather implements IMessageHandler {
	private static final String MsgId = "Weather_Msg";
	private final static Logger log = Logger.getLogger(MsgWeather.class.getName());
	private final static int[] handledMessages = {Map.WEATHER_CANCELED, Map.WEATHER_ERROR, 
		Map.WEATHER_INITED, Map.WEATHER_SHOW};
	
	@Override
	public int[] getHandledMessages() {	
		return handledMessages;
	}
	public String getId() {
		return MsgId;
	}
	
	@Override
	public boolean handle(Message msg,final Map map) {
		ProgressDialog dialog2 = map.dialog2;
		switch (msg.what){
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
		}
		return false;
	}

}
