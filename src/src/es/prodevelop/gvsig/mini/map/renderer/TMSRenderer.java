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

package es.prodevelop.gvsig.mini.map.renderer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import org.apache.http.util.ByteArrayBuffer;
import org.gvsig.remoteclient.utils.Utilities;
import org.gvsig.remoteclient.wms.WMSStatus;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.CapabilitiesTags;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.phonecache.CRSFactory;
import es.prodevelop.gvsig.mini.phonecache.ConversionCoords;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.views.overlay.TileRaster;

/**
 * MapRednerer for TileMapServices
 * @author aromeu 
 * @author rblanco
 *
 */
public class TMSRenderer extends MapRenderer {

	private final static Logger logger = LoggerFactory
			.getLogger(TMSRenderer.class);

	public Extent BBOX = new Extent(258000.000000, 4485000.000000,
			536000.000000, 4752000.000000);
	public double originX = 258000.000000;
	public double originY = 4485000.000000;
	Vector tempRes = new Vector();
	static String encoding = "UTF-8";

	TMSRenderer(final String aBaseUrl, final String aName,
			final String aImageFilenameEnding, final int aZoomMax,
			final int aTileSizePX, final int type) {
		super(aBaseUrl, aName, aImageFilenameEnding, aZoomMax, aTileSizePX,
				type);
	}

	/**
	 * Connects to the URL to retrieve the info of the layer in the TileMapService and 
	 * returns an instance of a TMSRenderer ready to consume tiles
	 * @param url The URL of a layer of a Tile Map Service
	 * @param layerName The name of the layer (visible for users)
	 * @return A TMSRenderer
	 */
	public static TMSRenderer getTMSRenderer(final String url, String layerName) {
		TMSRenderer t = TMSRenderer.getTMSRenderer(url, layerName, null, 0, 0,
				3);
		t.tryParseTileSet(url);
		return t;
	}

	/**
	 * Loads the properties of a TMSRenderer formatted into a String[] @see Layers and layers.txt
	 * @param props The properties of the TMSRenderer
	 * @param layerName The name of the layer
	 * @return A TMSRenderer
	 */
	public static TMSRenderer loadProperties(final String[] props,
			String layerName) {
		try {
			int type = Integer.valueOf(props[0]).intValue();
			String url = props[1];
			String format = props[2];
			int zoomLevel = Integer.valueOf(props[3]).intValue();
			int tileSize = Integer.valueOf(props[4]).intValue();
			double minX = Double.valueOf(props[5]).doubleValue();
			double minY = Double.valueOf(props[6]).doubleValue();
			double maxX = Double.valueOf(props[7]).doubleValue();
			double maxY = Double.valueOf(props[8]).doubleValue();
			double originX = Double.valueOf(props[9]).doubleValue();
			double originY = Double.valueOf(props[10]).doubleValue();
			String SRS = props[11];

			String[] resol = props[12].split(":");
			final int length = resol.length;
			double[] resolutions = new double[length];
			for (int i = 0; i < length; i++) {
				resolutions[i] = Double.valueOf(resol[i]).doubleValue();
			}

			TMSRenderer renderer = new TMSRenderer(url, layerName, format,
					zoomLevel, tileSize, type);

			renderer.BBOX = new Extent(minX, minY, maxX, maxY);			
			renderer.originX = originX;
			renderer.originY = originY;
			renderer.SRS = SRS;
			renderer.resolutions = resolutions;
			return renderer;
		} catch (Exception e) {
			logger.error("getTMSRenderer: ", e);
			return null;
		}
	}

	/**
	 * Instantiates a TMSRenderer
	 * @param props if props contains the  name of the layer and the URL, connects
	 * to the Tile Map Service to retrieve the TMSRenderer properties. If props contains
	 * all the properties simply instantiates the TMSRenderer
	 * @param layerName The name of the layer
	 * @return A TMSRenderer
	 */
	public static TMSRenderer getTMSRenderer(final String[] props,
			String layerName) {

		try {
			if (props.length == 2) {
				return TMSRenderer.getTMSRenderer(props[1], layerName);
			}
			return TMSRenderer.loadProperties(props, layerName);
		} catch (Exception e) {
			logger.error("getTMSRenderer: ", e);
			return null;
		}
	}

