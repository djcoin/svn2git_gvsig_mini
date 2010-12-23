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