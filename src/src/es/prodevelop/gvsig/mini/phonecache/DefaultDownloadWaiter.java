/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* Prodevelop, S.L.
* Pza. Juan de Villarrasa, 14 - 5
* Valencia
* Spain
* Tel: +34 963510612
* e-mail: prode@prodevelop.es
* http://www.prodevelop.es
*
*/

package es.prodevelop.gvsig.mini.phonecache;

/**
 *
 * @author aromeu 
 * @author rblanco
 */
public class DefaultDownloadWaiter implements DownloadWaiter {

    public void incrementProgressBar(int progress) {
        System.out.println("progress: "+progress);
    }

    public void downloadCanceled() {
        System.out.println("download canceled");
    }

    public void finishDownload() {
        System.out.println("download finished");
    }

    public void numTilesRetrieved(int totalNumTiles) {
        System.out.println(totalNumTiles);
    }

    public void startDownload() {
        System.out.println("start download");
    }

    public void tileDownloaded(String URL) {
        System.out.println(URL);
    }

}
