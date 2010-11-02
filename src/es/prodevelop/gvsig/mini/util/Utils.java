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
 *   author Rub�n Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm.views.util;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */

package es.prodevelop.gvsig.mini.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.map.GeoMath;
import es.prodevelop.gvsig.mini.map.GeoUtils;

/**
 * Utility mehtods
 * @author aromeu 
 * @author rblanco
 *
 */
public class Utils implements GeoUtils {
	
	public final static String LAYERS_VERSION = "v0.2.1";
	public final static String APP_DIR = "gvSIG";
	public final static String MAPS_DIR = "maps";
	public final static String CONFIG_DIR = "config";
	public final static String LAYERS_DIR = "layers";
	public final static Level LOG_LEVEL = Level.FINE;
	public final static Level FS_LOG_LEVEL = Level.SEVERE;
	public final static String LOG_DIR = "logs";
	public static int BUFFER_SIZE = 2;
	public static int ROTATE_BUFFER_SIZE = 2;
	public final static int COMPASS_ACCURACY = 1;
	public final static int MIN_ROTATION = 10;
	public final static int REPAINT_TIME = 200;
	public static int[] getMapTileFromCoordinates(final int aLat,
			final int aLon, final int zoom, final int[] reuse) {
		return getMapTileFromCoordinates(aLat / 1E6, aLon / 1E6, zoom, reuse);
	}
	
	public final static String SUPPORT_MAIL = "minijira[at]prodevelop[dot]es";

	public static int[] getMapTileFromCoordinates(final double aLat,
			final double aLon, final int zoom, final int[] aUseAsReturnValue) {
		final int[] out = (aUseAsReturnValue != null) ? aUseAsReturnValue
				: new int[2];

		out[MAPTILE_LATITUDE_INDEX] = (int) Math.floor((1 - Math.log(Math
				.tan(aLat * Math.PI / 180)
				+ 1 / Math.cos(aLat * Math.PI / 180))
				/ Math.PI)
				/ 2 * (1 << zoom));
		out[MAPTILE_LONGITUDE_INDEX] = (int) Math.floor((aLon + 180) / 360
				* (1 << zoom));

		return out;
	}

	public static GeoMath getBoundingBoxFromMapTile(final int[] aMapTile,
			final int zoom) {
		final int y = aMapTile[MAPTILE_LATITUDE_INDEX];
		final int x = aMapTile[MAPTILE_LONGITUDE_INDEX];
		return new GeoMath(tile2lat(y, zoom), tile2lon(x + 1, zoom), tile2lat(
				y + 1, zoom), tile2lon(x, zoom));
	}

	private static double tile2lon(int x, int aZoom) {
		return (x / Math.pow(2.0, aZoom) * 360.0) - 180;
	}

	private static double tile2lat(int y, int aZoom) {
		final double n = Math.PI - ((2.0 * Math.PI * y) / Math.pow(2.0, aZoom));
		return 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
	}

	public static final int IO_BUFFER_SIZE = 8 * 1024;

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				android.util.Log.e("IO", "Could not close stream", e);
			}
		}
	}

	public static boolean isSDMounted() {
		try {
			return Environment.getExternalStorageState().compareTo(
					Environment.MEDIA_MOUNTED) == 0;
			// return false;
		} catch (Exception e) {
			android.util.Log.e("IO", "isSDMounted", e);
			return false;
		}
	}

//	public static void sendExceptionEmail(final Context ctx, final String pBody) {
//		openEmail(ctx, pBody, "gvSIG Mini-Exception (v" + ")",
//				new String[] { "aromeu@prodevelop.es" });
//		Toast.makeText(ctx, "Please describe your bug!", Toast.LENGTH_LONG)
//				.show();
//	}

	public static void openEmail(final Context ctx, final String pBody,
			final String pSubject, final String[] pReceivers, String fileAttach) {
		final Intent mailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// final Intent mailIntent = new Intent(Intent.ACTION_WEB_SEARCH);

		// mailIntent.putExtra(SearchManager.QUERY, "alrocar");

		mailIntent.setType("plain/text");

		if (pReceivers != null && pReceivers.length > 0) {
			mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, pReceivers);
		}

		if (pSubject != null) {
			mailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, pSubject);
		}

		if (pBody != null) {
			mailIntent.putExtra(android.content.Intent.EXTRA_TEXT, pBody);
		}
		mailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileAttach));
		mailIntent.setType("text/plain");

		ctx.startActivity(Intent
				.createChooser(mailIntent, "Select Mail Client"));
	}

	public static void openWeb(final Context ctx, final String pBody,
			final String pSubject, final String[] pReceivers) {
//		final Intent mailIntent = new Intent(android.content.Intent.ACTION_SEND);
//		 final Intent mailIntent = new Intent(Intent.ACTION_WEB_SEARCH);
//
//		 mailIntent.putExtra(SearchManager.QUERY, "gvsigminilayers.zip");
		 final Intent mailIntent = new Intent(Intent.ACTION_WEB_SEARCH);

		 mailIntent.putExtra(SearchManager.QUERY, "gvsigminilayers.gvtiles");		 

//		mailIntent.setType("plain/text");
//
//		if (pReceivers != null && pReceivers.length > 0) {
//			mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, pReceivers);
//		}
//
//		if (pSubject != null) {
//			mailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, pSubject);
//		}
//
//		if (pBody != null) {
//			mailIntent.putExtra(android.content.Intent.EXTRA_TEXT, pBody);
//		}
//		mailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+fileAttach));
//		mailIntent.setType("text/plain");
//
		ctx.startActivity(Intent
				.createChooser(mailIntent, "Select Web Browser"));
	}
	
	public static int getNextSquareNumberAbove(final double factor) {
		int out = 0;
		int cur = 1;
		int i = 1;
		while (true) {
			if (cur > factor) {
				return out;
			}

			out = i;
			cur *= 2;
			i++;
		}
	}

	public static InputStream openConnection(String query) throws IOException {
		final URL url = new URL(query.replace(" ", "%20"));
		URLConnection urlconnec = url.openConnection();
		urlconnec.setConnectTimeout(30000);
		return urlconnec.getInputStream();
	}	
	
	public static void downloadLayerFile(final Context ctx) {
		openWeb(ctx, "", ctx.getResources().getString(R.string.app_name_itemizedoverlay) + "-Exception",
				new String[] { getSupportMail() });		
	}
	
	public static void askLayers(final Context ctx) {
		openWeb(ctx, "", ctx.getResources().getString(R.string.app_name_itemizedoverlay) + "-Layers",
				new String[] { getSupportMail() });		
	}
	
	public static String getSupportMail() {
		return SUPPORT_MAIL.replace("[at]", "@").replace("[dot]", ".");
	}
	
	
	
}
