/* SIGATEX. Gestión de Recursos Turísticos de Extremadura
 * Funded by European Union. FEDER program.
 *
 * Copyright (C) 2008 Junta de Extremadura.
 * Developed by: Prodevelop, S.L.
 * For more information, contact:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-  1307,USA.
 *
 * For more information, contact:
 *
 * Junta de Extremadura
 * Consejería de Cultura y Turismo
 * C/ Almendralejo 14 Mérida
 * 06800 Badajoz
 * SPAIN
 *
 * Tel: +34 924007009
 * http://www.culturaextremadura.com
 *
 * or
 *
 * Prodevelop, S.L.
 * Pza. Juan de Villarrasa, 14 - 5
 * Valencia
 * Spain
 * Tel: +34 963510612
 * e-mail: prode@prodevelop.es
 * http://www.prodevelop.es
 *
 *
 * Modified for gvSIG Mini.
 */
package es.prodevelop.gvsig.mini.yours;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

import bm.core.tools.StringTokenizer;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.geom.Feature;
import es.prodevelop.gvsig.mini.geom.FeatureCollection;
import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.geom.MultiPoint;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Tags;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.gvsig.mobile.fmap.proj.Projection;

/**
 * This class contains every RoutePoint selected by the user and parses the
 * response from the YOURS service
 * 
 * @author Alberto Romeu Carrasco (aromeu@prodevelop.es)
 * @author aromeu 
 * @author rblanco - modified for gvSIG phone
 */
public class Route {

	private Point startPoint = null;
	private Point endPoint = null;
	private int state = 0;
	private String transport = "motorcar";
	private int type = 0;
	private String UIID = "";
	private FeatureCollection routeCollection;
	public boolean done = false;
	public boolean iscancelled = false;

	/**
	 * transport types of the YOURS service. motorcar, bicycle, foot
	 */
	public String[] transportTypes = new String[] { "motorcar", "bicycle",
			"foot" };

	/**
	 * type of the route shortest, fastest
	 */
	public String[] routeType = new String[] { "shortest", "fastest" };
	protected Projection projection;
	private static final String KML_ROOT = "kml";
	private static final String COORDINATES_TAG = "coordinates";
	private static final String url = " http://www.yournavigation.org/api/1.0/gosmore.php?";
	private static final String FLAT = "flat=";
	private static final String FLON = "&flon=";
	private static final String TLAT = "&tlat=";
	private static final String TLON = "&tlon=";
	private static final String TRANSPORT = "&v=";
	private static final String TYPE = "&fast=";

	/**
	 * The constructor
	 * 
	 * @param baseLayer
	 *            The base layer of the map
	 */
	public Route() {
		routeCollection = new FeatureCollection();
	}

	/**
	 * Sets the origin of the route
	 * 
	 * @param point
	 *            The RoutePoint representing the origin
	 */
	public void setStartPoint(final Point point) {
		if (point.distance(0,0) == 0) return;
		switch (state) {
		case Tags.ROUTE_EMPTY:
			this.startPoint = point;
			this.state = Tags.ROUTE_WITH_START_POINT;
			break;

		case Tags.ROUTE_WITH_2_POINT:
			this.startPoint = point;
			this.state = Tags.ROUTE_WITH_2_POINT;
			break;

		case Tags.ROUTE_WITH_END_POINT:
			this.startPoint = point;
			this.state = Tags.ROUTE_WITH_2_POINT;
			break;

		case Tags.ROUTE_WITH_PASS_POINT:
			this.startPoint = point;
			this.state = Tags.ROUTE_WITH_START_AND_PASS_POINT;
			break;

		case Tags.ROUTE_WITH_N_POINT:
			this.startPoint = point;
			this.state = Tags.ROUTE_WITH_N_POINT;
			break;
			
		case Tags.ROUTE_WITH_START_POINT:
			this.startPoint = point;
			this.state = Tags.ROUTE_WITH_START_POINT;
			break;

		default:

			break;
		}

	}

	/**
	 * Deletes the origin of the route
	 * 
	 * @param point
	 */
	public void deleteStartPoint() {
		switch (state) {
		case Tags.ROUTE_WITH_START_POINT:
			this.startPoint = null;
			this.state = Tags.ROUTE_EMPTY;
			break;

		case Tags.ROUTE_WITH_START_AND_PASS_POINT:
			this.startPoint = null;
			this.state = Tags.ROUTE_WITH_PASS_POINT;
			break;

		default:

			break;
		}

	}

	/**
	 * Sets the destination of the route
	 * 
	 * @param point
	 */
	public void setEndPoint(final Point point) {
		if (point.distance(0,0) == 0) return;
		switch (state) {
		case Tags.ROUTE_EMPTY:
			this.endPoint = point;
			this.state = Tags.ROUTE_WITH_END_POINT;
			break;

		case Tags.ROUTE_WITH_START_POINT:
			this.endPoint = point;
			this.state = Tags.ROUTE_WITH_2_POINT;
			break;
			
		case Tags.ROUTE_WITH_END_POINT:
			this.endPoint = point;
			this.state = Tags.ROUTE_WITH_END_POINT;
			break;

		case Tags.ROUTE_WITH_2_POINT:
			this.endPoint = point;
			this.state = Tags.ROUTE_WITH_2_POINT;
			break;

		default:

			break;
		}

	}

	/**
	 * Deletes the end point
	 */
	public void deleteEndPoint() {
		switch (state) {
		case Tags.ROUTE_WITH_END_POINT:
			this.endPoint = null;
			this.state = Tags.ROUTE_EMPTY;
			break;

		default:

			break;
		}

	}

