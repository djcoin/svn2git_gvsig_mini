/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   http://www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integraci�n de Tecnolog�as SL
 *   Conde Salvatierra de �lava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 *
 *    or
 *
 *   Instituto de Rob�tica
 *   Apartado de correos 2085
 *   46071 Valencia
 *   (Spain)
 *   
 *   +34 963 543 577
 *   jjordan@robotica.uv.es
 *   http://robotica.uv.es
 *   
 */
package es.prodevelop.gvsig.mini.map;

/**
 * Utility class to keep WMS server string constants, used in WMS requests
 * and rsponses.
 *  
 * @see es.prodevelop.gvsig.mobile.fmap.driver.raster.wms.WMSProtocolHandler
 * @see es.prodevelop.gvsig.mobile.fmap.driver.raster.wms.WmsRasterDriver
 *  
 * @author jldominguez
 * @author aromeu 
 * @author rblanco - modified for gvSIG phone
 *
 */
public class CapabilitiesTags {
    
    public final static String CAPABILITIES_ROOT1_1_0 = "WMT_MS_Capabilities";
    public final static String CAPABILITIES_ROOT1_1_1 = "WMT_MS_Capabilities";
    public final static String CAPABILITIES_ROOT1_3_0 = "WMS_Capabilities";
    public final static String CAPABILITY = "Capability";
    public final static String SERVICE = "Service";
    public final static String NAME = "Name";
    public final static String TITLE = "Title";
    public final static String ABSTRACT = "Abstract";
    public final static String KEYWORDLIST = "KeywordList";
    public final static String KEYWORD = "Keyword";
    public final static String ONLINERESOURCE = "OnlineResource";
    public final static String CONTACTINFORMATION = "ContactInformation";
    public final static String CONTACTPOSITION = "ContactPosition";
    public final static String CONTACTADRESS = "ContactAddress";
    public final static String CONTACTVOICETELEPHONE = "ContactVoiceTelephone";
    public final static String CONTACTFACSIMILETELEPHONE = "ContactFacsimileTelephone";
    public final static String CONTACTPERSONPRIMARY = "ContactPersonPrimary";
    public final static String CONTACTPERSON = "ContactPerson";
    public final static String CONTACTORGANIZATION = "ContactOrganization";
    public final static String CONTACTEMAILADRESS = "ContactElectronicMailAddress";
    public final static String FEES = "Fees";
    public final static String ACCESSCONSTRAINTS = "AccessConstraints";
    public final static String REQUEST = "Request";
    public final static String GETCAPABILITIES = "GetCapabilities";
    public final static String FORMAT = "Format";
    public final static String TILEFORMAT = "TileFormat";
    public final static String DCPTYPE = "DCPType";
    public final static String XMLNS_XLINK = "xmlns:xlink";
    public final static String XLINK_TYPE = "xlink:type";
    public final static String XLINK_HREF = "xlink:href";
    public final static String HTTP = "HTTP";
    public final static String GET = "Get";
    public final static String POST = "Post";
    public final static String GETMAP = "GetMap";
    public final static String GETFEATUREINFO = "GetFeatureInfo";
    public final static String DESCRIBELAYER = "DescribeLayer";
    public final static String GETLEGENDGRAPHIC = "GetLegendGraphic";
    public final static String EXCEPTION = "Exception";
    public final static String VENDORSPECIFICCAPABILITIES = "VendorSpecificCapabilities";
    public final static String USERDEFINEDSYMBOLIZATION = "UserDefinedSymbolization";
    public final static String LAYER = "Layer";
//		<!ELEMENT Layer ( Name?, Title, Abstract?, KeywordList?, SRS*,
//				LatLonBoundingBox?, BoundingBox*, Dimension*, Extent*,
//				Attribution?, AuthorityURL*, Identifier*, MetadataURL*, DataURL*,
//				FeatureListURL*, Style*, ScaleHint?, Layer* ) >
    public final static String SRS = "SRS";
    public final static String CRS = "CRS";
    public final static String BOUNDINGBOX = "BoundingBox";
    // Used in the WMS as "LatLonBoundingBox" don't change it and create your own one
    public final static String LATLONBOUNDINGBOX = "LatLonBoundingBox";
    public final static String EX_GEOGRAPHICBOUNDINGBOX = "EX_GeographicBoundingBox";
    public final static String METADATAURL = "MetadataURL";
    public final static String LOGOURL = "LogoURL";
    public final static String AUTHORITYURL = "AuthorityURL";
    public final static String STYLE = "Style";
    public final static String LEGENDURL = "LegendURL";
    public final static String SCALEHINT = "ScaleHint";
    public final static String MINSCALEDENOMINATOR = "MinScaleDenominator";
    public final static String DIMENSION = "Dimension";
    public final static String TIME = "Time";

