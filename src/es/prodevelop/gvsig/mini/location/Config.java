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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * This class contains some properties to configure the application
 * 
 * @author aromeu
 * 
 */
public class Config {

	private final static Logger logger = Logger.getLogger(Config.class
			.getName());

	private static Hashtable properties;
	private static Config config;

	public static String configDirFile = Utils.APP_DIR;

	private static Context context;

	public static String SERVER_TAG = "server";
	public static String USER_TAG = "user";
	public static String PASS_TAG = "pass";
	public static String DOMAIN_TAG = "domain";
	public static String PORT_TAG = "port";
	public static String LOG_TAG = "logLevel";
	public static String RETRYFAIL_TAG = "retryFail";
	public static String WAKETIME_TAG = "wakeTime";
	public static String LASTTIME_TAG = "lastTime";
	public static String GPSTIMER_TAG = "gpsTimerDelay";
	public static String TO_TAG = "to";
	public static String USERADDRESS_TAG = "userAddress";
	public static String USERNAME_TAG = "userName";
	public static String USENETWORK_TAG = "useNetworkLocation";
	public static String SENDPERIOD_TAG = "sendMessagePeriod";
	public static String MINDISTANCE_TAG = "minTime";
	public static String MINTIME_TAG = "minDistance";

	/**
	 * A singleton. You must previously call Config.setContext method
	 * 
	 * @return
	 */
	public static synchronized Config getInstance() {
		try {
			if (config == null) {
				config = new Config();
				loadProperties(false);
				// Config.getInstance().setLogLevel(logger);
			}
			return config;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getInstance: " + e.getMessage());
			return null;
		}
	}

	public static void setContext(Context context) {
		Config.context = context;
	}

