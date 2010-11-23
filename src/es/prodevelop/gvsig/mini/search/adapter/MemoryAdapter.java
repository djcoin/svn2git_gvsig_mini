/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */

package es.prodevelop.gvsig.mini.search.adapter;

import android.widget.Filter;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.filter.MemoryFilter;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class MemoryAdapter extends PinnedHeaderListAdapter {

	private int type;
	private int distance;
	private Point point;
	private Point centerM;
	
	public MemoryAdapter(SearchActivity activity, int type, int distance, Point center) {
		super(activity);
		this.type = type;
		this.distance = distance;
		this.point = center;
	}
	
	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new MemoryFilter(activity, type, distance);
		}
		return mFilter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (activity.getResultsList() != null)
			return activity.getResultsList().size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (activity.getResultsList() != null) {
			return activity.getResultsList().get(arg0);
		}

		return null;
	}

	@Override
	public long getItemId(int arg0) {
		if (activity.getResultsList() != null)
			return ((POI) activity.getResultsList().get(arg0)).getId();
		return -1;
	}
	
	@Override
	protected Point getCenterToCompare() {
		return point;
	}
	
	public Point getCenterMercator() {
		if (centerM == null) {
			double[] centerMercator = ConversionCoords.reproject(
					getCenterToCompare().getX(), getCenterToCompare().getY(),
					CRSFactory.getCRS("EPSG:4326"),
					CRSFactory.getCRS("EPSG:900913"));
			this.centerM = new Point(centerMercator[0],
					centerMercator[1]);
		}
		
		return centerM;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		// pos = -1;
		super.notifyDataSetChanged();
		activity.setTitle(activity.getResources().getString(R.string.find_pois_near) + ": " +activity.getQuery());
		activity.setProgressBarIndeterminateVisibility(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#notifyDataSetInvalidated()
	 */
	@Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		// pos = -1;
		super.notifyDataSetInvalidated();
		activity.setTitle(activity.getResources().getString(R.string.find_pois_near) + ": " +activity.getQuery());
		activity.setProgressBarIndeterminateVisibility(false);
	}
}