    // capabilities attributes
    public final static String VERSION = "version";
    public final static String UPDATESEQUENCE = "updatesequence";
    public final static String ENCODING = "encoding";
    public final static String STANDALONE = "standalone";
    public final static String SUPPORTSLD = "SupportSLD";
    public final static String USERLAYER = "UserLayer";
    public final static String USERSTYLE = "UserStyle";
    public final static String QUERYABLE = "queryable";
    public final static String CASCADED = "cascaded";
    public final static String NOSUBSETS = "noSubsets";
    public final static String OPAQUE = "opaque";
    public final static String FIXEDWIDTH = "fixedWidth";
    public final static String FIXEDHEIGHT = "fixedHeight";
    public static final String ATTRIBUTION = "Attribution";
    //LegendURL attributes
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public final static String MINX = "minx";
    public final static String MINY = "miny";
    public final static String MAXX = "maxx";
    public final static String MAXY = "maxy";
    public final static String RESX = "resx";
    public final static String RESY = "rexy";
    public final static String WESTBOUNDLONGITUDE = "westBoundLongitude";
    public final static String EASTBOUNDLONGITUDE = "eastBoundLongitude";
    public final static String SOUTHBOUNDLATITUDE = "southBoundLatitude";
    public final static String NORTHBOUNDLATITUDE = "northBoundLatitude";
    public final static String TYPE = "type";
    public final static String MIN = "min";
    public final static String MAX = "max";
    public final static String DIMENSION_NAME = "name";
    public final static String DIMENSION_UNITS = "units";
    public final static String DIMENSION_UNIT_SYMBOL = "unitSymbol";

    // WMS Extent specifics
    public static final String EXTENT = "Extent";
    public static final String EXTENT_MULTIPLE_VALUES = "multipleValues";
    public static final String EXTENT_NEAREST_VALUE = "nearestValue";
    public static final String EXTENT_CURRENT = "current";
    // WCS specific
    public static final String WCS_CAPABILITIES_ROOT1_0_0 = "WCS_Capabilities";
    public static final String WCS_CONTENTMETADATA = "ContentMetadata";
    public static final String WCS_LABEL = "label";
    public static final String WCS_KEYWORDS = "keywords";
    public static final String WCS_DESCRIPTION = "description";
    public static final String DESCRIBECOVERAGE = "DescribeCoverage";
    public static final String GETCOVERAGE = "GetCoverage";
    public static final String WCS_COVERAGEOFFERING = "CoverageOffering";
    public static final String WCS_COVERAGEOFFERINGBRIEF = "CoverageOfferingBrief";

    // Miscelaneous
    public final static String DEFAULT = "default";
    public final static String EPSG_4326 = "EPSG:4326";
    public final static String CRS_84 = "CRS:84";

    //WFS specific
    public static final String WFS_CAPABILITIES_ROOT1_0_0 = "WFS_Capabilities";
    public static final String WFS_TITLE = "Title";
    public static final String WFS_ABSTRACT = "Abstract";
    public final static String WFS_ONLINERESOURCE = "OnlineResource";
    public final static String WFS_FEATURETYPELIST = "FeatureTypeList";
    public final static String WFS_FEATURETYPE = "FeatureType";
    public final static String WFS_SCHEMAROOT = "schema";
    public final static String WFS_DESCRIBEFEATURETYPE = "DescribeFeatureType";
    public final static String WFS_GETFEATURE = "GetFeature";
    public final static String WFS_KEYWORDS = "Keywords";
    public final static String WFS_FEATUERE_COLLECTION = "FeatureCollection";
    public final static String LATLONGBOUNDINGBOX = "LatLongBoundingBox";
    public final static String COMPLEXTYPE = "complexType";
    public final static String COMPLEXCONTENT = "complexContent";
    public final static String EXTENSION = "extension";
    public final static String SEQUENCE = "sequence";
    public final static String ELEMENT = "element";
    public final static String ELEMENT_NAME = "name";
    public final static String ELEMENT_TYPE = "type";
    public final static String ELEMENT_MINOCCURS = "minOccurs";
    public final static String ELEMENT_MAXOCCURS = "maxOccurs";
    public final static String ELEMENT_REF = "ref";
    public final static String SIMPLETYPE = "simpleType";
    public final static String RESTRICTION = "restriction";
    public final static String TOTAL_DIGITS = "totalDigits";
    public final static String FRACTION_DIGITS = "fractionDigits";
    public final static String VALUE = "value";
    public final static String BASE = "base";
    public final static String CHOICE = "choice";
    
    /*Tile Map Service*/
    
    public final static String SERVICES = "Services";
    public final static String TILE_MAP_SERVICE = "TileMapService";
    public final static String HREF = "href";
    public final static String TILE_MAPS = "TileMaps";
    public final static String TILE_MAP = "TileMap";
    public final static String PROFILE = "profile";    
    public final static String ORIGIN = "Origin";
    public final static String X = "x";
    public final static String Y = "y";
    public final static String TILE_FORMAT = "TileFormat";
    public final static String MIME_TYPE = "mime-type";
    public final static String TILE_SETS = "TileSets";
    public final static String TILE_SET = "TileSet";
    public final static String ORDER = "order";
    public final static String UNITS_PER_PIXEL = "units-per-pixel";
    public final static String RESOLUTIONS = "Resolutions";
    public final static String LAYERS = "Layers";
    public final static String STYLES = "Styles";
}
