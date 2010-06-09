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
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.tasks.weather;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.anddev.android.weatherforecast.weather.GoogleWeatherHandler;
import org.anddev.android.weatherforecast.weather.WeatherSet;
import org.apache.http.util.ByteArrayBuffer;
import org.kxml2.io.KXmlParser;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Message;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * Queries the Google weather service with the current lon, lat of the Map Activity, parses
 * the result and notifies the MapHandler
 * Map.WEATHER_CANCELED = The user cancels the weather task
 * Map.WEATHER_SHOW = The Map should show the weather info
 * Map.WEATHER_INITED = The weather task inits
 * Map.WEATHER_ERROR = An error while parsing
 * TaskHandler.NO_RESPONSE = No response from server
 * @author aromeu 
 * @author rblanco
 *
 */
public class WeatherFunctionality extends Functionality {

	private final static Logger log = Logger
			.getLogger(WeatherFunctionality.class.getName());

	private double lat;
	private double lon;
	public WeatherSet ws;
	private String place;
	private int res;
	
	public WeatherFunctionality(Map map, int id) {		
		super(map, id);		
		double[] center = getMap().osmap.getCenterLonLat();
		this.lon = center[0];
		this.lat = center[1];
		WeatherHandler handler = new WeatherHandler();
		this.addObserver(handler);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WeatherFunctionality(Map map, int id, double lat, double lon) {
		super(map, id);
		this.lat = lat;
		this.lon = lon;
		WeatherHandler handler = new WeatherHandler();
		this.addObserver(handler);
	}

	@Override
	public boolean execute() {
		URL url;
		try {
			String queryString = "http://ws.geonames.org/findNearbyPlaceName?lat="
					+ lat + "&lng=" + lon;
			InputStream is = Utils.openConnection(queryString);
			BufferedInputStream bis = new BufferedInputStream(is);			

			/* Read bytes to the Buffer until there is nothing more to read(-1). */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				if (isCanceled()) {
					res = TaskHandler.CANCELED;
					return true;
				}
				baf.append((byte) current);

			}

			place = this.parseGeoNames(baf.toByteArray());

			queryString = "http://www.google.com/ig/api?weather=" + place;
			/* Replace blanks with HTML-Equivalent. */
			url = new URL(queryString.replace(" ", "%20"));

			/* Get a SAXParser from the SAXPArserFactory. */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();

			/*
			 * Create a new ContentHandler and apply it to the XML-Reader
			 */
			GoogleWeatherHandler gwh = new GoogleWeatherHandler();
			xr.setContentHandler(gwh);

			if (isCanceled()) {
				res = TaskHandler.CANCELED;				
				return true;
			}
			/* Parse the xml-data our URL-call returned. */
			xr.parse(new InputSource(url.openStream()));

			if (isCanceled()) {
				res = TaskHandler.CANCELED;
				return true;
			}
			/* Our Handler now provides the parsed weather-data to us. */
			ws = gwh.getWeatherSet();

			ws.place = place;
			res = TaskHandler.FINISHED;
			// map.showWeather(ws);
		} catch (IOException e) {
			if (e instanceof UnknownHostException) {				
				res = TaskHandler.NO_RESPONSE;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			res = TaskHandler.ERROR;			
		} finally {
//			super.stop();
			return true;
		}
	}

	private String parseGeoNames(final byte[] data) {

		final KXmlParser kxmlParser = new KXmlParser();
		StringBuffer s = new StringBuffer();

		try {
			kxmlParser.setInput(new ByteArrayInputStream(data), "UTF-8");
			kxmlParser.nextTag();
			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null, "geonames");
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo("name") == 0) {
							final String desc = kxmlParser.nextText();
							s.append(desc).append(",");
						} else if (kxmlParser.getName()
								.compareTo("countryName") == 0) {
							final String desc = kxmlParser.nextText();
							s.append(desc);
						}
						break;

					case KXmlParser.END_TAG:
						break;
					}
					tag = kxmlParser.next();
				}
				// kxmlParser.require(KXmlParser.END_DOCUMENT, null, null);
			}
		} catch (XmlPullParserException parser_ex) {
			log.log(Level.SEVERE,"",parser_ex);
		} catch (IOException ioe) {
			log.log(Level.SEVERE,"",ioe);
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
			log.log(Level.SEVERE,"",ou);
		} finally {
			return s.toString();
		}
	}

	@Override
	public int getMessage() {
		return res;
	}
	
	private class WeatherHandler extends TaskHandler {

		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {						
			case TaskHandler.CANCELED:
				getMap().getMapHandler().sendEmptyMessage(Map.WEATHER_CANCELED);
				break;
			case TaskHandler.FINISHED:
				getMap().getMapHandler().sendEmptyMessage(Map.WEATHER_SHOW);
				break;
			case TaskHandler.INITED:
				getMap().getMapHandler().sendEmptyMessage(Map.WEATHER_INITED);
				break;
			case TaskHandler.ERROR:
				getMap().getMapHandler().sendEmptyMessage(Map.WEATHER_ERROR);
				break;
			case TaskHandler.NO_RESPONSE:
				getMap().getMapHandler().sendEmptyMessage(TaskHandler.NO_RESPONSE);
				break;
			case TaskHandler.BAD_RESPONSE:
				getMap().getMapHandler().sendEmptyMessage(Map.WEATHER_ERROR);
				break;			
			}
		}
	}

}
