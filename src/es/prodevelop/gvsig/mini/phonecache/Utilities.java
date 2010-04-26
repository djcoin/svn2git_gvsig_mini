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
package es.prodevelop.gvsig.mini.phonecache;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

/**
 * Utility class
 * 
 * @author Alberto Romeu Carrasco (aromeu@prodevelop.es)
 */
public class Utilities {

	public static Hashtable canceledGroup = new Hashtable();
	private static int groupID = -10;

	/**
	 * Checks if the Location API is supported into the current phone
	 * 
	 * @return True if it is supported
	 */
	public static boolean isLocationApiSupported() {
		try {
			Class.forName("javax.microedition.location.LocationProvider");
			Class.forName("javax.microedition.location.Criteria");
			return true;
		} catch (final java.lang.NoClassDefFoundError c) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks if the Location API is supported into the current phone
	 * 
	 * @return True if it is supported
	 */
	public static boolean isWebServiceApiSupported() {
		try {
			Class.forName("javax.microedition.xml.rpc.Operation");
			return true;
		} catch (final java.lang.NoClassDefFoundError c) {
			return false;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if the FileConnection API is supported into the current phone
	 * 
	 * @return True if it is supported
	 */
	public static boolean isFileConnectionApiSupported() {
		try {
			Class.forName("javax.microedition.io.file.FileConnection");
			return true;
		} catch (final java.lang.NoClassDefFoundError c) {
			return false;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String trimDecimals(final String number,
			final int numberDecimals) {
		try {
			final int length = number.length();
			for (int i = 0; i < length; i++) {
				if (String.valueOf(number.charAt(i)).compareTo(".") == 0) {
					return number.substring(0, i + numberDecimals + 1);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return number;
	}

	public final static boolean isEmpty(final String string) {
		return (string == null || string.trim().compareTo("") == 0);

	}

	public static boolean checkValidExtent(final Extent ex, final String srs) {

		if (ex == null) {
			return false;
		} else {
			// if(CRSFactory.getCRS(srs).getUnitsAbbrev().compareTo("u") == 0) {
			// return false;
			// }

			// if (srs.indexOf("EPSG:4") == 0 && srs.length() == 9) {
			// if (ex.getMinX() < -180 || ex.getMinX() > 180) {
			// return false;
			// }
			// if (ex.getMinY() < -90 || ex.getMinY() > 90) {
			// return false;
			// }
			// if (ex.getMaxX() < -180 || ex.getMaxX() > 180) {
			// return false;
			// }
			// if (ex.getMaxY() < -90 || ex.getMaxY() > 90) {
			// return false;
			// }
			// }

			if (ex.area() <= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * URLEncode method.
	 * 
	 * @param s
	 *            string to urlencode
	 * @return urlencoded string
	 */
	public static String urlEncode(final String s) {
		if (s == null) {
			return null;
		}

		final String blank = "%20";
		final StringTokenizer st = new StringTokenizer(s.trim(), " ");

		final int size = st.countTokens();

		if (size <= 0) {
			return s;
		}

		StringBuffer url = new StringBuffer();
		int i = 0;
		while (st.hasMoreTokens()) {
			i++;
			url.append(st.nextToken());
			if (i < size) {
				url.append(blank);
			}
		}
		return url.toString();
	}

	/**
	 * Parses the description of a named instance of a Name Finder response. The
	 * description contains tags such as "<strong>" that this method deletes
	 * 
	 * @param description
	 *            The description
	 * @return A clean description to show on the screen
	 */
	public static String parseNamedDescription(final String description) {

		StringBuffer res = null;
		try {
			if (!Utilities.isEmpty(description)) {
				final StringBuffer s = new StringBuffer(description);
				final int size = s.length();
				int ch;
				boolean skip = false;
				res = new StringBuffer();
				for (int i = 0; i < size; i++) {
					ch = s.charAt(i);

					if (ch == 60 || ch == 91) {
						skip = true;
					}
					if (!skip) {
						res.append(s.charAt(i));
					}
					if (ch == 62 || ch == 93) {
						skip = false;
					}
				}
			}
			return res.toString();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Resturns a unique cancellable ID
	 * 
	 * @return
	 */
	public static synchronized Integer getCancellableID() {
		return new Integer(groupID--);
	}
	
	public static synchronized Cancellable getNewCancellable() {
		groupID++;
		Cancellable c = new Cancellable();
		c.groupID = groupID;
		return c;
	}

	/**
	 * Orders an array of points with the quicksort algorithm
	 * 
	 * @param a
	 *            The array of points to be ordered
	 * @param center
	 *            The point to compare with. The array will be sorted from
	 *            nearest to the center to furthest
	 */
	public static void quicksort(final Point[] a, final Point center) {
		quicksort(a, 0, a.length - 1, center);
	}

	// quicksort a[left] to a[right]
	private static void quicksort(final Point[] a, final int left,
			final int right, final Point center) {
		if (right <= left) {
			return;
		}
		int i = partition(a, left, right, center);
		quicksort(a, left, i - 1, center);
		quicksort(a, i + 1, right, center);
	}

	// partition a[left] to a[right], assumes left < right
	private static int partition(final Point[] a, final int left,
			final int right, final Point center) {
		int i = left - 1;
		int j = right;
		while (true) {

			while (less(a[++i].distance(center), a[right].distance(center)))
				// find item on left to swap
				; // a[right] acts as sentinel

			while (less(a[right].distance(center), a[right].distance(center))) // find
																				// item
																				// on
																				// right
																				// to
																				// swap
			{
				if (j == left) {
					break; // don't go out-of-bounds

				}
			}
			if (i >= j) {
				break; // check if pointers cross

			}
			exch(a, i, j); // swap two elements into place

		}
		exch(a, i, right); // swap with partition element

		return i;
	}

	// is x < y ?
	private static boolean less(final double x, final double y) {
		return (x < y);
	}

	// exchange a[i] and a[j]
	private static void exch(final Point[] a, final int i, final int j) {
		Point swap = a[i];

		a[i] = a[j];
		a[j] = swap;
	}

	public static synchronized File downloadFile(URL url, String name,
			Cancellable cancel, String type, DownloadWaiter waiter) throws IOException,
			ConnectException, UnknownHostException {
		File f = null;

		if (cancel.getCanceled()) {
			waiter.downloadCanceled();
			return null;
		}
		
		File tempDirectory = new File(name);
		if (!tempDirectory.exists()) {
			tempDirectory.mkdirs();
		}
		f = new File(new StringBuffer().append(name).append("a").append(type)
				.toString());

		DataOutputStream dos;
		try {
			DataInputStream is;
			OutputStreamWriter os = null;
			
			is = new DataInputStream(url.openStream());

			dos = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(f)));
			byte[] buffer = new byte[1024 * 4];

			long readed = 0;
			for (int i = is.read(buffer); i > 0; i = is.read(buffer)) {
				if (cancel.getCanceled()) {
					waiter.downloadCanceled();
					return null;
				}

				dos.write(buffer, 0, i);
				readed += i;

			}

			if (os != null) {
				os.close();
			}
			dos.close();
			is.close();
			is = null;
			dos = null;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return f;
	}

	public static synchronized File downloadFileYahoo(URL url, String name,
			Cancellable cancel, String type, DownloadWaiter waiter) throws IOException,
			ConnectException, UnknownHostException {
		File f = null;
		
		if (cancel.getCanceled()) {
			waiter.downloadCanceled();
			return null;
		}
		
		File tempDirectory = new File(name);
		if (!tempDirectory.exists()) {
			tempDirectory.mkdirs();
		}
		f = new File(new StringBuffer().append(name).append("a").append(type)
				.toString());

		DataOutputStream dos;
		try {
			DataInputStream is;
			OutputStreamWriter os = null;
			HttpURLConnection connection = null;
			// If the used protocol is HTTPS

			connection = (HttpURLConnection) url.openConnection();

			is = new DataInputStream(url.openStream());

			dos = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(f)));
			byte[] buffer = new byte[1024 * 4];

			long readed = 0;
			for (int i = is.read(buffer); i > 0; i = is.read(buffer)) {
				if (cancel.getCanceled()) {
					waiter.downloadCanceled();
					return null;
				}
				String s = new String(buffer);
				int index = s.indexOf("image/jpeg\r\n\r\n");
				if (index == -1) {
					dos.write(buffer, 0, i);
					readed += i;
				} else {
					for (int j = index + 14; j < 1024 * 4; j++) {
						dos.write(buffer[j]);
					}
					// s = s.substring(index + 14);
					// dos.write(s.getBytes(), 0, s.getBytes().length);
					// readed += s.getBytes().length;
				}

			}

			if (os != null) {
				os.close();
			}
			dos.close();
			is.close();
			is = null;
			dos = null;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return f;
	}
}
