package es.prodevelop.gvsig.mini._lg;


import java.util.Timer;
import java.util.TimerTask;

import es.prodevelop.gvsig.mini.activities.Map;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class SimpleMockLocationProvider {
	
	private static final String TAG = SimpleMockLocationProvider.class.getName();	
	public static final String mockLocationProvider =  LocationManager.GPS_PROVIDER; //"gps";
	
	
	public LocationManager locationManager;
	private UpdateLocationTask updateLocationTask;
	
	public Timer chronos; // do what you want with the internal chronos...
	private FakeLocationSource dataSource;
	private Location lastLocation;
	
	
	public static SimpleMockLocationProvider mockProvider = null;
	private static boolean is_registered_mock = false;
	private static Map map;
	private boolean started;
	
	private SimpleMockLocationProvider(LocationManager locationManager) {	
		this.locationManager = locationManager;		
		chronos = new Timer();
	}
	
	public static SimpleMockLocationProvider makeInstance(LocationManager manager, Map _map){
		map = _map;
		if(mockProvider != null)
			mockProvider.stop();
		mockProvider = new SimpleMockLocationProvider(manager);
		if (!is_registered_mock)
			registerGPSMockLocationsSimulation(manager);
		return mockProvider;
	}
	
	public static SimpleMockLocationProvider getInstance(){
		return mockProvider; // return null if not set
	}
	
	public void start(FakeLocationSource dataSource, long period){
		if (!is_registered_mock){
			Log.d(TAG, "You did not registered the mock location provider!!!");
			return;
		}
		this.dataSource = dataSource;
		updateLocationTask = new UpdateLocationTask();
		chronos.scheduleAtFixedRate(updateLocationTask, 0, period);
		started = true;
	}
	public void start(FakeLocationSource dataSource){
		this.start(dataSource, 2000); // every 2 seconds
	}
	public void stop(){
		chronos.cancel();
		started = false;
	}
	
	public boolean isStarted(){
		return started;
	}
	
	private class UpdateLocationTask extends TimerTask {
		Location location;
		public void run() {
			location = SimpleMockLocationProvider.this.dataSource.getNextLocation();
			if (location == null) {
				SimpleMockLocationProvider.this.stop();
				Log.i(TAG, "DataSource exhausted, cancel updating");
			}			
			location.setProvider(mockLocationProvider);
			location.setTime(System.currentTimeMillis());
			
			lastLocation = location;
			Log.d(TAG, "Sending new mock location: " + location.toString());
			
			SimpleMockLocationProvider.map.onLocationChanged(location);
			// locationManager.setTestProviderLocation(mockLocationProvider, lastLocation);
		}
	}

	public static void registerGPSMockLocationsSimulation(LocationManager manager) {
		try {
			Log.i(TAG, "startGPSMockLocationsSimulation");
			if(mockProvider != null) {
				Log.i(TAG, "Stop GPSMockLocationsSimulation");				
			}	
			manager.addTestProvider(mockLocationProvider, false, true, false, false, true, true, true, 0, 100);
			manager.setTestProviderEnabled(mockLocationProvider, true);
			is_registered_mock  = true;
			Log.i(TAG, "MockProvider started");
		} catch (Exception e) {
			Log.i(TAG,e.getMessage());
		}		
	}
}
