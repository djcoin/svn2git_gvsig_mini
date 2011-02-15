package es.prodevelop.gvsig.mini.context.osmpoi;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import es.prodevelop.android.spatialindex.cluster.Cluster;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.context.map.DefaultContext;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.tasks.map.ShowStreetView;
import es.prodevelop.gvsig.mini.tasks.poi.AddFavourite;
import es.prodevelop.gvsig.mini.tasks.poi.CommonPOITask;
import es.prodevelop.gvsig.mini.tasks.poi.RemoveFavourite;
import es.prodevelop.gvsig.mini.tasks.twitter.ShareMyLocationFunc;
import es.prodevelop.gvsig.mini.tasks.yours.FinishPointFunctionality;
import es.prodevelop.gvsig.mini.tasks.yours.StartPointFunctionality;

public class OSMPOIContext extends DefaultContext {

	private boolean isFavourite;
	private boolean isResultSearch;

	private Point poi;

	public OSMPOIContext(boolean isFavourite, boolean isResultSearch, POI poi) {
		super();
		this.isFavourite = isFavourite;
		this.isResultSearch = isResultSearch;
		this.poi = poi;
	}

	public OSMPOIContext(Map map, boolean isFavourite, boolean isResultSearch,
			Point poi) {
		super(map);
		this.isFavourite = isFavourite;
		this.isResultSearch = isResultSearch;
		this.poi = poi;
	}

	@Override
	public HashMap getFunctionalities() {
		h = new HashMap();
		try {
			ShowStreetView ssv = new ShowStreetView(map,
					R.layout.streetview_image_button);
			StartPointFunctionality sp = new StartPointFunctionality(map,
					R.layout.route_start_image_button);
			FinishPointFunctionality fp = new FinishPointFunctionality(map,
					R.layout.route_end_image_button);
			ShareMyLocationFunc tp = new ShareMyLocationFunc(map,
					R.layout.twitter_image_button);

			h.put(sp.getID(), sp);
			h.put(fp.getID(), fp);
			h.put(tp.getID(), tp);
			h.put(ssv.getID(), ssv);

			if (poi instanceof Cluster) {
				// TODO add list pois
			} else {
				if (!(poi instanceof POI))
					return h;

				POI p = (POI) poi;
				if (isFavourite) {
					// RemoveFavourite rf = new RemoveFavourite(map,
					// R.layout.rem_fav_image_button, p, map);
					// h.put(rf.getID(), rf);
				} else {
					AddFavourite af = new AddFavourite(map,
							R.layout.add_fav_image_button, p, map);
					h.put(af.getID(), af);
				}

				CommonPOITask csn = new CommonPOITask(map,
						R.layout.streets_near_image_button, p, map,
						CommonPOITask.FIND_STREET);

				h.put(csn.getID(), csn);

				CommonPOITask cpn = new CommonPOITask(map,
						R.layout.pois_near_image_button, p, map,
						CommonPOITask.FIND_POI);

				h.put(cpn.getID(), cpn);

				if (poi instanceof OsmPOI) {
					CommonPOITask ccn = new CommonPOITask(map,
							R.layout.call_image_button, p, map,
							CommonPOITask.CALL);

					h.put(ccn.getID(), ccn);

					CommonPOITask cwn = new CommonPOITask(map,
							R.layout.web_image_button, p, map,
							CommonPOITask.WEB);

					h.put(cwn.getID(), cwn);

					CommonPOITask cwin = new CommonPOITask(map,
							R.layout.wikipedia_image_button, p, map,
							CommonPOITask.WIKI);

					h.put(cwin.getID(), cwin);
				}
			}
		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
		}
		return h;
	}

	@Override
	public int[] getViewsId() {
		try {
			ArrayList list = new ArrayList();
			list.add(R.layout.twitter_image_button);
			list.add(R.layout.route_end_image_button);
			list.add(R.layout.route_start_image_button);
			list.add(R.layout.streetview_image_button);
			if (isFavourite) {
				// list.add(R.layout.rem_fav_image_button);
			} else {
				if (poi instanceof POI)
					list.add(R.layout.add_fav_image_button);
			}

			list.add(R.layout.streets_near_image_button);
			list.add(R.layout.pois_near_image_button);

			if (poi instanceof Cluster) {

			} else {
				if (poi instanceof OsmPOI) {
					if (((OsmPOI) poi).getPhone() != null
							&& ((OsmPOI) poi).getPhone().length() > 0)
						list.add(R.layout.call_image_button);

					if (((OsmPOI) poi).getWebsite() != null
							&& ((OsmPOI) poi).getWebsite().length() > 0)
						list.add(R.layout.web_image_button);

					if (((OsmPOI) poi).getWikipedia() != null
							&& ((OsmPOI) poi).getWikipedia().length() > 0)
						list.add(R.layout.wikipedia_image_button);
				}
			}

			int size = list.size();
			int[] res = new int[size];

			for (int i = 0; i < size; i++) {
				res[i] = (Integer) list.get(i);
			}

			return res;

		} catch (Exception e) {
			if (e != null && e.getMessage() != null)
				Log.e("", e.getMessage());
			return null;
		}
	}

}
