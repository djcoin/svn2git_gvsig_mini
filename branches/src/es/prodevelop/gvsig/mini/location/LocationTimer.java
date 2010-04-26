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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Pequeña y
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

import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.util.Utils;
import android.location.Location;
import android.location.LocationProvider;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class LocationTimer extends TimerTask {

	private final static Logger logger = LoggerFactory
			.getLogger(LocationTimer.class);
	private long lastTimeCheck = System.currentTimeMillis();
	private long sendCellPeriod = 60000;
	private LocationHandler locationHandler;
	Map map;
	Timer timer;

	public LocationTimer(LocationHandler locationHandler) {
		logger.setLevel(Utils.LOG_LEVEL);
		logger.setClientID(this.toString());
		map = (Map) locationHandler.mContext;
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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
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
			logger.debug("Scheduling GPSTimerTask with a period of: " + period);
			cancel();
			timer = new Timer();
			timer.scheduleAtFixedRate(this, 0, period);
		} catch (Exception e) {
			logger.error("schedule: " + e.getMessage());
		}
	}

	/**
	 * cancels the task
	 */
	public boolean cancel() {
		try {
			if (timer != null) {
				timer.cancel();
				return super.cancel();
			}
		} catch (Exception e) {
			logger.error("cancel: " + e.getMessage());
			return false;
		}
		return false;
	}
}