package es.prodevelop.gvsig.mini._lg;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import es.prodevelop.gvsig.mini._lg.LgPOI.POIState;
import es.prodevelop.gvsig.mini._lg.MockScenarioPlayer.Action;
import es.prodevelop.gvsig.mini.activities.Map;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class _MapPOI extends Map implements IAreaEventListener {
	
	private static final String TAG = _MapPOI.class.getName();
	
	private _PointOverlay ovi;
	SimpleMockLocationProvider mock;

	private Area area1, area2;
	public _MapPOI() {
		AreaManager.getInstance(this).registerZoneChanged(this);
		
	}
	
	public void loadUI(Bundle savedInstanceState) {
		try {
			super.loadUI(savedInstanceState);
			ovi = new _PointOverlay(this, osmap, _PointOverlay.DEFAULT_NAME);
			this.osmap.addOverlay(ovi);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError ou) {
			System.gc();
			log.log(Level.SEVERE, "", ou);
		}
	}
	
	private List<Action> createActions(){
		List<Action> l = new ArrayList<Action>();
		area1 = new Area("My area 1");
		area2 = new Area("My area 2");
		AreaManager am = AreaManager.getInstance(this);
		
		l.add(new MockScenarioPlayer.AreaVisitedAction(am, area1));
		
		// l.add(new MockScenarioPlayer.AreaChangedAction(am, a, event)
		
		l.add(new MockScenarioPlayer.AreaVisitedAction(am, area2));
		
		l.add(new MockScenarioPlayer.AreaUnvisitedAction(am, area1));
		l.add(new MockScenarioPlayer.AreaUnvisitedAction(am, area2));
		// new MockScenarioPlayer.ScenarioTask(null);
		
		return l;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// starting the mock... | this (map should not be here);
		// mock = SimpleMockLocationProvider.makeInstance((LocationManager) this.getSystemService(this.LOCATION_SERVICE), this);
		// mock.start(new FakeLocationSource(mock.mockLocationProvider, true)); // start playing fake GPS every two seconds AND LOOP
		
		Thread t = new Thread(
				new Runnable() {
					public void run() {
						Log.d("Thread Scenario", "WAITING 5 SECS");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} // attends 5 secondes
						Log.d("Thread Scenario", "WAITED");
						MockScenarioPlayer.getInstance().play(createActions(), true); // does loop on this !					
					}
				}
		);
		t.start();
	};
	

	@Override
	public void onLocationChanged(Location pLoc) {		
		super.onLocationChanged(pLoc);
	}
	
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
	
}
