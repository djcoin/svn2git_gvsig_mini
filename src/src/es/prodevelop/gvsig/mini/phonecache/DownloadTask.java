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

import java.io.FileNotFoundException;
import java.net.URL;

/**
 *
 * @author aromeu 
 * @author rblanco
 */
public class DownloadTask extends Cancellable implements Runnable {

    private URL URL;    
    private String quadKey;
    private String baseDir;
    private String type; 
    private DownloadWaiter waiter;
    private double totalTiles;
    protected Grid grid;
    private WMSHandler handler;
    
    public DownloadTask(final WMSHandler handler, final URL URL, final String quadKey, final String baseDir, final String type, final DownloadWaiter d, double totalTiles, Grid g) {
        this.groupID = Utilities.getCancellableID();
        this.URL = URL;        
        this.quadKey = quadKey;
        this.baseDir = baseDir;
        this.type = type;
        this.waiter = d;
        this.totalTiles = totalTiles;
        grid = g;
        this.handler = handler;
    }
    public void run() {
        try {        	
        	if (handler instanceof YahooHandler)
        		Utilities.downloadFileYahoo(URL, baseDir+quadKey, this, type, waiter);
        	else
        		Utilities.downloadFile(URL, baseDir+quadKey, this, type, waiter);            
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: Avisar si hay un error en la descarga
        } finally {
        	if (grid.checkCanceled()) {
				return;
			}
        	this.waiter.tileDownloaded(URL.toString());
        	grid.actualTile++;
        	if (grid.actualTile == totalTiles) {
        		this.waiter.finishDownload();
        	}
        }
    }
}
