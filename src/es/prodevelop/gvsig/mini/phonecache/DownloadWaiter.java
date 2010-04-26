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
 * This interface is used to watch the progress of the download process.
 * @author aromeu 
 * @author rblanco
 */
public interface DownloadWaiter {
    
    /**
     * This event is thrown after every single tile has been downloaded
     * 
     * @param URL - The URL of the image
     */
    public void tileDownloaded(String URL);  
    
    /**
     * The first event thrown
     * @param totalNumTiles - The total number tiles to be downloaded
     */
    public void numTilesRetrieved(final int totalNumTiles);
    
    /**
     * This event is thrown when no more tiles left to be downloaded
     */
    public void finishDownload();
    
    /**
     * This event indicates the download proceess is going to start
     */
    public void startDownload();    
    
    public void downloadCanceled();
    
    /**
     * This event indicates that the progress bar must be incremented
     * @param  progress - The progress of the download task from 0 to 100
     */
    public void incrementProgressBar(int progress);
}
