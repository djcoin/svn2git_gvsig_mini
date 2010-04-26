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
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.views.overlay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.map.GPSPoint;
import es.prodevelop.gvsig.mini.map.ViewPort;
import es.prodevelop.gvsig.mini.phonecache.Grid;
import es.prodevelop.gvsig.mini.phonecache.OSMHandler;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mini.utiles.Tags;

public class CacheView extends View {

	public CacheView(Context context) {
		super(context);
		
	}

	public CacheView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void build() {
		LayoutInflater factory = LayoutInflater.from(getContext());
		final View textEntryView = factory.inflate(R.layout.cache, null);
		Spinner sp = (Spinner) textEntryView.findViewById(R.id.spinner_edit);
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				Log.e("", String.valueOf(position));
//				reportView = (TextView) textEntryView
//						.findViewById(R.id.ReportView);

//				GPSPoint center = Map.this.osmap.getMapCenter();
//				Point centerDouble = new Point(center.getLongitudeE6() / 1E6,
//						center.getLatitudeE6() / 1E6);
//				centerDouble = TileConversor.latLonToMercator(centerDouble
//						.getX(), centerDouble.getY());
			
//				Extent e = ViewPort.calculateExtent(centerDouble,
//						Tags.RESOLUTIONS[Map.this.osmap.getZoomLevel()],
//						Map.this.osmap.getWidth(), Map.this.osmap.getHeight());

				
//				OSMHandler handler = new OSMHandler();
//				handler.setURL(new String[] { Map.this.osmap.getMRendererInfo()
//						.getBASEURL() });
//
//				double total = Grid.calcTotalTilesToDownload(
//						Tags.RESOLUTIONS[0], Tags.RESOLUTIONS[position + 1], e,
//						handler);
//				reportView.setText("Tiles to download: "
//						+ String.valueOf(total));
			}

			@Override
			public void onNothingSelected(AdapterView arg0) {

			}
		});
		AlertDialog.Builder alertCache = new AlertDialog.Builder(getContext());
		alertCache.setView(textEntryView).setIcon(R.drawable.menu04).setTitle(
				R.string.alert_dialog_cache_title).setPositiveButton(
				R.string.alert_dialog_start_cache,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						Spinner zoomLevels = (Spinner) textEntryView
								.findViewById(R.id.spinner_edit);

						int minZoom = 0;
						int maxZoom = Integer.valueOf(
								zoomLevels.getSelectedItem().toString())
								.intValue();

						TextView reportView = (TextView) textEntryView
								.findViewById(R.id.ReportView);

//						Map.this.startCache(Map.this.osmap.getMRendererInfo(),
//								minZoom, maxZoom);

					}

				}).setNegativeButton(R.string.alert_dialog_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).create();
		alertCache.show();
	}
}
