package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import es.prodevelop.gvsig.mini._lg.LgPOI.POIState;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.MapLocation;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

// YOU NEED TO HAVE YOUR SERVICEAREAMANAGER LAUNCHED !
public class _MapPOI extends VanillaMap {
	
	private static final String TAG = _MapPOI.class.getName();
	// See ServiceAreaManager project: res.AreaManager.Intent_Action
	private static final String INTENT_ACTION_AreaManager = "areamanager.event";
	private static final String INTENT_ACTION_AreaService = "lg.area.AreaService";
	private static final String INTENT_ACTION_AreaMockLocationService = "lg.area.mock.location.MockLocationService";
	private static final String INTENT_ACTION_AreaMain = "lg.area.Main";
	
	private final boolean use_mock = false;
	
	private _PointOverlay ovi;

	private Area area1, area2;

	private AreaBroadcastReceiver areaBroadcastReceiver;

	private ILgPoiFactory factory;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		areaBroadcastReceiver = new AreaBroadcastReceiver();
//		factory = new ILgPoiFactory.StaticFactory();
//		if (factory == null)
//			System.out.println("FACTORY1 IS NULL!!!");
	};
	
	public void loadUI(Bundle savedInstanceState) {
		try {
			super.loadUI(savedInstanceState);
//			if (factory == null)
//				System.out.println("FACTOv RY2 IS NULL!!!");
			// ovi = new _PointOverlay(this, osmap, _PointOverlay.DEFAULT_NAME, factory);
			// this.osmap.addOverlay(ovi);
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
	public void onStart() {
		super.onStart();
//	public void onResume() {
//		super.onResume();

		if (use_mock){		
			registerReceiver(areaBroadcastReceiver, new IntentFilter(INTENT_ACTION_AreaManager));		
			startActivity(new Intent(INTENT_ACTION_AreaMain));
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
//	public void onPause() {
//		super.onPause();
		
		if(use_mock){
			unregisterReceiver(areaBroadcastReceiver);
			stopService(new Intent(INTENT_ACTION_AreaService));
			stopService(new Intent(INTENT_ACTION_AreaMockLocationService));
		}
	}

	
}
