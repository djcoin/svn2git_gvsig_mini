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

package es.prodevelop.gvsig.mini.test;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONStringer;

import es.prodevelop.gvsig.mini.geom.FeatureCollection;
import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.json.GeoJSONParser;

public class TestGeoJSONParser extends TestCase {

	String geoJSONLineString = "";
	String geoJSONPoint = "";

	public void setUp() {
		geoJSONLineString = "{ \"type\": \"FeatureCollection\",\"features\": [{ \"type\": \"Feature\",\"geometry\": {\"type\": \"LineString\",\"coordinates\": [[0.0,0.0],[5.0,0.0],[5.0,4.0],[0.0,3.0],[0.0,0.0]]},\"properties\": {}}]}";
		geoJSONPoint = "{ \"type\": \"FeatureCollection\",\"features\": [{ \"type\": \"Feature\",\"geometry\": {\"type\": \"Point\", \"coordinates\": [2.61904761904762,1.76190476190476]},\"properties\": {}}]}";

	}

	public void testDecodeLineString() {
		GeoJSONParser g = new GeoJSONParser(new JSONStringer());
		try {
			FeatureCollection f = g.decode(geoJSONLineString);
			assertEquals(f.getSize(), 1);
			LineString l = (LineString)f.getFeatureAt(0).getGeometry();
			
			assertEquals(l.getXCoords()[0], 0.0);
			assertEquals(l.getXCoords()[1], 5.0);
			assertEquals(l.getXCoords()[2], 5.0);
			assertEquals(l.getXCoords()[3], 0.0);
			assertEquals(l.getXCoords()[4], 0.0);
			
			assertEquals(l.getYCoords()[0], 0.0);
			assertEquals(l.getYCoords()[1], 0.0);
			assertEquals(l.getYCoords()[2], 4.0);
			assertEquals(l.getYCoords()[3], 3.0);
			assertEquals(l.getYCoords()[4], 0.0);			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDecodePoint() {
		GeoJSONParser g = new GeoJSONParser(new JSONStringer());
		try {
			FeatureCollection f = g.decode(geoJSONPoint);
			assertEquals(f.getSize(), 1);
			Point p = (Point)f.getFeatureAt(0).getGeometry();
			assertEquals(p.getX(), 2.61904761904762);
			assertEquals(p.getY(), 1.76190476190476);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}