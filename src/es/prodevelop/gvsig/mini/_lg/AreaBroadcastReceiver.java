package es.prodevelop.gvsig.mini._lg;

import java.util.List;

import es.prodevelop.gvsig.mini._lg.LgPOI.POIState;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AreaBroadcastReceiver extends BroadcastReceiver {

	public AreaBroadcastReceiver() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		String area_name = b.getString("area");
		String type = b.getString("type");			
		System.out.println("Receiving AreaManager Intent: " + area_name );
		System.out.println("------------------------------------");
	}

	/*
	@Override
	public void onAreaChanged(int event, Area z) {
		System.out.println("I saw there is a change in area " + z.toString());
//		for (Point iterable_element : ovi.getPoints()) {
//			
//		}
	}
	@Override
	public void onAreaUnvisited(Area z) {
		System.out.println("I saw we unvisited area: " + z.toString());
		changeState(z.getName(), POIState.NOTVISITED);
	}
	@Override
	public void onAreaVisited(Area z) {
		System.out.println("I saw we visited area: " + z.toString());
		changeState(z.getName(), POIState.VISITED);
	}
	
	private void changeState(String POIname, POIState state){
		LgPOI poi = null;
		if(POIname == area1.getName()){
			poi = ovi.findPoiByName("Point1");
		}
		if(POIname == area2.getName()){
			poi = ovi.findPoiByName("Point2");
		}
		if (poi != null){
			poi.setState(state);
			osmap.resumeDraw();
		}
		else {
			Log.d(TAG, "Could not find any POI matching" + POIname);
		}
	}
	*/
	
}
