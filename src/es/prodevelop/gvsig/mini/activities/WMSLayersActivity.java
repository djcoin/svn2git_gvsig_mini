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
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la Peque�a y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2009.
 *   author Alberto Romeu aromeu@prodevelop.es 
 *   author Ruben Blanco rblanco@prodevelop.es 
 *   
 */

package es.prodevelop.gvsig.mini.activities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gvsig.remoteclient.utils.BoundaryBox;
import org.gvsig.remoteclient.wms.WMSLayer;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.wms.FMapWMSDriver;
import es.prodevelop.gvsig.mini.wms.FMapWMSDriverFactory;
import es.prodevelop.gvsig.mini.wms.WMSException;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

/**
 * A ListActivity which shows the results of a GetCapabilities. Contains a plain
 * list of layers represented by checkboxes
 * @author aromeu 
 * @author rblanco
 *
 */
public class WMSLayersActivity extends ListActivity {

	private Logger log = Logger.getLogger(WMSLayersActivity.class.getName());
	private String server;
	private String preferredFormat = "png";
	private String preferredSRS = "EPSG:230";

	public void onCreate(android.os.Bundle savedInstanceState) {
		try {
			CompatManager.getInstance().getRegisteredLogHandler().configureLogger(log);
			log.log(Level.FINE, "onCreate WMSLayersActivity");
			
			super.onCreate(savedInstanceState);
			setTitle(R.string.LayersActivity_9);
			server = getIntent().getStringExtra("server");
			final TileServerAdapter la = new TileServerAdapter();
			setListAdapter(la);
			getListView().setTextFilterEnabled(true);
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
			LogFeedbackActivity.showSendLogDialog(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		try {
			pMenu.add(0, 0, 0, R.string.WMSLayersActivity_0);
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		try {
			switch (item.getItemId()) {
			case 0:
				try {
					Intent data = new Intent();
					data.putExtra("loadLayers", true);
					ArrayList selectedLayers = new ArrayList();
					final ArrayList layerNames = getIntent()
							.getStringArrayListExtra("layers");

					boolean[] selected = ((TileServerAdapter) getListView()
							.getAdapter()).selected;
					final int length = selected.length;

					for (int i = 0; i < length; i++) {
						if (selected[i]) {
							log.log(Level.FINE, "selected: " + layerNames.get(i));
							selectedLayers.add(layerNames.get(i));
						}
					}

					data.putExtra("server", this.server);
					log.log(Level.FINE, "server: " + server);

					FMapWMSDriver wmsDriver = FMapWMSDriverFactory
							.getFMapDriverForURL(new URL(server));
					WMSLayer root = wmsDriver.getClient().getRootLayer();
					String srs = getBestSupportedSRS(root);
					// String srs = root.getAllSrs().get(0).toString();
					BoundaryBox bbox = root.getBbox(srs);
					if (bbox == null) {
						ArrayList l = root.getChildren();
						final int size = l.size();
						for (int i = 0; i < size && bbox == null; i++) {
							WMSLayer children = (WMSLayer) l.get(i);
							bbox = children.getBbox(srs);
						}
					}
					String format = getBestFormat(wmsDriver.getFormats());

					data.putExtra("version", wmsDriver.getVersion());

					data.putExtra("format", format);

					final int size = selectedLayers.size();
					String[] layers = new String[size];

					for (int i = 0; i < size; i++) {
						layers[i] = selectedLayers.get(i).toString();
					}
					data.putExtra("name", layers[0]);
					data.putExtra("layers", layers);
					data.putExtra("srs", srs);
					data.putExtra("minX", bbox.getXmin());
					data.putExtra("minY", bbox.getYmin());
					data.putExtra("maxX", bbox.getXmax());
					data.putExtra("maxY", bbox.getYmax());
					log.log(Level.FINE, "name: " + layers[0]);
					log.log(Level.FINE, "srs: " + srs);
					log.log(Level.FINE, "extent: " + bbox.toString());
					log.log(Level.FINE, "format: " + format);
					setResult(RESULT_OK, data);
					finish();
				} catch (WMSException we) {
					log.log(Level.SEVERE,"The layer is not supported: ", we);
					LogFeedbackActivity.showSendLogDialog(this);
					Toast.makeText(this, R.string.WMSLayersActivity_1,
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					log.log(Level.SEVERE,"",e);
					LogFeedbackActivity.showSendLogDialog(this);
//					Utils.showSendLogDialog(this, R.string.fatal_error);
					Toast.makeText(this, R.string.WMSLayersActivity_1,
							Toast.LENGTH_LONG).show();
				}
				break;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE,"",e);
		}
		return true;
	}

	private String getBestFormat(Vector aFormats) throws WMSException {
		String format = "";

		if (aFormats == null) {
			log.log(Level.FINE, "formats == null");
			throw new WMSException(this.getResources().getString(
					R.string.WMSLayersActivity_2));
		}

		final int length = aFormats.size();
		String temp = "";
		for (int i = 0; i < length; i++) {
			temp = aFormats.get(i).toString();
			if (temp.toLowerCase().contains(this.preferredFormat)) {
				return temp;
			}
		}
		return aFormats.get(0).toString();
	}

	private String getBestSupportedSRS(WMSLayer aRoot) throws WMSException {

		if (aRoot.getLatLonBox() != null) {
			return "EPSG:4326";
		}

		final Vector aSRS = aRoot.getAllSrs();
		final int length = aSRS.size();

		String temp = "";
		for (int i = 0; i < length; i++) {
			temp = aSRS.get(i).toString();
			if (temp.toUpperCase().contains(this.preferredSRS)) {
				log.log(Level.FINE, "selected SRS: " + temp);
				return temp;
			}
		}

		for (int i = 0; i < length; i++) {
			temp = aSRS.get(i).toString();
			log.log(Level.FINE, "SRS: " + temp);
			if (CRSFactory.isSupportedSRS(temp)) {
				return temp;
			}
		}

		throw new WMSException(this.getResources().getString(
				R.string.WMSLayersActivity_3));

	}

	protected class TileServerAdapter extends BaseAdapter {

		ArrayList layerNames;
		boolean[] selected;

		public TileServerAdapter() {
			try {
				layerNames = getIntent().getStringArrayListExtra("layers");
				selected = new boolean[layerNames.size()];
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
			}
		}

		@Override
		public int getCount() {
			try {
				return layerNames.size();
			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final LinearLayout mainView = new LinearLayout(
					WMSLayersActivity.this);
			mainView.setOrientation(LinearLayout.VERTICAL);
			try {
				final int pos = position;

				final ImageView img = new ImageView(WMSLayersActivity.this);
				img.setAdjustViewBounds(true);

				final CheckBox nameView = new CheckBox(WMSLayersActivity.this);
				nameView.setClickable(true);
				nameView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							selected[pos] = !selected[pos];
							nameView.setChecked(selected[pos]);
						} catch (Exception e) {
							log.log(Level.SEVERE,"",e);
						}
					}
				});

				// try {
				// FMapWMSDriver wmsDriver = FMapWMSDriverFactory
				// .getFMapDriverForURL(new URL(server));
				// WMSLayer root = wmsDriver.getClient().getRootLayer();
				// String srs = root.getAllSrs().get(0).toString();
				// BoundaryBox bbox = root.getBbox(srs);
				// WMSStatus wmsStatus = new WMSStatus();
				// wmsStatus.addLayerName(layerNames.get(pos).toString());
				// wmsStatus.setSrs(srs);
				// // wmsStatus.setHeight(_height);
				// // wmsStatus.setWidth(_width);
				//wmsStatus.setFormat(wmsDriver.getFormats().get(0).toString());
				// wmsStatus.setExtent(new Extent(bbox.getXmin(),
				// bbox.getYmin(),
				// bbox.getXmax(), bbox.getYmax()));
				// wmsStatus.setWidth(128);
				// wmsStatus.setHeight(128);
				// try {
				// File f = wmsDriver.getMap(wmsStatus, new WMSCancellable());
				// FileInputStream fin = new FileInputStream(f);
				// byte[] data = new byte[(int) f.length()];
				// fin.read(data);
				// Bitmap b = BitmapFactory.decodeByteArray(data, 0,
				// data.length);
				//
				// img.setImageBitmap(b);
				//
				// } catch (WMSException e) {
				// log.log(Level.SEVERE,"",e);
				// } catch (OutOfMemoryError e) {
				// System.gc();
				// System.gc();
				// log.log(Level.SEVERE,"",e);
				// } catch (Exception e) {
				// log.log(Level.SEVERE,"",e);
				// }
				// } catch (ConnectException e) {
				// log.log(Level.SEVERE,"",e);
				// } catch (MalformedURLException e) {
				// log.log(Level.SEVERE,"",e);
				// } catch (IOException e) {
				// log.log(Level.SEVERE,"",e);
				// }

				nameView.setChecked(selected[pos]);

				String layerName = layerNames.get(pos).toString();

				if (layerName.length() == 0)
					layerName = "null";

				nameView.setText(layerName);
				nameView.setTextSize(20);
				nameView.setTextColor(Color.WHITE);

				mainView.addView(nameView, 0);
				// mainView.addView(img, 1);

			} catch (Exception e) {
				log.log(Level.SEVERE,"",e);
			}
			return mainView;
		}
	}
}
