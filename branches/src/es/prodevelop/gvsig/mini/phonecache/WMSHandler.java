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

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.geom.Point;

/**
 *
 * @author aromeu 
 * @author rblanco
 */
public abstract class WMSHandler {  
    
    public static String[] URL = new String[]{};
    public static String type = ".png";
    private int index = 0;

    public String getType() {
        return type;
    }    
    
    public String getURL() {
        if(index >= URL.length) {
            index = 0;
        }
        return URL[index++];
    }
    
    public void setType(String aType) {
        type = aType;
    }
    
    public void setURL(String[] aURL) {
        URL = aURL;
    }
    
    public abstract String buildQuery(final Extent e, final int zoomLevel, final double resolution);
    
    /**
     * Calculates the quadkey string of the current tile
     * @param zoomLevel The current zoomLevel of the map
     * @return A StringBuffer with the quadkey String
     */
    public abstract StringBuffer getQuadkey(final int tileX, final int tileY, final int zoomLevel);
    
    public abstract Pixel getTileNumber(final Extent e, final int zoomLevel, final double resolution, final Projection proj);
    
    public abstract Extent getExtentIncludingCenter(Point center, int zoomLevel, Point origin);

}
