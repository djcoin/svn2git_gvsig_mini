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
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.tasks.weather.WeatherFunctionality;

public class MsgMisc implements IMessageHandler {
	private static final String MsgId = "Common_Msg";
	private final static Logger log = Logger.getLogger(MsgMisc.class.getName());
	private final static int[] handledMessages = {Map.GETFEATURE_INITED, Map.SHOW_ADDRESS_DIALOG};
	
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
		case Map.GETFEATURE_INITED:
			log.log(Level.FINE, "GETFEATURE_INITED");
			map.dialog2 = ProgressDialog.show(map, map
					.getResources().getString(R.string.please_wait),
					"GetFeatureInfo...", true);
			map.dialog2.setCancelable(true);
			map.dialog2.setCanceledOnTouchOutside(true);
			map.dialog2.setIcon(R.drawable.infobutton);
			map.dialog2.setOnCancelListener(new OnCancelListener() {
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
		case Map.SHOW_ADDRESS_DIALOG:
			log.log(Level.FINE, "SHOW_ADDRESS_DIALOG");
			map.showCtrl.showSearchDialog();
			break;
		default:
			return false;
		}
		
		return true;

	}

}
