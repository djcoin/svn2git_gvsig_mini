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
import java.io.IOException;
import java.net.URL;

import android.os.Environment;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Pixel;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mini.tasks.WorkQueue;
import es.prodevelop.gvsig.mini.util.Utils;

/**
 * 
 * @author aromeu 
 * @author rblanco
 */
public class Grid implements Runnable {

	private DownloadWaiter downloadWaiter;
	private Extent e;
	private double minResolution;
	private double maxResolution;
	private WMSHandler handler;
	private String baseDir;
	private double totalTiles = 0;
	protected int actualTile = 0;
	protected Cancellable cancel;
	private String SDDIR = null;

	public Grid(WMSHandler handler, Extent extent, double minResolution,
			double maxResolution, String baseDir, Cancellable canc) throws IOException {
		this.e = (Extent)extent.clone();
		this.maxResolution = maxResolution;
		this.minResolution = minResolution;
		this.handler = handler;		
		this.cancel = canc;
		if (!Utils.isSDMounted()) {
			throw new IOException();
		} else {
			SDDIR = Environment.getExternalStorageDirectory().getPath();
			if (SDDIR == null) {
				throw new IOException();
			} else {
				this.baseDir = SDDIR + "/" + Utils.APP_DIR + "/"
						+ Utils.MAPS_DIR + "/" + baseDir + "/" + "0/";
			}
		}
	}

	/**
	 * Returns the total number of tiles to be downloaded. This method can be called
	 * prior to launch the massive download task
	 * @param minResolution The min resolution
	 * @param maxResolution The max resolution
	 * @param e The Bounding Box
	 * @param handler The WMSHandler to use
	 * @return the total number of tiles to be downloaded
	 */
	public static double calcTotalTilesToDownload(double minR, double maxR, Extent ext, WMSHandler h) {
		int minZoomLevel = -1;
		int maxZoomLevel = -1;
		for (int i = 0; i < Tags.RESOLUTIONS.length; i++) {
			if (Tags.RESOLUTIONS[i] == minR) {
				minZoomLevel = i;
			}
			if (Tags.RESOLUTIONS[i] == maxR) {
				maxZoomLevel = i;
			}
		}

		// Calculamos el extent que cubre toda la zona
		Extent minExtent = h.getExtentIncludingCenter(ext
				.getLefBottomCoordinate(), maxZoomLevel, null);
		Extent maxExtent = h.getExtentIncludingCenter(ext
				.getRightTopCoordinate(), maxZoomLevel, null);

		ext = (Extent)ext.clone();
		ext.setLeftBottomCoordinate(minExtent.getLefBottomCoordinate());
		ext.setRightTopCoordinate(maxExtent.getRightTopCoordinate());

		double distanceX = ext.getMaxX() - ext.getMinX();
		double distanceY = ext.getMaxY() - ext.getMinY();

		return calculateNumTiles(distanceX, distanceY, minZoomLevel,
				maxZoomLevel);

	}

	/**
	 * Adds the downloadWaiter
	 * 
	 * @param downloadWaiter
	 *            wich bill be notified of tile download events
	 * @see #downloadWaiter
	 */
	public void addDownloadWaiter(DownloadWaiter downloadWaiter) {
		this.downloadWaiter = downloadWaiter;
	}

