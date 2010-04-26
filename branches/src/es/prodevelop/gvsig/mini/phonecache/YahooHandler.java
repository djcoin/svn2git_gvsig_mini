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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.phonecache;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.projection.TileConversor;

/**
 *
 * @author aromeu 
 * @author rblanco
 */
public class YahooHandler extends WMSHandler {

    @Override
    public Extent getExtentIncludingCenter(Point center, int zoomLevel, Point origin) {
    	Extent ex = null;
        Pixel tileNumber = null;
        String profile = null;

        try {
//            double minX = 107897.0;
//            double minY = 4201010.0;
//            double minX = origin.getX();
//            double minY = origin.getY();
            Point p = null;
//            switch (type) {
//                case Tags.TYPE_TILE_SERVER:
//                    p = TileConversor.mercatorToLatLon(center.getX(), center.getY());
                    //p = center;
//                    tileNumber = TileConversor.getTileNumber(p.getX(), p.getY(), zoomLevel);
//                    ex = TileConversor.tileOSMMercatorBounds(tileNumber.getX(), tileNumber.getY(), zoomLevel);
                    //ex = TileConversor.tileOSMGeodeticBounds(tileNumber.getX(), tileNumber.getY(), map.Map.zoomLevel);
//                    break;
//                case Tags.TYPE_MICROSOFT_TILE_SERVER:
//                    p = TileConversor.mercatorToLatLon(center.getX(), center.getY());
//                    //p = center;
//                    tileNumber = TileConversor.getTileNumber(p.getX(), p.getY(), es.prodevelop.gvsig.phone.map.Map.zoomLevel);
//                    ex = TileConversor.tileOSMMercatorBounds(tileNumber.getX(), tileNumber.getY(), es.prodevelop.gvsig.phone.map.Map.zoomLevel);
//                    //ex = TileConversor.tileOSMGeodeticBounds(tileNumber.getX(), tileNumber.getY(), map.Map.zoomLevel);
//                    break;
//                case Tags.TYPE_YAHOO_TILE_SERVER:
                    p = TileConversor.mercatorToLatLon(center.getX(), center.getY());
////                    p = center;
                    tileNumber = TileConversor.getTileNumber(p.getX(), p.getY(), zoomLevel);
                    ex = TileConversor.tileOSMMercatorBounds(tileNumber.getX(), tileNumber.getY(), zoomLevel);
////                    ex = TileConversor.tileOSMGeodeticBounds(tileNumber.getX(), tileNumber.getY(), map.Map.zoomLevel);
//                    break;
//                case Tags.TYPE_WMS_CACHE_CAPABILITIES_SERVER:
//                    profile = this.wmsCacheLayerDescription.profile;
//                    //si el perfil es mercator
//                    if (profile.toLowerCase().startsWith("global-m")) {
//                        //si las coordenadas están en grados
//                        if (CRSFactory.getCRS(this.SRS).getUnitsAbbrev().compareTo("g") == 0) {
//                            tileNumber = TileConversor.latLonToTileM(center.getX(), center.getY(), resolution);
//                        } else {
//                            tileNumber = TileConversor.mercatorToTile(center.getX(), center.getY(), resolution);
//                        }
//                        ex = TileConversor.tileBoundsM(tileNumber.getX(), tileNumber.getY(), resolution);
//                    } else if (profile.toLowerCase().startsWith("global-g")) {
//                        tileNumber = TileConversor.latLonToTileG(center.getX(), center.getY(), resolution);
//                        ex = TileConversor.tileBoundsG(tileNumber.getX(), tileNumber.getY(), resolution);
//                    } else {
//                        //local profile
//                        if (CRSFactory.getCRS(this.SRS).getUnitsAbbrev().compareTo("g") == 0) {
//                            tileNumber = TileConversor.latLonToTileG(center.getX(), center.getY(), resolution);
//                            ex = TileConversor.tileBoundsG(tileNumber.getX(), tileNumber.getY(), resolution);
//                        } else {
//                            tileNumber = TileConversor.metersToTile(center.getX(), center.getY(), resolution, -minX, -minY);
//                            ex = TileConversor.tileMeterBounds(tileNumber.getX(), tileNumber.getY(), resolution);
//                        }
//                    }
//                    break;
//                case Tags.TYPE_WMS_CACHE_TMS_SERVER:
//                    profile = this.wmsCacheLayerDescription.profile;
//                    //si el perfil es mercator
//                    if (profile.toLowerCase().startsWith("global-m")) {
//                        //si las coordenadas están en grados
//                        if (CRSFactory.getCRS(this.SRS).getUnitsAbbrev().compareTo("g") == 0) {
//                            tileNumber = TileConversor.latLonToTileM(center.getX(), center.getY(), resolution);
//                        } else {
//                            tileNumber = TileConversor.mercatorToTile(center.getX(), center.getY(), resolution);
//                        }
//                        ex = TileConversor.tileBoundsM(tileNumber.getX(), tileNumber.getY(), resolution);
//                    } else if (profile.toLowerCase().startsWith("global-g")) {
//                        tileNumber = TileConversor.latLonToTileG(center.getX(), center.getY(), resolution);
//                        ex = TileConversor.tileBoundsG(tileNumber.getX(), tileNumber.getY(), resolution);
//                    } else {
//                        //local profile
//                        tileNumber = TileConversor.metersToTile(center.getX(), center.getY(), resolution, -minX, -minY);
//                        ex = TileConversor.tileMeterBounds(tileNumber.getX(), tileNumber.getY(), resolution);
//                    }
//                    break;
//                case Tags.TYPE_WMS_SERVER:
//                    if (CRSFactory.getCRS(this.SRS).getUnitsAbbrev().compareTo("g") == 0) {
//                        tileNumber = TileConversor.latLonToTileG(center.getX(), center.getY(), resolution);
//                        ex = TileConversor.tileBoundsG(tileNumber.getX(), tileNumber.getY(), resolution);
//                    } else {
//                        tileNumber = TileConversor.metersToTile(center.getX(), center.getY(), resolution, -minX, -minY);
//                        ex = TileConversor.tileMeterBounds(tileNumber.getX(), tileNumber.getY(), resolution);
//                    }
//
//                    break;
//            }

//            Point tile = TileConversor.metersToTile(center.getX(), center.getY(), resolution, -minX, -minY);
//            ex = TileConversor.tileMeterBounds((int)tile.getX(), (int)tile.getY(), resolution);            

        } catch (final Exception e) {
            e.printStackTrace();
        }
        return ex;
    }
    