	public static TMSRenderer getTMSRenderer(final String aBaseUrl,
			final String aName, final String aImageFilenameEnding,
			final int aZoomMax, final int aTileSizePX, final int type) {

		TMSRenderer r = new TMSRenderer(aBaseUrl, aName, aImageFilenameEnding,
				aZoomMax, aTileSizePX, type);
		return r;
	}

	public static TMSRenderer getICCOrtoRenderer() {
		return getTMSRenderer(
				"http://sagitari2.icc.cat/tilecache/tilecache.py/1.0.0/orto/",
				"ICC - ORTO", "jpg", 10, 256, 3);
	}

	public static TMSRenderer getICCGeolRenderer() {
		return getTMSRenderer(
				"http://sagitari2.icc.cat/tilecache/tilecache.py/1.0.0/geol/",
				"ICC - GEOL", "jpg", 10, 256, 3);
	}

	public static TMSRenderer getICCTopoRenderer() {
		return getTMSRenderer(
				"http://sagitari2.icc.cat/tilecache/tilecache.py/1.0.0/topo/",
				"ICC - TOPO", "jpg", 10, 256, 3);
	}

	@Override
	public String getTileURLString(final int[] tileID, final int zoomLevel) {
		String s = new StringBuilder().append(this.getBASEURL()).append(
				zoomLevel).append("/").append(tileID[1]).append("/").append(
				tileID[0]).append(".").append(this.getIMAGE_FILENAMEENDING())
				.toString();
//		logger.debug(s);
		return s;
	}
	
	@Override
	public Extent getExtent() {
		return this.BBOX;
	}

	@Override
	public double getOriginX() {
		return this.originX;
	}

	@Override
	public double getOriginY() {
		return this.originY;
	}

	public double[] getResolutions() {
		return this.resolutions;
	}

