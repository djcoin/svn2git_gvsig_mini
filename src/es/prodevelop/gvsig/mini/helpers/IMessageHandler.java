package es.prodevelop.gvsig.mini.helpers;

import android.os.Message;
import es.prodevelop.gvsig.mini.activities.Map;

public interface IMessageHandler {	
	// We should maybe not give a handle to the map directly...
	boolean handle(final Message msg, Map map);
	String getId();
	int[] getHandledMessages();
}
