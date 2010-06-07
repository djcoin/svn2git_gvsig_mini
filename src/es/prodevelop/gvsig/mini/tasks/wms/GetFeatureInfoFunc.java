package es.prodevelop.gvsig.mini.tasks.wms;

import java.net.URL;

import org.gvsig.remoteclient.wms.WMSStatus;

import android.os.Message;
import android.widget.Toast;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.namefinder.Named;
import es.prodevelop.gvsig.mini.tasks.Functionality;
import es.prodevelop.gvsig.mini.tasks.TaskHandler;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.wms.FMapWMSDriver;
import es.prodevelop.gvsig.mini.wms.FMapWMSDriverFactory;
import es.prodevelop.gvsig.mini.wms.WMSCancellable;
import es.prodevelop.tilecache.renderer.MapRenderer;
import es.prodevelop.tilecache.renderer.wms.WMSRenderer;

public class GetFeatureInfoFunc extends Functionality {

	private int res;
	private Logger logger = LoggerFactory.getLogger(GetFeatureInfoFunc.class);

	public GetFeatureInfoFunc(Map map, int id) {
		super(map, id);
	}

	@Override
	public boolean execute() {
		try {
			if (!Utils.isSDMounted()) {
				Message m = Message.obtain();
				m.what = Map.SHOW_TOAST;
				m.obj = getMap().getText(R.string.LayersActivity_1);
				getMap().getMapHandler().sendMessage(m);
				return true;
			}
			getMap().getMapHandler().sendEmptyMessage(Map.GETFEATURE_INITED);
			final MapRenderer mapRenderer = getMap().osmap.getMRendererInfo();
			if (mapRenderer.getType() == MapRenderer.WMS_RENDERER) {

				final WMSRenderer wmsRenderer = (WMSRenderer) mapRenderer;
				FMapWMSDriver driver = FMapWMSDriverFactory
						.getFMapDriverForURL(new URL(wmsRenderer.getBASEURL()));
				boolean hasConnected = driver.connect(new WMSCancellable());
				if (!hasConnected) {
					getMap().showToast(R.string.error_connecting_server);
					res = TaskHandler.FINISHED;
					return true;
				}
				if (driver.isQueryable()) {
					final WMSStatus status = wmsRenderer.buildWMSStatus();
					status.setWidth(getMap().osmap.getWidth());
					status.setHeight(getMap().osmap.getHeight());
					status
							.setExtent(getMap().vp.calculateExtent(getMap().osmap.getWidth(),
									getMap().osmap.getHeight(), getMap().osmap.getMRendererInfo()
											.getCenter()));
					String info = driver.getFeatureInfo(status, getMap().osmap
							.getWidth() / 2, getMap().osmap.getHeight() / 2, 10,
							new WMSCancellable());
					logger.debug("GetFeatureInfo: " + info);
					Message m = Message.obtain();
					m.what = Map.SHOW_OK_DIALOG;
					m.obj = info;
					getMap().getMapHandler().sendMessage(m);
				} else {
					showToastError();
				}
			} else {
				showToastError();
			}
		} catch (Exception e) {
			logger.error(e);
			res = TaskHandler.ERROR;
		}
		return true;
	}
	
	private void showToastError() {
		Message m = Message.obtain();
		m.what = Map.SHOW_TOAST;
		m.obj = getMap().getResources().getString(R.string.GetFeatureInfoFunc_0); 
		getMap().getMapHandler().sendMessage(m);
		res = TaskHandler.FINISHED;
	}

	@Override
	public int getMessage() {
		return res;
	}
}
