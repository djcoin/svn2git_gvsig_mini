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


/**
 * Constants class
 * 
 * @author Alberto Romeu Carrasco (aromeu@prodevelop.es)
 */
public final class Tags {

    public static final int DEFAULT_STEP = 20;
    public static final int MAX_STEP = 100;
    public static final int DEFAULT_TILE_SIZE = 256;
    
    public static int DEFAULT_NUMBER_THREADS = 2;

    public static final char FILE_SEPARATOR = '|';
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String DEFAULT_SRS = "EPSG:4326";

    public static final int TOP = 0;
    public static final int BOTTOM = 2;
    public static final int LEFT = 1;
    public static final int RIGHT = 3;

    public static final int SCROLL_TOP = 1;
    public static final int SCROLL_BOTTOM = -1;
    public static final int SCROLL_RIGHT = 1;
    public static final int SCROLL_LEFT = -1;
    
    public static final int START_POINT = 0;
    public static final int END_POINT = 1;
    public static final int PASS_POINT = 2;
    
    public final static String EXCEPTION_ROOT="ServiceExceptionReport";
    public final static String SERVICE_EXCEPTION="ServiceException";
    public final static String CODE="code";
    
    public static final long MAX_TIME_ALLOWED = 15000;
    public static final long MAX_TIME_ALLOWED_STEP = 500;
    
    public static final int ROUTE_EMPTY = 0;
    public static final int ROUTE_WITH_START_POINT = 1;
    public static final int ROUTE_WITH_END_POINT = 2;
    public static final int ROUTE_WITH_2_POINT = 3;
    public static final int ROUTE_WITH_START_AND_PASS_POINT = 4;
    public static final int ROUTE_WITH_N_POINT = 5;
    public static final int ROUTE_WITH_PASS_POINT = 6;    
    
    public static final String RS_CONFIG_NAME = "GVSIG";
    public static final String BASE_DIR = "gvSIG/";
    public static final String MAPS_CACHE_DIR = "maps/";
    
    public static final int HASH_SIZE = 97;
    public static final int POIS_ZOOM_LEVEL = 7;
    
    public static final int GOOGLE_MAX_ZOOM_LEVEL = 22;
    public static final int OSM_MAX_ZOOM_LEVEL = 18;
    public static final int YAHOO_MIN_ZOOM_LEVEL = 17;
    
    /**
     * Resolutions (meters/pixel) for each zoom level in the Mercator Projection. These values
     * are precalculated using TileConversor.resolutionMercator(int zoomLevel) method.
     */
//    public static final double[] RESOLUTIONS = new double[]{0.703125,
//                    0.3515625, 0.17578125, 0.087890625,
//                    0.0439453125};
    
    public static final double[] RESOLUTIONS = new double[]{156543.03392804096153584694438047,
                    78271.516964020480767923472190235, 39135.758482010240383961736095118, 19567.879241005120191980868047559,
                    9783.9396205025600959904340237794, 4891.9698102512800479952170118897, 2445.9849051256400239976085059448,
                    1222.9924525628200119988042529724, 611.49622628141000599940212648621, 305.74811314070500299970106324311,
                    152.87405657035250149985053162155, 76.437028285176250749925265810776, 38.218514142588125374962632905388,
                    19.109257071294062687481316452694, 9.5546285356470313437406582263471, 4.7773142678235156718703291131735,
                    2.3886571339117578359351645565868, 1.1943285669558789179675822782934, 0.59716428347793945898379113914669,
                    /*0.29858214173896972949189556957335, 0.14929107086948486474594778478667, 0.074645535434742432372973892393336,
                    0.037322767717371216186486946196668, 0.018661383858685608093243473098334, 0.009330691929342804046621736549167*/};
    
    public static final int POI_SEARCH_DISTANCE = 5000;    
    public static final int POI_INFORMATION_DISTANCE = 20;
    public static final int POI_MAX_NUM_SEARCH = 50;
    
    public static String ROUTE_URL = "";
    public static String TILE_URL = "";
    public static String LAYER = "";
    
