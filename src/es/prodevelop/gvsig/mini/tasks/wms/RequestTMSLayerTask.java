/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2011 Prodevelop.
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
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.tasks.wms;

import android.os.Handler;
import android.os.Message;
import es.prodevelop.gvsig.mini.activities.LayersActivity;
import es.prodevelop.gvsig.mini.wms.WMSCancellable;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.TMSRenderer;

public class RequestTMSLayerTask implements Runnable {

	private String layerURL;
	private Handler handler;
	private WMSCancellable cancellable;

	public RequestTMSLayerTask(String layerURL, Handler handler) {
		this.layerURL = layerURL;
		this.handler = handler;
	}

	public void run() {
		try {
			cancellable = new WMSCancellable();
			int index = layerURL.lastIndexOf("/");
			int size = Layers.getInstance().getLayers().size();
			String layerName = "tms_layer" + size;

			if (index != -1) {
				if (index == layerURL.length() - 1) {
					int index2 = layerURL.substring(0, index - 1).lastIndexOf(
							"/");
					layerName = layerURL.substring(index2 + 1, index);
				} else {
					layerName = layerURL
							.substring(index + 1, layerURL.length());
					layerURL += "/";
				}
			}
			MapRenderer renderer = TMSRenderer.getTMSRenderer(layerURL,
					layerName);

			if (renderer == null)
				handler.sendEmptyMessage(LayersActivity.WMS_ERROR);

			Layers.getInstance().addLayer(size + "|" + renderer.toString());
			Layers.getInstance().persist();

			if (cancellable.isCanceled()) {
				handler.sendEmptyMessage(LayersActivity.WMS_CANCELED);
			} else {
				Message msg = handler.obtainMessage();
				msg.obj = layerName;
				msg.what = LayersActivity.TMS_CONNECTED;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(LayersActivity.WMS_ERROR);
		}
	}

	public void cancel() {
		if (cancellable != null)
			cancellable.canceled = true;
	}
}