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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;
import android.widget.TwoLineListItem;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.common.CompatManager;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.tasks.wms.GetCapabilitiesTask;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;
import es.prodevelop.gvsig.mini.wms.FMapWMSDriverFactory;
import es.prodevelop.gvsig.mini.wms.WMSLayerNode;
import es.prodevelop.tilecache.layers.Layers;
import es.prodevelop.tilecache.renderer.wms.WMSRenderer;

/**
 * An ExpandableListActivity to manage layers. The default layers file is
 * bundled into the apk, the first time the application starts this file is
 * saved on "/sdcard/gvsig/layers/layers.txt". WMS layers are appended to that
 * file and an advanced user is able to edit the layers.txt to add new layer
 * sources. See
 * 
 * You can send an Intent with a boolean extra "FROM_FILE_EXPLORER". If is set
 * to false then it takes the URI of the layer file from
 * getIntent().getDataString() and tries to download and parse the file. If is
 * set to true, then a local file is passed to the Activity.
 */
public class LayersActivity extends ExpandableListActivity {

	private final static Logger log = Logger.getLogger(LayersActivity.class
			.getName());

	private static final String LAYER = "LAYER";
	private static final String CHILD = "CHILD";
	private static final String FROM_FILE_EXPLORER = "FromFileExplorer";
	private static final String GVTILES = "gvtiles";

	private ExpandableListAdapter mAdapter;
	private boolean layers = false;
	private String server;
	public static final int WMS_CONNECTED = 0;
	public static final int WMS_ERROR = 1;
	public static final int WMS_CANCELED = 2;
	private Handler handler;
	GetCapabilitiesTask gt;
	ProgressDialog dialog2;
	private boolean hasChanges = false;
	String path;
	private String gvTiles;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			try {
				CompatManager.getInstance().getRegisteredLogHandler()
						.configureLogger(log);
			} catch (BaseException e) {

			}

