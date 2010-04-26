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

/**
 * A container for GPS locations
 * @author aromeu 
 * @author rblanco
 *
 */
public class GPSPoint implements GeoUtils {

	private int mLongitudeE6;
	private int mLatitudeE6;
	public float acc = 0;

	public GPSPoint(final int aLatitudeE6, final int aLongitudeE6) {
		this.mLatitudeE6 = aLatitudeE6;
		this.mLongitudeE6 = aLongitudeE6;

	}

	public static GPSPoint fromDoubleString(final String s, final char spacer) {
		final int spacerPos = s.indexOf(spacer);
		return new GPSPoint((int) (Double.parseDouble(s.substring(0,
				spacerPos - 1)) * 1E6), (int) (Double.parseDouble(s.substring(
				spacerPos + 1, s.length())) * 1E6));
	}

	public static GPSPoint fromIntString(final String s) {
		final int commaPos = s.indexOf(',');
		return new GPSPoint(Integer.parseInt(s.substring(0, commaPos - 1)),
				Integer.parseInt(s.substring(commaPos + 1, s.length())));
	}

	public int getLongitudeE6() {
		return this.mLongitudeE6;
	}

	public int getLatitudeE6() {
		return this.mLatitudeE6;
	}

	public void setLongitudeE6(final int aLongitudeE6) {
		this.mLongitudeE6 = aLongitudeE6;
	}

	public void setLatitudeE6(final int aLatitudeE6) {
		this.mLatitudeE6 = aLatitudeE6;
	}

	public void setCoordsE6(final int aLatitudeE6, final int aLongitudeE6) {
		this.mLatitudeE6 = aLatitudeE6;
		this.mLongitudeE6 = aLongitudeE6;
	}

	@Override
	public String toString() {
		return new StringBuilder().append(this.mLatitudeE6).append(",").append(
				this.mLongitudeE6).toString();
	}

	public String toDoubleString() {
		return new StringBuilder().append(this.mLatitudeE6 / 1E6).append(",")
				.append(this.mLongitudeE6 / 1E6).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GPSPoint))
			return false;
		GPSPoint g = (GPSPoint) obj;
		return g.mLatitudeE6 == this.mLatitudeE6
				&& g.mLongitudeE6 == this.mLongitudeE6;
	}

	/**
	 * @see Source@ http://www.geocities.com/DrChengalva/GPSDistance.html
	 * @param gpA
	 * @param gpB
	 * @return distance in meters
	 */	
	
	public int distanceTo(final GPSPoint other) {

		final double a1 = DEG2RAD * (this.mLatitudeE6 / 1E6);
		final double a2 = DEG2RAD * (this.mLongitudeE6 / 1E6);
		final double b1 = DEG2RAD * (other.mLatitudeE6 / 1E6);
		final double b2 = DEG2RAD * (other.mLongitudeE6 / 1E6);

		final double cosa1 = Math.cos(a1);
		final double cosb1 = Math.cos(b1);

		final double t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2);

		final double t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2);

		final double t3 = Math.sin(a1) * Math.sin(b1);

		final double tt = Math.acos(t1 + t2 + t3);

		return (int) (RADIUS_EARTH_METERS * tt);
	}

}
