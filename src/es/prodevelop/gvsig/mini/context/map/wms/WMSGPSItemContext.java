package es.prodevelop.gvsig.mini.context.map.wms;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.context.map.GPSItemContext;
import es.prodevelop.gvsig.mini.tasks.wms.GetFeatureInfoFunc;
import es.prodevelop.gvsig.mini.util.Utils;

public class WMSGPSItemContext extends GPSItemContext {
	
	private final static Logger log = Logger
			.getLogger(WMSGPSItemContext.class.getName());

	public WMSGPSItemContext() {
		super();
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	public WMSGPSItemContext(Map map) {
		super(map);
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	@Override
	public HashMap getFunctionalities() {
		HashMap h = super.getFunctionalities();
		try {
			h.put(R.layout.info_image_button, new GetFeatureInfoFunc(map,
					R.layout.info_image_button));
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
		return h;
	}

	@Override
	public int[] getViewsId() {
		int[] views = super.getViewsId();
		final int size = views.length;

		int[] v = new int[size + 1];
		try {

			for (int i = 0; i < size; i++) {
				v[i] = views[i];
			}
			v[size] = R.layout.info_image_button;
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			return null;
		}
		return v;
	}

}
