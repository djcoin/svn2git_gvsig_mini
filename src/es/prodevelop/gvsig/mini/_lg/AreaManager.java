package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;


public class AreaManager
{
	protected static final String TAG = AreaManager.class.getName();

	protected static AreaManager instance;

	protected ArrayList<Area> areasIn;

	/* OBSERVERS */
	public ArrayList<IAreaEventListener> obsAreaChanged = new ArrayList<IAreaEventListener>();

	/* CONSTRUCTOR */
	protected AreaManager(Context context)
	{
		areasIn = new ArrayList<Area>();
	}

	public static AreaManager getInstance(Context context)
	{
		if (instance == null)
			instance = new AreaManager(context);
		return instance;
	}
	
	public void fireAreaChange(Area a, int event){
		for (IAreaEventListener obs : obsAreaChanged){
			obs.onAreaChanged(event, a);
		}
	}
	
	public void fireAreaVisited(Area a){
		for (IAreaEventListener obs : obsAreaChanged){
			obs.onAreaVisited(a);
		}
	}
	
	public void fireAreaUnvisited(Area a){
		for (IAreaEventListener obs : obsAreaChanged){
			obs.onAreaUnvisited(a);
		}
	}

	public void registerZoneChanged(IAreaEventListener ael)
	{
		Log.d(TAG, "IZoneEventListener registered");
		if (!obsAreaChanged.contains(ael))
			obsAreaChanged.add(ael);
	}

	public void unregisterZoneChanged(IAreaEventListener ael)
	{
		Log.d(TAG, "IZoneEventListener unregistered");
		if (obsAreaChanged.contains(ael))
			obsAreaChanged.remove(ael);
	}
}
