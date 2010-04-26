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
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.tasks.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.location.Location;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;

/**
 * Retrieves Cell Location (lan lot) and calls onLocationChanged of Map activity
 * @author aromeu 
 * @author rblanco
 *
 */
public class GetCellLocationFunc extends Functionality {

	public GetCellLocationFunc(Map map, int id) {
		super(map, id);
	}

	@Override
	public boolean execute() {
		try {
			final double[] cellLocation = requestCellLocation(getMap());
			if (cellLocation[0] == 0 && cellLocation[1] == 0) {
				return true;
			}
			Location currentLocation = new Location("cell");
			currentLocation.setProvider("cell");
			currentLocation.setLatitude(cellLocation[0]);
			currentLocation.setLongitude(cellLocation[1]);
			currentLocation.setAccuracy((float) cellLocation[2]);
			currentLocation.setTime(System.currentTimeMillis());
			getMap().onLocationChanged(currentLocation);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public int getMessage() {		
		return TaskHandler.FINISHED;
	}
	
	public static double[] requestCellLocation(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
			
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();

			CellLocation.requestLocationUpdate();
			int cellID = location.getCid();
			int lac = location.getLac();

			String urlString = "http://www.google.com/glm/mmap";

			// ---open a connection to Google Maps API---
			URL url = new URL(urlString);
			
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.connect();

			// ---write some custom data to Google Maps API---
			OutputStream outputStream = httpConn.getOutputStream();
			writeData(outputStream, cellID, lac);

			// ---get the response---
			InputStream inputStream = httpConn.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);

			// ---interpret the response obtained---
			dataInputStream.readShort();
			dataInputStream.readByte();
			int code = dataInputStream.readInt();
			if (code == 0) {
				double lat = (double) dataInputStream.readInt() / 1000000D;
				double lng = (double) dataInputStream.readInt() / 1000000D;
				int a = dataInputStream.readInt();								
				return new double[] { lat, lng, a };
			}
			return new double[] { 0, 0, 0 };
		} catch (Exception e) {			
			return new double[] { 0, 0, 0 };
		}
	}

	private static void writeData(OutputStream out, int cellID, int lac)
			throws IOException {		
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeShort(21);
		dataOutputStream.writeLong(0);
		dataOutputStream.writeUTF("en");
		dataOutputStream.writeUTF("Android");
		dataOutputStream.writeUTF("1.0");
		dataOutputStream.writeUTF("Web");
		dataOutputStream.writeByte(27);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(3);
		dataOutputStream.writeUTF("");

		dataOutputStream.writeInt(cellID);
		dataOutputStream.writeInt(lac);

		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.flush();
	}

}
