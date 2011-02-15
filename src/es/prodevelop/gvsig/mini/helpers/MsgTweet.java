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

public class MsgTweet implements IMessageHandler {
	private static final String MsgId = "Tweet_Msg";
	private final static Logger log = Logger.getLogger(MsgTweet.class.getName());
	private final static int[] handledMessages = {Map.TWEET_ERROR, Map.TWEET_SENT, 
		Map.SHOW_TWEET_DIALOG, Map.SHOW_TWEET_DIALOG_SETTINGS};
	
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
		default:
			return false;
		}
		return true;
	}

}
