package es.prodevelop.gvsig.mini.helpers;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.anddev.android.weatherforecast.weather.WeatherCurrentCondition;
import org.anddev.android.weatherforecast.weather.WeatherForecastCondition;
import org.anddev.android.weatherforecast.weather.WeatherSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import es.prodevelop.gvsig.mini.R;
import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.activities.MapLocation;
import es.prodevelop.gvsig.mini.activities.SettingsActivity;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedText;
import es.prodevelop.gvsig.mini.activities.NameFinderActivity.BulletedTextListAdapter;
import es.prodevelop.gvsig.mini.util.Utils;
import es.prodevelop.gvsig.mini.utiles.WorkQueue;
import es.prodevelop.gvsig.mini.views.overlay.LongTextAdapter;

public class ShowController {
	
	private Map map;
	private static ShowController instance = null;
	private final static Logger log = Logger.getLogger(Map.class.getName());
	
	private ShowController(Map m){
		map = m;
	}
	
	public static ShowController getInstance(Map m){
		if (instance == null) {
			instance = new ShowController(m);
		}
		return instance;
	}
	
	/**
	 * Show an AlertDialog to the user to input the query string for NameFinder
	 * addresses
	 */
	public void showDownloadTilesDialog() {
		try {
			map.osmap.pauseDraw();
			log.log(Level.FINE, "show address dialog");
			AlertDialog.Builder alert = new AlertDialog.Builder(map);

			final LinearLayout l = (LinearLayout) map.getLayoutInflater()
					.inflate(R.layout.download_tiles, null);
			map.totalMB = (TextView) l
					.findViewById(R.id.download_total_transfer_text);
			map.totalTiles = (TextView) l
					.findViewById(R.id.download_total_tiles_text);
			map.totalZoom = (TextView) l
					.findViewById(R.id.download_zoom_level_text);
			map.downTilesSeekBar = (SeekBar) l
					.findViewById(R.id.download_zoom_level_seekbar);
			map.downTilesSeekBar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

						@Override
						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							try {
								// changed to public
								map.instantiateTileDownloaderTask(l, progress);
							} catch (Exception e) {
								log.log(Level.SEVERE, e.getMessage());
							}
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}
					});
			alert.setIcon(R.drawable.layerdonwload);
			alert.setTitle(R.string.download_tiles_14);
			map.downTilesSeekBar.setProgress(50);
			((CheckBox) l.findViewById(R.id.download_tiles_overwrite_check))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							map.instantiateTileDownloaderTask(l, map.downTilesSeekBar.getProgress());
						}
					});

			alert.setView(l);

			alert.setPositiveButton(R.string.download_tiles_14,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								map.resetCounter();
								// changed to public // changed to public
								map.downloadTileAlert = map.getDownloadTilesDialog(); 
								map.downloadTileAlert.show();
								// changed to public
								WorkQueue.getExclusiveInstance().execute(map.t);
							} catch (Exception e) {
								log.log(Level.SEVERE,
										"clickNameFinderAddress: ", e);
							} finally {
								// setRequestedOrientation(ActivityInfo.
								// SCREEN_ORIENTATION_SENSOR);
							}
							return;
						}
					});

			alert.setNegativeButton(R.string.alert_dialog_text_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// setRequestedOrientation(ActivityInfo.
							// SCREEN_ORIENTATION_SENSOR);
							map.osmap.resumeDraw();
							map.reloadLayerAfterDownload();
						}
					});
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			alert.show();
		} catch (Exception e) {
			map.osmap.resumeDraw();
			log.log(Level.SEVERE, "", e);
		}
	}
	
	/**
	 * Show an AlertDialog to the user to input the query string for NameFinder
	 * addresses
	 */
	public void showSearchDialog() {
		try {
			// this.onSearchRequested();
			log.log(Level.FINE, "show address dialog");
			AlertDialog.Builder alert = new AlertDialog.Builder(map);

			alert.setIcon(R.drawable.menu00);
			alert.setTitle(R.string.Map_3);
			final EditText input = new EditText(map);
			alert.setView(input);

			alert.setPositiveButton(R.string.alert_dialog_text_search,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								Editable value = input.getText();
								// Call to NameFinder with the text
								// changed visibility
								map.searchInNameFinder(value.toString(), false);

							} catch (Exception e) {
								log.log(Level.SEVERE,
										"clickNameFinderAddress: ", e);
							}
							return;
						}
					});

			alert.setNegativeButton(R.string.alert_dialog_text_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
	

	/**
	 * Shows an AlertDialog to the user to input his/her twitter account
	 * credentials
	 * 
	 * @deprecated
	 */
	public void showTweetDialog() {
		try {
			log.log(Level.FINE, "showTweetDialog");
			LayoutInflater factory = LayoutInflater.from(map);
			final View textEntryView = factory.inflate(
					R.layout.alert_dialog_text_entry, null);
			AlertDialog.Builder alertTweet = new AlertDialog.Builder(map);
			alertTweet
					.setView(textEntryView)
					.setIcon(R.drawable.menu04)
					.setTitle(R.string.alert_dialog_text_entry)
					.setPositiveButton(R.string.alert_dialog_tweet,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									try {

										EditText etrUserName = (EditText) textEntryView
												.findViewById(R.id.username_edit);
										String userName = etrUserName.getText()
												.toString();
										EditText etrUserPass = (EditText) textEntryView
												.findViewById(R.id.password_edit);
										String userPass = etrUserPass.getText()
												.toString();

										Map.twituser = userName;
										Map.twitpass = userPass;
										map.getItemContext().getFunctionalityByID(
												R.layout.twitter_image_button)
												.onClick(null);
									} catch (Exception e) {
										log.log(Level.SEVERE, "twitter: ", e);
									}
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
			alertTweet.show();
			//changed visibility
			map.userContext.setUsedTwitter(true);
			map.userContext.setLastExecTwitter();
		} catch (Exception e) {
			log.log(Level.SEVERE, "showTweetDialog: ", e);
		}
	}
	

	/**
	 * Shows an AlertDialog to the user to input the query string for NameFinder
	 * 
	 * @deprecated
	 */
	public void showPOIDialog() {
		try {
			log.log(Level.FINE, "showPOIDialog");
			AlertDialog.Builder alertPOI = new AlertDialog.Builder(map);

			alertPOI.setIcon(R.drawable.poismenu);
			alertPOI.setTitle(R.string.Map_21);

			final EditText inputPOI = new EditText(map);

			alertPOI.setView(inputPOI);

			alertPOI.setPositiveButton(R.string.search,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								Editable value = inputPOI.getText();
								// Call to NameFinder with the text
								map.searchInNameFinder(value.toString(), true);

							} catch (Exception e) {
								log.log(Level.SEVERE, "clickNameFinder: ", e);
							}
							return;
						}
					});

			alertPOI.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alertPOI.show();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
	

	/**
	 * Shows an AlertDialog with the results from WeatherFunctionality
	 * 
	 * @param ws
	 *            The results from WeatherFunctionality
	 */
	public void showWeather(WeatherSet ws) {
		try {
			log.log(Level.FINE, "showWeather");
			if (ws == null) {
				log.log(Level.FINE,
						"ws == null: Can't get weather. Check another location");
				Toast.makeText(map, R.string.Map_8, Toast.LENGTH_LONG)
						.show();
				return;
			}
			if (ws.getWeatherCurrentCondition() == null) {
				if (map.dialog2 != null)
					map.dialog2.dismiss();
				AlertDialog.Builder alertW = new AlertDialog.Builder(map);
				alertW.setCancelable(true);
				alertW.setIcon(R.drawable.menu03);
				alertW.setTitle(R.string.error);
				if (ws.place == null || ws.place.compareTo("") == 0) {
					ws.place = map.getResources().getString(R.string.Map_9);
				}

				log.log(Level.FINE, "The weather in " + ws.place
						+ " is not available");
				alertW.setMessage(String.format(
						map.getResources().getString(R.string.Map_10),
						ws.place));

				alertW.setNegativeButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
				alertW.show();
				return;
			}

			AlertDialog.Builder alertW = new AlertDialog.Builder(map);
			alertW.setCancelable(true);
			alertW.setIcon(R.drawable.menu03);
			alertW.setTitle(map.getResources().getString(R.string.Map_11)
					+ " " + ws.place);

			final ListView lv = new ListView(map);

			BulletedTextListAdapter adapter = new BulletedTextListAdapter(map);

			WeatherCurrentCondition wc = ws.getWeatherCurrentCondition();

			adapter.addItem(new BulletedText(new StringBuffer()
					.append(map.getResources().getString(R.string.Map_12))
					.append(" - ").append(wc.getTempCelcius()).append(" C")
					.append("\n").append(wc.getCondition()).append("\n")
					.append(wc.getWindCondition()).append("\n")
					.append(wc.getHumidity()).toString(), BulletedText
					.getRemoteImage(new URL("http://www.google.com"
							+ wc.getIconURL()))).setSelectable(false));

			ArrayList<WeatherForecastCondition> l = ws
					.getWeatherForecastConditions();

			WeatherForecastCondition temp;
			for (int i = 0; i < l.size(); i++) {
				try {
					temp = l.get(i);
					adapter.addItem(new BulletedText(new StringBuffer()
							.append(temp.getDayofWeek()).append(" - ")
							.append(temp.getTempMinCelsius()).append(" C")
							.append("/").append(temp.getTempMaxCelsius())
							.append(" C").append("\n")
							.append(temp.getCondition()).toString(),
							BulletedText
									.getRemoteImage(new URL(
											"http://www.google.com"
													+ temp.getIconURL())))
							.setSelectable(false));
				} catch (Exception e) {
					log.log(Level.SEVERE, "showWeather: ", e);
				}
			}

			lv.setAdapter(adapter);
			lv.setPadding(10, 0, 10, 0);

			alertW.setView(lv);

			alertW.setNegativeButton(
					map.getResources().getString(R.string.back),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alertW.show();
			if (map.dialog2 != null)
				map.dialog2.dismiss();
		} catch (Exception e) {
			log.log(Level.SEVERE, "showWeather: ", e);
		} finally {
		}
	}


	public void showDownloadDialog() {
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(map);

			// alert.setIcon(R.drawable.menu00);
			alert.setTitle(R.string.download_tiles_01);
			TextView text = new TextView(map);
			text.setText(R.string.download_tiles_02);

			alert.setView(text);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {
								// Utils.downloadLayerFile(MapLocation.this);
								Utils.downloadLayerFile(map);
							} catch (Exception e) {
								log.log(Level.SEVERE, "", e);
							}
						}
					});

			alert.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					});

			alert.show();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}
	

	/**
	 * Shows an AlertDialog to the user to input his/her twitter account
	 * credentials
	 * 
	 */
	public void showTweetDialogSettings() {
		try {
			log.log(Level.FINE, "showTweetDialogSettings");
			LayoutInflater factory = LayoutInflater.from(map);
			TextView t = new TextView(map);
			t.setText(R.string.twitter_go_settings);
			AlertDialog.Builder alertTweet = new AlertDialog.Builder(map);
			alertTweet
					.setView(t)
					.setIcon(R.drawable.menu04)
					.setTitle(R.string.alert_dialog_text_entry)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									try {
										Intent i = new Intent(map,
												SettingsActivity.class);
										i.putExtra("twitter", true);
										map.startActivityForResult(i, Map.CODE_SETTINGS);
									} catch (Exception e) {
										log.log(Level.SEVERE, "twitter: ", e);
									}
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
			alertTweet.show();
			map.userContext.setUsedTwitter(true);
			map.userContext.setLastExecTwitter();
		} catch (Exception e) {
			log.log(Level.SEVERE, "showTweetDialog: ", e);
		}
	}
	
	public void showNavigationModeAlert() {
		try {
			RadioGroup r = new RadioGroup(map);
			RadioButton r1 = new RadioButton(map);
			r1.setText(R.string.portrait);
			r1.setId(0);
			RadioButton r2 = new RadioButton(map);
			r2.setText(R.string.landscape);
			r2.setId(1);
			r.addView(r1);
			r.addView(r2);
			r.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					try {
						map.centerOnGPSLocation();
						map.osmap.setKeepScreenOn(true);
						map.navigation = true;
						map.osmap.onLayerChanged(map.osmap.getMRendererInfo()
								.getFullNAME());
						// final MapRenderer r =
						// Map.this.osmap.getMRendererInfo();
						map.osmap.setZoomLevel(17, true);
						switch (arg1) {
						case 0:
							log.log(Level.FINE, "navifation mode vertical on");
							map.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
							break;
						case 1:
							log.log(Level.FINE, "navifation mode horizontal on");
							map.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						log.log(Level.SEVERE, "onCheckedChanged", e);
					}
				}

			});
			AlertDialog.Builder alertCache = new AlertDialog.Builder(map);
			alertCache
					.setView(r)
					.setIcon(R.drawable.menu_navigation)
					.setTitle(R.string.Map_Navigator)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}

							}).create();
			alertCache.show();
			r1.setChecked(true);
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
	}

	public void showOKDialog(String textBody, int title, boolean editView) {
		try {
			log.log(Level.FINE, "show ok dialog");
			AlertDialog.Builder alert = new AlertDialog.Builder(map);

			if (textBody.length() > 1000) {
				Toast.makeText(map, R.string.Map_25, Toast.LENGTH_LONG).show();
				return;
			}

			if (textBody.contains("<html")) {
				try {
					WebView wv = new WebView(map);
					String html = textBody.substring(textBody.indexOf("<html"),
							textBody.indexOf("html>") + 5);

					wv.loadData(html, "text/html", "UTF-8");
					alert.setView(wv);
				} catch (Exception e) {
					log.log(Level.SEVERE, "", e);
					ListView l = new ListView(map);
					l.setAdapter(new LongTextAdapter(map, textBody, editView));
					l.setClickable(false);
					l.setLongClickable(false);
					l.setFocusable(false);
					alert.setView(l);
				} catch (OutOfMemoryError oe) {
					map.onLowMemory();
					log.log(Level.SEVERE, "", oe);
					map.showToast(R.string.MapLocation_3);
				}

			} else {
				ListView l = new ListView(map);
				l.setAdapter(new LongTextAdapter(map, textBody, editView));
				l.setClickable(false);
				l.setLongClickable(false);
				l.setFocusable(false);
				alert.setView(l);
			}

			alert.setTitle(title);

			alert.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							try {

							} catch (Exception e) {
								log.log(Level.SEVERE, "", e);
							}
						}
					});

			alert.show();
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		} catch (OutOfMemoryError oe) {
			map.onLowMemory();
			log.log(Level.SEVERE, "", oe);
			map.showToast(R.string.MapLocation_3);
		}
	}

}
