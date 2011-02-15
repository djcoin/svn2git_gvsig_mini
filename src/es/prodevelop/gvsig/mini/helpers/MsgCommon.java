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

public class MsgCommon implements IMessageHandler {
	private static final String MsgId = "Common_Msg";
	private final static Logger log = Logger.getLogger(MsgCommon.class.getName());

	private final static int[] handledMessages = {Map.SHOW_LOADING, Map.HIDE_LOADING, 
		TaskHandler.NO_RESPONSE, Map.SHOW_TOAST, Map.SHOW_OK_DIALOG, Map.VOID};
	
	@Override
	public int[] getHandledMessages() {	
		return handledMessages;
	}
	
	public String getId() {
		return MsgId;
	}

	@Override
	public boolean handle(Message msg, Map map) {
		ProgressDialog dialog2 = map.dialog2;
		switch (msg.what){
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
			map.showCtrl.showOKDialog(msg.obj.toString(),
					R.string.getFeatureInfo, true);
			break;
		case Map.VOID:
			if (dialog2 != null)
				dialog2.dismiss();
			break;
		default:
			return false;
		}
		return true;

	}

}