    public static final String URL_CARTOCIUDAD_WMS_CACHE = "http://www.cartociudad.es/wms-c/CARTOCIUDAD/CARTOCIUDAD";
    public static final String URL_METACARTA_WMS_CACHE = "http://labs.metacarta.com/wms-c/Basic.py";
    public static final String URL_CARTOCIUDAD_WMS = "http://www.cartociudad.es/wms/CARTOCIUDAD/CARTOCIUDAD?";
    public static final String URL_CATASTRO_WMS = "http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx?";    
    public static final String URL_PNOA = "http://www.idee.es/wms-c/PNOA/PNOA/";
    public static final String URL_SIGATEX = "http://194.179.111.10:8090/tilecache/tilecache.py/";
    
    /**
     * Yahoo minZoomLevel == 17 (GoogleMinZoomLevel == 0)
     * first YAHOO_y = (2*map.zoomLevel) +1
     *      Yahoo_zoom = 17 => 0
     *      Yahoo_zoom = 16 => 1
     *      Yahoo_zoom = 15 => 3
     *      Yahoo_zoom = 14 => 7
     *      Yahoo_zoom = 13 => 15
     *          :     
     * para zoomLevel > 0 : YAHOO_Y = 2*(map.zoomLevel-1) +1
     * sino : YAHOO_Y = 0
     */
    public static final String URL_YAHOO_MAP = "http://png.maps.yimg.com/png?t=m&v=4.1&s=256&f=j&x=XXXX&y={YAHOO_Y}&z={YAHOO_ZOOM}";
    public static final String URL_YAHOO_SAT = "http://aerial.maps.yimg.com/ximg?t=a&v=1.8&s=256&x=XXXX&y={YAHOO_Y}&z={YAHOO_ZOOM}";    
    public static final String URL_YAHOO_HYB = "http://aerial.maps.yimg.com/tile?t=p&v=2.5&x=XXXX&y={YAHOO_Y}&z={YAHOO_ZOOM}";
    public static final String URL_MICROSOFT_HYB = "http://h{MS_DIGIT}.ortho.tiles.virtualearth.net/tiles/h{MS_QUADCODE}.jpeg?g=174";
    public static final String URL_MICROSOFT_SAT = "http://a{MS_DIGIT}.ortho.tiles.virtualearth.net/tiles/a{MS_QUADCODE}.jpeg?g=174";
    public static final String URL_MICROSOFT_MAP = "http://r{MS_DIGIT}.ortho.tiles.virtualearth.net/tiles/r{MS_QUADCODE}.png?g=174";
    public static final String URL_MICROSOFT_TER = "http://r{MS_DIGIT}.ortho.tiles.virtualearth.net/tiles/r{MS_QUADCODE}.png?g=174&shading=hill";
    public static final String URL_OSM_MAPNIK = "http://tile.openstreetmap.org/{OSM_ZOOM}/XXXX/YYYY.png";
    public static final String URL_OSMARENDER = "http://tah.openstreetmap.org/Tiles/tile/{OSM_ZOOM}/XXXX/YYYY.png";    
    public static final String URL_OSM_CLOUDMADE_MOBILE = "http://tile.cloudmade.com/8bafab36916b5ce6b4395ede3cb9ddea/2/256/";
    public static final String URL_OSM_CYCLE_MAP = "http://andy.sandbox.cloudmade.com/tiles/cycle/";
    public static final String URL_OSM_MAPLINT = "http://tah.openstreetmap.org/Tiles/maplint/";
    public static final String URL_OPENSTREETMAP_WMS_CACHE_A = "http://a.tile.openstreetmap.org/";
    
    //standard OSM tile server
    public static final int TYPE_TILE_SERVER = 0;
    //yahoo tile server
    public static final int TYPE_YAHOO_TILE_SERVER = 1;
    //microsoft tile server
    public static final int TYPE_MICROSOFT_TILE_SERVER = 2;
    public static final int TYPE_WMS_CACHE_CAPABILITIES_SERVER = 3;
    public static final int TYPE_WMS_CACHE_TMS_SERVER = 4;
    public static final int TYPE_WMS_SERVER = 5;
    
    public static final int STATE_CREATE = 0;
    public static final int STATE_UPDATE = 1;
    public static final int STATE_CHANGE_NAME = 2;
    
    public static final int RMS_CREATE = 0;
    public static final int RMS_UPDATE = 1;
    public static final int RMS_NOTHING = 2;
    
    public static final int DEFAULT_ZOOM_LEVEL = 1;
    
    public static final int ROUTE_TASK_ID = -1;
    public static final int CAPABILITIES_TASK_ID = -2;
    
    public static final String NO_WORD = "null";
    
    public static final String EPSG_3785 = "EPSG:3785";
}
