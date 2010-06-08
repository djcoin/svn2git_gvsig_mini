package es.prodevelop.gvsig.mini.context.map.wms;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.context.map.RouteContext;
import es.prodevelop.gvsig.mini.tasks.wms.GetFeatureInfoFunc;
import es.prodevelop.gvsig.mini.util.Utils;

public class WMSRouteContext extends RouteContext {

	private final static Logger log = Logger
			.getLogger(WMSRouteContext.class.getName());

	public WMSRouteContext() {
		super();
		try {
			log.setLevel(Utils.LOG_LEVEL);
//			log.setClientID(this.toString());
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
	}

	public WMSRouteContext(Map map) {
		super(map);
		try {
			log.setLevel(Utils.LOG_LEVEL);
//			log.setClientID(this.toString());
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
