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

package es.prodevelop.gvsig.mini.openls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

import es.prodevelop.gvsig.mini.geom.LineString;
import es.prodevelop.gvsig.mini.ors.ORSInstruction;
import es.prodevelop.gvsig.mini.ors.ORSRoute;

public class OpenLSRouteParser {

	private ORSRoute route;

	public ORSRoute parse(final byte[] data) {

		final KXmlParser kxmlParser = new KXmlParser();

		route = new ORSRoute();

		try {
			kxmlParser.setInput(new ByteArrayInputStream(data), "UTF-8");
			kxmlParser.nextTag();
			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null, "xls:XLS");
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo("xls:TotalDistance") == 0) {
							parseDistance(kxmlParser);
						} else if (kxmlParser.getName().compareTo(
								"xls:TotalTime") == 0) {
						} else if (kxmlParser.getName().compareTo(
								"xls:BoundingBox") == 0) {
							parseBBox(kxmlParser);
						} else if (kxmlParser.getName().compareTo(
								"xls:RouteInstructionsList") == 0) {
							parseInstructions(kxmlParser);
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
			parser_ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
		} finally {
			return route;
		}
	}

	private void parseDistance(KXmlParser parser) {
		final int size = parser.getAttributeCount();

		for (int i = 0; i < size; i++) {
			if (parser.getAttributeName(i).compareTo("value") == 0) {
				route.setTotalDistance(new Double(parser.getAttributeValue(i))
						.doubleValue());
				return;
			}
		}
	}

	private void parseBBox(KXmlParser kxmlParser) {

		double[] leftBCorner = new double[2];
		double[] rightUCorner = new double[2];

		try {

			int i = 0;
			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null,
						"xls:BoundingBox");
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo("gml:pos") == 0) {
							String[] pos = parsePos(kxmlParser);
							if (i == 0) {
								leftBCorner[0] = new Double(pos[0])
										.doubleValue();
								leftBCorner[1] = new Double(pos[1])
										.doubleValue();
								i++;
							} else {
								rightUCorner[0] = new Double(pos[0])
										.doubleValue();
								rightUCorner[1] = new Double(pos[1])
										.doubleValue();
							}
						}
						break;

					case KXmlParser.END_TAG:
						if (kxmlParser.getName().compareTo("xls:BoundingBox") == 0) {
							return;
						}
						break;
					}
					tag = kxmlParser.next();
				}
			}
		} catch (XmlPullParserException parser_ex) {
			parser_ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
		} finally {
			route.setBBox(leftBCorner[0], leftBCorner[1], rightUCorner[0],
					rightUCorner[1]);
		}

	}

	private String[] parsePos(KXmlParser parser) {
		try {
			String pos = parser.nextText();
			return pos.split(" ");

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void parseInstructions(KXmlParser kxmlParser) {
		try {

			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null,
						"xls:RouteInstructionsList");
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo(
								"xls:RouteInstruction") == 0) {
							parseInstruction(kxmlParser);
						}
						break;

					case KXmlParser.END_TAG:
						if (kxmlParser.getName().compareTo(
								"xls:RouteInstructionsList") == 0) {
							return;
						}
						break;
					}
					tag = kxmlParser.next();
				}

			}
		} catch (XmlPullParserException parser_ex) {
			parser_ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
		} finally {

		}
	}

	private void parseInstruction(KXmlParser kxmlParser) {
		String desc = "";
		double distance = 0;

		try {

			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null,
						"xls:RouteInstruction");
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo("xls:Instruction") == 0) {
							desc = kxmlParser.nextText();
						} else if (kxmlParser.getName().compareTo(
								"xls:distance") == 0) {
							distance = new Double(kxmlParser
									.getAttributeValue(0)).doubleValue();
						} else if (kxmlParser.getName().compareTo(
								"xls:RouteInstructionGeometry") == 0) {
							LineString l = parseLineString(kxmlParser);

							ORSInstruction ins = new ORSInstruction(l
									.getXCoords(), l.getYCoords());
							ins.setDescription(desc);
							ins.setDistance(distance);
							route.addInstruction(ins);
						}
						break;

					case KXmlParser.END_TAG:
						if (kxmlParser.getName().compareTo(
								"xls:RouteInstructions") == 0) {
							return;
						}
						break;
					}
					tag = kxmlParser.next();
				}
			}
		} catch (XmlPullParserException parser_ex) {
			parser_ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
		} finally {

		}

	}

	private LineString parseLineString(KXmlParser kxmlParser) {

		LineString l = null;

		try {
			Vector xCoords = new Vector();
			Vector yCoords = new Vector();
			int tag;
			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null,
						"xls:RouteInstructionGeometry");
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {
					case KXmlParser.START_TAG:
						if (kxmlParser.getName().compareTo("gml:pos") == 0) {
							String[] pos = this.parsePos(kxmlParser);
							xCoords.add(new Double(pos[0]).doubleValue());
							yCoords.add(new Double(pos[1]).doubleValue());
						}
						break;

					case KXmlParser.END_TAG:
						if (kxmlParser.getName().compareTo(
								"xls:RouteInstructionGeometry") == 0) {
							final int xSize = xCoords.size();
							double[] x = new double[xSize];
							double[] y = new double[xSize];

							for (int i = 0; i < xSize; i++) {
								x[i] = ((Double) xCoords.elementAt(i))
										.doubleValue();
								y[i] = ((Double) yCoords.elementAt(i))
										.doubleValue();
							}

							l = new LineString(x, y);
							return l;
						}
						break;
					}
					tag = kxmlParser.next();
				}
			}
		} catch (XmlPullParserException parser_ex) {
			parser_ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (OutOfMemoryError ou) {
			System.gc();
			System.gc();
		} finally {
			return l;
		}
	}
}
