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

import java.text.DecimalFormat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import es.prodevelop.android.spatialindex.poi.Metadata;
import es.prodevelop.android.spatialindex.poi.Metadata.Indexed;
import es.prodevelop.android.spatialindex.poi.OsmPOI;
import es.prodevelop.android.spatialindex.poi.OsmPOIStreet;
import es.prodevelop.android.spatialindex.poi.POI;
import es.prodevelop.android.spatialindex.poi.POICategories;
import es.prodevelop.android.spatialindex.quadtree.provide.perst.PerstOsmPOIProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.search.MapPreview;
import es.prodevelop.gvsig.mini.search.POICategoryIcon;
import es.prodevelop.gvsig.mini.search.activities.POIDetailsActivity;
import es.prodevelop.gvsig.mini.search.activities.SearchActivity;
import es.prodevelop.gvsig.mini.search.filter.SimpleFilter;
import es.prodevelop.gvsig.mini.search.indexer.CategoryIndexer;
import es.prodevelop.gvsig.mini.utiles.Utilities;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class FilteredLazyAdapter extends BaseAdapter implements Filterable,
		SectionIndexer {

	SimpleFilter mFilter;
	SearchActivity activity;
	DecimalFormat formatter = new DecimalFormat("####.00");
	Indexed category;
	Metadata metadata;
	Bitmap bitmap;
	public int pos = -1;
	SectionIndexer mIndexer;
	public SectionIndexer defaultIndexer;

	public FilteredLazyAdapter(SearchActivity activity) {
		this.activity = activity;
		category = activity.getSearchOptions().getFilteredIndexed();
		if (category != null) {
			if (category.name.compareTo(POICategories.STREETS) == 0) {
				metadata = activity.getProvider().getStreetMetadata();
				bitmap = POICategoryIcon.getBitmap32ForCategory(category.name);
			} else {
				metadata = activity.getProvider().getPOIMetadata();
				Metadata.Category cat = metadata.getCategory(category.name);
				if (cat != null) {
					bitmap = POICategoryIcon.getBitmap32ForCategory(cat.name);
				} else {
					bitmap = POICategoryIcon.getBitmap32ForCategory(metadata
							.getCategoryForSubcategory(category.name).name);
				}
			}
			defaultIndexer = new CategoryIndexer(activity);
			setIndexer(defaultIndexer);
		} else {
			metadata = activity.getProvider().getPOIMetadata();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (activity.getResultsList() != null)
			return activity.getResultsList().size();
		return category.total;
	}

	@Override
	public Object getItem(int arg0) {
		if (activity.getResultsList() != null) {
			return activity.getResultsList().get(arg0);
		}

		return ((PerstOsmPOIProvider) activity.getProvider()).getAt(
				category.name, arg0);
	}

	@Override
	public long getItemId(int arg0) {
		if (activity.getResultsList() != null)
			return ((POI) activity.getResultsList().get(arg0)).getId();
		return ((POI) getItem(arg0)).getId();
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is
		// no need
		// to reinflate it. We only inflate a new View when the convertView
		// supplied
		// by ListView is null.
		final POI p = (POI) getItem(arg0);

		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.street_row, null);

			// Creates a ViewHolder and store references to the two children
			// views
			// we want to bind data to.
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.desc);
			holder.dist = (TextView) convertView.findViewById(R.id.dist);
			holder.poiImg = (ImageView) convertView.findViewById(R.id.img);

			try {
				LinearLayout l = new LinearLayout(activity);
				final RelativeLayout.LayoutParams zzParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				zzParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
				zzParams.addRule(RelativeLayout.CENTER_VERTICAL);
				// zzParams.setMargins(20, 20, 20, 20);
				MapPreview preview = new MapPreview(activity, CompatManager
						.getInstance().getRegisteredContext(),
						activity.metrics.widthPixels,
						activity.metrics.heightPixels / 2);
				l.addView(preview);
				((LinearLayout) ((LinearLayout) convertView)
						.findViewById(R.id.map_preview)).addView(l, zzParams);
				holder.previewLayout = l;
				holder.preview = preview;
				holder.optionsButton = (Button) convertView
						.findViewById(R.id.show_options);
				holder.detailsButton = (Button) convertView
						.findViewById(R.id.show_details);
				holder.optionsButton.setFocusable(false);
				holder.detailsButton.setFocusable(false);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		// ((TextView) arg1).setTextSize(26);
		// ((TextView) arg1).setTextColor(Color.WHITE);

		if (p == null)
			return convertView;
		holder.optionsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.getPOItemClickListener().onPOIClick(arg0, p);
			}
		});
		holder.detailsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, POIDetailsActivity.class);
				if (p != null && p instanceof OsmPOI) {
					OsmPOI poi = (OsmPOI) p;
					final Point centerM = getCenterMercator();
					final double distance = centerM.distance(ConversionCoords
							.reproject(p.getX(), p.getY(),
									CRSFactory.getCRS("EPSG:4326"),
									CRSFactory.getCRS("EPSG:900913")));
					String dist = formatter.format(formatKM(distance)) + " "
							+ unit(distance);
					i.putExtra(POIDetailsActivity.X, poi.getX());
					i.putExtra(POIDetailsActivity.Y, poi.getY());
					i.putExtra(POIDetailsActivity.DIST, dist);
					i.putExtra(POIDetailsActivity.DESC, poi.getDescription());
					i.putExtra(POIDetailsActivity.ADDR, poi.getAddress());
					i.putExtra(POIDetailsActivity.CAT, poi.getCategory());
					i.putExtra(POIDetailsActivity.SCAT, poi.getSubcategory());
					i.putExtra(POIDetailsActivity.IMG, poi.getImage());
					i.putExtra(POIDetailsActivity.INFO, poi.getInfo());
					i.putExtra(POIDetailsActivity.MAIL, poi.getEmail());
					i.putExtra(POIDetailsActivity.PHONE, poi.getPhone());
					i.putExtra(POIDetailsActivity.URL, poi.getUrl());
					i.putExtra(POIDetailsActivity.WEB, poi.getWebsite());
					i.putExtra(POIDetailsActivity.WIKI, poi.getWikipedia());
					activity.startActivity(i);
				} else {
					// throw exception
				}
			}
		});
		String desc = Utilities
				.capitalizeFirstLetters((p.getDescription() != null) ? p
						.getDescription() : "?");

		if (holder.preview != null) {
			if (arg0 == pos) {

				holder.previewLayout.setVisibility(View.VISIBLE);
				holder.optionsButton.setVisibility(View.VISIBLE);
				if (!(p instanceof OsmPOIStreet))
					holder.detailsButton.setVisibility(View.VISIBLE);

				holder.preview.setMapCenterFromLonLat(p);
				if ((getItem(arg0) instanceof OsmPOIStreet)) {
					holder.preview.setExtent(((OsmPOIStreet) p)
							.getBoundingBox());
				}
			} else {
				holder.previewLayout.setVisibility(View.GONE);
				holder.optionsButton.setVisibility(View.GONE);
				holder.detailsButton.setVisibility(View.GONE);

			}
		}

		// Bind the data efficiently with the holder.
		holder.text.setText(desc);
		final Point centerM = getCenterMercator();

		final double distance = centerM.distance(ConversionCoords.reproject(
				p.getX(), p.getY(), CRSFactory.getCRS("EPSG:4326"),
				CRSFactory.getCRS("EPSG:900913")));
		holder.dist.setText(activity.getResources()
				.getString(R.string.distance)
				+ " "
				+ formatter.format(formatKM(distance)) + " " + unit(distance));
		Bitmap b = bitmap;
		if (b == null) {
			if (p instanceof OsmPOI) {
				b = POICategoryIcon.getBitmap32ForCategory(((OsmPOI) p)
						.getCategory());
			} else {
				b = POICategoryIcon
						.getBitmap32ForCategory(POICategories.STREETS);
			}
		}

		holder.poiImg.setImageBitmap(b);

		// holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 :
		// mIcon2);

		return convertView;
		// if (arg0 == pos) {
		// LinearLayout v = new LinearLayout(HelloListView.this);
		// v.setOrientation(LinearLayout.VERTICAL);
		// v.addView(new EditText(HelloListView.this));
		// v.addView(new EditText(HelloListView.this));
		// TextView t = new TextView(HelloListView.this);
		// t.setText("ABCDEFGHASDFAS )AS DFASD" + "FASDFASD " + "AFDASD"
		// + "FSADFADFASDF");
		// Button ob = new Button(HelloListView.this);
		// v.addView(t);
		// v.addView(ob);
		// return v;
		// }
	}

	protected Point getCenterMercator() {
		return activity.getSearchOptions().getCenterMercator();
	}

	protected Point getCenterToCompare() {
		return activity.getSearchOptions().getCenterLatLon();
	}

	protected double formatKM(double meterDistance) {
		if (meterDistance > 1000) {
			return meterDistance / 1000;
		}
		return meterDistance;
	}

	protected String unit(double meterDistance) {
		if (meterDistance > 1000) {
			return "Km.";
		}
		return "m.";
		// return"";
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new SimpleFilter(activity);
		}
		return mFilter;
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
		activity.setTitle(R.string.search_results);
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
		activity.setTitle(R.string.search_results);
		activity.setProgressBarIndeterminateVisibility(false);
	}

	class ViewHolder {
		TextView text;
		TextView dist;
		MapPreview preview;
		LinearLayout previewLayout;
		ImageView poiImg;
		Button optionsButton;
		Button detailsButton;
	}

	public SectionIndexer getIndexer() {
		return mIndexer;
	}

	public Object[] getSections() {
		if (mIndexer == null) {
			return new String[] { "" };
		} else {
			return mIndexer.getSections();
		}
	}

	public int getPositionForSection(int sectionIndex) {
		if (mIndexer == null) {
			return -1;
		}

		return mIndexer.getPositionForSection(sectionIndex);
	}

	public int getSectionForPosition(int position) {
		if (mIndexer == null) {
			return -1;
		}

		return mIndexer.getSectionForPosition(position);
	}

	public void setIndexer(SectionIndexer indexer) {
		this.mIndexer = indexer;
	}

	public void setDefaultIndexer() {
		this.mIndexer = defaultIndexer;

	}
}