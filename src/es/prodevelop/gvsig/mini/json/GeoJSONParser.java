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

package es.prodevelop.gvsig.mini.json;

import java.util.Enumeration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.FeatureCollection;
import es.prodevelop.gvsig.mini.geom.IGeometry;
import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.geom.MultiLineString;
import es.prodevelop.gvsig.mini.geom.MultiPoint;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.geom.Polygon;

;

/**
 * Utility class to encode geometries into the GeoJSON exchange format
 * 
 * @author Alberto Romeu Carrasco (aromeu@prodevelop.es)
 */
public class GeoJSONParser {

	private final JSONStringer builder;

	/**
	 * Constructor.
	 * 
	 * @param builder
	 */
	public GeoJSONParser(JSONStringer builder) {
		this.builder = builder;
	}

	/**
	 * Main method to encode geometries.
	 * 
	 * @param o
	 *            The object to encode: It can be one of: <code>Feature</code>
	 *            <code>FeatureCollection</code> <code>Point</code>
	 *            <code>MultiPoint</code>
	 * @return The GeoJSON String with the object encoded
	 * @throws JSONException
	 */
	public String encode(Object o) throws JSONException {
		if (o instanceof Feature) {
			encodeFeature((Feature) o);
		} else if (o instanceof FeatureCollection) {
			encodeFeatureCollection((FeatureCollection) o);
		} else if (o instanceof Point) {
			encodeGeometry((Point) o);
		} else if (o instanceof MultiPoint) {
			encodeGeometry((MultiPoint) o);
		} else {
			// TODO
			// throw exception;
		}
		return builder.toString();
	}

	/**
	 * Main method to decode a GeoJSON String
	 * 
	 * @param s
	 *            The string in GeoJSON format
	 * @return if the paramater has inside a FeatureCollection tag, the method
	 *         will the decode the GeoJSON String and it will return a
	 *         FeatureCollection otherwise, it will return null;
	 * @throws JSONException
	 */
	public FeatureCollection decode(String s) throws JSONException {
		JSONTokener tokener = new JSONTokener(s);

		JSONObject o = new JSONObject(tokener);
		if (o.get("type").equals("FeatureCollection")) {
			return this.decodeFeatureCollection(o);
		} else {
			// TODO
			// throw exception
			return null;
		}

	}

	private FeatureCollection decodeFeatureCollection(JSONObject object)
			throws JSONException {

		FeatureCollection fCollection = null;

		JSONArray array = object.getJSONArray("features");
		if (array != null) {
			fCollection = new FeatureCollection();
			for (int i = 0; i < array.length(); i++) {
				JSONObject o = array.getJSONObject(i);
				if (o.get("type").equals("Feature")) {
					Feature f = this.decodeFeature(o);
					fCollection.addFeature(f);
				}
			}
		}

		return fCollection;
	}

	private Feature decodeFeature(JSONObject object) throws JSONException {
		Feature feature = null;
		JSONObject geom = object.getJSONObject("geometry");

		if (geom != null) {
			IGeometry geometry = this.decodeGeometry(geom);
			feature = new Feature(geometry);
		}

		// JSONObject properties = object.getJSONObject("properties");
		// String text = properties.get("TEXT").toString();
		// int id = Integer.valueOf(properties.get("ID").toString()).intValue();
		// double cost = Double.valueOf(properties.get("COST").toString())
		// .doubleValue();
		// double length = Double.valueOf(properties.get("LENGTH").toString())
		// .doubleValue();
		// RouteProperties r = new RouteProperties(text, cost, length);
		// feature.setID(id);
		// feature.setProp(r);
		return feature;
	}

	private IGeometry decodeGeometry(JSONObject object) throws JSONException {
		IGeometry geom = null;
		LineString[] lineStrings;
		if (object.get("type").equals("MultiLineString")) {
			JSONArray coordinates = object.getJSONArray("coordinates");
			int size = coordinates.length();
			lineStrings = new LineString[size];
			LineString l;
			for (int i = 0; i < size; i++) {
				JSONArray lineStrCoord = coordinates.getJSONArray(i);
				double[][] coords = this.decodeLineStringCoords(lineStrCoord);
				l = new LineString(coords[0], coords[1]);
				lineStrings[i] = l;
			}

			geom = new MultiLineString(lineStrings);
		} else if (object.get("type").equals("LineString")) {
			JSONArray coordinates = object.getJSONArray("coordinates");
			double[][] coords = this.decodeLineStringCoords(coordinates);
			geom = new LineString(coords[0], coords[1]);
		} else if (object.get("type").equals("Point")) {
			JSONArray coordinates = object.getJSONArray("coordinates");
			geom = new Point(coordinates.getDouble(0), coordinates.getDouble(1));
		} else if (object.get("type").equals("Polygon")) {
			JSONArray coordinates = object.getJSONArray("coordinates");
			double[][] coords = this.decodeLineStringCoords(coordinates);
			geom = new Polygon(coords[0], coords[1]);
		}

		return geom;
	}