	public static void loadProperties(boolean fromAsset) {
		try {
			CompatManager.getInstance().getRegisteredLogHandler()
					.configureLogger(logger);
		} catch (BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		File f = null;
		FileReader configReader = null;
		BufferedReader reader = null;
		boolean fromDisk = false;
		try {
			properties = new Hashtable();
			f = new File("/sdcard/" + Config.configDirFile + "/" + "conf.txt");
			if (f != null && f.exists() && !fromAsset) {
				configReader = new FileReader(f);
				reader = new BufferedReader(configReader);
				fromDisk = true;
			} else {
				InputStream is = context.getAssets().open("config.txt");

				reader = new BufferedReader(new InputStreamReader(is));
			}

			String line = null;
			String[] part;
			while ((line = reader.readLine()) != null) {
				part = line.split("=");
				properties.put(part[0], part[1]);
				logger.log(Level.FINE, "Found: " + part[0] + " with value: "
						+ part[1]);
			}

			if (isValid()) {
				logger.log(Level.FINE, "Config file is valid");
				if (!fromDisk) {
					persist();
					logger.log(Level.FINE, "Config file persisted");
				}
			} else {
				logger.log(Level.FINE,
						"Config file is not valid!! Trying to load from asset");
				Config.loadProperties(true);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "loadProperties: " + e.getMessage());
			logger.log(Level.FINE,
					"Config file is not valid!! Trying to load from asset");
			Config.loadProperties(true);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (configReader != null) {
				try {
					configReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static boolean isValid() {
		try {
			if (!Config.checkNotNull(Config.getInstance().getDomain())) {
				return false;
			}

			if (!Config.checkNotNull(Config.getInstance().getPass())) {
				return false;
			}

			if (!Config.checkNotNull(Config.getInstance().getServer())) {
				return false;
			}

			if (!Config.checkNotNull(Config.getInstance().getTo())) {
				return false;
			}

			if (!Config.checkNotNull(Config.getInstance().getUser())) {
				return false;
			}

			if (!Config.checkNotNull(Config.getInstance().getUserAddress())) {
				return false;
			}

			if (!Config.checkNotNull(Config.getInstance().getUserName())) {
				return false;
			}

			if (!Config.checkNotNull(String.valueOf(Config.getInstance()
					.getGPSTimerDelay()))) {
				return false;
			}

			if (!Config.checkNotNull(String.valueOf(Config.getInstance()
					.getLastTime()))) {
				return false;
			}

			if (!Config.checkNotNull(String.valueOf(Config.getInstance()
					.getLogLevel()))) {
				return false;
			}

			if (!Config.checkNotNull(String.valueOf(Config.getInstance()
					.getPort()))) {
				return false;
			}

			if (!Config.checkNotNull(String.valueOf(Config.getInstance()
					.getRetryFail()))) {
				return false;
			}

			if (!Config.checkNotNull(String.valueOf(Config.getInstance()
					.getWakeTime()))) {
				return false;
			}

			return true;

		} catch (Exception e) {
			logger.log(Level.SEVERE, "isValid: " + e.getMessage());
			return false;
		}
	}

	private static boolean checkNotNull(String value) {
		try {
			if (value == null || value.trim().compareTo("") == 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "CheckNotNull: " + e.getMessage());
			return false;
		}
	}

	public String getServer() throws NoSuchFieldError {
		try {
			return properties.get(Config.SERVER_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.SERVER_TAG);
		}
	}

	public String getUser() throws NoSuchFieldError {
		try {
			return properties.get(Config.USER_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.USER_TAG);
		}
	}

	public String getPass() throws NoSuchFieldError {
		try {
			return properties.get(Config.PASS_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.PASS_TAG);
		}
	}

	public String getDomain() throws NoSuchFieldError {
		try {
			return properties.get(Config.DOMAIN_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.DOMAIN_TAG);
		}
	}

	public int getPort() throws NoSuchFieldError {
		try {
			return new Integer(properties.get(Config.PORT_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.PORT_TAG);
		}
	}

	public int getLogLevel() throws NoSuchFieldError {
		try {
			return new Integer(properties.get(Config.LOG_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.LOG_TAG);
		}
	}

	public int getRetryFail() throws NoSuchFieldError {
		try {
			return new Integer(properties.get(Config.RETRYFAIL_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.RETRYFAIL_TAG);
		}
	}

	public int getWakeTime() throws NoSuchFieldError {
		try {
			return new Integer(properties.get(Config.WAKETIME_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.WAKETIME_TAG);
		}
	}

	public int getLastTime() throws NoSuchFieldError {
		try {
			return new Integer(properties.get(Config.LASTTIME_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.LASTTIME_TAG);
		}
	}

	public int getGPSTimerDelay() throws NoSuchFieldError {
		try {
			return new Integer(properties.get(Config.GPSTIMER_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.GPSTIMER_TAG);
		}
	}

	public String getTo() throws NoSuchFieldError {
		try {
			return properties.get(Config.TO_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.TO_TAG);
		}
	}

	public String getUserAddress() throws NoSuchFieldError {
		try {
			return properties.get(Config.USERADDRESS_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.USERADDRESS_TAG);
		}
	}

	public String getUserName() throws NoSuchFieldError {
		try {
			return properties.get(Config.USERNAME_TAG).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.USERNAME_TAG);
		}
	}

	public int getUseNetWorkLocation() throws NoSuchFieldError {
		try {
			return Integer.valueOf(
					properties.get(Config.USENETWORK_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.USENETWORK_TAG);
		}
	}

	public int getMinTime() throws NoSuchFieldError {
		try {
			int res = Integer.valueOf(
					properties.get(Config.MINTIME_TAG).toString()).intValue();
			if (res < 0)
				res = 0;
			return res;
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.MINTIME_TAG);
		}
	}

	public int getMinDistance() throws NoSuchFieldError {
		try {
			int res = Integer.valueOf(
					properties.get(Config.MINDISTANCE_TAG).toString())
					.intValue();
			if (res < 0)
				res = 0;
			return res;
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.MINDISTANCE_TAG);
		}
	}

	public boolean isUseNetWorkLocation() throws NoSuchFieldError {
		try {
			return Boolean.valueOf(
					properties.get(Config.USENETWORK_TAG).toString())
					.booleanValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.USENETWORK_TAG);
		}
	}

	public int getSendMessagePeriod() throws NoSuchFieldError {
		try {
			return Integer.valueOf(
					properties.get(Config.SENDPERIOD_TAG).toString())
					.intValue();
		} catch (Exception e) {
			throw new NoSuchFieldError(Config.SENDPERIOD_TAG);
		}
	}

	public static void persist() {
		try {
			File f = new File("/sdcard/" + Config.configDirFile + "/"
					+ "conf.txt");
			if (!f.exists()) {
				File dirPath = new File("/sdcard/" + Config.configDirFile + "/");
				dirPath.mkdirs();
				f.createNewFile();
			} else {
				f.delete();
				f.createNewFile();
			}
			FileWriter logwriter = new FileWriter(f, true);

			BufferedWriter out = new BufferedWriter(logwriter);

			out.write(Config.SERVER_TAG + "="
					+ Config.getInstance().getServer() + "\n");
			out.write(Config.USER_TAG + "=" + Config.getInstance().getUser()
					+ "\n");
			out.write(Config.PASS_TAG + "=" + Config.getInstance().getPass()
					+ "\n");
			out.write(Config.DOMAIN_TAG + "="
					+ Config.getInstance().getDomain() + "\n");
			out.write(Config.PORT_TAG + "=" + Config.getInstance().getPort()
					+ "\n");
			out.write(Config.LOG_TAG + "=" + Config.getInstance().getLogLevel()
					+ "\n");
			out.write(Config.RETRYFAIL_TAG + "="
					+ Config.getInstance().getRetryFail() + "\n");
			out.write(Config.WAKETIME_TAG + "="
					+ Config.getInstance().getWakeTime() + "\n");
			out.write(Config.LASTTIME_TAG + "="
					+ Config.getInstance().getLastTime() + "\n");
			out.write(Config.GPSTIMER_TAG + "="
					+ Config.getInstance().getGPSTimerDelay() + "\n");
			out.write(Config.TO_TAG + "=" + Config.getInstance().getTo() + "\n");
			out.write(Config.USERADDRESS_TAG + "="
					+ Config.getInstance().getUserAddress() + "\n");
			out.write(Config.USERNAME_TAG + "="
					+ Config.getInstance().getUserName() + "\n");
			out.write(Config.USENETWORK_TAG + "="
					+ Config.getInstance().getUseNetWorkLocation() + "\n");
			out.write(Config.SENDPERIOD_TAG + "="
					+ Config.getInstance().getSendMessagePeriod() + "\n");
			out.write(Config.MINTIME_TAG + "="
					+ Config.getInstance().getMinTime() + "\n");
			out.write(Config.MINDISTANCE_TAG + "="
					+ Config.getInstance().getMinDistance());
			out.close();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "persist: " + e.getMessage());
		}
	}

	public void setProperty(String key, String value) {
		try {
			properties.put(key, value);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	public String getProperty(String key) throws NoSuchFieldError {
		try {
			return properties.get(key).toString();
		} catch (Exception e) {
			throw new NoSuchFieldError(key);
		}
	}
}