	/**
	 * Clears the route points
	 */
	public void deleteRoute(boolean deletePoints) {
		if (deletePoints) {
			this.endPoint = null;
			this.startPoint = null;
			this.state = Tags.ROUTE_EMPTY;
		}
		this.setRoute(new FeatureCollection());
	}

	/**
	 * Checks if the route has enough points to be calculated
	 * 
	 * @return True if can be calculated
	 */
	public boolean canCalculate() {
		if ((this.hasStartPoint() && this.hasEndPoint())) {
			return true;
		}

		return false;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public int getState() {
		return state;
	}

	public void setState(final int state) {
		this.state = state;
	}

	public boolean hasStartPoint() {
		if (this.startPoint == null) {
			return false;
		} else {
			return true;
		}

	}

	public boolean hasEndPoint() {
		if (this.endPoint == null) {
			return false;
		} else {
			return true;
		}

	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public String getUID() {
		return UIID;
	}

	public void setUID(final String UID) {
		this.UIID = UID;
	}

	/**
	 * Returns a multipoint with the points of the route. This multipoint will
	 * be draw on the screen representing the selected points by the user
	 * 
	 * @return A multipoint
	 */
	public MultiPoint toMultiPoint() {

		int size = 0;
		if (startPoint != null) {
			size++;
		}

		if (endPoint != null) {

			size++;
		}

		Point[] p = new Point[size];

		int i = 0;
		if (startPoint != null) {
			p[i] = startPoint;
			i++;
		}

		if (endPoint != null) {
			p[i] = endPoint;
		}

		return new MultiPoint(p);
	}

	public FeatureCollection getRoute() {
		return routeCollection;
	}

	public void setRoute(final FeatureCollection route) {
		this.routeCollection = route;
	}

	/**
	 * not used
	 * 
	 * @param segment
	 */
	public void addRouteSegment(final Feature segment) {
		if (this.routeCollection == null) {
			this.routeCollection = new FeatureCollection();
		}
		this.routeCollection.addFeature(segment);
	}

	/**
	 * Creates the query for the YOURS service
	 * 
	 * @param origin
	 *            The origin point in WGS84
	 * @param end
	 *            The origin point in WGS84
	 * @return The YOURS URL to calculate the route
	 */
	public String toYOURS(final Point origin, final Point end) {

		StringBuffer query = new StringBuffer(url);

		query.append(FLAT).append(origin.getY()).append(FLON).append(
				origin.getX()).append(TLAT).append(end.getY()).append(TLON)
				.append(end.getX()).append(TRANSPORT).append(this.transport)
				.append(TYPE).append(type);
		return query.toString();
	}

	/**
	 * Parses the kml response from the YOURS service
	 * 
	 * @param route
	 *            The data
	 */
	public int fromYOURS(final byte[] route) {
		int res = 0;

		final KXmlParser kxmlParser = new KXmlParser();
		try {
			kxmlParser.setInput(new ByteArrayInputStream(route), "UTF-8");
			kxmlParser.nextTag();
			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null, KML_ROOT);
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo(COORDINATES_TAG) == 0) {
							kxmlParser.next();
							final StringTokenizer st = new StringTokenizer(
									kxmlParser.getText(), "\n");
							double[] xCoords = new double[st.length - 1];
							double[] yCoords = new double[st.length - 1];

							int i = 0;
							// TODO: ALBERTO -> SIMPLIFICAR Y USAR MERCATOR
							final Projection proj = CRSFactory
									.getCRS("EPSG:3785");

							String point;
							Point temp = null;
							try {
								while (st.hasMoreTokens()) {
									point = st.nextToken();

									double[] pointCoord = new double[2];
									if (point != null
											&& point.trim().compareTo("") != 0) {
										if (iscancelled) {
											res = -2;
											return res;

										}
										temp = Point.parseString(point.trim());
										if (temp != null) {

											xCoords[i] = temp.getX();
											yCoords[i] = temp.getY();
											i++;
										}
									}
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (i < 2) {
								res = -3;
								return res;

							}

							Feature f = new Feature(new LineString(xCoords,
									yCoords));
							FeatureCollection fe = new FeatureCollection();
							fe.addFeature(f);
							this.setRoute(fe);
							res = 1;
						}

						break;
					case KXmlParser.END_TAG:
						break;

					}

					tag = kxmlParser.next();

				}
			}
		} catch (XmlPullParserException parser_ex) {
			parser_ex.printStackTrace();
			res = -2;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			res = -2;
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
			res = -2;
		} finally {
			return res;
		}
	}

	/**
	 * When changing the map base layer, if there was any route point, it must
	 * be reprojected
	 * 
	 * @param previous
	 *            The projection of the previous layer.
	 * @param current
	 *            The projection of the current layer. Coordinates of the route
	 *            must be reprojected to this projection.
	 */
	public void onChangeLayer(final Projection current) {
		try {
			boolean update = false;

			if (startPoint != null) {
				final double[] start = ConversionCoords.reproject(
						this.startPoint.getX(), this.startPoint.getY(),
						projection, current);
				if (start != null) {
					this.startPoint.setX(start[0]);
					this.startPoint.setY(start[1]);
					update = true;
				}
			}

			if (endPoint != null) {
				final double[] end = ConversionCoords.reproject(this.endPoint
						.getX(), this.endPoint.getY(), projection, current);
				if (end != null) {
					this.endPoint.setX(end[0]);
					this.endPoint.setY(end[1]);
					update = true;
				}
			}

			if (update) {
				projection = current;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() {
		try {
			this.routeCollection.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