	private double[][] decodeLineStringCoords(JSONArray coordinates)
			throws JSONException {
		int size = coordinates.length();
		double[][] coordsLineStr = new double[2][size];
		for (int i = 0; i < size; i++) {
			JSONArray lineStrCoord = coordinates.getJSONArray(i);
			double[] coords = this.decodeCoordinates(lineStrCoord);
			coordsLineStr[0][i] = coords[0];
			coordsLineStr[1][i] = coords[1];
		}
		return coordsLineStr;
	}

	private double[] decodeCoordinates(JSONArray coordinates)
			throws JSONException {

		double[] coords = new double[2];
		int size = coordinates.length();
		for (int i = 0; i < size; i++) {
			coords[i] = Double.parseDouble(coordinates.get(i).toString());
		}
		return coords;
	}

	private void encodeFeatureCollection(FeatureCollection c)
			throws JSONException {
		builder.object();
		builder.key("type").value("FeatureCollection");
		builder.key("features");
		builder.array();

		Enumeration e = c.getFeatures().elements();

		while (e.hasMoreElements()) {
			Feature f = (Feature) e.nextElement();
			encodeFeature(f);
		}

		builder.endArray();
		builder.endObject();
	}

	private void encodeFeature(Feature f) throws JSONException {
		builder.object();
		builder.key("type").value("Feature");
		// builder.key("id").value(f.getID());
		builder.key("geometry");

		IGeometry g = f.getGeometry();
		encodeGeometry(g);

		builder.key("properties");
		builder.object();
		// TODO encode Feature
		// f.toJSON(builder);
		builder.endObject();
		builder.endObject();
	}

	private void encodeGeometry(IGeometry g) throws JSONException {

		builder.object();
		builder.key("type");
		String name = getGeometryName(g);
		if (name != null) {
			builder.value(name);
			if (name.compareTo("Point") == 0) {
				builder.key("coordinates");
				encodeCoordinate((Point) g);
			} else if (name.compareTo("MultiPoint") == 0) {
				MultiPoint mp = (MultiPoint) g;
				builder.key("coordinates");
				builder.array();
				for (int i = 0; i < mp.getNumPoints(); i++) {
					encodeCoordinate(((MultiPoint) g).getPoint(i));
				}
				builder.endArray();
			}
		}
		builder.endObject();
	}

	// /**
	// * Write the coordinates of a geometry
	// * @param coords The coordinates to encode
	// * @throws JSONException
	// */
	// private void encodeCoordinates(double[] coords)
	// throws JSONException {
	// builder.array();
	//
	// for (int i = 0; i < coords.length; i++) {
	// Point coord = new Point(coords[0], coords[1]);
	// encodeCoordinate(coord);
	// }
	//
	// builder.endArray();
	// }

	private void encodeCoordinate(Point coord) throws JSONException {
		builder.array();
		builder.value(coord.getX());
		builder.value(coord.getY());
		builder.endArray();
	}

	private void encodeBoundingBox(Extent env) throws JSONException {
		builder.key("bbox");
		builder.array();
		builder.value(env.getMinX());
		builder.value(env.getMinY());
		builder.value(env.getMaxX());
		builder.value(env.getMaxY());
		builder.endArray();
	}

	private String getGeometryName(IGeometry geometry) {
		if (geometry.getClass() == Point.class) {
			return "Point";
		} else if (geometry.getClass() == MultiPoint.class) {
			return "MultiPoint";
		} else if (geometry.getClass() == LineString.class) {
			return "LineString";
		} else if (geometry.getClass() == MultiLineString.class) {
			return "MultiLineString";
		} else {
			return "";
		}
	}
}