			setTitle(R.string.LayersActivity_0);
			handler = new WMSHandler(this);
			String layersData = this.getIntent().getDataString();
			Intent i = getIntent();
			if (layersData != null) {
				i.putExtra(FROM_FILE_EXPLORER, true);
				log.log(Level.FINE, "Layers activity from file explorer: "
						+ layersData);
			}
			onNewIntent(i);

		} catch (Exception e) {
			log.log(Level.SEVERE, "onCreate: ", e);
			LogFeedbackActivity.showSendLogDialog(this);
			// Utils.showSendLogDialog(this, R.string.fatal_error);
		}
	}

	@Override
	public void onNewIntent(Intent i) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader bf = null;

		try {
			log.log(Level.FINE, "on New intent");
			if (i.getBooleanExtra(FROM_FILE_EXPLORER, false)) {
				String layersData = this.getIntent().getDataString();
				if (layersData != null) {
					log.log(Level.FINE, "layersData: " + layersData);
					is = Utils.openConnection(layersData);
					isr = new InputStreamReader(is);
					bf = new BufferedReader(isr);
					Layers.getInstance().clearProperties();
					Layers.getInstance().parseLayersFile(bf);
					String layerFile = layersData.substring(
							layersData.lastIndexOf("/"), layersData.length());
					log.log(Level.FINE, "layerFile: " + layerFile);
					Layers.getInstance().persist(layerFile);
					String SDDIR = Environment.getExternalStorageDirectory()
							.getPath();
					gvTiles = SDDIR + File.separator + Utils.APP_DIR
							+ File.separator + Utils.LAYERS_DIR
							+ File.separator + layerFile;
					log.log(Level.FINE, "gvTiles: " + gvTiles);
					String text = this.getResources().getString(
							R.string.LayersActivity_4);
					if (layersData.startsWith("http")) {
						text = this.getResources().getString(
								R.string.download_tiles_03);
					}
					Toast.makeText(LayersActivity.this, text, Toast.LENGTH_LONG)
							.show();
				}
			} else {
				log.log(Level.FINE, "layersData is null");
				String filePath = null;
				try {
					filePath = i.getStringExtra(GVTILES);
					gvTiles = filePath;
					log.log(Level.FINE, "loading filePath: " + filePath);
				} catch (Exception e) {
					log.log(Level.SEVERE, "", e);
				}

				Layers.getInstance().loadProperties(filePath);
			}

			// if (i.getBooleanExtra("loadLayers", false)) {
			layers = true;
			Hashtable layers = Layers.getInstance().getLayersForView();
			this.loadLayersList(layers);
			// }
		} catch (Exception e) {
			log.log(Level.SEVERE, "onNewIntent: ", e);
			LogFeedbackActivity.showSendLogDialog(this);
		} finally {
			Utils.closeStream(is);
			Utils.closeStream(isr);
			Utils.closeStream(bf);
		}
	}

	/**
	 * Shows a dialog with the list of files of "sdcard/gvsig/layers". When
	 * selecting one of them it tries to parse and load the layers into de
	 * activity. If the directory is empty, then a toast informs the user about
	 * that.
	 */
	public void showLoadFileAlert() {
		try {
			log.log(Level.FINE, "showLoadFileAlert");

			if (!Utils.isSDMounted()) {
				log.log(Level.FINE, "SD not mounted");
				Toast.makeText(LayersActivity.this, R.string.LayersActivity_1,
						Toast.LENGTH_LONG).show();
			} else {
				String sdPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				path = sdPath + File.separator + Utils.APP_DIR + File.separator
						+ Utils.LAYERS_DIR;
				File f = new File(path);
				if (!f.exists()) {
					log.log(Level.FINE, path + " not exists");
					f.mkdirs();
				}
				final String[] files = f.list();
				if (files == null) {
					log.log(Level.FINE, path + " is empty");
					Toast.makeText(
							LayersActivity.this,
							this.getResources().getString(
									R.string.LayersActivity_2)
									+ ": " + f.getAbsolutePath(),
							Toast.LENGTH_LONG).show();
				} else {
					if (files.length == 0) {
						log.log(Level.FINE, path + " is empty");
						Toast.makeText(
								LayersActivity.this,
								this.getResources().getString(
										R.string.LayersActivity_2)
										+ ": " + f.getAbsolutePath(),
								Toast.LENGTH_LONG).show();
					} else {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								this);
						builder.setTitle(LayersActivity.this.getResources()
								.getString(R.string.LayersActivity_3)
								+ " ("
								+ path + ")");
						builder.setItems(files,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										try {
											server = path + File.separator
													+ files[item];
											log.log(Level.FINE,
													"selected layer file: "
															+ server);
											gvTiles = server;
											Layers.getInstance()
													.loadProperties(server);
											LayersActivity.this
													.loadLayersList(Layers
															.getInstance()
															.getLayersForView());
											log.log(Level.FINE,
													"layer file loaded: "
															+ server);
											Toast t = Toast.makeText(
													LayersActivity.this,
													R.string.LayersActivity_4,
													Toast.LENGTH_LONG);
											t.show();
										} catch (Exception e) {
											log.log(Level.SEVERE,
													"LayersActivity onClick: ",
													e);
											Toast t = Toast.makeText(
													LayersActivity.this,
													R.string.LayersActivity_5,
													2000);
											t.show();
										}
									}
								});
						AlertDialog alert = builder.create();

						alert.setIcon(R.drawable.menu02);
						// ListView l = new ListView(this);
						// l.setAdapter(new ArrayAdapter<String>(this,
						// android.R.layout.simple_list_item_1, files));
						// alert.setView(l);
						// l.setOnTouchListener(new OnTouchListener() {
						// @Override
						// public boolean onTouch(View arg0, MotionEvent arg1) {
						// try {
						// server = path + File.separator + ((TextView)
						// arg0).getText().toString();
						// Layers.getInstance().loadProperties(server);
						// LayersActivity.this.loadLayersList(Layers
						// .getInstance().getLayersForView());
						// } catch (Exception e) {
						// log.log(Level.SEVERE,e.getMessage());
						// Toast t = Toast
						// .makeText(
						// LayersActivity.this,
						// "Can't load file! The path you typed does not exist",
						// 2000);
						// t.show();
						// }
						// return true;
						// }
						// });
						// alert.setNegativeButton("Cancel",
						// new DialogInterface.OnClickListener() {
						// public void onClick(DialogInterface dialog,
						// int whichButton) {
						// }
						// });
						//
						alert.show();
					}
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "onMenuItemSelected: ", e);
		}
	}

	/***
	 * Shows a dialog to let the user type the address of a WMS server to
	 * request getCapabilities
	 */
	public void showGetCapabilitiesAlert() {
		try {
			log.log(Level.FINE, "showGetCapabilitiesAlert");
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setIcon(R.drawable.menu02);
			alert.setTitle(R.string.LayersActivity_6);
			final EditText input = new EditText(this);
			// input.setText("http://wms.globexplorer.com/gexservlets/wms");
			// input.setText(
			// "http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx");
			// input.setText("http://wms.larioja.org/request.asp?");

			// input
			// .setText(
			// "http://osami.prodevelop.es/tilecache/tilecache.py/1.0.0/prode2/"
			// );
			// input.setText(
			// "http://demo.gfosservices.it/cgi-bin/tc/tilecache.cgi");

			// input.setText("http://orto.wms.itacyl.es/WMS");

			// input
			// .setText(
			// "http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx");
			// input.setText(
			// "http://gdr.ess.nrcan.gc.ca/wmsconnector/com.esri.wms.Esrimap/gdr_e"
			// );

			alert.setView(input);

			alert.setPositiveButton(R.string.LayersActivity_7,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								Editable value = input.getText();
								server = value.toString();
								log.log(Level.FINE,
										"Get capabilities dialog ok: " + server);
								LayersActivity.this.callGetCapabilities(value
										.toString());
							} catch (Exception e) {
								log.log(Level.SEVERE,
										"onClick positiveButton: ", e);
							}
						}
					});

			alert.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							log.log(Level.FINE,
									"Get capabilities dialog canceled");
						}
					});

			alert.show();
		} catch (Exception e) {
			log.log(Level.SEVERE, "onMenuItemSelected: ", e);
		}
	}

	private void loadLayersList(Hashtable layers) {
		try {
			log.log(Level.FINE, "loadLayersList");
			List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
			List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
			final int size = layers.size();

			for (int i = 0; i < 3; i++) {
				Map<String, String> curGroupMap = new HashMap<String, String>();
				groupData.add(curGroupMap);
				Vector layersList = (Vector) layers.get(new Integer(i));
				String text = null;
				switch (i) {
				case 0:
					text = this.getResources().getString(
							R.string.LayersActivity_8);

					break;
				case 1:
					text = this.getResources().getString(
							R.string.LayersActivity_9);

					break;
				case 2:
					text = this.getResources().getString(
							R.string.LayersActivity_15);

					break;
				}
				curGroupMap.put(LAYER, text);

				// switch (i) {
				// case MapRenderer.OSM_RENDERER:
				//
				// break;
				// case MapRenderer.OSMPARMS_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Tile service (with different parameter encoding)";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.TMS_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Tile Map Service";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.QUADKEY_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Quadkey URL format";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.EQUATOR_RENDERER:
				// text = "Tile map service";
				// curGroupMap.put(LAYER, text);
				// // text = "Y origin at equator";
				// // curGroupMap.put(LAYER, text);
				// break;
				// case MapRenderer.WMS_RENDERER:
				// text = "WMS layers";
				// curGroupMap.put(LAYER, text);
				// break;
				// }

				List<Map<String, String>> children = new ArrayList<Map<String, String>>();
				if (layersList == null) {
					// if (text != null)
					// groupData.remove(curGroupMap);
				} else {
					final int length = layersList.size();

					for (int j = 0; j < length; j++) {
						Map<String, String> curChildMap = new HashMap<String, String>();
						;
						children.add(curChildMap);

						
						curChildMap.put(LAYER, layersList.elementAt(j)
								.toString());
					}
				}
				childData.add(children);

				if (i == 2) {
					Map<String, String> curGroupMap1 = new HashMap<String, String>();
					groupData.add(curGroupMap1);
					curGroupMap1.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_10));

					List<Map<String, String>> children1 = new ArrayList<Map<String, String>>();

					Map<String, String> curChildMap = new HashMap<String, String>();
					curChildMap.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_11));
					curChildMap.put(
							CHILD,
							this.getResources().getString(
									R.string.LayersActivity_12));
					children1.add(curChildMap);

					Map<String, String> curChildMap1 = new HashMap<String, String>();
					curChildMap1.put(
							LAYER,
							this.getResources().getString(
									R.string.LayersActivity_13));
					curChildMap1.put(
							CHILD,
							this.getResources().getString(
									R.string.LayersActivity_14));
					children1.add(curChildMap1);
					childData.add(children1);
				}
			}

			// Set up our adapter
			mAdapter = new SimpleExpandableListAdapter(this, groupData,
					android.R.layout.simple_expandable_list_item_1,
					new String[] { LAYER, CHILD }, new int[] {
							android.R.id.text1, android.R.id.text2 },
					childData, android.R.layout.simple_expandable_list_item_2,
					new String[] { LAYER, CHILD }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			setListAdapter(mAdapter);
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadLayersList: ", e);
		}
	}

	private void loadSampleList() {
		try {
			List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
			List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
			for (int i = 0; i < 20; i++) {
				Map<String, String> curGroupMap = new HashMap<String, String>();
				groupData.add(curGroupMap);
				curGroupMap.put(LAYER, "Group " + i);
				curGroupMap.put(CHILD, (i % 2 == 0) ? "This group is even"
						: "This group is odd");

				List<Map<String, String>> children = new ArrayList<Map<String, String>>();
				for (int j = 0; j < 15; j++) {
					Map<String, String> curChildMap = new HashMap<String, String>();
					children.add(curChildMap);
					curChildMap.put(LAYER, "Child " + j);
					curChildMap.put(CHILD, (j % 2 == 0) ? "This child is even"
							: "This child is odd");
				}
				childData.add(children);
			}

			// Set up our adapter
			mAdapter = new SimpleExpandableListAdapter(this, groupData,
					android.R.layout.simple_expandable_list_item_1,
					new String[] { LAYER, CHILD }, new int[] {
							android.R.id.text1, android.R.id.text2 },
					childData, android.R.layout.simple_expandable_list_item_2,
					new String[] { LAYER, CHILD }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			setListAdapter(mAdapter);
		} catch (Exception e) {
			log.log(Level.SEVERE, "loadSampleList: ", e);
		}
	}

	@Override
	public boolean onChildClick(android.widget.ExpandableListView parent,
			android.view.View v, int groupPosition, int childPosition, long id) {
		boolean result = true;

		try {
			result = super.onChildClick(parent, v, groupPosition,
					childPosition, id);
			log.log(Level.FINE, "onChildClick");
			if (groupPosition == 3) {
				if (childPosition == 0)
					this.showGetCapabilitiesAlert();
				else if (childPosition == 1)
					this.showLoadFileAlert();
			} else {
				if (layers) {
					TwoLineListItem t = (TwoLineListItem) v;
					String s = t.getText1().getText().toString();
					log.log(Level.FINE, "layer clicked: " + s);
					Intent i = new Intent();
					if (getIntent().getBooleanExtra(FROM_FILE_EXPLORER, false)) {
						i = new Intent(this,
								es.prodevelop.gvsig.mini.activities.Map.class);
						i.putExtra("layer", s);
						i.putExtra(GVTILES, gvTiles);
						log.log(Level.FINE,
								"Start activity Map from LayersActivity, layer: "
										+ s);
						startActivity(i);
					} else {
						i.putExtra("layer", s);
						i.putExtra(GVTILES, gvTiles);
						log.log(Level.FINE,
								"Back to Map from LayersActivity, layer: " + s);
						setResult(RESULT_OK, i);
					}
					this.finish();
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "onChildClick: ", e);
		}

		return result;
	}

	private void callGetCapabilities(String server) {
		try {
			gt = new GetCapabilitiesTask(server, handler);
			WorkQueue.getInstance().execute(gt);
			// ThreadPool.getInstance(3).assignFirst(gt);

			dialog2 = ProgressDialog.show(LayersActivity.this, this
					.getResources().getString(R.string.please_wait), this
					.getResources().getString(R.string.LayersActivity_7), true);
			dialog2.setCancelable(true);
			dialog2.setCanceledOnTouchOutside(true);
			dialog2.setIcon(R.drawable.menu02);
			dialog2.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog2) {
					try {
						if (gt != null)
							gt.cancel();
						dialog2.dismiss();
						log.log(Level.FINE, "Get capabilities task canceled");
					} catch (Exception e) {
						log.log(Level.SEVERE, "onCancel progressdialog: ", e);
					}
				}
			});
		} catch (Exception e) {
			log.log(Level.SEVERE, "callGetCapabilities: ", e);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		try {
			super.onActivityResult(requestCode, resultCode, intent);
			log.log(Level.FINE, "onActivityResult to layers activity");
			switch (requestCode) {
			case 0:
				switch (resultCode) {
				case RESULT_OK:
					log.log(Level.FINE, "RESULT_OK");
					String server = intent.getStringExtra("server");
					String name = intent.getStringExtra("name");
					String format = intent.getStringExtra("format");
					String[] layersName = intent.getStringArrayExtra("layers");
					String srs = intent.getStringExtra("srs");
					double minX = intent.getDoubleExtra("minX", 0);
					double minY = intent.getDoubleExtra("minY", 0);
					double maxX = intent.getDoubleExtra("maxX", 0);
					double maxY = intent.getDoubleExtra("maxY", 0);
					String version = intent.getStringExtra("version");

					WMSRenderer w = WMSRenderer.getWMSRenderer(
							new String[] { server }, name, format,
							WMSRenderer.DEFAULT_MAX_ZOOM_LEVEL, 0, 256,
							layersName, new Extent(minX, minY, maxX, maxY),
							srs, version, null);
					Layers.getInstance().addLayer(w.toString());
					Layers.getInstance().persist();

					if (intent.getBooleanExtra("loadLayers", false)) {
						layers = true;
						Hashtable layers = Layers.getInstance()
								.getLayersForView();
						this.loadLayersList(layers);
					}

					break;
				}
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "LayersActivity onActivityResult: ", e);
			// Utils.showSendLogDialog(this, R.string.fatal_error);
		}
	}

	/**
	 * Handler of the GetCapabilities background task, possible responses are
	 * LayersActivity.WMS_CONNECTED, LayersActivity.WMS_ERROR,
	 * LayersActivity.WMS_CANCEL
	 * 
	 * @author aromeu
	 * @author rblanco
	 * 
	 */
	class WMSHandler extends Handler {

		private Context context;

		public WMSHandler(Context context) {
			this.context = context;
		}

		@Override
		public void handleMessage(final Message msg) {
			try {
				final int what = msg.what;
				switch (what) {
				case LayersActivity.WMS_CONNECTED:
					log.log(Level.FINE, "WMS_CONNECTED");
					if (server == null) {
						log.log(Level.WARNING, "server == null");
						return;
					}

					WMSLayerNode w = null;
					try {
						w = FMapWMSDriverFactory.getFMapDriverForURL(
								new URL(server)).getLayersTree();
					} catch (ConnectException e) {
						log.log(Level.SEVERE, "ConnectException: ", e);
					} catch (MalformedURLException e) {
						log.log(Level.SEVERE, "MalformedURLException ", e);
					} catch (IOException e) {
						log.log(Level.SEVERE, "IOException ", e);
					}

					Intent intent = new Intent(context, WMSLayersActivity.class);

					if (w == null) {
						log.log(Level.WARNING, "WMSLayerNode == null");
						return;
					}

					final int size = w.getChildren().size();

					ArrayList names = new ArrayList(size);
					String name = null;
					for (int i = 0; i < size; i++) {
						name = ((WMSLayerNode) w.getChildren().get(i))
								.getName();
						names.add(name);
						log.log(Level.FINE, "name (" + i + ")" + name);
					}
					intent.putExtra("server", server);
					log.log(Level.FINE, "server: " + server);
					intent.putStringArrayListExtra("layers", names);

					((Activity) context).startActivityForResult(intent, 0);
					break;
				case LayersActivity.WMS_ERROR:
					log.log(Level.FINE, "WMS_ERROR");
					Toast.makeText(context, R.string.error_connecting_server,
							2000).show();
					break;
				case LayersActivity.WMS_CANCELED:
					log.log(Level.FINE, "WMS_CANCELED");
					Toast.makeText(context, R.string.task_canceled, 2000)
							.show();
					break;
				}

				dialog2.dismiss();
			} catch (Exception e) {
				log.log(Level.SEVERE, "LayersActivity handleMessage: ", e);
			} finally {
			}
		}
	}
}
