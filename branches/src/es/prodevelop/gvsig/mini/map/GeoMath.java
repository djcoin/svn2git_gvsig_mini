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
 *   author Rubén Blanco rblanco@prodevelop.es
 *
 *
 * Original version of the code made by Nicolas Gramlich.
 * No header license code found on the original source file.
 * 
 * Original source code downloaded from http://code.google.com/p/osmdroid/
 * package org.andnav.osm.util;
 * 
 * License stated in that website is 
 * http://www.gnu.org/licenses/lgpl.html
 * As no specific LGPL version was specified, LGPL license version is LGPL v2.1
 * is assumed.
 *  
 * 
 */

package es.prodevelop.gvsig.mini.map;

import java.util.ArrayList;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;


public class GeoMath implements GeoUtils {

	protected final int mLatNorthE6;
	protected final int mLatSouthE6;
	protected final int mLonEastE6;
	protected final int mLonWestE6;
	private static int RADIUS = 6371;
	private final static Logger log = LoggerFactory.getLogger(GeoMath.class);

	public GeoMath(final int northE6, final int eastE6, final int southE6,
			final int westE6) {
		this.mLatNorthE6 = northE6;
		this.mLatSouthE6 = southE6;
		this.mLonWestE6 = westE6;
		this.mLonEastE6 = eastE6;
	}

	public GeoMath(final double north, final double east, final double south,
			final double west) {
		this.mLatNorthE6 = (int) (north * 1E6);
		this.mLatSouthE6 = (int) (south * 1E6);
		this.mLonWestE6 = (int) (west * 1E6);
		this.mLonEastE6 = (int) (east * 1E6);
	}

	public int getDiagonalLengthInMeters() {		
		return new GPSPoint(this.mLatNorthE6, this.mLonWestE6)
				.distanceTo(new GPSPoint(this.mLatSouthE6, this.mLonEastE6));
	}

	public int getLatNorthE6() {
		return this.mLatNorthE6;
	}

	public int getLatSouthE6() {
		return this.mLatSouthE6;
	}

	public int getLonEastE6() {
		return this.mLonEastE6;
	}

	public int getLonWestE6() {
		return this.mLonWestE6;
	}

	public int getLatitudeSpanE6() {
		return Math.abs(this.mLatNorthE6 - this.mLatSouthE6);
	}

	public int getLongitudeSpanE6() {
		return Math.abs(this.mLonEastE6 - this.mLonWestE6);
	}

	public float[] getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(
			final int aLatitude, final int aLongitude, final float[] reuse) {
		float[] out = (reuse != null) ? reuse : new float[2];
		out[MAPTILE_LATITUDE_INDEX] = ((float) (this.mLatNorthE6 - aLatitude) / getLatitudeSpanE6());
		out[MAPTILE_LONGITUDE_INDEX] = 1 - ((float) (this.mLonEastE6 - aLongitude) / getLongitudeSpanE6());
		return out;
	}

	public float[] getRelativePositionOfGeoPointInBoundingBoxWithExactGudermannInterpolation(
			final int aLatitudeE6, final int aLongitudeE6, final float[] reuse) {
		float[] out = (reuse != null) ? reuse : new float[2];
		out[MAPTILE_LATITUDE_INDEX] = (float) ((gudermannInverse(this.mLatNorthE6 / 1E6) - gudermannInverse(aLatitudeE6 / 1E6)) / (gudermannInverse(this.mLatNorthE6 / 1E6) - gudermannInverse(this.mLatSouthE6 / 1E6)));
		out[MAPTILE_LONGITUDE_INDEX] = 1 - ((float) (this.mLonEastE6 - aLongitudeE6) / getLongitudeSpanE6());
		return out;
	}

	public GPSPoint getGeoPointOfRelativePositionWithLinearInterpolation(
			final float relX, final float relY) {

		int lat = (int) (this.mLatNorthE6 - (this.getLatitudeSpanE6() * relY));

		int lon = (int) (this.mLonWestE6 + (this.getLongitudeSpanE6() * relX));

		while (lat > 91000000)
			lat = 91000000;
		while (lat < -91000000)
			lat = -91000000;

		while (lon > 180000000)
			lon = 180000000;
		while (lon < -180000000)
			lon = -180000000;

		return new GPSPoint(lat, lon);
	}

	public GPSPoint getGeoPointOfRelativePositionWithExactGudermannInterpolation(
			final float relX, final float relY) {

		final double gudNorth = gudermannInverse(this.mLatNorthE6 / 1E6);
		final double gudSouth = gudermannInverse(this.mLatSouthE6 / 1E6);
		final double latD = gudermann((gudSouth + (1 - relY)
				* (gudNorth - gudSouth)));
		int lat = (int) (latD * 1E6);

		int lon = (int) ((this.mLonWestE6 + (this.getLongitudeSpanE6() * relX)));

		while (lat > 91000000)
			lat = 91000000;
		while (lat < -91000000)
			lat = -91000000;

		while (lon > 180000000)
			lon = 180000000;
		while (lon < -180000000)
			lon = -180000000;

		return new GPSPoint(lat, lon);
	}

	@Override
	public String toString() {
		return new StringBuffer().append("N:").append(this.mLatNorthE6).append(
				"; E:").append(this.mLonEastE6).append("; S:").append(
				this.mLatSouthE6).append("; W:").append(this.mLonWestE6)
				.toString();
	}

	public static GeoMath fromGeoPoints(
			final ArrayList<? extends GPSPoint> partialPolyLine) {
		int minLat = Integer.MAX_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int maxLon = Integer.MIN_VALUE;
		for (GPSPoint gp : partialPolyLine) {
			final int latitudeE6 = gp.getLatitudeE6();
			final int longitudeE6 = gp.getLongitudeE6();

			minLat = Math.min(minLat, latitudeE6);
			minLon = Math.min(minLon, longitudeE6);
			maxLat = Math.max(maxLat, latitudeE6);
			maxLon = Math.max(maxLon, longitudeE6);
		}

		return new GeoMath(minLat, maxLon, maxLat, minLon);
	}

	public static double gudermannInverse(double aLatitude) {
		return Math.log(Math.tan(PI_4 + (DEG2RAD * aLatitude / 2)));
	}

	public static double gudermann(double y) {
		return RAD2DEG * Math.atan(Math.sinh(y));
	}

	public static int mod(int number, final int modulus) {
		if (number > 0)
			return number % modulus;

		while (number < 0)
			number += modulus;

		return number;
	}

    /**
     * Distance between two points in kilometers.
     */
    public static double calcDistanceInKM(double lat1, double lon1, double lat2, double lon2) {
        double l1Rad = Math.toRadians(lat1);
        double l2Rad = Math.toRadians(lat2);
        double dLonRad = Math.toRadians(lon2 - lon1);
        
        return Math.acos(Math.sin(l1Rad) * Math.sin(l2Rad) + Math.cos(l1Rad) * Math.cos(l2Rad)
                * Math.cos(dLonRad))
                * RADIUS;
    }
    
    /**
     * Bearing between the two points in degrees
     */
    public static double calculateBearing(double lat1, double lon1, double lat2, double lon2) {
        double l1Rad = Math.toRadians(lat1);
        double l2Rad = Math.toRadians(lat2);
        double dLonRad = Math.toRadians(lon2 - lon1);

        double y = Math.sin(dLonRad) * Math.cos(l2Rad);
        double x = Math.cos(l1Rad) * Math.sin(l2Rad) - Math.sin(l1Rad) * Math.cos(l2Rad)
                * Math.cos(dLonRad);
        
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;        
    }
}