	public void run() {
		// calcular minimo zommlevel y maximozoomlevel

		int minZoomLevel = -1;
		int maxZoomLevel = -1;
		for (int i = 0; i < Tags.RESOLUTIONS.length; i++) {
			if (Tags.RESOLUTIONS[i] == minResolution) {
				minZoomLevel = i;
			}
			if (Tags.RESOLUTIONS[i] == maxResolution) {
				maxZoomLevel = i;
			}
		}

		if (minZoomLevel == -1 || maxZoomLevel == -1) {
			// TODO: Lanzar excepción
			return;
		}

		// Calculamos el extent que cubre toda la zona
		Extent minExtent = handler.getExtentIncludingCenter(this.e
				.getLefBottomCoordinate(), maxZoomLevel, null);
		Extent maxExtent = handler.getExtentIncludingCenter(this.e
				.getRightTopCoordinate(), maxZoomLevel, null);

		this.e.setLeftBottomCoordinate(minExtent.getLefBottomCoordinate());
		this.e.setRightTopCoordinate(maxExtent.getRightTopCoordinate());

		// para cada nivel de zoom recorremos filas y columnas y obtenemos tiles
		for (int i = minZoomLevel; i <= maxZoomLevel; i++) {
			if (checkCanceled()) {
				return;
			}
			double currentResolution = Tags.RESOLUTIONS[i];

			Extent tempExtent = (Extent)e.clone();

			// if (i > 0)
			// tempExtent = handler.getExtentIncludingCenter(e.getCenter(), i -
			// 1, null);

			double distanceX = this.e.getMaxX() - this.e.getMinX();
			double distanceY = this.e.getMaxY() - this.e.getMinY();

			double delta = currentResolution * 256.0;
			double totalTilesX = Math.floor(Math.abs(distanceX / (delta)));
			double totalTilesY = Math.floor(Math.abs(distanceY / (delta)));

			double res = distanceX < distanceY ? distanceY : distanceX;
			// Extent tempExtent = new Extent(this.e.getMinX(), this.e.getMaxY()
			// - delta, this.e.getMinX() + delta, this.e.getMaxY());

			// si la zona que tenemos que descargar cabe en un tile calculamos
			// el extent
			// que cubre ese tile
			if (currentResolution > res) {
				tempExtent = handler.getExtentIncludingCenter(e.getCenter(), i,
						null);
			}

			if (i == minZoomLevel) {
				// The total number of tiles (more or less :P) to be downloaded
				totalTiles = calculateNumTiles(distanceX, distanceY,
						minZoomLevel, maxZoomLevel);
				// Send the total number of tiles to the progress bar
				this.downloadWaiter.numTilesRetrieved((int) totalTiles);
				// Notify that we are going to start the download party!
				this.downloadWaiter.startDownload();
			}

			// recorremos desde -1 hasta total+1, para coger algunos
			// tiles de más
			// TODO: Intentar que no haya FileNotFoundException
			for (int j = -1; j <= totalTilesX + 1; j++) {
				if (checkCanceled()) {
					return;
				}
				// if (j != 0) {
				tempExtent = new Extent(e.getMinX() + (delta * j), this.e
						.getMaxY()
						- delta, e.getMinX() + (delta * (j + 1)), this.e
						.getMaxY());
				// }

				DownloadTask d = null;
				for (int k = -1; k <= totalTilesY + 1; k++) {
					if (checkCanceled()) {
						return;
					}
					try {
						// if (k != 0) {
						tempExtent.setMinY(e.getMaxY() - (delta * (k + 1)));
						tempExtent.setMaxY(e.getMaxY() - (delta * k));
						// }
						URL url = new URL(handler.buildQuery(
								(Extent)tempExtent.clone(), i, currentResolution));
						Pixel tileN = handler.getTileNumber((Extent)tempExtent.clone(),
								i, currentResolution, null);
						StringBuffer quadkey = handler.getQuadkey(tileN.getX(),
								tileN.getY(), i);
						String quadDir = TileConversor.quadKeyToDirectory(
								quadkey).toString();
						d = new DownloadTask(handler, url, quadDir, baseDir,
								handler.getType(), this.downloadWaiter,
								totalTiles, this);

					} catch (Exception e) {
						e.printStackTrace();
						this.actualTile++;
					} finally {
						if (d != null)		
							WorkQueue.getInstance().execute(d);
//							ThreadPool.getInstance(10).assign(d);
					}
				}
			}
		}
		// this.downloadWaiter.finishDownload();

		// para cada nivel de zoom
		// con la resolucion y el extent calcular el nÃºmero de tiles en x e y
		// para
		// hcaer dos bucles
		// EN TOTAL TRES BUCLES
		// for zoom
		// for x
		// for y

		// dentro del bucle con el handler obtener la query y el quadkey

		// asignar una tarea al threadpool
	}

	/**
	 * Calculates the total number of tiles to be downloaded
	 * 
	 * @param distanceX
	 * @param distanceY
	 * @param minZoomLevel
	 * @param maxZoomLevel
	 * @return
	 */
	private static double calculateNumTiles(double distanceX, double distanceY,
			int minZoomLevel, int maxZoomLevel) {
		double maxResolution = Tags.RESOLUTIONS[maxZoomLevel];
		double delta = maxResolution * 256.0;
		double totalTilesX = Math.floor(Math.abs(distanceX / (delta))) + 3;
		double totalTilesY = Math.floor(Math.abs(distanceY / (delta))) + 3;
		double totalMax = totalTilesX * totalTilesY;
		double total = totalMax;
		for (int i = minZoomLevel; i < maxZoomLevel; i++) {
			maxResolution = Tags.RESOLUTIONS[i];
			delta = maxResolution * 256.0;
			totalTilesX = Math.floor(Math.abs(distanceX / (delta))) + 3;
			totalTilesY = Math.floor(Math.abs(distanceY / (delta))) + 3;
			totalMax = totalTilesX * totalTilesY;

			// totalMax = totalMax/4;
			// if (totalMax <= 1) totalMax = 1;
			total += totalMax;
		}
		return total;
	}

	public boolean checkCanceled() {
		try {
			if (cancel.getCanceled()) {
				downloadWaiter.downloadCanceled();
				return true;
			}
		} catch (Exception ignore) {
			return false;
		}
		return false;
	}
}
