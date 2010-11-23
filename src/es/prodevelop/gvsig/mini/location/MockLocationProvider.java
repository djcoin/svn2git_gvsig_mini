
/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2009 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;

public class MockLocationProvider extends Thread {

	private final static Logger logger = Logger
			.getLogger(MockLocationProvider.class.getName());
	private List<String> data;
	public static LocationManager locationManager;
	private String mocLocationProvider;
	private Location lastLocation;
	private boolean stop = false;
	public boolean isAlive = true;
	public boolean repeat = false;
	
	public static MockLocationProvider mockProvider;
	
	public MockLocationProvider(LocationManager locationManager,
			String mocLocationProvider, List data) throws IOException {

		this.locationManager = locationManager;
		this.mocLocationProvider = mocLocationProvider;
		this.data = data;
		try {			
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(logger);
		} catch (BaseException e) {
			logger.log(Level.SEVERE,"Constructor: " + e.getMessage());
		}
	}

	/**
	 * Sends locations to LocationListeners registered
	 * 
	 * @param period
	 *            The period of time between updates
	 * @param equals
	 *            True if the locations are all the same lat/lon
	 */
	public void updateLocationsPeriodically(int period, boolean equals) {
		try {
			for (String str : data) {
				if (stop)
					return;

				try {
					// while (stop) {
					Thread.sleep(period);
					// }
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

				if (repeat) {
					if (lastLocation != null) {
						lastLocation.setTime(System.currentTimeMillis());
						locationManager.setTestProviderLocation(
								mocLocationProvider, lastLocation);
					}						
				} else {
					// if (/*!equals || */lastLocation == null) {
					// Set one position
					String[] parts = str.split(",");
					Double latitude = Double.valueOf(parts[0]);
					Double longitude = Double.valueOf(parts[1]);
					Double altitude = Double.valueOf(parts[2]);
					Location location = new Location(mocLocationProvider);
					location.setLatitude(latitude);
					location.setLongitude(longitude);
					location.setAltitude(altitude);

					logger.log(Level.FINE, location.toString());
					lastLocation = location;
					// }

					// set the time in the location. If the time on this
					// location
					// matches the time on the one in the previous set call, it
					// will be
					// ignored
					lastLocation.setTime(System.currentTimeMillis());

					locationManager.setTestProviderLocation(mocLocationProvider,
							lastLocation);
				}
				
			}
			this.updateLocationsPeriodically(3000, true);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"updateLocationsPeriodically" + e.getMessage());
		} finally {
			isAlive = false;
		}
	}

	/**
	 * Stops sending new locations
	 * 
	 * @param delay
	 *            The delay of time from now when the stop will occur
	 */
	public void stopUpdating(int delay) {
		try {
			Thread.sleep(delay);
			stop = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"stopUpdating: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		this.updateLocationsPeriodically(3000, true);
	}
	
	public static void startGPSMockLocationsSimulation(Context context, LocationManager manager) {
		try {
			logger.log(Level.FINE, "startGPSMockLocationsSimulation");
			List data = new ArrayList();
			InputStream is = context.getAssets().open("mock_locations.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				data.add(line);
				i++;
			}
			logger.log(Level.FINE, String.format("Found (%s) locations", i));

			// Log.e(LOG_TAG, data.size() + " lines");

			mockProvider = new MockLocationProvider(manager,
					"gps", data);
			manager.addTestProvider("gps", false, true, false, false, true, true, true, 0, 100);
			manager.setTestProviderEnabled("gps", true);			
			mockProvider.start();
			logger.log(Level.FINE, "MockProvider started");
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}		
	}
}