	/**
	 * parses TileSet tag
	 */
	public final void tryParseTileSet(String url) {
		URL parseURL = null;
		URLConnection urlconnec = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		ByteArrayBuffer baf = null;

		try {
			/* Define the URL we want to load data from. */
			parseURL = new URL(url);
			/* Open a connection to that URL. */
			urlconnec = parseURL.openConnection();
			/* Define InputStreams to read from the URLConnection. */
			is = urlconnec.getInputStream();
			bis = new BufferedInputStream(is);
			/* Read bytes to the Buffer until there is nothing more to read(-1). */
			baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}

			int tag;
			final KXmlParser kxmlParser = createParser(baf.toByteArray());
			kxmlParser.nextTag();

			if (kxmlParser.getEventType() != KXmlParser.END_DOCUMENT) {
				kxmlParser.require(KXmlParser.START_TAG, null,
						CapabilitiesTags.TILE_MAP);
				tag = kxmlParser.nextTag();
				while (tag != KXmlParser.END_DOCUMENT) {
					switch (tag) {

					case KXmlParser.START_TAG:
						if (kxmlParser.getName()
								.compareTo(CapabilitiesTags.SRS) == 0) {
							kxmlParser.next();
							SRS = kxmlParser.getText();
						} else if (kxmlParser.getName().compareTo(
								CapabilitiesTags.BOUNDINGBOX) == 0) {
							double minx = 0;
							double maxx = 0;
							double miny = 0;
							double maxy = 0;
							String value = "";

							try {

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.MINX);
								if (value != null) {
									minx = Double.parseDouble(value);
								}

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.MAXX);
								if (value != null) {
									maxx = Double.parseDouble(value);
								}

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.MINY);
								if (value != null) {
									miny = Double.parseDouble(value);
								}

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.MAXY);
								if (value != null) {
									maxy = Double.parseDouble(value);
								}

								BBOX = new Extent(minx, miny, maxx, maxy);
								// if (ext.area() > 0)
								// this.BBOX = ext;
								// else
								// SRS = "";

							} catch (Exception ex) {
								logger.error(ex);
							}

						} else if (kxmlParser.getName().compareTo(
								CapabilitiesTags.ORIGIN) == 0) {
							double x = 0;
							double y = 0;
							String value = "";

							try {

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.X);
								if (value != null) {
									originX = Double.parseDouble(value);
								}

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.Y);
								if (value != null) {
									originY = Double.parseDouble(value);
								}
							} catch (Exception ex) {
								logger.error(ex);
							}

						} else if (kxmlParser.getName().compareTo(
								CapabilitiesTags.TILE_FORMAT) == 0) {
							String value = "";

							try {
								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.WIDTH);
								if (value != null) {
									this.setMAPTILE_SIZEPX(Integer
											.parseInt(value));
								}

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.HEIGHT);
								if (value != null) {
									this.setMAPTILE_SIZEPX(Integer
											.parseInt(value));
								}

								value = kxmlParser.getAttributeValue("",
										CapabilitiesTags.EXTENSION);
								if (value != null) {
									this.setIMAGE_FILENAMEENDING(value);
								}

							} catch (Exception ex) {
								logger.error(ex);
							}

						} else if (kxmlParser.getName().compareTo(
								CapabilitiesTags.TILE_SETS) == 0) {
							this.parseTileSets(kxmlParser);
						}

						break;
					case KXmlParser.END_TAG:
						break;
					case KXmlParser.TEXT:
						break;
					}

					tag = kxmlParser.next();
				}

				kxmlParser.require(KXmlParser.END_DOCUMENT, null, null);
			}

		} catch (XmlPullParserException parser_ex) {
			logger.error(parser_ex);
		} catch (IOException ioe) {
			logger.error(ioe);
		} finally {
			if (tempRes != null) {
				final int length = tempRes.size();
				resolutions = new double[length];
				for (int i = 0; i < length; i++) {
					resolutions[i] = Double.valueOf(
							tempRes.elementAt(i).toString()).doubleValue();
				}
				this.setZOOM_MAXLEVEL(length-1);
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}

			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (baf != null) {
				baf.clear();
			}
		}
	}

	/**
	 * parse TileSet tag
	 * 
	 * @param parser
	 * @throws java.io.IOException
	 * @throws org.xmlpull.v1.XmlPullParserException
	 */
	public final void parseTileSets(final KXmlParser parser)
			throws IOException, XmlPullParserException {
		int currentTag;
		boolean end = false;

		parser.require(KXmlParser.START_TAG, null, CapabilitiesTags.TILE_SETS);
		currentTag = parser.next();

		// tileSets = new Hashtable();
		while (!end) {
			switch (currentTag) {
			case KXmlParser.START_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.TILE_SET) == 0) {
					String url = "";
					String res = "";
					try {
						url = parser.getAttributeValue("",
								CapabilitiesTags.HREF);
						res = parser.getAttributeValue("",
								CapabilitiesTags.UNITS_PER_PIXEL);
						tempRes.add(res);
					} catch (final Exception e) {
						logger.error(e);
					}
				}
			case KXmlParser.END_TAG:
				if (parser.getName().compareTo(CapabilitiesTags.TILE_SETS) == 0) {
					end = true;
				}

				break;
			case KXmlParser.TEXT:
				break;
			}

			if (!end) {
				currentTag = parser.next();
			}
		}
		// parser.require(KXmlParser.END_TAG, null,
		// CapabilitiesTags.CAPABILITY);
	}

	public static final KXmlParser createParser(final byte[] f) {
		KXmlParser parser = null;
		ByteArrayInputStream br;
		byte[] buffer;
		try {
			br = new ByteArrayInputStream(f);
			buffer = new byte[100];
			br.read(buffer);
			String string = new String(buffer);

			final int a = string.toLowerCase().indexOf("<?xml");
			if (a != -1) {
				string = string.substring(a, string.length());
			}
			final StringBuffer st = new StringBuffer(string);
			final String searchText = "encoding=\"";
			final int index = st.toString().indexOf(searchText);
			if (index > -1) {
				st.delete(0, index + searchText.length());
				encoding = st.toString().substring(0,
						st.toString().indexOf("\""));
			}
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}
		parser = new KXmlParser();
		try {
			br = new ByteArrayInputStream(f);
			buffer = new byte[100];
			br.read(buffer);
			String string = new String(buffer);
			int a = string.toLowerCase().indexOf("<?xml");
			if (a != -1) {
				string = string.substring(a, string.length());
				parser.setInput(br, encoding);
			} else // end patch
			{
				parser.setInput(br, encoding);
			}
			br.reset();
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			buffer = null;
			try {
				if (br != null) {
					br.close();
				}
			} catch (final Exception e) {
			}
		}
		return parser;
	}

	@Override
	public int getType() {
		return MapRenderer.TMS_RENDERER;
	}

	@Override
	public int isTMS() {
		return -1;
	}

	@Override
	public String toString() {
		StringBuffer layer = new StringBuffer();
		try {
			final int length = resolutions.length;
			StringBuffer res = new StringBuffer();
			for (int i = 0; i < length; i++) {
				res.append(resolutions[i]);
				if (i != length - 1) {
					res.append(":");
				}
			}

			layer.append(this.getNAME()).append(";").append(this.getType())
					.append(",").append(this.getBASEURL()).append(",").append(
							this.getIMAGE_FILENAMEENDING()).append(",").append(
							this.getZOOM_MAXLEVEL()).append(",").append(
							this.getMAPTILE_SIZEPX()).append(",").append(
							BBOX.toString()).append(",").append(originX)
					.append(",").append(originY).append(",").append(SRS)
					.append(",").append(res.toString());
			return layer.toString();

		} catch (Exception e) {
			logger.error("toString: ", e);
			return null;
		}
	}

	@Override
	public double[] transformCenter(String toCRS) {
		try {
			return ConversionCoords.reproject(center.getX(), center.getY(),
					CRSFactory.getCRS(this.getSRS()), CRSFactory.getCRS(toCRS));
		} catch (Exception e) {
			logger.error("transformCoords: ", e);
			return null;
		}
	}

	@Override
	public void setCenter(double x, double y) {
		this.center = new Point(x, y);

	}

	@Override
	public Point getCenter() {
		return new Point(this.center.getX(), this.center.getY());
	}

	public Pixel getUpperLeftCornerOfCenterMapTileInScreen(			
			final Pixel reuse) {
		final Pixel out = (reuse != null) ? reuse
				: new Pixel(0,0);
//		Pixel tile = TileConversor.latLonToTileG(this.getCenter().getX(), this
//				.getCenter().getY(), Map.vp.getDist1Pixel());		
		Pixel tile = TileConversor.metersToTile(this.getCenter().getX(), this
				.getCenter().getY(), Map.vp.getDist1Pixel(),
				-Map.vp.origin.getX(), -Map.vp.origin.getY());	
//		Extent e1 = TileConversor.tileBoundsG(tile.getX(), tile.getY(),
//				Map.vp.getDist1Pixel());		
		Extent e1 = TileConversor.tileMeterBounds(tile.getX(), tile.getY(),
				Map.vp.getDist1Pixel());
		Extent e2 = Map.vp.calculateExtent(TileRaster.mapWidth,
				TileRaster.mapHeight, new es.prodevelop.gvsig.mini.geom.Point(
						this.getCenter().getX(), this.getCenter().getY()));
		int[] centerTopLeft = Map.vp.fromMapPoint(new double[] { e1.getMinX(),
				e1.getMaxY() }, e2.getMinX(), e2.getMaxY());
		out.setX(centerTopLeft[0]);
		out.setY(centerTopLeft[1]);
		return out;
	}
	
	public int[] getMapTileFromCenter() {
//		Pixel tile = TileConversor.latLonToTileG(this.getCenter().getX(), this
//				.getCenter().getY(), Map.vp.getDist1Pixel());
		Pixel tile = TileConversor.metersToTile(this.getCenter().getX(), this
				.getCenter().getY(), Map.vp.getDist1Pixel(),
				-Map.vp.origin.getX(), -Map.vp.origin.getY());
		return new int[]{tile.getY(), tile.getX()};
	}

	@Override
	public double[] fromPixels(int[] pxy) {
		pxy[0] -= TileRaster.mTouchMapOffsetX;
		pxy[1] -= TileRaster.mTouchMapOffsetY;
		final double res = this.resolutions[this.getZoomLevel()];
		return ViewPort.toMapPoint(pxy, TileRaster.mapWidth, TileRaster.mapHeight, this.getCenter(), res);
	}

	@Override
	public Pixel getCenterE6() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] toPixels(double[] pxy) {
		final double res = this.resolutions[this.getZoomLevel()];
		Extent e = ViewPort.calculateExtent(this.getCenter(), res, TileRaster.mapWidth, TileRaster.mapHeight);
		int[] result = ViewPort.fromMapPoint(pxy, e.getMinX(), e.getMaxY(), res);
		result[0] += TileRaster.mTouchMapOffsetX;
		result[1] += TileRaster.mTouchMapOffsetY;
		return result;
	}

	@Override
	public void centerOnBBox() {
		this.center = this.getExtent().getCenter();
		this.setZoomLevel(0);		
	}
	
}
