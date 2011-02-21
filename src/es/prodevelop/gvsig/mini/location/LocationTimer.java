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

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.location.Location;
import android.location.LocationProvider;
import es.prodevelop.gvsig.mini._lg.IMap;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;



public class LocationTimer extends TimerTask {

	private final static Logger logger = Logger
			.getLogger(LocationTimer.class.getName());
	private long lastTimeCheck = System.currentTimeMillis();
	private long sendCellPeriod = 60000;
	private LocationHandler locationHandler;
	IMap map;
	Timer timer;

	public LocationTimer(LocationHandler locationHandler) {
		try {			
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(logger);
			map = (IMap) locationHandler.mContext;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"",e);
		}
	}

	@Override
	public void run() {
		try {
			boolean send = isTimeToSend();
			switch (Map.GPS_STATUS) {
			case LocationProvider.AVAILABLE:
				this.lastTimeCheck = System.currentTimeMillis();
				if (locationHandler.lastFixedLocation == null) {
					map.obtainCellLocation();
				} else {
					if (isTimeToSend(locationHandler.lastFixedLocation))
						map.obtainCellLocation();
				}
				break;
			default:
				if (send)
					map.obtainCellLocation();
				break;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,"",e);
		}
	}

	public boolean isTimeToSend() {
		try {
			long currentTimeCheck = System.currentTimeMillis();
			if (currentTimeCheck - this.lastTimeCheck > this.sendCellPeriod) {
				this.lastTimeCheck = currentTimeCheck;
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"",e);
			return false;
		}
	}

	public boolean isTimeToSend(Location location) {
		try {
			long locationTimeCheck = location.getTime();
			if (this.lastTimeCheck - locationTimeCheck > this.sendCellPeriod) {
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"",e);
			return false;
		}
	}

	/**
	 * Instantiates a Timer and schedules it at fixed rate with delay = period
	 * 
	 * @param period
	 *            The delay of the schedule
	 */
	public void schedule(int period) {
		try {
			logger.log(Level.FINE, "Scheduling GPSTimerTask with a period of: " + period);
			timer = new Timer();			
			timer.scheduleAtFixedRate(this, 0, period);
		} catch (Exception e) {
			logger.log(Level.SEVERE,"schedule: " + e);
		}
	}

	/**
	 * cancels the task
	 */
	public boolean cancel() {
		try {
			if (timer != null) {
				timer.cancel();
				boolean res = super.cancel();
				return res;
			}
			return false;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"cancel: " + e);
			return false;
		}		
	}
	
	public void finalize() {
		try {			
			cancel();	
			timer.purge();
			timer = null;
		} catch (Exception e) {
//			Log.e("", e.getMessage());
		}
	}
}