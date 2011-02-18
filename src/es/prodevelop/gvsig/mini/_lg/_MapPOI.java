package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import es.prodevelop.gvsig.mini._lg.LgPOI.POIState;
import es.prodevelop.gvsig.mini.activities.Map;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

// YOU NEED TO HAVE YOUR SERVICEAREAMANAGER LAUNCHED !
public class _MapPOI extends Map {
	
	private static final String TAG = _MapPOI.class.getName();
	// See ServiceAreaManager project: res.AreaManager.Intent_Action
	private static final String AreaManagerINTENT_ACTION = "areamanager.event";
	
	
	private _PointOverlay ovi;

	private Area area1, area2;

	private AreaBroadcastReceiver areaBroadcastReceiver;

	private ILgPoiFactory factory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		areaBroadcastReceiver = new AreaBroadcastReceiver();
		factory = new ILgPoiFactory.StaticFactory();
		
		// should change to a startService ?
		startActivity(new Intent("lg.area.Main"));
	};
	
	public void loadUI(Bundle savedInstanceState) {
		try {
			super.loadUI(savedInstanceState);
			ovi = new _PointOverlay(this, osmap, _PointOverlay.DEFAULT_NAME, factory);
			this.osmap.addOverlay(ovi);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError ou) {
			System.gc();
			log.log(Level.SEVERE, "", ou);
		}
	}

	@Override
	public void onLocationChanged(Location pLoc) {		
		super.onLocationChanged(pLoc);
		osmap.resumeDraw();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(areaBroadcastReceiver, new IntentFilter(AreaManagerINTENT_ACTION));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(areaBroadcastReceiver);
	}	
	
}
