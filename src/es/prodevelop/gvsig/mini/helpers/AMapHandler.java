package es.prodevelop.gvsig.mini.helpers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.Vector;

import es.prodevelop.gvsig.mini.activities.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author sim
 * This interface is used to perform delegation
 * for Handler that has to deal with messages.
 *
 */
public abstract class AMapHandler extends Handler {

	private static String TAG = AMapHandler.class.getSimpleName();
	protected Map map;
	
	// we may find better structure !
	protected HashMap<Integer, Vector<IMessageHandler>> messager = new LinkedHashMap<Integer, Vector<IMessageHandler>>();
		
	public AMapHandler(Map map) {
		super();
		this.map = map;
	}
	
	// return the matching
	public Vector<IMessageHandler> matchMessage(final int what){
		Vector<IMessageHandler> v = messager.get(what);
		if (v == null ){
			Log.d(TAG, "No receivers found for " + what);
			// IMessageHandler[] m = {};
			return new Vector<IMessageHandler>();
		}
		//return (IMessageHandler[])v.toArray();		
		return v;
	}
	
	// simple strategy: execute all from first to last one
	public boolean processMessage(final Message msg){
		// IMessageHandler[] m = matchMessage(msg.what);
		Vector<IMessageHandler> m = matchMessage(msg.what);
		boolean atLeastOneHandled = false;
		
		for (IMessageHandler msgHandler : m) {
			if (msgHandler.handle(msg, this.map))
				atLeastOneHandled = true;
		}
		
		return atLeastOneHandled;
	}
	
	public void unbindMessage(String msgHandlerId, int[] messages){
		Vector<IMessageHandler> v = null;
		for (int i : messages) {
			v = messager.get(i);			
			if (v == null) //huho no msgHandler to delete associated with the message!
				continue;
			int index = getIndexOfById(v, msgHandlerId);
			if (index != -1)
				v.remove(index);
				Log.d(TAG, "Just removed from message id:" + i + "handler " + msgHandlerId);
			}
	}
	
	public int getIndexOfById(Vector<IMessageHandler> v, String msgHandlerId){
		for (int j = 0; j < v.size(); j++)
			if (v.get(j).getId() == msgHandlerId)
				return j;
		return -1;
	}
	//public boolean containsIMessageHandler()
	
	public void unbindMessage(IMessageHandler msgHandler, int[] messages){
		Vector<IMessageHandler> v = null;
		for (Integer i : messages) {
			v = messager.get(i);
			if (v == null)
				continue;
			v.remove(msgHandler); // check if removed ?!
			Log.d(TAG, "Just removed from message id:" + i + "handler " + msgHandler.getId());
		}
	}
	
	// only checks if it contains the exact same msgHandler (do not check id...). FIXME ?
	public void bindMessage(IMessageHandler msgHandler, int[] messages, boolean erase) {
		Vector<IMessageHandler> v = null;
		for (Integer i : messages) {
			v = messager.get(i);
			if (v == null)
				v = new Vector<IMessageHandler>();
			else if (erase)
				v.clear();
			else if (v.contains(msgHandler)) {
				Log.d("TAG", msgHandler.getId() + " already binded for message " + i + "!!!");
				continue;
			}
			
			v.add(msgHandler);
			messager.put(new Integer(i), v);
			Log.d(TAG, "Just ADDED for message id:" + i + "handler " + msgHandler.getId());
		}

	}
		
}