    public String buildQuery(final Extent e, final int zoomLevel, final double resolution) {
        final Point center = e.getCenter();
        int zoomYahoo = Tags.YAHOO_MIN_ZOOM_LEVEL - zoomLevel + 1;
                
                
        Pixel tileN = this.getTileNumber(e, zoomLevel, resolution, null);
        
        StringBuffer query = new StringBuffer().append(getURL())
                .append("&x=").append(tileN.getX())
                .append("&y=").append(tileN.getY())
                .append("&z=").append(zoomYahoo).append(".png");
                
        return query.toString();
    }
    
    /**
     * Calculates the quadkey string of the current tile
     * @param zoomLevel The current zoomLevel of the map
     * @return A StringBuffer with the quadkey String
     */
    public StringBuffer getQuadkey(final int tileX, final int tileY, final int zoomLevel) {
        try {
            if (tileX == -1) {
                return null;
            } else {
                return TileConversor.tileXYToQuadKey(tileX, tileY, zoomLevel);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Pixel getTileNumber(final Extent e, final int zoomLevel, final double resolution, final Projection proj) {
        Point p = TileConversor.mercatorToLatLon(e.getCenter().getX(), e.getCenter().getY());
        return TileConversor.getYahooTileNumber(p.getX(), p.getY(), zoomLevel);        
    }
}
